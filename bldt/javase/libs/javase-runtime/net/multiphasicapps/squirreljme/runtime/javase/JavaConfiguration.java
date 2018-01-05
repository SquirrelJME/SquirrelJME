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

import java.lang.ref.Reference;
import java.util.Arrays;
import net.multiphasicapps.squirreljme.kernel.Kernel;
import net.multiphasicapps.squirreljme.kernel.KernelConfiguration;
import net.multiphasicapps.squirreljme.kernel.KernelTask;

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
			case "net.multiphasicapps.squirreljme.kernel.lib.LibraryClient":
				return JavaLibraryServerFactory.class.getName();
				
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
		return Arrays.<String>asList(
			"net.multiphasicapps.squirreljme.kernel.lib.LibraryClient");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/01/03
	 */
	@Override
	public KernelTask systemTask(Reference<Kernel> __k)
		throws NullPointerException
	{
		if (__k == null)
			throw new NullPointerException("NARG");
		
		return new JavaSystemTask(__k);
	}
}

