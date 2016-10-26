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

import net.multiphasicapps.squirreljme.paths.NativePath;

/**
 * This represents a single mount within a mounted file system tree.
 *
 * @since 2016/09/05
 */
final class __Mount__
	implements Comparable<__Mount__>
{
	/** The owning mount system. */
	protected final VirtualMounts mounts;
	
	/** The path of this mount point. */
	protected final NativePath path;
	
	/** The file source. */
	protected final VirtualFileSource source;
	
	/**
	 * Initializes the virtual mount point.
	 *
	 * @param __m The owning mount manager.
	 * @param __p The native path where the mount point exists.
	 * @param __st The file store to base files from.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/05
	 */
	__Mount__(VirtualMounts __m, NativePath __p, VirtualFileSource __st)
		throws NullPointerException
	{
		// Check
		if (__m == null || __p == null || __st == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.mounts = __m;
		this.path = __p;
		this.source = __st;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/05
	 */
	@Override
	public int compareTo(__Mount__ __o)
	{
		// Check
		if (__o == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error CA02 Cannot compare two mount points owned by
		// two differing mount managers.}
		if (__o.mounts != this.mounts)
			throw new IllegalArgumentException("CA02");
		
		// Get this and the other path
		NativePath mp = this.path, op = __o.path;
		
		// Compare roots first, for example on DOS there may be A: and B:
		// It must compare in a way where A is before B accordingly.
		NativePath mr = mp.getRoot(), or = op.getRoot();
		int rv;
		if (0 != (rv = mr.compareTo(or)))
			return rv;
		
		// Compare the name count next, make it so paths with higher components
		// appear first. This is because getting of a file from a mount point
		// will linearly traverse and there may be two mount points such as
		// {@code /foo} and {@code /foo/bar}. If the one with less names
		// appears first then the source for {@code /foo/bar} will never be
		// accessed because it will find a match for {@code /foo}.
		int mc = mp.getNameCount(), oc = op.getNameCount();
		if (mc > oc)
			return -1;
		else if (mc < oc)
			return 1;
		
		// Finally compare the actual paths themselves
		return mp.compareTo(op);
	}
}

