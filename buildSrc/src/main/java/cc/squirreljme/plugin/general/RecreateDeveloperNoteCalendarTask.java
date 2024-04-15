// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.general;

import cc.squirreljme.plugin.util.FossilExe;
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
		this.doLast(new RecreateDeveloperNoteCalendarTaskAction());
	}
}
