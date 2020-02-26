package cc.squirreljme.emulator;

import com.sun.jna.FunctionMapper;
import com.sun.jna.NativeLibrary;

import java.lang.reflect.Method;

/**
 * Maps all of the various JNI functions.
 *
 * @since 2020/02/25
 */
public final class JNIFunctionMapper
	implements FunctionMapper
{
	/**
	 * {@inheritDoc}
	 * @since 2020/02/25
	 */
	@Override
	public String getFunctionName(NativeLibrary library, Method method)
	{
		return "Java_cc_squirreljme_jvm_Assembly_classInfoOfByte";
	}
}
