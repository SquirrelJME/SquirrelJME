// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.swm.launch;

import cc.squirreljme.jvm.mle.brackets.JarPackageBracket;
import cc.squirreljme.jvm.mle.brackets.TaskBracket;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.swm.EntryPoint;
import cc.squirreljme.runtime.swm.SuiteInfo;
import java.io.InputStream;

/**
 * Represents a single application that can be launched.
 *
 * @since 2020/12/28
 */
public final class Application
{
	/** Manifest property for appearing on the launcher. */
	private static final String _NO_LAUNCHER =
		"X-SquirrelJME-NoLauncher";
	
	/**
	 * Initializes the application.
	 * 
	 * @param __info The JAR information.
	 * @param __jar The JAR to reference.
	 * @param __libs The lazy library initializer.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/12/29
	 */
	Application(SuiteInfo __info, JarPackageBracket __jar,
		__Libraries__ __libs)
		throws NullPointerException
	{
		throw Debugging.todo();
	}
	
	/**
	 * Returns the display name of the application.
	 * 
	 * @return The display name of the application.
	 * @since 2020/12/29
	 */
	public String displayName()
	{
		throw Debugging.todo();
	}
	
	/**
	 * Returns the entry point of the task.
	 * 
	 * @return The entry point that represents this application.
	 * @since 2020/12/29
	 */
	public final EntryPoint entryPoint()
	{
		throw Debugging.todo();
	}
	
	/**
	 * Returns the stream to the application icon data.
	 * 
	 * @return The stream for the application data or {@code null} if there is
	 * no icon.
	 * @since 2020/12/29
	 */
	public InputStream iconStream()
	{
		throw Debugging.todo();
	}
	
	/**
	 * Indicates that this should not appear on the launcher.
	 * 
	 * @return If this should not appear on the launcher.
	 * @since 2020/12/29
	 */
	public boolean isNoLauncher()
	{
		throw Debugging.todo();
	}
	
	/**
	 * Launches the specified task.
	 * 
	 * @return The bracket for the task.
	 * @since 2020/12/29
	 */
	public TaskBracket launch()
	{
		throw Debugging.todo();
	}
	
	/**
	 * Returns the SquirrelJME name of the application.
	 * 
	 * @return The SquirrelJME name of the application.
	 * @since 2020/12/29
	 */
	public String squirrelJMEName()
	{
		throw Debugging.todo();
	}
}
