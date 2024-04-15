// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.general;

import cc.squirreljme.plugin.SquirrelJMEPluginConfiguration;
import cc.squirreljme.plugin.multivm.AlwaysFalse;
import cc.squirreljme.plugin.util.FossilExe;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import org.gradle.api.DefaultTask;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.tasks.javadoc.Javadoc;

/**
 * Updates the Fossil JavaDoc in the unversioned space.
 *
 * @since 2022/08/29
 */
public class UpdateFossilJavaDoc
	extends DefaultTask
{
	/**
	 * Initializes the JavaDoc update task.
	 * 
	 * @since 2022/08/29
	 */
	@Inject
	public UpdateFossilJavaDoc()
	{
		// What does this do?
		this.setGroup("squirreljmeGeneral");
		this.setDescription("Updates the Fossil unversioned JavaDoc.");
		
		// Fossil must be available, and since unversioned changes are always
		// done under a user we need a user!
		this.onlyIf((__task) -> FossilExe.isAvailable(true));
		
		// Always runs no matter what,
		this.getOutputs().upToDateWhen(new AlwaysFalse());
		
		// Add action to this task, dependencies are added when 
		this.doLast(new UpdateFossilJavaDocAction());
	}
}
