// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

/**
 * This is used to read comments from a source code file.
 *
 * @since 2020/02/22
 */
public class CommentReader
	extends Reader
{
	/** The source reader. */
	protected final BufferedReader source;
	
	/** Extra character queue. */
	private final StringBuilder _queue =
		new StringBuilder();
	
	/** Line remainder. */
	private final StringBuilder _remainder =
		new StringBuilder();
	
	/** Are we in multi-line comment? */
	private boolean _inMultiLine;
	
	/**
	 * Initializes the comment reader.
	 *
	 * @param __in The input source.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/02/22
	 */
	public CommentReader(InputStream __in)
		throws NullPointerException
	{
		try
		{
			this.source = new BufferedReader(
				new InputStreamReader(__in, "utf-8"));
		}
		catch (UnsupportedEncodingException e)
		{
			throw new RuntimeException("UTF-8 is not supported?", e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/02/22
	 */
	@Override
	public void close()
		throws IOException
	{
		this.source.close();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2002/02/22
	 */
	@Override
	public int read(char[] __c, int __o, int __l)
		throws IndexOutOfBoundsException, IOException
	{
		if (__o < 0 || __l < 0 || (__o + __l) < 0 || (__o + __l) > __c.length)
			throw new IndexOutOfBoundsException();
		
		BufferedReader source = this.source;
		StringBuilder queue = this._queue;
		StringBuilder remainder = this._remainder;
		boolean inMultiLine = this._inMultiLine;
		
		// Constantly try to fill the output buffer
		int putCount = 0;
		while (putCount < __l)
		{
			// If there are characters in the queue, drain from it
			if (queue.length() > 0)
			{
				// Extract the first character
				char ch = queue.charAt(0);
				queue.deleteCharAt(0);
				
				// Place it in the output
				__c[__o++] = ch;
				putCount++;
				
				// Try again
				continue;
			}
			
			// Read the next source line
			String ln = (remainder.length() > 0 ? remainder.toString() :
				source.readLine());
			if (ln == null)
				return (putCount > 0 ? putCount : -1);
			
			// Always trim lines to remove extra clutter
			ln = ln.trim();
			
			// Always clear the remainder, since we might push it again later
			remainder.setLength(0);
			
			// The length of the line
			int len = ln.length();
			
			// Are we in a multi-line comment still?
			if (inMultiLine)
			{
				// This is likely a JavaDoc continuation so drop that
				if (ln.startsWith("* "))
					ln = ln.substring(2);
				
				// Length may have changed so recalculate it
				len = ln.length();
				
				// Find end of line
				int eml = ln.indexOf("*/");
				
				// There are no ending slashes, use entire line
				if (eml < 0)
					queue.append(ln);
				
				// Otherwise our multi-line is going to end and we need to
				// process the remainder of the line
				else
				{
					// Add starting chunk to the queue for output
					queue.append(ln, 0, eml);
					
					// Store the remainder of the line since we may still have
					// another comment on it
					remainder.append(ln, eml + 2, len);
					
					// Stop being in multi-line mode
					inMultiLine = false;
				}
			}
			
			// Normal line
			else
			{
				// Get potential start positions of lines
				int sng = ln.indexOf("//");
				int mul = ln.indexOf("/*");
				
				// Detect double star for JavaDoc
				if (mul >= 0)
				{
					int jdoc = ln.indexOf("/**", mul);
					if (jdoc >= 0)
						mul = jdoc;
				}
				
				// No comment on this line
				if (sng < 0 && mul < 0)
					continue;
				
				// Single line comment
				if (sng >= 0 && (mul < 0 || sng < mul))
				{
					// Add fragment to the queue
					queue.append(ln, sng + 2, len);
				}
				
				// Multi-line comment
				else
				{
					// Does the multi-line end on the same line?
					int eml = ln.indexOf("*/", mul + 2);
					if (eml > mul)
					{
						// Add the comment area to the queue
						queue.append(ln, mul + 2, eml);
						
						// Add the rest of the line to the remainder for
						// later processing
						remainder.append(ln, eml + 2, len);
					}
					
					// Does not, so keep reading
					else
					{
						// Now in a multi-line
						inMultiLine = true;
						
						// Add our comment into the queue
						queue.append(ln, mul + 2, len);
					}
				}
			}
		}
		
		// Store comment type for later runs
		this._inMultiLine = inMultiLine;
		
		// Return the number out placed characters
		return putCount;
	}
	
	/**
	 * Reads the entire file to a string.
	 *
	 * @param __sb The input buffer to write to.
	 * @return {@code __sb}.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/02/22
	 */
	public final StringBuilder readAll(StringBuilder __sb)
		throws IOException, NullPointerException
	{
		if (__sb == null)
			throw new NullPointerException();
		
		char[] buf = new char[512];
		for (;;)
		{
			int rc = this.read(buf, 0, 512);
			
			if (rc < 0)
				break;
			
			__sb.append(buf, 0, rc);
		}
		
		return __sb;
	}
	
	/**
	 * The type of comment being parsed.
	 *
	 * @since 2020/02/22
	 */
	private enum CommentType
	{
		/** No comment. */
		NONE,
		
		/** Single line comment. */
		SINGLE,
		
		/** Block comment. */
		BLOCK,
		
		/** End. */
		;
	}
}
