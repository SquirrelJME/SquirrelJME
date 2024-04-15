// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.c;

import java.io.IOException;

/**
 * C utilities.
 *
 * @since 2023/06/24
 */
public final class CUtils
{
	/**
	 * Not used.
	 * 
	 * @since 2023/06/24
	 */
	private CUtils()
	{
	}
	
	/**
	 * Quotes the input as a string.
	 * 
	 * @param __input The input.
	 * @return The input as a quoted string.
	 * @since 2023/05/31
	 */
	public static String quotedString(CharSequence __input)
	{
		if (__input == null)
			return null;
		
		// Open string
		StringBuilder sb = new StringBuilder("\"");
		
		// Whatever bytes need to be encoded properly
		try
		{
			for (byte b : __input.toString().getBytes("utf-8"))
			{
				// C will treat NUL as the end of string, so it has to be
				// modified with modified UTF [0b110_00000, 0b10_000000]
				if (b == '\0')
					sb.append("\\300\\200");
					
				// As normal
				else if (b == '\t')
					sb.append("\\t");
				else if (b == '\r')
					sb.append("\\r");
				else if (b == '\n')
					sb.append("\\n");
				else if (b == '\\')
					sb.append("\\\\");
				else if (b == '\"')
					sb.append("\\\"");
				else if (b == '?')
					sb.append("\\?");
				else if (b >= 0x20 && b < 0x7F)
					sb.append((char)b);
				
				// This is really the only most consistent here
				else
					sb.append(String.format("\\%03o", b & 0xFF));
			}
		}
		catch (IOException __e)
		{
			throw new RuntimeException(__e);
		}
		
		// Close string
		sb.append("\"");
		
		return sb.toString();
	}
}
