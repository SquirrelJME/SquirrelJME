// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat;

import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.vm.springcoat.exceptions.SpringFatalException;
import cc.squirreljme.vm.springcoat.exceptions.SpringUnmappableObjectException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import net.multiphasicapps.classfile.ClassName;
import net.multiphasicapps.classfile.MethodDescriptor;
import net.multiphasicapps.classfile.MethodName;
import net.multiphasicapps.classfile.MethodNameAndType;

/**
 * A SpringCoat proxy from a VM object to a real object via interfaces.
 *
 * @since 2024/08/04
 */
public class SpringVisProxy
	implements InvocationHandler
{
	/** The machine being proxied under. */
	protected final SpringMachine machine;
	
	/** The VM object being proxied. */
	protected final SpringObject vmObject;
	
	/**
	 * Initializes the host to VM proxy.
	 *
	 * @param __machine The machine to proxy under.
	 * @param __vmObject The VM object being proxied.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/08/04
	 */
	private SpringVisProxy(SpringMachine __machine, SpringObject __vmObject)
		throws NullPointerException
	{
		if (__machine == null || __vmObject == null)
			throw new NullPointerException("NARG");
		
		this.machine = __machine;
		this.vmObject = __vmObject;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/08/04
	 */
	@Override
	public Object invoke(Object __proxy, Method __method, Object[] __args)
		throws Throwable
	{
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
			
			// Map method being invoked
			MethodNameAndType nat = new MethodNameAndType(
				new MethodName(__method.getName()),
				SpringVisObject.vmTypes(machine,
					__method.getReturnType(),
					__method.getParameterTypes())); 
			
			// Map parameters
			int n = (__args == null ? 0 : __args.length);
			Object[] vmArgs = new Object[n + 1];
			vmArgs[0] = this.vmObject;
			for (int i = 0; i < n; i++)
				vmArgs[i + 1] = SpringVisObject.asVm(worker, null,
					__args[i]);
			
			// Call VM method, if it fails then this returns an exception
			Object result = contextThread.invokeMethod(false,
				this.vmObject.type().name(), nat, vmArgs);
			if (result instanceof MethodInvokeException)
			{
				MethodInvokeException t = (MethodInvokeException)result;
				
				// Emit the exception
				t.printStackTrace();
				t.printVmTrace(System.err);
				
				// Wrap
				throw new MLECallError(String.format("VM Exception: %s",
					t.exception), t);
			}
			
			// As native object
			if (result != null)
				return SpringVisObject.asNative(worker,
					nat.type().returnValue(),
					result);
			return null;
		}
	}
	
	/**
	 * Wraps the VM object with a native proxy.
	 *
	 * @param __machine The machine to proxy for.
	 * @param __vmObject The object to proxy.
	 * @return The resultant native proxied object.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/08/04
	 */
	public static Object of(SpringMachine __machine, SpringObject __vmObject)
		throws NullPointerException
	{
		if (__machine == null || __vmObject == null)
			throw new NullPointerException("NARG");
		
		// Determine which interfaces are actually valid in the real world
		List<Class<?>> interfaces = new ArrayList<>();
		for (SpringClass vmClass : __vmObject.type().interfaceClasses())
			try
			{
				// Try to locate the class for this
				Class<?> realClass = Class.forName(vmClass.name()
					.toRuntimeString());
				
				// Only interfaces are valid and this might be an interface
				// in the VM but not one natively
				if (realClass.isInterface())
					interfaces.add(realClass);
			}
			catch (ClassNotFoundException __ignored)
			{
			}
		
		// Wrap in proxy
		return Proxy.newProxyInstance(SpringVisProxy.class.getClassLoader(),
			interfaces.toArray(new Class[interfaces.size()]),
			new SpringVisProxy(__machine, __vmObject));
	}
}
