// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * This is a {@link ByteArrayOutputStream} which does not expose the internal
 * buffer field via the {@link #writeTo(OutputStream)} method.
 *
 * @since 2018/01/15
 */
public final class SecureByteArrayOutputStream
	extends ByteArrayOutputStream
{
	/**
	 * Initializes using the default capacity.
	 *
	 * @since 2018/01/15
	 */
	public SecureByteArrayOutputStream()
	{
		super();
	}
	
	/**
	 * Initializes using the specified capacity.
	 *
	 * @param __n The number of bytes to store.
	 * @since 2018/01/15
	 */
	public SecureByteArrayOutputStream(int __n)
	{
		super(__n);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/01/15
	 */
	@Override
	public void writeTo(OutputStream __os)
		throws IOException, NullPointerException
	{
		if (__os == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
}

