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
public final class PosixPath
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
	
	/** The root component. */
	protected final PosixPath root;
	
	/** Components (will be null if single). */
	private final PosixPath[] _comps;
	
	/** String representation of this path. */
	private volatile Reference<String> _string;
	
	/** Normalized path. */
	private volatile Reference<PosixPath> _normalized;
	
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
		this.root = (__isroot ? this : null);
		this._comps = null;
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
		this.root = null;
		this._comps = null;
	}
	
	/**
	 * Initializes a path which contains multiple components and an optional
	 * root component.
	 *
	 * @param __root The root component, which is optional.
	 * @param __comps The components that make up the path.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/08/21
	 */
	PosixPath(PosixPath __root, PosixPath... __comps)
		throws NullPointerException
	{
		// Check
		if (__comps == null)
			throw new NullPointerException("NARG");
		
		// Not a root or a fragment
		this.isroot = false;
		this.fragment = null;
		
		// Set optional root
		this.root = __root;
		
		// Set components
		this._comps = __comps;
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
	public final PosixPath getRoot()
	{
		return this.root;
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
	public final boolean isAbsolute()
	{
		// It is absolute, but only if there is a root
		return this.root != null;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/08/21
	 */
	@Override
	public PosixPath normalize()
	{
		// Not a componentized path, use self
		if (this._comps == null)
			return this;
		
		// Get
		Reference<PosixPath> ref = this._normalized;
		PosixPath rv;
		
		// Cache?
		if (ref == null || null == (rv = ref.get()))
			throw new Error("TODO");
		
		// Return
		return rv;
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
			// Target string
			StringBuilder sb = new StringBuilder();
			
			// Contains just a single component, the fragment
			String fragment = this.fragment;
			if (fragment != null)
				sb.append(fragment);
			
			// Compound path
			else
			{
				// Append the root component, if there is one
				PosixPath root = this.root;
				if (root != null)
					sb.append(root.toString());
				
				// Add any components
				boolean sl = false;
				for (PosixPath c : this._comps)
				{
					// Append directory separator after the first
					if (sl)
						sb.append('/');
					sl = true;
					
					// Append component data
					sb.append(c.toString());
				}
			}
			
			// Finish
			this._string = new WeakReference<>((rv = sb.toString()));
		}
		
		// Return
		return rv;
	}
}

