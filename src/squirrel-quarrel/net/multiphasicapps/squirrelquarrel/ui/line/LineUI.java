// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirrelquarrel.ui.line;

import java.util.Iterator;
import javax.microedition.lui.Display;
import net.multiphasicapps.squirrelquarrel.ui.GameUI;

/**
 * This game user interface uses the MEEP line based interface to display the
 * game and interact with the user.
 *
 * @since 2016/09/07
 */
public class LineUI
	extends GameUI
{
	/** The display to use. */
	protected final Display display;
	
	/**
	 * Initializes the line based user interface.
	 *
	 * @since 2016/09/08
	 */
	public LineUI()
	{
		// Find displays, use ones that provide keys first (if any)
		Display use = null;
__outer:
		for (boolean keys = true;; keys = false)
		{
			Iterator<Display> it = Display.getDisplays(keys);
			while (it.hasNext())
			{
				// Get
				Display d = it.next();
				
				// Try to claim the display
				d.setHardwareAssigned(true);
				if (d.isHardwareAssigned())
				{
					use = d;
					break __outer;
				}
			}
			
			// {@squirreljme.error BY02 No line based displays found.}
			if (!keys)
				throw new RuntimeException("BY02");
		}
		
		// Set
		this.display = use;
	}
}

