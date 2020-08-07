// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.multivm;

import org.gradle.api.Action;
import org.gradle.api.Task;

/**
 * Performs the action of building the virtual machine.
 *
 * @since 2020/08/07
 */
public class MultiVMLibraryTaskAction
	implements Action<Task>
{
	/** The single instance action. */
	public static final MultiVMLibraryTaskAction INSTANCE =
		new MultiVMLibraryTaskAction();
	
	/**
	 * {@inheritDoc}
	 * @since 2020/08/07
	 */
	@Override
	public void execute(Task __task)
	{
		throw new Error("TODO");
	}
}
