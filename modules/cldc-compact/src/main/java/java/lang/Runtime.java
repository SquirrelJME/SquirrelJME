// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.lang;

import cc.squirreljme.jvm.mle.DebugShelf;
import cc.squirreljme.jvm.mle.RuntimeShelf;
import cc.squirreljme.jvm.mle.brackets.TracePointBracket;
import cc.squirreljme.jvm.mle.constants.StandardPipeType;
import cc.squirreljme.jvm.mle.constants.VMStatisticType;
import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.debug.CallTraceUtils;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.cldc.io.ConsoleOutputStream;
import cc.squirreljme.runtime.cldc.io.NonClosedOutputStream;
import java.io.PrintStream;
import org.jetbrains.annotations.Contract;

/**
 * This class contains information about the host memory environment along
 * with providing methods to perform garbage collection and exit the
 * virtual machine.
 *
 * @since 2018/10/14
 */
@Api
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
	@Api
	@Contract("_ -> fail")
	public void exit(int __v)
		throws SecurityException
	{
		// Check that we can exit
		System.getSecurityManager().checkExit(__v);
		
		// Indicate that the VM is exiting
		try
		{
			Debugging.debugNote("Exiting VM with %d...", __v);
			
			// Print the trace where this exit is for debugging
			if (__v != 0)
			{
				TracePointBracket[] trace = DebugShelf.traceStack();
				CallTraceUtils.printStackTrace(new PrintStream(
						new NonClosedOutputStream(
							new ConsoleOutputStream(StandardPipeType.STDERR,
								true))),
					"EXIT", trace,
					null, null, 0);
			}
		}
		catch (Throwable ignored)
		{
		}
		
		// Then do the exit if no exception was thrown
		RuntimeShelf.exit(__v);
	}
	
	/**
	 * Returns the amount of memory that is free for the JVM to use.
	 *
	 * @return The amount of free memory.
	 * @since 2018/10/14
	 */
	@Api
	public long freeMemory()
	{
		return RuntimeShelf.vmStatistic(VMStatisticType.MEM_FREE);
	}
	
	/**
	 * Indicates that the application should have garbage collection be
	 * performed. It is unspecified when garbage collection occurs.
	 *
	 * @since 2017/02/08
	 */
	@Api
	public void gc()
	{
		RuntimeShelf.garbageCollect();
	}
	
	/**
	 * Returns the maximum amount of memory that the virtual machine will
	 * attempt to use, if there is no limit then {@link Long#MAX_VALUE} will
	 * be used.
	 *
	 * @return The maximum amount of memory available to the virtual machine.
	 * @since 2018/10/14
	 */
	@Api
	public long maxMemory()
	{
		return RuntimeShelf.vmStatistic(VMStatisticType.MEM_MAX);
	}
	
	/**
	 * Returns the total amount of memory that is being used by the virtual
	 * machine. This is a count of all the memory claimed by the virtual
	 * machine itself for its memory pools and such.
	 *
	 * @return The amount of memory being used by the virtual machine.
	 * @since 2018/10/14
	 */
	@Api
	public long totalMemory()
	{
		return RuntimeShelf.vmStatistic(VMStatisticType.MEM_USED);
	}
	
	/**
	 * Returns the single instance of this class.
	 *
	 * Only a single runtime is valid and there will only be one.
	 *
	 * @return The current run-time.
	 * @since 2018/03/01
	 */
	@Api
	public static Runtime getRuntime()
	{
		return Runtime._INSTANCE;
	}
}

