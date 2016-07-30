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
 * This class is used to create instances of POSIX compatible pathnames.
 *
 * @since 2016/07/30
 */
public class PosixPaths
	implements NativePaths
{
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
	protected NativePath get(String __f, String... __r)
		throws InvalidNativePathException
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/30
	 */
	@Override
	protected String pathSeparator()
	{
		return ":";
	}
}

