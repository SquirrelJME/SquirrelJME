// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat.brackets;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.vm.springcoat.SpringClass;
import cc.squirreljme.vm.springcoat.SpringMachine;
import cc.squirreljme.vm.springcoat.SpringObject;
import cc.squirreljme.vm.springcoat.SpringProxyObject;
import cc.squirreljme.vm.springcoat.SpringProxyObjectType;
import cc.squirreljme.vm.springcoat.SpringThreadWorker;
import cc.squirreljme.vm.springcoat.exceptions.SpringClassNotFoundException;
import cc.squirreljme.vm.springcoat.exceptions.SpringFatalException;
import cc.squirreljme.vm.springcoat.exceptions.SpringMLECallError;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.multiphasicapps.classfile.ClassName;
import net.multiphasicapps.classfile.FieldDescriptor;
import net.multiphasicapps.classfile.InvalidClassFormatException;
import net.multiphasicapps.classfile.MethodDescriptor;
import net.multiphasicapps.classfile.MethodNameAndType;

/**
 * This is a very powerful proxy bridge from SpringCoat to a real base object.
 *
 * @since 2024/08/02
 */
public class DynamicSuperProxy
	extends SpringProxyObject
	implements InvocationHandler
{
	/** The wrapped object. */
	public final Object wrapped;
	
	/**
	 * Initializes the super proxy.
	 *
	 * @param __machine The machine to use.
	 * @param __in The object to wrap.
	 * @param __className The class name to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/08/02
	 */
	private DynamicSuperProxy(SpringMachine __machine, Object __in,
		ClassName __className)
		throws NullPointerException
	{
		super(__className, __machine);
		
		if (__in == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.wrapped = __in;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	public Object invoke(Object __proxy, Method __method, Object[] __args)
		throws Throwable
	{
		if (__args == null)
			__args = new Object[0];
		Debugging.debugNote("INVOKE: %s %s %s%n", __proxy, __method,
			Arrays.asList(__args));
		return __method.invoke(this.wrapped, __args);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/02
	 */
	@Override
	public Object invokeProxy(SpringThreadWorker __thread,
		MethodNameAndType __method, Object[] __args)
	{
		if (__thread == null || __method == null)
			throw new SpringMLECallError("Invalid proxy call");
		
		// Get arguments first
		MethodDescriptor type = __method.type();
		int argCount = type.argumentCount();
		Class<?>[] argTypes = new Class[argCount];
		for (int i = 0; i < argCount; i++)
			argTypes[i] = DynamicSuperProxy.__resolve(type.argument(i));
		
		// Try finding the target method
		Object wrapped = this.wrapped;
		try
		{
			// Locate method to call
			Method method = wrapped.getClass().getMethod(
				__method.name().toString(), argTypes);
			
			// Normalize arguments
			Object[] normal = new Object[argCount];
			for (int i = 0; i < argCount; i++)
				normal[i] = __thread.asNativeObject(__args[i]);
			
			// Call into it
			return __thread.asVMObject(method.invoke(wrapped, normal));
		}
		catch (NoSuchMethodException|IllegalAccessException|
			   InvocationTargetException __e)
		{
			throw new SpringFatalException("Invalid method proxy call.",
				__e);
		}
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
				DynamicSuperProxy.__resolve(__field.rootComponentType()),
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
	
	/**
	 * Initializes the super proxy.
	 *
	 * @param __machine The machine to use.
	 * @param __in The object to wrap.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/08/02
	 */
	public static SpringObject of(SpringMachine __machine, Object __in)
		throws NullPointerException
	{
		if (__machine == null || __in == null)
			throw new NullPointerException("NARG");
		
		// All the target interfaces on both ends
		List<Class<?>> upper = new ArrayList<>();
		List<ClassName> lower = new ArrayList<>();
		
		// These are always set to identify a proxy
		upper.add(SpringObject.class);
		upper.add(SpringProxyObjectType.class);
		
		// Determine all eligible interfaces to implement, and ones to create
		for (Class<?> at = __in.getClass(); at != Object.class;
			 at = at.getSuperclass())
			for (Class<?> xface : at.getInterfaces())
			{
				upper.add(xface);
				
				try
				{
					ClassName maybe = new ClassName(
						xface.getName().replace('.', '/'));
					
					if (__machine.classLoader().loadClass(maybe) != null)
						lower.add(maybe);
				}
				catch (SpringClassNotFoundException|
					   InvalidClassFormatException __e)
				{
				}
			}
		
		// Create super proxy object
		return (SpringObject)Proxy.newProxyInstance(
			DynamicSuperProxy.class.getClassLoader(),
			upper.toArray(new Class[upper.size()]),
			new DynamicSuperProxy(__machine, __in, lower.get(0)));
	}
}
