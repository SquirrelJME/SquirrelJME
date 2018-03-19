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

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * This allows for easier writing to the replay output without requiring that
 * {@link IOException} be caught each time.
 *
 * @since 2018/03/19
 */
public final class ReplayOutputStream
{
	/** The stream to write to. */
	protected final DataOutputStream out;
	
	/**
	 * Initializes the output stream.
	 *
	 * @param __out The stream to write to.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/19
	 */
	public ReplayOutputStream(OutputStream __out)
		throws NullPointerException
	{
		if (__out == null)
			throw new NullPointerException("NARG");
		
		this.out = (__out instanceof DataOutputStream ?
			(DataOutputStream)__out : new DataOutputStream(__out));
	}
}

