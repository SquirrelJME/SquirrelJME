// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.runtime.javase;

import net.multiphasicapps.squirreljme.runtime.kernel.KernelTask;
import net.multiphasicapps.squirreljme.runtime.kernel.KernelTasks;

/**
 * This is the task manager which runs on the Java SE host system.
 *
 * @since 2017/12/27
 */
public class JavaTasks
	extends KernelTasks
{
	/**
	 * Initializes the task manager.
	 *
	 * @param __st The system task.
	 * @since 2017/12/27
	 */
	public JavaTasks(KernelTask __st)
	{
		super(__st);
	}
}

