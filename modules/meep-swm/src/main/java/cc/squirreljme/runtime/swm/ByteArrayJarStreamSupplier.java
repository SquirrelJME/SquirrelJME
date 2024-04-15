// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.swm;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * This class provides a stream of a JAR file through a byte array.
 *
 * @since 2017/12/28
 */
public final class ByteArrayJarStreamSupplier
	implements JarStreamSupplier
{
	/** The offset. */
	protected final int offset;
	
	/** The length. */
	protected final int length;
	
	/** The byte array. */
	private final byte[] _data;
	
	/**
	 * Initializes the stream supplier for a simple byte array.
	 *
	 * @param __b The byte array to wrap.
	 * @param __o The offset into the array.
	 * @param __l The number of bytes to read.
	 * @throws ArrayIndexOutOfBoundsException If the offset and/or length
	 * are negative or exceed the array bounds.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/12/28
	 */
	public ByteArrayJarStreamSupplier(byte[] __b, int __o, int __l)
		throws ArrayIndexOutOfBoundsException, NullPointerException
	{
		if (__b == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0 || (__o + __l) < 0 || (__o + __l) > __b.length)
			throw new ArrayIndexOutOfBoundsException("IOOB");
		
		this._data = __b;
		this.offset = __o;
		this.length = __l;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/12/28
	 */
	@Override
	public InputStream get()
	{
		return new ByteArrayInputStream(this._data, this.offset, this.length);
	}
}

