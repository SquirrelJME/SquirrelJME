// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package dev.shadowtail.classfile.pool;

/**
 * This contains the dual class and run-time constant pool information, the
 * run-time relies on the main class one, however only the run-time one is
 * needed for execution.
 *
 * @since 2019/07/17
 */
public final class DualClassRuntimePool
{
	/**
	 * Loads a value from the pool by its index.
	 *
	 * @param __rt Read from the run-time pool?
	 * @param __dx The index to read.
	 * @return The entry for the given index.
	 * @since 2019/09/07
	 */
	public final BasicPoolEntry getByIndex(boolean __rt, int __dx)
	{
		throw new todo.TODO();
	}
}

