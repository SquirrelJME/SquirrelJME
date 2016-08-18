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
import java.util.LinkedList;
import java.util.List;
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
import net.multiphasicapps.squirreljme.os.generic.GenericStringType;

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
	
	/** The output data endianess. */
	protected final DataEndianess endianess;
	
	/** Visible lock. */
	final Object _lock =
		this.lock;
	
	/** The global constant pool, for condensing entries. */
	final __GlobalPool__ _gpool =
		new __GlobalPool__(this);
	
	/** The entry index. */
	private final List<__Index__> _index =
		new LinkedList<>();
	
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
				this.output, __cn);
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
				this.output, __name);
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
				
				try (ExtendedDataOutputStream dos = this.output)
				{
					// Align
					__align();
					
					// Write the global string table and constant pool
					if (true)
						throw new Error("TODO");
					
					// Align
					__align();
				
					// Write magic identifier
					dos.writeInt(GenericBlob.CENTRAL_DIRECTORY_MAGIC_NUMBER);
				
					// Record the index
					List<__Index__> index = this._index;
					int n = index.size();
					for (__Index__ i : index)
					{
						// Get upper and lower sizes
						int bss = i._size;
						int ups = bss >>> GenericBlob.NAMESPACE_SHIFT;
						int lws = bss & GenericBlob.ALIGN_MASK;
						
						// Write the blob type and the lower bits of the size
						dos.writeByte(i._type.ordinal());
						dos.writeByte(lws);
						
						// Write name index
						dos.writeShort(i._namedx._index);
						
						// The position and size
						int xpos;
						dos.writeShort((xpos = i._datapos) >>>
							GenericBlob.NAMESPACE_SHIFT);
						dos.writeShort(ups);
						
						// {@squirreljme.error BA15 The position is not aligned
						// to the required alignment.}
						if (0 != (xpos & GenericBlob.ALIGN_MASK))
							throw new JITException("BA15");
					}
				
					// Record the count and magic
					dos.writeInt(n);
					dos.writeInt(GenericBlob.
						END_CENTRAL_DIRECTORY_MAGIC_NUMBER);
				}
				
				// {@squirreljme.error BA0m Failed to write the end of the
				// namespace.}
				catch (IOException e)
				{
					throw new JITException("BA0m", e);
				}
			}
		}
	}
	
	/**
	 * Aligns the output stream.
	 *
	 * @throws IOException on write errors.
	 * @since 2016/08/12
	 */
	final void __align()
		throws IOException
	{
		ExtendedDataOutputStream dos = this.output;
		int m = GenericBlob.ALIGN_MASK;
		while ((dos.size() & m) != 0)
			dos.writeByte(0);
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
			
			// Add to the index
			this._index.add(new __Index__(__bw));
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
	 * Writes a string.
	 *
	 * @param __dos The stream to write to.
	 * @param __upper The upper byte which has any meaning.
	 * @param __s The string to print.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/08/13
	 */
	static void __writeString(ExtendedDataOutputStream __dos, int __upper,
		String __s)
		throws IOException, NullPointerException
	{
		// Check
		if (__dos == null || __s == null)
			throw new NullPointerException("NARG");
		
		// Determine if there are wide characters used
		boolean wide = false;
		int n = __s.length();
		for (int i = 0; i < n; i++)
			if (__s.charAt(i) > 255)
			{
				wide = true;
				break;
			}
		
		// Upper byte which has an undefined meaning
		__dos.writeByte(__upper);
		
		// Print bytes per char
		__dos.writeByte((wide ? 2 : 1));
		
		// Wide?
		if (wide)
			for (int i = 0; i < n; i++)
				__dos.writeShort((short)__s.charAt(i));
		
		// Narrow?
		else
			for (int i = 0; i < n; i++)
				__dos.writeByte((short)__s.charAt(i));
	}
}

