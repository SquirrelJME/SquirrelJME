// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.io.streamproc;

import java.io.InputStream;
import java.io.IOException;

/**
 * This wraps an {@link InputStream} to {@link StreamInput}.
 *
 * @since 2017/02/24
 */
public class InputStreamInput
	implements StreamInput
{
	/**
	 * Initializes the stream input.
	 *
	 * @param __in The input stream to wrap.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/02/24
	 */
	public InputStreamInput(InputStream __in)
		throws NullPointerException
	{
		// Check
		if (__in == null)
			throw new NullPointerException("NARG");
		
		this.in = __in;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/24
	 */
	@Override
	public abstract int read(byte[] __b, int __o, int __l)
		throws ArrayIndexOutOfBoundsException, IOException,
			NullPointerException
	{
		return this.in.read(__b, __o, __l);
	}
}

