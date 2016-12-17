// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.build.interpreter;

import net.multiphasicapps.squirreljme.build.projects.ProjectManager;
import net.multiphasicapps.squirreljme.kernel.KernelLaunchParameters;
import net.multiphasicapps.squirreljme.kernel.SystemInstalledSuites;

/**
 * This class manages and determines which suites are used and auto-installed
 * in the system class path.
 *
 * Note that the suites to use are selected by the kernel launch parameters and
 * as such any system suites by their project name which are not specified
 * will not appear in the class path apart from the built-in defaults.
 *
 * @since 2016/12/17
 */
public class InterpreterSystemSuites
	extends SystemInstalledSuites
{
	/**
	 * Initializes the system suite manager.
	 *
	 * @param __ai The interpreter which runs the system.
	 * @param __klp The launch parameters for the kernel.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/12/17
	 */
	public InterpreterSystemSuites(AutoInterpreter __ai,
		KernelLaunchParameters __klp)
		throws NullPointerException
	{
		// Check
		if (__ai == null || __klp == null)
			throw new NullPointerException("NARG");
		
		// Need this to find the system suites
		ProjectManager pm = __ai.projectManager();
		
		throw new Error("TODO");
	}
}

