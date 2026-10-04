// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.io;

import java.io.OutputStream;

/**
 * This is an output stream which does nothing.
 *
 * @since 2019/06/30
 */
public final class NullOutputStream
	extends OutputStream
{
	/**
	 * {@inheritDoc}
	 * @since 2019/06/30
	 */
	@Override
	public final void close()
	{
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/06/30
	 */
	@Override
	public final void flush()
	{
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/06/30
	 */
	@Override
	public final void write(int __b)
	{
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/06/30
	 */
	@Override
	public final void write(byte[] __b)
	{
		if (__b == null)
			throw new NullPointerException("NARG");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/06/30
	 */
	@Override
	public final void write(byte[] __b, int __o, int __l)
	{
		if (__b == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0 || (__o + __l) < 0 || (__o + __l) > __b.length)
			throw new IndexOutOfBoundsException("IOOB");
	}
}

