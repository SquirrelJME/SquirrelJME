// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.gcf;

import java.io.IOException;
import java.io.OutputStream;
import net.multiphasicapps.squirreljme.unsafe.SquirrelJME;

/**
 * This wraps the mailbox datagram connection for output.
 *
 * @since 2016/10/13
 */
class __IMCOutputStream__
	extends OutputStream
{
	/** The mailbox descriptor. */
	private final int _fd;
	
	/**
	 * Initializes the output stream.
	 *
	 * @param __fd The descriptor to write to.
	 * @since 2016/10/13
	 */
	__IMCOutputStream__(int __fd)
	{
		this._fd = __fd;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/13
	 */
	@Override
	public void close()
		throws IOException
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/13
	 */
	@Override
	public void flush()
		throws IOException
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/13
	 */
	@Override
	public void write(int __b)
		throws IOException
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/13
	 */
	@Override
	public void write(byte[] __b, int __o, int __l)
		throws ArrayIndexOutOfBoundsException, IOException,
			NullPointerException
	{
		throw new Error("TODO");
	}
}

