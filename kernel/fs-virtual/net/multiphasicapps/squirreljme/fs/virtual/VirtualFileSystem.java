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
	implements NativeFileSystem
{
	/** The native path system to use. */
	protected final NativePaths paths;
	
	/** Mount point manager. */
	protected final VirtualMounts mounts =
		new VirtualMounts(this);
	
	/**
	 * Initializes the virtual file system.
	 *
	 * @param __np The native path system to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/05
	 */
	public VirtualFileSystem(NativePaths __np)
		throws NullPointerException
	{
		// Check
		if (__np == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.paths = __np;
	}
		
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
	public final NativePath[] getRootDirectories()
		throws IOException
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/03
	 */
	@Override
	public final NativePaths paths()
	{
		return this.paths;
	}
	
	/**
	 * Returns the virtual mounts which are used in this filesystem.
	 *
	 * @return The virtual mounting points.
	 * @since 2016/09/05
	 */
	public final VirtualMounts virtualMounts()
	{
		return this.mounts;
	}
}

