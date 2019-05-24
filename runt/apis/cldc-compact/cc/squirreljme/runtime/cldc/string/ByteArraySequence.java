// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.string;

/**
 * This is a sequence of characters which uses basic bytes to represent basic
 * character sequences.
 *
 * @since 2019/04/27
 */
@Deprecated
public final class ByteArraySequence
	extends BasicSequence
{
	/** The array data. */
	protected final byte[] data;
	
	/** The array length. */
	protected final int length;
	
	/**
	 * Initializes the byte array sequence.
	 *
	 * @param __data The input data, this is set directly and is not copied.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/04/27
	 */
	public ByteArraySequence(byte... __data)
		throws NullPointerException
	{
		if (__data == null)
			throw new NullPointerException("NARG");
		
		this.data = __data;
		this.length = __data.length;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/04/27
	 */
	@Override
	public final char charAt(int __i)
		throws StringIndexOutOfBoundsException
	{
		try
		{
			throw new todo.TODO();
		}
		catch (IndexOutOfBoundsException e)
		{
			throw new StringIndexOutOfBoundsException(__i);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/04/27
	 */
	@Override
	public final int length()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/04/27
	 */
	@Override
	public char[] toCharArray()
	{
		throw new todo.TODO();
	}
}

