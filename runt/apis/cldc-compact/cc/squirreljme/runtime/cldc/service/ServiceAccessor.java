// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.service;

import java.util.HashMap;
import java.util.Map;

/**
 * This class provides access to services which are available to the system
 * for clients to use.
 *
 * @since 2018/03/02
 */
public final class ServiceAccessor
{
	/**
	 * Not used.
	 *
	 * @since 2018/03/02
	 */
	private ServiceAccessor()
	{
	}
	
	/**
	 * Obtains the specified service instance.
	 *
	 * @param __cl The class of the client service interface.
	 * @return The instance of the client interface.
	 * @throws NoSuchServiceException If the specified service does not
	 * exist.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/02
	 */
	public static final <R> R service(Class<R> __cl)
		throws NoSuchServiceException, NullPointerException
	{
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
}

