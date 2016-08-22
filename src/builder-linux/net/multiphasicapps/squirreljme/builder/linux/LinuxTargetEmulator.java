// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.builder.linux;

import java.io.IOException;
import net.multiphasicapps.squirreljme.builder.TargetEmulator;
import net.multiphasicapps.squirreljme.builder.TargetEmulatorArguments;
import net.multiphasicapps.squirreljme.emulator.Emulator;
import net.multiphasicapps.squirreljme.emulator.os.linux.LinuxEmulator;
import net.multiphasicapps.squirreljme.emulator.os.linux.LinuxEmulatorConfig;
import net.multiphasicapps.squirreljme.paths.posix.PosixPath;
import net.multiphasicapps.squirreljme.paths.posix.PosixPaths;
import net.multiphasicapps.zip.blockreader.ZipEntry;
import net.multiphasicapps.zip.blockreader.ZipFile;

/**
 * This is the base class for all Linux based emulators.
 *
 * @since 2016/08/21
 */
public abstract class LinuxTargetEmulator
	extends TargetEmulator
{
	/**
	 * Sets up the emulator target.
	 *
	 * @param __args The arguments to the emulator.
	 * @since 2016/08/21
	 */
	public LinuxTargetEmulator(TargetEmulatorArguments __args)
	{
		super(__args);
	}
	
	/**
	 * This creates an architecture dependent Linux emulator.
	 *
	 * @return The Linux emulator.
	 * @since 2016/08/21
	 */
	protected abstract LinuxEmulator createLinuxEmulator();
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/30
	 */
	@Override
	public final Emulator emulator()
		throws IOException
	{
		// Create architecture dependent emulator
		LinuxEmulator rv = createLinuxEmulator();
		LinuxEmulatorConfig conf = rv.config();
		
		// Mount the ZIP to /opt
		PosixPaths pxp = PosixPaths.instance();
		PosixPath loaddir = pxp.get("/opt");
		conf.mount(arguments.zipFile(), loaddir);
		
		// Determine the binary name to launch
		TargetEmulatorArguments args = this.arguments;
		PosixPath binpath = loaddir.resolve(pxp.get(
			args.executableName("squirreljme")));
		
		// Debug
		System.err.printf("DEBUG -- Execute: %s%n", binpath);
		
		if (true)
			throw new Error("TODO");
		
		// Return it
		return rv;
	}
}

