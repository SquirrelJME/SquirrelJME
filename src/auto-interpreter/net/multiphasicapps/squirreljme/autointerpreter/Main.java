// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.autointerpreter;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.microedition.lcdui.Display;
import net.multiphasicapps.squirreljme.projects.ProjectGroup;
import net.multiphasicapps.squirreljme.projects.ProjectList;
import net.multiphasicapps.squirreljme.projects.ProjectName;
import net.multiphasicapps.util.sorted.SortedTreeMap;

/**
 * This is the main entry point for the auto-interpreter.
 *
 * @since 2016/10/11
 */
public class Main
	implements Runnable
{
	/** Projects that may be executed. */
	protected final ProjectList projects;
	
	/** The canvas for the main system view. */
	protected final SystemCanvas canvas;
	
	/** The display used. */
	protected final Display display;
	
	/** JAR to namespace mappings, which JARs are available for use. */
	protected final Map<String, RuntimeNamespace> jarnamespaces =
		new SortedTreeMap<>();
	
	/** The excution engine used for executing project code. */
	protected final ExecutionEngine execengine;
	
	/**
	 * Initializes the auto interpreter.
	 *
	 * @param __d A precomposed display to use, may be {@code null} where
	 * one will be auto-selected.
	 * @param __args Arguments to the interpreter.
	 * @since 2016/10/11
	 */
	public Main(Display __d, String... __args)
	{
		// Force to exist
		if (__args == null)
			__args = new String[0];
		
		// Load project list
		ProjectList projects = ProjectList.getGlobalProjectList();
		if (projects == null)
			try
			{
				projects = new ProjectList(
					Paths.get(System.getProperty("user.dir")));
			}
			
			// {@squirreljme.error EO01 Could not load the project list.}
			catch (IOException e)
			{
				throw new RuntimeException("EO01", e);
			}
		this.projects = this.projects;
		
		// Add namespaces for every project that exists
		Map<String, RuntimeNamespace> jarnamespaces = this.jarnamespaces;
		for (Map.Entry<ProjectName, ProjectGroup> e : projects.entrySet())
			jarnamespaces.put(e.getKey().toString(),
				new GroupNamespace(e.getValue()));
		
		// Setup canvas for the interpretive view (to see what goes on)
		SystemCanvas canvas = new SystemCanvas();
		this.canvas = canvas;
		
		// If no display was specified, then choose one automatically
		if (__d == null)
		{
			// {@squirreljme.error EO02 No display is available for usage.}
			Display[] ds = Display.getDisplays(Display.SUPPORTS_INPUT_EVENTS);
			if (ds == null || ds.length <= 0)
				throw new RuntimeException("EO02");
			
			// Use the first
			__d = ds[0];
		}
		
		// Set
		this.display = __d;
		
		// Make the canvas current
		__d.setCurrent(canvas);
		
		// Setup execution engine
		ExecutionEngine execengine = new FastExecutionEngine();
		this.execengine = execengine;
		
		// Load launcher as the main process
		execengine.addProcess(__bootLauncher());
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/11
	 */
	@Override
	public void run()
	{
		// Run the loop forever, until the launcher signals a VM shutdown
		this.execengine.run();
	}
	
	/**
	 * Bootstraps the launcher.
	 *
	 * @return The process for the bootstrapped launcher.
	 * @since 2016/10/11
	 */
	private InterpreterProcess __bootLauncher()
	{
		throw new Error("TODO");
	}
	
	/**
	 * Main entry point.
	 *
	 * @param __args Program arguments.
	 * @since 2016/10/11
	 */
	public static void main(String... __args)
	{
		// Create interpreter engine
		Main m = new Main(null, __args);
		m.run();
	}
}

