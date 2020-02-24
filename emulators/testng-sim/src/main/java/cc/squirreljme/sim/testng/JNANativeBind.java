package cc.squirreljme.sim.testng;

import cc.squirreljme.jvm.Assembly;
import com.sun.jna.*;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Binds native methods.
 *
 * @since 2020/02/23
 */
public class JNANativeBind
{
	/** Has this been bound yet? */
	private static volatile boolean _BOUND;
	
	/**
	 * Forces binding on initialization.
	 *
	 * @since 2020/02/23
	 */
	static
	{
		bind();
	}
	
	/**
	 * Binds native methods.
	 *
	 * @since 2020/02/23
	 */
	public static void bind()
	{
		if (_BOUND)
			return;
		
		// Make sure objects can be used because lots of the helper methods
		// use these!
		Map<String, Object> options = new HashMap<>();
		options.put(Library.OPTION_ALLOW_OBJECTS,
			true);
		
		// Load our library
		NativeLibrary lib = NativeLibrary.getInstance(
			"/testng-sim-native.nl",
			options);
		
		// Register native assembly support
		Native.register(Assembly.class, lib);
		
		// Now is bound
		_BOUND = true;
	}
	
	/**
	 * Proxy handler.
	 *
	 * @param __proxy The proxy to access.
	 * @param __target The target method.
	 * @param __args The arguments to call.
	 * @since 2020/02/23
	 */
	private static Object __proxy(Object __proxy, Method __target,
		Object... __args)
	{
		switch (__target.getName())
		{
			default:
				throw new IllegalArgumentException(
					String.format("No proxy defined for %s.", __target));
		}
	}
	
	/**
	 * Maps functions.
	 *
	 * @since 2020/02/23
	 */
	private static final class __FunctionMapper__
		implements FunctionMapper
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/02/23
		 */
		@Override
		public String getFunctionName(NativeLibrary library, Method method)
		{
			// Map to some function which hopefully will never be called
			return "putchar";
		}
	}
	
	/**
	 * Invocation mapper.
	 *
	 * @since 2020/02/23
	 */
	private static final class __InvocationMapper__
		implements InvocationMapper
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/02/23
		 */
		@Override
		public InvocationHandler getInvocationHandler(
			NativeLibrary __lib, Method __method)
		{
			System.err.printf(">>> Mapped %s -> %s%n", __lib.getName(),
				__method);
			
			return JNANativeBind::__proxy;
		}
	}
}
