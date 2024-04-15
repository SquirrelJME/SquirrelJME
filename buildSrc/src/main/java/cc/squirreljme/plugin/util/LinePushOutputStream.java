// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Output stream for pushing lines out.
 *
 * @since 2022/09/11
 */
public abstract class LinePushOutputStream
	extends OutputStream
{
	/** The working output buffer for logging. */
	private final ByteArrayOutputStream _buffer =
		new ByteArrayOutputStream();
	
	/**
	 * Pushes the given string.
	 * 
	 * @param __string The string to push.
	 * @since 2022/09/11
	 */
	protected abstract void push(String __string);
	
	/**
	 * {@inheritDoc}
	 * @since 2022/07/01
	 */
	@Override
	public final void write(int __b)
		throws IOException
	{
		synchronized (this)
		{
			if (__b == '\r' || __b == '\n')
				this.__push((byte)__b);
			else
				this._buffer.write(__b);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/07/01
	 */
	@Override
	public final void write(byte[] __b)
		throws NullPointerException
	{
		if (__b == null)
			throw new NullPointerException("NARG");
		
		this.write(__b, 0, __b.length);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/07/01
	 */
	@Override
	public final void write(byte[] __b, int __o, int __l)
		throws IndexOutOfBoundsException, NullPointerException
	{
		if (__b == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0 || (__o + __l) < 0 || (__o + __l) > __b.length)
			throw new IndexOutOfBoundsException("IOOB");
		
		synchronized (this)
		{
			for (int i = 0; i < __l; i++)
			{
				byte b = __b[__o + i];
				
				if (b == '\r' || b == '\n')
					this.__push(b);
				else
					this._buffer.write(b);
			}
		}
	}
	
	/**
	 * Pushes the byte to the output.
	 *
	 * @param __b The byte which triggered the push.
	 * @since 2022/07/01
	 */
	private void __push(byte __b)
	{
		// Ignore carriage returns
		if (__b == '\r')
			return;
		
		// Read string to print, wipe everything for the next round
		ByteArrayOutputStream buffer = this._buffer;
		String string = buffer.toString();
		buffer.reset();
		
		// Inform subclass
		this.push(string);
	}
}
