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
import net.multiphasicapps.squirreljme.jit.JITConstantPool;
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
	
	/** The current order. */
	private volatile JITCompilerOrder _order =
		JITCompilerOrder.FIRST;
	
	/** Has this been closed? */
	private volatile boolean _closed;
	
	/** The constant pool to use. */
	private volatile JITConstantPool _pool;
	
	/** The string table position. */
	private volatile int _stringpos;
	
	/** The number of strings in the table. */
	private volatile int _stringcount;
	
	/** The constant pool table position. */
	private volatile int _poolpos;
	
	/** The number of entries in the constant pool. */
	private volatile int _poolcount;
	
	/**
	 * Initializes the generic class writer.
	 *
	 * @param __nsw The owning namespace writer.
	 * @param __dos The stream to write to.
	 * @param __cn The class name.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/27
	 */
	GenericClassWriter(GenericNamespaceWriter __nsw,
		ExtendedDataOutputStream __dos, ClassNameSymbol __cn)
		throws NullPointerException
	{
		super(__nsw, __dos, BlobContentType.CLASS, __cn.toString());
		
		// Check
		if (__cn == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.classname = __cn;
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
	 * @since 2016/08/12
	 */
	@Override
	public void constantPool(JITConstantPool __pool)
	{
		// Check
		if (__pool == null)
			throw new NullPointerException("NARG");
		
		// Lock
		synchronized (this.lock)
		{
			// Check order
			__order(JITCompilerOrder.SET_CONSTANT_POOL);
			
			// Just set the pool
			this._pool = __pool;
			
			// Setup pool writer then write it
			try
			{
				// Write the pool
				__PoolWriter__ pw = new __PoolWriter__(__pool);
				pw.__write(this.output);
				
				// Store the string table position and the constant pool
				// position
				this._stringpos = pw._stringpos;
				this._stringcount = pw._stringcount;
				this._poolpos = pw._poolpos;
				this._poolcount = pw._poolcount;
			}
			
			// {@squirreljme.error BA0u Failed to write the constant pool.}
			catch (IOException e)
			{
				throw new JITException("BA0u", e);
			}
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
			
			throw new Error("TODO");
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
			
			throw new Error("TODO");
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

