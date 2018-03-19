// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirrelquarrel.util;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * This allows for easier writing to the replay output without requiring that
 * {@link IOException} be caught each time.
 *
 * @since 2018/03/19
 */
public final class ReplayOutputStream
{
	/** The stream to write to. */
	protected final DataOutputStream out;
	
	/**
	 * Initializes the output stream.
	 *
	 * @param __out The stream to write to.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/19
	 */
	public ReplayOutputStream(OutputStream __out)
		throws NullPointerException
	{
		if (__out == null)
			throw new NullPointerException("NARG");
		
		this.out = (__out instanceof DataOutputStream ?
			(DataOutputStream)__out : new DataOutputStream(__out));
	}
	
	/**
	 * Writes a byte value.
	 *
	 * @param __v The value to write.
	 * @throws ReplayIOException If it could not be written.
	 * @since 2018/03/19
	 */
	public void writeByte(int __v)
		throws ReplayIOException
	{
		try
		{
			this.out.writeByte(__v);
		}
		
		// {@squirreljme.error BE0s Could not write the byte value.
		// (The value to write)}
		catch (IOException e)
		{
			throw new ReplayIOException(String.format("BE0s %d", __v), e);
		}
	}
	
	/**
	 * Writes an integer value.
	 *
	 * @param __v The value to write.
	 * @throws ReplayIOException If it could not be written.
	 * @since 2018/03/19
	 */
	public void writeInt(int __v)
		throws ReplayIOException
	{
		try
		{
			this.out.writeInt(__v);
		}
		
		// {@squirreljme.error BE0o Could not write the integer value.
		// (The value to write)}
		catch (IOException e)
		{
			throw new ReplayIOException(String.format("BE0o %d", __v), e);
		}
	}
	
	/**
	 * Writes a long value.
	 *
	 * @param __v The value to write.
	 * @throws ReplayIOException If it could not be written.
	 * @since 2018/03/19
	 */
	public void writeLong(long __v)
		throws ReplayIOException
	{
		try
		{
			this.out.writeInt((int)(__v >> 32));
			this.out.writeInt((int)__v);
		}
		
		// {@squirreljme.error BE0p Could not write the long value.
		// (The value to write)}
		catch (IOException e)
		{
			throw new ReplayIOException(String.format("BE0p %d", __v), e);
		}
	}
}

