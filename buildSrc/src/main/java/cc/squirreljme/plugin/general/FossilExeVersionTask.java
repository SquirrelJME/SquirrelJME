// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
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
 * Prints the Fossil version.
 *
 * @since 2020/06/24
 */
public class FossilExeVersionTask
	extends DefaultTask
{
	/**
	 * Initializes the task.
	 * 
	 * @param __exeTask The executable task.
	 * @since 2020/06/24
	 */
	@Inject
	public FossilExeVersionTask(FossilExeTask __exeTask)
	{
		// Set details of this task
		this.setGroup("squirreljmeGeneral");
		this.setDescription("Prints the Fossil executable path.");
		
		// Depend on the EXE
		this.dependsOn(__exeTask);
		
		// Action to perform
		this.doLast(this::action);
	}
	
	/**
	 * Performs the task action.
	 * 
	 * @since 2020/06/24
	 */
	private void action(Task __task)
	{
		__task.getLogger().lifecycle(
			"Fossil version is: " + FossilExe.instance().version());
	}
}
