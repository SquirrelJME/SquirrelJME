// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.util;

import cc.squirreljme.plugin.SourceError;
import java.io.Closeable;
import java.io.IOException;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.Deque;

/**
 * This is the tokenizer for error lists.
 *
 * @since 2020/03/01
 */
public class ErrorListTokenizer
	implements Closeable
{
	/** The tag used to detect errors. */
	public static final String _ERROR_TAG =
		"squirreljme.error";
	
	/** The internal tokenizer. */
	protected final StreamTokenizer tokenizer;
	
	/** The original stream. */
	protected final Reader reader;
	
	/** The original file name. */
	protected final Path fileName;
	
	/**
	 * Initializes the error list tokenizer.
	 *
	 * @param __fn The origin file name.
	 * @param __in The input stream.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/03/01
	 */
	public ErrorListTokenizer(Path __fn, Reader __in)
		throws NullPointerException
	{
		if (__in == null)
			throw new NullPointerException("NARG");
		
		// The original reader for input (for closing).
		this.reader = __in;
		this.fileName = __fn;
		
		// Create tokenizer over the stream
		StreamTokenizer tokenizer = new StreamTokenizer(__in);
		this.tokenizer = tokenizer;
		
		// Configure settings
		tokenizer.resetSyntax();
		tokenizer.eolIsSignificant(false);
		tokenizer.slashSlashComments(false);
		tokenizer.slashStarComments(false);
		tokenizer.wordChars('a', 'z');
		tokenizer.wordChars('A', 'Z');
		tokenizer.wordChars('.', '.');
		tokenizer.wordChars('0', '9');
		tokenizer.whitespaceChars('\0', ' ');
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/03/01
	 */
	@Override
	public final void close()
		throws IOException
	{
		this.reader.close();
	}
	
	/**
	 * Returns the next error in the stream.
	 *
	 * @return The next error, or {@code null} if none remain.
	 * @throws IOException On read errors.
	 * @since 2020/03/01
	 */
	public final SourceError next()
		throws IOException
	{
		// Token stack for handling tokens
		Deque<String> stack = new ArrayDeque<>();
		
		// Keep reading tokens until we find the sequence as needed
		boolean withinBlock = false;
		boolean withinJavaDocTag = false;
		for (;;)
		{
			// Keep reading tokens until none are left
			String token = this.__nextToken();
			if (token == null)
				return null;
			
			//System.err.printf("Token: %s%n", token);
			
			// If we are not within a block, check for opening brace
			if (!withinBlock)
			{
				if (token.equals("{"))
					withinBlock = true;
				
				// Drop any tokens here, even the closing block
				continue;
			}
			
			// Check to see if this is the JavaDoc tag
			if (!withinJavaDocTag)
			{
				// If this is at then it is a tag definition
				if (token.equals("@"))
					withinJavaDocTag = true;
				
				// Otherwise just stop because it will not be
				else
					withinBlock = false;
				
				// Keep running
				continue;
			}
			
			// End of declared block
			if (token.equals("}"))
			{
				// Build result
				SourceError result = null;
				try
				{
					result = (!stack.isEmpty() &&
						"squirreljme.error".equals(stack.pollFirst()) ?
						new SourceError(stack, this.fileName) : null);
				}
				catch (IllegalArgumentException e)
				{
					// Ignore errors but still report them
					System.err.printf("Invalid error code %s in %s%n",
						stack, this.fileName);
				}
				
				// Reset state for next tag run
				stack.clear();
				withinBlock = withinJavaDocTag = false;
				
				// Return the parsed token
				if (result != null)
					return result;
				continue;
			}
			
			// Put token on the stack to evaluate it later
			stack.addLast(token);
		}
	}
	
	/**
	 * Returns the next token.
	 *
	 * @return The next token or {@code null}.
	 * @since 2020/03/01
	 */
	private String __nextToken()
		throws IOException
	{
		// Read in the next token
		StreamTokenizer tokenizer = this.tokenizer;
		int type = tokenizer.nextToken();
		
		// Single character
		if (type > 0)
			return Character.toString((char)type);
		
		// Quoted string
		else if (type == StreamTokenizer.TT_WORD)
			return tokenizer.sval;
		
		// Number
		else if (type == StreamTokenizer.TT_NUMBER)
			return Double.toString(tokenizer.nval);
		
		// EOF
		return null;
	}
}
