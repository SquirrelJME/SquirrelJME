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
import cc.squirreljme.kernel.lib.server.LibrariesProviderFactory;

/**
 * This is used as a base for initializing the file library provider.
 *
 * @since 2018/01/31
 */
public abstract class FileLibrariesProviderFactory
	extends LibrariesProviderFactory
{
	/**
	 * Creates an instance of the provider which is implementation
	 * dependent.
	 *
	 * @return The provider instance.
	 * @since 2018/01/31
	 */
	protected abstract FileLibrariesProvider createFileLibraries();
	
	/**
	 * {@inheritDoc}
	 * @since 2018/01/31
	 */
	@Override
	protected final LibrariesProvider createLibraries()
	{
		return this.createFileLibraries();
	}
}

