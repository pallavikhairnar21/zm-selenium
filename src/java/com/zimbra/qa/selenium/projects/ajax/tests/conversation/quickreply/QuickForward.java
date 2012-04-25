package com.zimbra.qa.selenium.projects.ajax.tests.conversation.quickreply;

import org.testng.annotations.Test;

import com.zimbra.qa.selenium.framework.items.MailItem;
import com.zimbra.qa.selenium.framework.ui.Action;
import com.zimbra.qa.selenium.framework.ui.Button;
import com.zimbra.qa.selenium.framework.util.HarnessException;
import com.zimbra.qa.selenium.framework.util.ZAssert;
import com.zimbra.qa.selenium.framework.util.ZimbraAccount;
import com.zimbra.qa.selenium.framework.util.ZimbraSeleniumProperties;
import com.zimbra.qa.selenium.projects.ajax.core.PrefGroupMailByConversationTest;
import com.zimbra.qa.selenium.projects.ajax.ui.mail.*;


public class QuickForward extends PrefGroupMailByConversationTest {

	public QuickForward() {
		logger.info("New "+ QuickForward.class.getCanonicalName());

	}
	
	@Test(	description = "Quick Reply (Forward) a conversation (1 message, 1 recipient)",
			groups = { "smoke" })
	public void QuickForward_01() throws HarnessException {
		
		ZimbraAccount destination = new ZimbraAccount();
		destination.provision();
		destination.authenticate();
		
		ZimbraAccount account1 = new ZimbraAccount();
		account1.provision();
		account1.authenticate();
		

		// Create the message data to be sent
		String subject = "subject" + ZimbraSeleniumProperties.getUniqueString();
		String content = "content" + ZimbraSeleniumProperties.getUniqueString();
		String forward = "quickforward" + ZimbraSeleniumProperties.getUniqueString();
		
		account1.soapSend(
					"<SendMsgRequest xmlns='urn:zimbraMail'>" +
						"<m>" +
							"<e t='t' a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
							"<su>"+ subject +"</su>" +
							"<mp ct='text/plain'>" +
								"<content>"+ content +"</content>" +
							"</mp>" +
						"</m>" +
					"</SendMsgRequest>");

		// Click Get Mail button
		app.zPageMail.zToolbarPressButton(Button.B_GETMAIL);
		
		// Select the item
		DisplayConversation display = (DisplayConversation)app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);
		
		// Quick Reply
		FormMailNew form = (FormMailNew)display.zPressButton(Button.B_QUICK_REPLY_FORWARD);
		form.zFillField(FormMailNew.Field.To, destination.EmailAddress);
		form.zFillField(FormMailNew.Field.Body, forward);
		form.zToolbarPressButton(Button.B_SEND);
	

		
		MailItem mailItem;

		// Verify message is received by the destination
		mailItem = MailItem.importFromSOAP(destination, "subject:("+ subject +") from:("+ app.zGetActiveAccount().EmailAddress +")");
		ZAssert.assertNotNull(mailItem, "Verify the message is in the sent folder");

		// Verify message is not received by the sender
		mailItem = MailItem.importFromSOAP(account1, "subject:("+ subject +") from:("+ app.zGetActiveAccount().EmailAddress +")");
		ZAssert.assertNull(mailItem, "Verify the message is received by the original sender");

	}

	@Test(	description = "Quick Reply (forward) a conversation (1 message, 2 recipients)",
			groups = { "functional" })
	public void QuickForward_02() throws HarnessException {
		
		ZimbraAccount destination1 = new ZimbraAccount();
		destination1.provision();
		destination1.authenticate();
		
		ZimbraAccount destination2 = new ZimbraAccount();
		destination2.provision();
		destination2.authenticate();
		
		ZimbraAccount account1 = new ZimbraAccount();
		account1.provision();
		account1.authenticate();
		
		ZimbraAccount account2 = new ZimbraAccount();
		account2.provision();
		account2.authenticate();


		
		// Create the message data to be sent
		String subject = "subject" + ZimbraSeleniumProperties.getUniqueString();
		String content = "content" + ZimbraSeleniumProperties.getUniqueString();
		String forward = "quickforward" + ZimbraSeleniumProperties.getUniqueString();
		
		account1.soapSend(
					"<SendMsgRequest xmlns='urn:zimbraMail'>" +
						"<m>" +
							"<e t='t' a='"+ account2.EmailAddress +"'/>" +
							"<e t='t' a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
							"<su>"+ subject +"</su>" +
							"<mp ct='text/plain'>" +
								"<content>"+ content +"</content>" +
							"</mp>" +
						"</m>" +
					"</SendMsgRequest>");

		// Click Get Mail button
		app.zPageMail.zToolbarPressButton(Button.B_GETMAIL);
		
		// Select the item
		DisplayConversation display = (DisplayConversation)app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);
		
		// Quick Reply
		FormMailNew form = (FormMailNew)display.zPressButton(Button.B_QUICK_REPLY_FORWARD);
		form.zFillField(FormMailNew.Field.To, destination1.EmailAddress + ";" + destination2.EmailAddress);
		form.zFillField(FormMailNew.Field.Body, forward);
		form.zToolbarPressButton(Button.B_SEND);
	

		MailItem mailItem;

		// Verify message is received by the destination
		mailItem = MailItem.importFromSOAP(destination1, "subject:("+ subject +") from:("+ app.zGetActiveAccount().EmailAddress +")");
		ZAssert.assertNotNull(mailItem, "Verify the message is in the sent folder");

		mailItem = MailItem.importFromSOAP(destination2, "subject:("+ subject +") from:("+ app.zGetActiveAccount().EmailAddress +")");
		ZAssert.assertNotNull(mailItem, "Verify the message is in the sent folder");

		// Verify message is not received by the sender
		mailItem = MailItem.importFromSOAP(account1, "subject:("+ subject +") from:("+ app.zGetActiveAccount().EmailAddress +")");
		ZAssert.assertNull(mailItem, "Verify the message is received by the original sender");

		mailItem = MailItem.importFromSOAP(account2, "subject:("+ subject +") from:("+ app.zGetActiveAccount().EmailAddress +")");
		ZAssert.assertNull(mailItem, "Verify the message is received by the original sender");

	}

	@Test(	description = "Quick Reply (forward) a conversation (1 message, 1 recipient, 1 CC, 1 BCC)",
			groups = { "functional" })
	public void QuickForward_03() throws HarnessException {
		
		ZimbraAccount destination = new ZimbraAccount();
		destination.provision();
		destination.authenticate();
		
		ZimbraAccount account1 = new ZimbraAccount();
		account1.provision();
		account1.authenticate();
		
		ZimbraAccount account2 = new ZimbraAccount();
		account2.provision();
		account2.authenticate();

		ZimbraAccount account3 = new ZimbraAccount();
		account3.provision();
		account3.authenticate();

		ZimbraAccount account4 = new ZimbraAccount();
		account4.provision();
		account4.authenticate();

		
		// Create the message data to be sent
		String subject = "subject" + ZimbraSeleniumProperties.getUniqueString();
		String content = "content" + ZimbraSeleniumProperties.getUniqueString();
		String forward = "quickforward" + ZimbraSeleniumProperties.getUniqueString();
		
		account1.soapSend(
					"<SendMsgRequest xmlns='urn:zimbraMail'>" +
						"<m>" +
							"<e t='t' a='"+ account2.EmailAddress +"'/>" +
							"<e t='c' a='"+ account3.EmailAddress +"'/>" +
							"<e t='b' a='"+ account4.EmailAddress +"'/>" +
							"<e t='b' a='"+ app.zGetActiveAccount().EmailAddress +"'/>" +
							"<su>"+ subject +"</su>" +
							"<mp ct='text/plain'>" +
								"<content>"+ content +"</content>" +
							"</mp>" +
						"</m>" +
					"</SendMsgRequest>");

		// Click Get Mail button
		app.zPageMail.zToolbarPressButton(Button.B_GETMAIL);
		
		// Select the item
		DisplayConversation display = (DisplayConversation)app.zPageMail.zListItem(Action.A_LEFTCLICK, subject);
		
		// Quick Reply
		FormMailNew form = (FormMailNew)display.zPressButton(Button.B_QUICK_REPLY_FORWARD);
		form.zFillField(FormMailNew.Field.To, destination.EmailAddress);
		form.zFillField(FormMailNew.Field.Body, forward);
		form.zToolbarPressButton(Button.B_SEND);
	

		MailItem mailItem;

		// Verify message is received by the destination
		mailItem = MailItem.importFromSOAP(destination, "subject:("+ subject +") from:("+ app.zGetActiveAccount().EmailAddress +")");
		ZAssert.assertNotNull(mailItem, "Verify the message is in the sent folder");


		// Verify message is not received by the sender
		mailItem = MailItem.importFromSOAP(account1, "subject:("+ subject +") from:("+ app.zGetActiveAccount().EmailAddress +")");
		ZAssert.assertNull(mailItem, "Verify the message is received by the original sender");

		mailItem = MailItem.importFromSOAP(account2, "subject:("+ subject +") from:("+ app.zGetActiveAccount().EmailAddress +")");
		ZAssert.assertNull(mailItem, "Verify the message is received by the original sender");

		mailItem = MailItem.importFromSOAP(account3, "subject:("+ subject +") from:("+ app.zGetActiveAccount().EmailAddress +")");
		ZAssert.assertNull(mailItem, "Verify the message is received by the original sender");

		mailItem = MailItem.importFromSOAP(account4, "subject:("+ subject +") from:("+ app.zGetActiveAccount().EmailAddress +")");
		ZAssert.assertNull(mailItem, "Verify the message is received by the original sender");


	}

}
