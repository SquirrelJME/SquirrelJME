// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit;

import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;

/**
 * This class is used to store registers. Since representing some values may
 * require multiple registers to be used, this makes a single type safe means
 * of having multiple registers specified. This also simplifies changing which
 * registers are used.
 *
 * A register may only be specified once.
 *
 * This class is immutable.
 *
 * @since 2017/04/16
 */
public final class RegisterList
	extends AbstractCollection<Register>
{
	/** The hashcode for the register list. */
	protected final int hashcode;
	
	/** The list of registers. */
	private final Register[] _registers;
	
	/**
	 * This initializes the register list using the given array for registers.
	 *
	 * @param __r The registers to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/04/16
	 */
	public RegisterList(Register[] __r)
		throws NullPointerException
	{
		this(Arrays.<Register>asList(
			Objects.<Register[]>requireNonNull(__r, "NARG")));
	}
	
	/**
	 * This initializes the register list using specified registers.
	 *
	 * @param __r The first register to use.
	 * @param __mr The remaining registers to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/04/16
	 */
	public RegisterList(Register __r, Register... __mr)
		throws NullPointerException
	{
		this(__combine(__r, __mr));
	}
	
	/**
	 * This initializes the register list using the given iterable for
	 * registers.
	 *
	 * @param __r The registers to use.
	 * @throws IllegalArgumentException If there are zero registers.
	 * @throws NullPointerException On null arguments or if the iterable
	 * sequence contains a null.
	 * @since 2017/04/16
	 */
	public RegisterList(Iterable<Register> __r)
		throws IllegalArgumentException, NullPointerException
	{
		// Check
		if (__r == null)
			throw new NullPointerException("NARG");
		
		// Copy registers
		Set<Register> set = new LinkedHashSet<>();
		for (Register r : __r)
		{
			// Check
			if (r == null)
				throw new NullPointerException("NARG");
			
			// Add
			set.add(r);
		}
		
		// {@squirreljme.error AQ1w No registers were specified for the
		// register list.}
		int n = set.size();
		if (n <= 0)
			throw new IllegalArgumentException("AQ1w");
		
		// Set
		Register[] registers = set.<Register>toArray(new Register[n]);
		this._registers = registers;
		
		// Calculate hashcode
		int hc = 0;
		for (Register r : registers)
			hc ^= r.hashCode();
		this.hashcode = hc;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/04/21
	 */
	@Override
	public boolean contains(Object __o)
	{
		// Never contains null
		if (__o == null)
			return false;
		
		// Need only one match
		for (Register r : this._registers)
			if (r.equals(__o))
				return true;
		
		// Not contained
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/04/16
	 */
	@Override
	public boolean equals(Object __o)
	{
		// Check
		if (!(__o instanceof RegisterList))
			return false;
		
		// Quick compare hashcode first
		RegisterList o = (RegisterList)__o;
		if (this.hashcode != o.hashcode)
			return false;
		
		throw new todo.TODO();
	}
	
	/**
	 * Obtains the register at the given index.
	 *
	 * @param __i The index to get.
	 * @return The register at that index.
	 * @throws IndexOutOfBoundsException If the register is not within
	 * bounds.
	 * @since 2017/04/21
	 */
	public Register get(int __i)
		throws IndexOutOfBoundsException
	{
		return this._registers[__i];
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/04/16
	 */
	@Override
	public int hashCode()
	{
		return this.hashcode;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/04/19
	 */
	@Override
	public Iterator<Register> iterator()
	{
		return new __Iterator__();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/04/21
	 */
	@Override
	public boolean remove(Object __o)
		throws UnsupportedOperationException
	{
		// The exception is only thrown if a match is found
		if (contains(__o))
			throw new UnsupportedOperationException("RORO");
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/04/19
	 */
	@Override
	public int size()
	{
		return this._registers.length;
	}
	
	/**
	 * Combines into a single array.
	 *
	 * @param __r The first value.
	 * @param __mr The remaining values.
	 * @return The combined 
	 * @throws NullPointerException On null arguments.
	 * @since 2017/04/19
	 */
	private static Register[] __combine(Register __r, Register... __mr)
		throws NullPointerException
	{
		// Check
		if (__r == null || __mr == null)
			throw new NullPointerException("NARG");
		
		// Merge into a single array
		int n = __mr.length;
		Register[] rv = new Register[n + 1];
		rv[0] = __r;
		for (int i = 0, j = 1; i < n; i++, j++)
			rv[j] = __mr[i];
		
		return rv;
	}
	
	/**
	 * Iterator over elements.
	 *
	 * @since 2017/04/21
	 */
	private final class __Iterator__
		implements Iterator<Register>
	{
		/** The index count. */
		protected final int count =
			RegisterList.this.size();
		
		/** The current index. */
		private volatile int _dx;
		
		/**
		 * {@inheritDoc}
		 * @since 2017/04/21
		 */
		@Override
		public boolean hasNext()
		{
			return this._dx < this.count;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2017/04/21
		 */
		@Override
		public Register next()
			throws NoSuchElementException
		{
			// None left
			int dx = this._dx;
			if (dx >= this.count)
				throw new NoSuchElementException("NSEE");
			
			// Return the next one
			this._dx = dx + 1;
			return RegisterList.this._registers[dx];
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2017/04/21
		 */
		@Override
		public void remove()
			throws UnsupportedOperationException
		{
			throw new UnsupportedOperationException("RORO");
		}
	}
}

