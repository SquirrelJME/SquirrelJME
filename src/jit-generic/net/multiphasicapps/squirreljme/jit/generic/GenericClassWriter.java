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
	
	/** Where class data is written to. */
	protected final ExtendedDataOutputStream output;
	
	/** The current order. */
	private volatile JITCompilerOrder _order =
		JITCompilerOrder.FIRST;
	
	/** Has this been closed? */
	private volatile boolean _closed;
	
	/** The field count. */
	private volatile int _fieldcount =
		-1;
	
	/** The method count. */
	private volatile int _methodcount =
		-1;
	
	/** Offsets to fields. */
	private volatile int[] _fieldoffsets;
	
	/** Offsets to methods. */
	private volatile int[] _methodoffsets;
	
	/**
	 * Initializes the generic class writer.
	 *
	 * @param __nsw The owning namespace writer.
	 * @param __cn The class name.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/27
	 */
	GenericClassWriter(GenericNamespaceWriter __nsw, ClassNameSymbol __cn)
		throws NullPointerException
	{
		super(__nsw, Objects.toString(__cn), BlobContentType.CLASS);
		
		// Check
		if (__cn == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.classname = __cn;
		
		// Setup output
		ExtendedDataOutputStream output = new ExtendedDataOutputStream(
			this.rawoutput);
		output.setEndianess(__nsw.__endianess());
		this.output = output;
		
		// Write the class header
		try
		{
			// Class magic number
			output.writeInt(GenericBlobConstants.CLASS_MAGIC);
			
			// Output class name
			output.writeInt(__nsw.__addString(__cn.toString()));
		}
		
		// {@squirreljme.error BA0a Failed to write the initial header.}
		catch (IOException e)
		{
			throw new JITException("BA0a");
		}
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
				this.output.writeInt(v);
			}
			
			// {@squirreljme.error BA0k Failed to write the class flags.}
			catch (IOException e)
			{
				throw new JITException("BA0k", e);
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
			
			// Need to add many strings
			GenericNamespaceWriter nsw = this.owner;
			int n = __ins.length;
			
			// Could fail
			ExtendedDataOutputStream output = this.output;
			try
			{
				// Write count
				output.writeInt(n);
			
				// The interface string IDs
				for (int i = 0; i < n; i++)
					output.writeInt(nsw.__addString(
						__ins[i].toString()));
			}
			
			// {@squirreljme.error BA0m Failed to write the interfaces.}
			catch (IOException e)
			{
				throw new JITException("BA0m", e);
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
			
			try
			{
				// Write -1 if there is none, since zero could be a valid
				// string index
				ExtendedDataOutputStream output = this.output;
				if (__cn == null)
					output.writeInt(-1);
			
				// Otherwise the string index
				else
					output.writeInt(this.owner.__addString(
						__cn.toString()));
			}
			
			// {@squirreljme.error BA0l Failed to write the super class.}
			catch (IOException e)
			{
				throw new JITException("BA0l", e);
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

