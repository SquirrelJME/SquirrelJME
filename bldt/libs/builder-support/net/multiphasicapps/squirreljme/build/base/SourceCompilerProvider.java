// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.build.base;

import java.util.ServiceLoader;

/**
 * This class is used to create instances of source code compilers so that
 * each compilation instance is standalone. This class is used via the
 * service loader.
 *
 * @since 2016/12/19
 */
public abstract class SourceCompilerProvider
{
	/** Service lookup. */
	private static final ServiceLoader<SourceCompilerProvider> _SERVICES =
		ServiceLoader.<SourceCompilerProvider>load(
		SourceCompilerProvider.class);
	
	/**
	 * Creates a new instance of the source compiler.
	 *
	 * @return The compiler instance.
	 * @since 2016/12/19
	 */
	public abstract SourceCompiler newCompilerInstance();
	
	/**
	 * Creates a new instance of the source compiler.
	 *
	 * @return The compiler instance.
	 * @throws RuntimeException If no compiler is available.
	 * @since 2016/12/19
	 */
	public static SourceCompiler newInstance()
		throws RuntimeException
	{
		// Lock
		ServiceLoader<SourceCompilerProvider> services = _SERVICES;
		synchronized (services)
		{
			// Use the first service, whatever that may be
			for (SourceCompilerProvider p : services)
			{
				SourceCompiler rv = p.newCompilerInstance();
				if (rv != null)
					return rv;
			}
		}
		
		// {@squirreljme.error AU02 No compiler is available for usage.}
		throw new RuntimeException("AU02");
	}
}

