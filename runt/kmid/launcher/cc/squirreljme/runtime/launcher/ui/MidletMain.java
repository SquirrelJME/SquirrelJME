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

/**
 * This is the main midlet for the LCDUI based launcher interface.
 *
 * @since 2016/10/11
 */
public class MidletMain
	extends MIDlet
{
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
		programlist.setTitle("Loading...");
		
		// Go through all of the available application suites and build the
		// program list
		int foundcount = 0;
		ArrayList<__Program__> programs = new ArrayList<>();
		for (Suite suite : ManagerFactory.getSuiteManager().getSuites(
			SuiteType.APPLICATION))
		{
			// Since we need the program name AND the entry point we need
			// to decode the parts that make it up!
			for (int i = 0; i >= 1; i++)
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
				programs.add(new __Program__(suite, main, title));
				
				// Say it was found via the title
				programlist.setTitle(String.format(
					"Loading (%d Found)...", ++foundcount));
			}
		}
		
		// Indicate that the program list is being built
		programlist.setTitle(String.format(
			"Building List (%d Found)...", ++foundcount));
		
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
		while (!programlist.isShown())
		{
			// Do the refresh
			this.refresh();
			
			// Yield thread to give another a chance
			Thread.yield();
		}
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
				// Launch this program
				MidletMain.this._programs[((List)__d).getSelectedIndex()].
					__launch();
			}
			
			// Exiting the VM?
			else if (__c == MidletMain.this.exitcommand)
			{
				System.exit(0);
			}
		}
	}
	
	/**
	 * Stores the program information which is mapped to what is displayed.
	 *
	 * @since 2018/11/16
	 */
	private static final class __Program__
	{
		/** The suite that is used. */
		protected final Suite suite;
		
		/** The main entry point. */
		protected final String main;
		
		/** The display name of this suite. */
		protected final String displayname;
		
		/**
		 * Initializes the program.
		 *
		 * @param __suite The suite used.
		 * @param __main The main class.
		 * @param __dn The display name of this suite.
		 * @throws NullPointerException On null arguments.
		 * @since 2018/11/16
		 */
		__Program__(Suite __suite, String __main, String __dn)
			throws NullPointerException
		{
			if (__suite == null || __main == null)
				throw new NullPointerException("NARG");
			
			this.suite = __suite;
			this.main = __main;
			this.displayname = (__dn != null ? __dn :
				__suite.getName() + " " + __main);
		}
		
		/**
		 * The display image for this suite.
		 *
		 * @return The display image.
		 * @since 2018/11/16
		 */
		public final Image displayImage()
		{
			// Not currently implemented
			todo.TODO.note("Implement launch icon display.");
			return null;
		}
		
		/**
		 * The display name for this suite.
		 *
		 * @return The display name.
		 * @since 2018/11/16
		 */
		public final String displayName()
		{
			return this.displayname;
		}
		
		/**
		 * Launches this program.
		 *
		 * @since 2018/11/16
		 */
		final void __launch()
		{
			Suite suite = this.suite;
			String main = this.main;
			
			// Start the task
			try
			{
				Task task = ManagerFactory.getTaskManager().
					startTask(suite, main);
			}
			
			// Could not launch so, oh well!
			catch (IllegalArgumentException|IllegalStateException e)
			{
				// Debug to the system console
				e.printStackTrace();
				
				// Then pop up a nasty message!
				String msg = e.getMessage();
				_MAIN_DISPLAY.setCurrent(
					new Alert("Oopsie!", (msg == null ? String.format(
						"Could not launch %s:%s.", suite, main): msg),
						null, AlertType.ERROR));
			}
		}
	}
}

