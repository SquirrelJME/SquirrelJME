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
 * PrintF flags which may exist to be used.
 *
 * @since 2018/09/24
 */
enum __PrintFFlag__
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
	
	/** Internal values. */
	private static final __PrintFFlag__[] _VALUES =
		__PrintFFlag__.values();
	
	/** The number of flags used. */
	public static final int COUNT =
		__PrintFFlag__._VALUES.length;
	
	/**
	 * Returns the value for the given ordinal/
	 *
	 * @param __i The ordinal.
	 * @return The flag.
	 * @throws IndexOutOfBoundsException If the ordinal is out of range.
	 * @since 2018/09/29
	 */
	public static final __PrintFFlag__ valueOf(int __i)
		throws IndexOutOfBoundsException
	{
		if (__i < 0 || __i >= __PrintFFlag__.COUNT)
			throw new IndexOutOfBoundsException("IOOB");
		return __PrintFFlag__._VALUES[__i];
	}
	
	/**
	 * Decodes the given character to a flag.
	 *
	 * @param __c The character to decode.
	 * @return The flag for the character or {@code null} if it is not valid.
	 * @since 2018/09/24
	 */
	static final __PrintFFlag__ __decode(char __c)
	{
		switch (__c)
		{
			case '-':	return __PrintFFlag__.LEFT_JUSTIFIED;
			case '#':	return __PrintFFlag__.ALTERNATIVE_FORM;
			case '+':	return __PrintFFlag__.ALWAYS_SIGNED;
			case ' ':	return __PrintFFlag__.SPACE_FOR_POSITIVE;
			case '0':	return __PrintFFlag__.ZERO_PADDED;
			case ',':	return __PrintFFlag__.LOCALE_GROUPING;
			case '(':	return __PrintFFlag__.NEGATIVE_PARENTHESIS;
			default:
				return null;
		}
	}
}

