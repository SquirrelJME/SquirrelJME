// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.jsr353;

import com.oracle.json.JsonException;
import com.oracle.json.stream.JsonLocation;
import com.oracle.json.stream.JsonParsingException;
import java.io.Closeable;
import java.io.IOException;
import java.io.Reader;

/**
 * This reads input from a {@link Reader}.
 *
 * @since 2014/08/03
 */
public class ReaderInput
	extends BaseDecoder.Input
	implements Closeable
{
	/** Internal reader capable of handling lines and columns. */
	protected final LineColumnReader lcr;
	
	/** Peeked value. */
	private int _peek =
		Integer.MIN_VALUE;
	
	/** Has been closed. */
	private boolean _closed;
	
	/**
	 * Parses data from the specified stream and interprets as tokens.
	 *
	 * @param __r Input reader to parse data from.
	 * @since 2014/08/03
	 */
	public ReaderInput(Reader __r)
	{
		
		// Cannot be null
		if (__r == null)
			throw new NullPointerException("noin");
		
		// Use line reader as the base
		this.lcr = new LineColumnReader(__r, LineColumnReader.DEFAULT);
	}
	
	/**
	 * Returns the current JSON location.
	 *
	 * @return The current location.
	 * @since 2014/08/04
	 */
	@Override
	public JsonLocation getLocation()
	{
		synchronized (this.ilock)
		{
			// Cannot have been closed
			if (this._closed)
				throw new IllegalStateException("closedin");
		
			// Replaced by sub-classes if they support this.
			return new SomeLocation(this.lcr.getLineNumber() + 1L, this.lcr.getColumnNumber());
		}
	}
	
	/**
	 * Closes the input.
	 *
	 * @throws JsonException If it cannot be closed.
	 * @since 2014/08/03
	 */
	@Override
	public void close()
	{
		synchronized (this.ilock)
		{
			// Ignore if closed already
			if (this._closed)
				return;
			
			// Set closed
			this._closed = true;
			
			// Closing may cause an exception
			try
			{
				this.lcr.close();
			}
			
			// Oops
			catch (IOException ioe)
			{
				throw new JsonException("clioe", ioe);
			}
		}
	}
	
	/**
	 * Reads raw tokens from the reader.
	 *
	 * @return The raw type of token just read, {@code null} if there is
	 * nothing left.
	 * @throws JsonException Any internal reading errors possibly.
	 * @throws JsonParsingException On any parser errors.
	 * @since 2014/08/03
	 */
	@Override
	protected Data next()
	{
		synchronized (this.ilock)
		{
			// Cannot have been closed
			if (this._closed)
				throw new IllegalStateException("closedin");
			
			// Obtain the next one
			try
			{
				return this.__nextInternal();
			}
		
			// Error reading from stream.
			catch (IOException ioe)
			{
				throw new JsonException("clioe", ioe);
			}
		}
	}
	
	/**
	 * Internally reads the token, for {@link IOException} wrapping with less
	 * indentation.
	 *
	 * @return Type of read token, or {@code null}.
	 * @throws JsonParsingException On parser errors.
	 * @since 2014/08/04
	 */
	private Data __nextInternal()
		throws IOException
	{
		// Reading loop
		for (;;)
		{
			// Read character
			JsonLocation jl = this.getLocation();
			int c = this.__read();
			if (c < 0)	// EOF
				return null;
			
			// Skip whitespace
			if (c == '\t' || c == '\n' || c == '\r' || c == ' ')
				continue;
			
			// Start Object
			if (c == '{')
				return new Data(Type.START_OBJECT, null);
			
			// End object
			else if (c == '}')
				return new Data(Type.END_OBJECT, null);
			
			// Start array
			else if (c == '[')
				return new Data(Type.START_ARRAY, null);
			
			// End array
			else if (c == ']')
				return new Data(Type.END_ARRAY, null);
			
			// String
			else if (c == '\"')
			{
				StringBuilder sb = new StringBuilder();
				
				// Read remaining string bits
				boolean esc = false;
				for (;;)
				{
					// Read character
					c = this.__read();
					
					// Cannot be EOF or any non-space whitespace
					if (c < 0 || c == '\t' || c == '\r' || c == '\n')
						throw new JsonParsingException("seol",
							jl);
					
					// Escaped
					if (esc)
					{
						// Complex unicode sequence
						if (c == 'u')
						{
							int[] hex = new int[4];
							for (int i = 0; i < 4; i++)
							{
								// Read char, cannot be EOF
								if ((hex[i] = this.__read()) < 0)
									throw new JsonParsingException(
										"ueof", jl);
								
								// Read hex digit
								int dig = Character.digit(
									(char)hex[i], 16);
								
								// Not a digit
								if (dig < 0)
									throw new JsonParsingException(
										String.format("bx", hex[i]), jl);
								
								// Shrunk down
								hex[i] = dig;
							}
							
							// Build sequence
							c = (hex[0] << 12) | (hex[1] << 8) | (hex[2] << 4)
								| hex[3];
						}
						
						// Not complex
						else
							switch (c)
							{
								case '\"': c = '\"'; break;
								case '\\': c = '\\'; break;
								case '/': c = '/'; break;
								case 'b': c = '\b'; break;
								case 'f': c = '\f'; break;
								case 'n': c = '\n'; break;
								case 'r': c = '\r'; break;
								case 't': c = '\t'; break;
								default:
									throw new JsonParsingException(
										String.format("illesc", c), jl);
							}
						
						// Append normal and stop escaping
						sb.append((char)c);
						esc = false;
					}
					
					// Not escaped
					else
					{
						// End string
						if (c == '\"')
							break;
						
						// Escape
						else if (c == '\\')
							esc = true;
						
						// Put into token
						else
							sb.append((char)c);
					}
				}
				
				// Is a string
				return new Data(Type.STRING, sb.toString());
			}
			
			// Start reading value
			else if (c == ':')
				return new Data(Type.COLON, null);
			
			// Next element
			else if (c == ',')
				return new Data(Type.COMMA, null);
			
			// Numbers
			else if (c == '-' || (c >= '0' && c <= '9'))
			{
				StringBuilder sb = new StringBuilder();
				
				// Read flags
				boolean diddeci = false;
				boolean didexpo = false;
				boolean cansign = false;
				
				// Continual read
				for (;;)
				{
					// Reached EOF
					if (c < 0)
						throw new JsonParsingException("eoflit",
							jl);
					
					// Bad character
					if (!(c == '-' || c == '+' || (c >= '0' && c <= '9') ||
						c == 'e' || c == 'E' || c == '.'))
						throw new JsonParsingException(String.format(
							"illgnum", (char)c), jl);
					
					// Add
					sb.append((char)c);
					
					// Peek next character, if it is a numerical form then
					// continue
					c = this.__peek();
					if (c == '-' || c == '+' || (c >= '0' && c <= '9') ||
						c == 'e' || c == 'E' || c == '.')
					{
						// Make big E, a little e
						if (c == 'E')
							c = 'e';
						
						// Is a sign
						if (c == '-' || c == '+')
						{
							if (!cansign)
								throw new JsonParsingException("badnumsign", jl);
							cansign = false;
						}
						
						// Never sign
						else
							cansign = false;
						
						// Exponent
						if (c == 'e')
						{
							if (didexpo)
								throw new JsonParsingException("mulexpo", jl);
							didexpo = true;
							
							// Could sign
							cansign = true;
						}
						
						// Decimal point
						if (c == '.')
						{
							if (diddeci)
								throw new JsonParsingException("muldeci", jl);
							diddeci = true;
						}
						
						c = this.__read();
					}
					
					// Otherwise stop
					else
						break;
				}
				
				// A literal
				return new Data(Type.LITERAL, sb.toString());
			}
			
			// False, true, or null
			else if (c == 'f' || c == 't' || c == 'n')
			{
				StringBuilder sb = new StringBuilder();
				
				// True, false, or null?
				boolean t = (c == 't');
				boolean n = (c == 'n');
				boolean f = (c == 'f');
				
				// Length to read
				int rl = (t || n ? 4 : 5);
				
				// Read input literal
				sb.append((char)c);
				for (int i = 1; i < rl; i++)
				{
					// Read
					if ((c = this.__read()) < 0)
						throw new JsonParsingException("eoflit", jl);
					
					// Append
					sb.append((char)c);
				}
				
				// As string
				String ls = sb.toString();
				
				// Not the correct literal
				if (!ls.equals("true") && !ls.equals("false") &&
					!ls.equals("null"))
					throw new JsonParsingException(
						String.format("illlit", ls),
						jl);
				
				// A literal
				return new Data(Type.LITERAL, sb.toString());
			}
			
			// Unknown
			else
				throw new JsonParsingException(String.format(
					"illchart", (char)c), jl);
		}
	}
	
	/**
	 * This returns the next value that would be read from the input reader.
	 *
	 * @return The read character.
	 * @throws IOException On a bad read.
	 * @since 2015/03/09
	 */
	private final int __peek()
		throws IOException
	{
		// Peeked already
		if (this._peek > Integer.MIN_VALUE)
			return this._peek;
		
		// Otherwise read
		this._peek = this.lcr.read();
		return this._peek;
	}
	
	/**
	 * This returns the next value and removes it from the peek list.
	 *
	 * @return The next character, removing any peeked value.
	 * @throws IOException On a bad read.
	 * @since 2015/03/09
	 */
	private final int __read()
		throws IOException
	{
		// Loop for queue
		for (;;)
		{
			// In queue
			if (this._peek > Integer.MIN_VALUE)
			{
				int rv = this._peek;
				this._peek = Integer.MIN_VALUE;
				return rv;
			}
			
			// Peek
			this.__peek();
		}
	}
}

