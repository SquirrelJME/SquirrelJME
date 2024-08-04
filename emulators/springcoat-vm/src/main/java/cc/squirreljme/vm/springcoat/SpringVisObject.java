// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.vm.springcoat.exceptions.SpringArrayIndexOutOfBoundsException;
import cc.squirreljme.vm.springcoat.exceptions.SpringArrayStoreException;
import cc.squirreljme.vm.springcoat.exceptions.SpringClassNotFoundException;
import cc.squirreljme.vm.springcoat.exceptions.SpringFatalException;
import cc.squirreljme.vm.springcoat.exceptions.SpringNoSuchMethodException;
import cc.squirreljme.vm.springcoat.exceptions.SpringUnmappableObjectException;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import net.multiphasicapps.classfile.ClassName;
import net.multiphasicapps.classfile.FieldDescriptor;
import net.multiphasicapps.classfile.MethodDescriptor;
import net.multiphasicapps.classfile.MethodNameAndType;
import net.multiphasicapps.classfile.PrimitiveType;

/**
 * A proxy virtualized object that wraps one on the host.
 *
 * @since 2024/08/04
 */
public class SpringVisObject
	extends SpringProxyObject
	implements SpringArray, SpringObject
{
	/** The real object to access. */
	protected final Object real;
	
	/** The real class type. */
	protected final Class<?> realClass;
	
	/** The vis class for this object. */
	private volatile SpringVisClass _visClass;
	
	/**
	 * Initializes the vis object.
	 *
	 * @param __machine The machine to use.
	 * @param __real The real object to access.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/08/04
	 */
	public SpringVisObject(SpringMachine __machine, Object __real)
		throws NullPointerException
	{
		super(new ClassName(__real.getClass().getName()
			.replace('.', '/')), __machine);
		
		if (__machine == null || __real == null)
			throw new NullPointerException("NARG");
		
		this.real = __real;
		this.realClass = __real.getClass();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/04
	 */
	@Override
	public Object array()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/04
	 */
	@Override
	public <C> C get(Class<C> __cl, int __dx)
		throws NullPointerException, SpringArrayIndexOutOfBoundsException
	{
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		// Get the thread to call under
		SpringMachine machine = this.machine;
		SpringThread contextThread = machine.getCurrentThread();
		try (CallbackThread callbackThread = (contextThread == null ?
			machine.obtainCallbackThread(true) : null))
		{
			// Re-obtain context thread accordingly
			if (contextThread == null)
				contextThread = callbackThread.thread();
			
			// Get worker
			SpringThreadWorker worker = contextThread._worker;
			if (worker == null)
				throw new SpringFatalException(
					String.format("No worker thread for %s", contextThread));
			
			// Convert to native type
			return __cl.cast(SpringVisObject.asVm(worker,
				this.realClass.getComponentType(),
				Array.get(this.real, __dx)));
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/04
	 */
	@Override
	public boolean isArray()
	{
		return this.realClass.isArray();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/04
	 */
	@Override
	public void set(int __dx, Object __v)
		throws SpringArrayStoreException, SpringArrayIndexOutOfBoundsException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/04
	 */
	@Override
	public int length()
	{
		if (!this.isArray())
			return -1;
		
		return Array.getLength(this.real);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/04
	 */
	@Override
	protected Object invokeProxy(SpringThreadWorker __thread,
		MethodNameAndType __method, Object[] __args)
		throws NullPointerException
	{
		if (__thread == null || __method == null)
			throw new NullPointerException("NARG");
		
		// Debug
		Debugging.debugNote("VIS.invokeProxy(%s, %s, %s)",
			__thread, __method, Arrays.toString(__args));
		
		// Resolve the real method type
		Class<?>[] realTypes = SpringVisObject.realType(__method.type());
		
		// Find the real method this is for
		Class<?> realClass = this.realClass;
		Method realMethod;
		try
		{
			realMethod = realClass.getMethod(__method.name().toString(),
				realTypes);
		}
		catch (NoSuchMethodException __e)
		{
			throw new SpringNoSuchMethodException(
				String.format("No real %s::%s",
					realClass.getName(), __method), __e);
		}
		
		// Get actual native arguments to use
		int n = realTypes.length;
		Object[] realArgs = new Object[n];
		for (int i = 0; i < n; i++)
			realArgs[i] = SpringVisObject.asNative(__thread,
				__method.type().argument(i), __args[i]);
		
		// Invoke the method
		try
		{
			// Invoke call
			return SpringVisObject.asVm(__thread, realMethod.getReturnType(),
				realMethod.invoke(this.real, realArgs));
		}
		catch (InvocationTargetException __e)
		{
			throw SpringException.convert(__e.getCause());
		}
		catch (IllegalAccessException __e)
		{
			throw new SpringFatalException(
				String.format("Could not invoke real %s::%s",
					realClass.getName(), __method), __e);
		}
		catch (IllegalArgumentException __e)
		{
			__e.printStackTrace(System.err);
			throw Debugging.todo(__thread, __method,
				Arrays.asList(__args),
				Arrays.asList(realTypes),
				Arrays.asList(realArgs));
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/04
	 */
	@Override
	public SpringClass type()
	{
		SpringVisClass result;
		synchronized (this)
		{
			// Has this been cached already?
			result = this._visClass;
			if (result != null)
				return result;
			
			// Virtualize it
			result = this.machine.virtualizeClass(this.real.getClass());
			this._visClass = result;
			return result;
		}
	}
	
	/**
	 * Maps the object as a native object.
	 *
	 * @param __thread The context thread.
	 * @param __context The context class.
	 * @param __in The input VM object.
	 * @return The resultant native object.
	 * @throws NullPointerException If no thread was specified.
	 * @since 2024/08/04
	 */
	public static Object asNative(SpringThreadWorker __thread,
		FieldDescriptor __context, Object __in)
		throws NullPointerException
	{
		if (__thread == null)
			throw new NullPointerException("NARG");
		
		// Boolean value?
		if (__context != null &&
			__context.primitiveType() == PrimitiveType.BOOLEAN)
			return ((Integer)__in != 0);
		
		// Try mapping it
		try
		{
			// If this is a vis object, then unwrap it
			if (__in instanceof SpringVisObject)
				return ((SpringVisObject)__in).real;
			
			// Map using SpringCoat code
			else
				return __thread.asNativeObject(__in);
		}
		catch (SpringUnmappableObjectException __e)
		{
			// We can only map VM objects natively
			if (!(__in instanceof SpringObject))
				throw new SpringFatalException(
					String.format("Cannot asNative(%s)", __in), __e);
			
			// If this is an object, then create an interface proxy map
			// to call into the VM with
			return SpringVisProxy.of(__thread.machine,
				(SpringObject)__in);
		}
	}
	
	/**
	 * Maps the object as a VM object.
	 *
	 * @param __thread The context thread.
	 * @param __context The context class.
	 * @param __in The input real object.
	 * @return The resultant VM object.
	 * @throws NullPointerException If no thread was specified.
	 * @since 2024/08/04
	 */
	public static Object asVm(SpringThreadWorker __thread,
		Class<?> __context, Object __in)
		throws NullPointerException
	{
		if (__thread == null)
			throw new NullPointerException("NARG");
		
		// Try mapping natively
		try
		{
			return __thread.asVMObject(__in);
		}
		
		// Otherwise, wrap in a vis object like this one
		catch (SpringUnmappableObjectException __ignored)
		{
			return new SpringVisObject(__thread.machine, __in);
		}
	}
	
	/**
	 * Resolves the real type for the given field descriptor.
	 *
	 * @param __type The type to get the real class for.
	 * @return The resultant real class.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/08/04
	 */
	public static Class<?> realType(FieldDescriptor __type)
		throws NullPointerException
	{
		if (__type == null)
			throw new NullPointerException("NARG");
		
		// Primitive?
		if (__type.isPrimitive())
			switch (__type.primitiveType())
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
		
		// If an array, we have to make one as we cannot just resolve it
		// as there is no resolution for arrays really
		if (__type.isArray())
		{
			// Get base component, the real type of it anyway
			Class<?> component = SpringVisObject.realType(
				__type.componentType());
			
			// We need to actually make the array for it to exist in
			// essentially every Java virtual machine
			return Array.newInstance(component, 0).getClass();
		}
		
		// Normal class
		try
		{
			return Class.forName(__type.className().toRuntimeString());
		}
		catch (ClassNotFoundException __e)
		{
			throw new SpringClassNotFoundException(__type.className(), __e);
		}
	}
	
	/**
	 * Resolves the real type for a method descriptor.
	 *
	 * @param __type The type to use.
	 * @return The native class types. 
	 * @throws NullPointerException On null arguments.
	 * @since 2024/08/04
	 */
	public static Class<?>[] realType(MethodDescriptor __type)
		throws NullPointerException
	{
		if (__type == null)
			throw new NullPointerException("NARG");
		
		// Resolve real class names
		int n = __type.argumentCount();
		Class<?>[] result = new Class[n];
		for (int i = 0; i < n; i++)
			result[i] = SpringVisObject.realType(__type.argument(i));
		
		// Use it!
		return result;
	}
	
	/**
	 * Maps the real type to a virtual machine type.
	 *
	 * @param __machine The machine context.
	 * @param __type The type to convert.
	 * @return The resultant field descriptor.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/08/04
	 */
	public static FieldDescriptor vmType(SpringMachine __machine,
		Class<?> __type)
	{
		if (__machine == null || __type == null)
			throw new NullPointerException("NARG");
		
		// Void maps to null
		if (__type == Void.TYPE)
			return null;
		
		// Primitive type?
		if (__type == Boolean.TYPE)
			return ClassName.fromPrimitiveType(
				PrimitiveType.BOOLEAN).field();
		else if (__type == Byte.TYPE)
			return ClassName.fromPrimitiveType(
				PrimitiveType.BYTE).field();
		else if (__type == Short.TYPE)
			return ClassName.fromPrimitiveType(
				PrimitiveType.SHORT).field();
		else if (__type == Character.TYPE)
			return ClassName.fromPrimitiveType(
				PrimitiveType.CHARACTER).field();
		else if (__type == Integer.TYPE)
			return ClassName.fromPrimitiveType(
				PrimitiveType.INTEGER).field();
		else if (__type == Long.TYPE)
			return ClassName.fromPrimitiveType(
				PrimitiveType.LONG).field();
		else if (__type == Float.TYPE)
			return ClassName.fromPrimitiveType(
				PrimitiveType.FLOAT).field();
		else if (__type == Double.TYPE)
			return ClassName.fromPrimitiveType(
				PrimitiveType.DOUBLE).field();
		
		// Class type
		return ClassName.fromRuntimeName(__type.getName()).field();
	}
	
	/**
	 * Maps the real type to a virtual machine type.
	 *
	 * @param __machine The machine context.
	 * @param __rVal The return value type.
	 * @param __args The arguments.
	 * @return The resultant descriptor.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/08/04
	 */
	public static MethodDescriptor vmTypes(SpringMachine __machine,
		Class<?> __rVal, Class<?>[] __args)
		throws NullPointerException
	{
		if (__machine == null || __rVal == null || __args == null)
			throw new NullPointerException("NARG");
		
		return new MethodDescriptor(SpringVisObject.vmType(__machine, __rVal),
			SpringVisObject.vmTypes(__machine, __args));
	}
	
	/**
	 * Maps the real type to a virtual machine type.
	 *
	 * @param __machine The machine context.
	 * @param __args The arguments.
	 * @return The resultant descriptor.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/08/04
	 */
	public static FieldDescriptor[] vmTypes(SpringMachine __machine,
		Class<?>[] __args)
	{
		if (__machine == null || __args == null)
			throw new NullPointerException("NARG");
		
		// Map each type
		int n = __args.length;
		FieldDescriptor[] result = new FieldDescriptor[n];
		for (int i = 0; i < n; i++)
			result[i] = SpringVisObject.vmType(__machine, __args[i]);
		
		// Use the result
		return result;
	}
}
