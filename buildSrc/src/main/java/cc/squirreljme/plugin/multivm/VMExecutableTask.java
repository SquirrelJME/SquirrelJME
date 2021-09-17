// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.multivm;

import org.gradle.api.Task;

/**
 * Executable multi-vm task.
 *
 * @since 2020/08/15
 */
public interface VMExecutableTask
	extends Task
{
	/**
	 * Returns the source set that is used.
	 * 
	 * @return The source set for the task.
	 * @since 2020/08/21
	 */
	String getSourceSet();
}
