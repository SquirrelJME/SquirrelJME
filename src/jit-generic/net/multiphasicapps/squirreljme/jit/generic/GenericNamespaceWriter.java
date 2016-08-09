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
import net.multiphasicapps.io.datadequestream.ByteDequeOutputStream;
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
	
	/** Intended output file. */
	protected final OutputStream intendedoutput;
	
	/** Code output (backing queue). */
	protected final ByteDeque bdcode =
		new ByteDeque();
	
	/** Code output. */
	protected final ExtendedDataOutputStream outcode =
		new ExtendedDataOutputStream(new ByteDequeOutputStream(this.bdcode));
	
	/** Data output (backing queue). */
	protected final ByteDeque bddata =
		new ByteDeque();
	
	/** Data output. */
	protected final ExtendedDataOutputStream outdata =
		new ExtendedDataOutputStream(new ByteDequeOutputStream(this.bddata));
	
	/** The output data endianess. */
	protected final DataEndianess endianess;
	
	/** The string table. */
	final __StringTable__ _strings =
		new __StringTable__();
	
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
		
		// The blank string is always first
		strings.put("", 0);
		
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
			// Create intended output for writing on close
			this.intendedoutput = cc.createCache(__ns);
			
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
			this.outcode.setEndianess(end);
			this.endianess = end;
			
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
			
			// {@squirreljme.error BA07 Cannot start writing a new class
			// because another class or resource is being written.}
			if (this._current != null)
				throw new JITException("BA07");
			
			// Create
			GenericClassWriter rv = new GenericClassWriter(this, __cn);
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
			GenericResourceWriter rv = new GenericResourceWriter(this, __name);
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
			
			// Could fail
			try
			{
				if (true)
					throw new Error("TODO");
				
				/*
				// Write final pieces before closing
				ExtendedDataOutputStream output = this.output;
				if (!this._closed)
				{
					// Mark closed
					this._closed = true;
				
					// The string table and the contents
					Map<String, Integer> strings = this.strings;
					__Contents__ contents = this._contents;
					int numcontents = contents.size();
					
					// Sort entries before being used (faster to do it now)
					contents.sortEntries();
					
					// Add strings for all entry names
					int[] contstrid = new int[numcontents];
					for (int i = 0; i < numcontents; i++)
						contstrid[i] = __addString(contents.get(i)._name);
					
					// Write the string table
					int numstrings = strings.size();
					int stpos = __writeStringTable(output, strings);
					
					// Align to 4
					long contentp = 0;
					while (((contentp = output.size()) & 3) != 0)
						output.writeByte(0xFD);
					
					// {@squirreljme.error BA0r Pointer to table of contents
					// is not within the range of 2GiB.}
					if (contentp < 0 || contentp > Integer.MAX_VALUE)
						throw new JITException("BA0r");
					
					// Write the contents table
					for (int i = 0; i < numcontents; i++)
					{
						__Contents__.__Entry__ e = contents.get(i);
						
						// Write
						output.writeShort(e._type.ordinal());
						output.writeShort(contstrid[i]);
						output.writeInt(e._startpos);
						output.writeInt(e._size);
					}
					
					// Align to 4
					while ((output.size() & 3) != 0)
						output.writeByte(0xFD);
					
					// Write the string table and content pointer info
					output.writeInt(stpos);
					output.writeInt(numstrings);
					output.writeInt((int)contentp);
					output.writeInt(numcontents);
				}
				
				// Close output
				output.close();
				*/
			}
			
			// {@squirreljme.error BA06 Failed to close the generic namespace
			// writer.}
			catch (IOException e)
			{
				throw new JITException("BA06", e);
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
			
			// Add to contents directory
			if (true)
				throw new Error("TODO");
			/*
			this._contents.__add(__bw._startpos, this.output.size(),
				__bw._contenttype, __bw._contentname);*/
			
			// Clear
			this._current = null;
		}
	}
	
	/**
	 * Returns the code output.
	 *
	 * @return The code output.
	 * @since 2016/08/09
	 */
	final ExtendedDataOutputStream __code()
	{
		return this.outcode;
	}
	
	/**
	 * Returns the data output.
	 *
	 * @return The data output.
	 * @since 2016/08/09
	 */
	final ExtendedDataOutputStream __data()
	{
		return this.outdata;
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
	 * Writes the string table to the output namespace binary.
	 *
	 * @param __edos The output stream.
	 * @param __strs The strings to write.
	 * @return The string table position.
	 * @throws IOException On null arguments.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/30
	 */
	private int __writeStringTable(ExtendedDataOutputStream __edos,
		Map<String, Integer> __strs)
		throws IOException, NullPointerException
	{
		// Check
		if (__edos == null || __strs == null)
			throw new NullPointerException("NARG");
		
		// Align to 4
		while ((__edos.size() & 3) != 0)
			__edos.writeByte(0xFD);
		
		// Write all strings
		int numstrings = __strs.size(), q = 0;
		int[] stp = new int[numstrings];
		for (String s : __strs.keySet())
		{
			// {@squirreljme.error BA0n String exceeds 65KiB. (The string
			// length)}
			int n = s.length();
			if (n > 65535)
				throw new JITException(String.format("BA0n %d", n));
			
			// Determine if the write is narrow or wide
			boolean wide = false;
			for (int i = 0; i < n; i++)
				if (s.charAt(i) >= 0x100)
				{
					wide = true;
					break;
				}
			
			// Align to 4
			long cp = 0;
			while (((cp = __edos.size()) & 3) != 0)
				__edos.writeByte(0xFD);
			
			// {@squirreljme.error BA0o String starts at address which is
			// outside the range of 2GiB.}
			if (cp > Integer.MAX_VALUE || cp < 0)
				throw new JITException("BA0o");
			stp[q++] = (int)cp;
			
			// Write length
			__edos.writeShort(n);
			
			// Narrow write?
			if (!wide)
			{
				__edos.writeByte(GenericStringType.BYTE.ordinal());
				for (int i = 0; i < n; i++)
					__edos.writeByte(s.charAt(i));
			}
			
			// Wide write
			else
			{
				__edos.writeByte(GenericStringType.CHAR.ordinal());
				for (int i = 0; i < n; i++)
					__edos.writeChar(s.charAt(i));
			}
		}
		
		// Align to 4
		long tpos = 0;
		while (((tpos = __edos.size()) & 3) != 0)
			__edos.writeByte(0xFD);
		
		// {@squirreljme.error BA0p String pointer table at address which is
		// outside the range of 2GiB.}
		if (tpos > Integer.MAX_VALUE || tpos < 0)
			throw new JITException("BA0p");
		
		// Write the string pointer data
		for (int i = 0; i < numstrings; i++)
			__edos.writeInt(stp[i]);
		
		// Return the table position
		return (int)tpos;
	}
}

