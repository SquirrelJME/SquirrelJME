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
import cc.squirreljme.vm.springcoat.exceptions.SpringMLECallError;
import net.multiphasicapps.classfile.ClassName;
import net.multiphasicapps.classfile.MethodNameAndType;

/**
 * An adapter for any kind of callback class.
 *
 * @since 2022/06/28
 */
public abstract class SpringCallbackAdapter
{
	/** Printing of stack traces. */
	private static final MethodNameAndType _PRINT_STACK_TRACE_NAT =
		new MethodNameAndType("printStackTrace", "()V");
	
	/** Throwable classes. */
	private static final ClassName _THROWABLE_CLASS =
		new ClassName("java/lang/Throwable");
	
	/** The callback class. */
	protected final ClassName callbackClass;
	
	/** The object to call into. */
	protected final SpringObject target;
	
	/** The machine to call for when callbacks occur. */
	protected final SpringMachine machine;
	
	/**
	 * Initializes the callback adapter.
	 *
	 * @param __callbackClass The class used to callback.
	 * @param __machine The machine used.
	 * @param __target The callback to call.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/06/28
	 */
	protected SpringCallbackAdapter(ClassName __callbackClass,
		SpringMachine __machine, SpringObject __target)
		throws NullPointerException
	{
		if (__callbackClass == null || __machine == null || __target == null)
			throw new NullPointerException("NARG");
		
		this.callbackClass = __callbackClass;
		this.machine = __machine;
		this.target = __target;
	}
	
	/**
	 * Invokes the callback.
	 * 
	 * @param __nat The name and type.
	 * @param __args The arguments to the call.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/06/28
	 */
	protected final Object invokeCallback(MethodNameAndType __nat,
		Object... __args)
		throws NullPointerException
	{
		if (__nat == null || __args == null)
			throw new NullPointerException("NARG");
		
		// Inject our object into the call
		int argLen = __args.length;
		Object[] callArgs = new Object[argLen + 1];
		System.arraycopy(__args, 0, callArgs, 1, argLen);
		callArgs[0] = this.target;
		
		// Setup callback thread for handling
		try (CallbackThread cb = this.machine.obtainCallbackThread(
			true))
		{
			// Invoke the given method
			Object result = cb.thread().invokeMethod(false,
				this.callbackClass, __nat, callArgs);
			
			// Request failed, do not fail but eat the exception
			if (result instanceof MethodInvokeException)
			{
				MethodInvokeException mie = (MethodInvokeException)result;
				
				Debugging.debugNote("--------------------------------");
				
				// Print outside exception to try to get the original call
				Debugging.debugNote("Callback exception: %s",
					mie.exception);
				mie.printStackTrace(System.err);
				
				// Print stack trace through the VM if possible
				Debugging.debugNote("Within VM:");
				cb.thread().invokeMethod(false,
					SpringCallbackAdapter._THROWABLE_CLASS,
					SpringCallbackAdapter._PRINT_STACK_TRACE_NAT,
					mie.exception);
				
				Debugging.debugNote("--------------------------------");
				
				throw new SpringMLECallError("Callback threw exception!");
			}
			
			// Use the given result, if any
			return result;
		}
	}
}
