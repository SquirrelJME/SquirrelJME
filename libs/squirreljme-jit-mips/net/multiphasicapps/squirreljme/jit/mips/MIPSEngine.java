// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.mips;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import net.multiphasicapps.squirreljme.classformat.CodeVariable;
import net.multiphasicapps.squirreljme.classformat.StackMapType;
import net.multiphasicapps.squirreljme.jit.ActiveCacheState;
import net.multiphasicapps.squirreljme.jit.ArgumentAllocation;
import net.multiphasicapps.squirreljme.jit.CacheState;
import net.multiphasicapps.squirreljme.jit.DataType;
import net.multiphasicapps.squirreljme.jit.JITException;
import net.multiphasicapps.squirreljme.jit.JITStateAccessor;
import net.multiphasicapps.squirreljme.jit.Register;
import net.multiphasicapps.squirreljme.jit.SnapshotCacheState;
import net.multiphasicapps.squirreljme.jit.StackSlotOffsets;
import net.multiphasicapps.squirreljme.jit.TranslationEngine;
import net.multiphasicapps.squirreljme.linkage.MethodLinkage;

/**
 * This is the engine which is able to generate MIPS machine code.
 *
 * The ABI that this engine uses on MIPS machines is NUBI, documentation of it
 * is available here:
 * {@link ftp://ftp.linux-mips.org/pub/linux/mips/doc/NUBI/} in a file called
 * {@code MD00438-2C-NUBIDESC-SPC-00.20.pdf}.
 *
 * @see NUBI
 * @since 2017/02/11
 */
public class MIPSEngine
	extends TranslationEngine
{
	/** Minimum i-offset. */
	private static final int _MIN_IOFFSET =
		-32768;
	
	/** Maximum i-offset. (*/
	private static final int _MAX_IOFFSET =
		32767;
	
	/** The configuration used. */
	protected final MIPSConfig config;
	
	/** Saved registers which were used, so they are stored/restored. */
	final Set<MIPSRegister> _savedused =
		new LinkedHashSet<>();
	
	/**
	 * Initializes the MIPS engine.
	 *
	 * @param __conf The MIPS configuration to use.
	 * @param __jsa The accessor to the JIT state.
	 * @since 2017/02/11
	 */
	public MIPSEngine(MIPSConfig __conf, JITStateAccessor __jsa)
	{
		super(__conf, __jsa);
		
		// Set
		this.config = __conf;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/03/20
	 */
	@Override
	public ArgumentAllocation[] allocationForEntry(StackMapType[] __t)
		throws NullPointerException
	{
		// Check
		if (__t == null)
			throw new NullPointerException("NARG");
		
		// Setup return value
		int n = __t.length;
		ArgumentAllocation[] rv = new ArgumentAllocation[n];
		
		// Starting register points where arguments are placed
		MIPSRegister ni = NUBI.FIRST_INT_ARGUMENT;
		MIPSRegister nf = NUBI.FIRST_FLOAT_ARGUMENT;
		
		// Go through types
		MIPSConfig config = this.config;
		int bits = config.bits();
		for (int i = 0; i < n; i++)
		{
			// Allocate values into registers, if they do not fit then they
			// will be placed on the stack
			DataType type = toDataType(__t[i]);
			switch (type)
			{
					// 32-bit int
				case INTEGER:
					if (ni != null)
					{
						// Set used registers
						rv[i] = new ArgumentAllocation(type, ni);
						
						// Claimed
						ni = NUBI.nextArgument(ni);
						continue;
					}
					break;
					
					// 64-bit long
				case LONG:
					if (true)
						throw new todo.TODO();
					break;
				
					// NUBI has 64-bit registers but 
				case FLOAT:
				case DOUBLE:
					if (true)
						throw new todo.TODO();
					break;
				
					// Should not happen
				default:
					throw new RuntimeException("OOPS");
			}
			
			// On the stack?
			throw new todo.TODO();
		}
		
		return rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/03/22
	 */
	@Override
	public ArgumentAllocation allocationForReturn(StackMapType __t)
		throws NullPointerException
	{
		// Check
		if (__t == null)
			throw new NullPointerException("NARG");
		
		// Depends on the type
		MIPSConfig config = this.config;
		DataType type;
		switch ((type = toDataType(__t)))
		{
				// int
			case INTEGER:
				return new ArgumentAllocation(type, NUBI.A0);
			
				// long
			case LONG:
				if (isLongLong())
					return new ArgumentAllocation(type, NUBI.A0); 
				else
					return new ArgumentAllocation(type, NUBI.A0, NUBI.A1);
			
				// float
			case FLOAT:
				return new ArgumentAllocation(type, NUBI.FA0);
			
				// double
			case DOUBLE:
				if (config.isLongDouble())
					return new ArgumentAllocation(type, NUBI.FA0);
				else
					return new ArgumentAllocation(type, NUBI.FA0, NUBI.FA1);
			
				// Unknown
			default:
				throw new RuntimeException("OOPS");
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/03/21
	 */
	@Override
	public Register framePointerRegister()
	{
		return NUBI.FP;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/03/03
	 */
	@Override
	public void invokeMethod(CacheState __in, ActiveCacheState __out,
		MethodLinkage __ml, ActiveCacheState.Slot __rv,
		CacheState.Slot[] __args, ArgumentAllocation[] __allocs)
	{
		// Check
		if (__in == null || __out == null || __ml == null || __args == null)
			throw new NullPointerException("NARG");
		
		// Debug
		System.err.printf("DEBUG -- in=%s out=%s ml=%s rv=%s args=%s al=%s%n",
			__in, __out, __ml, __rv, Arrays.asList(__args),
			Arrays.asList(__allocs));
		
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/03/16
	 */
	@Override
	public boolean isRegisterArgument(Register __r)
		throws NullPointerException
	{
		// Check
		if (__r == null)
			throw new NullPointerException("NARG");
		
		return (__r == NUBI.A0 || __r == NUBI.A1 || __r == NUBI.A2 ||
			__r == NUBI.A3 || __r == NUBI.A4 || __r == NUBI.A5 ||
			__r == NUBI.A6 || __r == NUBI.A7 ||
			__r == NUBI.FA0 || __r == NUBI.FA1 || __r == NUBI.FA2 ||
			__r == NUBI.FA3 || __r == NUBI.FA4 || __r == NUBI.FA5 ||
			__r == NUBI.FA6 || __r == NUBI.FA7);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/03/16
	 */
	@Override
	public boolean isRegisterSaved(Register __r)
		throws NullPointerException
	{
		// Check
		if (__r == null)
			throw new NullPointerException("NARG");
		
		return (__r == NUBI.S0 || __r == NUBI.S1 || __r == NUBI.S2 ||
			__r == NUBI.S3 || __r == NUBI.S4 || __r == NUBI.S5 ||
			__r == NUBI.S6 || __r == NUBI.S7 || __r == NUBI.S8 ||
			__r == NUBI.S9 || __r == NUBI.S10 || __r == NUBI.S11 ||
			__r == NUBI.FS0 || __r == NUBI.FS1 || __r == NUBI.FS2 ||
			__r == NUBI.FS3 || __r == NUBI.FS4 || __r == NUBI.FS5 ||
			__r == NUBI.FS6 || __r == NUBI.FS7 || __r == NUBI.FS8 ||
			__r == NUBI.FS9 || __r == NUBI.FS10 || __r == NUBI.FS11);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/03/16
	 */
	@Override
	public boolean isRegisterTemporary(Register __r)
		throws NullPointerException
	{
		// Check
		if (__r == null)
			throw new NullPointerException("NARG");
		
		// Includes the PIC function call address
		return (__r == NUBI.PF || __r == NUBI.AT ||
			__r == NUBI.T1 || __r == NUBI.T2 ||
			__r == NUBI.FT0 || __r == NUBI.FT1 || __r == NUBI.FT2 ||
			__r == NUBI.FT3 || __r == NUBI.FT4 || __r == NUBI.FT5 ||
			__r == NUBI.FT6 || __r == NUBI.FT7 || __r == NUBI.FT8 ||
			__r == NUBI.FT9 || __r == NUBI.FT10 || __r == NUBI.FT11);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/03/21
	 */
	@Override
	public void loadRegister(DataType __t, List<Register> __dest,
		int __off, Register __base)
		throws JITException, NullPointerException
	{
		// Check
		if (__t == null || __dest == null || __base == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error AM0e Cannot use a floating point register as a
		// base for a load operation. (The specified register)}
		MIPSRegister base = (MIPSRegister)__base;
		if (base.isFloat())
			throw new JITException(String.format("AM0e %s", base));
		
		// {@squirreljme.error AM0b Offset out of range for load. (The offset)}
		if (__off < _MIN_IOFFSET || __off > _MAX_IOFFSET)
			throw new JITException(String.format("AM0b %d", __off));
		
		MIPSConfig config = this.config;
		
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/03/21
	 */
	@Override
	public void moveRegister(DataType __t, List<Register> __src,
		List<Register> __dest)
		throws JITException, NullPointerException
	{
		// Check
		if (__t == null || __src == null || __dest == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/03/21
	 */
	@Override
	public void storeRegister(DataType __t, List<Register> __src,
		int __off, Register __base)
		throws JITException, NullPointerException
	{
		// Check
		if (__t == null || __src == null || __base == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error AM0d Cannot use a floating point register as a
		// base for a store operation. (The specified register)}
		MIPSRegister base = (MIPSRegister)__base;
		if (base.isFloat())
			throw new JITException(String.format("AM0d %s", base));
		
		// Registers in slots start with lower indexes having least significant
		// values
		// So because of this on big endian place values at the end
		int at, sign;
		if (isBigEndian())
		{
			at = __off + __t.length();
			sign = -1;
		}
		
		// Otherwise the lower end
		else
		{
			at = __off;
			sign = 1;
		}
		
		// Write all registers
		MIPSFragmentOutput output = this.accessor.<MIPSFragmentOutput>
			codeFragment(MIPSFragmentOutput.class);
		MIPSConfig config = this.config;
		for (int i = 0, n = __src.size(); i < n; i++)
		{
			// Get
			MIPSRegister r = (MIPSRegister)__src.get(i);
			
			// Get the size of thi
			// Integers match the word size
			int rs;
			if (r.isInteger())
				rs = (isLongLong() ? 8 : 4);
			
			// Float depends (could be 32-bit or 64-bit)
			else
				throw new todo.TODO();
			
			// Lower first for big endian since it starts at the end
			if (isBigEndian())
				at -= rs;
			
			// {@squirreljme.error AM0c Offset out of range for store. (The
			// offset)}
			if (at < _MIN_IOFFSET || at > _MAX_IOFFSET)
				throw new JITException(String.format("AM0c %d", __off));
			
			// Generate
			output.storeWord(r, at, base);
			
			// Little endian adds following to get to higher addresses
			if (!isBigEndian())
				at += rs;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/20
	 */
	@Override
	public DataType toDataType(StackMapType __t)
		throws NullPointerException
	{
		// Check
		if (__t == null)
			throw new NullPointerException("NARG");
		
		// If an object use the size of a pointer
		if (__t == StackMapType.OBJECT)
			return (isLongLong() ? DataType.LONG : DataType.INTEGER);
		
		// Use normal mapping
		return toDataType(DataType.of(__t));
	}
	
	/**
	 * Aliases the given data type to handle software floating point.
	 *
	 * @parma __t The type to alias.
	 * @return The same type or the alias of the type for example if it is
	 * using software floating point.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/02/20
	 */
	public DataType toDataType(DataType __t)
		throws NullPointerException
	{
		// Check
		if (__t == null)
			throw new NullPointerException("NARG");
		
		// Depends
		switch (__t)
		{
				// Keep as is
			case INTEGER:
			case LONG:
				return __t;
				
				// Adjust if software mode
			case FLOAT:
			case DOUBLE:
				throw new todo.TODO();
				
				// Unknown
			default:
				throw new RuntimeException("OOPS");
		}
	}
}

