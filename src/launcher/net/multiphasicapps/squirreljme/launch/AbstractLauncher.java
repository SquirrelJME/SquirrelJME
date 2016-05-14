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
 * This is the base class for the launcher interfaces which are defined by
 * systems to provide anything that the default launcher does not provide
 * when it comes to interfaces.
 *
 * @since 2016/05/14
 */
public abstract class AbstractLauncher
{
	/**
	 * Creates a view of a console window.
	 *
	 * Note that if multi-headed consoles are supported then the interface
	 * may show multiple terminals either in windows, tabs, or some other
	 * interface specific means. If a console does not support multiple heads
	 * then any console being displayed will potentially erase or draw over
	 * a previously drawn console.
	 *
	 * @return A newly created console window or {@code null} if it could not
	 * be created for some reason.
	 * @since 2016/05/14
	 */
	public abstract AbstractConsoleView createConsoleView();
	
	/**
	 * After initialization of a launcher, this should be called to actually
	 * start interacting with the system and the user.
	 *
	 * @since 2016/05/14
	 */
	public final void runLauncherLoop()
	{
		// For now just use the console
		ConsoleLauncherController clc = new ConsoleLauncherController(this);
		
		// Run the loop
		for (;;)
		{
			// Update the controller
			clc.update();
			
			// Yeild thread to let others run
			Thread.yield();
		}
	}
}

