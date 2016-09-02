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

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import net.multiphasicapps.squirreljme.jit.base.JITException;
import net.multiphasicapps.squirreljme.jit.base.JITTriplet;

/**
 * This class is used to generate instances of {@link GenericABI} which is
 * used by the register allocator and the generic JIT compiler to determine
 * how other methods are called.
 *
 * @since 2016/09/01
 */
public final class GenericABIBuilder
{
	/** Lock. */
	protected final Object lock =
		new Object();
	
	/** Integer registers. */
	final Map<GenericRegister, GenericRegisterIntegerType> _intregs =
		new LinkedHashMap<>();
	
	/** Floating point registers. */
	final Map<GenericRegister, GenericRegisterFloatType> _floatregs =
		new LinkedHashMap<>();
	
	/** Saved registers. */
	final Set<GenericRegister> _saved =
		new LinkedHashSet<>();
	
	/** Temporary registers. */
	final Set<GenericRegister> _temps =
		new LinkedHashSet<>();
	
	/** Arguments. */
	final Set<GenericRegister> _args =
		new LinkedHashSet<>();
	
	/** Results. */
	final Set<GenericRegister> _result =
		new LinkedHashSet<>();
	
	/** The current stack register. */
	volatile GenericRegister _stack;
	
	/** The stack direction. */
	volatile GenericStackDirection _stackdir;
	
	/** The stack alignment. */
	volatile int _stackalign =
		-1;
	
	/**
	 * Adds an argument register.
	 *
	 * @param __r The register that is part of the input arguments for a
	 * method.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/01
	 */
	public final void addArgument(GenericRegister __r)
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
	public final void addRegister(GenericRegister __r, GenericRegisterType __t)
		throws NullPointerException
	{
		// Check
		if (__r == null || __t == null)
			throw new NullPointerException("NARG");
		
		// Lock
		synchronized (this.lock)
		{
			// Integer?
			if (__t instanceof GenericRegisterIntegerType)
				this._intregs.put(__r, (GenericRegisterIntegerType)__t);
			
			// Floating point?
			if (__t instanceof GenericRegisterFloatType)
				this._floatregs.put(__r, (GenericRegisterFloatType)__t);
		}
	}
	
	/**
	 * Adds a result register.
	 *
	 * @param __r The register that is used for the return value.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/01
	 */
	public final void addResult(GenericRegister __r)
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
	 * @throws JITException If the register is temporary or is the stack
	 * register.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/01
	 */
	public final void addSaved(GenericRegister __r)
		throws JITException, NullPointerException
	{
		// Check
		if (__r == null)
			throw new NullPointerException("NARG");
		
		// Lock
		synchronized (this.lock)
		{
			// {@squirreljme.error BA1b Cannot add the specified register
			// because it is the stack register or is temporary. (The register
			// being added)}
			if (this._temps.contains(__r) || this._stack.equals(__r))
				throw new JITException(String.format("BA1b %s", __r));
			
			this._saved.add(__r);
		}
	}
	
	/**
	 * Adds a caller saved register.
	 *
	 * @param __r The register that is caller saved.
	 * @throws JITException If the register is saved or is the stack register.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/01
	 */
	public final void addTemporary(GenericRegister __r)
		throws JITException, NullPointerException
	{
		// Check
		if (__r == null)
			throw new NullPointerException("NARG");
		
		// Lock
		synchronized (this.lock)
		{
			// {@squirreljme.error BA1c Cannot add the specified register
			// because it is the stack register or is saved. (The register
			// being added)}
			if (this._saved.contains(__r) || this._stack.equals(__r))
				throw new JITException(String.format("BA1c %s", __r));
			
			this._temps.add(__r);
		}
	}
	
	/**
	 * Builds the given ABI set.
	 *
	 * @return The generic ABI set.
	 * @throws JITException If a group contains no registers, the stack
	 * register was not set, or the direction register was not set.
	 * @since 2016/09/01
	 */
	public final GenericABI build()
		throws JITException
	{
		// Lock
		synchronized (this.lock)
		{
			return new GenericABI(this);
		}
	}
	
	/**
	 * Sets the stack register.
	 *
	 * @param __r The register to use for the stack.
	 * @throws JITException If the register is temporary or saved.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/01
	 */
	public final void stack(GenericRegister __r)
		throws JITException, NullPointerException
	{
		// Check
		if (__r == null)
			throw new NullPointerException("NARG");
		
		// Lock
		synchronized (this.lock)
		{
			// {@squirreljme.error BA0p Cannot use the specified register as
			// the stack register because it is saved and/or temporary. (The
			// register to be used as the stack register)}
			if (this._saved.contains(__r) || this._temps.contains(__r))
				throw new JITException(String.format("BA0p %s", __r));
			
			this._stack = __r;
		}
	}
	
	/**
	 * Sets the requires stack alignment needed for method calls.
	 *
	 * @param __i The number of bytes to align to.
	 * @throws JITException If the alignment is zero or negative.
	 * @since 2016/09/01
	 */
	public final void stackAlignment(int __i)
		throws JITException
	{
		// {@squirreljme.error BA0u The stack alignment is zero or negative.
		// (The alignment)}
		if (__i <= 0)
			throw new JITException(String.format("BA0u %d", __i));
		
		// Lock
		synchronized (this.lock)
		{
			this._stackalign = __i;
		}
	}
	
	/**
	 * Sets the direction of the stack.
	 *
	 * @param __d The stack direction.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/01
	 */
	public final void stackDirection(GenericStackDirection __d)
		throws NullPointerException
	{
		// Check
		if (__d == null)
			throw new NullPointerException("NARG");
		
		// Lock
		synchronized (this.lock)
		{
			this._stackdir = __d;
		}
	}
	
	/**
	 * Returns the integer type that matches the bit size of the given triplet.
	 *
	 * @param __t The triplet to get the type from.
	 * @return The integer type.
	 * @throws JITException If the type was not known.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/02
	 */
	public static GenericRegisterIntegerType intRegisterTypeFromTriplet(
		JITTriplet __t)
		throws JITException, NullPointerException
	{
		// Check
		if (__t == null)
			throw new NullPointerException("NARG");
		
		// Depends on the bits
		switch (__t.bits())
		{
			case 8: return GenericRegisterIntegerType.BYTE;
			case 16: return GenericRegisterIntegerType.SHORT;
			case 32: return GenericRegisterIntegerType.INTEGER;
			case 64: return GenericRegisterIntegerType.LONG;
			
				// {@squirreljme.error BA1f Could not get the integer register
				// type from the specified triplet. (The triplet)}
			default:
				throw new JITException(String.format("BA1f %s", __t));
		}
	}
	
	/**
	 * Returns the floating point type that matches the hardware floating
	 * point type used by the given triplet.
	 *
	 * @param __t The triplet to get the type from.
	 * @return The floating point type.
	 * @throws JITException If the type could not be determined.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/02
	 */
	public static GenericRegisterFloatType floatRegisterTypeFromTriplet(
		JITTriplet __t)
		throws JITException, NullPointerException
	{
		// Check
		if (__t == null)
			throw new NullPointerException("NARG");
		
		// Depends on the float type
		switch (__t.floatingPoint())
		{
			case HARD32: return GenericRegisterFloatType.FLOAT;
			case HARD64: return GenericRegisterFloatType.DOUBLE;
			
				// {@squirreljme.error BA1g Could not get the float register
				// type from the specified triplet. (The triplet)}
			default:
				throw new JITException(String.format("BA1g %s", __t));
		}
	}
}

