// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.lint;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * This is the main entry point for the linter.
 *
 * @since 2025/12/31
 */
@SquirrelJMEVendorApi
public class Main
{
	/**
	 * Main entry point.
	 *
	 * @param __args Program arguments.
	 * @throws IOException On read/write errors.
	 * @since 2025/12/31
	 */
	@SuppressWarnings("JvmTaintAnalysis")
	@SquirrelJMEVendorApi
	public static void main(String... __args)
		throws IOException
	{
		if (__args == null)
			throw new NullPointerException("NARG");
		
		// Arguments are 
		for (String arg : __args)
		{
			// Read input file
			Path path = Paths.get(arg).normalize().toAbsolutePath();
			
			// Open input file to parse and process all tokens, groups, and
			// otherwise. The linter requires no context and is syntax only!
			try (InputStream in = Files.newInputStream(path,
				StandardOpenOption.READ))
			{
				LevelClass classy = new LevelClass(in);
				
				throw Debugging.todo();
			}
		}
	}
}
