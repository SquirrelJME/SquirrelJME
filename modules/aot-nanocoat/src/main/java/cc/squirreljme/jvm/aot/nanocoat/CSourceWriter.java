// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot.nanocoat;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.cldc.io.PrintStreamWriter;
import cc.squirreljme.runtime.cldc.util.CharArrayCharSequence;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Writer;

/**
 * Writer for C source code.
 *
 * @since 2023/05/28
 */
public class CSourceWriter
	extends Writer
{
	/** The stream to write to. */
	private final PrintStreamWriter out;
	
	/** The current line. */
	private volatile int _line;
	
	/** The current column. */
	private volatile int _column;
	
	/** The last character written. */
	private volatile char _lastChar;
	
	/**
	 * Initializes the C source writer.
	 * 
	 * @param __out The stream output.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/05/28
	 */
	public CSourceWriter(PrintStream __out)
		throws NullPointerException
	{
		if (__out == null)
			throw new NullPointerException("NARG");
		
		this.out = new PrintStreamWriter(__out);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/05/28
	 */
	@Override
	public void close()
		throws IOException
	{
		throw Debugging.todo();
	}
	
	/**
	 * Flushes the output.
	 * 
	 * @throws IOException On flush errors.
	 * @since 2023/05/28
	 */
	@Override
	public void flush()
		throws IOException
	{
		this.out.flush();
	}
	
	/**
	 * Start on a fresh line.
	 * 
	 * @throws IOException On write errors.
	 * @since 2023/05/28
	 */
	private void freshLine()
		throws IOException
	{
		// Only need to do this if we are not at the line start
		if (this._column > 0)
			this.write("\n");
	}
	
	/**
	 * Outputs a preprocessor line.
	 * 
	 * @param __symbol The preprocesor symbol. 
	 * @param __format The format.
	 * @param __args The arguments to the format.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/05/28
	 */
	protected void preprocessorLine(String __symbol,
		String __format, Object... __args)
		throws IOException, NullPointerException
	{
		if (__symbol == null || __format == null)
			throw new NullPointerException("NARG");
		
		// Start on fresh line?
		this.freshLine();
		
		// Write line start
		this.write("#");
		this.write(__symbol);
		
		// Space
		this.write(" ");
		
		// Write the preprocessor line
		this.write(String.format(__format, __args));
		this.write("\n");
			
		// End of a fresh line
		this.freshLine();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/05/28
	 */
	@Override
	public void write(char[] __c, int __o, int __l)
		throws IOException
	{
		// Write to output
		char lastChar = this._lastChar;
		for (int i = 0; i < __l; i++, __o++)
		{
			char c = __c[__o];
			
			// New line?
			if (c == '\r' || c == '\n')
			{
				// Do not write double newlines
				if (lastChar == '\r' || lastChar == '\n')
					continue;
				
				// Move line ahead
				this._line++;
				this._column = 0;
			}
			
			// Align tabs always to four columns
			else if (c == '\t')
				this._column += 4 - (this._column % 4);
				
			// Otherwise, increase column size
			else
				this._column++;
			
			// Just write single character, as long as it is not CR
			if (c != '\r')
			{
				// Debug
				if (c == '\n')
					System.err.println();
				else
					System.err.print(__c[__o]);
				
				// Write to output file
				this.out.write(__c, __o, 1);
			}
			lastChar = c;
		}
		
		// Store last character for later writes
		this._lastChar = lastChar;
	}
}
