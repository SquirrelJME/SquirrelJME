// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.builder.support.vm;

import cc.squirreljme.builder.support.Binary;
import cc.squirreljme.builder.support.ProjectManager;
import cc.squirreljme.builder.support.TimeSpaceType;
import cc.squirreljme.vm.VirtualMachine;
import cc.squirreljme.vm.VMFactory;
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
public class VMMain
{
	/**
	 * Main entry point using the given project manager.
	 *
	 * @param __pm The project manager.
	 * @param __pn The project to be launched.
	 * @param __args Program arguments.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/12/22
	 */
	public static void main(ProjectManager __pm, String __pn, String... __args)
		throws NullPointerException
	{
		if (__pm == null || __pn == null)
			throw new NullPointerException("NARG");
			
		// Copy arguments for processing
		Queue<String> args = new ArrayDeque<>();
		if (__args != null)
			for (String a : __args)
				if (a != null)
					args.add(a);
		
		// Determine the project and launch ID to execute
		String project = __pn;
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
		Set<Binary> xclasspath = new LinkedHashSet<>();
		Binary[] vclasspath = __pm.build(project);
		
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
		
		// Need to convert to array because we use direct index references
		// on the array
		Binary[] fcpa = finalclasspath.<Binary>toArray(
			new Binary[finalclasspath.size()]);
		
		// Build class references
		int numlibs = fcpa.length;
		String[] classpath = new String[numlibs];
		for (int i = 0; i < numlibs; i++)
			classpath[i] = fcpa[i].name().toString();
		
		// Profiled class information
		ProfilerSnapshot profiler = new ProfilerSnapshot();
		
		// Initialize the virtual machine with our launch ID
		VirtualMachine machine = VMFactory.main(null, profiler,
			new BuildSuiteManager(__pm), classpath,
			null, launchid, -1, null,
			args.<String>toArray(new String[args.size()]));
		
		// Run the VM until it terminates
		int exitcode = -1;
		try
		{
			exitcode = machine.runVm();
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
					"squirreljme-vm-%TF_%TH-%TM-%TS-", now, now, now, now),
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
		
		// Exit with our given code, we cannot exit before the finally
		// because then the profiler would never be generatedg
		System.exit(exitcode);
	}
	
	/**
	 * Main entry point (for old compatibility).
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
		
		// Forward
		VMMain.main(pm, args.remove(),
			args.<String>toArray(new String[args.size()]));
	}
}

