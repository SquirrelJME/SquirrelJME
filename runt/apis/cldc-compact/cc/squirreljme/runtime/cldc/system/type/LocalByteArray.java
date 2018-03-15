// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.system.type;

/**
 * This represents a byte array which is local to the current task.
 *
 * @since 2018/03/01
 */
public final class LocalByteArray
	implements ByteArray
{
	/** The byte array to read/write. */
	protected final byte[] array;
	
	/**
	 * Initializes the local byte array.
	 *
	 * @param __a The array to wrap.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/01
	 */
	public LocalByteArray(byte[] __a)
		throws NullPointerException
	{
		if (__a == null)
			throw new NullPointerException("NARG");
		
		this.array = __a;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/04
	 */
	@Override
	public final byte get(int __i)
		throws ArrayIndexOutOfBoundsException
	{
		byte[] array = this.array;
		if (__i < 0 || __i >= array.length)
			throw new ArrayIndexOutOfBoundsException("IOOB");
		
		return array[__i];
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/04
	 */
	@Override
	public final void get(int __i, byte[] __v, int __o, int __l)
		throws ArrayIndexOutOfBoundsException, NullPointerException
	{
		if (__v == null)
			throw new NullPointerException("NARG");
		
		byte[] array = this.array;
		if (__o < 0 || __l < 0 || (__o + __l) > __v.length ||
			__i < 0 || (__i + __l) > array.length)
			throw new ArrayIndexOutOfBoundsException("IOOB");
		
		for (int c = 0, i = __i, o = __o; c < __l; c++, i++, o++)
			__v[o] = array[i];
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/04
	 */
	@Override
	public final int length()
	{
		return this.array.length;
	}
	
	/**
	 * Returns the local access so that it is directly accessed.
	 *
	 * @return The local array.
	 * @since 2018/03/04
	 */
	public final byte[] localArray()
	{
		return this.array;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/04
	 */
	@Override
	public final void set(int __i, byte __v)
		throws ArrayIndexOutOfBoundsException
	{
		byte[] array = this.array;
		if (__i < 0 || __i >= array.length)
			throw new ArrayIndexOutOfBoundsException("IOOB");
		
		array[__i] = __v;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/04
	 */
	@Override
	public final void set(int __i, byte[] __v, int __o, int __l)
		throws ArrayIndexOutOfBoundsException, NullPointerException
	{
		if (__v == null)
			throw new NullPointerException("NARG");
		
		byte[] array = this.array;
		if (__o < 0 || __l < 0 || (__o + __l) > __v.length ||
			__i < 0 || (__i + __l) > array.length)
			throw new ArrayIndexOutOfBoundsException("IOOB");
		
		for (int c = 0, i = __i, o = __o; c < __l; c++, i++, o++)
			array[i] = __v[o];
	}
}

