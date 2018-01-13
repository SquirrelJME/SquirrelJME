// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.lang;

import cc.squirreljme.runtime.cldc.SystemCall;

public class Runtime
{
	/** THere is only a single instance of the run-time. */
	private static final Runtime _INSTANCE =
		new Runtime();
	
	private Runtime()
	{
	}
	
	/**
	 * Indicates that the application exits with the given code.
	 *
	 * @param __v The exit code, the value of this code may change according
	 * to the host operating system and the resulting process might not exit
	 * with the given code.
	 * @since 2017/02/08
	 */
	public void exit(int __v)
	{
		SystemCall.exit(__v);
	}
	
	public long freeMemory()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Indicates that the application should have garbage collection be
	 * performed. It is unspecified when garbage collection occurs.
	 *
	 * @since 2017/02/08
	 */
	public void gc()
	{
		SystemCall.gc();
	}
	
	public long maxMemory()
	{
		throw new todo.TODO();
	}
	
	public long totalMemory()
	{
		throw new todo.TODO();
	}
	
	public static Runtime getRuntime()
	{
		throw new todo.TODO();
	}
}

