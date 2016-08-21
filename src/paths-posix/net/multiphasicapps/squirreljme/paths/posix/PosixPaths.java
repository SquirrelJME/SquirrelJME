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
 * This class is used to create instances of POSIX compatible pathnames.
 *
 * @since 2016/07/30
 */
public class PosixPaths
	implements NativePaths
{
	/** The single instance (since only one is needed in most cases). */
	private static volatile Reference<PosixPaths> _INSTANCE;
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/30
	 */
	@Override
	public String fileSeparator()
	{
		return "/";
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/30
	 */
	@Override
	public PosixPath get(String __f, String... __r)
		throws InvalidNativePathException, NullPointerException
	{
		// Check
		if (__f == null)
			throw new NullPointerException("NARG");
		
		// Combine all strings into a single unit
		StringBuilder sb = new StringBuilder(__f);
		if (__r != null)
			for (String s : __r)
				if (s == null)
					throw new NullPointerException("NARG");
				else
				{
					sb.append('/');
					sb.append(s);
				}
		String s = sb.toString();
		sb = null;
		
		// If the path contains no slashes then it is just a standard single
		// file name
		int fs = s.indexOf('/');
		if (fs < 0)
			return new PosixPath(s);
		
		// Special double root?
		else if (s.equals("//"))
			return PosixPath.SPECIAL_ROOT;
		
		
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/30
	 */
	@Override
	public String pathSeparator()
	{
		return ":";
	}
	
	/**
	 * Returns the single instance of this path manager.
	 *
	 * @return The path manager.
	 * @since 2016/08/21
	 */
	public static PosixPaths instance()
	{
		Reference<PosixPaths> ref = _INSTANCE;
		PosixPaths rv;
		
		// Create?
		if (ref == null || null == (rv = ref.get()))
			_INSTANCE = new WeakReference<>((rv = new PosixPaths()));
		
		// Return
		return rv;
	}
}

