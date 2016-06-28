// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.ssjit.powerpc.b32;

import net.multiphasicapps.squirreljme.ssjit.powerpc.PowerPCFunctionProvider;
import net.multiphasicapps.squirreljme.ssjit.SSJITProducer;
import net.multiphasicapps.squirreljme.ssjit.SSJITFunction;
import net.multiphasicapps.squirreljme.ssjit.SSJITFunctionProvider;
import net.multiphasicapps.squirreljme.ssjit.SSJITVariant;

/**
 * This targets 32-bit PowerPC systems.
 *
 * @since 2016/06/25
 */
public class PowerPC32FunctionProvider
	extends PowerPCFunctionProvider
{
	/**
	 * Initializes the function provider with no operating system.
	 *
	 * @since 2016/06/25
	 */
	public PowerPC32FunctionProvider()
	{
		super("powerpc32", null);
	}
	
	/**
	 * Initializes the function provider with an optional operating system.
	 *
	 * @param __os The operating system to use.
	 * @since 2016/06/25
	 */
	public PowerPC32FunctionProvider(String __os)
	{
		super("powerpc32", __os);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/06/27
	 */
	@Override
	public SSJITFunction[] functions(SSJITVariant __var)
		throws IllegalArgumentException
	{
		throw new Error("TODO");
	}
}

