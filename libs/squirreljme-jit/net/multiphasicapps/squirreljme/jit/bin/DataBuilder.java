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

import java.io.UnsupportedEncodingException;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import net.multiphasicapps.util.datadeque.ByteDeque;

/**
 * This class is used to construct blocks of data.
 *
 * It is similar to {@link java.io.DataOutputStream} except it writes to a
 * seekable and arbitrary position writable output rather than just at the
 * end of any given stream.
 *
 * @since 2017/09/01
 */
public class DataBuilder
{
	/** Deque for the bytes within the section. */
	private final ByteDeque _bytes =
		new ByteDeque();
	
	/** Buffer for multi-byte data. */
	private final byte[] _buf =
		new byte[8];
	
	/**
	 * Appends a single byte to the output section.
	 *
	 * @param __v The byte to add.
	 * @since 2017/06/27
	 */
	public final void append(byte __v)
	{
		this._bytes.addLast(__v);
	}
	
	/**
	 * Appends multiple bytes to the output section.
	 *
	 * @param __v The bytes to add.
	 * @param __o The offset into the array.
	 * @param __l The number of bytes to add.
	 * @throws IndexOutOfBoundsException If the offset and/or length are
	 * negative or exceeds the array bounds.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/06/27
	 */
	public final void append(byte[] __v, int __o, int __l)
		throws IndexOutOfBoundsException, NullPointerException
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
	 * @since 2017/09/15
	 */
	public final void appendDouble(double __v)
	{
		appendLong(Double.doubleToRawLongBits(__v));
	}
	
	/**
	 * Appends a float to the fragment.
	 *
	 * @param __v The value to append.
	 * @since 2017/09/15
	 */
	public final void appendFloat(float __v)
	{
		appendInteger(Float.floatToRawIntBits(__v));
	}
	
	/**
	 * Appends an integer to the fragment.
	 *
	 * @param __v The value to append.
	 * @since 2017/09/15
	 */
	public final void appendInteger(int __v)
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
	 * @since 2017/09/15
	 */
	public final void appendLong(long __v)
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
	 * @since 2017/09/15
	 */
	public final void appendShort(short __v)
	{
		byte[] buf = this._buf;
		buf[0] = (byte)(__v >>> 8);
		buf[1] = (byte)(__v);
		append(buf, 0, 2);
	}
	
	/**
	 * Appends the specified string to the output as UTF-8 encoded bytes.
	 *
	 * @param __s The string to append.
	 * @param __nul If {@code true} the string is terminated with a NUL.
	 * @throws NullPointerException On null arguments.
	 * @throws RuntimeException If the UTF-8 encoding is not supported, which
	 * should not occur.
	 * @since 2017/09/01
	 */
	public final void appendUTFString(String __s, boolean __nul)
		throws IllegalArgumentException, NullPointerException, RuntimeException
	{
		// If NUL is requested then need an extra byte to fit it
		appendUTFString(__s, __s.length() + (__nul ? 1 : 0), __nul);
	}
	
	/**
	 * Appends the specified string to the output as UTF-8 encoded bytes.
	 *
	 * @param __s The string to append.
	 * @param __l The maximum number of bytes to write.
	 * @param __nul If {@code true} the string is terminated with a NUL
	 * character even if the sequence to write is too short.
	 * @throws IllegalArgumentException If the length is negative.
	 * @throws NullPointerException On null arguments.
	 * @throws RuntimeException If the UTF-8 encoding is not supported, which
	 * should not occur.
	 * @since 2017/09/01
	 */
	public final void appendUTFString(String __s, int __l, boolean __nul)
		throws IllegalArgumentException, NullPointerException, RuntimeException
	{
		// Check
		if (__s == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error JI29 Cannot write a string with a negative
		// length.}
		if (__l < 0)
			throw new IllegalArgumentException("JI29");
		
		// Writing a zero length string is pointless
		if (__l == 0)
			return;
		
		// Need to get the UTF-8 data from the string
		byte[] from;
		try
		{
			from = __s.getBytes("utf-8");
		}
		
		// {@squirreljme.error JI0z UTF-8 is not supported on this virtual
		// machine which means it does not implement something which is
		// required.}
		catch (UnsupportedEncodingException e)
		{
			throw new RuntimeException("JI0z", e);
		}
		
		// UTF-8 strings can potentially be chopped off in their multi-byte
		// sequences. This can cause security flaws to occur on some machines
		// potentially because a specially crafted input string could cause
		// a NUL byte to just be treated as part of a sequence rather
		// This is not a SquirrelJME bug, but there are OSes/libraries which do
		// exhibit this behavior so this curbs it with statically generated
		// strings.
		if (true)
			throw new todo.TODO();
		
		throw new todo.TODO();
		/*
		// Append sequence
		append(from);*/
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
	
	/**
	 * Returns a byte array containing all of the written data.
	 *
	 * @return The byte array of the data.
	 * @since 2017/09/01
	 */
	public final byte[] toByteArray()
	{
		return this._bytes.toByteArray();
	}
}

