// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.image;

import java.io.IOException;
import java.io.Reader;

/**
 * This is a character stripper which is used to remove C/C++ comments and
 * return the series of strings between curly braces. This does not perform
 * tokenization to remain as simple and as fast as possible, so XPM images
 * which use the preprocessor will likely not load properly. The decoder
 * generally assumes that the XPM is well formed.
 *
 * If a comma appears inside of the string array, then the character that would
 * be returned in its place is {@link #END_OF_LINE} character. This is because
 * XPM is a line based format.
 *
 * @since 2016/05/22
 */
class __CharStripper__
	extends Reader
{
	/** Special end of line code. */
	public static final int END_OF_LINE =
		Integer.MIN_VALUE;
	
	/** Lock to make sure there is a consistent state. */
	protected final Object lock =
		new Object();
	
	/** The input reader. */
	protected final Reader in;
	
	/** The current comment type (0 = none, 1 = single, 2 = multi. */
	private volatile int _comment;
	
	/** In a string literal? */
	private volatile boolean _instring;
	
	/** In the XPM bracket data. */
	private volatile boolean _inbracket;
	
	/** End of XPM? */
	private volatile boolean _endxpm;
	
	/**
	 * Initializes the character stripper.
	 *
	 * @param __r The source to strip characters from.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/22
	 */
	__CharStripper__(Reader __r)
		throws NullPointerException
	{
		// Check
		if (__r == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.in = __r;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/22
	 */
	@Override
	public void close()
		throws IOException
	{
		// Lock
		synchronized (this.lock)
		{
			this.in.close();
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/22
	 */
	@Override
	public int read()
		throws IOException
	{
		// Lock
		synchronized (this.lock)
		{
			// Read loop to find a valid character
			Reader in = this.in;
			for (;;)
			{
				// End of the XPM?
				if (this._endxpm)
					return -1;
				
				// Read single character
				int c = in.read();
				
				// EOF?
				if (c < 0)
					return -1;
				
				// In a multi-line comment?
				int comment = this._comment;
				if (comment == 2)
				{
					// If an asterisk, possible end of multi-line	
					if (c == '*')
					{
						c = in.read();
						
						// EOF?
						if (c < 0)
							return -1;
						
						// End of multi-line?
						if (c == '/')
							this._comment = 0;
					}
					
					continue;
				}
				
				// Single line comment
				else if (comment == 1)
				{
					// If \r or \n, end of comment
					if (c == '\r' || c == '\n')
						this._comment = 0;
					
					continue;
				}
				
				// In string?
				if (this._instring)
				{
					// Escape sequence?
					if (c == '\\')
					{
						// Get character
						c = in.read();
						
						// Depends on the character
						switch (c)
						{
							case 'a': return (char)0x07;
							case 'b': return (char)0x08;
							case 'f': return (char)0x0C;
							case 'n': return '\n';
							case 'r': return '\r';
							case 't': return '\t';
							case 'v': return (char)0x0B;
							case '\\': return '\\';
							case '\'': return '\'';
							case '"': return '"';
							case '?': return '?';
							
							case 'x':
								{
									// Read all chars as hex
									int x = Character.digit((char)in.read(),
										16);
									int y = Character.digit((char)in.read(),
										16);
									
									// If all are valid, use it
									if (x >= 0 && y >= 0)
										return (x << 4) | (y);
									continue;
								}
								
							case '0':
							case '1':
							case '2':
							case '3':
							case '4':
							case '5':
							case '6':
							case '7':
							case '8':
							case '9':
								{
									// Read all chars as octal
									int x = Character.digit((char)c, 8);
									int y = Character.digit((char)in.read(),
										8);
									int z = Character.digit((char)in.read(),
										8);
									
									// If all are valid, use it
									if (x >= 0 && y >= 0 && z >= 0)
										return (x << 6) | (y << 3) | (z);
									continue;
								}
							
								// Unknown, ignore
							default:
								continue;
						}
					}
					
					// End of string?
					else if (c == '"')
					{
						this._instring = false;
						continue;
					}
					
					// Normal character, use it
					else
						return c;
				}
				
				// Outside a string
				else
				{
					// Starting bracket
					if (c == '{')
					{
						// Set in bracket
						this._inbracket = true;
					}
					
					// Ending brakcet
					else if (c == '}')
					{
						// End the XPM but only if in a bracket
						if (this._inbracket)
						{
							this._endxpm = true;
							continue;
						}
					}
					
					// Start of a string?
					else if (c == '"')
						this._instring = true;
					
					// Possible start of comment?
					else if (c == '/')
					{
						// Read next
						c = in.read();
						
						// EOF?
						if (c < 0)
							return -1;
						
						// Single line comment?
						if (c == '/')
							this._comment = 1;
						
						// Multi-line comment?
						else if (c == '*')
							this._comment = 2;
						
						// Unknown, ignore
						else
							continue;
					}
					
					// String separator
					else if (c == ',')
					{
						// If in a bracket, return a special code
						if (this._inbracket)
							return __CharStripper__.END_OF_LINE;
						
						// Otherwise, ignore it
						continue;
					}
					
					// Unknown, ignore
					else
						continue;
				}
			}
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/22
	 */
	@Override
	public int read(char[] __b, int __o, int __l)
		throws IOException, NullPointerException
	{
		// Check
		if (__b == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0 || (__o + __l) < 0 || (__o + __l) > __b.length)
			throw new IndexOutOfBoundsException("BAOB");
		
		// Lock
		synchronized (this.lock)
		{
			// Read loop
			for (int i = 0; i < __l; i++)
				try
				{
					// Read single character
					int c = this.read();
				
					// EOF?
					if (c < 0)
						return (i > 0 ? i : -1);
				
					// Set
					__b[__o + i] = (char)c;
				}
			
				// Failed read
				catch (IOException e)
				{
					// If nothing read, fail
					if (i <= 0)
						throw e;
				
					// Otherwise return the read count
					return i;
				}
		
			// All characters read
			return __l;
		}
	}
}

