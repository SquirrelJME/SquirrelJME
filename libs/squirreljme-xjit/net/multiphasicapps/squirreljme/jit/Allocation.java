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

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.List;
import net.multiphasicapps.util.unmodifiable.UnmodifiableList;

/**
 * This is used to store the information such as which registers and the stack
 * offsets which are used to store a given argument on initial entry of a
 * method, when calling it, or when otherwise needed.
 *
 * @since 2017/03/20
 */
public final class Allocation
{
	/** The type of data used for the allocation. */
	protected final NativeType type;
	
	/** The stack offset. */
	protected final int stackoffset;
	
	/** Registers available for usage. */
	private final Register[] _registers;
	
	/** String representation. */
	private volatile Reference<String> _string;
	
	/** List of registers. */
	private volatile Reference<List<Register>> _lregs;
	
	/**
	 * Initializes the argument allocation with the specified registers.
	 *
	 * @param __t The type of data stored in the argument.
	 * @param __r The registers used to store the value.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/03/20
	 */
	public Allocation(NativeType __t, Register... __r)
		throws NullPointerException
	{
		// Check
		if (__t == null || __r == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.type = __t;
		this.stackoffset = Integer.MIN_VALUE;
		
		// Copy registers
		__r = __r.clone();
		this._registers = __r;
		for (Register r : __r)
			if (r == null)
				throw new NullPointerException("NARG");
	}
	
	/**
	 * Initializes the argument allocation with the specified stack offset.
	 *
	 * @param __t The type of data stored in the argument.
	 * @param __so The stack offset of the argument.
	 * @throws IllegalArgumentException If the stack offset is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/03/20
	 */
	public Allocation(NativeType __t, int __so)
		throws IllegalArgumentException, NullPointerException
	{
		// Check
		if (__t == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error AQ09 Invalid stack offset specified.}
		if (__so == Integer.MIN_VALUE)
			throw new IllegalArgumentException("AQ09");
		
		// Set
		this.type = __t;
		this.stackoffset = __so;
		this._registers = null;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/03/20
	 */
	@Override
	public boolean equals(Object __o)
	{
		// Check
		if (!(__o instanceof Allocation))
			return false;
		
		Allocation o = (Allocation)__o;
		return this.type == o.type &&
			this.stackoffset == o.stackoffset &&
			Arrays.equals(this._registers, o._registers);
	}
	
	/**
	 * Obtains the register at the given index.
	 *
	 * @param <R> The class to cast to.
	 * @param __cl The class to cast to.
	 * @param __i The register index to get.
	 * @return The register at this index.
	 * @throws ClassCastException If the class type is not valid.
	 * @throws IndexOutOfBoundsException If the register is out of bounds.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/03/20
	 */
	public <R extends Register> R getRegister(Class<R> __cl, int __i)
		throws ClassCastException, IndexOutOfBoundsException,
			NullPointerException
	{
		// Check
		if (__i < 0 || __i >= numRegisters())
			throw new IndexOutOfBoundsException("IOOB");
		
		Register[] registers = this._registers;
		return __cl.cast(registers[__i]);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/03/20
	 */
	@Override
	public int hashCode()
	{
		int rv = this.type.hashCode() ^ (~this.stackoffset);
		Register[] registers = this._registers;
		if (registers != null)
			for (Register r : registers)
				rv ^= r.hashCode();
		return rv;
	}
	
	/**
	 * Returns {@code true} if the allocation has registers.
	 *
	 * @return {@code true} if registers are valid.
	 * @since 2017/03/20
	 */
	public boolean hasRegisters()
	{
		return 0 != numRegisters();
	}
	
	/**
	 * Checks if these this allocation is register compatible with another.
	 *
	 * @param __o The other allocation to check.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/03/22
	 */
	public boolean isRegisterCompatible(Allocation __o)
		throws NullPointerException
	{
		// Check
		if (__o == null)
			throw new NullPointerException("NARG");
		
		return this.type == __o.type &&
			Arrays.equals(this._registers, __o._registers);
	}
	
	/**
	 * Returns the number of registers used.
	 *
	 * @return The register use count.
	 * @since 2017/03/20
	 */
	public int numRegisters()
	{
		Register[] registers = this._registers;
		return (registers == null ? 0 : registers.length);
	}
	
	/**
	 * Returns the list of registers that are available for usage.
	 *
	 * @return The list of registers, this list is not modifiable.
	 * @since 2017/03/31
	 */
	public List<Register> registerList()
	{
		Reference<List<Register>> ref = this._lregs;
		List<Register> rv;
		
		// Cache?
		if (ref == null || null == (rv = ref.get()))
			this._lregs = new WeakReference<>((rv =
				UnmodifiableList.<Register>of(Arrays.<Register>asList(
				this._registers))));
		
		return rv;
	}
	
	/**
	 * Returns all of the allocated registers.
	 *
	 * @return The allocated registers or {@code null} if there are none.
	 * @since 2017/03/20
	 */
	public Register[] registers()
	{
		Register[] registers = this._registers;
		return (registers == null ? null : registers.clone());
	}
	
	/**
	 * Returns the stack length of the argument.
	 *
	 * @return The stack length of the argument or {@link Integer#MIN_VALUE}
	 * if it is not valid.
	 * @since 2017/03/20
	 */
	public int stackLength()
	{
		return type().length();
	}
	
	/**
	 * Returns the stack offset of the argument.
	 *
	 * @return The stack offset or {@link Integer#MIN_VALUE} if it is not
	 * valid.
	 * @since 2017/03/20
	 */
	public int stackOffset()
	{
		return this.stackoffset;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/03/20
	 */
	@Override
	public String toString()
	{
		Reference<String> ref = this._string;
		String rv;
		
		// Cache?
		if (ref == null || null == (rv = ref.get()))
		{
			// On the stack?
			NativeType type = this.type;
			Register[] registers = this._registers;
			if (registers == null)
				rv = String.format("%s:s%d", type, this.stackoffset);
			
			// or in registers?
			else
				rv = String.format("%s:r%s", type,
					Arrays.asList(registers).toString());
			
			this._string = new WeakReference<>(rv);
		}
		
		return rv;
	}
	
	/**
	 * The type of data being stored.
	 *
	 * @return The data type used.
	 * @since 2017/03/20
	 */
	public NativeType type()
	{
		return this.type;
	}
}

