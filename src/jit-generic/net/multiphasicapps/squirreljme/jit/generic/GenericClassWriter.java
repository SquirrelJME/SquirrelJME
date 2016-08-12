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
import java.util.Objects;
import net.multiphasicapps.squirreljme.java.symbols.ClassNameSymbol;
import net.multiphasicapps.squirreljme.jit.base.JITClassFlag;
import net.multiphasicapps.squirreljme.jit.base.JITClassFlags;
import net.multiphasicapps.squirreljme.jit.base.JITException;
import net.multiphasicapps.squirreljme.jit.JITClassWriter;
import net.multiphasicapps.squirreljme.jit.JITCompilerOrder;
import net.multiphasicapps.squirreljme.os.generic.BlobContentType;
import net.multiphasicapps.squirreljme.os.generic.GenericBlobConstants;
import net.multiphasicapps.io.data.ExtendedDataOutputStream;

/**
 * This writes classes to the output namespace.
 *
 * @since 2016/07/27
 */
public final class GenericClassWriter
	extends __BaseWriter__
	implements JITClassWriter
{
	/** The name of the class being written. */
	protected final ClassNameSymbol classname;
	
	/** The class module. */
	protected final __Class__ modclass;
	
	/** The current order. */
	private volatile JITCompilerOrder _order =
		JITCompilerOrder.FIRST;
	
	/** Has this been closed? */
	private volatile boolean _closed;
	
	/**
	 * Initializes the generic class writer.
	 *
	 * @param __nsw The owning namespace writer.
	 * @param __cn The class name.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/27
	 */
	GenericClassWriter(GenericNamespaceWriter __nsw, __Class__ __cl)
		throws NullPointerException
	{
		super(__nsw);
		
		// Check
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.modclass = __cl;
		this.classname = __cl.__className();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/27
	 */
	@Override
	public void classFlags(JITClassFlags __cf)
		throws JITException, NullPointerException
	{
		// Check
		if (__cf == null)
			throw new NullPointerException("NARG");
		
		// Lock
		synchronized (this.lock)
		{
			// Check order
			__order(JITCompilerOrder.CLASS_FLAGS);
			
			// Build value
			int v = 0;
			for (JITClassFlag f : __cf)
				v |= (1 << f.ordinal());
			
			// Write
			try
			{
				this.output.writeShort(v);
			}
			
			// {@squirreljme.error BA0l Failed to write the class flags.}
			catch (IOException e)
			{
				throw new JITException("BA0l", e);
			}
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
			// Close if not closed
			if (!this._closed)
			{
				// Mark closed
				this._closed = true;
			}
			
			// Super close
			super.close();
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/27
	 */
	@Override
	public void interfaceClasses(ClassNameSymbol... __ins)
		throws JITException, NullPointerException
	{
		// Check
		if (__ins == null)
			throw new NullPointerException("NARG");
		
		// Lock
		synchronized (this.lock)
		{
			// Check order
			__order(JITCompilerOrder.INTERFACE_CLASS_NAMES);
			
			// Store classes
			int n = __ins.length;
			__ImportClass__[] ics = new __ImportClass__[n];
			__Imports__ imps = this._imports;
			for (int i = 0; i < n; i++)
			{
				// Get class name
				ClassNameSymbol cn = __ins[i];
				if (cn == null)
					throw new NullPointerException("NARG");
				
				// Store
				__ImportClass__ icl;
				ics[i] = (icl = imps.__importClass(cn));
				
				// Mark as implementing something
				icl._implemented = true;
			}
			
			// Write all the indices
			try
			{
				ExtendedDataOutputStream dos = this.output;
				
				// Align
				while ((dos.size() & 3) != 0)
					dos.writeByte(0);
				
				// Set position here
				this.modclass._itablepos = (int)dos.size();
				this.modclass._itablesize = n;
				
				// Write the table
				for (int i = 0; i < n; i++)
					dos.writeShort(ics[i]._index);
			}
			
			// {@squirreljme.error BA0x Could not write the interface classes
			// import indices.}
			catch (IOException e)
			{
				throw new JITException("BA0x", e);
			}
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/27
	 */
	@Override
	public void superClass(ClassNameSymbol __cn)
		throws JITException
	{
		// Lock
		synchronized (this.lock)
		{
			// Check order
			__order(JITCompilerOrder.SUPER_CLASS_NAME);
			
			// Import the class
			__ImportClass__ ic = this._imports.__importClass(__cn);
			
			// Declare that it is extended
			ic._extended = true;
			
			// Write the import index
			try
			{
				ExtendedDataOutputStream dos = this.output;
				dos.writeShort(ic._index);
			}
			
			// {@squirreljme.error BA0w Could not write the super class
			// import index.}
			catch (IOException e)
			{
				throw new JITException("BA0w", e);
			}
		}
	}
	
	/**
	 * Checks that the current order is the given expected order and proceeds
	 * to the next order.
	 *
	 * @param __exp The current order that is expected.
	 * @throws JITException If the order is not correct.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/22
	 */
	private void __order(JITCompilerOrder __exp)
		throws JITException, NullPointerException
	{
		// Check
		if (__exp == null)
			throw new NullPointerException("NARG");
		
		// Lock
		synchronized (this.lock)
		{
			// {@squirreljme.error BA0j JIT invocation is not in order.
			// (The order that was attempted to be used; The expected order)}
			JITCompilerOrder order = this._order;
			if (order != __exp)
				throw new JITException(String.format("BA0j %s %s", __exp,
					order));
			
			// Set next
			this._order = order.next();
		}
	}
}

