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

import cc.squirreljme.jvm.mle.JarPackageShelf;
import cc.squirreljme.jvm.mle.brackets.JarPackageBracket;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.cldc.io.ResourceInputStream;
import cc.squirreljme.runtime.swm.EntryPoint;
import java.io.IOException;
import java.io.InputStream;
import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Image;
import javax.microedition.swm.ManagerFactory;
import javax.microedition.swm.Suite;
import javax.microedition.swm.Task;
import javax.microedition.swm.TaskStatus;
import net.multiphasicapps.tool.manifest.JavaManifest;

/**
 * Stores the program information which is mapped to what is displayed.
 *
 * @since 2018/11/16
 */
final class __Program__
{
	/** The JAR used. */
	protected final JarPackageBracket jar;
	
	/** The entry point used. */
	protected final EntryPoint entry;
	
	/** The display name of this suite. */
	protected final String displayName;
	
	/** The currently active task. */
	final __ActiveTask__ _activeTask;
	
	/** The icon to show for this program. */
	Image _icon;
	
	/**
	 * @param __jar The JAR the program is in.
	 * @param __man The manifest of the JAR.
	 * @param __activeTask The active task.
	 * @param __entry The entry point.
	 * @throws NullPointerException
	 */
	public __Program__(JarPackageBracket __jar, JavaManifest __man,
		__ActiveTask__ __activeTask, EntryPoint __entry)
		throws NullPointerException
	{
		if (__jar == null || __man == null || __activeTask == null ||
			__entry == null)
			throw new NullPointerException("NARG");
		
		this.jar = __jar;
		this._activeTask = __activeTask;
		this.entry = __entry;
		
		String midName = __man.getMainAttributes().getValue("MIDlet-Name");
		this.displayName = (__entry.isMidlet() ? __entry.name() : midName);
	}
	
	/**
	 * The display image for this suite.
	 *
	 * @return The display image.
	 * @since 2018/11/16
	 */
	public final Image displayImage()
	{
		// Image already known?
		Image rv = this._icon;
		if (rv != null)
			return rv;
		
		// No image is here at all
		String iconRc = this.entry.imageResource();
		if (iconRc == null || iconRc.isEmpty())
			return null;
		
		// Load image from JAR resource
		try (InputStream in = JarPackageShelf.openResource(this.jar, iconRc))
		{
			// No resource exists
			if (in == null)
				return null;
			
			// Load image data
			rv = Image.createImage(in);
		}
		
		// Not a valid image, ignore
		catch (IOException e)
		{
			return null;
		}
		
		// Cache and use
		this._icon = rv;
		return rv;
	}
	
	/**
	 * The display name for this suite.
	 *
	 * @return The display name.
	 * @since 2018/11/16
	 */
	public final String displayName()
	{
		return this.displayName;
	}
	
	/**
	 * Launches this program.
	 *
	 * @since 2018/11/16
	 */
	final void __launch()
	{
		throw Debugging.todo();
		/*
		// Need these
		Suite suite = this.suite;
		String main = this.main;
		
		// Make it so only a single thing can be launched
		__ActiveTask__ activetask = this._activeTask;
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
		
		 */
	}
}

