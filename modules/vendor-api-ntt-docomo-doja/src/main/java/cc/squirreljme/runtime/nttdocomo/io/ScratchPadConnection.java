// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.nttdocomo.io;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.microedition.io.StreamConnection;

/**
 * Handles the scratch pad for i-mode/i-appli applications, this is a small
 * storage area within the application.
 *
 * These are in the format of {@code scratchpad:///0;pos=0,length=0}.
 *
 * @since 2021/11/30
 */
public class ScratchPadConnection
	implements StreamConnection
{
	/** The parameters for the scratch pad. */
	private final __ScratchPadParams__ params;
	
	/** The byte area length. */
	private final int length;
	
	/** The requested scratch pad. */
	private final int pad;
	
	/** The position to start read/writes from. */
	private final int pos;
	
	/**
	 * Initializes the scratch pad connection.
	 *
	 * @param __params The scratch pad parameters.
	 * @param __pad The requested pad.
	 * @param __pos The position.
	 * @param __len The length.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/12/01
	 */
	public ScratchPadConnection(__ScratchPadParams__ __params, int __pad,
		int __pos, int __len)
		throws NullPointerException
	{
		if (__params == null)
			throw new NullPointerException("NARG");
		
		this.params = __params;
		this.pad = __pad;
		this.pos = __pos;
		this.length = __len;
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @since 2021/11/30
	 */
	@Override
	public void close()
		throws IOException
	{
		// Do nothing
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @since 2021/11/30
	 */
	@Override
	public DataInputStream openDataInputStream()
		throws IOException
	{
		return new DataInputStream(this.openInputStream());
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @since 2021/11/30
	 */
	@Override
	public DataOutputStream openDataOutputStream()
		throws IOException
	{
		return new DataOutputStream(this.openOutputStream());
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @since 2021/11/30
	 */
	@Override
	public OutputStream openOutputStream()
		throws IOException
	{
		return __ScratchPadStore__.__open(this.pad, this.params).outputStream(
			this.pos, this.length);
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @since 2021/11/30
	 */
	@Override
	public InputStream openInputStream()
		throws IOException
	{
		return __ScratchPadStore__.__open(this.pad, this.params).inputStream(
			this.pos, this.length);
	}
}
