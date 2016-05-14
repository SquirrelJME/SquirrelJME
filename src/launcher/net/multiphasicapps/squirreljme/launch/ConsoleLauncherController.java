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

import java.util.Calendar;

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
	
	/** The current time. */
	protected final Calendar currentcal =
		Calendar.getInstance();
	
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
		this.console = __al.createConsoleView();
		if (this.console == null)
			throw new RuntimeException("AY02");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/14
	 */
	@Override
	public void update()
	{
		// Update the calendar
		Calendar currentcal = this.currentcal;
		currentcal.setTimeInMillis(System.currentTimeMillis());
		
		// Get the console
		AbstractConsoleView console = this.console;
		int cols = console.getColumns(), rows = console.getRows();
		
		// Draw the name of the software
		console.put(0, 0, "SquirrelJME");
		
		// Force the console to be drawn
		console.displayConsole();
	}
}

