// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.paths.posix;

import net.multiphasicapps.squirreljme.paths.InvalidNativePathException;
import net.multiphasicapps.squirreljme.paths.NativePath;
import net.multiphasicapps.squirreljme.paths.NativePaths;

/**
 * This represents a native POSIX path element or elements.
 *
 * @since 2016/07/30
 */
public class PosixPath
	implements NativePath
{
	/** The root filesystem. */
	public static final PosixPath ROOT =
		new PosixPath(true, "/");
	
	/** The operating system specific root (as defined by POSIX). */
	public static final PosixPath SPECIAL_ROOT =
		new PosixPath(true, "//");
	
	/** Is this a root? */
	protected final boolean isroot;
	
	/** The single path fragment, will be null if a compound path. */
	protected final String fragment;
	
	/**
	 * Initializes a path that is special and may be a root.
	 *
	 * @param __isroot Is this path a root path?
	 * @param __s The string representation of the path.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/30
	 */
	private PosixPath(boolean __isroot, String __s)
		throws NullPointerException
	{
		// Check
		if (__s == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.isroot = __isroot;
		this.fragment = __s;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/30
	 */
	@Override
	public int compareTo(NativePath __o)
	{
		// {@squirreljme.error BN01 The other path is not a POSIX path. (This
		// path; the other path)}
		if (!(__o instanceof PosixPath))
			throw new InvalidNativePathException(String.format("BN01 %s %s",
				this, __o));
		
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/30
	 */
	@Override
	public boolean equals(Object __o)
	{
		// Must be another POSIX path
		if (!(__o instanceof PosixPath))
			return false;
		
		throw new Error("TODO");
	}
}

