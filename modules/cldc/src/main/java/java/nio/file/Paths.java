// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.nio.file;

import cc.squirreljme.runtime.cldc.annotation.Api;

/**
 * This class provides a single static method which is used to create instances
 * of {@link Path} which represent native filesystem paths.
 *
 * @since 2017/06/12
 */
@Api
public final class Paths
{
	/**
	 * Not used.
	 *
	 * @since 2017/06/12
	 */
	private Paths()
	{
	}
	
	/**
	 * This creates a representation of a native filesystem path. It has the
	 * same function and rules
	 * as {@link FileSystem#getPath(String, String...)}.
	 *
	 * @param __a The first path fragment.
	 * @param __b The optional remaining path fragments.
	 * @return The native representation of the native filesystem path.
	 * @throws InvalidPathException If the specified path cannot be represented
	 * on the native system.
	 * @since 2017/06/12
	 */
	@Api
	public static Path get(String __a, String... __b)
		throws InvalidPathException
	{
		return FileSystems.getDefault().getPath(__a, __b);
	}
}

