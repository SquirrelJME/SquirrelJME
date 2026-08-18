// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.io;

import cc.squirreljme.jvm.mle.TerminalShelf;
import cc.squirreljme.jvm.mle.brackets.PipeBracket;
import cc.squirreljme.jvm.mle.constants.PipeErrorType;
import cc.squirreljme.jvm.mle.constants.StandardPipeType;
import cc.squirreljme.runtime.cldc.annotation.KeepWhenCompacting;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import java.io.IOException;
import java.io.InputStream;

/**
 * This is an input stream which reads from a {@link PipeBracket}.
 * 
 * This class is specific to SquirrelJME as Java ME lacks standard input of
 * any kind.
 *
 * @since 2020/11/22
 */
@SquirrelJMEVendorApi
public class PipeInputStream
	extends InputStream
{
	/** The pipe to read from. */
	@SquirrelJMEVendorApi
	protected final PipeBracket pipe;
	
	/**
	 * Initializes the pipe reader.
	 *
	 * @param __pipe The pipe to read from.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/07/06
	 */
	@SquirrelJMEVendorApi
	public PipeInputStream(PipeBracket __pipe)
		throws NullPointerException
	{
		if (__pipe == null)
			throw new NullPointerException("NARG");
		
		this.pipe = __pipe;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/11/22
	 */
	@Override
	public int available()
		throws IOException
	{
		return PipeInputStream.__checkError(
			TerminalShelf.available(this.pipe), false);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/11/22
	 */
	@Override
	public void close()
		throws IOException
	{
		PipeInputStream.__checkError(
			TerminalShelf.close(this.pipe), false);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/11/22
	 */
	@Override
	public int read()
		throws IOException
	{
		// Read single byte
		return PipeInputStream.__checkError(
			TerminalShelf.read(this.pipe), true);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/11/22
	 */
	@Override
	public int read(byte[] __b)
		throws IOException
	{
		if (__b == null)
			throw new NullPointerException("NARG");
		
		return this.read(__b, 0, __b.length);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/11/22
	 */
	@Override
	public int read(byte[] __b, int __o, int __l)
		throws IndexOutOfBoundsException, IOException, NullPointerException
	{
		if (__b == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0 || (__o + __l) < 0 || (__o + __l) > __b.length)
			throw new IndexOutOfBoundsException("IOOB");
	
		return PipeInputStream.__checkError(
			TerminalShelf.read(this.pipe, __b, __o,
				__l), true);
	}
	
	/**
	 * Opens a pipe stream from standard input.
	 *
	 * @return The resultant pipe.
	 * @since 2025/07/06
	 */
	@SquirrelJMEVendorApi
	public static PipeInputStream stdIn()
	{
		return new PipeInputStream(
			TerminalShelf.fromStandard(StandardPipeType.STDIN));
	}
	
	/**
	 * Checks for error result codes from standard input.
	 * 
	 * @param __code The code to check.
	 * @param __eofOkay If end of file is okay and is not treated as an error.
	 * @return {@code __code}.
	 * @throws IOException If the code indicates an error.
	 * @since 2020/11/22
	 */
	@KeepWhenCompacting
	static int __checkError(int __code, boolean __eofOkay)
		throws IOException
	{
		if (__code < 0)
		{
			if (__eofOkay && __code == PipeErrorType.END_OF_FILE)
				return PipeErrorType.END_OF_FILE;
			
			/* {@squirreljme.error AE05 I/O Exception reading from standard
			input. (The error)} */
			throw new IOException("AE05 " + __code);
		}
		
		return __code;
	}
}
