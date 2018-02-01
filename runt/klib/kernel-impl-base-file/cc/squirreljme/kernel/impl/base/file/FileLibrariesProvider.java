// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.kernel.impl.base.file;

import cc.squirreljme.kernel.lib.server.LibrariesProvider;

/**
 * This provides access to installed libraries which have been compiled and
 * stores on the filesystem using the standard SquirrelJME storage areas.
 *
 * @since 2018/01/13
 */
public abstract class FileLibrariesProvider
	extends LibrariesProvider
{
	/** The paths to used to store libraries. */
	protected final StandardPaths paths;
	
	/**
	 * Initializes the file library provider.
	 *
	 * @since 2018/01/03
	 */
	public FileLibrariesProvider()
	{
		this(StandardPaths.DEFAULT);
	}
	
	/**
	 * Initializes the file library provider using the given paths.
	 *
	 * @param __sp The paths to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/01/31
	 */
	public FileLibrariesProvider(StandardPaths __sp)
		throws NullPointerException
	{
		if (__sp == null)
			throw new NullPointerException("NARG");
		
		this.paths = __sp;
		
		throw new todo.TODO();
	}
}

