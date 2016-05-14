// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.launch;

/**
 * This is the launcher controller which uses a console to interact with the
 * user. This uses a single console to display to the user and provides a
 * console like interface to programs running (multiple programs could be
 * displayed at the same time if desired) and shows the result of programs
 * as they are running.
 *
 * @since 2016/05/14
 */
public class ConsoleLauncherController
	extends LauncherController
{
	/** The console view which interacts with the user directly. */
	protected final AbstractConsoleView console;
	
	/**
	 * Initializes the console launcher controller.
	 *
	 * @param __al The launcher interface.
	 * @since 2016/05/14
	 */
	ConsoleLauncherController(AbstractLauncher __al)
	{
		super(__al);
		
		// {@squirreljme.error AY02 Could not initialize the console
		// that the console launcher controller would be using to display
		// and interact with the user.}
		console = __al.createConsoleView();
		if (console == null)
			throw new RuntimeException("AY02");
	}
}

