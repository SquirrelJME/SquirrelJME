package cc.squirreljme.emulator;

import cc.squirreljme.jvm.Assembly;
import com.sun.jna.*;
import com.sun.jna.win32.StdCallFunctionMapper;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Collections;
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
		options.put(Library.OPTION_FUNCTION_MAPPER,
			new JNIFunctionMapper());
		
		// Search in the nowhere and root paths so we have a better chance of
		// finding our library
		NativeLibrary.addSearchPath("emulator-base", "");
		NativeLibrary.addSearchPath("emulator-base", "/");
		
		// Load our library, note that we need to prefix with forward slash
		// so JNA treats it as an absolute path (takes just the name) but we
		// also need to get all the associated extensions and prefix as
		// defined in the system. JNA uses Java's own library mapping anyway
		// to locate libraries.
		NativeLibrary lib = NativeLibrary.getInstance(
			"/" + System.mapLibraryName("emulator-base"),
			options);
		
		// Register native assembly support
		Native.register(Assembly.class, lib);
		
		// Now is bound
		_BOUND = true;
	}
}
