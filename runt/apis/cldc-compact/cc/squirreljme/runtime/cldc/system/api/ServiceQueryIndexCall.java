// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.system.api;

import cc.squirreljme.runtime.cldc.system.SystemFunction;

/**
 * Interface for {@link SystemFunction#SERVICE_QUERY_INDEX}.
 *
 * @since 2018/03/14
 */
public interface ServiceQueryIndexCall
	extends Call
{
	/**
	 * Queries the index of the service which implements the given class.
	 *
	 * @param __cl The class type to check the local service for.
	 * @return The index of the service which implements the given class.
	 * @since 2018/03/02
	 */
	public abstract int serviceQueryIndex(Class<?> __cl);
}

