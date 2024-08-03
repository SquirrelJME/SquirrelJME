// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat.callbacks;

import cc.squirreljme.jvm.mle.scritchui.ScritchUnifiedInterface;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.vm.springcoat.SpringMachine;
import cc.squirreljme.vm.springcoat.SpringNullObject;
import cc.squirreljme.vm.springcoat.SpringProxyObject;
import cc.squirreljme.vm.springcoat.SpringThreadWorker;
import cc.squirreljme.vm.springcoat.exceptions.SpringMLECallError;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import net.multiphasicapps.classfile.FieldDescriptor;
import net.multiphasicapps.classfile.MethodDescriptor;
import net.multiphasicapps.classfile.MethodNameAndType;

/**
 * Unified ScritchUI Interface proxy.
 *
 * @since 2024/06/15
 */
public class ScritchUnifiedProxy
	extends SpringProxyObject
{
	/** The proxied interface. */
	public final ScritchUnifiedInterface wrapped;
	
	/** The native class. */
	protected final Class<?> nativeClass;
	
	/**
	 * Initializes the unified proxy.
	 *
	 * @param __machine The machine to use.
	 * @param __wrapped The wrapped interface.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/06/15
	 */
	public ScritchUnifiedProxy(SpringMachine __machine,
		ScritchUnifiedInterface __wrapped)
		throws NullPointerException
	{
		super(ScritchUnifiedInterface.class, __machine);
		
		if (__wrapped == null)
			throw new NullPointerException("NARG");
		
		this.wrapped = __wrapped;
		this.nativeClass = ScritchUnifiedInterface.class;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	protected final Object invokeProxy(SpringThreadWorker __thread,
		MethodNameAndType __method, Object[] __args)
	{
		if (__thread == null || __method == null)
			throw new SpringMLECallError("Invalid proxy call");
		
		// Get arguments first
		MethodDescriptor type = __method.type();
		int argCount = type.argumentCount();
		Class<?>[] argTypes = new Class[argCount];
		for (int i = 0; i < argCount; i++)
			argTypes[i] = ScritchUnifiedProxy.__resolve(type.argument(i));
		
		// Try finding the target method
		try
		{
			// Locate method to call
			Method method = ScritchUnifiedInterface.class.getMethod(
				__method.name().toString(), argTypes);
			
			// Normalize arguments
			Object[] normal = new Object[argCount];
			for (int i = 0; i < argCount; i++)
				normal[i] = this.__asNativeObject(__thread, __args[i]);
			
			// Call into it
			return this.__asVMObject(__thread,
				method.invoke(this.wrapped, normal));
		}
		catch (NoSuchMethodException|IllegalAccessException|
			InvocationTargetException __e)
		{
			throw new SpringMLECallError("Invalid method proxy call.",
				__e);
		}
	}
	
	/**
	 * Returns the object as a native object.
	 *
	 * @param __thread The thread.
	 * @param __arg The argument.
	 * @return The native object.
	 * @since 2024/08/02
	 */
	private Object __asNativeObject(SpringThreadWorker __thread, Object __arg)
	{
		if (__arg == null || __arg == SpringNullObject.NULL)
			return null;
		
		throw Debugging.todo();
	}
	
	/**
	 * Returns the native as a wrapped object.
	 *
	 * @param __thread The thread.
	 * @param __arg The argument.
	 * @return The native object.
	 * @since 2024/08/02
	 */
	private Object __asVMObject(SpringThreadWorker __thread, Object __arg)
	{
		if (__arg == null)
			return SpringNullObject.NULL;
		
		throw Debugging.todo();
	}
	
	/**
	 * Resolves the field type. 
	 *
	 * @param __field The field to resolve.
	 * @return The resultant field.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/08/02
	 */
	private static Class<?> __resolve(FieldDescriptor __field)
		throws NullPointerException
	{
		if (__field == null)
			throw new NullPointerException("NARG");
		
		// Array?
		if (__field.isArray())
			return Array.newInstance(
				ScritchUnifiedProxy.__resolve(__field.rootComponentType()),
				__field.dimensions()).getClass();
		
		// Primitive?
		if (__field.isPrimitive())
			switch (__field.primitiveType())
			{
				case BYTE:
					return Byte.TYPE;
					
				case CHARACTER:
					return Character.TYPE;
					
				case DOUBLE:
					return Double.TYPE;
					
				case FLOAT:
					return Float.TYPE;
					
				case INTEGER:
					return Integer.TYPE;
					
				case LONG:
					return Long.TYPE;
					
				case SHORT:
					return Short.TYPE;
					
				case BOOLEAN:
					return Boolean.TYPE;
			}
		
		// Normal class
		try
		{
			return Class.forName(__field.className().toRuntimeString());
		}
		catch (ClassNotFoundException __e)
		{
			throw new SpringMLECallError(
				"Could not find class: " + __field,
				__e);
		}
	}
}
