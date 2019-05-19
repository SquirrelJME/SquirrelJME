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

import cc.squirreljme.runtime.cldc.io.ResourceInputStream;
import java.io.InputStream;
import java.io.IOException;
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
	
	/** The name of the suite. */
	protected final String suitename;
	
	/** The main entry point. */
	protected final String main;
	
	/** The display name of this suite. */
	protected final String displayname;
	
	/** The SquirrelJME name. */
	protected final String squirreljmename;
	
	/** The name of the JAR (SquirrelJME specific). */
	protected final String jarfile;
	
	/** The icon resource to use. */
	protected final String iconrc;
	
	/** The active task. */
	final __ActiveTask__ _activetask;
	
	/** The icon to show for this program. */
	Image _icon;
	
	/**
	 * Initializes the program.
	 *
	 * @param __suite The suite used.
	 * @param __main The main class.
	 * @param __dn The display name of this suite.
	 * @param __at The active task.
	 * @param __iconrc The icon resource used, may be {@code null}.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/11/16
	 */
	__Program__(Suite __suite, String __main, String __dn,
		__ActiveTask__ __at, String __iconrc)
		throws NullPointerException
	{
		if (__suite == null || __main == null || __at == null)
			throw new NullPointerException("NARG");
		
		this.suite = __suite;
		this.main = __main;
		this.iconrc = __iconrc;
		
		String suitename = __suite.getName();
		this.suitename = suitename;
		
		String displayname;
		this.displayname = (displayname = (__dn != null ? __dn :
			suitename + " " + __main));
		this._activetask = __at;
		
		// Try to get the internal project name for SquirrelJME, this is used
		// for quick launching
		String sjn = __suite.getAttributeValue(
			"X-SquirrelJME-InternalProjectName");
		if (sjn == null)
		{
			// Only add normal characters
			StringBuilder sb = new StringBuilder();
			for (int i = 0, n = displayname.length(); i < n; i++)
			{
				char c = displayname.charAt(i);
				if (Character.isDigit(c) || Character.isLowerCase(c) ||
					Character.isUpperCase(c))
					sb.append(c);
			}
			
			sjn = sb.toString();
		}
		
		// SquirrelJME special name
		String squirreljmename = sjn.toLowerCase();
		this.squirreljmename = squirreljmename;
		
		// SquirrelJME specific name for the JAR file this belongs to,
		// note that this is only valid within SquirrelJME itself as the
		// property is set from the launcher
		String jarfile = __suite.getAttributeValue("x-squirreljme-jarfile");
		this.jarfile = (jarfile == null ? squirreljmename : jarfile);
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
		String iconrc = this.iconrc;
		if (iconrc == null)
			return null;
		
		// Load image from JAR resource
		try (InputStream in = ResourceInputStream.open(this.jarfile, iconrc))
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

