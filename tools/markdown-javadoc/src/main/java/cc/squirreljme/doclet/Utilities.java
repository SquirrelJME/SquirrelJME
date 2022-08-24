// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.doclet;

/**
 * Utilities for document generation.
 *
 * @since 2022/08/24
 */
public final class Utilities
{
	/**
	 * Not used.
	 * 
	 * @since 2022/08/24
	 */
	private Utilities()
	{
	}
	
	/**
	 * Returns the first line of text.
	 * 
	 * @param __text The text to get.
	 * @return The first line of text.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/08/24
	 */
	public static String firstLine(String __text)
		throws NullPointerException
	{
		if (__text == null)
			throw new NullPointerException("NARG");
		
		// Find where the string ends.
		int len = __text.length();
		char lastChar = 0xFFFD;
		for (int i = 0; i < len; i++)
		{
			char nowChar = __text.charAt(i);
			
			// Whitespace followed by a period
			if (lastChar == '.' && Character.isWhitespace(nowChar))
				return __text.substring(0, i);
			
			// Set new last character
			lastChar = nowChar;
		}
		
		// If this point was reached, entire is used
		return __text;
	}
	
	/**
	 * Neatens the text so it does not look horrible.
	 * 
	 * @param __text The text to neaten.
	 * @return The neatened text.
	 * @since 2022/08/24
	 */
	public static String neatText(String __text)
	{
		// If null, just carry it over
		if (__text == null)
			return null;
		
		StringBuilder result = new StringBuilder(__text.length());
		
		// Process every character
		char lastChar = 0xFFFD;
		boolean useChar;
		for (int i = 0, n = __text.length(); i < n; i++)
		{
			// Use the character by default
			useChar = true;
			char nowChar = __text.charAt(i);
			
			// Normalize any and all whitespace to just space
			if (Character.isWhitespace(nowChar))
				nowChar = ' ';
			
			// If there is whitespace after whitespace, trim it down to just
			// a single one...
			if (lastChar == ' ' && nowChar == ' ')
				useChar = false;
			
			// Use character?
			if (useChar)
				result.append(nowChar);
			
			// Set last character for processing
			lastChar = nowChar;
		}
		
		return result.toString();
	}
}
