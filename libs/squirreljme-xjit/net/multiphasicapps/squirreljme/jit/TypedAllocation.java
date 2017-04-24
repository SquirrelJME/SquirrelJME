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
import java.util.Objects;
import net.multiphasicapps.util.unmodifiable.UnmodifiableList;

/**
 * This is used to store the information such as which registers and the stack
 * offsets which are used to store a given argument on initial entry of a
 * method and when calling it.
 *
 * @since 2017/03/20
 */
public final class TypedAllocation
{
	/** The type of data used for the allocation. */
	protected final NativeType type;
	
	/** The stack offset. */
	protected final int stackoffset;
	
	/** Registers available for usage. */
	protected final RegisterList registers;
	
	/** String representation. */
	private volatile Reference<String> _string;
	
	/**
	 * Initializes the argument allocation with the specified registers.
	 *
	 * @param __t The type of data stored in the argument.
	 * @param __r The registers used to store the value.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/03/20
	 */
	public TypedAllocation(NativeType __t, RegisterList __r)
		throws NullPointerException
	{
		// Check
		if (__t == null || __r == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.type = __t;
		this.stackoffset = Integer.MIN_VALUE;
		this.registers = __r;
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
	public TypedAllocation(NativeType __t, int __so)
		throws IllegalArgumentException, NullPointerException
	{
		// Check
		if (__t == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error AQ1t Invalid stack offset specified.}
		if (__so == Integer.MIN_VALUE)
			throw new IllegalArgumentException("AQ1t");
		
		// Set
		this.type = __t;
		this.stackoffset = __so;
		this.registers = null;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/03/20
	 */
	@Override
	public boolean equals(Object __o)
	{
		// Check
		if (!(__o instanceof TypedAllocation))
			return false;
		
		TypedAllocation o = (TypedAllocation)__o;
		return this.type == o.type &&
			this.stackoffset == o.stackoffset &&
			Objects.equals(this.registers, o.registers);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/03/20
	 */
	@Override
	public int hashCode()
	{
		return this.type.hashCode() ^ (~this.stackoffset) ^
			Objects.hashCode(this.registers);
	}
	
	/**
	 * Returns {@code true} if the allocation has registers.
	 *
	 * @return {@code true} if registers are valid.
	 * @since 2017/03/20
	 */
	public boolean hasRegisters()
	{
		return null != this.registers;
	}
	
	/**
	 * Checks if these this allocation is register compatible with another.
	 *
	 * @param __o The other allocation to check.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/03/22
	 */
	public boolean isRegisterCompatible(TypedAllocation __o)
		throws NullPointerException
	{
		// Check
		if (__o == null)
			throw new NullPointerException("NARG");
		
		return this.type == __o.type &&
			Objects.equals(this.registers, __o.registers);
	}
	
	/**
	 * Returns all of the allocated registers.
	 *
	 * @return The allocated registers or {@code null} if there are none.
	 * @since 2017/03/20
	 */
	public RegisterList registers()
	{
		return this.registers;
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
			RegisterList registers = this.registers;
			if (registers == null)
				rv = String.format("%s:s%d", type, this.stackoffset);
			
			// or in registers?
			else
				rv = String.format("%s:r%s", type, registers);
			
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

