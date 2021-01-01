// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.swm.launch;

import cc.squirreljme.jvm.mle.JarPackageShelf;
import cc.squirreljme.jvm.mle.TaskShelf;
import cc.squirreljme.jvm.mle.brackets.JarPackageBracket;
import cc.squirreljme.jvm.mle.brackets.TaskBracket;
import cc.squirreljme.jvm.mle.constants.TaskPipeRedirectType;
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
	
	/** The suite information. */
	protected final SuiteInfo info;
	
	/** The JAR this references. */
	protected final JarPackageBracket jar;
	
	/** The entry point used. */
	protected final EntryPoint entryPoint;
	
	/** The library information. */
	private final __Libraries__ _libraries;
	
	/**
	 * Initializes the application.
	 * 
	 * @param __info The JAR information.
	 * @param __jar The JAR to reference.
	 * @param __libs The lazy library initializer.
	 * @param __entryPoint The entry point used.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/12/29
	 */
	Application(SuiteInfo __info, JarPackageBracket __jar,
		__Libraries__ __libs, EntryPoint __entryPoint)
		throws NullPointerException
	{
		if (__info == null || __jar == null || __libs == null ||
			__entryPoint == null)
			throw new NullPointerException("NARG");
		
		this.info = __info;
		this.jar = __jar;
		this.entryPoint = __entryPoint;
		this._libraries = __libs;
	}
	
	/**
	 * Returns the display name of the application.
	 * 
	 * @return The display name of the application.
	 * @since 2020/12/29
	 */
	public String displayName()
	{
		EntryPoint entry = this.entryPoint;
		String result = (entry.isMidlet() ? entry.name() :
			this.info.manifest().getMainAttributes()
				.getValue("MIDlet-Name"));
		
		return (result == null ? entry.entryPoint() : result);
	}
	
	/**
	 * Returns the entry point of the task.
	 * 
	 * @return The entry point that represents this application.
	 * @since 2020/12/29
	 */
	public final EntryPoint entryPoint()
	{
		return this.entryPoint;
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
		String imgRc = this.entryPoint.imageResource();
		if (imgRc != null)
			return JarPackageShelf.openResource(this.jar,
				this.entryPoint.imageResource());
		return null;
	}
	
	/**
	 * Indicates that this should not appear on the launcher.
	 * 
	 * @return If this should not appear on the launcher.
	 * @since 2020/12/29
	 */
	public boolean isNoLauncher()
	{
		return this.info.manifest().getMainAttributes()
			.definesValue(Application._NO_LAUNCHER);
	}
	
	/**
	 * Launches the specified task.
	 * 
	 * @return The bracket for the task.
	 * @since 2020/12/29
	 */
	public TaskBracket launch()
	{
		// Find libraries to base off
		Library[] libraries = this._libraries.matchDependencies(
			this.info.dependencies(), true);
		int numLibs = libraries.length;
		
		// Determine the class path used from this
		JarPackageBracket[] classPath = new JarPackageBracket[numLibs + 1];
		for (int i = 0; i < numLibs; i++)
			classPath[i] = libraries[i].jar;
		classPath[numLibs] = this.jar;
		
		// Have the task launch itself
		EntryPoint entry = this.entryPoint;
		return TaskShelf.start(classPath,
			(entry.isMidlet() ? "javax.microedition.midlet.__MainHandler__" :
				entry.entryPoint()),
			(entry.isMidlet() ? new String[]{entry.entryPoint()} :
				new String[0]),
			new String[0],
			TaskPipeRedirectType.TERMINAL,
			TaskPipeRedirectType.TERMINAL);
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
