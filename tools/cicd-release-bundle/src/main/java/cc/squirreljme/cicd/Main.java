// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.cicd;

import java.io.IOException;

/**
 * Main entry point delegation.
 *
 * @since 2025/10/22
 */
public class Main
{
	/**
	 * Main entry point.
	 *
	 * @param __args Program arguments.
	 * @throws IOException On read/write errors.
	 * @since 2025/10/22
	 */
	public static void main(String... __args)
		throws IOException
	{
		// Doxygen
		MainDoxygen.main(__args);
		
		// Bundle
		MainBundling.main(__args);
	}
}
