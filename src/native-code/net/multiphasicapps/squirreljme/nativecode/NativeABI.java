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
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.multiphasicapps.squirreljme.nativecode.base.NativeEndianess;
import net.multiphasicapps.squirreljme.nativecode.base.NativeTarget;
import net.multiphasicapps.util.unmodifiable.UnmodifiableList;
import net.multiphasicapps.util.unmodifiable.UnmodifiableMap;
import net.multiphasicapps.util.unmodifiable.UnmodifiableSet;

/**
 * This contains the information on a given ABI such as which registers are
 * used and the purpose of their usage.
 *
 * @since 2016/09/01
 */
public final class NativeABI
{
	/** The stack frame layout. */
	protected final NativeStackFrameLayout stacklayout;
	
	/** The native CPU target. */
	protected final NativeTarget nativetarget;
	
	/** Special purpose register, which is optional. */
	protected final NativeRegister special;
	
	/** The integer group. */
	private final __Group__ _int;
	
	/** The floating point group. */
	private final __Group__ _float;
	
	/** Integer registers. */
	private final Map<NativeRegister, NativeRegisterIntegerType> _intregs;
	
	/** Floating point registers. */
	private final Map<NativeRegister, NativeRegisterFloatType> _floatregs;
	
	/**
	 * Initializes the ABI from the given builder.
	 *
	 * @param __b The builder creating this.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/01
	 */
	NativeABI(NativeABIBuilder __b)
		throws NullPointerException
	{
		// Check
		if (__b == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error AR1k No native stack layout was specified.}
		NativeStackFrameLayout stacklayout = __b._stacklayout;
		if (stacklayout == null)
			throw new NativeCodeException("AR1k");
		this.stacklayout = stacklayout;
		
		// {@squirreljme.error AR1j The native target was not specified.}
		NativeTarget nativetarget = __b._nativetarget;
		if (nativetarget == null)
			throw new NativeCodeException("AR1j");
		this.nativetarget = nativetarget;
		
		// Fill integer registers
		Map<NativeRegister, NativeRegisterIntegerType> irs;
		this._intregs = (irs = UnmodifiableMap.<NativeRegister,
			NativeRegisterIntegerType>of(new LinkedHashMap<>(__b._intregs)));
			
		// Fill floating point registers
		Map<NativeRegister, NativeRegisterFloatType> frs;
		this._floatregs = (frs = UnmodifiableMap.<NativeRegister,
			NativeRegisterFloatType>of(new LinkedHashMap<>(__b._floatregs)));
		
		// {@squirreljme.error AR05 A register is both an integer type and a
		// floating point type, however they differ in size. (The register; The
		// integer type; The floating point type; The number of bytes used to
		// store the integer value; The number of bytes used to store the
		// floating point value)}
		for (Map.Entry<NativeRegister, NativeRegisterFloatType> e :
			frs.entrySet())
		{
			NativeRegister k = e.getKey();
			NativeRegisterFloatType v = e.getValue();
			
			// Does it also have an associated integer register
			NativeRegisterIntegerType i = irs.get(k);
			int a, b;
			if (i != null && (a = i.bytes()) != (b = v.bytes()))
				throw new NativeCodeException(String.format(
					"AR05 %s %s %s %d %d", k, i, v, a, b));
		}
		
		// Setup integer and float registers
		this._int = new __Group__(false, __b);
		this._float = new __Group__(true, __b);
		
		// Special purpose
		this.special = __b._special;
	}
	
	/**
	 * Returns the total number of bytes which are used in a given allocation.
	 *
	 * @param __na The allocation to get the total used size for.
	 * @return The number of bytes used in the allocation.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/18
	 */
	public final int allocationTotalConsumed(NativeAllocation __na)
		throws NullPointerException
	{
		// Check
		if (__na == null)
			throw new NullPointerException("NARG");
		
		// Start with the stack
		int rv = __na.stackLength();
		
		// And add all register sizes
		List<NativeRegister> regs = __na.registers();
		for (int i = 0, n = regs.size(); i < n; i++)
			rv += registerType(regs.get(i)).bytes();
		return rv;
	}
	
	/**
	 * Returns the size of the allocation. If a value is associated with the
	 * allocation then that size is used, otherwise the stack space along with
	 * the register space is used.
	 *
	 * @param __na The allocation to get the size for.
	 * @return The number of bytes used for the allocation.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/18
	 */
	public final int allocationValueSize(NativeAllocation __na)
		throws NullPointerException
	{
		// Check
		if (__na == null)
			throw new NullPointerException("NARG");
		
		// Use the value type size first
		NativeRegisterType vt = __na.valueType();
		if (vt != null)
			return vt.bytes();
		
		// Otherwise add all bytes together
		return allocationTotalConsumed(__na);
	}
	
	/**
	 * Returns the number of bytes which are wasted in the allocation that
	 * over-represents the value of a given type. For example, storing a
	 * 32-bit {@code int} in a 64-bit register wastes 4 byts of space.
	 *
	 * @param __na The allocation to get the wasted byte count.
	 * @return The number of wasted bytes in the allocation.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/18
	 */
	public final int allocationWastedSpace(NativeAllocation __na)
		throws NullPointerException
	{
		return allocationTotalConsumed(__na) - allocationValueSize(__na);
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
	public final List<NativeRegister> arguments(NativeRegisterKind __k)
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
	 * @return The float type or {@code null} if not a float register.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/02
	 */
	public final NativeRegisterFloatType floatType(NativeRegister __r)
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
	 * @return The integer type or {@code null} if not an integer register.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/02
	 */
	public final NativeRegisterIntegerType intType(NativeRegister __r)
		throws NullPointerException
	{
		// Check
		if (__r == null)
			throw new NullPointerException("NARG");
		
		return this._intregs.get(__r);
	}
	
	/**
	 * Checks if the specified register is an argument register.
	 *
	 * @param __r The register to check.
	 * @return {@code true} if the register is an argument register.
	 * @since 2016/09/17
	 */
	public final boolean isArgument(NativeRegister __r)
		throws NullPointerException
	{
		// Check
		if (__r == null)
			throw new NullPointerException("NARG");
		
		// Arguments are checked
		return this._int._args.contains(__r) ||
			this._float._args.contains(__r);
	}
	
	/**
	 * Checks if the specified register is a callee saved register.
	 *
	 * @param __r The register to check if it is callee saved.
	 * @return {@code true} then it is calle saved.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/01
	 */
	public final boolean isSaved(NativeRegister __r)
		throws NullPointerException
	{
		// Check
		if (__r == null)
			throw new NullPointerException("NARG");
		
		// Only considered if it is in the saved set
		return this._int._ssaved.contains(__r) ||
			this._float._ssaved.contains(__r);
	}
	
	/**
	 * Checks if the specified register is an other kind of usage register.
	 *
	 * @param __r The register to check.
	 * @return {@code true} if the register is not an argument, saved,
	 * temporary register, or stack register.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/17
	 */
	public final boolean isOther(NativeRegister __r)
		throws NullPointerException
	{
		// Check
		if (__r == null)
			throw new NullPointerException("NARG");
		
		// Only if it is not anything
		NativeStackFrameLayout stacklayout = this.stacklayout;
		return !isArgument(__r) && !isSaved(__r) && !isTemporary(__r) &&
			!stacklayout.stackRegister().equals(__r) &&
			!stacklayout.frameRegister().equals(__r);
	}
	
	/**
	 * Checks if the specified register is a caller saved register.
	 *
	 * @param __r The register to check if it is caller saved.
	 * @return {@code true} then it is caller saved.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/01
	 */
	public final boolean isTemporary(NativeRegister __r)
		throws NullPointerException
	{
		// Check
		if (__r == null)
			throw new NullPointerException("NARG");
		
		// Temporaries are checked
		return this._int._stemps.contains(__r) ||
			this._float._stemps.contains(__r);
	}
	
	/**
	 * Returns the native target.
	 *
	 * @return The native target.
	 * @since 2016/09/22
	 */
	public final NativeTarget nativeTarget()
	{
		return this.nativetarget;
	}
	
	/**
	 * Returns the number of bytes that the target uses for pointers.
	 *
	 * @return The number of bytes in a pointer.
	 * @since 2016/09/25
	 */
	public final int pointerBytes()
	{
		return this.nativetarget.bits() / 8;
	}
	
	/**
	 * Returns the integer register type which is used for pointers.
	 *
	 * @return The pointer type used.
	 * @since 2016/09/25
	 */
	public final NativeRegisterIntegerType pointerType()
	{
		switch (this.nativetarget.bits())
		{
				// Map
			case 8: return NativeRegisterIntegerType.BYTE;
			case 16: return NativeRegisterIntegerType.SHORT;
			case 32: return NativeRegisterIntegerType.INTEGER;
			case 64: return NativeRegisterIntegerType.LONG;
			
				// Unknown
			default:
				throw new RuntimeException("OOPS");
		}
	}
	
	/**
	 * Returns the type of a given register. The integer type is preferred
	 * if a register also stores floating point values.
	 *
	 * @param __r The register to get the type for.
	 * @return The type of the register, if it is both a floating point type
	 * and an integer type then only the integer type will be returned.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/18
	 */
	public NativeRegisterType registerType(NativeRegister __r)
		throws NullPointerException
	{
		NativeRegisterIntegerType i = intType(__r);
		NativeRegisterFloatType f = floatType(__r);
		
		// Prefer integer first
		return (i != null ? i : f);
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
	public final List<NativeRegister> result(NativeRegisterKind __k)
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
	public final List<NativeRegister> saved(NativeRegisterKind __k)
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
	 * This returns the list of scratch registers.
	 *
	 * @param __k The kind of scratch registers to get.
	 * @return The list of scratch registers.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/23
	 */
	public final List<NativeRegister> scratch(NativeRegisterKind __k)
		throws NullPointerException
	{
		// Check
		if (__k == null)
			throw new NullPointerException("NARG");
		
		// Depends
		switch (__k)
		{
			case INTEGER: return _int._lscratch;
			case FLOAT: return _float._lscratch;
			
				// Unknown
			default:
				throw new RuntimeException("OOPS");
		}
	}
	
	/**
	 * Returns the scratch register with the specified index or {@code null}
	 * if there is none.
	 *
	 * @param __k The kind of scratch register to get.
	 * @param __i The index of the given register.
	 * @return The scratch register with that index or {@code null} if it was
	 * not found.
	 * @throws IndexOutOfBoundsException If the index is negative.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/26
	 */
	public final NativeRegister scratch(NativeRegisterKind __k, int __i)
		throws IndexOutOfBoundsException, NullPointerException
	{
		// {@squirreljme.error AR0a The scratch index is negative.}
		if (__i < 0)
			throw new IndexOutOfBoundsException("AR0a");
		
		// Get list
		List<NativeRegister> scratches = scratch(__k);
		
		// Could be outside of bounds
		if (__i >= scratches.size())
			return null;
		return scratches.get(__i);
	}
	
	/**
	 * Returns the optional special purpose register.
	 *
	 * @return The special purpose register or {@code null} if it has not been
	 * set.
	 * @since 2016/09/25
	 */
	public final NativeRegister special()
	{
		return this.special;
	}
	
	/**
	 * This returns the stack frame layout that is used for method calls.
	 *
	 * @return The native stack frame layout.
	 * @since 2016/09/27
	 */
	public final NativeStackFrameLayout stackLayout()
	{
		return this.stacklayout;
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
	public final List<NativeRegister> temporary(NativeRegisterKind __k)
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
		Iterable<NativeRegister> __from, Collection<NativeRegister> __to)
		throws NullPointerException
	{
		// Check
		if (__from == null || __to == null)
			throw new NullPointerException("NARG");
		
		// Copy registers of the same kind
		int rv = 0;
		for (NativeRegister r : __from)
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
		private final Set<NativeRegister> _ssaved;
	
		/** Temporary registers (set). */
		private final Set<NativeRegister> _stemps;
		
		/** Scratch registers. (set). */
		private final Set<NativeRegister> _sscratch;
	
		/** Saved registers (list). */
		private final List<NativeRegister> _lsaved;
	
		/** Temporary registers (list). */
		private final List<NativeRegister> _ltemps;
		
		/** Scratch registers. (list) */
		private final List<NativeRegister> _lscratch;
	
		/** Arguments. */
		private final List<NativeRegister> _args;
	
		/** Results. */
		private final List<NativeRegister> _result;
		
		/**
		 * Initializes the grouping.
		 *
		 * @param __float Use floating point registers?
		 * @param __b The builder to get registers from.
		 * @throws NullPointerException On null arguments.
		 * @since 2016/09/01
		 */
		private __Group__(boolean __float, NativeABIBuilder __b)
			throws NullPointerException
		{
			// Check
			if (__b == null)
				throw new NullPointerException("NARG");
			
			// Register totals
			int total = 0;
			
			// Add results
			Iterable<NativeRegister> xresult = __b._result;
			List<NativeRegister> result = new ArrayList<>();
			total += __fill(__float, xresult, result);
			this._result = UnmodifiableList.<NativeRegister>of(result);
			
			// Add arguments
			Iterable<NativeRegister> xargs = __b._args;
			List<NativeRegister> args = new ArrayList<>();
			total += __fill(__float, xargs, args);
			this._args = UnmodifiableList.<NativeRegister>of(args);
			
			// Add saved registers
			Iterable<NativeRegister> xsaved = __b._saved;
			Set<NativeRegister> saved = new LinkedHashSet<>();
			total += __fill(__float, xsaved, saved);
			this._ssaved = UnmodifiableSet.<NativeRegister>of(saved);
			this._lsaved = UnmodifiableList.<NativeRegister>of(
				new ArrayList<>(saved));
			
			// Add temporary registers
			Iterable<NativeRegister> xtemps = __b._temps;
			Set<NativeRegister> temps = new LinkedHashSet<>();
			total += __fill(__float, xtemps, temps);
			this._stemps = UnmodifiableSet.<NativeRegister>of(temps);
			this._ltemps = UnmodifiableList.<NativeRegister>of(
				new ArrayList<>(temps));
			
			// Scratch registers
			Iterable<NativeRegister> xscratch = __b._scratch;
			Set<NativeRegister> scratch = new LinkedHashSet<>();
			total += __fill(__float, xscratch, scratch);
			this._sscratch = UnmodifiableSet.<NativeRegister>of(scratch);
			this._lscratch = UnmodifiableList.<NativeRegister>of(
				new ArrayList<>(scratch));
				
			// Get stack and frame registers
			NativeStackFrameLayout stacklayout = NativeABI.this.stacklayout;
			NativeRegister sp = stacklayout.stackRegister(),
				fp = stacklayout.frameRegister();
			
			// {@squirreljme.error AR1e A register may only be saved,
			// temporary, or a scratch register. It is either multiple types or
			// are registers related to the stack. (The register)}
			for (NativeRegister r : saved)
				if (temps.contains(r) || scratch.contains(r) || sp.equals(r) ||
					fp.equals(r))
					throw new NativeCodeException(String.format("AR1e %s", r));
			for (NativeRegister r : temps)
				if (scratch.contains(r) || sp.equals(r) || fp.equals(r))
					throw new NativeCodeException(String.format("AR1e %s", r));
			
			// Make sure the collections have all matching types
			if (total > 0)
			{
				// {@squirreljme.error AR0y No result registers were
				// specified.}
				if (result.size() <= 0)
					throw new NativeCodeException("AR0y");
				
				// {@squirreljme.error AR0q No argument registers were
				// specified.}
				if (args.size() <= 0)
					throw new NativeCodeException("AR0q");
				
				// {@squirreljme.error AR1d The total number of temporary
				// registers and saved registers is zero.}
				if ((saved.size() + temps.size()) <= 0)
					throw new NativeCodeException("AR1d");
			}
			
			// Float is optional
			else
			{
				// {@squirreljme.error AR0r No integer registers were
				// specified.}
				if (!__float)
					throw new NativeCodeException("AR0r");
			}	
		}
	}
}

