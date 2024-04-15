// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.emulator;

import cc.squirreljme.jvm.mle.brackets.PipeBracket;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Not Described.
 *
 * @since 2022/03/19
 */
public class EmulatedPipeBracket
	implements Closeable, PipeBracket
{
	/** The input pipe. */
	final InputStream _in;
	
	/** The output pipe. */
	final OutputStream _out;
	
	/**
	 * Initializes an emulated pipe bracket.
	 * 
	 * @param __in The stream to read from.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/03/19
	 */
	public EmulatedPipeBracket(InputStream __in)
		throws NullPointerException
	{
		if (__in == null)
			throw new NullPointerException("NARG");
		
		this._in = __in;
		this._out = null;
	}
	
	/**
	 * Initializes an emulated pipe bracket.
	 * 
	 * @param __out The stream to write to.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/03/19
	 */
	public EmulatedPipeBracket(OutputStream __out)
		throws NullPointerException
	{
		if (__out == null)
			throw new NullPointerException("NARG");
		
		this._in = null;
		this._out = __out;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/03/19
	 */
	@Override
	public void close()
		throws IOException
	{
		if (this._in != null)
			this._in.close();
		
		if (this._out != null)
			this._out.close();
	}
}
