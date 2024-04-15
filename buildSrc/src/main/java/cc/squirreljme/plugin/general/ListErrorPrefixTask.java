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
 * Lists error prefixes for the task.
 *
 * @since 2020/08/22
 */
public class ListErrorPrefixTask
	extends DefaultTask
{
	/**
	 * Initializes the task.
	 * 
	 * @since 2020/08/22
	 */
	@Inject
	public ListErrorPrefixTask()
	{
		// Describe this one
		this.setGroup("squirreljmeGeneral");
		this.setDescription("Lists error prefixes.");
		
		// The action to perform
		this.doLast(new ListErrorPrefixTaskAction());
	}
}
