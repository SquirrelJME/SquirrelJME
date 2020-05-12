// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.UnsupportedEncodingException;

/**
 * This class contains helpers for all of the system calls to more reliably
 * have a type-safe and easier to use interface to them.
 *
 * @see SystemCallIndex
 * @since 2020/03/27
 */
public final class SystemCall
{
	/**
	 * Not used.
	 *
	 * @since 2020/03/27
	 */
	private SystemCall()
	{
	}
	
	/**
	 * Returns the current task ID.
	 *
	 * @return The current task ID.
	 * @since 2020/05/03
	 */
	public static int currentTaskId()
	{
		// Use pure call because this could potentially infinite loop here
		return Assembly.sysCallPV(SystemCallIndex.FRAME_TASK_ID_GET);
	}
	
	/**
	 * Exits the virtual machine.
	 *
	 * @param __i The exit code.
	 * @since 2020/04/09
	 */
	public static void exit(int __i)
	{
		Assembly.sysCall(SystemCallIndex.EXIT, __i);
	}
	
	/**
	 * Gets the error for the system call.
	 *
	 * @param __si The system call to check the error for.
	 * @return The error for the system call.
	 * @since 2020/03/27
	 */
	public static int getError(short __si)
	{
		return Assembly.sysCallPV(SystemCallIndex.ERROR_GET, __si);
	}
	
	/**
	 * Has an error occurred from this system call.
	 *
	 * @param __si The system call to check.
	 * @return If this system call has an error.
	 * @since 2020/05/01
	 */
	public static boolean hasError(short __si)
	{
		return SystemCall.getError(__si) != SystemCallError.NO_ERROR;
	}
	
	/**
	 * Does the error for the system call match this specified error?
	 *
	 * @param __si The system call to check the error for.
	 * @param __error The error to match against.
	 * @return If the error matches.
	 * @since 2020/03/27
	 */
	public static boolean isError(short __si, int __error)
	{
		return SystemCall.getError(__si) == __error;
	}
	
	/**
	 * Is this system call supported?
	 *
	 * @param __si The system call to check.
	 * @return If the system call is supported.
	 * @since 2020/03/27
	 */
	public static boolean isSupported(short __si)
	{
		return Assembly.sysCallPV(SystemCallIndex.QUERY_INDEX, __si) != 0;
	}
	
	/**
	 * Loads the specified class for the current context.
	 *
	 * @param __name The name of the class.
	 * @return The class information.
	 * @since 2020/03/27
	 */
	public static ClassInfo loadClass(byte... __name)
		throws ClassNotFoundException
	{
		long nameP = Assembly.objectToPointer(__name);
		
		// The VM itself may be able to perform class loading, so first we
		// try to ask the VM if it is capable of doing so and to initialize
		// the class if so
		if (SystemCall.isSupported(SystemCallIndex.LOAD_CLASS_BYTES))
		{
			long rvP = Assembly.sysCallPVL(SystemCallIndex.LOAD_CLASS_BYTES,
				Assembly.longUnpackHigh(nameP), Assembly.longUnpackLow(nameP));
			
			// Failed to load the class
			if (rvP == 0)
			{
				// {@squirreljme.error ZZ3V No such class exists.}
				if (SystemCall.isError(SystemCallIndex.LOAD_CLASS_BYTES,
					SystemCallError.NO_SUCH_CLASS))
					throw new ClassNotFoundException("ZZ3V");
				
				// {@squirreljme.error ZZ3W Invalid class.}
				throw new NoClassDefFoundError("ZZ3W");
			}
			
			// Use this created info
			return Assembly.pointerToClassInfo(rvP);
		}
		
		// Loading of classes is our responsibility
		throw Debugging.todo();
	}
	
	/**
	 * Searches the ROM for the given JAR.
	 *
	 * @param __name The name of the ROM to search for.
	 * @return The ROM reference or {@code null} if no ROM was found.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/05/12
	 */
	public static RomReference searchJar(String __name)
		throws NullPointerException
	{
		if (__name == null)
			throw new NullPointerException("NARG");
		
		// The system call expects only UTF-8, so we need to decode it as that
		try
		{
			return SystemCall.searchJar(__name.getBytes("utf-8"));
		}
		catch (UnsupportedEncodingException e)
		{
			// {@squirreljme.error ZZ4A Could not decode JAR name.}
			throw new RuntimeException("ZZ4A", e);
		}
	}
	
	/**
	 * Searches the ROM for the given JAR.
	 *
	 * @param __name The name of the ROM to search for.
	 * @return The ROM reference or {@code null} if no ROM was found.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/05/12
	 */
	public static RomReference searchJar(byte... __name)
		throws NullPointerException
	{
		if (__name == null)
			throw new NullPointerException("NARG");
		
		// If this system call is not supported, then just stop here
		if (SystemCall.isSupported(SystemCallIndex.ROM_ACCESS))
			return null;
		
		// Lookup search
		long nameP = Assembly.objectToPointer(__name);
		long id = Assembly.sysCallVL(SystemCallIndex.ROM_ACCESS,
			RomAccessControl.CONTROL_SEARCH_BY_JAR_BYTES,
			Assembly.longUnpackHigh(nameP), Assembly.longUnpackLow(nameP));
		
		// {@squirreljme.error ZZ4B Could not check for ROM access.
		if (SystemCall.hasError(SystemCallIndex.ROM_ACCESS))
		{
			if (!SystemCall.isError(SystemCallIndex.ROM_ACCESS,
				SystemCallError.UNSUPPORTED_SYSTEM_CALL))
				throw new RuntimeException("ZZ4B " +
					SystemCall.getError(SystemCallIndex.ROM_ACCESS));
			else
				return null;
		}
		
		// Make this type safe rather than having to use integers
		if (id == 0)
			return null;
		return new RomReference(id);
	}
}
