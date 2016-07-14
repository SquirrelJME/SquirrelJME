// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.java.cp.jar;

import java.io.InputStream;
import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.nio.channels.SeekableByteChannel;
import java.util.HashMap;
import java.util.Map;
import net.multiphasicapps.squirreljme.java.symbols.ClassNameSymbol;
import net.multiphasicapps.squirreljme.java.ci.CIClass;
import net.multiphasicapps.squirreljme.java.ci.CIException;
import net.multiphasicapps.squirreljme.java.ci.std.CISClass;
import net.multiphasicapps.squirreljme.java.cp.ClassUnit;
import net.multiphasicapps.zip.blockreader.ZipEntry;
import net.multiphasicapps.zip.blockreader.ZipFile;

/**
 * This provides access to classes from a JAR file.
 *
 * @since 2016/05/25
 */
public abstract class JarClassUnit
	extends ClassUnit
{
	/** The key this is associated with. */
	protected final String key;
	
	/** The lock on the close count. */
	private final Object _lock =
		new Object();
	
	/** The loaded cache of classes. */
	private final Map<ClassNameSymbol, Reference<CIClass>> _cache =
		new HashMap<>();
	
	/** The currently opened ZIP file. */
	private volatile ZipFile _zip;
	
	/** The current count. */
	private volatile int _count;
	
	/**
	 * This represents a single class unit which is provided within a JAR.
	 *
	 * @param __k The key this maps to.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/25
	 */
	public JarClassUnit(String __k)
		throws NullPointerException
	{
		// Check
		if (__k == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.key = __k;
	}
	
	/**
	 * Obtains the channel which is used to access the ZIP file data.
	 *
	 * @return The channel for the current class unit.
	 * @throws IOException If it could not be contained due to a read error.
	 * @since 2016/05/28
	 */
	protected abstract SeekableByteChannel obtainChannel()
		throws IOException;
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/27
	 */
	@Override
	public final CIClass locateClass(ClassNameSymbol __cns)
		throws CIException, NullPointerException
	{
		// Check
		if (__cns == null)
			throw new NullPointerException("NARG");
		
		// Lock on the cache of classes
		Map<ClassNameSymbol, Reference<CIClass>> cache = this._cache;
		synchronized (cache)
		{
			// Get
			Reference<CIClass> ref = cache.get(__cns);
			CIClass rv;
			
			// Load the class?
			if (ref == null || null == (rv = ref.get()))
				try (InputStream is = locateResource(
					__cns.toString() + ".class"))
				{
					// Not found?
					if (is == null)
						return null;
					
					// Load in class data and cache it
					cache.put(__cns, new WeakReference<>(
						(rv = new CISClass(is))));
				}
				
				// {@squirreljme.error BD03 Could not load the specified class
				// because there was an error during read. (The JAR key; The
				// class being read)}
				catch (IOException e)
				{
					throw new CIException(String.format("BO03 %s %s", this.key,
						__cns));
				}
			
			// Return it
			return rv;
		}
	}
	
	/**
	 * Locates a resource using the given absolute name.
	 *
	 * @param __res The absolute name of the resource to find, this must not
	 * start with a forward slash.
	 * @return The input stream which is associated with the given resource or
	 * {@code null} if it was not found.
	 * @since 2016/05/28
	 */
	@Override
	public final InputStream locateResource(String __res)
		throws NullPointerException
	{
		// Check
		if (__res == null)
			throw new NullPointerException("NARG");
		
		// Count
		try (__Counter__ counter = __count())
		{
			// Load the ZIP
			ZipFile zip = this._zip;
			
			// See if the zip contains the given entry
			ZipEntry ent = zip.get(__res);
			
			// Does not exist?
			if (ent == null)
				return null;
			
			InputStream rv = null;
			try
			{
				// Return a wrapped input stream which uses a counter
				return new __CountedInputStream__(ent.open());
			}
			
			// Open failed
			catch (IOException|Error|RuntimeException e)
			{
				// Make sure it is closed
				try
				{
					rv.close();
				}
				
				// Suppress it
				catch (Throwable t)
				{
					e.addSuppressed(t);
				}
				
				// Rethrow
				throw e;
			}
		}
		
		// Ignore
		catch (IOException e)
		{
			return null;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/26
	 */
	@Override
	public final String toString()
	{
		return this.key;
	}
	
	/**
	 * Creates a closeable reference counter for the ZIP file access.
	 *
	 * @return The counter for the ZIP.
	 * @since 2016/05/28
	 */
	private final __Counter__ __count()
	{
		// Lock
		synchronized (this._lock)
		{
			// Already loaded? Make a counter
			if (this._zip != null)
				return new __Counter__();
			
			// Otherwise obtain the channel
			SeekableByteChannel chan;
			try
			{
				chan = obtainChannel();
			}
			
			// {@squirreljme.error BO01 Could not obtain the seekable byte
			// channel for the given JAR unit. (The JAR key)}
			catch (IOException e)
			{
				throw new CIException(String.format("BO01 %s", this.key), e);
			}
			
			// Setup ZIP
			try
			{
				// Open ZIP
				ZipFile zip = ZipFile.open(chan);
				
				// Associate it
				this._zip = zip;
				
				// Setup counter
				return new __Counter__();
			}
			
			// Failed to handle the ZIP
			catch (IOException|RuntimeException|Error e)
			{
				// Make sure the channel is closed
				try
				{
					chan.close();
				}
				
				// Add more exception if they occur
				catch (Throwable t)
				{
					e.addSuppressed(t);
				}
				
				// Clear reference always
				this._zip = null;
				
				// {@squirreljme.error BO02 Failed to handle the given file
				// as a ZIP file (it likely is not one). (The JAR key)}
				throw new CIException(String.format("BO02 %s", this.key), e);
			}
		}
	}
	
	/**
	 * This wraps an input stream and provides counting for it.
	 *
	 * @since 2016/05/28
	 */
	private final class __CountedInputStream__
		extends InputStream
	{
		/** The wrapped stream. */
		private final InputStream _base;
		
		/** The counter. */
		private final __Counter__ _count;
		
		/**
		 * Initializes the counted input stream.
		 *
		 * @param __base The stream to wrap.
		 * @throws NullPointerException On null arguments.
		 * @since 2016/05/28
		 */
		private __CountedInputStream__(InputStream __base)
			throws NullPointerException
		{
			// Check
			if (__base == null)
				throw new NullPointerException("NARG");
			
			// Set
			this._base = __base;
			this._count = new __Counter__();
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/05/28
		 */
		@Override
		public int available()
			throws IOException
		{
			return this._base.available();
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/05/28
		 */
		@Override
		public void close()
			throws IOException
		{
			// Close the counter
			this._count.close();
			
			// And the wrapped stream
			this._base.close();
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/05/28
		 */
		@Override
		public int read()
			throws IOException
		{
			return this._base.read();
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/05/28
		 */
		@Override
		public int read(byte[] __b)
			throws IOException
		{
			return this._base.read(__b);
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/05/28
		 */
		@Override
		public int read(byte[] __b, int __o, int __l)
			throws IOException
		{
			return this._base.read(__b, __o, __l);
		}
	}
	
	/**
	 * This is used for counting how many resources are currently open within
	 * the JAR.
	 *
	 * @since 2016/05/28
	 */
	private final class __Counter__
		implements AutoCloseable
	{
		/** Already closed? */
		private volatile boolean _didclose;
		
		/**
		 * This initializes the counter.
		 *
		 * @since 2016/05/28
		 */
		private __Counter__()
		{
			// Lock
			synchronized (JarClassUnit.this._lock)
			{
				// Increase count
				JarClassUnit.this._count++;
			}
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/05/28
		 */
		@Override
		public void close()
		{
			// Lock
			synchronized (JarClassUnit.this._lock)
			{
				// If already closed, do nothing
				if (_didclose)
					return;
				
				// Reduce count
				int newcount = --JarClassUnit.this._count;
				
				// Mark as closed
				_didclose = true;
			}
		}
	}
}

