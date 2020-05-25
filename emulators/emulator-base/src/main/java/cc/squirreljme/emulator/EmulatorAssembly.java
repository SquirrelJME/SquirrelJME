// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.emulator;

import cc.squirreljme.emulator.fb.NativeFramebuffer;
import cc.squirreljme.jvm.SystemCallError;
import cc.squirreljme.jvm.SystemCallIndex;
import cc.squirreljme.jvm.config.ConfigRomKey;
import cc.squirreljme.jvm.config.ConfigRomType;
import cc.squirreljme.jvm.config.LineEndingType;
import java.io.IOException;
import java.io.OutputStream;

/**
 * This contains the implementation of some system calls in the event that the
 * JNI emulation layer does not have a C-based implementation of a method.
 *
 * @since 2020/02/26
 */
public final class EmulatorAssembly
{
	/** The thread contexts, storing thread specific information. */
	private static final ThreadLocal<EmulatorThreadContext> _CONTEXT =
		new ThreadLocal<>();
	
	/**
	 * Not used.
	 *
	 * @since 2020/02/26
	 */
	private EmulatorAssembly()
	{
	}
	
	/**
	 * Handles system calls in Java.
	 *
	 * @param __si System call index.
	 * @param __a Argument.
	 * @param __b Argument.
	 * @param __c Argument.
	 * @param __d Argument.
	 * @param __e Argument.
	 * @param __f Argument.
	 * @param __g Argument.
	 * @param __h Argument.
	 * @return The result.
	 * @since 2020/02/26
	 */
	public static long systemCall(short __si, int __a, int __b,
		int __c, int __d, int __e, int __f, int __g, int __h)
	{
		// We need the context for thread based info
		EmulatorThreadContext context = EmulatorAssembly.threadContext();
		
		// Depends on the system call
		switch (__si)
		{
				// System calls that are supported?
			case SystemCallIndex.QUERY_INDEX:
				{
					// Always succeeds
					context.setError(__si, 0);
					
					// Depends on the system call requested
					switch (__a)
					{
							// Implemented here
						case SystemCallIndex.CONFIG_GET_TYPE:
						case SystemCallIndex.CONFIG_GET_VALUE:
						case SystemCallIndex.ERROR_GET:
						case SystemCallIndex.ERROR_SET:
						case SystemCallIndex.FRAMEBUFFER:
						case SystemCallIndex.QUERY_INDEX:
						case SystemCallIndex.PD_OF_STDERR:
						case SystemCallIndex.PD_OF_STDOUT:
						case SystemCallIndex.PD_WRITE_BYTE:
						case SystemCallIndex.TIME_MILLI_WALL:
						case SystemCallIndex.TIME_NANO_MONO:
							return 1;
						
							// Not-implemented
						default:
							return 0;
					}
				}
				
				// Get configuration type
			case SystemCallIndex.CONFIG_GET_TYPE:
				switch (__a)
				{
					case ConfigRomKey.LINE_ENDING:
						return ConfigRomType.INTEGER;
					
					default:
						context.setError(__si,
							SystemCallError.NO_SUCH_CONFIG_KEY);
						return 0;
				}
			
				// Get raw value
			case SystemCallIndex.CONFIG_GET_VALUE:
				switch (__a)
				{
					case ConfigRomKey.LINE_ENDING:
						switch (System.getProperty("line.ending"))
						{
							case "\r":		return LineEndingType.CR;
							case "\r\n":	return LineEndingType.CRLF;
							case "\n":		return LineEndingType.LF;
							default:		return LineEndingType.UNSPECIFIED;
						}
						
					default:
						context.setError(__si,
							SystemCallError.NO_SUCH_CONFIG_KEY);
						return 0;
				}
			
				// Get error
			case SystemCallIndex.ERROR_GET:
				{
					context.setError(__si, 0);
					
					return context.getError((short)__a);
				}
			
				// Set error
			case SystemCallIndex.ERROR_SET:
				{
					context.setError(__si, 0);
					
					int oldError = context.getError((short)__a);
					context.setError((short)__a, __b);
					return oldError;
				}
			
				// Access the framebuffer
			case SystemCallIndex.FRAMEBUFFER:
				return NativeFramebuffer.getInstance().systemCall(context,
					__a, __b, __c, __d, __e, __f, __g, __h);
				
				// Descriptor of standard error
			case SystemCallIndex.PD_OF_STDERR:
				context.setError(__si, 0);
				return 2;
				
				// Descriptor of standard output
			case SystemCallIndex.PD_OF_STDOUT:
				context.setError(__si, 0);
				return 1;
				
				// Write byte
			case SystemCallIndex.PD_WRITE_BYTE:
				{
					OutputStream pipe = (__a == 2 ? System.err :
						(__a == 1 ? System.out : null));
					
					if (pipe != null)
					{
						try
						{
							pipe.write(__b);
							context.setError(__si, 0);
							
							return 1;
						}
						catch (IOException e)
						{
							context.setError(__si,
								SystemCallError.PIPE_DESCRIPTOR_BAD_WRITE);
							
							return 0;
						}
					}
					else
					{
						context.setError(__si,
							SystemCallError.PIPE_DESCRIPTOR_INVALID);
						return 0;
					}
				}
				
				// Current wall clock
			case SystemCallIndex.TIME_MILLI_WALL:
				{
					context.setError(__si, 0);
					
					return System.currentTimeMillis();
				}
			
				// Current monotonic clock
			case SystemCallIndex.TIME_NANO_MONO:
				{
					context.setError(__si, 0);
					
					return System.nanoTime();
				}
			
				// Un-handled, set as not supported and return a default value
			default:
				// Debug
				System.err.printf(
					"SysCall?: %d(%d, %d, %d, %d, %d, %d, %d, %d)%n",
					__si, __a, __b, __c, __d, __e, __f, __g, __h);
				
				// Set error
				context.setError(__si,
					SystemCallError.UNSUPPORTED_SYSTEM_CALL);
				return 0;
		}
	}
	
	/**
	 * Returns the current thread context.
	 *
	 * @return The thread context.
	 * @since 2020/02/26
	 */
	public static EmulatorThreadContext threadContext()
	{
		// Has this been created already?
		EmulatorThreadContext rv = EmulatorAssembly._CONTEXT.get();
		if (rv != null)
			return rv;
		
		// Does not exist, needs to be created
		synchronized (EmulatorAssembly.class)
		{
			// Check again
			rv = EmulatorAssembly._CONTEXT.get();
			if (rv != null)
				return rv;
			
			EmulatorAssembly._CONTEXT.set((rv = new EmulatorThreadContext()));
			return rv;
		}
	}
}
