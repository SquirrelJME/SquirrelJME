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

import java.io.OutputStream;
import net.multiphasicapps.squirreljme.ssjit.powerpc.PowerPCProducerFactory;
import net.multiphasicapps.squirreljme.ssjit.SSJITProducer;
import net.multiphasicapps.squirreljme.ssjit.SSJITProducerFactory;

/**
 * This targets 32-bit PowerPC systems.
 *
 * @since 2016/06/25
 */
public class PowerPC32ProducerFactory
	extends PowerPCProducerFactory
{
	/**
	 * Initializes the producer factory with no operating system.
	 *
	 * @since 2016/06/25
	 */
	public PowerPC32ProducerFactory()
	{
		super("powerpc32", null);
	}
	
	/**
	 * Initializes the producer factory with an optional operating system.
	 *
	 * @param __os The operating system to use.
	 * @since 2016/06/25
	 */
	public PowerPC32ProducerFactory(String __os)
	{
		super("powerpc32", __os);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/06/25
	 */
	@Override
	public SSJITProducer createProducer(OutputStream __os, Variant __v)
	{
		throw new Error("TODO");
	}
}

