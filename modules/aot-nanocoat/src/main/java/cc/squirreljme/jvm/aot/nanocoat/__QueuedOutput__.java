// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot.nanocoat;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.ref.Reference;

/**
 * Handles storing the file buffer for file output.
 *
 * @since 2023/08/12
 */
final class __QueuedOutput__
	extends OutputStream
{
	/** Temporary data buffer. */
	protected final ByteArrayOutputStream data =
		new ByteArrayOutputStream();
	
	/** The owning queue. */
	protected final Reference<ArchiveOutputQueue> owner;
	
	/** Has this been closed? */
	private volatile boolean _isClosed;
	
	/**
	 * Initializes the data queue.
	 *
	 * @param __owner The owner of this, where the data is written to on close.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/08/12
	 */
	__QueuedOutput__(Reference<ArchiveOutputQueue> __owner)
		throws NullPointerException
	{
		if (__owner == null)
			throw new NullPointerException("NARG");
		
		this.owner = __owner;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/08/12
	 */
	@Override
	public void close()
		throws IOException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/08/12
	 */
	@Override
	public void write(int __b)
		throws IOException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/08/12
	 */
	@Override
	public void write(byte[] __b)
		throws IOException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/08/12
	 */
	@Override
	public void write(byte[] __b, int __o, int __l)
		throws IOException
	{
		throw Debugging.todo();
	}
}
