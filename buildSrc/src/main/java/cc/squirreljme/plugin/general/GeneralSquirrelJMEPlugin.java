// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.general;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

/**
 * This is a plugin that performs general SquirrelJME tasks and is mostly used
 * for extra utilities that are not needed by the build at all. Examples of
 * this would be managing the blog or otherwise.
 *
 * @since 2020/06/24
 */
public class GeneralSquirrelJMEPlugin
	implements Plugin<Project>
{
	/**
	 * {@inheritDoc}
	 * @since 2020/06/24
	 */
	@Override
	public void apply(Project __project)
	{
		// Print Fossil executable path
		__project.getTasks().create("fossilExe",
			FossilExeTask.class);
	}
}
