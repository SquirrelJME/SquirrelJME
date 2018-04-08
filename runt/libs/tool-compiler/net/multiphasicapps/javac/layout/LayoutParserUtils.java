// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.javac.layout;

import java.io.IOException;
import net.multiphasicapps.classfile.BinaryName;
import net.multiphasicapps.javac.token.ExpandedToken;
import net.multiphasicapps.javac.token.ExpandingSource;
import net.multiphasicapps.javac.token.TokenType;

/**
 * This class contains some utilities that are used to parse layouts.
 *
 * @since 2018/04/08
 */
public final class LayoutParserUtils
{
	/**
	 * Not used.
	 *
	 * @since 2018/04/08
	 */
	private LayoutParserUtils()
	{
	}
	
	/**
	 * Reads a single binary name from the input.
	 *
	 * @param __t The tokens to read from.
	 * @return The parsed binary name.
	 * @throws LayoutParserException If a binary name was not read.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/04/08
	 */
	public static BinaryName readBinaryName(ExpandingSource __t)
		throws LayoutParserException, IOException, NullPointerException
	{
		if (__t == null)
			throw new NullPointerException("NARG");
		
		ExpandedToken token = __t.peek();
		TokenType type = token.type();
		
		// {@squirreljme.error AQ26 Expected an identifier to start a binary
		// name. (The token)}
		if (type != TokenType.IDENTIFIER)
			throw new LayoutParserException(token,
				String.format("AQ26 %s", token));
		
		// Handle tokens
		StringBuilder sb = new StringBuilder();
		for (;;)
		{
			// Read next token
			token = __t.next();
			type = token.type();
			
			// {@squirreljme.error AQ27 Expected identifier while parsing
			// binary name sequence. (The token)}
			if (type != TokenType.IDENTIFIER)
				throw new LayoutParserException(token,
					String.format("AQ27 %s", token));
			
			// Append identifier
			sb.append(token.characters());
			
			// If the next token is a dot then the binary name continues for
			// more identifier parts
			token = __t.peek();
			if (token.type() == TokenType.SYMBOL_DOT)
			{
				// Add slash
				sb.append('/');
				
				// Consume the token
				__t.next();
			}
			
			// Unknown, end of sequence
			else
				break;
		}
		
		// Build
		return new BinaryName(sb.toString());
	}
}

