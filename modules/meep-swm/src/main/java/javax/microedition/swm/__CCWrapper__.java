// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.swm;

import cc.squirreljme.runtime.cldc.asm.ConsoleCallback;
import java.io.IOException;
import java.io.OutputStream;

/**
 * This is a wrapper for when a task writes to its console, this will be
 * called which just forwards to the output stream specified.
 *
 * @since 2019/02/02
 */
final class __CCWrapper__
	implements ConsoleCallback
{
	/** The stream to forward to. */
	protected final OutputStream out;
	
	/**
	 * Initializes the wrapper to the given stream.
	 *
	 * @param __out The stream to write to.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/02/02
	 */
	__CCWrapper__(OutputStream __out)
		throws NullPointerException
	{
		if (__out == null)
			throw new NullPointerException("NARG");
		
		this.out = __out;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/02/02
	 */
	@Override
	public final boolean close()
	{
		try
		{
			this.out.close();
			return true;
		}
		catch (IOException e)
		{
			return false;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/02/02
	 */
	@Override
	public final boolean flush()
	{
		try
		{
			this.out.flush();
			return true;
		}
		catch (IOException e)
		{
			return false;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/02/02
	 */
	@Override
	public final boolean write(byte[] __b, int __o, int __l)
	{
		try
		{
			this.out.write(__b, __o, __l);
			return true;
		}
		catch (IOException e)
		{
			return false;
		}
	}
}

