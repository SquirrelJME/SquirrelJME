// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.kernel.archive;

import net.multiphasicapps.squirreljme.kernel.Kernel;

/**
 * This is the base class for classes which are used to search for archives
 * within the virtual machine.
 *
 * Finders may return archives which are built-in to the executable the JVM
 * runs from, on ROM, or on the filesystem (if one exists).
 *
 * @since 2016/05/18
 */
public abstract class ArchiveFinder
{
	/** The kernel which owns this. */
	protected final Kernel kernel;
	
	/**
	 * Initializes the archive finder.
	 *
	 * @param __k The kernel which owns the finder (may use it to access
	 * internal ROM details or the host filesystem).
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/18
	 */
	public ArchiveFinder(Kernel __k)
		throws NullPointerException
	{
		// Check
		if (__k == null)
			throw new NullPointerException("NARG");
		
		// Set
		kernel = __k;
	}
	
	/**
	 * Returns the name of this archive finder.
	 *
	 * @return The archive finder name.
	 * @since 2016/05/18
	 */
	public abstract String name();
}

