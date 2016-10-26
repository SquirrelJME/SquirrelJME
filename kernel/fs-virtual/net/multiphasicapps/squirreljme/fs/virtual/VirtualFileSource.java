// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.fs.virtual;

/**
 * This is a file source which is used to provide access to actual files in
 * a virtual manner.
 *
 * @since 2016/09/05
 */
public abstract class VirtualFileSource
{
	/** File source lock. */
	protected final Object lock =
		new Object();
	
	/**
	 * Initializes the base file source.
	 *
	 * @since 2016/09/05
	 */
	public VirtualFileSource()
	{
	}
}

