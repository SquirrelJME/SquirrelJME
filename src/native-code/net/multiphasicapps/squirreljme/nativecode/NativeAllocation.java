// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.nativecode;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.multiphasicapps.util.unmodifiable.UnmodifiableList;

/**
 * This represents a single allocation which specifies that something is either
 * on the stack, inside of registers, or both.
 *
 * This class is immutable.
 *
 * @since 2016/09/09
 */
public final class NativeAllocation
{
	/** The stack position. */
	protected final int stackpos;
	
	/** The stack length. */
	protected final int stacklen;
	
	/** The allocated register set. */
	protected final List<NativeRegister> registers;
	
	/** String cache. */
	private volatile Reference<String> _string;
	
	/**
	 * Initializes the native allocation using the specified registers and
	 * potential stack areas.
	 *
	 * @param __sp The position on the stack, if the stack is not used then
	 * this must be negative.
	 * @param __sl The number of bytes used on the stack, if not used then
	 * the value must be zero.
	 * @param __r The registers used in the allocation, this is used
	 * directly.
	 * @param NativeCodeException If the stack requirements are not met;
	 * neither any stack or registers are allocated; or a register is
	 * duplicated.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/09
	 */
	NativeAllocation(int __sp, int __sl, NativeRegister... __r)
		throws NativeCodeException, NullPointerException
	{
		// Check
		if (__r == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error AR01 If the stack is not used then the length
		// must be zero and the stack position must be negative. (The stack
		// position; The stack length)}
		if (__sl < 0 || (__sl > 0 && __sp < 0))
			throw new NativeCodeException(String.format("AR01 %d %d", __sp,
				__sl));
		
		// {@squirreljme.error AR02 There must be at least one byte allocated
		// or at least one register allocated.}
		int nr = __r.length;
		if (nr <= 0 && __sl < 0)
			throw new NativeCodeException("AR02");
		
		// Set
		this.stackpos = __sp;
		this.stacklen = __sl;
		this.registers = UnmodifiableList.<NativeRegister>of(
			Arrays.<NativeRegister>asList(__r));
		
		// {@squirreljme.error AR03 The specified register was specified
		// multiple times in the allocation. (The register that was
		// duplicated)}
		Set<NativeRegister> dups = new HashSet<>();
		for (NativeRegister r : __r)
			if (r == null)
				throw new NullPointerException("NARG");
			else if (!dups.add(r))
				throw new NativeCodeException(String.format("AR03 %s", r));
				
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/09
	 */
	@Override
	public final boolean equals(Object __o)
	{
		// Must be the same
		if (!(__o instanceof NativeAllocation))
			return false;
		
		// Cast and check
		NativeAllocation o = (NativeAllocation)__o;
		return this.stackpos == o.stackpos &&
			this.stacklen == o.stacklen &&
			this.registers.equals(o.registers);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/09
	 */
	@Override
	public final int hashCode()
	{
		return (~this.stackpos) ^ (this.stacklen) ^ this.registers.hashCode();
	}
	
	/**
	 * Returns the list of used registers.
	 *
	 * @return The register list.
	 * @since 2016/09/09
	 */
	public final List<NativeRegister> registers()
	{
		return this.registers;
	}
	
	/**
	 * Returns the number of bytes that are used to store the allocation on
	 * the stack.
	 *
	 * @return The number of bytes used on the stack.
	 * @since 2016/09/09
	 */
	public final int stackLength()
	{
		return this.stacklen;
	}
	
	/**
	 * Returns the byte offse of the allocation on the stack.
	 *
	 * @return The position of the stack allocation, will be a negative value
	 * if the stack is not used.
	 * @since 2016/09/09
	 */
	public final int stackPosition()
	{
		return this.stackpos;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/09
	 */
	@Override
	public final String toString()
	{
		Reference<String> ref = this._string;
		String rv;
		
		// Cache?
		if (ref == null || null == (rv = ref.get()))
		{
			// Efficient print of both
			int stackpos = this.stackpos;
			int stacklen = this.stacklen;
			List<NativeRegister> registers = this.registers;
			
			// Stack and registers
			if (stacklen > 0 && !registers.isEmpty())
				rv = registers.toString() + "+" + stacklen + "(@" + stackpos +
					")";
			
			// Registers only
			else if (stacklen <= 0)
				rv = registers.toString();
			
			// Stack only
			else
				rv = stacklen + "(@" + stackpos + ")";
			
			// Cache
			this._string = new WeakReference<>(rv);
		}
		
		// Return
		return rv;
	}
	
	/**
	 * Returns the allocation type.
	 *
	 * @return The allocation type.
	 * @since 2016/09/18
	 */
	public final NativeAllocationType type()
	{
		// Both registers and stack?
		boolean r = useRegisters(), s = useStack();
		if (r && s)
			return NativeAllocationType.BOTH;
		
		// Registers only
		else if (r)
			return NativeAllocationType.REGISTER;
		
		// Stack only
		return NativeAllocationType.STACK;
	}
	
	/**
	 * Are registers being used?
	 *
	 * @return {@code true} if registers are being used.
	 * @since 2016/09/09
	 */
	public final boolean useRegisters()
	{
		return !this.registers.isEmpty();
	}
	
	/**
	 * Is the stack being used?
	 *
	 * @return {@code true} if the stack is being used.
	 * @since 2016/09/09
	 */
	public final boolean useStack()
	{
		return this.stacklen > 0;
	}
}

