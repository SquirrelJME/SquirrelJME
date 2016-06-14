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

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
		Map<String, Path> roots = new LinkedHashMap<>();
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
			{
				// {@squirreljme.error BV05 The CPU was already specified.}
				if (wantcpu != null)
					throw new IllegalArgumentException("BV05");
				
				wantcpu = a.substring(2);
			}
			
			// {@squirreljme.cmdline -O(os) Select operating system to
			// simulate.}
			else if (a.startsWith("-O"))
			{
				// {@squirreljme.error BV06 The operating system was already
				// specified.}
				if (wantos != null)
					throw new IllegalArgumentException("BV06");
				
				wantos = a.substring(2);
			}
			
			// {@squirreljme.cmdline -R(name=(mode)=(over)=(path)) Adds a root
			// filesystem to the simulated machine. The name specifies the name
			// of the root (such as {@code /} or {@code C}. Not all simulators
			// support multiple roots or incorrectly named roots. The mode
			// determines how the root is to be treated.
			// "{@code w}" makes the root writable, otherwise it is read-only.
			// The overlay is optional and if it is not used then it must be
			// blank. The overlay is used so that any modifications to the
			// filesystem performed by the simulator are performed, they are
			// placed in the given root instead. If the overlay is not used
			// then all writes (assuming the filesystem is writable) go
			// to the actual filesystem.
			// The path specifies the source of which files make up the given
			// root filesystem. This may be a ZIP or ISO file so that a common
			// base for pre-existing files may be used for simpler debugging.
			// Note that for write support when using ZIP/ISO an overlay must
			// be specified.}
			else if (a.startsWith("-R"))
			{
				throw new Error("TODO");
			}
			
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

