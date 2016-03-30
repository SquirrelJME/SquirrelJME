// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.interpreter;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.AbstractList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import net.multiphasicapps.descriptors.FieldSymbol;
import net.multiphasicapps.descriptors.MethodSymbol;

/**
 * This class is given a chunk of byte code .
 *
 * @since 2016/03/29
 */
public class JVMByteProgram
	extends AbstractList<JVMByteProgram.Op>
{
	/** Lock. */
	protected final Object lock =
		new Object();
	
	/** Maximum local variables. */
	protected final int maxlocals;
	
	/** Maximum stack variables. */
	protected final int maxstack;
	
	/** The code length. */
	private final int length;
	
	/** The buffer containing the raw byte code. */
	private final byte[] _code;
	
	/** The position of each logical instruction to a physical one. */
	private final int[] _ipos;
	
	/** The operation cache. */
	private final Map<Op, Reference<Op>> _opcache =
		new WeakHashMap<>();
	
	/**
	 * This initializes the program using the specified code array.
	 *
	 * @param __ml Maximum number of local variables.
	 * @param __ms Maximum number of stack entries.
	 * @param __inst Is this an instance method?
	 * @param __desc The descriptor which describes the argument of this
	 * method, this is used to seed the initial stack.
	 * @param __ins Is this an instance method (which has a {@code this}?).
	 * @param __code The input byte code, note that it is not copied and that
	 * it is used directly.
	 * @throws JVMVerifyException If the maximum stack and local entries are
	 * negative, or there is not enough room
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/29
	 */
	public JVMByteProgram(int __ml, int __ms, MethodSymbol __desc,
		boolean __ins, byte... __code)
		throws JVMVerifyException, NullPointerException
	{
		// Check
		if (__desc == null || __code == null)
			throw new NullPointerException("NARG");
		
		// Set
		maxlocals = __ml;
		maxstack = __ms;
		_code = __code;
		length = _code.length;
		
		// Determine the position of all operations so that they can be
		// condensed into single index points (they all consume a single
		// address rather than multiple ones).
		int n = length;
		int[] bp = new int[n];
		int bpa = 0;
		for (int i = 0; i < n;)
		{
			// Set position where this instruction starts
			bp[bpa++] = i;
			
			// Get instruction size here
			int sz = __ByteCodeSizes__.__sizeOf(i, __code);
			
			// Negative or zero size?
			if (sz <= 0)
				throw new RuntimeException("WTFX");
			
			// Go to next instruction
			i += sz;
		}
		
		// The byte code for this method entirely uses single byte instructions
		// so no condensation is needed
		if (bpa == n)
			_ipos = bp;
		
		// Otherwise, condense
		else
		{
			// Setup array
			int[] actbp = new int[bpa];
			
			// Copy into it
			for (int i = 0; i < bpa; i++)
				actbp[i] = bp[i];
			
			// Use this array instead
			_ipos = actbp;
		}
		
		// Not needed
		bp = null;
		
		// Determine the control flow graph to determine which operations
		// flow into each other and which ones do not.
		
		// Setup the initial program state based on the method descriptor.
		if (true)
			throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/30
	 */
	@Override
	public Op get(int __i)
		throws IndexOutOfBoundsException
	{
		// Check
		if (__i < 0 || __i >= size())
			throw new IndexOutOfBoundsException(String.format("IOOB %d", __i));
		
		// lock
		synchronized (lock)
		{
			// The keyed integer
			Integer key = Integer.valueOf(__i);
			
			// Obtain reference
			Map<Op, Reference<Op>> cache = _opcache;
			Reference<Op> ref = cache.get(key);
			Op rv;
			
			// Needs caching?
			if (ref == null || null == (rv = ref.get()))
			{
				// Set it up, since the value is both a key and a value
				rv = new Op(__i);
				
				// Store into the map
				cache.put(rv, new WeakReference<>(rv));
			}
			
			// Return it
			return rv;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/30
	 */
	@Override
	public int size()
	{
		return _ipos.length;
	}
	
	/**
	 * This represents a single operation in the byte code chain.
	 *
	 * @since 2016/03/30
	 */
	public class Op
	{
		/** The address of this operation. */
		protected final int address;
		
		/**
		 * Initializes the operation.
		 *
		 * @param __pc The operation address.
		 * @since 2016/03/30
		 */
		private Op(int __pc)
		{
			address = __pc;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/03/30
		 */
		@Override
		public boolean equals(Object __o)
		{
			// If an integer, compare against the address
			if (__o instanceof Integer)
				return (address == ((Integer)__o).intValue());
			
			// Otherwise must be this
			return this == __o;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/03/30
		 */
		@Override
		public int hashCode()
		{
			// Just use the address
			return address;
		}
		
		/**
		 * Returns the next operation.
		 *
		 * @return The next operation or {@code null} if this is the last.
		 * @since 2016/03/30
		 */
		public Op next()
		{
			if (address >= (_ipos.length - 1))
				return null;
			return JVMByteProgram.this.get(address + 1);
		}
		
		/**
		 * Returns the previous operation.
		 *
		 * @return The previous operation or {@code null} if this is the first.
		 * @since 2016/03/30
		 */
		public Op previous()
		{
			if (address <= 0)
				return null;
			return JVMByteProgram.this.get(address - 1);
		}
	}
}

