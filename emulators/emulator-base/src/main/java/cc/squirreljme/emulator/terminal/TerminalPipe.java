// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.emulator.terminal;

import cc.squirreljme.emulator.MLECallWouldFail;
import java.io.Closeable;
import java.io.IOException;

/**
 * This is the base class for a single output pipe.
 *
 * @since 2020/07/06
 */
public interface TerminalPipe
	extends Closeable
{
	/**
	 * Closes the output.
	 * 
	 * @throws IOException On close errors.
	 * @since 2020/07/06
	 */
	@Override
	void close()
		throws IOException;
	
	/**
	 * Flushes the output.
	 * 
	 * @throws IOException On flush errors.
	 * @since 2020/07/06
	 */
	void flush()
		throws IOException;
	
	/**
	 * @param __b The buffer.
	 * @param __o The offset into the buffer.
	 * @param __l The length.
	 * @return The number of read bytes or a negative value on EOF.
	 * @throws IOException On read errors.
	 * @throws MLECallWouldFail If the offset and/or length are
	 * negative or exceed the array bounds or on null arguments.
	 * @since 2020/07/06
	 */
	int read(byte[] __b, int __o, int __l)
		throws IOException, MLECallWouldFail;
	
	/**
	 * Writes a single byte.
	 * 
	 * @param __b The byte to write.
	 * @throws IOException On write errors.
	 * @since 2020/07/06
	 */
	void write(int __b)
		throws IOException;
	
	/**
	 * Writes multiple bytes to the buffer.
	 * 
	 * @param __b The buffer.
	 * @param __o The offset into the buffer.
	 * @param __l The length.
	 * @throws IOException On write errors.
	 * @throws MLECallWouldFail If the offset and/or length are
	 * negative or exceed the array bounds.
	 * @since 2020/07/06
	 */
	void write(byte[] __b, int __o, int __l)
		throws IOException, MLECallWouldFail;
}
