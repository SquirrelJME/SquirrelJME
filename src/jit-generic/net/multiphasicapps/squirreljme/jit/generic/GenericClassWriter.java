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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.multiphasicapps.squirreljme.java.symbols.ClassNameSymbol;
import net.multiphasicapps.squirreljme.java.symbols.FieldSymbol;
import net.multiphasicapps.squirreljme.java.symbols.IdentifierSymbol;
import net.multiphasicapps.squirreljme.java.symbols.MethodSymbol;
import net.multiphasicapps.squirreljme.jit.base.JITClassFlag;
import net.multiphasicapps.squirreljme.jit.base.JITClassFlags;
import net.multiphasicapps.squirreljme.jit.base.JITException;
import net.multiphasicapps.squirreljme.jit.base.JITFieldFlags;
import net.multiphasicapps.squirreljme.jit.base.JITMethodFlags;
import net.multiphasicapps.squirreljme.jit.JITClassWriter;
import net.multiphasicapps.squirreljme.jit.JITCompilerOrder;
import net.multiphasicapps.squirreljme.jit.JITConstantPool;
import net.multiphasicapps.squirreljme.jit.JITMethodWriter;
import net.multiphasicapps.squirreljme.os.generic.BlobContentType;
import net.multiphasicapps.squirreljme.os.generic.GenericBlob;
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
	/** The maximum size a class may be. */
	static final int _SIZE_LIMIT =
		65535;
	
	/** The name of the class being written. */
	protected final ClassNameSymbol classname;
	
	/** The class name index. */
	protected final int classnamedx;
	
	/** Fields in this class. */
	private final List<__Field__> _fields =
		new ArrayList<>();
	
	/** Methods in this class. */
	private final List<__Method__> _methods =
		new ArrayList<>();
	
	/** The current order. */
	private volatile JITCompilerOrder _order =
		JITCompilerOrder.FIRST;
	
	/** Has this been closed? */
	private volatile boolean _closed;
	
	/** The constant pool to use. */
	private volatile JITConstantPool _xpool;
	
	/** Class flags, written later. */
	private volatile JITClassFlags _flags;
	
	/** The super class pool ID. */
	private volatile int _scpooldx;
	
	/** The interface count. */
	private volatile int _ifacecount;
	
	/** The pointer where interfaces are stored. */
	private volatile int _ifacepos;
	
	/** The number of fields available. */
	private volatile int _fieldcount =
		-1;
	
	/** The number of fields currently written. */
	private volatile int _writtenfields;
	
	/** The number of methods available. */
	private volatile int _methodcount =
		-1;
	
	/** The number of methods currently written. */
	private volatile int _writtenmethods;
	
	/** The field table position. */
	private volatile int _fieldtable;
	
	/** The method table position. */
	private volatile int _methodtable;
	
	/** The code stream being written to. */
	private volatile __CodeStream__ _codes;
	
	/** The current method being written. */
	private volatile __Method__ _method;
	
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
		
		// Get the index for the class name also
		this.classnamedx = this._gpool.__loadClass(__cn)._index;
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
			
			// Write later
			this._flags = __cf;
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
				
				// Could fail
				try
				{
					// Get output
					ExtendedDataOutputStream dos = this.output;
					
					// Align
					while ((dos.size() & 3) != 0)
						dos.writeByte(0);
					
					// Current class name
					dos.writeShort(this.classnamedx);
					
					// The super class name
					dos.writeShort(this._scpooldx);
					
					// The interfaces implemented
					dos.writeShort(this._ifacepos);
					dos.writeShort(this._ifacecount);
					
					// The class flags
					int flags = 0;
					for (JITClassFlag f : this._flags)
						flags |= (1 << f.ordinal());
					dos.writeShort(flags);
					
					// Field table
					dos.writeShort(this._fieldtable);	// offset
					dos.writeShort(this._fieldcount);	// size
					
					// Method table
					dos.writeShort(this._methodtable);	// offset
					dos.writeShort(this._methodcount);	// size
					
					// {@squirreljme.error BA13 The final class size exceeds
					// the class size limitation. (The class size)}
					long esz;
					if ((esz = dos.size()) > _SIZE_LIMIT)
						throw new JITException(String.format("BA13 %d", esz));
				}
			
				// {@squirreljme.error BA11 Failed to write the end of the
				// class.}
				catch (IOException e)
				{
					throw new JITException("BA11", e);
				}
				
				// Clear the current pool
				this._gpool.__setCurrent(null);
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
	public void constantPool(JITConstantPool __pool, int __cndx)
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
			this._xpool = __pool;
			this._gpool.__setCurrent(__pool);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/08/19
	 */
	@Override
	public JITMethodWriter code()
		throws JITException
	{
		try
		{
			// Lock
			synchronized (this.lock)
			{
				// Check order
				__order(JITCompilerOrder.METHOD_CODE);
			
				// Align
				GenericNamespaceWriter owner = this.owner;
				owner.__align();
			
				// Set current code position
				ExtendedDataOutputStream output = this.output;
				__Method__ m = this._method;
				m._codestart = (int)output.size();
			
				// Create code stream
				__CodeStream__ cs = new __CodeStream__(this, output);
				this._codes = cs;
			
				// Need 
				GenericOutput goutput = owner._output;
			
				// Method logic is architecture dependent and as such this is
				// delegated to other classes created by the handler for the
				// given architecture
				return goutput.__methodWriter(cs);
			}
		}
		
		// {@squirreljme.error BA0n Could not start writing machine code.}
		catch (IOException e)
		{
			throw new JITException("BA0n", e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/08/17
	 */
	@Override
	public void endClass()
		throws JITException
	{
		// Lock
		synchronized (this.lock)
		{
			// Check order
			__order(JITCompilerOrder.END_CLASS);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/08/18
	 */
	@Override
	public void endField()
		throws JITException
	{
		// Lock
		synchronized (this.lock)
		{
			// Increase field number so the true end can be detected
			this._writtenfields++;
		
			// Check order
			__order(JITCompilerOrder.END_FIELD);
		}
	}
	
	/**
	 * {@inheritDoc
	 * @since 20165/08/19
	 */
	@Override
	public void endMethod()
	{
		// Lock
		synchronized (this.lock)
		{
			// Need to detect the end
			this._writtenmethods++;
			
			// No longer in a method
			this._method = null;
		
			// Check order
			__order(JITCompilerOrder.END_METHOD);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/08/18
	 */
	@Override
	public void field(JITFieldFlags __f, IdentifierSymbol __n,
		int __ni, FieldSymbol __t, int __ti, Object __cv)
		throws JITException, NullPointerException
	{
		// Check
		if (__f == null || __n == null || __t == null)
			throw new NullPointerException("NARG");
		
		// Lock
		synchronized (this.lock)
		{
			// Check order
			__order(JITCompilerOrder.FIELD_INFORMATION);
			
			// Add new field
			this._fields.add(new __Field__(this, __f, __n, __t, __cv));
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/08/17
	 */
	@Override
	public void fieldCount(int __n)
		throws JITException
	{
		// Lock
		synchronized (this.lock)
		{
			// Needs to be set before so that if there are no fields they
			// can fully be ignored.
			this._fieldcount = __n;
		
			// Check order
			__order(JITCompilerOrder.FIELD_COUNT);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/27
	 */
	@Override
	public void interfaceClasses(ClassNameSymbol[] __ins, int[] __dxs)
		throws JITException, NullPointerException
	{
		// Check
		if (__ins == null || __dxs == null)
			throw new NullPointerException("NARG");
		
		// Lock
		synchronized (this.lock)
		{
			// Check order
			__order(JITCompilerOrder.INTERFACE_CLASS_NAMES);
			
			// Could fail
			try
			{
				// Get output
				ExtendedDataOutputStream dos = this.output;
				
				// Align
				while ((dos.size() & 3) != 0)
					dos.writeByte(0);
			
				// {@squirreljme.error BA0z The interface table starts at a
				// position outside the range of the class size limit.}
				long pos = dos.size();
				if (pos < 0 || pos > _SIZE_LIMIT)
					throw new JITException("BA0z");
				this._ifacepos = (int)pos;
			
				// Write
				__GlobalPool__ gpool = this._gpool;
				int n = __ins.length;
				this._ifacecount = n;
				for (int i = 0; i < n; i++)
					dos.writeShort(gpool.__loadClass(__ins[i])._index);
			}
			
			// {@squirreljme.error BA10 Failed to write the interface table.}
			catch (IOException e)
			{
				throw new JITException("BA10", e);
			}
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/08/19
	 */
	@Override
	public void method(JITMethodFlags __f, IdentifierSymbol __n,
		int __ni, MethodSymbol __t, int __ti)
		throws JITException, NullPointerException
	{
		// Check
		if (__f == null || __n == null || __t == null)
			throw new NullPointerException("NARG");
		
		// Lock
		synchronized (this.lock)
		{
			// Check order
			__order(JITCompilerOrder.METHOD_INFORMATION);
			
			// Add new method
			__Method__ rv;
			this._methods.add((rv = new __Method__(this, __f, __n, __t)));
			this._method = rv;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/08/17
	 */
	@Override
	public void methodCount(int __n)
		throws JITException
	{
		// Lock
		synchronized (this.lock)
		{
			// Needs to be set before as methods could be skipped.
			this._methodcount = __n;
			
			// Check order
			__order(JITCompilerOrder.METHOD_COUNT);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/08/19
	 */
	@Override
	public void noCode()
		throws JITException
	{
		// Lock
		synchronized (this.lock)
		{
			// Check order
			__order(JITCompilerOrder.METHOD_CODE);
			
			throw new Error("TODO");
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/27
	 */
	@Override
	public void superClass(ClassNameSymbol __cn, int __dx)
		throws JITException
	{
		// Lock
		synchronized (this.lock)
		{
			// Check order
			__order(JITCompilerOrder.SUPER_CLASS_NAME);
			
			// Set
			this._scpooldx = (__cn == null ? 0 :
				this._gpool.__loadClass(__cn)._index);
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
			// {@squirreljme.error BA0i Attempt to process another part of
			// a class before the machine code writer has been closed.}
			if (this._codes != null)
				throw new JITException("BA0i");
			
			// {@squirreljme.error BA12 Partial write of class exceeds class
			// size limitation. (The current class size)}
			long sz;
			if ((sz = this.output.size()) > _SIZE_LIMIT)
				throw new JITException(String.format("BA12 %d", sz));
			
			// {@squirreljme.error BA0j JIT invocation is not in order.
			// (The order that was attempted to be used; The expected order)}
			JITCompilerOrder order = this._order;
			if (order != __exp)
				throw new JITException(String.format("BA0j %s %s", __exp,
					order));
			
			// Set next
			this._order = order.next(this._writtenfields, this._fieldcount,
				this._writtenmethods, this._methodcount);
		}
	}
}

