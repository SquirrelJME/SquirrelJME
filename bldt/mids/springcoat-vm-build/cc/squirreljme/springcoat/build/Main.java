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
import cc.squirreljme.runtime.cldc.asm.TaskAccess;
import cc.squirreljme.runtime.cldc.lang.GuestDepth;
import cc.squirreljme.vm.springcoat.SpringClassLoader;
import cc.squirreljme.vm.springcoat.SpringFatalException;
import cc.squirreljme.vm.springcoat.SpringMachine;
import cc.squirreljme.vm.springcoat.SpringMachineExitException;
import cc.squirreljme.vm.springcoat.SpringTaskManager;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayDeque;
import java.util.Calendar;
import java.util.LinkedHashSet;
import java.util.Queue;
import java.util.Set;
import net.multiphasicapps.profiler.ProfilerSnapshot;

/**
 * Main entry point for the virtual machine which is layered on the build
 * system.
 *
 * @since 2018/09/13
 */
public class Main
{
	/** Fixed projects to always include. */
	private static final String[] _FIXED_PROJECTS =
		new String[]
		{
			"midp-lcdui",
			"meep-rms",
			"media-api",
		};
	
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
		
		// {@squirreljme.error BA03 No project to launch was specified. The
		// format is project-name or project-name:entry-point-id.}
		if (args.isEmpty())
			throw new IllegalArgumentException("BA03");
		
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
		
		// Include some dependenices to always be included so that SpringCoat
		// can run a few more programs rather than what is in the base library
		Set<Binary> xclasspath = new LinkedHashSet<>();
		for (String fp : _FIXED_PROJECTS)
			for (Binary b : pm.build(TimeSpaceType.BUILD, fp))
				xclasspath.add(b);
		
		// Get the project and all of its dependencies built which forms
		// the class path
		Binary[] vclasspath = pm.build(TimeSpaceType.TEST, project);
		
		// The boot entry always must be last
		Binary bootp = vclasspath[vclasspath.length - 1];
		
		// Merge the sets of classpaths
		Set<Binary> finalclasspath = new LinkedHashSet<>();
		for (Binary b : xclasspath)
			finalclasspath.add(b);
		for (Binary b : vclasspath)
			finalclasspath.add(b);
		
		// Remove and re-add the boot entry so it is always last
		finalclasspath.remove(bootp);
		finalclasspath.add(bootp);
		
		// Build into final array
		Binary[] classpath = finalclasspath.<Binary>toArray(
			new Binary[finalclasspath.size()]);
		
		// Setup wrapped libraries
		int numlibs = classpath.length;
		BuildClassLibrary[] libs = new BuildClassLibrary[numlibs];
		for (int i = 0; i < numlibs; i++)
			libs[i] = new BuildClassLibrary(classpath[i]);
		
		// Initialize the class loader
		SpringClassLoader classloader = new SpringClassLoader(libs);
		
		// Profiled class information
		ProfilerSnapshot profiler = new ProfilerSnapshot();
		
		// Initialize the virtual machine with our launch ID
		BuildSuiteManager bm;
		SpringMachine machine = new SpringMachine(
			(bm = new BuildSuiteManager(pm, TimeSpaceType.TEST)),
			classloader, new SpringTaskManager(bm, profiler), launchid,
			GuestDepth.guestDepth() + 1, profiler,
			args.<String>toArray(new String[args.size()]));
		
		// Run the VM until it terminates
		int exitcode = -1;
		try
		{
			machine.run();
			exitcode = 0;
		}
		
		// Exiting with some given code
		catch (SpringMachineExitException e)
		{
			exitcode = e.code();
		}
		
		// Ignore these exceptions, just fatal exit
		catch (SpringFatalException e)
		{
			exitcode = TaskAccess.EXIT_CODE_FATAL_EXCEPTION;
		}
		
		// Dump the profiler snapshot somewhere
		finally
		{
			// Dump to file
			try
			{
				// Create temporary file
				Calendar now = Calendar.getInstance();
				Path temp = Files.createTempFile(String.format(
					"springcoat-%TF_%TH-%TM-%TS-", now, now, now, now),
					".nps");
				
				// Write snapshot to this file
				try (OutputStream os = Files.newOutputStream(temp,
					StandardOpenOption.WRITE, StandardOpenOption.CREATE))
				{
					profiler.writeTo(os);
				}
			}
			
			// Ignore
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		
		// Exit with our given code
		System.exit(exitcode);
	}
}

