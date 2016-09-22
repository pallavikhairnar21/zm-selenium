/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2013, 2014, 2016 Synacor, Inc.
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software Foundation,
 * version 2 of the License.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program.
 * If not, see <https://www.gnu.org/licenses/>.
 * ***** END LICENSE BLOCK *****
 */
package com.zimbra.qa.selenium.projects.ajax.tests.main.attributes;

import org.testng.annotations.Test;
import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.SleepUtil;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAdminAccount;
import com.zimbra.qa.selenium.framework.util.ConfigProperties;
import com.zimbra.qa.selenium.framework.util.staf.StafServicePROCESS;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCommonTest;

public class ZimbraHelpAdvancedURL extends AjaxCommonTest {

	public ZimbraHelpAdvancedURL() {
		logger.info("New " + ZimbraHelpAdvancedURL.class.getCanonicalName());
		super.startingPage = app.zPageMail;
	}

	
	@Bugs(ids = "101023")
	@Test(description = "Verify the product help URL", priority=5, 
		groups = { "functional" })

	public void ZimbraHelpAdvancedURL_01() throws HarnessException {

		StafServicePROCESS staf = new StafServicePROCESS();
		String url = "/zimbra/help/advanced/zimbra_user_help.htm";
		String domainID = null;

		try {

			staf.execute("mkdir -p /helpUrl/help/adv && echo '<html><body><h1>Temp Help</h1><p> This is the advanced help of zimbra </p></body></html>' >/helpUrl/help/adv/help.html");

			// To get domain id
			String targetDomain = ConfigProperties.getStringProperty("testdomain");
			ZimbraAdminAccount.AdminConsoleAdmin().soapSend("<GetDomainRequest xmlns='urn:zimbraAdmin'>"
					+ "<domain by='name'>" + targetDomain + "</domain>" + "</GetDomainRequest>");

			domainID = ZimbraAdminAccount.AdminConsoleAdmin()
					.soapSelectValue("//admin:GetDomainResponse/admin:domain", "id").toString();

			// Modify the domain and change the help URL
			ZimbraAdminAccount.AdminConsoleAdmin()
					.soapSend("<ModifyDomainRequest xmlns='urn:zimbraAdmin'>" + "<id>" + domainID + "</id>"
							+ "<a n='zimbraHelpAdvancedURL'>/helpUrl/help/adv/help.html</a>"
							+ "<a n='zimbraVirtualHostname'>" + ConfigProperties.getStringProperty("server.host")
							+ "</a>" + "</ModifyDomainRequest>");

			app.zPageMain.zToolbarPressPulldown(Button.B_ACCOUNT, Button.O_PRODUCT_HELP);
			SleepUtil.sleepVeryLong();

			// Check the URL
			ZAssert.assertTrue(app.zPageMain.sGetWindowURL("ZWC Help").contains("/helpUrl/help/adv/help.html"),	"Product Help URL is not as set in zimbraHelpAdvancedURL");

		} finally {

			// Revert the changes done in attribute 'zimbraHelpAdminURL'
			ZimbraAdminAccount.AdminConsoleAdmin()
					.soapSend("<ModifyDomainRequest xmlns='urn:zimbraAdmin'>" + "<id>" + domainID + "</id>"
							+ "<a n='zimbraHelpAdminURL'>" + url + "</a>" + "<a n='zimbraVirtualHostname'>" + ""
							+ "</a>" + "</ModifyDomainRequest>");

			// Restart zimbra services
			staf.execute("zmmailboxdctl restart");

			SleepUtil.sleepVeryLong();
			for (int i = 0; i <= 10; i++) {
				app.zPageLogin.sRefresh();
				if (app.zPageLogin.sIsElementPresent("css=input[class^='ZLoginButton']") == true || 
						app.zPageLogin.sIsElementPresent("css=div[id$='parent-ZIMLET'] td[id$='ZIMLET_textCell']") == true) {
					break;
				} else {
					SleepUtil.sleepLong();
					if (i == 5) {
						staf.execute("zmmailboxdctl restart");
						SleepUtil.sleepVeryLong();
					}
					continue;
				}
			}

		}

	}
}