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

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;
import javax.microedition.swm.ManagerFactory;
import javax.microedition.swm.Suite;
import javax.microedition.swm.SuiteManager;
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
		throw new todo.TODO();
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
		
		// Initial load of all the programs that are available, more
		// efficient to refresh before it is fully displayed
		this.refresh();
		
		// Display the program list
		disp.setCurrent(programlist);
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
		
		/**
		 * Initializes the program.
		 *
		 * @param __suite The suite used.
		 * @param __main The main class.
		 * @throws NullPointerException On null arguments.
		 * @since 2018/11/16
		 */
		__Program__(Suite __suite, String __main)
			throws NullPointerException
		{
			if (__suite == null || __main == null)
				throw new NullPointerException("NARG");
			
			this.suite = __suite;
			this.main = __main;
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

