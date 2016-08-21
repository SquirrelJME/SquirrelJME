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

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
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
	
	/** The current directory. */
	public static final PosixPath CURRENT_DIR =
		new PosixPath(false, ".");
	
	/** The parent directory. */
	public static final PosixPath PARENT_DIR =
		new PosixPath(false, "..");
	
	/** The empty path. */
	public static final PosixPath EMPTY =
		new PosixPath(false, "");
	
	/** Is this a root? */
	protected final boolean isroot;
	
	/** The single path fragment, will be null if a compound path. */
	protected final String fragment;
	
	/** String representation of this path. */
	private volatile Reference<String> _string;
	
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
	 * Initializes a path which contains no directory components.
	 *
	 * @param __s The string representing the single fragment.
	 * @throws InvalidNativePathException If the fragment contains a NUL
	 * character or a forward slash.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/08/21
	 */
	PosixPath(String __s)
		throws InvalidNativePathException, NullPointerException
	{
		// Check
		if (__s == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error BN02 A single path fragment cannot contain the
		// NUL character or a forward slash.}
		if (__s.indexOf(0) >= 0 || __s.indexOf('/') >= 0)
			throw new InvalidNativePathException("BN02");
		
		// Not root
		this.isroot = false;
		this.fragment = __s;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/30
	 */
	@Override
	public final int compareTo(NativePath __o)
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
	public final boolean equals(Object __o)
	{
		// Must be another POSIX path
		if (!(__o instanceof PosixPath))
			return false;
		
		// Just forward to equals
		return compareTo((PosixPath)__o) == 0;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/08/21
	 */
	@Override
	public final int hashCode()
	{
		return toString().hashCode();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/08/21
	 */
	@Override
	public final boolean isAbsolutePath()
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/08/21
	 */
	@Override
	public final NativePath resolve(NativePath __o)
		throws InvalidNativePathException
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/08/21
	 */
	@Override
	public final String toString()
	{
		// Get
		Reference<String> ref = this._string;
		String rv;
		
		// Need to cache the string?
		if (ref == null || null == (rv = ref.get()))
		{
			throw new Error("TODO");
		}
		
		// Return
		return rv;
	}
}

