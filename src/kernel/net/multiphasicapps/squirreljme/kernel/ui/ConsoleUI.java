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
import java.util.List;
import java.util.ListIterator;
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
		"Tasks";
	
	/** The about menu item. */
	public static final String MENU_ABOUT =
		"About SquirrelJME";
	
	/** The quit menu item. */
	public static final String MENU_QUIT =
		"Quit";
	
	/** The console view which interacts with the user directly. */
	protected final ConsoleDisplay console;
	
	/** The current time. */
	protected final Calendar currentcal =
		Calendar.getInstance();
	
	/** The current time string builder. */
	protected final StringBuilder timebuilder =
		new StringBuilder();
		
	/** The event queue to use. */
	protected final EventQueue eventqueue =
		new EventQueue();
	
	/** The available archive finders. */
	protected final List<ArchiveFinder> finders;
	
	/** The current menu. */
	private volatile RecursiveMenu _menu;
	
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
		this._menu = rm;
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
		
		System.err.printf("DEBUG -- Keyboard %d %c%n", __port, __c);
		
		// Depends on the key
		switch (__c)
		{
			case '<':
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
			eventqueue.handleEvents(this);
			
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
		RecursiveMenu menu = this._menu;
		
		// Correct cursor position
		int ni = menu.size();
		int cp = menu.getCursor();
	
		// Determine the number of pages to draw
		int numpages = (__rows / __nr) + 1;
		int itemsperpage = Math.min(__nr, ni);
		int onpage = Math.max(0, (cp / (Math.max(1, itemsperpage))) - 1);
	
		// Draw all items on the given page
		for (int i = 0, dr = __sr, j = (onpage * itemsperpage);
			i < itemsperpage; i++, dr++, j++)
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
}

