// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.general;

import javax.inject.Inject;
import org.gradle.api.DefaultTask;

/**
 * Task which essentially just outputs the executable path to Fossil.
 *
 * @since 2020/06/24
 */
public class FossilExeTask
	extends DefaultTask
{
	/**
	 * Initializes the task.
	 * 
	 * @since 2020/06/24
	 */
	@Inject
	public FossilExeTask()
	{
		// Set details of this task
		this.setGroup("squirreljmeGeneral");
		this.setDescription("Prints the Fossil executable path.");
		
		// Action to perform
		this.doLast(new FossilExeTaskAction());
	}
}
