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
import cc.squirreljme.plugin.util.NoteCalendarFinder;
import cc.squirreljme.plugin.util.NoteCalendarGenerator;
import java.io.IOException;
import javax.inject.Inject;
import org.gradle.api.DefaultTask;
import org.gradle.api.Task;

/**
 * Creates the developer note Calendar.
 *
 * @since 2020/06/27
 */
public class RecreateDeveloperNoteCalendarTask
	extends DefaultTask
{
	/**
	 * Initializes the task.
	 * 
	 * @param __exeTask The executable task.
	 * @since 2020/06/26
	 */
	@Inject
	public RecreateDeveloperNoteCalendarTask(FossilExeTask __exeTask)
	{
		// Set details of this task
		this.setGroup("squirreljmeGeneral");
		this.setDescription("Edits the developer note for the current day.");
		
		// The executable task must run first
		this.dependsOn(__exeTask);
		
		// Fossil must exist
		this.onlyIf(__task -> FossilExe.isAvailable(true));
		
		// Action to perform
		this.doLast(this::action);
	}
	
	/**
	 * Performs the task action.
	 * 
	 * @param __task The called task.
	 * @since 2020/06/26
	 */
	private void action(Task __task)
	{
		try
		{
			FossilExe exe = FossilExe.instance();
			NoteCalendarGenerator.generate(exe,
				NoteCalendarFinder.findNotes(exe));
		}
		catch (IOException e)
		{
			throw new RuntimeException("Could not generate calendar.", e);
		}
	}
}
