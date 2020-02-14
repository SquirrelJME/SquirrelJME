// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classfile;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * This represents a single attribute which exists within a class.
 *
 * @since 2018/05/14
 */
public final class Attribute
{
	/** The attribute name. */
	protected final String name;
	
	/** The attribute data. */
	private final byte[] _data;
	
	/**
	 * Initializes the attribute.
	 *
	 * @param __name The name of the attribute.
	 * @param __b The data that makes up the attribute.
	 * @param __o The offset to the attribute data.
	 * @param __l The number of bytes in the attribute.
	 * @throws IndexOutOfBoundsException If the offset and/or length are
	 * negative or exceed the array bounds.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/05/15
	 */
	public Attribute(String __name, byte[] __b, int __o, int __l)
		throws IndexOutOfBoundsException, NullPointerException
	{
		if (__name == null || __b == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0 || (__o + __l) > __b.length)
			throw new ArrayIndexOutOfBoundsException("IOOB");
		
		this.name = __name;
		
		// Copy the array
		byte[] clone = new byte[__l];
		System.arraycopy(__b, __o, clone, 0, __l);
		this._data = clone;
	}
	
	/**
	 * Returns a copy of the attribute bytes.
	 *
	 * @return The attribute bytes.
	 * @since 2018/05/15
	 */
	public final byte[] bytes()
	{
		return this._data.clone();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/05/15
	 */
	@Override
	public final boolean equals(Object __o)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/05/15
	 */
	@Override
	public final int hashCode()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Returns the name of the attribute.
	 *
	 * @return The attribute name.
	 * @since 2018/05/14
	 */
	public final String name()
	{
		return this.name;
	}
	
	/**
	 * Opens the attribute as a stream of bytes.
	 *
	 * @return A stream over the bytes of the attribute.
	 * @since 2018/05/14
	 */
	public final DataInputStream open()
	{
		return new DataInputStream(new ByteArrayInputStream(this._data));
	}
	
	/**
	 * Returns the size of the attribute.
	 *
	 * @return The attribute size.
	 * @since 2018/05/14
	 */
	public final int size()
	{
		return this._data.length;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/05/15
	 */
	@Override
	public final String toString()
	{
		throw new todo.TODO();
	}
}

