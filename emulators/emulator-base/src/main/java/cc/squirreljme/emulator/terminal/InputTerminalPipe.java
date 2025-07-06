// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.emulator.terminal;

import cc.squirreljme.emulator.MLECallWouldFail;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.IOException;
import java.io.InputStream;

/**
 * An input terminal pipe.
 *
 * @since 2025/07/06
 */
public class InputTerminalPipe
	implements TerminalPipe
{
	/** The input stream. */
	protected final InputStream in;
	
	/**
	 * The pipe to read from.
	 *
	 * @param __in The input stream.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/07/06
	 */
	public InputTerminalPipe(InputStream __in)
		throws NullPointerException
	{
		if (__in == null)
			throw new NullPointerException("NARG");
		
		this.in = __in;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/07/06
	 */
	@Override
	public void close()
		throws IOException
	{
		this.in.close();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/07/06
	 */
	@Override
	public void flush()
		throws IOException
	{
		throw new MLECallWouldFail("Cannot write an input pipe.");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/07/06
	 */
	@Override
	public int read()
		throws IOException, MLECallWouldFail
	{
		return this.in.read();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/07/06
	 */
	@Override
	public int read(byte[] __b, int __o, int __l)
		throws IOException, MLECallWouldFail
	{
		if (__b == null || __o < 0 || __l < 0 || (__o + __l) < 0 ||
			(__o + __l) > __b.length)
			throw new MLECallWouldFail("Null or out of bounds read."); 
		
		return this.in.read(__b, __o, __l);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/07/06
	 */
	@Override
	public void write(int __b)
		throws IOException
	{
		throw new MLECallWouldFail("Cannot write an input pipe.");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/07/06
	 */
	@Override
	public void write(byte[] __b, int __o, int __l)
		throws IOException, MLECallWouldFail
	{
		throw new MLECallWouldFail("Cannot write an input pipe.");
	}
}
