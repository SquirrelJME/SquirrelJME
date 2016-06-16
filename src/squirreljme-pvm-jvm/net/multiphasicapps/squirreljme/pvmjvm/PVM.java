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
	/** The list of packages which are available. */
	protected final PackageList packagelist;
	
	/**
	 * Initializes the paravirtual machine.
	 *
	 * @param __jd The path to the directory containing all of the system
	 * JARs.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/06/16
	 */
	public PVM(Path __jd)
		throws NullPointerException
	{
		// Check
		if (__jd == null)
			throw new NullPointerException("NARG");
		
		// Setup package list
		try
		{
			this.packagelist = new PackageList(__jd, null);
		}
		
		// {@squirreljme.error CL01 Could not initialize the package list.}
		catch (IOException e)
		{
			throw new RuntimeException("CL01", e);
		}
	}
}

