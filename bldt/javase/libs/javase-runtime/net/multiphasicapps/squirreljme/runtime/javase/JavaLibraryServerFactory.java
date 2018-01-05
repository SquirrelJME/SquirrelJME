// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.runtime.javase;

import net.multiphasicapps.squirreljme.kernel.lib.LibraryServer;
import net.multiphasicapps.squirreljme.kernel.lib.LibraryServerFactory;

/**
 * This allows the Java kernel to manage libraries as needed.
 *
 * @since 2018/01/05
 */
public class JavaLibraryServerFactory
	extends LibraryServerFactory
{
	/**
	 * {@inheritDoc}
	 * @since 2018/01/05
	 */
	@Override
	protected LibraryServer createLibraryServer()
	{
		return new JavaLibraryServer();
	}
}

