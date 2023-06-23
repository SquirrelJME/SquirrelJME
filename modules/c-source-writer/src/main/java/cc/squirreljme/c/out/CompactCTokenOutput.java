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
 * Wraps the output and makes it very compact.
 *
 * @since 2023/06/19
 */
public class CompactCTokenOutput
	implements CTokenOutput
{
	/** The output to wrap. */
	protected final CTokenOutput out;
	
	/** Was the last token output a space? */
	private volatile boolean _lastWhitespace;
	
	/**
	 * Initializes the output wrapper.
	 * 
	 * @param __wrap The output to wrap.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/06/19
	 */
	public CompactCTokenOutput(CTokenOutput __wrap)
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
		// Ignore all indentation, we do not care for it here
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/22
	 */
	@Override
	public void newLine(boolean __force)
		throws IOException
	{
		// Only add newline if forced
		if (__force)
		{
			this.out.newLine(true);
			
			// We did whitespace here
			this._lastWhitespace = true;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/22
	 */
	@Override
	public void space()
		throws IOException
	{
		this.__space();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/22
	 */
	@Override
	public void tab()
		throws IOException
	{
		// Treat tabs as spaces
		this.__space();
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
		
		// Always forward these
		this.out.token(__cq, __forceNewline);
		
		// If we forced a newline then we already have the whitespace there
		// so we do not need to emit it at the end
		this._lastWhitespace = __forceNewline;
	}
	
	/**
	 * Handles both spaces and tabs so that they are only emitted once.
	 * 
	 * @throws IOException On write errors.
	 * @since 2023/06/22
	 */
	private void __space()
		throws IOException
	{
		// Do not emit multiple spaces
		if (!this._lastWhitespace)
		{
			this.out.space();
			
			// Do not emit more whitespace
			this._lastWhitespace = true;
		}
	}
}
