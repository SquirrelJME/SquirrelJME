// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.fs.virtual;

import java.io.Closeable;
import java.io.IOException;
import net.multiphasicapps.squirreljme.fs.NativeFileSystem;
import net.multiphasicapps.squirreljme.paths.NativePath;
import net.multiphasicapps.squirreljme.paths.NativePaths;

/**
 * This represents a virtual file system which combines multiple data sources
 * to build a mounted directory tree.
 *
 * @since 2016/09/03
 */
public class VirtualFileSystem
	extends NativeFileSystem
{
	/**
	 * {@inheritDoc}
	 * @since 2016/09/03
	 */
	@Override
	public void close()
		throws IOException
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/03
	 */
	@Override
	public NativePath[] getRootDirectories()
		throws IOException
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/03
	 */
	@Override
	public NativePaths paths()
	{
		throw new Error("TODO");
	}
}

