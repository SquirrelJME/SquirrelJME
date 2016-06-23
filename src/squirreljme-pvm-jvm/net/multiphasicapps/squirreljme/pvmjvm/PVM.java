// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.pvmjvm;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import net.multiphasicapps.squirreljme.java.symbols.ClassLoaderNameSymbol;
import net.multiphasicapps.squirreljme.java.symbols.ClassNameSymbol;
import net.multiphasicapps.sjmepackages.PackageInfo;
import net.multiphasicapps.sjmepackages.PackageList;
import net.multiphasicapps.sjmepackages.PackageName;
import net.multiphasicapps.squirreljme.java.cp.ClassPath;
import net.multiphasicapps.squirreljme.java.cp.ClassUnit;
import net.multiphasicapps.squirreljme.java.cp.ClassUnitProvider;
import net.multiphasicapps.squirreljme.java.cp.jar.fs.FSJarClassUnitProvider;

/**
 * This is the primary paravirtual machine controller, it bridges with the
 * host JVM's reflection libraries and other such things to provide the
 * virtualized environment.
 *
 * @since 2016/06/16
 */
public class PVM
{
	/** The launcher to use for the user interface. */
	public static final String LAUNCHER_PROJECT =
		"launcher-lui.jar";
	
	/** The provider for class units. */
	protected final ClassUnitProvider cup;
	
	/** Processes that currently exist. */
	private final Map<Integer, PVMProcess> _processes =
		new TreeMap<>();
	
	/** Lock on the next process ID. */
	private final Object _nextpidlock =
		new Object();
	
	/** The next process ID to use. */
	private volatile int _nextpid;
	
	/**
	 * Initializes the paravirtual machine.
	 *
	 * @param __jd The path to the directory containing all of the system
	 * JARs.
	 * @param __args Arguments to pass to the launcher.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/06/16
	 */
	public PVM(Path __jd, String... __args)
		throws NullPointerException
	{
		// Check
		if (__jd == null)
			throw new NullPointerException("NARG");
		
		// Must always exist
		if (__args == null)
			__args = new String[0];
		
		// Setup package list
		ClassUnitProvider cup = new FSJarClassUnitProvider(__jd);
		this.cup = cup;
		
		// Start the line based user interface
		// {@squirreljme.error CL02 The launcher JAR could not be located.
		// (The JAR to use for the launcher)}
		ClassPath cp = cup.getStandardClassPath(LAUNCHER_PROJECT);
		
		// Get the main class unit
		ClassUnit lcu = cp.locateUnit(LAUNCHER_PROJECT);
		if (lcu == null)
			throw new RuntimeException(String.format("CL02 %s",
				LAUNCHER_PROJECT));
		
		// Create process and pass all input arguments to it
		createProcess(cp, lcu.mainClass(), (String[])__args);
	}
	
	/**
	 * Creates a new process in the para-virtual machine using the specified
	 * package and its dependencies and optional dependencies if they are
	 * available.
	 *
	 * @param __cp The class path to use.
	 * @param __main The main class.
	 * @param __args The arguments to the program.
	 * @return The newly created process.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/06/19
	 */
	public final PVMProcess createProcess(ClassPath __cp,
		ClassNameSymbol __main, String... __args)
		throws IllegalArgumentException, NullPointerException
	{
		// Check
		if (__main == null || __cp == null)
			throw new NullPointerException("NARG");
		
		// Determine the pid of the process
		int pid;
		synchronized (this._nextpidlock)
		{
			pid = this._nextpid++;
		}
		
		// Setup new process
		PVMProcess rv = new PVMProcess(this, __cp, pid);
		
		Map<Integer, PVMProcess> processes = this._processes;
		try
		{
			// Add to process map
			synchronized (processes)
			{
				processes.put(pid, rv);
			}
		
			// Create initial thread
			PVMThread thr = rv.createThread(__main, (Object[])__args);
			thr.start();
		}
		
		// Failed, remove from the process map
		catch (RuntimeException|Error e)
		{
			synchronized (processes)
			{
				processes.remove(pid);
			}
			
			// Rethrow
			throw e;
		}
		
		// Return it
		return rv;
	}
}

