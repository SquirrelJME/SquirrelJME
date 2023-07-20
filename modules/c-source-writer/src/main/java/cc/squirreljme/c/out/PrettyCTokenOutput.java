// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.c.out;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.IOException;

/**
 * Wraps the output and makes it very pretty.
 *
 * @since 2023/06/19
 */
public class PrettyCTokenOutput
	implements CTokenOutput
{
	/** The tab size. */
	private static final int _TAB_SIZE =
		4;
	
	/** The column limit. */
	private static final int _GUTTER =
		69;
	
	/** The output. */
	protected final CTokenOutput out;
	
	/** Current indentation level. */
	private volatile int _indent;
	
	/** The current column. */
	private volatile int _column;
	
	/**
	 * Initializes the output wrapper.
	 * 
	 * @param __wrap The output to wrap.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/06/19
	 */
	public PrettyCTokenOutput(CTokenOutput __wrap)
		throws NullPointerException
	{
		if (__wrap == null)
			throw new NullPointerException("NARG");
		
		this.out = __wrap;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/22
	 */
	@Override
	public void close()
		throws IOException
	{
		this.out.close();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/22
	 */
	@Override
	public void indent(int __adjust)
	{
		this._indent = Math.max(0, this._indent + __adjust);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/22
	 */
	@Override
	public void newLine(boolean __force)
		throws IOException
	{
		this.__newLine(__force);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/22
	 */
	@Override
	public void space()
		throws IOException
	{
		// Emit space
		this.__token(" ");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/22
	 */
	@Override
	public void tab()
		throws IOException
	{
		// Ignore explicit tabs
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/22
	 */
	@Override
	public void token(CharSequence __cq, boolean __forceNewline)
		throws IOException, NullPointerException
	{
		if (__cq == null)
			throw new NullPointerException("NARG");
		
		// Is a newline being emitted?
		int len = __cq.length();
		char first = (len == 0 ? 0 : __cq.charAt(0));
		if (len == 1 && first == '\n')
		{
			this.__newLine(__forceNewline);
			return;
		}
		
		// Ignore explicit tabs
		else if (len == 1 && first == '\t')
			return;
		
		// Open or close of brace
		else if (len == 1 && (first == '{' || first == '}'))
		{
			// Always have a new line before
			this.__newLine(true);
			
			// Emit token
			this.__token(__cq);
			
			// Always have a new line after
			this.__newLine(true);
			return;
		}
		
		// Emit token otherwise
		this.__token(__cq);
	}
	
	/**
	 * Emits a newline.
	 *
	 * @param __forceNewline Is the newline forced?
	 * @throws IOException On write errors.
	 * @since 2023/07/20
	 */
	private void __newLine(boolean __forceNewline)
		throws IOException
	{
		CTokenOutput out = this.out;
		
		// Emit newline only after the first column or when forced
		int column = this._column;
		if (column > 0 || __forceNewline)
		{
			// Emit newline
			out.newLine(true);
			
			// Reset column back to the base
			this._column = 0;
		}
	}
	
	/**
	 * Emits a token.
	 *
	 * @param __cq The sequence to emit.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/07/20
	 */
	private void __token(CharSequence __cq)
		throws IOException, NullPointerException
	{
		if (__cq == null)
			throw new NullPointerException("NARG");
		
		CTokenOutput out = this.out;
		
		// If column is past the gutter, start on a fresh line
		int column = this._column;
		if (column >= PrettyCTokenOutput._GUTTER)
		{
			this.__newLine(true);
			column = this._column;
		}
		
		// If on the first column, we need to indent
		if (column == 0)
		{
			// Get the current indentation level, to determine tabs to write
			int indent = this._indent;
			for (int i = 0; i < indent; i++)
				out.tab();
			
			// Column is at this base
			column = indent * PrettyCTokenOutput._TAB_SIZE;
			this._column = column;
		}
		
		// Emit token
		out.token(__cq, false);
		
		// Move column up
		this._column = column + __cq.length();
	}
}
