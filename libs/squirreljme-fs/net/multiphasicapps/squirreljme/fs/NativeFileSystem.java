// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.fs;

import java.io.Closeable;
import java.io.IOException;
import net.multiphasicapps.squirreljme.paths.NativePath;
import net.multiphasicapps.squirreljme.paths.NativePaths;

/**
 * This is an interface which provides access to native filesystems which
 * may be supported on a given system.
 *
 * @since 2016/08/21
 */
public interface NativeFileSystem
	extends Closeable
{
	/**
	 * Returns an array containing the root directories which are currently
	 * available in this filesystem. Due to the potential for new volumes to
	 * be mounted and unmounted at unspecified times, the return value may
	 * differ between invocations.
	 *
	 * @return An array containing the root directories.
	 * @since 2016/08/21
	 */
	public abstract NativePath[] getRootDirectories()
		throws IOException;
	
	/**
	 * Returns the path system which is used for this filesystem.
	 *
	 * @return The path system used for this filesystem.
	 * @since 2016/08/21
	 */
	public abstract NativePaths paths();
}

