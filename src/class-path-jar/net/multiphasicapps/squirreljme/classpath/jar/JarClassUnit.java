// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.classpath.jar;

import java.io.IOException;
import java.nio.channels.SeekableByteChannel;
import net.multiphasicapps.descriptors.ClassNameSymbol;
import net.multiphasicapps.squirreljme.ci.CIClass;
import net.multiphasicapps.squirreljme.ci.CIException;
import net.multiphasicapps.squirreljme.classpath.ClassUnit;
import net.multiphasicapps.zips.StandardZIPFile;

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
	
	/** The currently opened ZIP file. */
	private volatile StandardZIPFile _zip;
	
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
		
		// Count
		try (__Counter__ counter = __count())
		{
			throw new Error("TODO");
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
				StandardZIPFile zip = StandardZIPFile.open(chan);
				
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
				
				// {@squirreljme.error BD02 Failed to handle the given file
				// as a ZIP file (it likely is not one). (The JAR key)}
				throw new CIException(String.format("BO02 %s", this.key), e);
			}
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
				JarClassUnit.this._count--;
				
				// Mark as closed
				_didclose = true;
			}
		}
	}
}

