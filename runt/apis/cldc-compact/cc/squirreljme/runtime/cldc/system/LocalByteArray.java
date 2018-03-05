// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.system;

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
		return this.array[__i];
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/04
	 */
	@Override
	public final void get(int __i, byte[] __b, int __o, int __l)
		throws ArrayIndexOutOfBoundsException, NullPointerException
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/01
	 */
	@Override
	public final int length()
	{
		return this.array.length;
	}
}

