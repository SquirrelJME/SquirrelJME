// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.string;

/**
 * This is a sequence which is backed by a directly access character array.
 *
 * The array data is not copied.
 *
 * @since 2018/09/16
 */
public final class CharArraySequence
	extends BasicSequence
{
	/** The array data. */
	protected final char[] data;
	
	/** The array length. */
	protected final int length;
	
	/**
	 * Initializes the character array sequence.
	 *
	 * @param __data The input data, this is set directly and is not copied.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/09/16
	 */
	public CharArraySequence(char... __data)
		throws NullPointerException
	{
		if (__data == null)
			throw new NullPointerException("NARG");
		
		this.data = __data;
		this.length = __data.length;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/09/16
	 */
	@Override
	public final char charAt(int __i)
		throws StringIndexOutOfBoundsException
	{
		try
		{
			return this.data[__i];
		}
		catch (IndexOutOfBoundsException e)
		{
			throw new StringIndexOutOfBoundsException(__i);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/09/16
	 */
	@Override
	public final int length()
	{
		return this.length;
	}
}

