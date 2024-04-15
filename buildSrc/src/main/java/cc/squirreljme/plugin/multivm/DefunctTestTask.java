// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.multivm;

import javax.inject.Inject;
import org.gradle.api.Task;
import org.gradle.api.tasks.testing.Test;

/**
 * This represents a test task which is defunct, it relies on another task
 * for testing.
 *
 * @since 2020/10/07
 */
public class DefunctTestTask
	extends Test
{
	/**
	 * Initializes the test.
	 * 
	 * @since 2020/10/07
	 */
	@Inject
	public DefunctTestTask()
	{
		// Set details of this task
		this.setGroup("defunct");
		this.setDescription("Defunct test task, relies on another test task.");
		
		// Never runs
		this.onlyIf(new AlwaysFalse());
		
		// Depend on testHosted since all of the tests are there and those
		// may assume as such
		this.dependsOn(this.getProject()
			.getTasks().findByName("testHosted"));
		
		// Make sure the task fails as quickly as possibles
		this.doFirst(new DefunctTestTaskAction());
	}
}
