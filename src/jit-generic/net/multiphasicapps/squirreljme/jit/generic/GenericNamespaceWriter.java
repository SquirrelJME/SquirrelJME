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
import net.multiphasicapps.squirreljme.java.symbols.ClassNameSymbol;
import net.multiphasicapps.squirreljme.jit.base.JITCPUEndian;
import net.multiphasicapps.squirreljme.jit.base.JITException;
import net.multiphasicapps.squirreljme.jit.JITCacheCreator;
import net.multiphasicapps.squirreljme.jit.JITClassWriter;
import net.multiphasicapps.squirreljme.jit.JITNamespaceWriter;
import net.multiphasicapps.squirreljme.jit.JITOutputConfig;
import net.multiphasicapps.squirreljme.jit.JITResourceWriter;
import net.multiphasicapps.squirreljme.os.generic.GenericBlobConstants;
import net.multiphasicapps.io.data.DataEndianess;
import net.multiphasicapps.io.data.ExtendedDataOutputStream;

/**
 * This is a generic writer for namespaces which writes standard blobs which
 * are used by all architectures.
 *
 * @since 2016/07/27
 */
public final class GenericNamespaceWriter
	implements JITNamespaceWriter
{
	/** Write lock. */
	protected final Object lock =
		new Object();
	
	/** The output configuration. */
	protected final JITOutputConfig.Immutable config;
	
	/** The owner of this writer. */
	protected final GenericOutput owner;
	
	/** The namespace name. */
	protected final String namespace;
	
	/** Where data is to be written. */
	protected final ExtendedDataOutputStream output;
	
	/** Has this been closed? */
	private volatile boolean _closed;
	
	/** The position the current entry starts at. */
	private volatile long _curpos =
		-1;
	
	/**
	 * Initializes the generic namespace writer.
	 *
	 * @param __go The owning output (used to obtain code generators).
	 * @param __ns The Namespace to be written.
	 * @throws JITException If the writer could not be initialized.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/27
	 */
	GenericNamespaceWriter(GenericOutput __go, String __ns)
		throws JITException, NullPointerException
	{
		// Check
		if (__go == null || __ns == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.owner = __go;
		this.namespace = __ns;
		JITOutputConfig.Immutable config = __go.config();
		this.config = config;
		
		// Get the cache creator because all generic output is to blobs
		// {@squirreljme.error BA01 The JIT output configuration does not have
		// an associated cache generator. All generic JIT output is written
		// to the cache to be later handled. (The configuration)}
		JITCacheCreator cc = config.cacheCreator();
		if (cc == null)
			throw new JITException(String.format("BA01 %s", config));
		
		// Might fail
		try
		{
			// Create an output for the namespace writer
			ExtendedDataOutputStream output = new ExtendedDataOutputStream(
				cc.createCache(__ns));
			
			// Set endianess
			DataEndianess end;
			JITCPUEndian jitend;
			switch ((jitend = config.triplet().endianess()))
			{
				case BIG:
					end = DataEndianess.BIG;
					break;
					
				case LITTLE:
					end = DataEndianess.LITTLE;
					break;
				
					// {@squirreljme.error BA03 Do not know how to write the
					// specified endianess. (The endianess)}
				default:
					throw new JITException(String.format("BA03 %s", jitend));
			}
			output.setEndianess(end);
			
			// Write basic header so that blobs are identifiable
			output.writeLong(GenericBlobConstants.FIRST_MAGIC);
			output.writeLong(GenericBlobConstants.SECOND_MAGIC);
			
			// Set
			this.output = output;
		}
		
		// {@squirreljme.error BA02 Could not create the output cache.}
		catch (IOException e)
		{
			throw new JITException("BA02", e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/27
	 */
	@Override
	public JITClassWriter beginClass(ClassNameSymbol __cn)
		throws JITException, NullPointerException
	{
		// Check
		if (__cn == null)
			throw new NullPointerException("NARG");
		
		// Lock
		synchronized (this.lock)
		{
			// {@squirreljme.error BA04 Cannot begin a new class because the
			// current namespace writer is closed.}
			if (this._closed)
				throw new JITException("BA04");
			
			throw new Error("TODO");
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/27
	 */
	@Override
	public JITResourceWriter beginResource(String __name)
		throws JITException, NullPointerException
	{
		// Check
		if (__name == null)
			throw new NullPointerException("NARG");
		
		// Lock
		synchronized (this.lock)
		{
			// {@squirreljme.error BA05 Cannot begin a new resource because the
			// current namespace writer is closed.}
			if (this._closed)
				throw new JITException("BA05");
			
			throw new Error("TODO");
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
		// Lock
		synchronized (this.lock)
		{
			// Could fail
			try
			{
				// Write final pieces before closing
				ExtendedDataOutputStream output = this.output;
				if (!this._closed)
				{
					// Mark closed
					this._closed = true;
			
					throw new Error("TODO");
				}
		
				// Close output
				output.close();
			}
			
			// {@squirreljme.error BA06 Failed to close the generic namespace
			// writer.}
			catch (IOException e)
			{
				throw new JITException("BA06", e);
			}
		}
	}
}

