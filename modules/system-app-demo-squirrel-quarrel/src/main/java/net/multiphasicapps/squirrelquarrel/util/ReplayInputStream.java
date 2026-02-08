// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirrelquarrel.util;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * This allows for easier reading from the replay input without requiring that
 * {@link IOException} be caught each time.
 *
 * @since 2018/03/19
 */
public final class ReplayInputStream
{
	/** The stream to read from. */
	protected final DataInputStream in;
	
	/**
	 * Initializes the input stream.
	 *
	 * @param __in The stream to read from.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/19
	 */
	public ReplayInputStream(InputStream __in)
		throws NullPointerException
	{
		if (__in == null)
			throw new NullPointerException("NARG");
		
		this.in = (__in instanceof DataInputStream ? (DataInputStream)__in :
			new DataInputStream(__in));
	}
	
	/**
	 * Reads a byte value.
	 *
	 * @return The read value.
	 * @throws ReplayIOException On read errors.
	 * @since 2018/03/29
	 */
	public final int readByte()
		throws ReplayIOException
	{
		try
		{
			return this.in.readByte();
		}
		
		/* {@squirreljme.error BE0i Could not read byte from the input.} */
		catch (IOException e)
		{
			throw new ReplayIOException("BE0i", e);
		}
	}
	
	/**
	 * Reads an integer value.
	 *
	 * @return The read value.
	 * @throws ReplayIOException On read errors.
	 * @since 2018/03/29
	 */
	public final int readInt()
		throws ReplayIOException
	{
		try
		{
			return this.in.readInt();
		}
		
		/* {@squirreljme.error BE0j Could not read integer from the input.} */
		catch (IOException e)
		{
			throw new ReplayIOException("BE0j", e);
		}
	}
	
	/**
	 * Reads a long value.
	 *
	 * @return The read value.
	 * @throws ReplayIOException On read errors.
	 * @since 2018/03/29
	 */
	public final long readLong()
		throws ReplayIOException
	{
		try
		{
			long rv = this.in.readInt();
			rv <<= 32;
			rv |= (this.in.readInt() & 0xFFFFFFFF);
			return rv;
		}
		
		/* {@squirreljme.error BE0k Could not read long from the input.} */
		catch (IOException e)
		{
			throw new ReplayIOException("BE0k", e);
		}
	}
}

