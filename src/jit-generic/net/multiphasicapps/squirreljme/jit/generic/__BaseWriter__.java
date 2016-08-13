// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.generic;

import java.io.IOException;
import java.io.OutputStream;
import net.multiphasicapps.io.data.DataEndianess;
import net.multiphasicapps.io.data.ExtendedDataOutputStream;
import net.multiphasicapps.squirreljme.jit.base.JITException;
import net.multiphasicapps.squirreljme.os.generic.BlobContentType;
import net.multiphasicapps.squirreljme.os.generic.GenericBlob;

/**
 * This is the base class for class and resource writers, since they share
 * similar logic when it comes to output..
 *
 * @since 2016/07/27
 */
abstract class __BaseWriter__
	implements AutoCloseable
{
	/** Internal lock. */
	protected final Object lock;
	
	/** The owning namespace writer. */
	protected final GenericNamespaceWriter owner;
	
	/** Single stream output. */
	protected final ExtendedDataOutputStream output;
	
	/** The entry start address. */
	protected final int startaddr;
	
	/** The data address. */
	protected final int dataaddr;
	
	/**
	 * Initializes the base writer.
	 *
	 * @param __nsw The owning namespace writer.
	 * @param __dos The writer to use for output.
	 * @param __ct The type of blob this is.
	 * @param __name The name of this entry.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/27
	 */
	__BaseWriter__(GenericNamespaceWriter __nsw,
		ExtendedDataOutputStream __dos, BlobContentType __ct, String __name)
		throws NullPointerException
	{
		// Check
		if (__nsw == null || __ct == null || __name == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.owner = __nsw;
		this.lock = __nsw._lock;
		
		// Could fail
		try
		{
			// Align
			while ((__dos.size() & 3) != 0)
				__dos.writeByte(0);
			
			// {@squirreljme.error BA0b Start of class or resource is at a
			// position beyond 2GiB.}
			long sa = __dos.size();
			if (sa < 0 || sa > Integer.MAX_VALUE)
				throw new JITException("BA0b");
			this.startaddr = (int)sa;
			
			// Write magic
			__dos.writeInt(GenericBlob.START_ENTRY_MAGIC_NUMBER);
			__nsw.__writeString(__dos, __ct.ordinal(), __name);
			
			// Align
			while ((__dos.size() & 3) != 0)
				__dos.writeByte(0);
			
			// {@squirreljme.error BA0c The data area of a class or resource
			// exceeds beyond 2GiB.}
			long da = __dos.size();
			if (da < 0 || da > Integer.MAX_VALUE)
				throw new JITException("BA0c");
			this.dataaddr = (int)da;
			
			// Wrap output
			ExtendedDataOutputStream output;
			output = new ExtendedDataOutputStream(new __Output__(__dos));
			output.setEndianess(__dos.getEndianess());
			this.output = output;
		}
		
		// {@squirreljme.error BA0a Failed to write the entry start.}
		catch (IOException e)
		{
			throw new JITException("BA0a", e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/27
	 */
	@Override
	public void close()
		throws JITException
	{
		// Just call close on the writer
		this.owner.__close(this);
	}
	
	/**
	 * This wraps the output for writing.
	 *
	 * @since 2016/08/13
	 */
	private final class __Output__
		extends OutputStream
	{
		/** The real output to write to. */
		protected final ExtendedDataOutputStream real;
		
		/**
		 * Initializes the wrapped output.
		 *
		 * @param __r The real stream to write to.
		 * @throws NullPointerException On null arguments.
		 * @since 2016/08/19
		 */
		private __Output__(ExtendedDataOutputStream __r)
			throws NullPointerException
		{
			// Check
			if (__r == null)
				throw new NullPointerException("NARG");
			
			// Set
			this.real = __r;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/08/13
		 */
		@Override
		public void close()
			throws IOException
		{
			throw new Error("TODO");
		}
	}
}

