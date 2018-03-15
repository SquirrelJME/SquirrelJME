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
 * Interface for {@link SystemFunction#SERVICE_QUERY_CLASS}.
 *
 * @since 2018/03/14
 */
public interface ServiceQueryClassCall
{
	/**
	 * Queries which class the client should use for the given service index.
	 *
	 * @param __dx The index to get the client class for.
	 * @return The client class for the given index.
	 * @since 2018/03/02
	 */
	public abstract ClassType serviceQueryClass(int __dx);
}

