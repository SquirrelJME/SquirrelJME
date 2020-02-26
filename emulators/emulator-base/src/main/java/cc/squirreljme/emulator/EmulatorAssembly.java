package cc.squirreljme.emulator;

import cc.squirreljme.jvm.Assembly;
import cc.squirreljme.jvm.SystemCallError;
import cc.squirreljme.jvm.SystemCallIndex;

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
		// Debug
		System.err.printf("System call: %d(%d, %d, %d, %d, %d, %d, %d, %d)%n",
			__si, __a, __b, __c, __d, __e, __f, __g, __h);
	
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
						case SystemCallIndex.ERROR_GET:
						case SystemCallIndex.ERROR_SET:
						case SystemCallIndex.QUERY_INDEX:
						case SystemCallIndex.TIME_MILLI_WALL:
						case SystemCallIndex.TIME_NANO_MONO:
							return 1;
						
							// Not-implemented
						default:
							return 0;
					}
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
		EmulatorThreadContext rv = _CONTEXT.get();
		if (rv != null)
			return rv;
		
		// Does not exist, needs to be created
		synchronized (EmulatorAssembly.class)
		{
			// Check again
			rv = _CONTEXT.get();
			if (rv != null)
				return rv;
			
			_CONTEXT.set((rv = new EmulatorThreadContext()));
			return rv;
		}
	}
}
