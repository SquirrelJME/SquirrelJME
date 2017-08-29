// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.bin;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import net.multiphasicapps.util.datadeque.ByteDeque;

/**
 * This class is used to generate temporary fragments which are then output
 * to fragments which are then placed into sections.
 *
 * @see TemporaryFragment
 * @since 2017/08/24
 */
public class TemporaryFragmentBuilder
{
	/** Deque for the bytes within the section. */
	private final ByteDeque _bytes =
		new ByteDeque();
	
	/** Buffer for multi-byte data. */
	private final byte[] _buf =
		new byte[8];
	
	/**
	 * Appends a single non-dynamic byte to the output section.
	 *
	 * @param __v The byte to add.
	 * @throws IllegalStateException If the fragment has been finished.
	 * @since 2017/06/27
	 */
	public final void append(byte __v)
		throws IllegalStateException
	{
		this._bytes.addLast(__v);
	}
	
	/**
	 * Appends multiple non-dynamic bytes to the output section.
	 *
	 * @param __v The bytes to add.
	 * @param __o The offset into the array.
	 * @param __l The number of bytes to add.
	 * @throws IllegalStateException If the fragment has been finished.
	 * @throws IndexOutOfBoundsException If the offset and/or length are
	 * negative or exceeds the array bounds.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/06/27
	 */
	public final void append(byte[] __v, int __o, int __l)
		throws IllegalStateException, IndexOutOfBoundsException,
			NullPointerException
	{
		// Check
		if (__v == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0 || (__o + __l) > __v.length)
			throw new IndexOutOfBoundsException("IOOB");
		
		this._bytes.addLast(__v, __o, __l);
	}
	
	/**
	 * Appends a double to the fragment.
	 *
	 * @param __v The value to append.
	 * @throws IllegalStateException If the fragment builder is finished.
	 * @since 2017/09/15
	 */
	public final void appendDouble(double __v)
		throws IllegalStateException
	{
		appendLong(Double.doubleToRawLongBits(__v));
	}
	
	/**
	 * Appends a float to the fragment.
	 *
	 * @param __v The value to append.
	 * @throws IllegalStateException If the fragment builder is finished.
	 * @since 2017/09/15
	 */
	public final void appendFloat(float __v)
		throws IllegalStateException
	{
		appendInteger(Float.floatToRawIntBits(__v));
	}
	
	/**
	 * Appends an integer to the fragment.
	 *
	 * @param __v The value to append.
	 * @throws IllegalStateException If the fragment builder is finished.
	 * @since 2017/09/15
	 */
	public final void appendInteger(int __v)
		throws IllegalStateException
	{
		byte[] buf = this._buf;
		buf[0] = (byte)(__v >>> 24);
		buf[1] = (byte)(__v >>> 16);
		buf[2] = (byte)(__v >>> 8);
		buf[3] = (byte)(__v);
		append(buf, 0, 4);
	}
	
	/**
	 * Appends a long to the fragment.
	 *
	 * @param __v The value to append.
	 * @throws IllegalStateException If the fragment builder is finished.
	 * @since 2017/09/15
	 */
	public final void appendLong(long __v)
		throws IllegalStateException
	{
		byte[] buf = this._buf;
		buf[0] = (byte)(__v >>> 56);
		buf[1] = (byte)(__v >>> 48);
		buf[2] = (byte)(__v >>> 40);
		buf[3] = (byte)(__v >>> 32);
		buf[4] = (byte)(__v >>> 24);
		buf[5] = (byte)(__v >>> 16);
		buf[6] = (byte)(__v >>> 8);
		buf[7] = (byte)(__v);
		append(buf, 0, 8);
	}
	
	/**
	 * Appends a short to the fragment.
	 *
	 * @param __v The value to append.
	 * @throws IllegalStateException If the fragment builder is finished.
	 * @since 2017/09/15
	 */
	public final void appendShort(short __v)
		throws IllegalStateException
	{
		byte[] buf = this._buf;
		buf[0] = (byte)(__v >>> 8);
		buf[1] = (byte)(__v);
		append(buf, 0, 2);
	}
	
	/**
	 * Builds the fragment which has been generated from this.
	 *
	 * @return The resulting built fragment.
	 * @since 2017/08/29
	 */
	public final TemporaryFragment build()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Returns the number of currently written bytes.
	 *
	 * @return The number of currently written bytes.
	 * @since 2017/08/14
	 */
	public final int length()
	{
		return this._bytes.size();
	}
}

