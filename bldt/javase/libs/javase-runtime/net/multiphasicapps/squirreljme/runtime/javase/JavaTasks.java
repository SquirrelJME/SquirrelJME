// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.runtime.javase;

import java.io.IOException;
import java.lang.ref.Reference;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.multiphasicapps.squirreljme.runtime.kernel.Kernel;
import net.multiphasicapps.squirreljme.runtime.kernel.KernelProgram;
import net.multiphasicapps.squirreljme.runtime.kernel.KernelTask;
import net.multiphasicapps.squirreljme.runtime.kernel.KernelTasks;

/**
 * This is the task manager which runs on the Java SE host system.
 *
 * @since 2017/12/27
 */
public class JavaTasks
	extends KernelTasks
{
	/** The command used to launch Java. */
	public static final String JAVA_COMMAND =
		"net.multiphasicapps.squirreljme.runtime.javase.java";
	
	/**
	 * Initializes the task manager.
	 *
	 * @param __rk Owning kernel reference.
	 * @param __st The system task.
	 * @since 2017/12/27
	 */
	public JavaTasks(Reference<Kernel> __rk, KernelTask __st)
	{
		super(__rk, __st);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/12/31
	 */
	@Override
	protected KernelTask accessLaunch(int __id, KernelProgram[] __cp,
		KernelProgram __program, String __mainclass, int __perms,
		Map<String, String> __properties)
		throws NullPointerException
	{
		if (__cp == null || __program == null || __mainclass == null ||
			__properties == null)
			throw new NullPointerException("NARG");
		
		// Determine the real classpath to use when launching the program
		Set<Path> classpath = new LinkedHashSet<>();
		for (KernelProgram kp : __cp)
			classpath.add(((JavaProgram)kp).jarPath());
		
		// Launch the Java command
		List<String> args = new ArrayList<>();
		args.add(System.getProperty(JAVA_COMMAND, "java"));
		
		// Define the classpath
		args.add("-cp");
		StringBuilder sb = new StringBuilder();
		String psep = System.getProperty("path.separator");
		for (Path p : classpath)
		{
			if (sb.length() > 0)
				sb.append(psep);
			sb.append(p.toString());
		}
		args.add(sb.toString());
		
		// Set bootstrap entry point which additionally forces client mode
		// to be used
		args.add("-D" + Main.CLIENT_MAIN + "=" + __mainclass);
		
		// Add all system properties
		for (Map.Entry<String, String> e : __properties.entrySet())
			args.add("-D" + e.getKey() + "=" + e.getValue());
		
		// Use the main of the bootstrap entry
		args.add("net.multiphasicapps.squirreljme.runtime.javase.Main");
		
		// Build process
		try
		{
			ProcessBuilder pb = new ProcessBuilder(args);
			
			// Spit standard error to our actual error so that it is not
			// consumed by our process
			pb.redirectError(ProcessBuilder.Redirect.INHERIT);
			
			// Start it
			return new JavaProcessKernelTask(__id, pb.start());
		}
		
		// {@squirreljme.error AF05 Could not launch the sub-process. (The
		// command line arguments)}
		catch (IOException e)
		{
			throw new RuntimeException(String.format("AF05 %s", args), e);
		}
	}
}

