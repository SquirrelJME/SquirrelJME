// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.util;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.IOException;
import org.jetbrains.annotations.NotNull;

/**
 * An {@link Appendable} on a fixed {@code char[]} array.
 *
 * @since 2026/07/05
 */
@SquirrelJMEVendorApi
public final class CharArrayAppendable
	implements Appendable, CharSequence
{
	/** The characters to append onto. */
	private final char[] _chars;
	
	/** The current position. */
	private volatile int _position;
	
	/**
	 * Initializes the appendable wrapping the given character array.
	 *
	 * @param __chars The character array to wrap.
	 * @throws NullPointerException On null arguments.
	 * @since 2026/07/05
	 */
	@SquirrelJMEVendorApi
	public CharArrayAppendable(char[] __chars)
		throws NullPointerException
	{
		if (__chars == null)
			throw new NullPointerException("NARG");
		
		this._chars = __chars;
	}
	
	/**
	 * Initializes the appendable with the given array length.
	 *
	 * @param __n The length of the character array.
	 * @throws ArrayIndexOutOfBoundsException If the array size is negative.
	 * @since 2026/07/05
	 */
	@SquirrelJMEVendorApi
	public CharArrayAppendable(int __n)
		throws ArrayIndexOutOfBoundsException
	{
		if (__n < 0)
			throw new ArrayIndexOutOfBoundsException("NEGV");
		
		this._chars = new char[__n];
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2026/07/05
	 */
	@Override
	public Appendable append(CharSequence __c)
		throws IOException
	{
		// null?
		if (__c == null)
			__c = "null";
		
		// Forward
		this.append(__c, 0,  __c.length());
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2026/07/05
	 */
	@Override
	public Appendable append(CharSequence __c, int __s, int __e)
		throws IndexOutOfBoundsException, IOException
	{
		// null?
		if (__c == null)
			__c = "null";
		
		// Check bounds
		int vn = __c.length();
		if (__s < 0 || __e < 0 || __e > vn || __s > __e)
			throw new IndexOutOfBoundsException("IOOB");
		
		// Append each individual character
		for (int i = __s; i < __e; i++)
			this.append(__c.charAt(i));
		
		// This always returns self
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2026/07/05
	 */
	@Override
	public Appendable append(char __c)
		throws IOException
	{
		char[] chars = this._chars;
		int position = this._position;
		
		// Cannot add more?
		if (position >= chars.length)
			throw new IOException("EOFF");
		
		// Set and bump up one
		chars[position] = __c;
		this._position = position + 1;
		
		// This always returns self
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2026/07/05
	 */
	@Override
	public char charAt(int __i)
		throws IndexOutOfBoundsException
	{
		char[] chars = this._chars;
		if (__i < 0 || __i >=  chars.length)
			throw new IndexOutOfBoundsException("IOOB");
		
		return chars[__i];
	}
	
	/**
	 * Returns the character array. 
	 *
	 * @return The character array.
	 * @since 2026/07/05
	 */
	@SquirrelJMEVendorApi
	public char[] charArray()
	{
		return this._chars;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2026/07/05
	 */
	@Override
	public int length()
	{
		return this._chars.length;
	}
	
	
	/**
	 * Returns the current appendable position. 
	 *
	 * @return The appendable position.
	 * @since 2026/07/05
	 */
	@SquirrelJMEVendorApi
	public int position()
	{
		return this._position;
	}
	
	/**
	 * Resets the position.
	 *
	 * @since 2026/07/05
	 */
	@SquirrelJMEVendorApi
	public void reset()
	{
		this._position = 0;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2026/07/05
	 */
	@Override
	public CharSequence subSequence(int __s, int __e)
	{
		if (__e < __s)
			throw new IndexOutOfBoundsException("NEGV");
		
		return new CharArrayCharSequence(this._chars, __s, __e - __s);
	}
}
