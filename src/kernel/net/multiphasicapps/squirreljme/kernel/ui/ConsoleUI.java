// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.kernel.ui;

import java.util.Calendar;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import net.multiphasicapps.squirreljme.kernel.archive.Archive;
import net.multiphasicapps.squirreljme.kernel.archive.ArchiveFinder;
import net.multiphasicapps.squirreljme.kernel.display.ConsoleDisplay;
import net.multiphasicapps.squirreljme.kernel.event.EventHandler;
import net.multiphasicapps.squirreljme.kernel.event.EventKind;
import net.multiphasicapps.squirreljme.kernel.event.EventQueue;
import net.multiphasicapps.squirreljme.kernel.event.KeyChars;
import net.multiphasicapps.squirreljme.kernel.Kernel;

/**
 * This is the launcher controller which uses a console to interact with the
 * user. This uses a single console to display to the user and provides a
 * console like interface to programs running (multiple programs could be
 * displayed at the same time if desired) and shows the result of programs
 * as they are running.
 *
 * @since 2016/05/14
 */
public class ConsoleUI
	extends StandardUI
	implements EventHandler.Key, Runnable
{
	/** The number of nanoseconds to spend in a console frame. */
	public static final long CONSOLE_DELAY =
		50_000_000L;
	
	/** The starting row number to print menu items on. */
	public static final int STARTING_ROW =
		2;
	
	/** The column the cursor is on. */
	public static final int CURSOR_COLUMN =
		1;
	
	/** The column text starts on. */
	public static final int ITEM_COLUMN =
		2;
	
	/** The task menu item. */
	public static final String MENU_TASKS =
		"<Tasks>";
	
	/** The about menu item. */
	public static final String MENU_ABOUT =
		"<About>";
	
	/** The quit menu item. */
	public static final String MENU_QUIT =
		"<Quit>";
	
	/** Go to the previous menu. */
	public static final String MENU_BACK =
		"<Go Back>";
	
	/** Do quit. */
	public static final String MENU_QUIT_YES =
		"<Really Quit>";
	
	/** Refresh current archive. */
	public static final String MENU_REFRESH =
		"<Refresh>";
	
	/** Easter egg. */
	public static final String MENU_EASTER_EGG =
		"      SquirrelJME";
	
	/** The console view which interacts with the user directly. */
	protected final ConsoleDisplay console;
	
	/** The current time. */
	protected final Calendar currentcal =
		Calendar.getInstance();
	
	/** The current time string builder. */
	protected final StringBuilder timebuilder =
		new StringBuilder();
		
	/** The event queue to use. */
	protected final EventQueue eventqueue;
	
	/** The available archive finders. */
	protected final List<ArchiveFinder> finders;
	
	/** The menu stack. */
	protected final Deque<RecursiveMenu> menus =
		new LinkedList<>();
	
	/** Current oops. */
	private volatile String _oops;
	
	/** The current archive being looked at for refreshing. */
	private volatile ArchiveFinder _refresharchive;
	
	/**
	 * Initializes the console launcher controller.
	 *
	 * @param __al The launcher interface.
	 * @since 2016/05/14
	 */
	public ConsoleUI(Kernel __al)
	{
		super(__al);
		
		// {@squirreljme.error AY02 Could not initialize the console
		// that the console launcher controller would be using to display
		// and interact with the user.}
		this.console = __al.createConsoleDisplay();
		if (this.console == null)
			throw new RuntimeException("AY02");
		
		// Setup finder seeker
		List<ArchiveFinder> finders = __al.archiveFinders();;
		int nf = finders.size();
		this.finders = finders;
		
		// Setup the main menu items
		Object[] ii = new Object[nf + 3];
		for (int i = 0; i < nf; i++)
			ii[i] = finders.get(i);
		ii[nf] = MENU_TASKS;
		ii[nf + 1] = MENU_ABOUT;
		ii[nf + 2] = MENU_QUIT;
		
		// Set the menu
		RecursiveMenu rm = new RecursiveMenu(ii);
		this.menus.offerLast(rm);
		
		// Use the kernel event queue
		this.eventqueue = __al.kernelProcess().eventQueue();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/18
	 */
	@Override
	public int handleKeyEvent(EventKind __k, int __port, char __c)
	{
		// Only accept typing events
		if (__k != EventKind.KEY_TYPED)
			return EventHandler.PASS_EVENT;
		
		// Get the top most menu
		RecursiveMenu menu = this.menus.peekLast();
		if (menu == null)
			return EventHandler.PASS_EVENT;
		
		// Depends on the key
		switch (__c)
		{
				// Menu up
			case '1':
			case 'a':
			case 'A':
			case 'w':
			case 'W':
			case '<':
			case ',':
			case KeyChars.JOYSTICK_UP:
			case KeyChars.JOYSTICK_UP_SECONDARY:
			case KeyChars.JOYSTICK_LEFT:
			case KeyChars.JOYSTICK_LEFT_SECONDARY:
			case KeyChars.JOYSTICK_LEFT_SHOULDER_0:
			case KeyChars.JOYSTICK_LEFT_SHOULDER_0 + 1:
			case KeyChars.KP_LEFT:
			case KeyChars.KP_UP:
			case KeyChars.LEFT:
			case KeyChars.UP:
			case KeyChars.NUMPAD_1:
			case KeyChars.PEN_SCROLL_UP:
			case KeyChars.PEN_SCROLL_LEFT:
				menu.previousItem();
				break;
			
				// Menu down
			case '3':
			case 'd':
			case 'D':
			case 's':
			case 'S':
			case '>':
			case '.':
			case KeyChars.JOYSTICK_DOWN:
			case KeyChars.JOYSTICK_DOWN_SECONDARY:
			case KeyChars.JOYSTICK_RIGHT:
			case KeyChars.JOYSTICK_RIGHT_SECONDARY:
			case KeyChars.JOYSTICK_RIGHT_SHOULDER_0:
			case KeyChars.JOYSTICK_RIGHT_SHOULDER_0 + 1:
			case KeyChars.KP_RIGHT:
			case KeyChars.KP_DOWN:
			case KeyChars.RIGHT:
			case KeyChars.DOWN:
			case KeyChars.NUMPAD_3:
			case KeyChars.PEN_SCROLL_DOWN:
			case KeyChars.PEN_SCROLL_RIGHT:
				menu.nextItem();
				break;
			
				// The light gun only can trigger hitting the screen or
				// missing the screen, so if the menu would fall off the end
				// Also do the same for the select button (aka Zelda style).
				// Also, there could possibly only be two GPIO buttons and as
				// such have one use this selection style.
			case KeyChars.JOYSTICK_LIGHTGUN_MISSED:
			case KeyChars.JOYSTICK_SELECT:
			case KeyChars.PEN_SECONDARY_CLICK:
			case KeyChars.GPIO_BUTTON_0:
				{
					// Get the current and move the cursor up
					int at = menu.getCursor();
					int nx = menu.nextItem();
					int no = menu.getCursor();
					
					// If the cursor is at the same location, set it to the
					// start position otherwise the user will be stuck at the
					// last item and can never go back.
					if (at == no)
						menu.setCursor(0);
				}
				break;
				
				// Select item in the menu
			case ' ':
			case '2':
			case KeyChars.ENTER:
			case KeyChars.JOYSTICK_LIGHTGUN_HIT:
			case KeyChars.JOYSTICK_START:
			case KeyChars.JOYSTICK_FACE_BUTTON_0:
			case KeyChars.JOYSTICK_FACE_BUTTON_0 + 1:
			case KeyChars.JOYSTICK_FACE_BUTTON_0 + 2:
			case KeyChars.JOYSTICK_FACE_BUTTON_0 + 3:
			case KeyChars.NUMPAD_2:
			case KeyChars.NUMPAD_ENTER:
			case KeyChars.PEN_PRIMARY_CLICK:
			case KeyChars.GPIO_BUTTON_0 + 1:
				__selectMenu();
				break;
			
				// Unknown, pass it through
			default:
				return EventHandler.PASS_EVENT;
		}
		
		// Consume it
		return EventHandler.CONSUME_EVENT;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/14
	 */
	@Override
	public void run()
	{
		// Get some things
		Calendar currentcal = this.currentcal;
		EventQueue eventqueue = this.eventqueue;
		
		// Loop
		for (;;)
		{
			// Get the entry time
			long entertime = System.nanoTime();
			
			// Update the calendar
			long nowtime = System.currentTimeMillis();
			currentcal.setTimeInMillis(nowtime);
		
			// Get the console
			ConsoleDisplay console = this.console;
			int cols = console.getColumns(), rows = console.getRows();
			
			// Clear the console
			console.clear();
		
			// Draw the name of the software
			console.put(0, 0, "SquirrelJME");
		
			// Setup the time to draw
			StringBuilder timebuilder = this.timebuilder;
			__handleTime(timebuilder, currentcal);
			console.put((cols - 1) - timebuilder.length(), 0, timebuilder);
			
			// Draw the menu
			__drawMenu(console, cols, rows, STARTING_ROW,
				Math.max(1, rows - (STARTING_ROW + 1)));
			
			// Handle console events.
			try
			{
				eventqueue.handleEvents(this);
			}
			
			// Oops!
			catch (Throwable t)
			{
				this._oops = t.toString();
			}
			
			// Is there an oops?
			String oops = this._oops;
			if (oops != null)
				console.put(0, 1, oops);
			
			// If there is enough time to draw the console then display it
			long durtime = System.nanoTime() - entertime;
			if (durtime < CONSOLE_DELAY)
			{
				// Force the console to be drawn
				console.displayConsole();
			}
			
			// Get the console frame duration
			durtime = System.nanoTime() - entertime;
			long restime = CONSOLE_DELAY - durtime;
			if (restime > 0)
				try
				{
					// If the rest time is really high then do not sleep for
					// an extreme amount of time, otherwise the user interface
					// would freeze solid
					Thread.sleep(Math.min(CONSOLE_DELAY, restime) /
						1_000_000L);
				}
				
				// Ignore
				catch (InterruptedException e)
				{
				}
			
			// The user interface might be busy so do not go crazy and
			// consume all available cycles
			else
				Thread.yield();
		}
	}

	/**
	 * Draws the menu.
	 *
	 * @param __con The console display.
	 * @param __cols The column count.
	 * @param __rows The row count.
	 * @param __sr The starting row to draw on.
	 * @param __nr The number of rows to draw.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/18
	 */
	private void __drawMenu(ConsoleDisplay __con, int __cols,
		int __rows, int __sr, int __nr)
		throws NullPointerException
	{
		// Check
		if (__con == null)
			throw new NullPointerException("NARG");
		
		// Get the menu
		RecursiveMenu menu = this.menus.peekLast();
		
		// Correct cursor position
		int ni = menu.size();
		int cp = menu.getCursor();
	
		// Determine the number of pages to draw
		int itemsperpage = Math.min(__nr, ni);
		int numpages = (ni / itemsperpage) + 1;
		int onpage = Math.max(0, cp / itemsperpage);
	
		// Draw all items on the given page
		for (int i = 0, dr = __sr, j = (onpage * itemsperpage);
			i < itemsperpage && j < ni; i++, dr++, j++)
		{
			// Draw the selection cursor
			console.put(CURSOR_COLUMN, dr, (cp == j ? "*" : " "));
			
			// Draw object
			console.put(ITEM_COLUMN, dr, String.valueOf(menu.get(j)));
		}
	}
	
	/**
	 * Prints the current time to the given string.
	 *
	 * @param __sb The output buffer.
	 * @param __cal The calendar interface to get the time from.
	 * @since 2016/05/14
	 */
	private void __handleTime(StringBuilder __sb, Calendar __cal)
	{
		// Clear it
		__sb.setLength(0);
		
		// Hour
		int h;
		if ((h = __cal.get(Calendar.HOUR_OF_DAY)) < 10)
			__sb.append('0');
		__sb.append(h);
		
		// Space
		__sb.append(':');
		
		// Minute
		int m;
		if ((m = __cal.get(Calendar.MINUTE)) < 10)
			__sb.append('0');
		__sb.append(m);
		
		// Space
		__sb.append(':');
		
		// Seocnd
		int s;
		if ((s = __cal.get(Calendar.SECOND)) < 10)
			__sb.append('0');
		__sb.append(s);
	}
	
	/**
	 * Selects the current item on the menu.
	 *
	 * @since 2016/05/18
	 */
	private void __selectMenu()
	{
		// Get the top most menu
		Deque<RecursiveMenu> queue = this.menus;
		RecursiveMenu menu = queue.peekLast();
		if (menu == null || menu.isEmpty())
			return;
		
		// Get the item to be selected
		Object item = menu.get(menu.getCursor());
		
		// Quit the kernel?
		if (item == MENU_QUIT)
		{
			// Could have selected the menu by accident, so confirm it
			RecursiveMenu rm = new RecursiveMenu(MENU_BACK, MENU_QUIT_YES);
			queue.offerLast(rm);
		}
		
		// Really quit
		else if (item == MENU_QUIT_YES)
			this.kernel.quitKernel();
		
		// Go to the previous menu
		else if (item == MENU_BACK)
			queue.removeLast();
		
		// The about menu
		else if (item == MENU_ABOUT)
			queue.offerLast(new RecursiveMenu(
				MENU_BACK,
				"Multi-Phasic Applications:",
				MENU_EASTER_EGG,
				"(C) 2013-2016 Steven Gawroriski",
				"mail: <steven@multiphasicapps.net>",
				"webs: <http://multiphasicapps.net/>",
				"Under the GNU AGPLv3+",
				"",
				"Mascot by: `puppenstein`",
				"<http://www.furaffinity.net/user/",
				"puppenstein/>"));
		
		// An easter egg
		else if (item == MENU_EASTER_EGG)
			queue.offerLast(new RecursiveMenu(
				MENU_BACK,
				"  ;;;        ;; ",
				"  ;. ; ;;; ;;.; ",
				" ;.. ;;; ;; ..; ",
				" ;.,.     . ,.; ",
				" ;,. ,     .,,; ",
				" ;. ,       .,; ",
				" ;,,   .    .,; ",
				" ;,,. .,.   ,;; ",
				" ;,.|.  |.  ,;  ",
				" ;; |   | ..,;  ",
				"  ;         ;;  ",
				"  ; \\ / ,  ;,;  ",
				"  ;; |  ,  ;,;  ",
				"   ;;;;;  ;,,;  ",
				"    |||  ;,,;;  ",
				"     ;;;;;;;    "));
		
		// Refresh the current archive
		else if (item == MENU_REFRESH)
		{
			// Get the archive
			ArchiveFinder af = this._refresharchive;
			
			// Only if not null, is a refresh done
			if (af != null)
				af.refresh();
		}
		
		// Is an archive?
		else if (item instanceof ArchiveFinder)
		{
			// Cast
			ArchiveFinder af = (ArchiveFinder)item;
			
			// Refresh it
			af.refresh();
			
			// Get list of items to make new menu for it
			List<Archive> list = af.archives();
			int n = list.size();
			
			// Setup basic menu items
			Object[] ix = new Object[n + 2];
			ix[0] = MENU_BACK;
			ix[1] = MENU_REFRESH;
			
			// Add archives to items
			for (int i = 0; i < n; i++)
				ix[2 + i] = list.get(i);
			
			// Setup new menu
			RecursiveMenu pu = new RecursiveMenu(ix);
			
			// Add menu to top of the stack
			queue.offerLast(pu);
			
			// Set the archive to refresh on
			this._refresharchive = af;
		}
	}
}

