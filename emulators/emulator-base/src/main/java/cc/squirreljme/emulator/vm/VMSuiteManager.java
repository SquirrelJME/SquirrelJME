// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.emulator.vm;

import cc.squirreljme.vm.VMClassLibrary;

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
	String[] listLibraryNames();
	
	/**
	 * Loads the specified library by the given string.
	 *
	 * It is recommended that this caches the library internally so that it may
	 * be reused accordingly as such.
	 *
	 * @param __s The name of the library to load.
	 * @return The loaded library, or {@code null} if it does not exist.
	 * @since 2018/10/16
	 */
	VMClassLibrary loadLibrary(String __s)
		throws NullPointerException;
}

