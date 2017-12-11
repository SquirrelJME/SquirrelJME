// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.runtime.syscall;

/**
 * This is a system caller which is intended to be used by sub-tasks which are
 * running in another process. It uses the basic packet system when interacting
 * with the kernel.
 *
 * @since 2017/12/10
 */
public final class SubTaskSystemCaller
	extends SystemCaller
{
	/**
	 * {@inheritDoc}
	 * @since 2017/12/11
	 */
	@Override
	public final SystemProgram[] listPrograms(int __typemask)
	{
		throw new todo.TODO();
	}
}

