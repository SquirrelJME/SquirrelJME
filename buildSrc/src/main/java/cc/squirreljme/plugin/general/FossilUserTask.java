// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.general;

import cc.squirreljme.plugin.util.FossilExe;
import javax.inject.Inject;
import org.gradle.api.DefaultTask;
import org.gradle.api.Task;

/**
 * Prints the current fossil user.
 *
 * @since 2020/06/27
 */
public class FossilUserTask
	extends DefaultTask
{
	/**
	 * Initializes the task.
	 * 
	 * @param __exeTask The executable task.
	 * @since 2020/06/27
	 */
	@Inject
	public FossilUserTask(FossilExeTask __exeTask)
	{
		// Set details of this task
		this.setGroup("squirreljmeGeneral");
		this.setDescription("Prints the current Fossil user.");
		
		// The executable task must run first
		this.dependsOn(__exeTask);
		
		// Fossil must exist
		this.onlyIf(__task -> FossilExe.isAvailable(false));
		
		// Action to perform
		this.doLast(this::action);
	}
	
	/**
	 * Performs the task action.
	 * 
	 * @param __task The called task.
	 * @since 2020/06/27
	 */
	private void action(Task __task)
	{
		__task.getLogger().lifecycle(
			"Fossil user is: " + FossilExe.instance().currentUser());
	}
}
