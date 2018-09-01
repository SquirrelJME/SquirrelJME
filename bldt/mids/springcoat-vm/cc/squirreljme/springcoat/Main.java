// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.springcoat;

import cc.squirreljme.builder.support.Binary;
import cc.squirreljme.builder.support.ProjectManager;
import cc.squirreljme.builder.support.TimeSpaceType;
import cc.squirreljme.kernel.suiteinfo.EntryPoint;
import cc.squirreljme.kernel.suiteinfo.EntryPoints;
import cc.squirreljme.springcoat.vm.SpringClass;
import cc.squirreljme.springcoat.vm.SpringMachine;
import java.util.ArrayDeque;
import java.util.Queue;

/**
 * Main entry class for the SpringCoat virtual machine, this initializes and
 * runs the virtual machine or runs the compiler.
 *
 * @since 2018/07/29
 */
public class Main
{
	/**
	 * Main entry point.
	 *
	 * @param __args Arguments.
	 * @since 2018/07/29
	 */
	public static void main(String... __args)
	{
		// Copy arguments for processing
		Queue<String> args = new ArrayDeque<>();
		if (__args != null)
			for (String a : __args)
				if (a != null)
					args.add(a);
		
		// Setup project manager
		ProjectManager pm = ProjectManager.fromArguments(args);
		
		// {@squirreljme.error BK01 No project to launch was specified. The
		// format is project-name or project-name:entry-point-id.}
		if (args.isEmpty())
			throw new IllegalArgumentException("BK01");
		
		// Determine the project and launch ID to execute
		String project = args.remove();
		int launchid = 0;
		
		// Check if a launch ID was specified, separate with colon because
		// there could be command line arguments to launch with using the
		// classic entry point. So we cannot really specificy the entry point
		// using a switch as we would need to specially handle it. Not to
		// mention the program may require it.
		int col = project.indexOf(':');
		if (col >= 0)
		{
			launchid = Integer.valueOf(project.substring(col + 1));
			project = project.substring(0, col);
		}
		
		// Get the project and all of its dependencies built which forms
		// the class path
		Binary[] classpath = pm.build(TimeSpaceType.BUILD, project);
		
		// The binary to launch is the final binary specified, get those
		// entry points
		Binary bootbin = classpath[classpath.length - 1];
		EntryPoints entries = new EntryPoints(bootbin.manifest());
		
		// Print entry points out out for debug
		System.err.println("Entry points:");
		int n = entries.size();
		for (int i = 0; i < n; i++)
			System.err.printf("    %d: %s%n", i, entries.get(i));
		
		// Use the first program if the ID is not valid
		if (launchid < 0 || launchid >= n)
			launchid = 0;
		
		// Initialize the machine
		SpringMachine machine = new SpringMachine(classpath);
		
		// Find the entry point
		SpringClass entrycl = machine.locateClass(entries.get(launchid).
			entryPoint().replace('.', '/'));
		
		// Create main thread
		SpringThread mainthread = new SpringThread("main");
		
		throw new todo.TODO();
	}
}

