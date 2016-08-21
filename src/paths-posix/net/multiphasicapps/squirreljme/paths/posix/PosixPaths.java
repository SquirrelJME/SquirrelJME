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
		
		// Empty?
		if (s.equals(""))
			return PosixPath.EMPTY;
		
		// Count the number of roots at the start
		int n = s.length(), numroots = 0, rootend = 0;
		for (int i = 0; i < n; i++)
		{
			char c = s.charAt(i);
			
			// Is there a slash?
			if (c == '/')
			{
				rootend++;
				numroots++;
			}
			
			// Stop on any other character
			else
				break;
		}
		
		// Determine the root to use (if any)
		PosixPath useroot = (numroots == 0 ? null :
			(numroots == 2 ? PosixPath.SPECIAL_ROOT : PosixPath.ROOT));
		
		// If the path only contains a root component then use that
		if (rootend >= n)
			return useroot;
		
		// Build up sub-fragments from inner paths
		for (int i = rootend; i < n; i++)
			throw new Error("TODO");
		
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

