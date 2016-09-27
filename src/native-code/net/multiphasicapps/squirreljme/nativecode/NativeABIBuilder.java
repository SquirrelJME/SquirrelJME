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

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import net.multiphasicapps.squirreljme.nativecode.base.NativeEndianess;
import net.multiphasicapps.squirreljme.nativecode.base.NativeFloatType;
import net.multiphasicapps.squirreljme.nativecode.base.NativeTarget;

/**
 * This class is used to generate instances of {@link NativeABI} which is
 * used by the register allocator and the generic JIT compiler to determine
 * how other methods are called.
 *
 * @since 2016/09/01
 */
public final class NativeABIBuilder
{
	/** Lock. */
	protected final Object lock =
		new Object();
	
	/** Integer registers. */
	final Map<NativeRegister, NativeRegisterIntegerType> _intregs =
		new LinkedHashMap<>();
	
	/** Floating point registers. */
	final Map<NativeRegister, NativeRegisterFloatType> _floatregs =
		new LinkedHashMap<>();
	
	/** Saved registers. */
	final Set<NativeRegister> _saved =
		new LinkedHashSet<>();
	
	/** Temporary registers. */
	final Set<NativeRegister> _temps =
		new LinkedHashSet<>();
	
	/** Arguments. */
	final Set<NativeRegister> _args =
		new LinkedHashSet<>();
	
	/** Scratch registers. */
	final Set<NativeRegister> _scratch =
		new LinkedHashSet<>();
	
	/** Results. */
	final Set<NativeRegister> _result =
		new LinkedHashSet<>();
	
	/** The strack frame layout. */
	volatile NativeStackFrameLayout _stacklayout;
	
	/** The native target. */
	volatile NativeTarget _nativetarget;
	
	/** Special purpose register, is optional. */
	volatile NativeRegister _special;
	
	/**
	 * Adds an argument register.
	 *
	 * @param __r The register that is part of the input arguments for a
	 * method.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/01
	 */
	public final void addArgument(NativeRegister __r)
		throws NullPointerException
	{
		// Check
		if (__r == null)
			throw new NullPointerException("NARG");
		
		// Lock
		synchronized (this.lock)
		{
			this._args.add(__r);
		}
	}
	
	/**
	 * Adds a register to the register list.
	 *
	 * @param __r The register to add.
	 * @param __t The type of value the register is.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/02
	 */
	public final void addRegister(NativeRegister __r, NativeRegisterType __t)
		throws NullPointerException
	{
		// Check
		if (__r == null || __t == null)
			throw new NullPointerException("NARG");
		
		// Lock
		synchronized (this.lock)
		{
			// Integer?
			if (__t instanceof NativeRegisterIntegerType)
				this._intregs.put(__r, (NativeRegisterIntegerType)__t);
			
			// Floating point?
			if (__t instanceof NativeRegisterFloatType)
				this._floatregs.put(__r, (NativeRegisterFloatType)__t);
		}
	}
	
	/**
	 * Adds a result register.
	 *
	 * @param __r The register that is used for the return value.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/01
	 */
	public final void addResult(NativeRegister __r)
		throws NullPointerException
	{
		// Check
		if (__r == null)
			throw new NullPointerException("NARG");
		
		// Lock
		synchronized (this.lock)
		{
			this._result.add(__r);
		}
	}
	
	/**
	 * Adds a callee saved register.
	 *
	 * @param __r The register that is callee saved.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/01
	 */
	public final void addSaved(NativeRegister __r)
		throws NullPointerException
	{
		// Check
		if (__r == null)
			throw new NullPointerException("NARG");
		
		// Lock
		synchronized (this.lock)
		{
			this._saved.add(__r);
		}
	}
	
	/**
	 * Adds a scratch register.
	 *
	 * @param __r The register to add.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/26
	 */
	public final void addScratch(NativeRegister __r)
		throws NullPointerException
	{
		// Check
		if (__r == null)
			throw new NullPointerException("NARG");
		
		// Lock
		synchronized (this.lock)
		{
			this._scratch.add(__r);
		}
	}
	
	/**
	 * Adds a caller saved register.
	 *
	 * @param __r The register that is caller saved.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/01
	 */
	public final void addTemporary(NativeRegister __r)
		throws NullPointerException
	{
		// Check
		if (__r == null)
			throw new NullPointerException("NARG");
		
		// Lock
		synchronized (this.lock)
		{
			this._temps.add(__r);
		}
	}
	
	/**
	 * Builds the given ABI set.
	 *
	 * @return The generic ABI set.
	 * @throws NativeCodeException If a group contains no registers, the stack
	 * register was not set, or the direction register was not set.
	 * @since 2016/09/01
	 */
	public final NativeABI build()
		throws NativeCodeException
	{
		// Lock
		synchronized (this.lock)
		{
			return new NativeABI(this);
		}
	}
	
	/**
	 * Sets the native target of the specified CPU.
	 *
	 * @param __t The native target.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/22
	 */
	public final void nativeTarget(NativeTarget __t)
		throws NullPointerException
	{
		// Check
		if (__t == null)
			throw new NullPointerException("NARG");
		
		// Lock
		synchronized (this.lock)
		{
			this._nativetarget = __t;
		}
	}
	
	/**
	 * Sets the special purpose register that may be used for any purpose
	 * required by the native code generator, as needed.
	 *
	 * @param __r The register to use, may be {@code null} to clear it.
	 * @since 2016/09/25
	 */
	public final void special(NativeRegister __r)
	{
		// Lock
		synchronized (this.lock)
		{
			this._special = __r;
		}
	}
	
	/**
	 * Sets the stack frame layout which is used.
	 *
	 * @param __l The layout to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/27
	 */
	public final void stackLayout(NativeStackFrameLayout __l)
		throws NullPointerException
	{
		// Check
		if (__l == null)
			throw new NullPointerException("NARG");
		
		// Lock
		synchronized (this.lock)
		{
			return this._stacklayout;
		}
	}
	
	/**
	 * Returns the integer type that matches the bit size of the given bits.
	 *
	 * @param __t The number of bits the CPU uses
	 * @return The integer type.
	 * @throws NativeCodeException If the type was not known.
	 * @since 2016/09/02
	 */
	public static NativeRegisterIntegerType intRegisterType(int __t)
		throws NativeCodeException
	{
		// Depends on the bits
		switch (__t)
		{
			case 8: return NativeRegisterIntegerType.BYTE;
			case 16: return NativeRegisterIntegerType.SHORT;
			case 32: return NativeRegisterIntegerType.INTEGER;
			case 64: return NativeRegisterIntegerType.LONG;
			
				// {@squirreljme.error AR1f Could not get the integer register
				// type from the specified bit count. (The bit count)}
			default:
				throw new NativeCodeException(String.format("AR1f %d", __t));
		}
	}
	
	/**
	 * Returns the floating point type that matches the hardware floating
	 * point type.
	 *
	 * @param __t The floating point type to use.
	 * @return The floating point type.
	 * @throws NativeCodeException If the type could not be determined.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/02
	 */
	public static NativeRegisterFloatType floatRegisterType(
		NativeFloatType __t)
		throws NativeCodeException, NullPointerException
	{
		// Check
		if (__t == null)
			throw new NullPointerException("NARG");
		
		// Depends on the float type
		switch (__t)
		{
			case HARD32: return NativeRegisterFloatType.FLOAT;
			case HARD64: return NativeRegisterFloatType.DOUBLE;
			
				// {@squirreljme.error AR1g Could not get the float register
				// type from the specified floating point type. (The floating
				// point type)}
			default:
				throw new NativeCodeException(String.format("AR1g %s", __t));
		}
	}
}

