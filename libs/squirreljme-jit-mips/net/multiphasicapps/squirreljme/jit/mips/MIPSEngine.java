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
import net.multiphasicapps.squirreljme.jit.Card;
import net.multiphasicapps.squirreljme.jit.DataType;
import net.multiphasicapps.squirreljme.jit.Deck;
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
	 * @since 2017/03/03
	 */
	@Override
	public void invokeRegister(Register __r)
		throws JITException, NullPointerException
	{
		// Check
		if (__r == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error AM0j Cannot jump to the value contained in the
		// floating point register. (The register)}
		MIPSRegister r = (MIPSRegister)__r;
		if (r.isFloat())
			throw new JITException(String.format("AM0j %s", __r));
		
		// Execute the jump
		if (true)
			throw new todo.TODO();
		/*
		MIPSFragmentOutput output = this.accessor.<MIPSFragmentOutput>
			codeFragment(MIPSFragmentOutput.class);
		output.jumpAndLinkRegisterImplied(r);
		
		// Do nothing in the branch delay slot
		if (config.hasBranchDelaySlots())
			output.nop();
		*/
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
		__commonLoadStore(__t, __dest, __off, __base, false);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/03/23
	 */
	@Override
	public void methodReturn()
		throws JITException
	{
		if (true)
			throw new todo.TODO();
		/*
		// Generate return to the link register
		MIPSFragmentOutput output = this.accessor.<MIPSFragmentOutput>
			codeFragment(MIPSFragmentOutput.class);
		output.jumpRegister(NUBI.RA);
		
		// Branch delay slot
		if (config.hasBranchDelaySlots())
			output.nop();
		*/
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
		__commonLoadStore(__t, __src, __off, __base, true);
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
	
	/**
	 * Loading and storing of register values since they share common logic.
	 *
	 * @param __t The type used.
	 * @param __ds The destination or source register.
	 * @param __off The offset from the base register.
	 * @param __base The base register.
	 * @param __store If {@code true} then a value is stored.
	 * @throws JITException If the registers are not valid or the stack offset
	 * is too large.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/03/23
	 */
	private void __commonLoadStore(DataType __t, List<Register> __ds,
		int __off, Register __base, boolean __store)
		throws JITException, NullPointerException
	{
		// Check
		if (__t == null || __ds == null || __base == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error AM0d Cannot use a floating point register as a
		// base for a load/store operation. (The specified register)}
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
		if (true)
			throw new todo.TODO();
		/*
		MIPSFragmentOutput output = this.accessor.<MIPSFragmentOutput>
			codeFragment(MIPSFragmentOutput.class);
		MIPSConfig config = this.config;
		for (int i = 0, n = __ds.size(); i < n; i++)
		{
			// Get
			MIPSRegister r = (MIPSRegister)__ds.get(i);
			
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
			
			// {@squirreljme.error AM0c Offset out of range for load/store.
			// (The offset)}
			if (at < _MIN_IOFFSET || at > _MAX_IOFFSET)
				throw new JITException(String.format("AM0c %d", __off));
			
			// Store
			if (__store)
			{
				switch (__t)
				{
						// byte
					case BYTE:
						throw new todo.TODO();
						
						// short
					case SHORT:
						throw new todo.TODO();
						
						// int
					case INTEGER:
						output.storeWord(r, at, base);
						break;
						
						// long
					case LONG:
						throw new todo.TODO();
						
						// float
					case FLOAT:
						throw new todo.TODO();
						
						// double
					case DOUBLE:
						throw new todo.TODO();
					
						// Unknown
					default:
						throw new RuntimeException("OOPS");
				}
			}
			
			// Load
			else
			{
				switch (__t)
				{
						// byte
					case BYTE:
						throw new todo.TODO();
						
						// short
					case SHORT:
						throw new todo.TODO();
						
						// int
					case INTEGER:
						output.loadWord(r, at, base);
						break;
						
						// long
					case LONG:
						throw new todo.TODO();
						
						// float
					case FLOAT:
						throw new todo.TODO();
						
						// double
					case DOUBLE:
						throw new todo.TODO();
					
						// Unknown
					default:
						throw new RuntimeException("OOPS");
				}
				
				// Generate NOP if no more loads are to be performed
				if (i + 1 >= n && config.hasLoadDelaySlots())
					output.nop();
			}
			
			// Little endian adds following to get to higher addresses
			if (!isBigEndian())
				at += rs;
		}
		*/
	}
}

