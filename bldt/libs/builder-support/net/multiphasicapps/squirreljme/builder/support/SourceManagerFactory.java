// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.builder.support;

import java.nio.file.Path

/**
 * This is the factory which is used to create instances of
 * {@code SourceManager} which provides access to specific timespaces
 * depending on the needed context.
 *
 * @since 2017/11/10
 */
public class SourceManagerFactory
{
	/** The root of the source tree. */
	protected final Path root;
	
	/**
	 * Initializes the source manager.
	 *
	 * @param __root The root of the source tree.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/11/14
	 */
	public SourceManagerFactory(Path __root)
		throws NullPointerException
	{
		if (__root == null)
			throw new NullPointerException("NARG");
		
		this.root = __root;
	}
}

