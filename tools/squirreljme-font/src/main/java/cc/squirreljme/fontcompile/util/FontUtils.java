// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.fontcompile.util;

import cc.squirreljme.fontcompile.InvalidFontException;
import java.util.Arrays;

/**
 * Font utilities.
 *
 * @since 2024/05/25
 */
public final class FontUtils
{
	/**
	 * Not used.
	 *
	 * @since 2024/05/25
	 */
	private FontUtils()
	{
	}
	
	/**
	 * Parses an integer value from a set of tokens, this uses a radix of 10.
	 *
	 * @param __tokens The input tokens.
	 * @param __dx The index to parse.
	 * @return The parsed value.
	 * @throws IndexOutOfBoundsException If the index is negative.
	 * @throws InvalidFontException If the number is not valid or the index
	 * is not a valid token.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/05/25
	 */
	public static int parseInteger(String[] __tokens, int __dx)
		throws IndexOutOfBoundsException, InvalidFontException,
			NullPointerException
	{
		return FontUtils.parseInteger(__tokens, __dx, 10);
	}
	
	/**
	 * Parses an integer value from a set of tokens.
	 *
	 * @param __tokens The input tokens.
	 * @param __dx The index to parse.
	 * @param __radix The radix of the value.
	 * @return The parsed value.
	 * @throws IndexOutOfBoundsException If the index is negative.
	 * @throws InvalidFontException If the number is not valid or the index
	 * is not a valid token.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/05/25
	 */
	public static int parseInteger(String[] __tokens, int __dx, int __radix)
		throws IndexOutOfBoundsException, InvalidFontException,
			NullPointerException
	{
		if (__dx < 0)
			throw new IndexOutOfBoundsException("NEGV");
		
		if (__tokens == null)
			throw new NullPointerException("NARG");
		
		if (__dx >= __tokens.length)
			throw new InvalidFontException(String.format(
				"Not enough tokens in %s, wanted %d.",
				Arrays.asList(__tokens), __dx));
		
		if (__tokens[__dx] == null)
			throw new NullPointerException("NARG");
		
		try
		{
			return Integer.parseInt(__tokens[__dx], __radix);
		}
		catch (NumberFormatException __e)
		{
			throw new InvalidFontException(String.format(
				"The token %s in %s is not a number.", __tokens[__dx],
				Arrays.asList(__tokens)));
		}
	}
}
