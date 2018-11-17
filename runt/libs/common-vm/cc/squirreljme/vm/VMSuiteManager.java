// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm;

/**
 * This is the suite manager which represents the.
 *
 * @since 2018/10/26
 */
public interface VMSuiteManager
{
	/**
	 * Lists the names of libraries which are available for usage.
	 *
	 * @return The list of available libraries.
	 * @since 2018/10/26
	 */
	public abstract String[] listLibraryNames();
	
	/**
	 * Loads the specified library by the given string.
	 *
	 * It is recommended that this caches the library internally so that it may
	 * be reused accordingly as such.
	 *
	 * @param __s The name of the library to load.
	 * @return The loaded library.
	 * @since 2018/10/16
	 */
	public abstract SpringClassLibrary loadLibrary(String __s)
		throws NullPointerException;
}

