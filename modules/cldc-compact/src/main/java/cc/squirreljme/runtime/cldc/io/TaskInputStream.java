// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.io;

import cc.squirreljme.jvm.mle.TaskShelf;
import cc.squirreljme.jvm.mle.brackets.TaskBracket;
import cc.squirreljme.jvm.mle.constants.PipeErrorType;
import java.io.IOException;
import java.io.InputStream;

/**
 * This is a wrapper around
 * {@link TaskShelf#read(TaskBracket, int, byte[], int, int)}.
 *
 * @since 2020/07/02
 */
public final class TaskInputStream
	extends InputStream
{
	/** The task to read from. */
	protected final TaskBracket task;
	
	/** The pipe descriptor. */
	protected final int fd;
	
	/**
	 * @param __task The task to read from.
	 * @param __fd The file descriptor.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/07/02
	 */
	public TaskInputStream(TaskBracket __task, int __fd)
		throws NullPointerException
	{
		if (__task == null)
			throw new NullPointerException("NARG");
		
		this.task = __task;
		this.fd = __fd;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/07/02
	 */
	@SuppressWarnings("MagicNumber")
	@Override
	public int read()
		throws IOException
	{
		// There is only the bulk read operation
		for (byte[] buf = new byte[1];;)
		{
			int rc = TaskShelf.read(this.task, this.fd, buf, 0, 1);
			
			// EOF?
			if (rc == PipeErrorType.END_OF_FILE)
				return -1;
			
			// {@squirreljme.error ZZ45 I/O Error. (The error)}
			else if (rc < 0)
				throw new IOException("ZZ45 " + rc);
			
			// Missed data
			else if (rc == 0)
				continue;
			
			// Return the read byte
			return buf[0] & 0xFF;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/07/02
	 */
	@Override
	public int read(byte[] __b)
		throws IOException
	{
		if (__b == null)
			throw new NullPointerException("NARG");
		
		// Forward into
		int rv = TaskShelf.read(this.task, this.fd, __b, 0, __b.length);
		
		// {@squirreljme.error ZZ4c I/O Exception. (The error)} 
		if (rv < 0 && rv != PipeErrorType.END_OF_FILE)
			throw new IOException("ZZ4c " + rv);
		
		return rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/07/02
	 */
	@Override
	public int read(byte[] __b, int __o, int __l)
		throws IOException
	{
		if (__b == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0 || (__o + __l) > __b.length)
			throw new IndexOutOfBoundsException("IOOB");
		
		// Forward into
		int rv = TaskShelf.read(this.task, this.fd, __b, __o, __l);
		
		// {@squirreljme.error ZZ4e I/O Exception. (The error)} 
		if (rv < 0 && rv != PipeErrorType.END_OF_FILE)
			throw new IOException("ZZ4e " + rv);
		
		return rv;
	}
}
