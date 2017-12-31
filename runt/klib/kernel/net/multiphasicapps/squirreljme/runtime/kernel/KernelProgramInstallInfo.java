// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.runtime.kernel;

import net.multiphasicapps.squirreljme.runtime.midlet.id.Suite;
import net.multiphasicapps.zip.blockreader.ZipBlockReader;

/**
 * This contains installation information for a program which is being
 * installed.
 *
 * @since 2017/12/31
 */
public final class KernelProgramInstallInfo
{
	/** The ZIP to use for installation. */
	protected final ZipBlockReader zip;
	
	/** The suite name of the program. */
	protected final Suite suite;
	
	/** The index of the program. */
	protected final int index;
	
	/** Dependencies of the program. */
	private final KernelProgram[] _depends;
	
	/**
	 * Initializes the installation information.
	 *
	 * @param __deps Program dependencies.
	 * @param __zip The ZIP to be recompiled.
	 * @param __s The suite information.
	 * @param __id The program index.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/12/31
	 */
	public KernelProgramInstallInfo(KernelProgram[] __deps,
		ZipBlockReader __zip, Suite __s, int __id)
		throws NullPointerException
	{
		if (__deps == null || __zip == null || __s == null)
			throw new NullPointerException("NARG");
		
		this._depends = __deps.clone();
		this.zip = __zip;
		this.suite = __s;
		this.index = __id;
	}
	
	/**
	 * Returns the index to use when installing the program.
	 *
	 * @return The program index to use.
	 * @since 2017/12/31
	 */
	public int index()
	{
		return this.index;
	}
	
	/**
	 * Returns the ZIP to be recompiled.
	 *
	 * @return The ZIP to recompile.
	 * @since 2017/12/31
	 */
	public ZipBlockReader zip()
	{
		return this.zip;
	}
}

