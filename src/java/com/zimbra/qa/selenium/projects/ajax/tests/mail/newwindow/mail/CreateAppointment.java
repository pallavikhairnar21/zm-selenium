package com.zimbra.qa.selenium.projects.ajax.tests.mail.newwindow.mail;


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


import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.PrefGroupMailByMessageTest;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.DialogAddAttendees;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.SeparateWindowDisplayMail;


public class CreateAppointment extends PrefGroupMailByMessageTest {

	public int delaySeconds = 10;

	public CreateAppointment() {
		logger.info("New "+ CreateAppointment.class.getCanonicalName());

		super.startingAccountPreferences.put("zimbraPrefMarkMsgRead", "" + delaySeconds);


	}

	@Test(	description = "Create Appointment from new window by action menu -> Create Appointment",
			groups = { "functional" })
	public void CreateAppointment_01() throws HarnessException {

		// Create the message data to be sent
		String subject = "subject"+ ZimbraSeleniumProperties.getUniqueString();

		ZimbraAccount.AccountA().soapSend(
				"<SendMsgRequest xmlns='urn:zimbraMail'>" +
						"<m>" +
						"<e t='t' a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
						"<su>"+ subject +"</su>" +
						"<mp ct='text/plain'>" +
						"<content>content"+ ZimbraSeleniumProperties.getUniqueString() +"</content>" +
						"</mp>" +
						"</m>" +
				"</SendMsgRequest>");


		// Click Get Mail button
		app.zPageMail.zToolbarPressButton(Button.B_GETMAIL);

		// Select the item
		app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);


		SeparateWindowDisplayMail window = null;

		try {

			// Choose Actions -> Launch in Window
			window = (SeparateWindowDisplayMail)app.zPageMail.zToolbarPressPulldown(Button.B_ACTIONS, Button.B_LAUNCH_IN_SEPARATE_WINDOW);

			window.zSetWindowTitle(subject);
			window.zWaitForActive();		// Make sure the window is there

			ZAssert.assertTrue(window.zIsActive(), "Verify the window is active");

			window.zToolbarPressPulldown(Button.B_ACTIONS, Button.O_CREATE_APPOINTMENT);
			SleepUtil.sleepMedium();
			app.zPageMail.zClickAt(DialogAddAttendees.Locators.zYesButton,"");
			SleepUtil.sleepLong();
			//window.zToolbarPressButton(Button.B_SAVE);
			app.zPageMail.zClickAt("css=div[id^='ztb__APPT-'] td[id$='_SEND_INVITE_title']", "");

		} finally {

			// Make sure to close the window
			if ( window != null ) {
				window.zCloseWindow();
				window = null;
			}

		}
		SleepUtil.sleepMedium();
		
		// Verify appointment exists on the server
		app.zGetActiveAccount().soapSend(
				"<SearchRequest xmlns='urn:zimbraMail' types='appointment'>"
						+		"<query>subject:("+ subject +")</query>"
						+	"</SearchRequest>");
		String id = app.zGetActiveAccount().soapSelectValue("//mail:appt", "invId");
		ZAssert.assertNotNull(id, "Verify meeting invite is not null");


	}



}