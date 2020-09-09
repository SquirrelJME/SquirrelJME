// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
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
		this.setGroup("squirreljme");
		this.setDescription("Returns the next free error prefix.");
		
		// The action to perform
		this.doLast(new NextErrorPrefixTaskAction());
	}
}
