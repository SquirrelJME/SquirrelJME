// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.kernel.impl.base.file;

import net.multiphasicapps.squirreljme.kernel.lib.server.LibrariesProvider;

/**
 * This provides access to installed libraries which have been compiled and
 * stores on the filesystem using the standard SquirrelJME storage areas.
 *
 * @since 2018/01/13
 */
public abstract class FileLibrariesProvider
	extends LibrariesProvider
{
	/**
	 * Initializes the file library provider.
	 *
	 * @since 2018/01/03
	 */
	public FileLibrariesProvider()
	{
	}
}

