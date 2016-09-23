// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.nativecode.powerpc;

import java.io.OutputStream;
import net.multiphasicapps.squirreljme.nativecode.NativeAllocation;
import net.multiphasicapps.squirreljme.nativecode.NativeCodeException;
import net.multiphasicapps.squirreljme.nativecode.NativeCodeWriter;
import net.multiphasicapps.squirreljme.nativecode.NativeCodeWriterOptions;
import net.multiphasicapps.squirreljme.nativecode.NativeRegister;
import net.multiphasicapps.squirreljme.nativecode.risc.RISCWriter;

/**
 * This writes PowerPC machine code.
 *
 * @since 2016/09/14
 */
public class PowerPCWriter
	extends RISCWriter
	implements NativeCodeWriter
{
	/** The instruction writer. */
	protected final PowerPCOutputStream output;
	
	/**
	 * Initializes the native code generator.
	 *
	 * @param __o The options to use.
	 * @param __os The output stream of machine code.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/15
	 */
	public PowerPCWriter(NativeCodeWriterOptions __o, OutputStream __os)
		throws NullPointerException
	{
		super(__o);
		
		// Check
		if (__os == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.output = new PowerPCOutputStream(__o, __os);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/23
	 */
	@Override
	public void load(NativeRegister __base, long __addr,
		NativeAllocation __dest)
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
	public void store(NativeAllocation __src, NativeRegister __base,
		long __addr)
		throws NativeCodeException, NullPointerException
	{
		// Check
		if (__src == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
}

