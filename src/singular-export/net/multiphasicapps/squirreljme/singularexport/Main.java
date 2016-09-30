// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.singularexport;

import java.util.ArrayDeque;
import java.util.Deque;
import net.multiphasicapps.squirreljme.projects.ProjectGroup;
import net.multiphasicapps.squirreljme.projects.ProjectInfo;
import net.multiphasicapps.squirreljme.projects.ProjectList;

/**
 * Main entry class for the singular package export system.
 *
 * @since 2016/09/29
 */
public class Main
{
	/**
	 * Main entry point.
	 *
	 * @param __args Program arguments.
	 * @since 2016/09/29
	 */
	public static void main(String... __args)
	{
		// Force to exist
		if (__args == null)
			__args = new String[0];
		
		// Queue all strings
		Deque<String> args = new ArrayDeque<>();
		for (String s : __args)
			args.offerLast(s);
		
		// Handle all arguments
		while (!args.isEmpty())
		{
			String s = args.peekFirst();
			
			// If not a switch then remove it
			if (!s.startsWith("-"))
				break;
			
			// Otherwise remove it
			args.removeFirst();
			
			// Handle
			switch (s)
			{
					// {@squirreljme.error DV01 Unknown command line
					// argument. (The switch)}
				default:
					throw new IllegalArgumentException(String.format("DV01 %s",
						s));
			}
		}
		
		// {@squirreljme.error DV02 No global project list has been
		// initialized, this project may only be launched from the build
		// system.}
		ProjectList pl = ProjectList.getGlobalProjectList();
		if (pl == null)
			throw new IllegalStateException("DV02");
			
		// {@squirreljme.error DV03 No projects were specified on the command
		// line.}
		if (args.isEmpty())
			throw new IllegalArgumentException("DV03");
		
		throw new Error("TODO");
	}
}

