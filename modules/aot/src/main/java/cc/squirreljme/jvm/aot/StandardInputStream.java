// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot;

import cc.squirreljme.jvm.mle.TerminalShelf;
import cc.squirreljme.jvm.mle.brackets.PipeBracket;
import cc.squirreljme.jvm.mle.constants.PipeErrorType;
import cc.squirreljme.jvm.mle.constants.StandardPipeType;
import java.io.IOException;
import java.io.InputStream;

/**
 * This is an input stream which handles standard input in SquirrelJME.
 * 
 * This class is specific to SquirrelJME as Java ME lacks standard input of
 * any kind.
 *
 * @since 2020/11/22
 */
public final class StandardInputStream
	extends InputStream
{
	/** Standard input pipe bracket. */
	final PipeBracket _in =
		TerminalShelf.fromStandard(StandardPipeType.STDIN);
	
	/**
	 * {@inheritDoc}
	 * @since 2020/11/22
	 */
	@Override
	public int available()
		throws IOException
	{
		return StandardInputStream.__checkError(
			TerminalShelf.available(this._in), false);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/11/22
	 */
	@SuppressWarnings("ResultOfMethodCallIgnored")
	@Override
	public void close()
		throws IOException
	{
		StandardInputStream.__checkError(
			TerminalShelf.close(this._in), false);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/11/22
	 */
	@Override
	public int read()
		throws IOException
	{
		for (byte[] buf = new byte[1];;)
		{
			// Read single byte
			int code = StandardInputStream.__checkError(
				TerminalShelf.read(this._in, buf, 0, 1),
				true);
			
			// Read nothing?
			if (code == 0)
				continue;
			
			// EOF?
			if (code == PipeErrorType.END_OF_FILE)
				return -1;
			
			return buf[0] & 0xFF;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/11/22
	 */
	@Override
	public int read(byte[] __b)
		throws IOException
	{
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
	
		return StandardInputStream.__checkError(
			TerminalShelf.read(this._in, __b, __o,
				__l), true);
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
	private static int __checkError(int __code, boolean __eofOkay)
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
