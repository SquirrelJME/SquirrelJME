// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirrelquarrel.util;

import java.io.DataInputStream;
import java.io.InputStream;
import java.io.IOException;

/**
 * This allows for easier reading from the replay input without requiring that
 * {@link IOException} be caught each time.
 *
 * @since 2018/03/19
 */
public final class ReplayInputStream
{
	/** The stream to read from. */
	protected final DataInputStream in;
	
	/**
	 * Initializes the input stream.
	 *
	 * @param __in The stream to read from.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/19
	 */
	public ReplayInputStream(InputStream __in)
		throws NullPointerException
	{
		if (__in == null)
			throw new NullPointerException("NARG");
		
		this.in = (__in instanceof DataInputStream ? (DataInputStream)__in :
			new DataInputStream(__in));
	}
}

