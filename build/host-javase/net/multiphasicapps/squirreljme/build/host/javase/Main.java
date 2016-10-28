// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.build.host.javase;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import net.multiphasicapps.squirreljme.build.projects.ProjectManager;

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
		// Setup project manager
		// {@squirreljme.error AD01 The system property used for the binary
		// path was not specified.}
		// {@squirreljme.error AD02 The system property used for the root of
		// the SquirrelJME source tree was not specified.}
		ProjectManager pm = new ProjectManager(
			Paths.get(Objects.requireNonNull(
				System.getProperty(BINARY_PROPERTY), "AD01")),
			Paths.get(Objects.requireNonNull(
				System.getProperty(SOURCE_PROPERTY), "AD02")));
		
		
		throw new Error("TODO");
	}
}

