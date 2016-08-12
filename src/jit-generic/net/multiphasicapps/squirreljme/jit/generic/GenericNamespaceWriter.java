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

import java.io.OutputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import net.multiphasicapps.io.data.DataEndianess;
import net.multiphasicapps.io.data.ExtendedDataOutputStream;
import net.multiphasicapps.squirreljme.java.symbols.ClassNameSymbol;
import net.multiphasicapps.squirreljme.jit.base.JITCPUEndian;
import net.multiphasicapps.squirreljme.jit.base.JITException;
import net.multiphasicapps.squirreljme.jit.JITCacheCreator;
import net.multiphasicapps.squirreljme.jit.JITClassWriter;
import net.multiphasicapps.squirreljme.jit.JITNamespaceWriter;
import net.multiphasicapps.squirreljme.jit.JITOutputConfig;
import net.multiphasicapps.squirreljme.jit.JITResourceWriter;
import net.multiphasicapps.squirreljme.os.generic.BlobContentType;
import net.multiphasicapps.squirreljme.os.generic.GenericBlob;
import net.multiphasicapps.squirreljme.os.generic.GenericBlobConstants;
import net.multiphasicapps.squirreljme.os.generic.GenericStringType;
import net.multiphasicapps.util.datadeque.ByteDeque;

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
	
	/** The namespace output. */
	protected final ExtendedDataOutputStream output;
	
	/** Intended output file. */
	protected final OutputStream intendedoutput;
	
	/** The output data endianess. */
	protected final DataEndianess endianess;
	
	/** The string table. */
	final __StringTable__ _strings =
		new __StringTable__();
	
	/** The import table. */
	final __Imports__ _imports =
		new __Imports__();
	
	/** Classes in the namespace. */
	final __Classes__ _classes =
		new __Classes__();
	
	/** Resources in the namespace. */
	final __Resources__ _resources =
		new __Resources__();
	
	/** Visible lock. */
	final Object _lock =
		this.lock;
	
	/** Has this been closed? */
	private volatile boolean _closed;
	
	/** The current writer being written. */
	private volatile __BaseWriter__ _current;
	
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
			// Setup output writer
			ExtendedDataOutputStream output = new ExtendedDataOutputStream(
				cc.createCache(__ns));
			this.output = output;
			
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
			
			// Set code/data endianess
			output.setEndianess(end);
			
			// Store endianess for later writing
			this.endianess = end;
			
			// Write magic number
			output.writeLong(GenericBlob.MAGIC_NUMBER);
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
			
			// {@squirreljme.error BA07 Cannot start writing a new class
			// because another class or resource is being written.}
			if (this._current != null)
				throw new JITException("BA07");
			
			// Create
			GenericClassWriter rv = new GenericClassWriter(this,
				this._classes.__newClass(__cn));
			this._current = rv;
			return rv;
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
			
			// {@squirreljme.error BA08 Cannot start writing a new resource
			// because another class or resource is being written.}
			if (this._current != null)
				throw new JITException("BA08");
			
			// Create
			GenericResourceWriter rv = new GenericResourceWriter(this,
				this._resources.__newResource(__name));
			this._current = rv;
			return rv;
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
			// {@squirreljme.error BA09 Cannot close the namespace writer
			// because a class or resource is currently being written.}
			if (this._current != null)
				throw new JITException("BA09");
			
			// Need to generate the actual binary?
			if (!this._closed)
			{
				// Mark closed
				this._closed = true;
				
				// Could fail
				try
				{
					__writeCache();
				}
				
				// {@squirreljme.error BA06 Failed to write the namespace to
				// the cache output.}
				catch (IOException e)
				{
					throw new JITException("BA06", e);
				}
			}
		}
	}
	
	/**
	 * Closes the given writer.
	 *
	 * @param __bw The writer to close.
	 * @throws JITException If it could not be closed.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/28
	 */
	final void __close(__BaseWriter__ __bw)
		throws JITException, NullPointerException
	{
		// Check
		if (__bw == null)
			throw new NullPointerException("NARG");
		
		// Lock
		synchronized (this.lock)
		{
			// {@squirreljme.error BA0h The entry to be closed is not the
			// current entry.}
			if (this._current != __bw)
				throw new JITException("BA0h");
			
			// Clear, there is no need to add the closed content to a table of
			// contents because that is managed when the content is created.
			this._current = null;
			
			// Write any strings between entries so they are output as soon
			// as possible.
			this._strings.__defer(this.output);
		}
	}
	
	/**
	 * Returns the endianess of the output data.
	 *
	 * @return The output endianess.
	 * @since 2016/07/27
	 */
	final DataEndianess __endianess()
	{
		return this.endianess;
	}
	
	/**
	 * Returns the stream output.
	 *
	 * @return The stream output.
	 * @since 2016/0812
	 */
	final ExtendedDataOutputStream __output()
	{
		return this.output;
	}
	
	/**
	 * Writes the final binary to the output cache.
	 *
	 * @throws IOException On write errors.
	 * @since 2016/08/11
	 */
	private final void __writeCache()
		throws IOException
	{
		// Write to the output
		try (ExtendedDataOutputStream dos = new ExtendedDataOutputStream(
			this.intendedoutput))
		{
			// Make sure the endianess matches
			dos.setEndianess(this.endianess);
			
			// Write magic number
			dos.writeLong(GenericBlob.MAGIC_NUMBER);
			
			// Write data to the output
			ByteDeque bddata = this.bddata;
			long datastart = dos.size();
			__writeDeque(bddata, dos);
			long dataend = dos.size();
			
			// Align
			while ((dos.size() & 7) != 0)
				dos.writeByte(0);
			
			// Write code to the output
			ByteDeque bdcode = this.bdcode;
			long codestart = dos.size();
			__writeDeque(bdcode, dos);
			long codeend = dos.size();
			
			throw new Error("TODO");
		}
	}
	
	/**
	 * Writes the given deque to the output stream.
	 *
	 * @param __d The deque to write.
	 * @param __os The stream to write to.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @sinced 2016/08/11
	 */
	private final void __writeDeque(ByteDeque __d, OutputStream __os)
		throws IOException, NullPointerException
	{
		// Check
		if (__d == null || __os == null)
			throw new NullPointerException("NARG");
		
		// Write all of the data
		int n = __d.available(), left = n;
		byte[] buf = new byte[4096];
		int bn = buf.length, at = 0;
		while (left > 0)
		{
			// Read what is needed
			int rc = Math.min(bn, left);
			__d.get(at, buf, 0, rc);
			
			// Write
			__os.write(buf, 0, rc);
			
			// Reduce
			left -= rc;
			at += rc;
		}
	}
}

