// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.bootstrap.javase;

import java.io.IOException;
import net.multiphasicapps.squirreljme.bootstrap.javase.lcdui.
	SwingDisplayServer;

/**
 * This is the main entry point for the bootstrap bridge from Java SE to
 * SquirrelJME's bootstrap.
 *
 * @since 2016/09/18
 */
public class Main
{
	/**
	 * Main entry point.
	 *
	 * @param __args Program arguments.
	 * @throws IOException On read/write errors.
	 * @since 2016/09/18
	 */
	public static void main(String... __args)
		throws IOException
	{
		// Initialize LCDUI display server
		SwingDisplayServer sds = new SwingDisplayServer();
		
		// Forward call
		net.multiphasicapps.squirreljme.bootstrap.Main.main(
			new BridgedJavaCompiler(), new BridgedLauncher(), __args);
	}
}

