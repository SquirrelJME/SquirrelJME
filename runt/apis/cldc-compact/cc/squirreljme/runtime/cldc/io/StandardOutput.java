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
 * This wraps the standard default output stream and writes to the system
 * call interface.
 *
 * @since 2016/06/16
 */
public final class StandardOutput
	extends OutputStream
{
	/**
	 * {@inheritDoc}
	 * @since 2016/06/16
	 */
	@Override
	public void write(int __b)
		throws IOException
	{
		// {@squirreljme.error ZZ08 Error writing to standard output.}
		if (ConsoleOutput.write(ConsoleOutput.OUTPUT, __b) != 0)
			throw new IOException("ZZ08");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/08/07
	 */
	@Override
	public void write(byte[] __b, int __o, int __l)
		throws IOException, NullPointerException
	{
		if (__b == null)
			throw new NullPointerException("NARG");
		
		for (int i = __o, e = (__o + __l); i < e; i++)
			this.write(__b[i]);
	}
}

