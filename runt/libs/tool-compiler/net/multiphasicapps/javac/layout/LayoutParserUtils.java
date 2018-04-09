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
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import net.multiphasicapps.classfile.BinaryName;
import net.multiphasicapps.classfile.ClassIdentifier;
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
	
	/**
	 * Reads a single import statement fragment
	 *
	 * @param __t The tokens to read from.
	 * @return The parsed import.
	 * @throws LayoutParserException If an import was not read.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/04/08
	 */
	public static final ClassImport readClassImport(ExpandingSource __t)
		throws LayoutParserException, IOException, NullPointerException
	{
		if (__t == null)
			throw new NullPointerException("NARG");
		
		ExpandedToken token = __t.peek();
		TokenType type = token.type();
		
		// Is this static?
		boolean isstatic = false;
		if (type == TokenType.KEYWORD_STATIC)
		{
			isstatic = true;
			
			// Consume static
			__t.next();
			
			// Peek for next read
			token = __t.peek();
			type = token.type();
		}
		
		// {@squirreljme.error AQ29 Expected an identifier to start an import
		// statement. (The token)}
		if (type != TokenType.IDENTIFIER)
			throw new LayoutParserException(token,
				String.format("AQ29 %s", token));
		
		// Read in tokens
		StringBuilder pksb = new StringBuilder();
		for (;;)
		{
			// Read next token
			token = __t.next();
			type = token.type();
			
			// Wildcard import? Finish parsing
			if (type == TokenType.OPERATOR_MULTIPLY)
				return new ClassImport(isstatic,
					new BinaryName(pksb.toString()), new ClassIdentifier("*"));
			
			// {@squirreljme.error AQ2a Expected identifier or wildcard symbol
			// while parsing the import statement. (The token)}
			else if (type != TokenType.IDENTIFIER)
				throw new LayoutParserException(token,
					String.format("AQ2a %s", token));
			
			// Store it for later
			ExpandedToken ident = token;
			
			// Peek next, determines if it ends or continues
			token = __t.peek();
			type = token.type();
			
			// Will read more parts?
			if (type == TokenType.SYMBOL_DOT)
			{
				// Add prefix slash for package?
				if (pksb.length() > 0)
					pksb.append('/');
				
				// Add that
				pksb.append(ident.characters());
				
				// Consume the dot
				__t.next();
			}
			
			// Stop otherwise
			else
				return new ClassImport(isstatic,
					new BinaryName(pksb.toString()),
					new ClassIdentifier(ident.characters()));
		}
	}
	
	/**
	 * Reads a group of tokens between brace types.
	 *
	 * @param __open The opening token.
	 * @param __src The source to read tokens from.
	 * @return The iterable containing input tokens.
	 * @throws LayoutParserException If it could not be parsed.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/04/08
	 */
	public static final Iterable<ExpandedToken> readGroup(
		ExpandedToken __open, ExpandingSource __src)
		throws LayoutParserException, IOException, NullPointerException
	{
		if (__open == null || __src == null)
			throw new NullPointerException("NARG");
		
		// Always push the opening token
		// Use linked list because it is more optimized for adding new
		// elements to the back, it is also intended to be used for
		// iteration
		List<ExpandedToken> rv = new LinkedList<>();
		
		// The returned group always has the surrounding token for
		// consistency
		rv.add(__open);
		
		// Determine the closing token type
		TokenType opentype = __open.type(),
			closetype;
		switch (opentype)
		{
			case SYMBOL_OPEN_PARENTHESIS:
				closetype = TokenType.SYMBOL_CLOSED_PARENTHESIS;
				break;
				
			case SYMBOL_OPEN_BRACE:
				closetype = TokenType.SYMBOL_CLOSED_BRACE;
				break;
			
			case SYMBOL_OPEN_BRACKET:
				closetype = TokenType.SYMBOL_CLOSED_BRACKET;
				break;
			
				// Generics, this should work just fine with the expander
			case COMPARE_LESS_THAN:
				closetype = TokenType.COMPARE_GREATER_THAN;
				break;
			
				// {@squirreljme.error AQ2i Could not determine the closing
				// type for the token. (The open type)}
			default:
				throw new LayoutParserException(__open,
					String.format("AQ2i %s", opentype));
		}
		
		// The processing stack for how deep the tokens are keep track of
		// the opening tokens in the event they are not properly closed.
		Deque<ExpandedToken> stack = new ArrayDeque<>();
		stack.push(__open);
		
		// Keep parsing token as long as the stack has tokens in it
		while (!stack.isEmpty())
		{
			ExpandedToken next = __src.next();
			TokenType type = next.type();
			
			// The token is always added to the result regardless
			rv.add(next);
			
			// {@squirreljme.error AQ2j Reached end of file before the end of
			// the token group was discovered, this means that one of the
			// specified tokens has not been closed properly.
			// (The group stack)}
			if (type == TokenType.END_OF_FILE)
				throw new LayoutParserException(stack.pollLast(),
					String.format("AQ2j %s", stack));
			
			// Closing, pop off the stack
			else if (type == closetype)
				stack.pop();
			
			// Opening a new one, push to the stack
			else if (type == opentype)
				stack.push(next);
		}
		
		return rv;
	}
	
	/**
	 * Reads a generic binary name from the input.
	 *
	 * @param __t The source to read tokens from.
	 * @return The generic binary name.
	 * @throws LayoutParserException If it could not be parsed.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/04/08
	 */
	public static final GenericBinaryName readGenericBinaryName(
		ExpandingSource __t)
		throws LayoutParserException, IOException, NullPointerException
	{
		if (__t == null)
			throw new NullPointerException("NARG");
		
		// Read normal binary name
		BinaryName bin = LayoutParserUtils.readBinaryName(__t);
		
		// Potentially parse generic parameters?
		ExpandedToken token = __t.peek();
		if (token.type() == TokenType.COMPARE_LESS_THAN)
			return new GenericBinaryName(bin,
				LayoutParserUtils.readGenericParameters(__t));
		
		// Binary name with no generics
		else
			return new GenericBinaryName(bin);
	}
	
	/**
	 * Reads and parses generic parameters.
	 *
	 * @param __t The source to read tokens from.
	 * @return The generic parameters.
	 * @throws LayoutParserException If it could not be parsed.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/04/09
	 */
	public static final GenericParameters readGenericParameters(
		ExpandingSource __t)
		throws LayoutParserException, IOException, NullPointerException
	{
		if (__t == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * Reads generic type information.
	 *
	 * @param __t The input token source.
	 * @return The parsed generic type.
	 * @throws LayoutParserException If it could not be parsed.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/04/09
	 */
	public static final GenericType readGenericType(ExpandingSource __t)
		throws LayoutParserException, IOException, NullPointerException
	{
		if (__t == null)
			throw new NullPointerException("NARG");
		
		// There is always a generic binary name for the type
		GenericBinaryName gbn = LayoutParserUtils.readGenericBinaryName(__t);
		
		// It may additionally be followed by array brackets
		int dims = 0;
		for (;;)
		{
			// Has to be an open bracket
			ExpandedToken token = __t.peek();
			if (token.type() != TokenType.SYMBOL_OPEN_BRACKET)
				break;
			
			// Count dimension and consume the token
			dims++;
			__t.next();
			
			// {@squirreljme.error AQ2s In array type, expected a closing
			// bracket to follow immedietly after the open bracket. (The
			// read token)}
			token = __t.next();
			if (token.type() != TokenType.SYMBOL_CLOSED_BRACKET)
				throw new LayoutParserException(token,
					String.format("AQ2s %s", token));
		}
		
		return new GenericType(gbn, dims);
	}
}

