// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin;

import org.gradle.api.Project;

/**
 * Runs SpringCoat application.
 *
 * @since 2020/02/16
 */
class __RunSpringCoatApplication__
	extends __RunVMApplication__
{
	/**
	 * Initializes the runner.
	 *
	 * @param __project The project to be ran.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/02/16
	 */
	__RunSpringCoatApplication__(Project __project)
		throws NullPointerException
	{
		super(__project, "springcoat");
		
		throw new Error("TODO");
	}
}

