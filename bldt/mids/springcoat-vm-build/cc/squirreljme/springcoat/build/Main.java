// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.springcoat.build;

import cc.squirreljme.builder.support.Binary;
import cc.squirreljme.builder.support.ProjectManager;
import cc.squirreljme.builder.support.TimeSpaceType;
import cc.squirreljme.springcoat.vm.SpringClassLoader;

/**
 * Main entry point for the virtual machine which is layered on the build
 * system.
 *
 * @since 2018/09/13
 */
public class Main
{
	/**
	 * Main entry point.
	 *
	 * @param __args Program arguments.
	 * @since 2018/09/13
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
		
		// {@squirreljme.error BA01 No project to launch was specified. The
		// format is project-name or project-name:entry-point-id.}
		if (args.isEmpty())
			throw new IllegalArgumentException("BA01");
		
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
		
		// Setup wrapped libraries
		int numlibs = classpath.length;
		BuildClassLibrary[] libs = new BuildClassLibrary[numlibs];
		for (int i = 0; i < numlibs; i++)
			libs[i] = new BuildClassLibrary(classpath[i]);
		
		// Initialize the class loader
		SpringClassLoader classloader = new SpringClassLoader(libs);
		
		// Initialize the virtual machine with our launch ID
		SpringMachine machine = new SpringMachine(classloader, launchid);
		
		// Run the VM until it terminates
		machine.run();
	}
}

