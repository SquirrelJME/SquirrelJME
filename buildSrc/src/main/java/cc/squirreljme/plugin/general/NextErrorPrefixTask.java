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
 * This determines what the next error prefix is.
 *
 * @since 2020/08/22
 */
public class NextErrorPrefixTask
	extends DefaultTask
{
	/**
	 * Initializes the task.
	 * 
	 * @since 2020/08/22
	 */
	@Inject
	public NextErrorPrefixTask()
	{
		// Describe this
		this.setGroup("squirreljmeGeneral");
		this.setDescription("Returns the next free error prefix.");
		
		// The action to perform
		this.doLast(new NextErrorPrefixTaskAction());
	}
}
