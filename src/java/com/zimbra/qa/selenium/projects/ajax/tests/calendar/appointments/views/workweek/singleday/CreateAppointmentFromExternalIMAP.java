/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2012, 2013, 2014 Zimbra, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.tests.calendar.appointments.views.workweek.singleday;

import java.util.Calendar;

import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.core.Bugs;
import com.zimbra.qa.selenium.framework.items.AppointmentItem;
import com.zimbra.qa.selenium.framework.items.FolderItem;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.CalendarWorkWeekTest;
import com.zimbra.qa.selenium.projects.ajax.ui.calendar.FormApptNew;
import com.zimbra.qa.selenium.projects.ajax.ui.calendar.FormApptNew.Field;

public class CreateAppointmentFromExternalIMAP extends CalendarWorkWeekTest {

	public CreateAppointmentFromExternalIMAP() {
		logger.info("New "+ CreateAppointmentFromExternalIMAP.class.getCanonicalName());
		super.startingPage = app.zPageCalendar;
	}
	
	@Bugs(ids = "104525,50096")	
	@Test(	description = "Appt. invite received from primary account though external account selected while creating appointment",
			groups = { "smoke" })
	
	public void CreateAppointmentFromExternalIMAP_01() throws HarnessException {
		
		// Create the external data source on the same server
		ZimbraAccount external = new ZimbraAccount();
		external.provision();
		external.authenticate();
		
		// Create the folder to put the data source
		String foldername = "external" + ZimbraSeleniumProperties.getUniqueString();
		
		app.zGetActiveAccount().soapSend(
				"<CreateFolderRequest xmlns='urn:zimbraMail'>" +
                	"<folder name='"+ foldername +"' l='1'/>" +
                "</CreateFolderRequest>");

		FolderItem folder = FolderItem.importFromSOAP(app.zGetActiveAccount(), foldername);
		ZAssert.assertNotNull(folder, "Verify the subfolder is available");
		
		// Create the data source
		String datasourcename = "datasource" + ZimbraSeleniumProperties.getUniqueString();
		String datasourceHost = ZimbraSeleniumProperties.getStringProperty("server.host");
		String datasourceImapPort = ZimbraSeleniumProperties.getStringProperty("server.imap.port");
		String datasourceImapType = ZimbraSeleniumProperties.getStringProperty("server.imap.type");
		
		app.zGetActiveAccount().soapSend(
				"<CreateDataSourceRequest xmlns='urn:zimbraMail'>"
			+		"<imap name='"+ datasourcename +"' l='"+ folder.getId() +"' isEnabled='true' "
			+			"port='"+ datasourceImapPort +"' host='"+ datasourceHost +"' connectionType='"+ datasourceImapType +"' leaveOnServer='true' "
			+			"username='"+ external.EmailAddress +"' password='"+ external.Password +"' />"
			+	"</CreateDataSourceRequest>");

		// Need to logout/login to get the new folder
		app.zPageLogin.zNavigateTo();
		startingPage.zNavigateTo();
		
		// Create appointment data
		AppointmentItem appt = new AppointmentItem();
		
		String apptSubject, apptAttendee1, apptContent;
		Calendar now = this.calendarWeekDayUTC;
		apptSubject = ZimbraSeleniumProperties.getUniqueString();
		apptAttendee1 = ZimbraAccount.AccountA().EmailAddress;
		apptContent = ZimbraSeleniumProperties.getUniqueString();
		
		appt.setSubject(apptSubject);
		appt.setAttendees(apptAttendee1);
		appt.setStartTime(new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 12, 0, 0));
		appt.setEndTime(new ZDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), 14, 0, 0));
		appt.setContent(apptContent);
	
		// Compose appointment and send it to invitee
		FormApptNew apptForm = (FormApptNew) app.zPageCalendar.zToolbarPressButton(Button.B_NEW);
		apptForm.zFill(appt);
		apptForm.zFillField(Field.From, external.EmailAddress);
		apptForm.zSubmit();
		app.zPageCalendar.zToolbarPressButton(Button.B_REFRESH);
		// Verify appointment exists in current view
        ZAssert.assertTrue(app.zPageCalendar.zVerifyAppointmentExists(apptSubject), "Appointment not displayed in current view");

        //TODO add soap verification once 104525 is fixed
	}

}