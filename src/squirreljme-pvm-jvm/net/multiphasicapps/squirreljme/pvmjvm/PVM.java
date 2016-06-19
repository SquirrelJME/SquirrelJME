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
import net.multiphasicapps.sjmepackages.PackageInfo;
import net.multiphasicapps.sjmepackages.PackageList;

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
		"launcher-lui";
	
	/** The list of packages which are available. */
	protected final PackageList packagelist;
	
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
		PackageList plist;
		try
		{
			System.err.print("Loading package lists...");
			this.packagelist = plist = new PackageList(__jd, null);
			System.err.println("Done!");
		}
		
		// {@squirreljme.error CL01 Could not initialize the package list.}
		catch (IOException e)
		{
			throw new RuntimeException("CL01", e);
		}
		
		// Start the line based user interface
		// {@squirreljme.error CL02 The launcher project could not be located.
		// (The project to use for the launcher)}
		PackageInfo luil = plist.get(LAUNCHER_PROJECT);
		if (luil == null)
			throw new RuntimeException(String.format("CL02 %s",
				LAUNCHER_PROJECT));
		createProcess(luil, __args);
	}
	
	/**
	 * Creates a new process in the para-virtual machine using the specified
	 * package and its dependencies and optional dependencies if they are
	 * available.
	 *
	 * @param __info The package to launch.
	 * @param __args The arguments to the program.
	 * @return The newly created process.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/06/19
	 */
	public PVMProcess createProcess(PackageInfo __info, String... __args)
		throws NullPointerException
	{
		// Check
		if (__info == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
}

