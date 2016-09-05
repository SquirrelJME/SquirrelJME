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

import java.io.IOException;
import net.multiphasicapps.squirreljme.paths.NativePath;

/**
 * This manages the mount points which are specified in the virtual filesystem
 * to locate files depending on the file source that they are using.
 *
 * @since 2016/09/05
 */
public class VirtualMounts
{
	/** Internal lock. */
	protected final Object lock =
		new Object();
	
	/** The owning virtual file system. */
	protected final VirtualFileSystem vfs;
	
	/**
	 * Initializes the virtual mount manager.
	 *
	 * @param __vfs The virtual file system owning this.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/05
	 */
	VirtualMounts(VirtualFileSystem __vfs)
		throws NullPointerException
	{
		// Check
		if (__vfs == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.vfs = __vfs;
	}
	
	/**
	 * Mounts the given file source to the given directory.
	 *
	 * @param __np The native path to mount to.
	 * @param __src The file source where data is to be obtained from.
	 * @throws IOException If it could not be mounted.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/05
	 */
	public final void mount(NativePath __np, VirtualFileSource __src)
		throws IOException, NullPointerException
	{
		// Check
		if (__np == null || __src == null)
			throw new NullPointerException("NARG");
		
		// Normalize the path
		__np = __np.normalize();
		
		// {@squirreljme.error CA01 Mount points must be absolute paths.
		// (The mount point)}
		if (!__np.isAbsolute())
			throw new IOException(String.format("CA01 %s", __np));
		
		throw new Error("TODO");
	}
}

