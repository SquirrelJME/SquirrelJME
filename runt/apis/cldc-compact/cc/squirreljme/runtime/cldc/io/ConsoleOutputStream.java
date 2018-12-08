// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.io;

import cc.squirreljme.runtime.cldc.asm.ConsoleOutput;
import java.io.IOException;
import java.io.OutputStream;

/**
 * This provides an output stream which writes to a console file descriptor.
 *
 * @since 2018/12/08
 */
public final class ConsoleOutputStream
	extends OutputStream
{
	/** the file descriptor to write to. */
	protected final int fd;
	
	/**
	 * Initializes the output stream.
	 *
	 * @param __fd The descriptor to write to.
	 * @since 2018/12/08
	 */
	public ConsoleOutputStream(int __fd)
	{
		this.fd = __fd;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/08
	 */
	@Override
	public void flush()
		throws IOException
	{
		// {@squirreljme.error ZZ3m Could not flush the console.}
		if (ConsoleOutput.flush(this.fd) < 0)
			throw new IOException("ZZ3m");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/06/16
	 */
	@Override
	public void write(int __b)
		throws IOException
	{
		// {@squirreljme.error ZZ08 Error writing to console.}
		if (ConsoleOutput.write(this.fd, __b) != 0)
			throw new IOException("ZZ08");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/05
	 */
	@Override
	public void write(byte[] __b)
		throws IOException, NullPointerException
	{
		if (__b == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error ZZ3a Error writing to console.}
		if (ConsoleOutput.write(this.fd, __b, 0, __b.length) < 0)
			throw new IOException("ZZ3a");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/05
	 */
	@Override
	public void write(byte[] __b, int __o, int __l)
		throws IndexOutOfBoundsException, IOException, NullPointerException
	{
		if (__b == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0 || (__o + __l) > __b.length)
			throw new IndexOutOfBoundsException("IOOB");
		
		// {@squirreljme.error ZZ38 Error writing to console.}
		if (ConsoleOutput.write(this.fd, __b, __o, __l) < 0)
			throw new IOException("ZZ38");
	}
}

