// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.os.linux.powerpc32;

import net.multiphasicapps.squirreljme.ssjit.SSJITProducer;
import net.multiphasicapps.squirreljme.ssjit.SSJITFunction;
import net.multiphasicapps.squirreljme.ssjit.SSJITFunctionProvider;
import net.multiphasicapps.squirreljme.ssjit.SSJITVariant;

/**
 * This function provider patches the PowerPC code generator in the event
 * that it requires adjustments to work on PowerPC Linux systems.
 *
 * @since 2016/06/27
 */
public class LinuxPPC32FunctionProvider
	extends SSJITFunctionProvider
{
	/** This is a generic variant. */
	public static final SSJITVariant GENERIC_VARIANT =
		new SSJITVariant()
		{
			/**
			 * {@inheritDoc}
			 * @since 2016/06/27
			 */
			@Override
			public String variant()
			{
				return "generic";
			}
		};
	
	/**
	 * Initializes the function provider.
	 *
	 * @since 2016/06/27
	 */
	public LinuxPPC32FunctionProvider()
	{
		super("powerpc32", "linux");
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
	
	/**
	 * {@inheritDoc}
	 * @since 2016/06/27
	 */
	@Override
	public SSJITVariant genericVariant()
	{
		return GENERIC_VARIANT;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/06/27
	 */
	@Override
	public SSJITVariant[] variants()
	{
		return new SSJITFunction[]{};
	}
}

