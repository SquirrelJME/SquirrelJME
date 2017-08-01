// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.build.host.javase;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import net.multiphasicapps.squirreljme.build.system.BuildSystem;

/**
 * This is the main entry point for the system which provides host support for
 * Java SE based virtual machine.
 *
 * @since 2016/10/27
 */
public class Main
{
	/**
	 * {@squirreljme.property net.multiphasicapps.squirreljme.build.binary=path
	 * This is the location where output target binaries are placed along with
	 * any built projects.}
	 */
	public static final String BINARY_PROPERTY =
		"net.multiphasicapps.squirreljme.build.binary";
	
	/**
	 * {@squirreljme.property net.multiphasicapps.squirreljme.build.source=path
	 * This is the path which points to the root of the SquirrelJME source
	 * tree.}
	 */
	public static final String SOURCE_PROPERTY =
		"net.multiphasicapps.squirreljme.build.source";
	
	/**
	 * Main entry point.
	 *
	 * @param __args Program arguments.
	 * @throws IOException On read/write errors.
	 * @since 2016/10/27
	 */
	public static void main(String... __args)
		throws IOException
	{
		// Setup build system
		// {@squirreljme.error BM01 The system property used for the binary
		// path was not specified.}
		// {@squirreljme.error BM02 The system property used for the root of
		// the SquirrelJME source tree was not specified.}
		BuildSystem bs = new BuildSystem(
			Paths.get(Objects.requireNonNull(
				System.getProperty(BINARY_PROPERTY), "BM01")),
			Paths.get(Objects.requireNonNull(
				System.getProperty(SOURCE_PROPERTY), "BM02")));
		
		// Call into it
		bs.main(__args);
	}
}

