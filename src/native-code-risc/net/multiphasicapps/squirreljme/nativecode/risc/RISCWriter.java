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
import net.multiphasicapps.squirreljme.nativecode.base.NativeEndianess;
import net.multiphasicapps.squirreljme.nativecode.base.NativeTarget;
import net.multiphasicapps.squirreljme.nativecode.NativeABI;
import net.multiphasicapps.squirreljme.nativecode.NativeAllocation;
import net.multiphasicapps.squirreljme.nativecode.NativeCodeException;
import net.multiphasicapps.squirreljme.nativecode.NativeCodeWriter;
import net.multiphasicapps.squirreljme.nativecode.NativeCodeWriterOptions;
import net.multiphasicapps.squirreljme.nativecode.NativeRegister;
import net.multiphasicapps.squirreljme.nativecode.NativeRegisterKind;
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
		
		// The number of bytes to write
		int bytesleft = abi.allocationValueSize(__src);
		
		// Need list of source and destination registers
		List<NativeRegister> srcregs = __src.registers();
		int srcrat = 0, srccnt = srcregs.size();
		List<NativeRegister> destregs = __dest.registers();
		int destrat = 0, destcnt = destregs.size();
		
		// Register to register, requires no stack work
		if (sr && dr)
			throw new Error("TODO");
		
		// Need the ABI for the stack direction and the offset for value
		// writing.
		NativeABI abi = this.abi;
		NativeTarget target = abi.nativeTarget();
		boolean bigend = (target.endianess() == NativeEndianess.BIG);
		NativeStackDirection sd = abi.stackDirection();
		
		// Figure out the bases needed for the source and the destination
		int srcbase = (ss ? sd.base(__src.stackPosition(), bytesleft) : 0),
			destbase = (ds ? sd.base(__dest.stackPosition(), bytesleft) : 0);
		
		// Determine the location write base (write from least significant to
		// most significant)
		int offwbase = (bigend ? bytesleft : 0);
		
		// Source registers from anything?
		List<NativeRegister> xregs = (sr ? srcregs : (dr ? destregs :
			abi.scratch(NativeRegisterKind.INTEGER)));
		int xrat = 0, xcnt = xregs.size();
		
		// Need the stack register
		NativeRegister stackreg = abi.stack();
		
		// Until all bytes are drained
		while (bytesleft > 0)
		{
			// Get the next register to act as the source/dest
			NativeRegister xreg = xregs.get(xrat++);
			
			// Determine the number of bytes to consume in the copy operation
			// Never consume more than what remains however
			int consume = Math.min(bytesleft, abi.registerType(xreg).bytes());
			
			// On big endian systems, lower values are written at higher
			// addresses. Since copying is from the base address (lower), this
			// means that the offset must be calculated before a write
			if (bigend)
				offwbase -= consume;
			
			// Register to stack
			if (sr)
				registerStore(consume, xreg, stackreg, destbase + offwbase);
	
			// Stack to register
			else if (dr)
				registerLoad(consume, stackreg, srcbase + offwbase, xreg);
	
			// Stack to stack, this is the worst operation since values must be
			// copied to the scratch registers first and then they have to be
			// copied to the destination. However, stack to stack causes a
			// recursive call of this method.
			else
			{
				if (true)
					throw new Error("TODO");
				
				// Scratch registers never run out, so reset
				xrat = 0;
			}
			
			// On little endian systems, writer higher values later in memory
			if (!bigend)
				offwbase += consume;
			
			// Those bytes were eaten
			bytesleft -= consume;
		}
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

