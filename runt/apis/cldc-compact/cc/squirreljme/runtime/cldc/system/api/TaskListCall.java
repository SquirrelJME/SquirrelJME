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
 * Interface for {@link SystemFunction#TASK_LIST}.
 *
 * @since 2018/03/14
 */
public interface TaskListCall
{
	/**
	 * Returns the list of tasks which are available on the system.
	 *
	 * @param __incsys Include system tasks?
	 * @return The array of tasks available to the system.
	 * @since 2018/03/02
	 */
	public abstract IntegerArray taskList(boolean __incsys);
}

