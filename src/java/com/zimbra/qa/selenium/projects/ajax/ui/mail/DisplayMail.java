/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2012, 2013, 2014, 2016 Synacor, Inc.
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
package com.zimbra.qa.selenium.projects.ajax.ui.mail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import com.zimbra.qa.selenium.framework.items.AttachmentItem;
import com.zimbra.qa.selenium.framework.ui.*;
import com.zimbra.qa.selenium.framework.util.*;
import com.zimbra.qa.selenium.framework.util.staf.Stafpostqueue;
import com.zimbra.qa.selenium.projects.ajax.ui.*;
import com.zimbra.qa.selenium.projects.ajax.ui.calendar.FormApptNew;

/**
 * The <code>DisplayMail<code> object defines a read-only view of a message in
 * the Zimbra Ajax client.
 * <p>
 * This class can be used to extract data from the message, such as To, From,
 * Subject, Received Date, message body. Additionally, it can be used to click
 * on certain links in the message body, such as "view entire message" and
 * "highlight objects".
 * <p>
 * Hover over objects, such as email or URL hover over, are encapsulated.
 * <p>
 * 
 * @author zimbra
 * @see http://wiki.zimbra.com/wiki/Testing:_Selenium:_ZimbraSelenium_Overview#Mail_Page
 */
public class DisplayMail extends AbsDisplay {

	WebElement we = null;

	public static class Locators {

		public static final String MessageViewPreviewAtBottomCSS = "css=div[id='zv__TV-main__MSG']";
		public static final String MessageViewPreviewAtRightCSS = "css=div[id='zv__TV-main__MSG']";
		public static final String MessageViewPreviewOff="css=td[id='off_check'] div[class='ImgMenuRadio']";
		// 4/26/2012, the message ID is now used in the locator
		// public static final String MessageViewOpenMessageCSS =
		// "css=div[id='zv__MSG-1__MSG']";
		public static final String MessageViewOpenMessageCSS = "css=div[id^='zv__MSG-'][id$='__MSG']";

		public static final String ConversationViewPreviewAtBottomCSS = "css=div[id='zv__CLV-main__CV']";
		public static final String ConversationViewPreviewAtRightCSS = "css=div[id='zv__CLV-main__CV']";
		public static final String ConversationViewOpenMessageCSS = "css=TODO#TODO";
		// public static final String ConversationViewPreviewAtBottomCSS =
		// "css=div[id='zv__CLV__MSG']";
		// public static final String ConversationViewPreviewAtRightCSS =
		// "css=div[id='zv__CLV__MSG']";

		// Accept, Decline & Tentative button, menus and dropdown locators
		public static final String CalendarDropdown = "css=td[id$='_calendarSelectToolbarCell'] td[id$='_select_container']";

		public static final String AcceptButton = "css=td[id$='__Inv__REPLY_ACCEPT_title']";
		public static final String AcceptDropdown = "css=td[id$='__Inv__REPLY_ACCEPT_dropdown']>div";
		public static final String AcceptNotifyOrganizerMenu = "id=REPLY_ACCEPT_NOTIFY_title";
		public static final String AcceptEditReplyMenu = "id=INVITE_REPLY_ACCEPT_title";
		public static final String AcceptDontNotifyOrganizerMenu = "id=REPLY_ACCEPT_IGNORE_title";

		public static final String TentativeButton = "css=td[id$='__Inv__REPLY_TENTATIVE_title']";
		public static final String TentativeDropdown = "css=td[id$='__Inv__REPLY_TENTATIVE_dropdown']>div";
		public static final String TentativeNotifyOrganizerMenu = "id=REPLY_TENTATIVE_NOTIFY_title";
		public static final String TentativeEditReplyMenu = "id=INVITE_REPLY_TENTATIVE_title";
		public static final String TentativeDontNotifyOrganizerMenu = "id=REPLY_TENTATIVE_IGNORE_title";

		public static final String DeclineButton = "css=td[id$='__Inv__REPLY_DECLINE_title']";
		public static final String DeclineDropdown = "css=td[id$='__Inv__REPLY_DECLINE_dropdown']>div";
		public static final String DeclineNotifyOrganizerMenu = "id=REPLY_DECLINE_NOTIFY_title";
		public static final String DeclineEditReplyMenu = "id=INVITE_REPLY_DECLINE_title";
		public static final String DeclineDontNotifyOrganizerMenu = "id=REPLY_DECLINE_IGNORE_title";

		public static final String AcceptProposeNewTimeButton = "css=td[id$='Cou__ACCEPT_PROPOSAL_title']";
		public static final String DeclineProposeNewTimeButton = "css=td[id$='Cou__DECLINE_PROPOSAL_title']";
		public static final String ProposeNewTimeButton = "css=td[id$='__Inv__PROPOSE_NEW_TIME_title']";

		public static final String zSubjectField = "css=div[id^=zv__COMPOSE] input[id$=_subject_control]";
		public static final String zReplyButton = "css=div[id$='__REPLY']";
		public static final String zReplyAllButton = "css=div[id$='__REPLY_ALL']";
		public static final String zForwardButton = "css=div[id$='__FORWARD']";
		public static final String zDeleteButton = "css=div[parentid='z_shell']:not([aria-hidden='true']) div[id$='__DELETE']";
		public static final String zMessageDecryptionFailed = "css=td[class$='ZmSecureMailCertificateRow'] td[id$='title']:contains('This message cannot be decrypted because your certificate is not valid or is missing.')";
		public static final String zCertificateValidationFailed = "css=td[class$='ZmSecureMailCertificateRow'] td[id$='title']:contains('Certificate is invalid because the email address does not match')";
	}

	/**
	 * The various displayed fields in a message
	 */
	public static enum Field {
		ReceivedTime,ReceivedDate,From, ResentFrom, ReplyTo, To, Cc, OnBehalfOf, OnBehalfOfLabel, Bcc,Subject, Body
	}

	public String ContainerLocator = Locators.MessageViewPreviewAtBottomCSS;

	/**
	 * Protected constuctor for this object. Only classes within this package
	 * should create DisplayMail objects.
	 * 
	 * @param application
	 */
	protected DisplayMail(AbsApplication application) {
		super(application);

		logger.info("new " + DisplayMail.class.getCanonicalName());
	}

	@Override
	public String myPageName() {
		return (this.getClass().getName());
	}

	@Override
	public AbsPage zPressButton(Button button) throws HarnessException {
		logger.info(myPageName() + " zDisplayPressButton(" + button + ")");

		tracer.trace("Click " + button);

		AbsPage page = this;
		String locator = null;
		boolean doPostfixCheck = false;

		if (button == Button.B_REMOVE_ALL) {

			locator = this.ContainerLocator + " a[id$='_removeAll']";
			page = new DialogWarning(DialogWarning.DialogWarningID.PermanentlyRemoveTheAttachment, MyApplication,
					((AppAjaxClient) MyApplication).zPageMail);

			if (!this.sIsElementPresent(locator))
				throw new HarnessException("locator is not present for button " + button + " : " + locator);

			this.sClick(locator); // sClick() is required for this element

			this.zWaitForBusyOverlay();

			return (page);

		} else if (button == Button.B_VIEW_ENTIRE_MESSAGE) {

			locator = this.ContainerLocator + " span[id$='_msgTruncation_link']";

			if (!this.sIsElementPresent(locator))
				throw new HarnessException("locator is not present for button " + button + " : " + locator);

			this.sClick(locator); // sClick() is required for this element

			this.zWaitForBusyOverlay();

			return (page);

		} else if (button == Button.B_VIEW_CERTIFICATE) {

			locator = "css=td[class='ZmSecureMailCertificateRow'] div[class='FakeAnchor'] td[id$='title']:contains('View certificate')";

			if (!this.sIsElementPresent(locator))
				throw new HarnessException("locator is not present for button " + button + " : " + locator);

			this.sClick(locator); // sClick() is required for this element

			this.zWaitForBusyOverlay();

			return (page);
			
		} else if (button == Button.B_HIGHLIGHT_OBJECTS) {

			locator = this.ContainerLocator + " span[id$='_highlightObjects_link']";

			if (!this.sIsElementPresent(locator))
				throw new HarnessException("locator is not present for button " + button + " : " + locator);

			this.sClick(locator); // sClick() is required for this element

			this.zWaitForBusyOverlay();

			return (page);

		} else if (button == Button.B_ACCEPT) {

			locator = Locators.AcceptButton;
			page = null;
			doPostfixCheck = true;

		} else if (button == Button.O_ACCEPT_NOTIFY_ORGANIZER) {

			locator = Locators.AcceptNotifyOrganizerMenu;
			page = null;
			doPostfixCheck = true;

		} else if (button == Button.O_ACCEPT_EDIT_REPLY) {

			locator = Locators.AcceptEditReplyMenu;
			page = null;
			doPostfixCheck = true;

		} else if (button == Button.O_ACCEPT_DONT_NOTIFY_ORGANIZER) {

			locator = Locators.AcceptDontNotifyOrganizerMenu;
			page = null;
			doPostfixCheck = true;

		} else if (button == Button.B_TENTATIVE) {

			locator = Locators.TentativeButton;
			page = null;
			doPostfixCheck = true;

		} else if (button == Button.O_TENTATIVE_NOTIFY_ORGANIZER) {

			locator = Locators.TentativeNotifyOrganizerMenu;
			page = null;
			doPostfixCheck = true;

		} else if (button == Button.O_TENTATIVE_EDIT_REPLY) {

			locator = Locators.TentativeEditReplyMenu;
			page = null;
			doPostfixCheck = true;

		} else if (button == Button.O_TENTATIVE_DONT_NOTIFY_ORGANIZER) {

			locator = Locators.TentativeDontNotifyOrganizerMenu;
			page = null;
			doPostfixCheck = true;

		} else if (button == Button.B_DECLINE) {

			locator = Locators.DeclineButton;
			page = null;
			doPostfixCheck = true;

		} else if (button == Button.O_DECLINE_NOTIFY_ORGANIZER) {

			locator = Locators.DeclineNotifyOrganizerMenu;
			page = null;
			doPostfixCheck = true;

		} else if (button == Button.O_DECLINE_EDIT_REPLY) {

			locator = Locators.DeclineEditReplyMenu;
			page = null;
			doPostfixCheck = true;

		} else if (button == Button.O_DECLINE_DONT_NOTIFY_ORGANIZER) {

			locator = Locators.DeclineDontNotifyOrganizerMenu;
			page = null;
			doPostfixCheck = true;

		} else if (button == Button.B_PROPOSE_NEW_TIME) {

			locator = Locators.ProposeNewTimeButton;
			page = new FormApptNew(this.MyApplication);
			doPostfixCheck = true;

		} else if (button == Button.B_ACCEPT_PROPOSE_NEW_TIME) {

			locator = Locators.AcceptProposeNewTimeButton;
			page = new FormApptNew(this.MyApplication);
			doPostfixCheck = true;

		} else if (button == Button.B_DECLINE_PROPOSE_NEW_TIME) {

			locator = Locators.DeclineProposeNewTimeButton;
			page = new FormMailNew(this.MyApplication);
			doPostfixCheck = true;

		} else if (button == Button.B_ACCEPT_SHARE) {

			locator = this.ContainerLocator + " td[id$='__Shr__SHARE_ACCEPT_title']";
			page = new DialogShareAccept(MyApplication, ((AppAjaxClient) MyApplication).zPageMail);
			doPostfixCheck = true;

		} else if (button == Button.B_DECLINE_SHARE) {

			locator = this.ContainerLocator + " td[id$='__Shr__SHARE_DECLINE_title']";
			page = new DialogShareDecline(MyApplication, ((AppAjaxClient) MyApplication).zPageMail);
			doPostfixCheck = true;

		} else if (button == Button.B_FORWARD) {

			locator = Locators.zForwardButton;
			page = null;
			doPostfixCheck = true;

		} else if (button == Button.B_REPLY) {

			locator = Locators.zReplyButton;
			page = null;
			doPostfixCheck = true;

		} else if (button == Button.B_REPLYALL) {

			locator = Locators.zReplyAllButton;
			page = null;
			doPostfixCheck = true;

		} else if (button == Button.B_DELETE) {

			locator = Locators.zDeleteButton;
			page = null;
			doPostfixCheck = true;

		} else {
			throw new HarnessException("no implementation for button: " + button);
		}

		if (locator == null)
			throw new HarnessException("no locator defined for button " + button);

		if (!this.sIsElementPresent(locator))
			throw new HarnessException("locator is not present for button " + button + " : " + locator);

		this.zClickAt(locator, "");
		this.zWaitForBusyOverlay();
		SleepUtil.sleepMedium();

		if (page != null) {
			page.zWaitForActive();
		}

		if (doPostfixCheck) {
			Stafpostqueue sp = new Stafpostqueue();
			sp.waitForPostqueue();
		}

		return (page);
	}

	public AbsPage zPressButtonPulldown(Button pulldown, Button option) throws HarnessException {

		logger.info(myPageName() + " zPressButtonPulldown(" + pulldown + ", " + option + ")");

		tracer.trace("Click pulldown " + pulldown + " then " + option);

		if (pulldown == null || option == null)
			throw new HarnessException("Button/options cannot be null!");

		String pulldownLocator = null;
		String optionLocator = null;
		AbsPage page = this;
		boolean doPostfixCheck = false;

		if (pulldown == Button.B_ACCEPT) {

			pulldownLocator = Locators.AcceptDropdown;

			if (option == Button.O_ACCEPT_NOTIFY_ORGANIZER) {

				optionLocator = Locators.AcceptNotifyOrganizerMenu;
				doPostfixCheck = true;
				page = this;

			} else if (option == Button.O_ACCEPT_EDIT_REPLY) {

				optionLocator = Locators.AcceptEditReplyMenu;
				doPostfixCheck = false;
				page = new FormMailNew(this.MyApplication);

			} else if (option == Button.O_ACCEPT_DONT_NOTIFY_ORGANIZER) {

				optionLocator = Locators.AcceptDontNotifyOrganizerMenu;
				doPostfixCheck = false;
				page = this;

			} else {

				throw new HarnessException("No logic defined for pulldown " + pulldown + " and option " + option);

			}

		} else if (pulldown == Button.B_TENTATIVE) {

			pulldownLocator = Locators.TentativeDropdown;

			if (option == Button.O_TENTATIVE_NOTIFY_ORGANIZER) {

				optionLocator = Locators.TentativeNotifyOrganizerMenu;
				doPostfixCheck = true;
				page = this;

			} else if (option == Button.O_TENTATIVE_EDIT_REPLY) {

				optionLocator = Locators.TentativeEditReplyMenu;
				doPostfixCheck = false;
				page = new FormMailNew(this.MyApplication);

			} else if (option == Button.O_TENTATIVE_DONT_NOTIFY_ORGANIZER) {

				optionLocator = Locators.TentativeDontNotifyOrganizerMenu;
				doPostfixCheck = false;
				page = this;

			} else {

				throw new HarnessException("No logic defined for pulldown " + pulldown + " and option " + option);

			}

		} else if (pulldown == Button.B_DECLINE) {

			pulldownLocator = Locators.DeclineDropdown;

			if (option == Button.O_DECLINE_NOTIFY_ORGANIZER) {

				optionLocator = Locators.DeclineNotifyOrganizerMenu;
				doPostfixCheck = true;
				page = this;

			} else if (option == Button.O_DECLINE_EDIT_REPLY) {

				optionLocator = Locators.DeclineEditReplyMenu;
				doPostfixCheck = false;
				page = new FormMailNew(this.MyApplication);

			} else if (option == Button.O_DECLINE_DONT_NOTIFY_ORGANIZER) {

				optionLocator = Locators.DeclineDontNotifyOrganizerMenu;
				doPostfixCheck = false;
				page = this;
			}

		} else if (pulldown == Button.B_CALENDAR) {

			pulldownLocator = Locators.CalendarDropdown;
			optionLocator = "css=div[id*='Menu_'] td[id$='_title']:contains('" + option + "')";
			doPostfixCheck = true;
			page = this;

		} else {

			throw new HarnessException("No logic defined for pulldown " + pulldown + " and option " + option);

		}

		// Click to dropdown and corresponding option
		zClickAt(pulldownLocator, "");
		zWaitForBusyOverlay();
		zClick(optionLocator);
		zWaitForBusyOverlay();
		if (page != null) {
			page.zWaitForActive();
		}

		if (doPostfixCheck) {
			Stafpostqueue sp = new Stafpostqueue();
			sp.waitForPostqueue();
		}
		SleepUtil.sleepMedium();

		return (page);
	}

	public AbsPage zPressButtonPulldown(Button pulldown, String option) throws HarnessException {

		SleepUtil.sleepMedium(); // test fails because it immidiately tries to
									// select dropdown

		logger.info(myPageName() + " zPressButtonPulldown(" + pulldown + ", " + option + ")");

		tracer.trace("Click pulldown " + pulldown + " then " + option);

		if (pulldown == null || option == null)
			throw new HarnessException("Button/options cannot be null!");

		String pulldownLocator = null;
		String optionLocator = null;
		AbsPage page = this;
		boolean doPostfixCheck = false;

		if (pulldown == Button.B_CALENDAR) {

			pulldownLocator = Locators.CalendarDropdown;
			optionLocator = "css=div[id*='Menu_'] td[id$='_title']:contains('" + option + "')";
			doPostfixCheck = false;
			page = this;

		} else {

			throw new HarnessException("No logic defined for pulldown " + pulldown + " and option " + option);

		}

		// Click to dropdown and corresponding option
		zClickAt(pulldownLocator, "");
		zWaitForBusyOverlay();
		zClick(optionLocator);
		zWaitForBusyOverlay();

		if (page != null) {
			page.zWaitForActive();
		}

		if (doPostfixCheck) {
			Stafpostqueue sp = new Stafpostqueue();
			sp.waitForPostqueue();
		}

		return (page);
	}

	/**
	 * Parse an attachment row from a message
	 * 
	 * @param locator
	 * @return
	 * @throws HarnessException
	 */
	protected AttachmentItem parseAttachmentRow(String locator) throws HarnessException {

		AttachmentItem item = new AttachmentItem();

		// Remember the locator to this structure.
		// The locator can be re-used when clicking on the attachment.
		item.setLocator(locator);

		// Set the icon class
		//
		// "tr>td>div" ... probably not the most stable locator, however, there
		// is no unique ID on the image element. If this locator breaks,
		// just file a bug to put an ID on that element, e.g. div[id$='_icon']
		//
		if (this.sIsElementPresent(locator + " tr>td>div")) {
			String icon = this.sGetAttribute(locator + " tr>td>div@class");
			item.setAttachmentIcon(AttachmentItem.AttachmentIcon.valueOf(icon));
		}

		// Set the file name
		if (this.sIsElementPresent(locator + " a[id$='_main']")) {
			item.setAttachmentName(this.sGetText(locator + " a[id$='_main']"));
		}

		return (item);

	}

	/**
	 * Get the list of attachments on the displayed message
	 * 
	 * @return
	 * @throws HarnessException
	 */
	public List<AttachmentItem> zListGetAttachments() throws HarnessException {
		logger.info(myPageName() + " zListGetAttachments()");

		List<AttachmentItem> items = new ArrayList<AttachmentItem>();

		String listLocator = "css=div[parentid='z_shell']:not([aria-hidden='true']) table[id$='attLinks_table']";

		// Make sure the button exists
		if (!this.sIsElementPresent(listLocator)) {
			// No attachments!
			return (items);
		}

		// How many items are in the table?
		String tableLocator = listLocator + ">tbody>tr";
		int count = this.sGetCssCount(tableLocator);
		logger.debug(myPageName() + " zListGetMessages: number of messages: " + count);

		// Get each conversation's data from the table list
		for (int i = 1; i <= count; i++) {

			// Add the new item to the list
			AttachmentItem item = parseAttachmentRow(listLocator + ">tbody>tr:nth-of-type(" + i + ") ");
			items.add(item);
			logger.info(item.prettyPrint());
		}

		return (items);
	}

	public boolean zMailSecurityPresent(String emailAddress) throws HarnessException{
		
		String locator="css=td[class='ZmSecureMailCertificateRow'] td[id$='title']:contains('" + emailAddress + "')";
		
		if (this.sIsElementPresent(locator)) {
			//Mail Security header found
			return true;
		}

		return false;
	}

	public boolean zMessageCannotBeDecrypted() throws HarnessException{		
		return sIsElementPresent(Locators.zMessageDecryptionFailed);
	}

	public boolean zCertificateValidationFailed() throws HarnessException{		
		return sIsElementPresent(Locators.zCertificateValidationFailed);
	}

	public AbsPage zListAttachmentItem(Button button, AttachmentItem attachment) throws HarnessException {

		if (button == null)
			throw new HarnessException("button cannot be null");

		if (attachment == null)
			throw new HarnessException("attachment cannot be null");

		if (attachment.getLocator() == null) {
			throw new HarnessException(
					"Unable to locate attachment with filename(" + attachment.getAttachmentName() + ")");
		}

		logger.info(myPageName() + " zListAttachmentItem(" + button + ", " + attachment.getAttachmentName() + ")");

		tracer.trace(button + " on attachment = " + attachment.getAttachmentName());

		AbsPage page = null;
		String locator = attachment.getLocator();

		if (attachment.getLocator() == null) {
			throw new HarnessException(
					"Unable to locate attachment with filename(" + attachment.getAttachmentName() + ")");
		}

		if (button == Button.B_REMOVE) {

			locator = attachment.getLocator() + "a[id$='_remove']";
			page = new DialogWarning(DialogWarning.DialogWarningID.PermanentlyRemoveTheAttachment, MyApplication,
					((AppAjaxClient) MyApplication).zPageMail);

			this.sClick(locator);

			this.zWaitForBusyOverlay();

			if (page != null) {
				page.zWaitForActive();
			}

			return (page);

		} else if (button == Button.B_ADD_TO_CALENDAR) {

			locator = attachment.getLocator() + "a[id$='_calendar']";
			page = new DialogAddToCalendar(MyApplication, ((AppAjaxClient) MyApplication).zPageMail);

			this.sClick(locator);

			this.zWaitForBusyOverlay();

			if (page != null) {
				page.zWaitForActive();
			}

			return (page);

		} else if (button == Button.B_BRIEFCASE) {

			locator = attachment.getLocator() + "a[id$='_briefcase']";
			page = new DialogAddToBriefcase(MyApplication, ((AppAjaxClient) MyApplication).zPageMail);

			this.sClick(locator);

			this.zWaitForBusyOverlay();

			if (page != null) {
				page.zWaitForActive();
			}

			return (page);

		} else if (button == Button.B_ADD_TO_MY_FILES) {

			locator = "implement me!";
			page = null;

		} else {
			throw new HarnessException("implement me!  action = " + button);
		}

		if (!this.sIsElementPresent(locator))
			throw new HarnessException("locator is not present for button " + button + " : " + locator);

		this.zClick(locator);

		this.zWaitForBusyOverlay();

		return (page);

	}

	public AbsPage zListAttachmentItem(Action action, AttachmentItem attachment) throws HarnessException {

		if (action == null)
			throw new HarnessException("action cannot be null");

		if (attachment == null)
			throw new HarnessException("attachment cannot be null");

		if (attachment.getLocator() == null) {
			throw new HarnessException(
					"Unable to locate attachment with filename(" + attachment.getAttachmentName() + ")");
		}

		logger.info(myPageName() + " zListAttachmentItem(" + action + ", " + attachment.getAttachmentName() + ")");

		tracer.trace(action + " on attachment = " + attachment.getAttachmentName());

		AbsPage page = null;
		String locator = attachment.getLocator();

		if (attachment.getLocator() == null) {
			throw new HarnessException(
					"Unable to locate attachment with filename(" + attachment.getAttachmentName() + ")");
		}

		if (action == Action.A_LEFTCLICK) {

			// Since the window opens without a title bar, initialize it first
			page = new SeparateWindowOpenAttachment(this.MyApplication);
			((SeparateWindowOpenAttachment) page).zInitializeWindowNames();

			// Left-Click on the item
			this.sClick(locator + " a[id$='_main']");

			this.zWaitForBusyOverlay();

			// Sucks. Wait for the page to display.
			// Any have ideas for a workaround?
			SleepUtil.sleepVeryLong();

			return (page);

		} else if (action == Action.A_RIGHTCLICK) {

			locator = "implement me!";
			page = null;

		} else if (action == Action.A_HOVEROVER) {

			locator = attachment.getLocator() + "a[id$='_main']";
			page = new TooltipImage(MyApplication);

			// If another tooltip is active, sometimes it takes a few seconds
			// for the new text to show
			// So, wait if the tooltip is already active
			// Don't wait if the tooltip is not active
			//

			if (page.zIsActive()) {

				// Mouse over
				this.sMouseOver(locator);
				this.zWaitForBusyOverlay();

				// Wait for the new text
				SleepUtil.sleep(5000);

				// Make sure the tooltip is active
				page.zWaitForActive();

			} else {

				// Mouse over
				this.sMouseOver(locator);
				this.zWaitForBusyOverlay();
				
				// Make sure the tooltip is active
				page.zWaitForActive();

			}

			return (page);

		} else {
			throw new HarnessException("implement me!  action = " + action);
		}

		// default return command
		return (page);

	}

	/**
	 * Get a list of bubble objects form the field
	 * 
	 * @param field
	 *            - must be To, Cc, Bcc
	 * @return
	 * @throws HarnessException
	 */
	public List<AbsBubble> zListGetBubbles(Field field) throws HarnessException {
		logger.info("DisplayMail.zListGetBubbles(" + field + ")");

		List<AbsBubble> bubbles = new ArrayList<AbsBubble>();
		String locator = null;
		String fieldLocator = null;

		if (field == Field.To) {

			fieldLocator = " tr[id$='_to'] ";

		} else if (field == Field.Cc) {

			fieldLocator = " tr[id$='_cc'] ";

		} else if (field == Field.From) {

			fieldLocator = " tr[id$='_from'] ";

		} else if (field == Field.OnBehalfOf) {

			fieldLocator = " tr[id$='_obo'] ";

		} else if (field == Field.ResentFrom) {

			fieldLocator = " tr[id$='_bwo'] ";

		} else if (field == Field.ReplyTo) {

			fieldLocator = " tr[id$='_reply to'] ";

		} else if (field == Field.Bcc) {

			throw new HarnessException("implement me");

		} else {

			throw new HarnessException("no logic defined for field " + field);

		}

		// Determine how many bubles are listed
		locator = this.ContainerLocator + fieldLocator + " td[class~='LabelColValue'] span[class='addrBubble']";

		int count = this.sGetCssCount(locator);

		for (int i = 1; i <= count; i++) {

			locator = this.ContainerLocator + fieldLocator + " td[class~='LabelColValue']>span:nth-of-type(" + i + ")";

			// Create a new bubble item
			AbsBubble bubble = new BubbleEmailAddress(MyApplication);
			bubble.parseBubble(locator);

			// Add the item to the list
			bubbles.add(bubble);

		}

		return (bubbles);

	}

	/**
	 * Return TRUE/FALSE whether the appointment Accept/Decline/Tentative
	 * buttons are present
	 * 
	 * @return
	 * @throws HarnessException
	 */
	public boolean zHasShareADButtons() throws HarnessException {

		// Haven't fully baked this method.
		// Maybe it works.
		// Maybe it needs to check "visible" and/or x/y/z coordinates

		List<String> locators = Arrays.asList(this.ContainerLocator + " td[id$='__Shr__SHARE_ACCEPT_title']",
				this.ContainerLocator + " td[id$='__Shr__SHARE_DECLINE_title']");

		for (String locator : locators) {
			if (!this.sIsElementPresent(locator))
				return (false);
		}

		return (true);

	}

	/**
	 * Return TRUE/FALSE whether the Accept/Decline/Tentative buttons are
	 * present
	 * 
	 * @return
	 * @throws HarnessException
	 */
	public boolean zHasADTButtons() throws HarnessException {

		List<String> locators = Arrays.asList(this.ContainerLocator + " td[id$='__Inv__REPLY_ACCEPT_title']",
				this.ContainerLocator + " td[id$='__Inv__REPLY_TENTATIVE_title']",
				this.ContainerLocator + " td[id$='__Inv__REPLY_DECLINE_title']",
				this.ContainerLocator + " td[id$='__Inv__PROPOSE_NEW_TIME_title']");

		for (String locator : locators) {
			if (!this.sIsElementPresent(locator))
				return (false);
		}

		return (true);
	}

	public HtmlElement zGetMailPropertyAsHtml(Field field) throws HarnessException {

		String source = null;

		if (field == Field.Body) {

			try {
				String bodyLocator = "body";
				webDriver().switchTo().defaultContent();
				webDriver().switchTo().frame(0);
				we = webDriver().findElement(By.cssSelector(bodyLocator));

				source = this.sGetHtmlSource();
				logger.info("DisplayMail.zGetBody(" + bodyLocator + ") = " + source);
				source = "<html>" + source + "</html>";

			} finally {
				this.sSelectFrame("relative=top");
				webDriver().switchTo().defaultContent();
			}

		} else {
			throw new HarnessException("not implemented for field " + field);
		}

		logger.info("DisplayMail.zGetMailPropertyAsHtml() = " + HtmlElement.clean(source).prettyPrint());
		return (HtmlElement.clean(source));
	}

	public String zGetHtmlMailBody(Field field) throws HarnessException {
		if (field == Field.Body) {

			try {
				String bodyLocator = "body";
				webDriver().switchTo().frame("ZmHtmlEditor1_body_ifr");
				we = webDriver().findElement(By.cssSelector("body#tinymce"));

				String html = this.sGetHtmlSource();
				logger.info("GetHtmlMailBody(" + bodyLocator + ") = " + html);
				return (html);

			} finally {
				this.sSelectFrame("relative=top");
				webDriver().switchTo().defaultContent();
			}

		} else {
			throw new HarnessException("not implemented for field " + field);
		}
	}

	public String zGetMailPropertyAsText(Field field) throws HarnessException {

		if (field == Field.Body) {

			try {
				String bodyLocator = "css=div[class='ZmComposeView'] textarea[id$='_body']";
				this.sFocus(bodyLocator);
				this.zClick(bodyLocator);
				logger.info(this.sGetValue(bodyLocator));
				String BodyText = this.sGetValue(bodyLocator);
				return (BodyText);

			} finally {
				// Make sure to go back to the original iframe
				// this.sSelectFrame("relative=top");
			}

		} else if (field == Field.Subject) {

			String locator = Locators.zSubjectField;
			this.sFocus(locator);
			this.zClick(locator);
			logger.info(this.sGetValue(locator));
			String Subject = this.sGetValue(locator);
			return (Subject);

		} else {
			throw new HarnessException("not implemented for field " + field);
		}
	}

	public void zVerifySignaturePlaceInText(String placeOfSignature, String signatureBody, String mode)
			throws HarnessException {
		int indexOfSignature;
		int indexOfOrignalMsg;

		String displayedBody = this.zGetMailPropertyAsText(Field.Body);
		indexOfSignature = displayedBody.indexOf(signatureBody);
		if (mode.toLowerCase().equals("forward")) {
			indexOfOrignalMsg = displayedBody.indexOf("Forwarded Message");
		} else {
			indexOfOrignalMsg = displayedBody.indexOf("Original Message");
		}

		if (placeOfSignature.equals("AboveIncludedMsg")) {
			Assert.assertTrue(indexOfSignature <= indexOfOrignalMsg,
					"The signature body " + signatureBody + " is  displayed above the Mail body");

		} else if (placeOfSignature.equals("BelowIncludedMsg")) {
			Assert.assertTrue(indexOfSignature > indexOfOrignalMsg,
					"The signature body " + signatureBody + " is  displayed below the Mail body");
		}
	}

	public void zVerifySignaturePlaceInHTML(String placeOfSignature, String signatureBody, String mode)
			throws HarnessException {

		int indexOfSignature;
		int indexOfOrignalMsg;

		String displayedBody = this.zGetHtmlMailBody(Field.Body);
		indexOfSignature = displayedBody.indexOf(signatureBody);
		indexOfOrignalMsg = displayedBody.indexOf("From");

		if (placeOfSignature.equals("AboveIncludedMsg")) {
			Assert.assertTrue(indexOfSignature <= indexOfOrignalMsg,
					"The signature body " + signatureBody + " is  displayed above the Mail body");

		} else if (placeOfSignature.equals("BelowIncludedMsg")) {
			Assert.assertTrue(indexOfSignature > indexOfOrignalMsg,
					"The signature body " + signatureBody + " is  displayed below the Mail body");
		}
	}

	/**
	 * Get the string value of the specified field
	 * 
	 * @return the displayed string value
	 * @throws HarnessException
	 */
	public String zGetMailProperty(Field field) throws HarnessException {
		logger.info("DisplayMail.zGetDisplayedValue(" + field + ")");

		String locator = null;

		if (field == Field.Bcc) {

			// Does this show in any mail views? Maybe in Sent?
			throw new HarnessException("implement me!");

		} else if (field == Field.Body) {
			
			int frames = this.sGetCssCount("css=iframe");
			logger.debug("Body: # of frames: " + frames);

			try {

				String bodyLocator = "body";
				webDriver().switchTo().defaultContent();
				
				if (frames >=1 ) {
					webDriver().switchTo().frame(0);
					we = webDriver().findElement(By.cssSelector(bodyLocator));
				} else {
					we = webDriver().findElement(By.className("MsgBody-text"));
				}				

				String html = this.sGetHtmlSource();
				logger.info("DisplayMail.zGetBody(" + bodyLocator + ") = " + html);
				return (html);

			} finally {
				this.sSelectFrame("relative=top");
				webDriver().switchTo().defaultContent();
			}

		} else if (field == Field.Cc) {

			locator = this.ContainerLocator
					+ " tr[id$='_cc'] td[class~='LabelColValue'] span[id$='_com_zimbra_email'] span span";
			if (!sIsElementPresent(locator)) {
				// no email zimlet case
				locator = this.ContainerLocator + " tr[id$='_cc'] td[class~='LabelColValue']";
			}

		} else if (field == Field.From) {

			locator = this.ContainerLocator + " td[id$='_from'] span[id$='_com_zimbra_email'] span span";
			if (!sIsElementPresent(locator)) {
				// no email zimlet case
				locator = this.ContainerLocator + " td[id$='_from']";
			}

		} else if (field == Field.OnBehalfOf) {

			// locator = this.ContainerLocator + " td[id$='_obo']
			// span[id$='_com_zimbra_email'] span span";
			locator = this.ContainerLocator
					+ "td[id$='_from'] span[id$='_com_zimbra_email'] span:contains(on behalf of)";

			if (!sIsElementPresent(locator)) {
				// no email zimlet case
				locator = this.ContainerLocator + " td[id$='_obo']";
			}

		} else if (field == Field.ResentFrom) {

			locator = this.ContainerLocator + " td[id$='_bwo'] span[id$='_com_zimbra_email'] span";
			if (!sIsElementPresent(locator)) {
				// no email zimlet case
				locator = this.ContainerLocator + " td[id$='_bwo']";
			}

		} else if (field == Field.OnBehalfOfLabel) {

			locator = this.ContainerLocator + " td[id$='_obo_label']";

		} else if (field == Field.ReplyTo) {

			locator = this.ContainerLocator
					+ " tr[id$='_reply to'] td.LabelColValue span[id$='_com_zimbra_email'] span span";
			if (!sIsElementPresent(locator)) {
				locator = this.ContainerLocator + " tr[id$='_reply to'] td.LabelColValue";
			}

		} else if (field == Field.ReceivedDate) {

			locator = this.ContainerLocator + " tr[id$='_hdrTableTopRow'] td[class~='DateCol']";

		} else if (field == Field.ReceivedTime) {

			String timeAndDateLocator = this.ContainerLocator + " td[class~='DateCol']";

			// Make sure the subject is present
			if (!this.sIsElementPresent(timeAndDateLocator))
				throw new HarnessException("Unable to find the time and date field!");

			// Get the subject value
			String timeAndDate = this.sGetText(timeAndDateLocator).trim();
			String date = this.zGetMailProperty(Field.ReceivedDate);

			// Strip the date so that only the time remains
			String time = timeAndDate.replace(date, "").trim();

			logger.info("DisplayMail.zGetDisplayedValue(" + field + ") = " + time);
			return (time);

		} else if (field == Field.Subject) {

			locator = this.ContainerLocator + " tr[id$='_hdrTableTopRow'] td[class~='SubjectCol']";

		} else if (field == Field.To) {

			locator = this.ContainerLocator
					+ " tr[id$='_to'] td[class='LabelColValue'] span[id$='_com_zimbra_email'] span span";
			if (!sIsElementPresent(locator)) {
				// no email zimlet case
				locator = this.ContainerLocator + " tr[id$='_to'] td[class='LabelColValue'] ";
			}

		} else {

			throw new HarnessException("no logic defined for field " + field);

		}

		// Make sure the subject is present
		if (!this.sIsElementPresent(locator))
			throw new HarnessException("Unable to find the field = " + field + " using locator = " + locator);

		// Get the subject value
		String value = this.sGetText(locator).trim();

		logger.info("DisplayMail.zGetDisplayedValue(" + field + ") = " + value);
		return (value);

	}

	public AbsPage zHoverOver(Field field) throws HarnessException {
		logger.info("zHoverOver(" + field + ")");

		if (field == null) {
			throw new HarnessException("field cannot be null");
		}

		AbsPage page = null;
		String locator = null;

		if (field == Field.From) {

			locator = this.ContainerLocator + " td[id$='_from'] span[class='addrBubble']";

		} else {
			throw new HarnessException("Logic not defined for field=" + field);

		}

		if (!(this.sIsElementPresent(locator))) {
			throw new HarnessException("locator not present: " + locator);
		}

		this.sMouseOver(locator);
		this.zWaitForBusyOverlay();

		return (page);

	}

	/**
	 * Wait for Zimlets to be rendered in the message
	 * 
	 * @throws HarnessException
	 */
	public void zWaitForZimlets() throws HarnessException {
		// TODO: don't sleep. figure out a way to query the app if zimlets are
		// applied
		logger.info("zWaitForZimlets: sleep a bit to let the zimlets be applied");
		SleepUtil.sleepLong();
	}

	@Override
	public boolean zIsActive() throws HarnessException {
		// logger.warn("implement me", new Throwable());
		zWaitForZimlets();

		// Determine which <div/> contains this preview
		// Use this 'top' css for all subsequent parsing

		if (this.zIsVisiblePerPosition(Locators.MessageViewOpenMessageCSS, 0, 0)) {
			int count = this.sGetCssCount(Locators.MessageViewOpenMessageCSS);
			if (count > 1) {
				throw new HarnessException("Too many message views open: " + count);
			}
			ContainerLocator = "css=div#" + this.sGetAttribute(Locators.MessageViewOpenMessageCSS + "@id");
			
		} else if (this.zIsVisiblePerPosition(PageMail.Locators.IsMsgViewActiveCSS, 0, 0)) {
			if (this.zIsVisiblePerPosition(Locators.MessageViewPreviewAtBottomCSS, 0, 0)) {
				ContainerLocator = Locators.MessageViewPreviewAtBottomCSS;
			} else if (this.zIsVisiblePerPosition(Locators.MessageViewPreviewAtRightCSS, 0, 0)) {
				ContainerLocator = Locators.MessageViewPreviewAtRightCSS;
			} else if (this.sIsElementPresent(Locators.MessageViewPreviewOff)){
				ContainerLocator=Locators.MessageViewPreviewOff;
			} else {
				throw new HarnessException("Unable to determine the current open view");
			}
			
		} else if (this.zIsVisiblePerPosition(PageMail.Locators.IsConViewActiveCSS, 0, 0)) {
			if (this.zIsVisiblePerPosition(Locators.ConversationViewPreviewAtBottomCSS, 0, 0)) {
				ContainerLocator = Locators.ConversationViewPreviewAtBottomCSS;
			} else if (this.zIsVisiblePerPosition(Locators.ConversationViewPreviewAtRightCSS, 0, 0)) {
				ContainerLocator = Locators.ConversationViewPreviewAtRightCSS;
			} else if (this.sIsElementPresent(Locators.MessageViewPreviewOff)){
				ContainerLocator=Locators.MessageViewPreviewOff;
			} else {
				throw new HarnessException("Unable to determine the current open view");
			}
			
		} else {
			throw new HarnessException("Unable to determine the current open view");
		}

		return (sIsElementPresent(this.ContainerLocator));

	}

}
