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
import cc.squirreljme.runtime.cldc.system.type.EnumType;

/**
 * Interface for {@link SystemFunction#SERVICE_CALL}.
 *
 * @since 2018/03/14
 */
public interface ServiceCallCall
	extends Call
{
	/**
	 * Performs a call into a service.
	 *
	 * @param __dx The service index.
	 * @param __func The function in the service.
	 * @param __args The function arguments.
	 * @return The return value of the call.
	 * @since 2018/03/02
	 */
	public abstract Object serviceCall(int __dx, EnumType __func,
		Object... __args);
}

