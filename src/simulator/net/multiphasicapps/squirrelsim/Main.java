// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirrelsim;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

/**
 * This is the main entry point for the SquirrelJME simulator.
 *
 * @since 2016/06/14
 */
public class Main
{
	/**
	 * Main entry point for the simulator.
	 *
	 * @param __args Program arguments.
	 * @since 2016/06/14
	 */
	public static void main(String... __args)
	{
		// Enqueue all arguments for handling
		Deque<String> aq = new LinkedList<>();
		for (String a : __args)
			aq.add(a);
		
		// Handle them all
		String wantcpu = null;
		String wantos = null;
		List<String> wantprogram = new ArrayList<>();
		boolean gotend = false;
		while (!aq.isEmpty())
		{
			// Get next
			String a = aq.removeFirst();
			
			// End of arguments?
			if (a.equals("--"))
			{
				// Correct command line
				gotend = true;
				
				// Fill the desired program arguments
				while (!aq.isEmpty())
					wantprogram.add(aq.removeFirst());
				
				// Stop
				break;
			}
			
			// {@squirreljme.cmdline -C(arch) Select CPU to simulate.}
			if (a.startsWith("-C"))
				throw new Error("TODO");
			
			// {@squirreljme.cmdline -O(os) Select operating system to
			// simulate.}
			else if (a.startsWith("-O"))
				throw new Error("TODO");
			
			// {@squirreljme.error BV02 Unknown command line argument. (The
			// command line switch)}
			else
				throw new IllegalArgumentException(String.format("BV02 %s",
					a));
		}
		
		// {@squirreljme.error Expected to be given the program to launch.}
		if (!gotend)
			throw new IllegalArgumentException("BV01");
		
		// {@squirreljme.error BV03 No CPU was specified.}
		if (wantcpu == null)
			throw new IllegalArgumentException("BV03");
		
		// {@squirreljme.error BV04 No operating system was specified.}
		if (wantos == null)
			throw new IllegalArgumentException("BV04");
		
		throw new Error("TODO");
	}
}

