// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.system;

import cc.squirreljme.runtime.cldc.system.api.Call;

/**
 * This interface is used to implement system call bridges to be sent to the
 * remote kernel end.
 *
 * If the system call throws an exception, it should be attempted to be
 * initialized accordingly with the class thrown by the remote end and a
 * stack trace initialized to one which contains the remote trace on top of
 * a local stack trace.
 *
 * System calls which are intended to be used on the local end should not be
 * sent to the remote end but should be handled by the implementing class.
 *
 * @since 2018/02/21
 */
public interface SystemCallImplementation
	extends Call
{
	/**
	 * Performs the given system call intended to be implemented by the local
	 * end.
	 *
	 * @param __func The function being called.
	 * @param __args The arguments to the function.
	 * @return The value returned from the call.
	 * @since 2018/02/21
	 */
	public abstract Object systemCall(SystemFunction __func, Object... __args)
		throws NullPointerException;
}

