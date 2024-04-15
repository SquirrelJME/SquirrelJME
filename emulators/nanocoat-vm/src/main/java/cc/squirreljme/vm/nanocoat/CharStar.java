// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.nanocoat;

/**
 * NUL terminated character string that is somewhere in C memory.
 *
 * @since 2023/12/16
 */
public final class CharStar
	implements CharStarPointer
{
	/** The pointer where this is located. */
	private final long _pointer;
	
	/** Cached length. */
	private volatile int _len =
		-1;
	
	/**
	 * Initializes the character array pointer.
	 *
	 * @param __addr The address where it is located.
	 * @throws NullPointerException On null addresses.
	 * @since 2023/12/16
	 */
	public CharStar(long __addr)
		throws NullPointerException
	{
		if (__addr == 0)
			throw new NullPointerException("NARG");
		
		this._pointer = __addr;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/12/16
	 */
	@Override
	public char charAt(int __dx)
		throws IllegalStateException, IndexOutOfBoundsException
	{
		if (__dx < 0 || __dx >= this.length())
			throw new IndexOutOfBoundsException("Out of bounds: " + __dx);
		
		int result = CharStar.__utfCharAt(this._pointer, __dx);
		if (result < 0)
			throw new IllegalStateException("Invalid character sequence.");
		
		return (char)result;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/12/16
	 */
	@Override
	public int length()
	{
		int result = this._len;
		if (result < 0)
			this._len = (result = CharStar.__utfStrlen(this._pointer));
		
		return result;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/12/16
	 */
	@Override
	public long pointerAddress()
	{
		return this._pointer;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/12/16
	 */
	@Override
	public CharSequence subSequence(int __start, int __end)
		throws IllegalArgumentException
	{
		int len = __end - __start;
		if (len < 0)
			throw new IllegalArgumentException("Invalid sequence.");
		
		else if (len == 0)
			return "";
		
		/* Extract as a Java string instead. */
		StringBuilder result = new StringBuilder(len);
		result.append(this, __start, __end);
		return result.toString();
	}
	
	/**
	 * UTF Character at.
	 *
	 * @param __pointer The pointer address.
	 * @param __dx The character index.
	 * @return The resultant character or {@code -1} if invalid.
	 * @since 2023/12/16
	 */
	private static native int __utfCharAt(long __pointer, int __dx);
	
	/**
	 * Returns the length of the given address.
	 *
	 * @param __addr The pointer address.
	 * @return The length of the given address.
	 * @since 2023/12/16
	 */
	private static native int __utfStrlen(long __addr);
}
