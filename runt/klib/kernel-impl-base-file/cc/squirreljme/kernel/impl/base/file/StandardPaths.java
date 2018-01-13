// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.kernel.impl.base.file;

import java.nio.file.Path;
import java.nio.file.Paths;
import cc.squirreljme.runtime.cldc.OperatingSystemType;
import cc.squirreljme.runtime.cldc.SystemCall;

/**
 * This class contains the standard set of paths which is where SquirrelJME's
 * data is stored.
 *
 * @since 2018/01/13
 */
public final class StandardPaths
{
	/** Determines the default paths to use. */
	public static final StandardPaths DEFAULT =
		__defaultPaths();
	
	/** The configuration path. */
	protected final Path config;
	
	/** The data path. */
	protected final Path data;
	
	/** The cache path. */
	protected final Path cache;
	
	/**
	 * Initializes a standard path using the specified path as a base for
	 * the required directories.
	 *
	 * @param __p The base path.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/01/13
	 */
	public StandardPaths(Path __p)
		throws NullPointerException
	{
		this(__p.resolve("config"), __p.resolve("data"), __p.resolve("cache"));
	}
	
	/**
	 * Initializes the standard path set.
	 *
	 * @param __conf The configuration directory.
	 * @param __data The data directory.
	 * @param __cache The cache directory.
	 * @throws NullPointerException On null arguments.
	 * @sicne 2018/01/13
	 */
	public StandardPaths(Path __conf, Path __data, Path __cache)
		throws NullPointerException
	{
		if (__conf == null || __data == null || __cache == null)
			throw new NullPointerException("NARG");
		
		this.config = __conf;
		this.data = __data;
		this.cache = __cache;
	}
	
	/**
	 * Determines how to get the default paths.
	 *
	 * @return The set of standard paths.
	 * @since 2018/01/13
	 */
	private static StandardPaths __defaultPaths()
	{
		OperatingSystemType ostype = SystemCall.operatingSystemType();
		
		Path userhome = StandardPaths.__getPropertyPath("user.home");
		Path userdir = StandardPaths.__getPropertyPath("user.dir");
		
		// Unix systems
		if (ostype.isUnix())
		{
			throw new todo.TODO();
		}
		
		// Unknown
		else
		{
			throw new todo.TODO();
		}
	}
	
	/**
	 * Returns the path of a system property.
	 *
	 * @param __s The system property to get.
	 * @return The path of the property or {@code null} if it is not set.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/01/13
	 */
	private static Path __getPropertyPath(String __s)
		throws NullPointerException
	{
		if (__s == null)
			throw new NullPointerException("NARG");
		
		String v = System.getProperty(__s);
		if (v != null)
			return Paths.get(v);
		return null;
	}
}

