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
 * Interface for {@link SystemFunction#THROWABLE_SET_STACK}.
 *
 * @since 2018/03/14
 */
public interface ThrowableSetStackCall
	extends Call
{
	/**
	 * Set the stack trace for the given throwable.
	 *
	 * @param __t The throwable to get the stack trace from.
	 * @param __e The elements to set the throwable's stack trace to.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/14
	 */
	public abstract void throwableSetStack(Throwable __t,
		CallTraceElement[] __e)
		throws NullPointerException;
}

