// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.kernel.archive.fs;

import net.multiphasicapps.squirreljme.kernel.archive.Archive;
import net.multiphasicapps.squirreljme.kernel.archive.ArchiveFinder;
import net.multiphasicapps.squirreljme.kernel.Kernel;

/**
 * This contains the archive finder which supports JAR files and utilizes
 * the host filesystem to load them.
 *
 * @since 2016/05/18
 */
public class FSArchiveFinder
	extends ArchiveFinder
{
	/**
	 * Initializes the file system based archive finder.
	 *
	 * @param __k The owning kernel for filesystem access.
	 * @since 2016/05/18
	 */
	public FSArchiveFinder(Kernel __k)
	{
		super(__k);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/18
	 */
	@Override
	public String name()
	{
		return "File System";
	}
}

