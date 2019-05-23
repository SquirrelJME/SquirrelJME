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

import cc.squirreljme.runtime.cldc.vki.Assembly;
import cc.squirreljme.runtime.cldc.vki.SystemCallError;
import cc.squirreljme.runtime.cldc.vki.SystemCallIndex;

/**
 * This class contains information about the host memory environment along
 * with providing methods to perform garbage collection and exit the
 * virtual machine.
 *
 * @since 2018/10/14
 */
public class Runtime
{
	/** There is only a single instance of the run-time. */
	private static final Runtime _INSTANCE =
		new Runtime();
	
	/**
	 * Not used.
	 *
	 * @since 2018/10/14
	 */
	private Runtime()
	{
	}
	
	/**
	 * Indicates that the application exits with the given code.
	 *
	 * @param __v The exit code, the value of this code may change according
	 * to the host operating system and the resulting process might not exit
	 * with the given code.
	 * @throws SecurityException If exiting is not permitted.
	 * @since 2017/02/08
	 */
	public void exit(int __v)
		throws SecurityException
	{
		// Check that we can exit
		System.getSecurityManager().checkExit(__v);
		
		// Then do the exit if no exception was thrown
		Assembly.sysCall(SystemCallIndex.EXIT, __v);
	}
	
	/**
	 * Returns the amount of memory that is free for the JVM to use.
	 *
	 * @return The amount of free memory.
	 * @since 2018/10/14
	 */
	public long freeMemory()
	{
		int rv = Assembly.sysCallV(SystemCallIndex.VMI_MEM_FREE);
		if (0 != SystemCallError.getError(SystemCallIndex.VMI_MEM_FREE))
			return Integer.MAX_VALUE;
		return rv;
	}
	
	/**
	 * Indicates that the application should have garbage collection be
	 * performed. It is unspecified when garbage collection occurs.
	 *
	 * @since 2017/02/08
	 */
	public void gc()
	{
		Assembly.sysCall(SystemCallIndex.GARBAGE_COLLECT);
	}
	
	/**
	 * Returns the maximum amount of memory that the virtual machine will
	 * attempt to use, if there is no limit then {@link Long#MAX_VALUE} will
	 * be used.
	 *
	 * @return The maximum amount of memory available to the virtual machine.
	 * @since 2018/10/14
	 */
	public long maxMemory()
	{
		int rv = Assembly.sysCallV(SystemCallIndex.VMI_MEM_MAX);
		if (0 != SystemCallError.getError(SystemCallIndex.VMI_MEM_MAX))
			return Integer.MAX_VALUE;
		return rv;
	}
	
	/**
	 * Returns the total amount of memory that is being used by the virtual
	 * machine. This is a count of all the memory claimed by the virtual
	 * machine itself for its memory pools and such.
	 *
	 * @return The amount of memory being used by the virtual machine.
	 * @since 2018/10/14
	 */
	public long totalMemory()
	{
		int rv = Assembly.sysCallV(SystemCallIndex.VMI_MEM_USED);
		if (0 != SystemCallError.getError(SystemCallIndex.VMI_MEM_USED))
			return 0;
		return rv;
	}
	
	/**
	 * Returns the single instance of this class.
	 *
	 * Only a single runtime is valid and there will only be one.
	 *
	 * @since 2018/03/01
	 */
	public static Runtime getRuntime()
	{
		return Runtime._INSTANCE;
	}
}

