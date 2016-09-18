// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.bootstrap;

import net.multiphasicapps.squirreljme.bootstrap.base.compiler.BootCompiler;
import net.multiphasicapps.squirreljme.bootstrap.base.launcher.BootLauncher;
import net.multiphasicapps.squirreljme.projects.PackageList;

/**
 * This is the bootstrapper which is used to build and potentially launch
 * projects such as the SquirrelJME target builder.
 *
 * @since 2016/09/18
 */
public class Bootstrapper
{
	/**
	 * Initializes the bootstrapper.
	 *
	 * @param __pl The package list.
	 * @param __bc The compier for packages, optional.
	 * @param __bl The launcher for packages, optional.
	 * @throws NullPointerException If no package list was specified.
	 * @since 2016/09/18
	 */
	public Bootstrapper(PackageList __pl, BootCompiler __bc, BootLauncher __bl)
		throws NullPointerException
	{
		// Check
		if (__pl == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
}

