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

import net.multiphasicapps.squirreljme.nativecode.NativeAllocation;
import net.multiphasicapps.squirreljme.nativecode.NativeCodeException;
import net.multiphasicapps.squirreljme.nativecode.NativeCodeWriter;
import net.multiphasicapps.squirreljme.nativecode.NativeCodeWriterOptions;

/**
 * This writes MIPS machine code.
 *
 * @since 2016/09/14
 */
public class MIPSWriter
	implements NativeCodeWriter
{
	/** The options to use for code generation. */
	protected final NativeCodeWriterOptions options;
	
	/**
	 * Initializes the native code generator.
	 *
	 * @param __o The options to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/15
	 */
	public MIPSWriter(NativeCodeWriterOptions __o)
		throws NullPointerException
	{
		// Check
		if (__o == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.options = __o;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/21
	 */
	@Override
	public void copy(NativeAllocation __src, NativeAllocation __dest)
		throws NativeCodeException, NullPointerException
	{
		// Check
		if (__src == null || __dest == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
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

