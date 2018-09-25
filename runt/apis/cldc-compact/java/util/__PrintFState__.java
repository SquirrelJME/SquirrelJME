// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.util;

/**
 * This stores the state for printf parsing.
 *
 * @since 2018/09/24
 */
final class __PrintFState__
{
	/** Flags specified. */
	final boolean[] _flags =
		new boolean[__Flag__.COUNT];
	
	/** The argument index. */
	int _argdx =
		-1;
	
	/** The width. */
	int _width =
		-1;
	
	/**
	 * Was a width specified?
	 *
	 * @return If a width was specified.
	 * @since 2018/09/24
	 */
	final boolean __hasWidth()
	{
		return this._width >= 1;
	}
	
	/**
	 * Sets the argument index.
	 *
	 * @param __dx The index to set.
	 * @throws IllegalArgumentException If the index is not valid.
	 * @since 2018/09/24
	 */
	final void __setArgumentIndex(int __dx)
		throws IllegalArgumentException
	{
		// {@squirreljme.error ZZ1n Argument index already set or is of an
		// invalid value. (The index used)}
		if (__dx <= 0 || this._argdx >= 1)
			throw new IllegalArgumentException("ZZ1n " + __dx);
		
		this._argdx = __dx;
	}
	
	/**
	 * Sets the specified flag.
	 *
	 * @param __c The flag to set.
	 * @return If the flag is valid.
	 * @throws IllegalArgumentException If the flag was duplicated.
	 * @since 2018/09/24
	 */
	final boolean __setFlag(char __c)
		throws IllegalArgumentException
	{
		// Is this flag one that exists?
		__Flag__ f = __Flag__.__decode(__c);
		if (f == null)
			return false;
		
		boolean[] flags = this._flags;
		
		// {@squirreljme.error ZZ1p Duplicate flag specified. (The flag)}
		int ord = f.ordinal();
		if (flags[ord])
			throw new IllegalArgumentException("ZZ1p " + __c);
		
		// Use it
		flags[ord] = true;
		return true;
	}
	
	/**
	 * Sets the width.
	 *
	 * @param __w The width to use.
	 * @throws IllegalArgumentException If the width is not valid.
	 * @since 2018/09/24
	 */
	final void __setWidth(int __w)
		throws IllegalArgumentException
	{
		// {@squirreljme.error ZZ1o Width already or is of an invalid value.
		// (The width used)}
		if (__w <= 0 || this._width >= 1)
			throw new IllegalArgumentException("ZZ1o " + __w);
		
		this._width = __w;
	}
	
	/**
	 * Enumeration flags which are available for use.
	 *
	 * @since 2018/09/24
	 */
	static enum __Flag__
	{
		/** Left justification. */
		LEFT_JUSTIFIED,
		
		/** Alternative form. */
		ALTERNATIVE_FORM,
		
		/** Include sign always. */
		ALWAYS_SIGNED,
		
		/** Padded sign with space. */
		SPACE_FOR_POSITIVE,
		
		/** Zero padded. */
		ZERO_PADDED,
		
		/** Locale specific grouping specifiers. */
		LOCALE_GROUPING,
		
		/** Negative numbers in parenthesis. */
		NEGATIVE_PARENTHESIS,
		
		/** End. */
		;
		
		/** The number of flags used. */
		public static final int COUNT =
			__Flag__.values().length;
		
		/**
		 * Decodes the given character to a flag.
		 *
		 * @param __c The character to decode.
		 * @return The flag for the character.
		 * @since 2018/09/24
		 */
		static final __Flag__ __decode(char __c)
		{
			switch (__c)
			{
				case '-':	return __Flag__.LEFT_JUSTIFIED;
				case '#':	return __Flag__.ALTERNATIVE_FORM;
				case '+':	return __Flag__.ALWAYS_SIGNED;
				case ' ':	return __Flag__.SPACE_FOR_POSITIVE;
				case '0':	return __Flag__.ZERO_PADDED;
				case ',':	return __Flag__.LOCALE_GROUPING;
				case '(':	return __Flag__.NEGATIVE_PARENTHESIS;
				default:
					return null;
			}
		}
	}
}

