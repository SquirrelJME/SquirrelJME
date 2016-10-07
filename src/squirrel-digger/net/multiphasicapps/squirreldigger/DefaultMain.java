// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreldigger;

import java.nio.file.Paths;
import net.multiphasicapps.squirreldigger.game.FrameTimer;
import net.multiphasicapps.squirreldigger.game.Game;
import net.multiphasicapps.squirreldigger.game.player.Controller;
import net.multiphasicapps.squirreldigger.gui.lui.LUIGUI;

/**
 * This is the classical main entry point.
 *
 * @since 2016/10/06
 */
public class DefaultMain
{
	/**
	 * Main entry point.
	 *
	 * @param __args Program arguments.
	 * @since 2016/10/06
	 */
	public static void main(String... __args)
	{
		// Force to exist
		if (__args == null)
			__args = new String[0];
		
		// Just use the console based interface for now
		Game g;
		LUIGUI lg = new LUIGUI(
			(g = new Game(0L, Paths.get(System.getProperty("user.dir")))));
		
		// Setup controllers
		for (int i = 0; i < Integer.MAX_VALUE; i++)
		{
			Controller c = lg.localController(i);
			
			// No local players anymore
			if (c == null)
				break;
			
			// Add player and set controller
			g.addPlayer().setController(c);
		}
		
		// Execute loop
		new FrameTimer(g, lg).run();
	}
}

