// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.narf.classinterface;

import java.util.AbstractSet;
import java.util.Arrays;
import java.util.Iterator;

/**
 * This is the base class for all flag collections.
 *
 * @param <F> The flag type.
 * @since 2016/04/23
 */
public abstract class NCIFlags<F extends NCIFlag>
	extends AbstractSet<F>
{
	/**
	 * Initializes the flag set.
	 *
	 * @param __cl The class type of the flag.
	 * @param __fl The input flags.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/23
	 */
	NCIFlags(Class<F> __cl, F[] __fl)
		throws NullPointerException
	{
		this(__cl, Arrays.<F>asList(__fl));
	}
	
	/**
	 * Initializes the flag set.
	 *
	 * @param __cl The class type of the flag.
	 * @param __fl The input flags.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/23
	 */
	NCIFlags(Class<F> __cl, Iterable<F> __fl)
	{
		// Check
		if (__cl == null || __fl == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/04/23
	 */
	@Override
	public Iterator<F> iterator()
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/04/23
	 */
	@Override
	public int size()
	{
		throw new Error("TODO");
	}
}

