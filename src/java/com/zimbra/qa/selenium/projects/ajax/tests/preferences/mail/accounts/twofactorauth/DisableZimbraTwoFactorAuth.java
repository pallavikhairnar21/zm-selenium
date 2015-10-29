/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2012, 2013, 2014 Zimbra, Inc.
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software Foundation,
 * version 2 of the License.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program.
 * If not, see <http://www.gnu.org/licenses/>.
 * ***** END LICENSE BLOCK *****
 */
package com.zimbra.qa.selenium.projects.ajax.tests.preferences.mail.accounts.twofactorauth;

import java.util.HashMap;

import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCommonTest;
import com.zimbra.qa.selenium.projects.ajax.ui.preferences.TreePreferences.TreeItem;
import com.zimbra.qa.selenium.projects.ajax.ui.preferences.PagePreferences.Locators;
import com.zimbra.qa.selenium.projects.ajax.ui.DialogWarning;
public class DisableZimbraTwoFactorAuth extends AjaxCommonTest {

	public DisableZimbraTwoFactorAuth() {
		
		super.startingAccountPreferences = new HashMap<String, String>() {
			private static final long serialVersionUID = 2485388299568483622L;
			{				
		 		put("zimbraFeatureTwoFactorAuthAvailable", "TRUE");
			}
		};

	}

	@Test(	description = "Disable two factor auth from preferences",
			groups = { "sanity" })
	public void DisableZimbraTwoFactorAuth_01() throws HarnessException {
		String totp, secret, tempToken;
		
		ZimbraAccount.AccountZWC().soapSend(
        		"<EnableTwoFactorAuthRequest xmlns='urn:zimbraAccount'>" +
        		"<name>" + ZimbraAccount.AccountZWC().EmailAddress + "</name>" +
        		"<password>" + ZimbraAccount.AccountZWC().Password + "</password>" +
        		"</EnableTwoFactorAuthRequest>");
        secret = ZimbraAccount.AccountZWC().soapSelectValue("//acct:EnableTwoFactorAuthResponse", "secret");
        tempToken = ZimbraAccount.AccountZWC().soapSelectValue("//acct:EnableTwoFactorAuthResponse", "authToken");

		totp = CommandLine.cmdExecOnServer(ZimbraAccount.AccountZWC().EmailAddress, secret);
		ZimbraAccount.AccountZWC().soapSend(
        		"<EnableTwoFactorAuthRequest xmlns='urn:zimbraAccount'>" +
        		"<name>" + ZimbraAccount.AccountZWC().EmailAddress + "</name>" +
                "<authToken verifyAccount='0'>" + tempToken + "</authToken>" +
                "<twoFactorCode>" + totp + "</twoFactorCode>" +
        		"</EnableTwoFactorAuthRequest>");
		// Login
		totp = CommandLine.cmdExecOnServer(ZimbraAccount.AccountZWC().EmailAddress, secret);
		app.zPageLogin.zLogin(ZimbraAccount.AccountZWC(), totp, false);
		
		// Verify main page becomes active
		ZAssert.assertTrue(app.zPageMain.zIsActive(), "Verify that the account is logged in");

		app.zPagePreferences.zNavigateTo();
		app.zTreePreferences.zTreeItem(Action.A_LEFTCLICK, TreeItem.MailAccounts);
		app.zPagePreferences.sClick(Locators.zDisable2FALink);
		DialogWarning dialog = (DialogWarning) new DialogWarning(DialogWarning.DialogWarningID.DisableTwoStepAuthentication, app, app.zPagePreferences);
		dialog.zClickButton(Button.B_YES);
		
		//Verification
        ZAssert.assertTrue(app.zPagePreferences.zVerifySetup2FALink(), "Verify set-up two factor auth link is present");

	}

}
