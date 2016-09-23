// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.nativecode.risc;

import java.io.OutputStream;
import java.util.List;
import net.multiphasicapps.squirreljme.nativecode.base.NativeTarget;
import net.multiphasicapps.squirreljme.nativecode.NativeABI;
import net.multiphasicapps.squirreljme.nativecode.NativeAllocation;
import net.multiphasicapps.squirreljme.nativecode.NativeCodeException;
import net.multiphasicapps.squirreljme.nativecode.NativeCodeWriter;
import net.multiphasicapps.squirreljme.nativecode.NativeCodeWriterOptions;
import net.multiphasicapps.squirreljme.nativecode.NativeRegister;
import net.multiphasicapps.squirreljme.nativecode.NativeStackDirection;

/**
 * This is the base class for writes which implement native code writers for
 * RISC-like systems.
 *
 * RISC systems do not have memory to memory operations, only register to
 * register, register to memory, and memory to register.
 *
 * @since 2016/09/21
 */
public abstract class RISCWriter
	implements NativeCodeWriter
{
	/** The options to use for code generation. */
	protected final NativeCodeWriterOptions options;
	
	/** The ABI to use. */
	protected final NativeABI abi;
	
	/**
	 * Initializes the native code generator.
	 *
	 * @param __o The options to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/15
	 */
	public RISCWriter(NativeCodeWriterOptions __o)
		throws NullPointerException
	{
		// Check
		if (__o == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.options = __o;
		this.abi = __o.abi();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/21
	 */
	@Override
	public final void copy(NativeAllocation __src, NativeAllocation __dest)
		throws NativeCodeException, NullPointerException
	{
		// Check
		if (__src == null || __dest == null)
			throw new NullPointerException("NARG");
		
		// Get destination usage
		boolean sr = __src.useRegisters(), ss = __src.useStack();
		boolean dr = __dest.useRegisters(), ds = __dest.useStack();
		
		// {@squirreljme.error DN01 The RISC native code generator is unable
		// to copy impure allocations that use both registers and stack.
		// (The source allocation; The destination allocation)}
		if (sr == ss || dr == ds)
			throw new NativeCodeException(String.format("DN01 %s %s", __src,
				__dest));
		
		// Need the ABI for the stack direction
		NativeABI abi = this.abi;
		NativeStackDirection sd = abi.stackDirection();
		
		// Determine number of bytes to read
		int bytesleft = abi.allocationValueSize(__src);
		
		// Must write all bytes
		while (bytesleft > 0)
		{
			// Register to register
			if (sr && dr)
				throw new Error("TODO");
		
			// Register to stack
			else if (sr && ds)
				throw new Error("TODO");
		
			// Stack to register
			else if (ss && dr)
				throw new Error("TODO");
		
			// Stack to stack
			else
				throw new Error("TODO");
		}
		
		/*
		// Get CPU endianess and word size
		NativeTarget nativetarget = abi.nativeTarget();
		int cpubytes = nativetarget.bits() / 8;
		
		// Needed so that only the minimum number of bytes are copied
		int bytesleft = abi.allocationValueSize(__src), byteshift = 0;
		
		// Destination 
		List<NativeRegister> dstregs = __dest.registers();
		int dstrat = 0, dstcnt = dstregs.size();
		
		// Copy the least significant part of the value first (always stored
		// in the register portion)
		if (bytesleft > 0 && sr)
		{
			// Will need to go through all of them
			List<NativeRegister> srcregs = __src.registers();
			int srcrat = 0, srccnt = srcregs.size();
			
			// Destination least significant
			while (bytesleft > 0 && srcrat < srccnt && dr)
				throw new Error("TODO");
			
			// Destination most significant
			while (bytesleft > 0 && srcrat < srccnt && ds)
			{
				// Get the next source register
				NativeRegister next = srcregs.get(srcrat++);
				
				throw new Error("TODO");
			}
		}
		
		// Copy the most significant part of the value last
		if (bytesleft > 0 && ss)
		{
			// Destination least significant
			while (bytesleft > 0 && dstrat < dstcnt && dr)
				throw new Error("TODO");
			
			// Destination most significant
			while (bytesleft > 0 && ds)
				throw new Error("TODO");
		}*/
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/15
	 */
	@Override
	public final NativeCodeWriterOptions options()
	{
		return this.options;
	}
}

