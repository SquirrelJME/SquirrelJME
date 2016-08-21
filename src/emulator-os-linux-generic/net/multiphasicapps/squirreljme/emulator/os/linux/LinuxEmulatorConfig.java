// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.emulator.os.linux;

import java.io.IOException;
import net.multiphasicapps.squirreljme.emulator.EmulatorConfig;
import net.multiphasicapps.squirreljme.paths.posix.PosixPath;
import net.multiphasicapps.squirreljme.paths.posix.PosixPaths;
import net.multiphasicapps.zip.blockreader.ZipEntry;
import net.multiphasicapps.zip.blockreader.ZipFile;

/**
 * Configuration for the Linux emulator.
 *
 * @since 2016/08/21
 */
public class LinuxEmulatorConfig
	extends EmulatorConfig
{
	/**
	 * Mounts the specified ZIP file at the given location.
	 *
	 * @param __zf The zip file to mount.
	 * @param __at The directory where the ZIP should be mounted.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/08/21
	 */
	public final void mount(ZipFile __zf, PosixPath __at)
		throws IOException, NullPointerException
	{
		// Check
		if (__at == null)
			throw new NullPointerException("NARG");
		
		// Lock
		synchronized (this.lock)
		{
			throw new Error("TODO");
		}
	}
}

