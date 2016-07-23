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
import net.multiphasicapps.squirreljme.java.symbols.ClassNameSymbol;
import net.multiphasicapps.squirreljme.jit.base.JITClassFlag;
import net.multiphasicapps.squirreljme.jit.base.JITClassFlags;
import net.multiphasicapps.squirreljme.jit.base.JITException;
import net.multiphasicapps.squirreljme.jit.JITClassWriter;
import net.multiphasicapps.squirreljme.jit.JITCompilerOrder;
import net.multiphasicapps.squirreljme.os.interpreter.ContentType;

/**
 * This writes classes to the output.
 *
 * @since 2016/07/22
 */
public class InterpreterClassWriter
	extends InterpreterBaseOutput
	implements JITClassWriter
{
	/** The class being written. */
	protected final ClassNameSymbol classname;
	
	/** Has this been closed? */
	private volatile boolean _closed;
	
	/** The current order. */
	private volatile JITCompilerOrder _order =
		JITCompilerOrder.FIRST;
	
	/**
	 * Initializes the class writer.
	 *
	 * @param __nsw The owning namespace writer.
	 * @param __cn The class name.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/22
	 */
	public InterpreterClassWriter(InterpreterNamespaceWriter __nsw,
		ClassNameSymbol __cn)
		throws NullPointerException
	{
		super(__nsw, String.valueOf(__cn), ContentType.CLASS);
		
		// Check
		if (__cn == null)
			throw new NullPointerException("NARG");
		
		// The class name
		this.classname = __cn;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/22
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
			interpreterOrder(JITCompilerOrder.CLASS_FLAGS);
			
			// Build value
			int v = 0;
			for (JITClassFlag f : __cf)
				v |= (1 << f.ordinal());
			
			// Write
			try
			{
				this.output.writeInt(v);
			}
			
			// {@squirreljme.error BV0c Failed to write the class flags.}
			catch (IOException e)
			{
				throw new JITException("BV0c", e);
			}
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
			// Handle closing
			if (!this._closed)
			{
				// Mark closed
				this._closed = true;
				
				// Write end of class
				try
				{
					this.output.writeInt(-1);
				}
				
				// {@squirreljme.error BV0d Failed to write the end of the
				// class.}
				catch (IOException e)
				{
					throw new JITException("BV0d", e);
				}
			}
			
			// Super handle
			super.close();
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/22
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
			interpreterOrder(JITCompilerOrder.INTERFACE_CLASS_NAMES);
			
			// Need to add many strings
			InterpreterNamespaceWriter nsw = this.writer;
			int n = __ins.length;
			
			// Number of interfaces
			DataOutputStream output = this.output;
			try
			{
				output.writeInt(n);
			
				// The interface string IDs
				for (int i = 0; i < n; i++)
					output.writeInt(nsw.interpreterAddString(
						__ins[i].toString()));
			}
			
			// {@squirreljme.error BV0e Failed to write the interfaces.}
			catch (IOException e)
			{
				throw new JITException("BV0e", e);
			}
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/22
	 */
	@Override
	public void superClass(ClassNameSymbol __cn)
		throws JITException
	{
		// Lock
		synchronized (this.lock)
		{
			// Check order
			interpreterOrder(JITCompilerOrder.SUPER_CLASS_NAME);
			
			try
			{
				// Write -1 if there is none, since zero could be a valid
				// string index
				DataOutputStream output = this.output;
				if (__cn == null)
					output.writeInt(-1);
			
				// Otherwise the string index
				else
					output.writeInt(this.writer.interpreterAddString(
						__cn.toString()));
			}
			
			// {@squirreljme.error BV0f Failed to write the super class.}
			catch (IOException e)
			{
				throw new JITException("BV0f", e);
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
	public void interpreterOrder(JITCompilerOrder __exp)
		throws JITException, NullPointerException
	{
		// Check
		if (__exp == null)
			throw new NullPointerException("NARG");
		
		// Lock
		synchronized (this.lock)
		{
			// {@squirreljme.error BV0b JIT invocation is not in order.
			// (The order that was attempted to be used; The expected order)}
			JITCompilerOrder order = this._order;
			if (order != __exp)
				throw new JITException(String.format("BV0b %s %s", __exp,
					order));
			
			// Set next
			this._order = order.next();
		}
	}
}

