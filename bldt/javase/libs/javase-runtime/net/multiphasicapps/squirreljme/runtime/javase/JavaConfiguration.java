// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.runtime.javase;

import java.util.Arrays;
import net.multiphasicapps.squirreljme.kernel.KernelConfiguration;

/**
 * This is used to specify instances of services as required.
 *
 * @since 2018/01/03
 */
public class JavaConfiguration
	implements KernelConfiguration
{
	/**
	 * {@inheritDoc}
	 * @since 2018/01/03
	 */
	@Override
	public String mapService(String __sv)
		throws NullPointerException
	{
		if (__sv == null)
			throw new NullPointerException("NARG");
		
		switch (__sv)
		{
			default:
				return null;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/01/03
	 */
	@Override
	public Iterable<String> services()
	{
		return Arrays.<String>asList(new String[0]);
	}
}

