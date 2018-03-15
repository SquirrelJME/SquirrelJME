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

import cc.squirreljme.runtime.cldc.system.type.ClassType;

/**
 * This interface is associated with any exception or error which may be
 * thrown by a system call.
 *
 * @since 2018/03/14
 */
public interface SystemCallThrowable
{
	/**
	 * Returns the class type of the throwable.
	 *
	 * @return The throwable class type.
	 * @since 2018/03/14
	 */
	public abstract ClassType classType();
}

