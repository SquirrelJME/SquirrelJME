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
 * Interface for {@link SystemFunction#THROWABLE_GET_STACK}.
 *
 * @since 2018/03/14
 */
public interface ThrowableGetStackCall
{
	/**
	 * Returns the stack trace for the given throwable.
	 *
	 * @param __t The throwable to get the stack trace from.
	 * @return The stack trace elements from the given throwable.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/14
	 */
	public abstract CallTraceElement[] throwableGetStack(Throwable __t)
		throws NullPointerException;
}

