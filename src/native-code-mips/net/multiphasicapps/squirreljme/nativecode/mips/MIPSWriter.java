// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.nativecode.mips;

import java.io.IOException;
import java.io.OutputStream;
import net.multiphasicapps.squirreljme.nativecode.NativeAllocation;
import net.multiphasicapps.squirreljme.nativecode.NativeCodeException;
import net.multiphasicapps.squirreljme.nativecode.NativeCodeWriter;
import net.multiphasicapps.squirreljme.nativecode.NativeCodeWriterOptions;
import net.multiphasicapps.squirreljme.nativecode.NativeRegister;
import net.multiphasicapps.squirreljme.nativecode.risc.RISCWriter;

/**
 * This writes MIPS machine code.
 *
 * @since 2016/09/14
 */
public class MIPSWriter
	extends RISCWriter
	implements NativeCodeWriter
{
	/** The instruction writer. */
	protected final MIPSOutputStream output;
	
	/**
	 * Initializes the native code generator.
	 *
	 * @param __o The options to use.
	 * @param __os The output stream of machine code.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/15
	 */
	public MIPSWriter(NativeCodeWriterOptions __o, OutputStream __os)
		throws NullPointerException
	{
		super(__o);
		
		// Check
		if (__os == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.output = new MIPSOutputStream(__o, __os);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/25
	 */
	@Override
	public void integerAddImmediate(NativeRegister __a, long __b,
		NativeRegister __dest)
		throws NativeCodeException, NullPointerException
	{
		// Check
		if (__a == null || __dest == null)
			throw new NullPointerException("NARG");
		
		// Write it
		try
		{
			this.output.mipsIntegerAddImmediate(__a, __b, __dest);
		}
		
		// {@squirreljme.error AW0d Failed to write the add immediate
		// operation.}
		catch (IOException e)
		{
			throw new NativeCodeException("AW0d", e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/23
	 */
	@Override
	public void registerLoad(int __b, NativeRegister __base, long __addr,
		NativeRegister __dest)
		throws NativeCodeException, NullPointerException
	{
		// Check
		if (__dest == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/23
	 */
	@Override
	public void registerStore(int __b, NativeRegister __src,
		NativeRegister __base, long __addr)
		throws NativeCodeException, NullPointerException
	{
		// Check
		if (__src == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error AW08 Address out of range. (The specified
		// address)}
		if (__addr < Integer.MIN_VALUE || __addr > Integer.MAX_VALUE)
			throw new NativeCodeException(String.format("AW08 %d", __addr));
		
		// Forward
		try
		{
			this.output.mipsIntegerStore(__b, __src, __base, (int)__addr);
		}
		
		// {@squirreljme.error AW0b Failed to write the register store.}
		catch (IOException e)
		{
			throw new NativeCodeException("AW0b", e);
		}
	}
}

