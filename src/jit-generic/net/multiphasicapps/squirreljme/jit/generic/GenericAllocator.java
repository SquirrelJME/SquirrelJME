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

import net.multiphasicapps.squirreljme.jit.base.JITException;
import net.multiphasicapps.squirreljme.jit.JITOutputConfig;

/**
 * This is a class which is used to allocate and manage native registers
 * which are available for a given CPU.
 *
 * @since 2016/08/30
 */
public final class GenericAllocator
{
	/** The configuration used. */
	protected final JITOutputConfig.Immutable config;
	
	/** The ABI used. */
	protected final GenericABI abi;
	
	/**
	 * Initializes the register allocator using the specified configuration
	 * and the given ABI.
	 *
	 * @param __conf The configuration being used.
	 * @param __abi The ABI used on the target system.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/01
	 */
	GenericAllocator(JITOutputConfig.Immutable __conf, GenericABI __abi)
		throws NullPointerException
	{
		// Check
		if (__conf == null || __abi == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.config = __conf;
		this.abi = __abi;
	}
}

