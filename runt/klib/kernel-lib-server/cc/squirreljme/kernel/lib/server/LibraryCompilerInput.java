// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.kernel.lib.server;

import cc.squirreljme.kernel.suiteinfo.SuiteInfo;
import cc.squirreljme.runtime.cldc.library.Library;
import net.multiphasicapps.zip.blockreader.ZipBlockReader;

/**
 * This class is used as input for the compiler and describes the class which
 * is being compiled.
 *
 * @since 2018/02/11
 */
public final class LibraryCompilerInput
{
	/** Library information. */
	protected final SuiteInfo info;
	
	/** Zip for class input. */
	protected final ZipBlockReader zip;
	
	/** The library index. */
	protected final int index;
	
	/** Dependencies. */
	private final Library[] _depends;
	
	/**
	 * Initializes the input.
	 *
	 * @param __info The library information.
	 * @param __zip The ZIP containing the library classes.
	 * @param __depends The dependencies of the library.
	 * @param __dx The library index.
	 * @return The compiled library.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/02/11
	 */
	LibraryCompilerInput(SuiteInfo __info,
		ZipBlockReader __zip, Library[] __depends, int __dx)
		throws NullPointerException
	{
		if (__info == null || __zip == null || __depends == null)
			throw new NullPointerException("NARG");
		
		this.info = __info;
		this.zip = __zip;
		this.index = __dx;
		this._depends = __depends.clone();
	}
	
	/**
	 * The library index.
	 *
	 * @return The library index.
	 * @since 2018/02/11
	 */
	public final int index()
	{
		return this.index;
	}
	
	/**
	 * Returns the ZIP of the JAR to be compiled.
	 *
	 * @return The JAR to compile.
	 * @since 2018/02/11
	 */
	public final ZipBlockReader zip()
	{
		return this.zip;
	}
}

