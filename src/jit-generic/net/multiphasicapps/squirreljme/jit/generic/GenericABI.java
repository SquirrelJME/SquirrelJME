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

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.multiphasicapps.squirreljme.jit.base.JITException;
import net.multiphasicapps.squirreljme.jit.JITObjectProperties;
import net.multiphasicapps.util.unmodifiable.UnmodifiableList;
import net.multiphasicapps.util.unmodifiable.UnmodifiableMap;
import net.multiphasicapps.util.unmodifiable.UnmodifiableSet;

/**
 * This contains the information on a given ABI such as which registers are
 * used and the purpose of their usage.
 *
 * @since 2016/09/01
 */
public final class GenericABI
	implements JITObjectProperties
{
	/** The integer group. */
	private final __Group__ _int;
	
	/** The floating point group. */
	private final __Group__ _float;
	
	/** The current stack register. */
	private final GenericRegister _stack;
	
	/** The stack direction. */
	private final GenericStackDirection _stackdir;
	
	/** The stack alignment. */
	private final int _stackalign;
	
	/** Integer registers. */
	private final Map<GenericRegister, GenericRegisterIntegerType> _intregs;
	
	/** Floating point registers. */
	private final Map<GenericRegister, GenericRegisterFloatType> _floatregs;
	
	/**
	 * Initializes the ABI from the given builder.
	 *
	 * @param __b The builder creating this.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/01
	 */
	GenericABI(GenericABIBuilder __b)
		throws NullPointerException
	{
		// Check
		if (__b == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error BA0v The stack alignment was not set.}
		int stackalign = __b._stackalign;
		if (stackalign <= 0)
			throw new JITException("BA0v");
		this._stackalign = stackalign;
		
		// {@squirreljme.error BA0w The stack direction was not set.}
		GenericStackDirection stackdir = __b._stackdir;
		if (stackdir == null)
			throw new JITException("BA0w");
		this._stackdir = stackdir;
		
		// {@squirreljme.error BA0x The stack register was not set.}
		GenericRegister stack = __b._stack;
		if (stack == null)
			throw new JITException("BA0x");
		this._stack = stack;
		
		// Fill integer registers
		this._intregs = UnmodifiableMap.<GenericRegister,
			GenericRegisterIntegerType>of(new LinkedHashMap<>(__b._intregs));
			
		// Fill floating point registers
		this._floatregs = UnmodifiableMap.<GenericRegister,
			GenericRegisterFloatType>of(new LinkedHashMap<>(__b._floatregs));
		
		// Setup integer and float registers
		this._int = new __Group__(false, __b);
		this._float = new __Group__(true, __b);
	}
	
	/**
	 * Returns the list of registers that are used for passing method
	 * arguments.
	 *
	 * @param __k The kind of registers used.
	 * @return The list of argument registers.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/01
	 */
	public final List<GenericRegister> arguments(GenericRegisterKind __k)
		throws NullPointerException
	{
		// Check
		if (__k == null)
			throw new NullPointerException("NARG");
		
		// Depends
		switch (__k)
		{
			case INTEGER: return _int._args;
			case FLOAT: return _float._args;
			
				// Unknown
			default:
				throw new RuntimeException("OOPS");
		}
	}
	
	/**
	 * Returns the type of float value that is stored in the specified
	 * register.
	 *
	 * @param __r The register to get the value size of.
	 * @return The float type.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/02
	 */
	public final GenericRegisterFloatType floatType(GenericRegister __r)
		throws NullPointerException
	{
		// Check
		if (__r == null)
			throw new NullPointerException("NARG");
		
		return this._floatregs.get(__r);
	}
	
	/**
	 * Returns the type of integer value that is stored in the specified
	 * register.
	 *
	 * @param __r The register to get the value size of.
	 * @return The integer type.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/02
	 */
	public final GenericRegisterIntegerType intType(GenericRegister __r)
		throws NullPointerException
	{
		// Check
		if (__r == null)
			throw new NullPointerException("NARG");
		
		return this._intregs.get(__r);
	}
	
	/**
	 * Checks if the specified register is a callee saved register.
	 *
	 * @param __r The register to check if it is callee saved.
	 * @return {@code true} then it is calle saved.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/01
	 */
	public final boolean isSaved(GenericRegister __r)
		throws NullPointerException
	{
		// Check
		if (__r == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
	
	/**
	 * Checks if the specified register is a caller saved register.
	 *
	 * @param __r The register to check if it is caller saved.
	 * @return {@code true} then it is caller saved.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/01
	 */
	public final boolean isTemporary(GenericRegister __r)
		throws NullPointerException
	{
		// Check
		if (__r == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/01
	 */
	@Override
	public final String[] properties()
	{
		throw new Error("TODO");
	}
	
	/**
	 * Returns the list of registers which are used to store the return value
	 * for when a method is called.
	 *
	 * @param __k The kinds of registers to get.
	 * @return The list of return value registers.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/01
	 */
	public final List<GenericRegister> result(GenericRegisterKind __k)
		throws NullPointerException
	{
		// Check
		if (__k == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
	
	/**
	 * Returns the list of saved registers which must be preserved across
	 * method calls. These registers are callee saved.
	 *
	 * @param __k The kinds of registers to get.
	 * @return The list of saved registers.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/01
	 */
	public final List<GenericRegister> saved(GenericRegisterKind __k)
		throws NullPointerException
	{
		// Check
		if (__k == null)
			throw new NullPointerException("NARG");
		
		// Depends
		switch (__k)
		{
			case INTEGER: return _int._lsaved;
			case FLOAT: return _float._lsaved;
			
				// Unknown
			default:
				throw new RuntimeException("OOPS");
		}
	}
	
	/**
	 * Returns the stack register.
	 *
	 * @return The stack register.
	 * @since 2016/09/01
	 */
	public final GenericRegister stack()
	{
		throw new Error("TODO");
	}
	
	/**
	 * Returns the direction of the stack.
	 *
	 * @return The stack direction.
	 * @since 2016/09/01
	 */
	public final GenericStackDirection stackDirection()
	{
		throw new Error("TODO");
	}
	
	/**
	 * Returns the list of registers which are considered temporary and are not
	 * saved across method calls. These registers are caller saved.
	 *
	 * @param __k The kind of registers used.
	 * @return The list of temporary registers.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/01
	 */
	public final List<GenericRegister> temporary(GenericRegisterKind __k)
		throws NullPointerException
	{
		// Check
		if (__k == null)
			throw new NullPointerException("NARG");
		
		// Depends
		switch (__k)
		{
			case INTEGER: return _int._ltemps;
			case FLOAT: return _float._ltemps;
			
				// Unknown
			default:
				throw new RuntimeException("OOPS");
		}
	}
	
	/**
	 * Fills from the source iterable sequence to the target collection using
	 * matching integer/floating point types.
	 *
	 * @param __float If {@code true} then floating point registers are
	 * considered.
	 * @param __from The source registers.
	 * @param __to The target registers to use.
	 * @return The total registers specified.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/01
	 */
	private int __fill(boolean __float,
		Iterable<GenericRegister> __from, Collection<GenericRegister> __to)
		throws NullPointerException
	{
		// Check
		if (__from == null || __to == null)
			throw new NullPointerException("NARG");
		
		// Copy registers of the same kind
		int rv = 0;
		for (GenericRegister r : __from)
			if ((__float && (null != floatType(r))) ||
				(!__float && (null != intType(r))))
			{
				__to.add(r);
				rv++;
			}
		
		// Return the add count
		return rv;
	}
	
	/**
	 * The group specifies the type of register to use.
	 *
	 * @since 2016/09/01
	 */
	private final class __Group__
	{
		/** Saved registers (set). */
		private final Set<GenericRegister> _ssaved;
	
		/** Temporary registers (set). */
		private final Set<GenericRegister> _stemps;
	
		/** Saved registers (list). */
		private final List<GenericRegister> _lsaved;
	
		/** Temporary registers (list). */
		private final List<GenericRegister> _ltemps;
	
		/** Arguments. */
		private final List<GenericRegister> _args;
	
		/** Results. */
		private final List<GenericRegister> _result;
		
		/**
		 * Initializes the grouping.
		 *
		 * @param __float Use floating point registers?
		 * @param __b The builder to get registers from.
		 * @throws NullPointerException On null arguments.
		 * @since 2016/09/01
		 */
		private __Group__(boolean __float, GenericABIBuilder __b)
			throws NullPointerException
		{
			// Check
			if (__b == null)
				throw new NullPointerException("NARG");
			
			// Register totals
			int total = 0;
			
			// Add results
			Iterable<GenericRegister> xresult = __b._result;
			List<GenericRegister> result = new ArrayList<>();
			total += __fill(__float, xresult, result);
			this._result = UnmodifiableList.<GenericRegister>of(result);
			
			// Add arguments
			Iterable<GenericRegister> xargs = __b._args;
			List<GenericRegister> args = new ArrayList<>();
			total += __fill(__float, xargs, args);
			this._args = UnmodifiableList.<GenericRegister>of(args);
			
			// Add saved registers
			Iterable<GenericRegister> xsaved = __b._saved;
			Set<GenericRegister> saved = new LinkedHashSet<>();
			total += __fill(__float, xsaved, saved);
			this._ssaved = UnmodifiableSet.<GenericRegister>of(saved);
			this._lsaved = UnmodifiableList.<GenericRegister>of(
				new ArrayList<>(saved));
			
			// Add temporary registers
			Iterable<GenericRegister> xtemps = __b._temps;
			Set<GenericRegister> temps = new LinkedHashSet<>();
			total += __fill(__float, xtemps, temps);
			this._stemps = UnmodifiableSet.<GenericRegister>of(temps);
			this._ltemps = UnmodifiableList.<GenericRegister>of(
				new ArrayList<>(temps));
			
			// {@squirreljme.error BA1e A register cannot be both saved
			// and temporary. (The register)}
			for (GenericRegister r : saved)
				if (temps.contains(r))
					throw new JITException(String.format("BA1e %s", r));
			
			// Make sure the collections have all matching types
			if (total > 0)
			{
				// {@squirreljme.error BA0y No result registers were
				// specified.}
				if (result.size() <= 0)
					throw new JITException("BA0y");
				
				// {@squirreljme.error BA0q No argument registers were
				// specified.}
				if (args.size() <= 0)
					throw new JITException("BA0q");
				
				// {@squirreljme.error BA1d The total number of temporary
				// registers and saved registers is zero.}
				if ((saved.size() + temps.size()) <= 0)
					throw new JITException("BA1d");
			}
			
			// Float is optional
			else
			{
				// {@squirreljme.error BA0r No integer registers were
				// specified.}
				if (!__float)
					throw new JITException("BA0r");
			}	
		}
	}
}

