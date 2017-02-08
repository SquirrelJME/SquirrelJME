// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.lang;

public class Runtime
{
	private Runtime()
	{
		super();
		throw new Error("TODO");
	}
	
	/**
	 * Indicates that the application exits with the given code.
	 *
	 * @param __e The exit code.
	 * @since 2017/02/08
	 */
	public void exit(int __a)
	{
		SquirrelJME.exit(__a);
	}
	
	public long freeMemory()
	{
		throw new Error("TODO");
	}
	
	/**
	 * Indicates that the application should have garbage collection be
	 * performed. It is unspecified when garbage collection occurs.
	 *
	 * @since 2017/02/08
	 */
	public void gc()
	{
		SquirrelJME.gc();
	}
	
	public long maxMemory()
	{
		throw new Error("TODO");
	}
	
	public long totalMemory()
	{
		throw new Error("TODO");
	}
	
	public static Runtime getRuntime()
	{
		throw new Error("TODO");
	}
}

