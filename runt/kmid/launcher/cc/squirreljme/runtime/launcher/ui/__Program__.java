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
import javax.microedition.lcdui.Image;
import javax.microedition.swm.ManagerFactory;
import javax.microedition.swm.Suite;
import javax.microedition.swm.Task;
import javax.microedition.swm.TaskStatus;

/**
 * Stores the program information which is mapped to what is displayed.
 *
 * @since 2018/11/16
 */
final class __Program__
{
	/** The suite that is used. */
	protected final Suite suite;
	
	/** The main entry point. */
	protected final String main;
	
	/** The display name of this suite. */
	protected final String displayname;
	
	/** The active task. */
	final __ActiveTask__ _activetask;
	
	/**
	 * Initializes the program.
	 *
	 * @param __suite The suite used.
	 * @param __main The main class.
	 * @param __dn The display name of this suite.
	 * @param __at The active task.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/11/16
	 */
	__Program__(Suite __suite, String __main, String __dn,
		__ActiveTask__ __at)
		throws NullPointerException
	{
		if (__suite == null || __main == null || __at == null)
			throw new NullPointerException("NARG");
		
		this.suite = __suite;
		this.main = __main;
		this.displayname = (__dn != null ? __dn :
			__suite.getName() + " " + __main);
		this._activetask = __at;
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
		// Need these
		Suite suite = this.suite;
		String main = this.main;
		
		// Make it so only a single thing can be launched
		__ActiveTask__ activetask = this._activetask;
		synchronized (activetask)
		{
			// Do not start another task until the current one has finished
			Task oldtask = activetask._task;
			if (oldtask != null)
			{
				TaskStatus status = oldtask.getStatus();
				if (status == TaskStatus.RUNNING ||
					status == TaskStatus.STARTING)
				{
					todo.DEBUG.note("Other task has not finished yet!");
					return;
				}
				
				// Not running, so we can forget it
				activetask._task = null;
			}
			
			// Start the task
			try
			{
				Task task = ManagerFactory.getTaskManager().
					startTask(suite, main);
				activetask._task = task;
			}
			
			// Could not launch so, oh well!
			catch (IllegalArgumentException|IllegalStateException e)
			{
				// Debug to the system console
				e.printStackTrace();
				
				// Then pop up a nasty message!
				String msg = e.getMessage();
				MidletMain._MAIN_DISPLAY.setCurrent(
					new Alert("Oopsie!", (msg == null ? String.format(
						"Could not launch %s:%s.", suite, main): msg),
						null, AlertType.ERROR));
			}
			
			// Setup timer to recover our display on termination
			MidletMain._TIMER.schedule(new __ReControlTask__(
				MidletMain._MAIN_DISPLAY, activetask), 500, 500);
		}
	}
}

