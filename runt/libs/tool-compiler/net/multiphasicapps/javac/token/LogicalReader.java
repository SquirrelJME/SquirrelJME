// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.javac.token;

import java.io.Closeable;
import java.io.IOException;
import java.io.Reader;
import net.multiphasicapps.javac.FileNameLineAndColumn;

/**
 * This is a logical reader which provides line and column position information
 * along with decoding of unicode escape sequences.
 *
 * @since 2017/09/09
 */
public class LogicalReader
	implements Closeable, FileNameLineAndColumn
{
	/** The size of tabs. */
	private static final int _TAB_SIZE =
		4;
	
	/** The reader to source from. */
	protected final Reader in;
	
	/** The file name. */
	protected final String filename;
	
	/** The current line. */
	private volatile int _line =
		1;
	
	/** The current column. */
	private volatile int _column =
		1;
	
	/** Was the last character CR? */
	private volatile boolean _wascr;
	
	/** The character which was in the queue after the slash. */
	private volatile int _slashrem =
		-1;
	
	/**
	 * Initializes the logical reader.
	 *
	 * @param __fn The name of the file.
	 * @param __r The stream to read from.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/09/09
	 */
	public LogicalReader(String __fn, Reader __r)
		throws NullPointerException
	{
		// Check
		if (__fn == null || __r == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.filename = __fn;
		this.in = __r;
	}
	
	
	/**
	 * {@inheritDoc}
	 * @since 2017/09/09
	 */
	@Override
	public void close()
		throws IOException
	{
		this.in.close();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/09/09
	 */
	@Override
	public int column()
	{
		return this._column;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/09/09
	 */
	@Override
	public int line()
	{
		return this._line;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/12
	 */
	@Override
	public String fileName()
	{
		return this.filename;
	}
	
	/**
	 * Reads the next character.
	 *
	 * @return The next character or a negative value on EOF.
	 * @throws IOException On read errors.
	 * @since 2017/09/09
	 */
	public int read()
		throws IOException
	{
		// An escape sequence was attempted to be read but it was not a
		// unicode escape sequence
		int slashrem = this._slashrem;
		if (slashrem >= 0)
		{
			this._slashrem = -1;
			return slashrem;
		}
		
		// Usually a single character will be read from the source unless it is
		// a escape sequence for unicode characters
		Reader in = this.in;
		boolean escaped = false,
			unicode = false;
		int unicodeval = 0,
			unicodemask = 0;
		for (;;)
		{
			// Full unicode mask read, return the read value
			if (unicode && unicodemask >= 0xFFFF)
				return unicodeval;
			
			// EOF?
			int c = in.read();
			if (c < 0)
			{
				// {@squirreljme.error AQ0c Unicode sequence truncated at
				// end of file.}
				if (unicode)
					throw new TokenizerException(this, "AQ0c");
				
				// Make sure the escape is returned before EOF
				if (escaped)
					return '\\';
				return -1;
			}
			
			// Reset carriage return check, this is for Windows line endings
			// which consist of CRLF pairs which must not be incremented as
			// if they were two lines
			boolean wascr = this._wascr;
			this._wascr = (c == '\r');
			
			// Do line/column counting now because some characters could end
			// up taking up much more space especially if they are escape
			// sequences. Logically there could be boop\nbap where the \n is
			// unicode escaped. For text editor compatibility that is not
			// treated as a new line when related to the count
			if (c == '\n' || wascr)
			{
				// Go to the next line
				this._line++;
				this._column = 1;
			}
			
			// Tab sets to the tab size
			else if (c == '\t')
			{
				int column = this._column;
				column = column + (_TAB_SIZE - (column % _TAB_SIZE));
				this._column = column;
			}
			
			// Single character increase
			else
				this._column++;
			
			// Reading unicode sequence
			if (unicode)
			{
				// Calculate hex value
				int val = Character.digit((char)c, 16);
				
				// Reading digits
				if (val >= 0 && unicodemask < 0xFFFF)
				{
					// Shift in value
					unicodeval <<= 4;
					unicodeval |= val;
					
					// Move mask up
					unicodemask <<= 4;
					unicodemask |= 0xF;
					
					// Read more of the value
					continue;
				}
				
				// Either another character or something else?
				else
				{
					// {@squirreljme.error AQ0d Invalid unicode escape
					// sequence. (The line; The column)}
					if (unicodemask != 0 || c != 'u')
						throw new TokenizerException(this, 
							String.format("AQ0d %d %d", this._line,
							this._column));
					
					// Just skip the u, there can be tons of them
					continue;
				}
			}
			
			// In escape mode
			else if (escaped)
			{
				// Unicode escape sequence?
				if (c == 'u')
				{
					// Enter unicode mode so that the sequence gets
					// counted properly
					unicode = true;
					continue;
				}
				
				// Not one, recycle it
				else
				{
					this._slashrem = c;
					return '\\';
				}
			}
			
			// not escaped
			else
			{
				// Could be a unicode sequence
				if (c == '\\')
				{
					escaped = true;
					continue;
				}
				
				// Return normal character
				else
					return c;
			}
		}
	}
}

