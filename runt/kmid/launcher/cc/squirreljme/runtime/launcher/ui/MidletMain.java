// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.launcher.ui;

import java.util.ArrayList;
import java.util.Timer;
import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.List;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;
import javax.microedition.swm.ManagerFactory;
import javax.microedition.swm.Suite;
import javax.microedition.swm.SuiteManager;
import javax.microedition.swm.SuiteType;
import javax.microedition.swm.Task;
import javax.microedition.swm.TaskManager;
import javax.microedition.swm.TaskStatus;

/**
 * This is the main midlet for the LCDUI based launcher interface.
 *
 * @since 2016/10/11
 */
public class MidletMain
	extends MIDlet
{
	/** Timer used to reschedule things. */
	static final Timer _TIMER =
		new Timer("LauncherRecoverThread");
	
	/** The display that is being used. */
	static volatile Display _MAIN_DISPLAY;
	
	/** The list which contains all of the programs we can run. */
	protected final List programlist =
		new List("SquirrelJME Launcher", Choice.EXCLUSIVE);
	
	/** Command used to launch a program. */
	protected final Command launchcommand =
		new Command("Launch", Command.OK, 1);
	
	/** Command used to exit the launcher and terminate. */
	protected final Command exitcommand =
		new Command("Exit", Command.EXIT, 2);
	
	/** The active task. */
	private final __ActiveTask__ _activetask =
		new __ActiveTask__();
	
	/** The programs which are mapped to the list. */
	private volatile __Program__[] _programs;
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/11
	 */
	@Override
	protected void destroyApp(boolean __uc)
		throws MIDletStateChangeException
	{
		// This is not used at all
	}
	
	/**
	 * Refreshes the list.
	 *
	 * @since 2018/11/16
	 */
	public void refresh()
	{
		// When a refresh is happening, change the title so that is
		// indicated
		List programlist = this.programlist;
		programlist.setTitle("Loading (Querying Suites)...");
		
		// Used for checking and such
		__ActiveTask__ activetask = this._activetask;
		
		// The list was previously not used
		if (programlist.size() <= 0)
		{
			// Add a message saying what is going on
			programlist.append("Loading available programs...", null);
			programlist.append("This might take awhile!", null);
			
			// Re-flip on this display
			if (_MAIN_DISPLAY.getCurrent() == programlist)
				_MAIN_DISPLAY.setCurrent(programlist);
		}
		
		// Go through all of the available application suites and build the
		// program list
		boolean queried = false;
		int foundcount = 0;
		ArrayList<__Program__> programs = new ArrayList<>();
		for (Suite suite : ManagerFactory.getSuiteManager().getSuites(
			SuiteType.APPLICATION))
		{
			// Hide this on the SquirrelJME launcher?
			if (Boolean.valueOf(suite.getAttributeValue(
				"X-SquirrelJME-NoLauncher")))
				continue;
			
			// Query is done, scan suites
			if (!queried)
			{
				programlist.setTitle("Loading (Scanning Suites)...");
				queried = true;
			}
			
			// Since we need the program name AND the entry point we need
			// to decode the parts that make it up!
			for (int i = 1; i >= 1; i++)
			{
				// No more programs in this suite
				String value = suite.getAttributeValue("MIDlet-" + i);
				if (value == null)
					break;
				
				// There will be two commas and the format is in:
				// title, icon-resource, mainclass
				int fc = value.indexOf(','),
					sc = value.lastIndexOf(',');
				if (fc < 0 || sc < 0)
					continue;
				
				// Split off
				String title = value.substring(0, fc).trim(),
					iconrc = value.substring(fc + 1, sc).trim(),
					main = value.substring(sc + 1).trim();
				
				// Build program
				programs.add(new __Program__(suite, main, title, activetask));
				
				// Say it was found via the title
				programlist.setTitle(String.format(
					"Loading (%d Found)...", ++foundcount));
			}
		}
		
		// Indicate that the program list is being built
		programlist.setTitle(String.format(
			"Building List (%d Found)...", foundcount));
		
		// Build program array
		__Program__[] arrprogs = programs.<__Program__>toArray(
			new __Program__[programs.size()]);
		
		// Clear the program list
		programlist.deleteAll();
		
		// Build the list in the program order
		for (__Program__ p : arrprogs)
			programlist.append(p.displayName(), p.displayImage());
		
		// Use this list
		this._programs = arrprogs;
		
		// All done so, return the title back
		programlist.setTitle("SquirrelJME Launcher");
		
		// Re-flip on this display
		if (_MAIN_DISPLAY.getCurrent() == programlist)
			_MAIN_DISPLAY.setCurrent(programlist);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/16
	 */
	@Override
	protected void startApp()
		throws MIDletStateChangeException
	{
		// We will need to access our own display to build the list of
		// MIDlets that could actually be ran
		Display disp = Display.getDisplay(this);
		_MAIN_DISPLAY = disp;
		
		// Add commands to the list so things can be done with them
		List programlist = this.programlist;
		programlist.addCommand(this.launchcommand);
		programlist.addCommand(this.exitcommand);
		
		// Need to handle commands and such
		programlist.setCommandListener(new __CommandHandler__());
		
		// Display the program list
		disp.setCurrent(programlist);
		
		// Only load the programs when the list is on the screen so that way
		// if the user is impatient and thinks nothing is happening when
		// something is.  Loading the program list can take awhile because
		// it checks through everything, so best to have an indicator of it.
		// Yield thread to give another a chance
		while (!programlist.isShown())
			Thread.yield();
		
		// Do the refresh now that it has been displayed
		this.refresh();
	}
	
	/**
	 * This is the handler for commands.
	 *
	 * @since 2018/11/16
	 */
	private final class __CommandHandler__
		implements CommandListener
	{
		/**
		 * {@inheritDoc}
		 * @since 2018/11/16
		 */
		@Override
		public final void commandAction(Command __c, Displayable __d)
		{
			// Launching a program?
			if (__c == MidletMain.this.launchcommand)
			{
				__Program__[] programs = MidletMain.this._programs;
				
				// If the list is empty then do nothing because it will NPE
				// or out of bounds if the selection is off
				List list = (List)__d;
				int seldx = list.getSelectedIndex();
				if (list.size() <= 0 || seldx < 0 || seldx >= programs.length)
					return;
				
				// Indication that something is happening
				MidletMain.this.programlist.setTitle("Launching...");
				
				// Launch this program
				programs[seldx].__launch();
				
				// All done so, return the title back
				programlist.setTitle("SquirrelJME Launcher");
			}
			
			// Exiting the VM?
			else if (__c == MidletMain.this.exitcommand)
			{
				// Indication that something is happening
				MidletMain.this.programlist.setTitle("Exiting...");
				
				System.exit(0);
			}
		}
	}
}

