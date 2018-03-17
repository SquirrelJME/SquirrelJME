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

import cc.squirreljme.runtime.cldc.system.type.EnumType;

/**
 * This interface is used on the server end to handle incoming requests.
 *
 * @since 2018/03/02
 */
public interface ServiceServer
{
	/**
	 * Handles a service call from the client.
	 *
	 * @param __func The function to handle.
	 * @param __args The arguments to the call.
	 * @return The result of the call.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/03
	 */
	public abstract Object serviceCall(EnumType __func, Object... __args)
		throws NullPointerException;
}

