// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.interpreter;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import net.multiphasicapps.squirreljme.java.symbols.ClassNameSymbol;
import net.multiphasicapps.squirreljme.jit.JITCacheCreator;
import net.multiphasicapps.squirreljme.jit.JITClassWriter;
import net.multiphasicapps.squirreljme.jit.base.JITException;
import net.multiphasicapps.squirreljme.jit.JITNamespaceWriter;
import net.multiphasicapps.squirreljme.jit.JITOutputConfig;
import net.multiphasicapps.squirreljme.jit.JITResourceWriter;

/**
 * This writes the namespace which is later interpreted.
 *
 * @since 2016/07/22
 */
public class InterpreterNamespaceWriter
	implements JITNamespaceWriter
{
	/** Lock. */
	protected final Object lock =
		new Object();
	
	/** The output configuration. */
	protected final JITOutputConfig.Immutable config;
	
	/** The name of the namespace. */
	protected final String namespace;
	
	/** The output data. */
	protected final DataOutputStream output;
	
	/** The output string table. */
	private final Map<String, Integer> _strings =
		new LinkedHashMap<>();
	
	/** Has this been closed? */
	private volatile boolean _closed;
	
	/** The current thing being written (one at a time). */
	private volatile InterpreterBaseOutput _now;
	
	/**
	 * Initializes the interpreter output.
	 *
	 * @param __conf The configuration used.
	 * @param __ns The namespace being used.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/22
	 */
	public InterpreterNamespaceWriter(JITOutputConfig.Immutable __conf,
		String __ns)
		throws NullPointerException
	{
		// Check
		if (__conf == null || __ns == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.config = __conf;
		this.namespace = __ns;
		
		// {@squirreljme.error BV01 No cache creator was associated with
		// the given configuration.}
		JITCacheCreator cc = __conf.cacheCreator();
		if (cc == null)
			throw new JITException("BV01");
		
		// Setup output
		try
		{
			this.output = new DataOutputStream(cc.createCache(__ns));
		}
		
		// {@squirreljme.error BV02 Could not create the cached output.}
		catch (IOException e)
		{
			throw new JITException("BV02", e);
		}
		
		// There is always a blank string in the table
		interpreterAddString("");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/22
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
			// {@squirreljme.error BV04 Cannot begin a new class because an
			// existing namespace content is being written.}
			if (this._now != null)
				throw new JITException("BV04");
			
			// {@squirreljme.error BV0g Cannot begin a new class because the
			// namespace writer is closed.}
			if (this._closed)
				throw new JITException("BV0g");
			
			// Setup writer
			InterpreterClassWriter rv = new InterpreterClassWriter(this, __cn);
			this._now = rv;
			return rv;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/22
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
			// {@squirreljme.error BV05 Cannot begin a new resource because an
			// existing namespace content is being written.}
			if (this._now != null)
				throw new JITException("BV05");
			
			// {@squirreljme.error BV0h Cannot begin a new resource because the
			// namespace writer is closed.}
			if (this._closed)
				throw new JITException("BV0h");
			
			// Setup writer
			InterpreterResourceWriter rv = new InterpreterResourceWriter(this,
				__name);
			this._now = rv;
			return rv;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/22
	 */
	@Override
	public void close()
		throws JITException
	{
		// Lock
		synchronized (this.lock)
		{
			// {@squirreljme.error BV0i Cannot close the namespace writer
			// because a content is being written.}
			if (this._now != null)
				throw new JITException("BV0i");
			
			// Finish writing if not closed
			DataOutputStream output = this.output;
			try
			{
				// Write the string table
				if (!this._closed)
				{
					// Close
					this._closed = true;
				
					// Start of the string table
					try (StringTableWriter stw = new StringTableWriter(this,
						this._strings))
					{
						// Set current
						this._now = stw;
						
						// Write this
						stw.write();
					}
					
					// Clear the now always
					finally
					{
						this._now = null;
					}
				}
				
				// Close output
				this.output.close();
			}
			
			// {@squirreljme.error BV03 Could not close the output.}
			catch (IOException e)
			{
				throw new JITException("BV03", e);
			}
		}
	}
	
	/**
	 * Adds a string to the interpreter blob and returns the index where the
	 * string is located.
	 *
	 * @param __s The string to add.
	 * @return The index of the string.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/22
	 */
	public final int interpreterAddString(String __s)
		throws NullPointerException
	{
		// Check
		if (__s == null)
			throw new NullPointerException("NARG");
		
		// Lock
		synchronized (this.lock)
		{
			// Get string table and its size
			Map<String, Integer> strings = this._strings;
			int size = strings.size();
			
			// Is this string in the table?
			Integer rv = strings.get(__s);
			if (rv != null)
				return rv;
			
			// Add it otherwise
			rv = Integer.valueOf(size);
			strings.put(__s, rv);
			
			// Return the old size
			return size;
		}
	}
	
	/**
	 * Returns the interpreter's lock.
	 *
	 * @return The locking object.
	 * @since 2016/07/22
	 */
	public final Object interpreterLock()
	{
		return this.lock;
	}
	
	/**
	 * Returns the output from the interpreter.
	 *
	 * @return The interpreter output.
	 * @since 2016/07/22
	 */
	public final DataOutputStream interpreterOutput()
	{
		return this.output;
	}
	
	/**
	 * Closes the currently being written output.
	 *
	 * @param __ibo The output to close.
	 * @throws JITException If the close could not be indicated.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/23
	 */
	final void __close(InterpreterBaseOutput __ibo)
		throws JITException, NullPointerException
	{
		// Check
		if (__ibo == null)
			throw new NullPointerException("NARG");
		
		// Lock
		synchronized (this.lock)
		{
			// {@squirreljme.error BV0a Could not close the current output.}
			InterpreterBaseOutput now = this._now;
			if (now != __ibo)
				throw new JITException("BV0a");
			
			// Clear
			this._now = null;
		}
	}
}

