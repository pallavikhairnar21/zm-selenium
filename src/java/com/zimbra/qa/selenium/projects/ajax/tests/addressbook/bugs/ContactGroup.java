/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2012, 2013 Zimbra Software, LLC.
 * 
 * The contents of this file are subject to the Zimbra Public License
 * Version 1.4 ("License"); you may not use this file except in
 * compliance with the License.  You may obtain a copy of the License at
 * http://www.zimbra.com/license.
 * 
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied.
 * ***** END LICENSE BLOCK *****
 */
package com.zimbra.qa.selenium.projects.ajax.tests.addressbook.bugs;



import java.util.*;

import org.testng.annotations.Test;

import com.zimbra.common.soap.Element;
import com.zimbra.qa.selenium.framework.items.*;
import com.zimbra.qa.selenium.framework.items.FolderItem.SystemFolder;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.projects.ajax.core.AjaxCommonTest;
import com.zimbra.qa.selenium.projects.ajax.ui.addressbook.*;
import com.zimbra.qa.selenium.projects.ajax.ui.addressbook.FormContactGroupNew.Field;


public class ContactGroup extends AjaxCommonTest  {

	public ContactGroup() {
		logger.info("New "+ ContactGroup.class.getCanonicalName());
		
		// All tests start at the Address page
		super.startingPage = app.zPageAddressbook;

		// Make sure we are using an account with conversation view
		super.startingAccountPreferences = null;		
		
	}
		
	
	/*
	 * http://bugzilla.zimbra.com/show_bug.cgi?id=60652#c0
	 * 
	 * 1)Login to ZWC
	 * 2)Create a new contact group, make sure you have some contacts created initially.
	 * 3)In the Right Panel observe the entries from GAL are self populated but when 
	 *   you select contacts from the "in:" drop down the contacts are not populated.
	 *   It shows "No results found" message.
	 */
	@Test(	description = "Contacts are not populated while creating a new contact group",
			groups = { "functional" })
	public void Bug60652_ContactsGetPopulated() throws HarnessException {
		
		//-- Data
		
		String groupname = "group" + ZimbraSeleniumProperties.getUniqueString();
		
		// Create a contact
		ContactItem contact = ContactItem.createContactItem(app.zGetActiveAccount());
		
		
		//-- GUI
		
		// Refresh
		app.zPageAddressbook.zRefresh();
		
		// open contact group form
		FormContactGroupNew formGroup = (FormContactGroupNew)app.zPageAddressbook.zToolbarPressPulldown(Button.B_NEW, Button.O_NEW_CONTACTGROUP);
        
		// Set the group name
		formGroup.zFillField(Field.GroupName, groupname);
			
		// select contacts option
		formGroup.zToolbarPressPulldown(Button.B_CONTACTGROUP_SEARCH_TYPE, Button.O_CONTACTGROUP_SEARCH_CONTACTS);
		
		// Get the displayed list
		ArrayList<ContactItem> ciArray = formGroup.zListGetSearchResults();
		
		boolean found=false;
		for (ContactItem ci: ciArray) {
			if ( ci.getName().equals(contact.getName()) ) {
				found = true;
				break;
			}
		}
        
		ZAssert.assertTrue(found, "Verify contact " + contact.getName() + " populated");

		// Try to close out the window
		//formGroup.zToolbarPressButton(Button.B_CONTACTGROUP_ADD_ADDRESS);
		
		//group137756059578165formGroup.zToolbarPressButton(Button.B_SAVE);
		

		
	}

	@Test(	description = "Click Delete Toolbar button in Edit Contact Group form",
			groups = { "functional" })
	public void Bug62026_ClickDeleteToolbarButtonInEditContactGroupForm() throws HarnessException {
		
		//-- Data
		
		// The trash folder
		FolderItem trash = FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Trash);
		
		// Create a contact group
		ContactGroupItem group = ContactGroupItem.createContactGroupItem(app.zGetActiveAccount());
		
		
		
		//-- GUI
		
		// Refresh
		app.zPageAddressbook.zRefresh();
		
		// Edit the group
		FormContactGroupNew form = (FormContactGroupNew)app.zPageAddressbook.zListItem(Action.A_RIGHTCLICK, Button.B_EDIT, group.getName());
		
		// In the form, click "Delete"
		form.zToolbarPressButton(Button.B_DELETE);
		
		

		//-- Verification
		
		// Verify the group is in the trash
		ContactGroupItem actual = ContactGroupItem.importFromSOAP(app.zGetActiveAccount(), "is:anywhere "+ group.getName());
		ZAssert.assertNotNull(actual, "Verify the group stil exists");
		
		ZAssert.assertEquals(actual.getFolderId(), trash.getId(), "Verify the group is located in trash");


   	}
	
	//Due to the bug 77968 the New Contact Group Context menu is disabled for GAL search result
	@Test( description="create a new contact group from GAL search result",
		   groups= { "depricated"  } )
	public void Bug66623_AddingAGALToAContactGroup() throws HarnessException{
		String email=ZimbraAccount.AccountB().EmailAddress.substring(0,ZimbraAccount.AccountB().EmailAddress.indexOf('@'));
		
		// search for a GAL
		app.zPageSearch.zToolbarPressPulldown(Button.B_SEARCHTYPE, Button.O_SEARCHTYPE_GAL); 		
		app.zPageSearch.zAddSearchQuery(email);	
		app.zPageSearch.zToolbarPressButton(Button.B_SEARCH);		
					
	 
		//Right click and select New Contact Group
	 	 SimpleFormContactGroupNew simpleFormGroup = (SimpleFormContactGroupNew) app.zPageAddressbook.zListItem(Action.A_RIGHTCLICK, Button.B_CONTACTGROUP, Button.O_NEW_CONTACTGROUP , email);     
	
	 	//Create a contact item
	 	ContactItem contactItem = new ContactItem(email);
	 	contactItem.email = ZimbraAccount.AccountB().EmailAddress;
	 	
	 	//Create contact group 
		 ContactGroupItem newGroup = new ContactGroupItem("group_" + ZimbraSeleniumProperties.getUniqueString().substring(8));
		 
		 //Add the member to the group	 
		 newGroup.addDListMember(contactItem);
	
		 
	 	//fill in group name 
		simpleFormGroup.zFill(newGroup);
		   
		//click Save
		simpleFormGroup.zSubmit(); 
		
		//verify toasted message 'group created'  
        String expectedMsg ="Group Created";
        ZAssert.assertStringContains(app.zPageMain.zGetToaster().zGetToastMessage(),
        		        expectedMsg , "Verify toast message '" + expectedMsg + "'");
    
	    
        

        //click "Contacts" folder
		FolderItem folder= FolderItem.importFromSOAP(app.zGetActiveAccount(), SystemFolder.Contacts);
	    app.zTreeContacts.zTreeItem(Action.A_LEFTCLICK, folder);
		
	    //verify group name is displayed		        
			List<ContactItem> contacts = app.zPageAddressbook.zListGetContacts();
			boolean isFileAsEqual=false;
			for (ContactItem ci : contacts) {
				if (ci.fileAs.equals(ci.fileAs)) {
		            isFileAsEqual = true;	
					break;
				}
			}
		ZAssert.assertTrue(isFileAsEqual, "Verify group name (" + newGroup.fileAs + ") displayed");

	    //verify the location is System folder "Contacts"
		ZAssert.assertEquals(app.zPageAddressbook.sGetText("css=td.companyFolder"), SystemFolder.Contacts.getName(), "Verify location (folder) is " + SystemFolder.Contacts.getName());
		
	}

	//Due to the bug 77968 GAL search result can be added from Context Menu to the existing Contact Group, but not to the New Contact Group
	@Test( description="Add GAL search result from Context Menu to the existing Contact Group",
			   groups= { "functional"  } )
		public void Bug77968_AddGALSearchToExistingContactGroup() throws HarnessException{
		//-- Data
		ZimbraAccount account = app.zGetActiveAccount(); 
		ZimbraAccount accountB = ZimbraAccount.AccountB(); 
		String galName = accountB.EmailAddress.substring(0,accountB.EmailAddress.indexOf('@'));
		String groupName = "group" + ZimbraSeleniumProperties.getUniqueString();
		String member1 = "m" + ZimbraSeleniumProperties.getUniqueString() + "@example.com";
		String member2 = "m" + ZimbraSeleniumProperties.getUniqueString() + "@example.com";
				
		//-- GUI
		// Refresh the addressbook
		app.zPageAddressbook.zRefresh();
				
		// open New Contact group form
		FormContactGroupNew form = (FormContactGroupNew)app.zPageAddressbook.zToolbarPressPulldown(Button.B_NEW, Button.O_NEW_CONTACTGROUP);
		    
		// fill in group name and email addresses
		//form.zFillField(Field.GroupName, groupName);
		String locator = "css=div[class*=ZmContactView][style*='100px'] input[id*=_groupName]";
		form.sType(locator,"");
		form.sType(locator,groupName);
		form.zFillField(Field.FreeFormAddress, member1);
		form.zFillField(Field.FreeFormAddress, member2);
		form.zSubmit();
		SleepUtil.sleepSmall();
		ContactGroupItem groupItem = ContactGroupItem.importFromSOAP(account, groupName);
		// search for a GAL
		app.zPageSearch.zToolbarPressPulldown(Button.B_SEARCHTYPE, Button.O_SEARCHTYPE_GAL); 		
		app.zPageSearch.zAddSearchQuery(galName);	
		app.zPageSearch.zToolbarPressButton(Button.B_SEARCH);		
		//due to the bug 77968 the New Contact Group Context menu is disabled and the GAL search result can be added only to the exiting contact group
		app.zPageAddressbook.zListItem(Action.A_RIGHTCLICK, Button.B_CONTACTGROUP, groupItem, galName);
		// Verify the contact group contains the GAL member
		ContactGroupItem.importFromSOAP(account, groupName);
		boolean found = false;
		Element[] members = account.soapSelectNodes("//mail:cn//mail:m");
		for (Element e : members) {
			String type = e.getAttribute("type", "notset");
			if ( type.equals("G") ){
				String value = e.getAttribute("value", "notset");			
				if ( value.contains(galName) ) {
					found = true;
					break;
				}
			}
		}
		ZAssert.assertTrue(found, "Verify the contact group conatins the GAL member");
	}
}
