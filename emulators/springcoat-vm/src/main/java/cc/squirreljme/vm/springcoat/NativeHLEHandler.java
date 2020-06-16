// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat;

import cc.squirreljme.jvm.mle.constants.BuiltInEncodingType;
import cc.squirreljme.jvm.mle.constants.BuiltInLocaleType;
import cc.squirreljme.jvm.mle.constants.StandardPipeType;
import cc.squirreljme.jvm.mle.constants.VMType;
import cc.squirreljme.runtime.cldc.debug.CallTraceElement;
import cc.squirreljme.runtime.cldc.lang.LineEndingUtils;
import cc.squirreljme.vm.springcoat.brackets.RefLinkObject;
import cc.squirreljme.vm.springcoat.brackets.TracePointObject;
import cc.squirreljme.vm.springcoat.brackets.TypeObject;
import cc.squirreljme.vm.springcoat.exceptions.SpringClassNotFoundException;
import cc.squirreljme.vm.springcoat.exceptions.SpringMachineExitException;
import cc.squirreljme.vm.springcoat.exceptions.SpringVirtualMachineException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import net.multiphasicapps.classfile.ClassName;
import net.multiphasicapps.classfile.MethodNameAndType;
import net.multiphasicapps.classfile.PrimitiveType;

/**
 * This contains the native HLE handler for SpringCoat, all functions that
 * are performed on the MLE layer will be truly implemented here.
 *
 * @since 2020/05/30
 */
@SuppressWarnings("OverlyComplexClass")
public final class NativeHLEHandler
{
	/** How many times to spin before yielding. */
	private static final int _SPIN_LIMIT =
		8;
	
	/** Ticker for atomic values. */
	private static final AtomicInteger _TICKER =
		new AtomicInteger();
	
	/** The GC lock. */
	private static final AtomicInteger _GC_LOCK =
		new AtomicInteger();
	
	/**
	 * Not used.
	 *
	 * @since 2020/05/30
	 */
	private NativeHLEHandler()
	{
	}
	
	/**
	 * Atomically locks the garbage collector.
	 *
	 * @param __thread The current thread.
	 * @return The lock key or {@code 0} if not locked.
	 * @since 2020/05/31
	 */
	public static int atomicGcLock(SpringThreadWorker __thread)
	{
		// Generate a key which will be returned in the lock
		int key = NativeHLEHandler.atomicTick(__thread);
		
		// When unlocked the lock will have zero, so we can set it to the key
		// we generated... otherwise we fail here
		if (NativeHLEHandler._GC_LOCK.compareAndSet(0, key))
			return key;
		return 0;
	}
	
	/**
	 * Atomically unlocks the garbage collector.
	 *
	 * @param __thread The thread performing the unlock.
	 * @param __key The locking key.
	 * @since 2020/05/31
	 */
	public static void atomicGcUnlock(
		@SuppressWarnings("unused") SpringThreadWorker __thread, int __key)
	{
		// Unlocking is simple and only works if we have the key used to lock
		// the garbage collector
		NativeHLEHandler._GC_LOCK.compareAndSet(__key, 0);
	}
	
	/**
	 * Performs spin-locking logic.
	 *
	 * @param __thread The thread doing the lock.
	 * @param __c The lock count.
	 * @since 2020/05/31
	 */
	@SuppressWarnings("unused")
	public static void atomicSpinLock(SpringThreadWorker __thread, int __c)
	{
		// If we spin for too long, instead give up our cycles
		if (__c > NativeHLEHandler._SPIN_LIMIT)
			Thread.yield();
	}
	
	/**
	 * Atomically ticks a counter.
	 *
	 * @param __thread The calling thread.
	 * @return An atomically ticked value.
	 * @since 2020/05/31
	 */
	public static int atomicTick(
		@SuppressWarnings("unused") SpringThreadWorker __thread)
	{
		return NativeHLEHandler._TICKER.decrementAndGet();
	}
	
	/**
	 * Gets the trace from the given throwable.
	 *
	 * @param __thread The thread calling this.
	 * @param __throwable The throwable to extract from.
	 * @return The trace from the throwable.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/06/14
	 */
	public static SpringArrayObjectGeneric debugGetThrowableTrace(
		SpringThreadWorker __thread, SpringSimpleObject __throwable)
		throws NullPointerException
	{
		if (__thread == null || __throwable == null)
			throw new NullPointerException("NARG");
		
		return (SpringArrayObjectGeneric)__throwable.fieldByField(
			__thread.resolveClass(new ClassName("java/lang/Throwable"))
			.lookupField(false, "_stack",
				"[Lcc/squirreljme/jvm/mle/brackets/TracePointBracket;"))
			.get();
	}
	
	
	/**
	 * Handles the dispatching of the native method.
	 *
	 * @param __thread The current thread this is acting under.
	 * @param __class The native class being called.
	 * @param __method The method being called.
	 * @param __args The arguments to the call.
	 * @return The resulting object returned by the dispatcher.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/05/30
	 */
	public static Object dispatch(SpringThreadWorker __thread,
		ClassName __class, MethodNameAndType __method, Object... __args)
		throws NullPointerException
	{
		if (__thread == null || __class == null)
			throw new NullPointerException("NARG");
		
		switch (__class.toString())
		{
				// Atomic calls
			case "cc/squirreljme/jvm/mle/AtomicShelf":
				return NativeHLEHandler.dispatchAtomic(__thread, __method,
					__args);
				
				// Debug calls
			case "cc/squirreljme/jvm/mle/DebugShelf":
				return NativeHLEHandler.dispatchDebug(__thread, __method,
					__args);
					
				// Object calls
			case "cc/squirreljme/jvm/mle/ObjectShelf":
				return NativeHLEHandler.dispatchObject(__thread, __method,
					__args);
		
				// Reference calls
			case "cc/squirreljme/jvm/mle/ReferenceShelf":
				return NativeHLEHandler.dispatchReference(__thread, __method,
					__args);
				
				// Runtime calls
			case "cc/squirreljme/jvm/mle/RuntimeShelf":
				return NativeHLEHandler.dispatchRuntime(__thread, __method,
					__args);
				
				// Terminal calls
			case "cc/squirreljme/jvm/mle/TerminalShelf":
				return NativeHLEHandler.dispatchTerminal(__thread, __method,
					__args);
		
				// Type calls
			case "cc/squirreljme/jvm/mle/TypeShelf":
				return NativeHLEHandler.dispatchType(__thread, __method,
					__args);
				
			default:
				throw new SpringVirtualMachineException(String.format(
					"Unknown MLE native call: %s::%s %s", __class, __method,
					Arrays.asList(__args)));
		}
	}
	
	/**
	 * Handles the dispatching of atomic shelf native methods.
	 *
	 * @param __thread The current thread this is acting under.
	 * @param __func The function which was called.
	 * @param __args The arguments to the call.
	 * @return The resulting object returned by the dispatcher.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/05/31
	 */
	public static Object dispatchAtomic(SpringThreadWorker __thread,
		MethodNameAndType __func, Object... __args)
		throws NullPointerException
	{
		if (__thread == null || __func == null)
			throw new NullPointerException("NARG");
		
		switch (__func.toString())
		{
			case "gcLock:()I":
				return NativeHLEHandler.atomicGcLock(__thread);
			
			case "gcUnlock:(I)V":
				NativeHLEHandler.atomicGcUnlock(__thread,
					(int)__args[0]);
				return null;
			
			case "spinLock:(I)V":
				NativeHLEHandler.atomicSpinLock(__thread,
					(int)__args[0]);
				return null;
			
			case "tick:()I":
				return NativeHLEHandler.atomicTick(__thread);
			
			default:
				throw new SpringVirtualMachineException(String.format(
					"Unknown Atomic MLE native call: %s %s", __func,
					Arrays.asList(__args)));
		}
	}
	
	/**
	 * Handles the dispatching of atomic shelf native methods.
	 *
	 * @param __thread The current thread this is acting under.
	 * @param __func The function which was called.
	 * @param __args The arguments to the call.
	 * @return The resulting object returned by the dispatcher.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/06/11
	 */
	public static Object dispatchDebug(SpringThreadWorker __thread,
		MethodNameAndType __func, Object... __args)
		throws NullPointerException
	{
		if (__thread == null || __func == null)
			throw new NullPointerException("NARG");
		
		switch (__func.toString())
		{
			case "getThrowableTrace:(Ljava/lang/Throwable;)" +
				"[Lcc/squirreljme/jvm/mle/brackets/TracePointBracket;":
				return NativeHLEHandler.debugGetThrowableTrace(
					__thread, (SpringSimpleObject)__args[0]);
			
			case "traceStack:()" +
				"[Lcc/squirreljme/jvm/mle/brackets/TracePointBracket;":
				return __thread.asVMObjectArray(__thread.resolveClass(
					new ClassName("[Lcc/squirreljme/jvm/" +
					"mle/brackets/TracePointBracket;")),
					NativeHLEHandler.traceTraceStack(__thread));
			
			default:
				throw new SpringVirtualMachineException(String.format(
					"Unknown Debug MLE native call: %s %s", __func,
					Arrays.asList(__args)));
		}
	}
	
	/**
	 * Handles the dispatching of object shelf native methods.
	 *
	 * @param __thread The current thread this is acting under.
	 * @param __func The function which was called.
	 * @param __args The arguments to the call.
	 * @return The resulting object returned by the dispatcher.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/06/09
	 */
	public static Object dispatchObject(SpringThreadWorker __thread,
		MethodNameAndType __func, Object... __args)
		throws NullPointerException
	{
		if (__thread == null || __func == null)
			throw new NullPointerException("NARG");
		
		switch (__func.toString())
		{
			case "arrayLength:(Ljava/lang/Object;)I":
				{
					SpringObject object = (SpringObject)__args[0];
					if (object instanceof SpringArrayObject)
						return ((SpringArrayObject)object).length();
					return 0;
				}
			
			case "arrayNew:(Lcc/squirreljme/jvm/mle/brackets/TypeBracket;I)" +
				"Ljava/lang/Object;":
				return __thread.allocateArray(((TypeObject)__args[0])
					.getSpringClass(), (int)__args[1]);
			
			default:
				throw new SpringVirtualMachineException(String.format(
					"Unknown Object MLE native call: %s %s", __func,
					Arrays.asList(__args)));
		}
	}
	
	/**
	 * Handles the dispatching of reference shelf native methods.
	 *
	 * @param __thread The current thread this is acting under.
	 * @param __func The function which was called.
	 * @param __args The arguments to the call.
	 * @return The resulting object returned by the dispatcher.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/05/30
	 */
	public static Object dispatchReference(SpringThreadWorker __thread,
		MethodNameAndType __func, Object... __args)
		throws NullPointerException
	{
		if (__thread == null || __func == null)
			throw new NullPointerException("NARG");
		
		switch (__func.toString())
		{
			case "linkGetObject:(Lcc/squirreljme/jvm/mle/brackets/" +
				"RefLinkBracket;)Ljava/lang/Object;":
				return ((RefLinkObject)__args[0]).getObject();
			
			case "linkSetObject:(Lcc/squirreljme/jvm/mle/brackets/" +
				"RefLinkBracket;Ljava/lang/Object;)V":
				((RefLinkObject)__args[0]).setObject(
					(SpringObject)__args[1]);
				return null;
				
			case "newLink:()Lcc/squirreljme/jvm/mle/brackets/RefLinkBracket;":
				return new RefLinkObject();
			
			case "objectGet:(Ljava/lang/Object;)" +
				"Lcc/squirreljme/jvm/mle/brackets/RefLinkBracket;":
				return ((SpringObject)__args[0]).refLink().get();
			
			case "objectSet:(Ljava/lang/Object;" +
				"Lcc/squirreljme/jvm/mle/brackets/RefLinkBracket;)V":
				((SpringObject)__args[0]).refLink().set(
					(RefLinkObject)__args[1]);
				return null;
			
			default:
				throw new SpringVirtualMachineException(String.format(
					"Unknown Reference MLE native call: %s %s", __func,
					Arrays.asList(__args)));
		}
	}
	
	/**
	 * Handles the run-time system such as locales and otherwise.
	 *
	 * @param __thread The thread being called under.
	 * @param __func The method to execute.
	 * @param __args The arguments to the call.
	 * @return The result of the call.
	 * @since 2020/06/11
	 */
	@SuppressWarnings("SwitchStatementWithTooFewBranches")
	public static Object dispatchRuntime(SpringThreadWorker __thread,
		MethodNameAndType __func, Object... __args)
	{
		if (__thread == null || __func == null)
			throw new NullPointerException("NARG");
		
		switch (__func.toString())
		{
			case "encoding:()I":
				return BuiltInEncodingType.UTF8;
			
			case "exit:(I)V":
				__thread.machine.exit((int)__args[0]);
				return null;
			
			case "lineEnding:()I":
				return LineEndingUtils.toType(
					System.getProperty("line.separator"));
			
			case "locale:()I":
				switch (System.getProperty("user.country"))
				{
					case "US":
						switch (System.getProperty("user.language"))
						{
							case "en":
								return BuiltInLocaleType.ENGLISH_US;
						}
						return BuiltInLocaleType.UNSPECIFIED;
					
					default:
						return BuiltInLocaleType.UNSPECIFIED;
				}
			
			case "vmType:()I":
				return VMType.SPRINGCOAT;
			
			default:
				throw new SpringVirtualMachineException(String.format(
					"Unknown Runtime MLE native call: %s %s", __func,
					Arrays.asList(__args)));
		}
	}
	
	/**
	 * Handles the dispatching of console shelf native methods.
	 *
	 * @param __thread The current thread this is acting under.
	 * @param __func The function which was called.
	 * @param __args The arguments to the call.
	 * @return The resulting object returned by the dispatcher.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/06/02
	 */
	private static Object dispatchTerminal(SpringThreadWorker __thread,
		MethodNameAndType __func, Object... __args)
	{
		if (__thread == null || __func == null)
			throw new NullPointerException("NARG");
		
		switch (__func.toString())
		{
			case "flush:(I)I":
				return NativeHLEHandler.terminalFlush(__thread,
					(int)__args[0]);
				
			case "write:(II)I":
				return NativeHLEHandler.terminalWrite(__thread,
					(int)__args[0], (int)__args[1]);
				
			case "write:(I[BII)I":
				return NativeHLEHandler.terminalWrite(__thread,
					(int)__args[0],
					((SpringArrayObjectByte)__args[1]).array(),
					(int)__args[2],
					(int)__args[3]);
			
			default:
				throw new SpringVirtualMachineException(String.format(
					"Unknown Terminal MLE native call: %s %s", __func,
					Arrays.asList(__args)));
		}
	}
	
	/**
	 * Flushes the output.
	 *
	 * @param __thread The thread that is writing.
	 * @param __fd The file descriptor.
	 * @return The error status.
	 * @since 2020/06/14
	 */
	private static int terminalFlush(
		@SuppressWarnings("unused") SpringThreadWorker __thread, int __fd)
	{
		try
		{
			NativeHLEHandler.__fdOutput(__fd).flush();
			return 1;
		}
		catch (IllegalArgumentException|IOException e)
		{
			return -1;
		}
	}
	
	/**
	 * Writes to the output.
	 *
	 * @param __thread The thread that is writing.
	 * @param __fd The file descriptor.
	 * @param __c The byte to write.
	 * @return The error status.
	 * @since 2020/06/14
	 */
	private static int terminalWrite(
		@SuppressWarnings("unused") SpringThreadWorker __thread, int __fd,
		int __c)
	{
		try
		{
			NativeHLEHandler.__fdOutput(__fd).write(__c);
			return 1;
		}
		catch (IllegalArgumentException|IOException e)
		{
			return -1;
		}
	}
	
	/**
	 * Writes to the output.
	 *
	 * @param __thread The thread that is writing.
	 * @param __fd The file descriptor.
	 * @param __b The bytes to write.
	 * @param __o The offset into the array.
	 * @param __l The number of bytes to write.
	 * @return The error status.
	 * @since 2020/06/14
	 */
	private static int terminalWrite(
		@SuppressWarnings("unused") SpringThreadWorker __thread, int __fd,
		byte[] __b, int __o, int __l)
	{
		try
		{
			NativeHLEHandler.__fdOutput(__fd).write(__b, __o, __l);
			return __l;
		}
		catch (IllegalArgumentException|IOException e)
		{
			return -1;
		}
	}
	
	/**
	 * Handles the dispatching of type shelf native methods.
	 *
	 * @param __thread The current thread this is acting under.
	 * @param __func The function which was called.
	 * @param __args The arguments to the call.
	 * @return The resulting object returned by the dispatcher.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/06/02
	 */
	public static Object dispatchType(SpringThreadWorker __thread,
		MethodNameAndType __func, Object... __args)
		throws NullPointerException
	{
		if (__thread == null || __func == null)
			throw new NullPointerException("NARG");
		
		switch (__func.toString())
		{
			case "classToType:(Ljava/lang/Class;)" +
				"Lcc/squirreljme/jvm/mle/brackets/TypeBracket;":
				return NativeHLEHandler.typeClassToType(__thread,
					((SpringSimpleObject)__args[0]));
			
			case "findType:(Ljava/lang/String;)" +
				"Lcc/squirreljme/jvm/mle/brackets/TypeBracket;":
				return NativeHLEHandler.typeFindType(__thread,
					__thread.<String>asNativeObject(String.class, __args[0]));
			
			case "isArray:(Lcc/squirreljme/jvm/mle/brackets/TypeBracket;)Z":
				return ((TypeObject)__args[0]).getSpringClass().isArray();
			
			case "objectType:(Ljava/lang/Object;)" +
				"Lcc/squirreljme/jvm/mle/brackets/TypeBracket;":
				return NativeHLEHandler.typeFindType(__thread,
					((SpringObject)__args[0]).type().name().toString());
				
			case "runtimeName:(Lcc/squirreljme/jvm/mle/brackets/" +
				"TypeBracket;)Ljava/lang/String;":
				return ((TypeObject)__args[0]).getSpringClass()
					.name().toRuntimeString();
			
			case "typeOfBoolean:()Lcc/squirreljme/jvm/mle/brackets/" +
				"TypeBracket;":
				return new TypeObject(__thread.loadClass(
					ClassName.fromPrimitiveType(PrimitiveType.BOOLEAN)));
			
			case "typeOfByte:()Lcc/squirreljme/jvm/mle/brackets/TypeBracket;":
				return new TypeObject(__thread.loadClass(
					ClassName.fromPrimitiveType(PrimitiveType.BYTE)));
			
			case "typeOfCharacter:()Lcc/squirreljme/jvm/mle/brackets/" +
				"TypeBracket;":
				return new TypeObject(__thread.loadClass(
					ClassName.fromPrimitiveType(PrimitiveType.CHARACTER)));
			
			case "typeOfDouble:()Lcc/squirreljme/jvm/mle/brackets/" +
				"TypeBracket;":
				return new TypeObject(__thread.loadClass(
					ClassName.fromPrimitiveType(PrimitiveType.DOUBLE)));
			
			case "typeOfFloat:()Lcc/squirreljme/jvm/mle/brackets/TypeBracket;":
				return new TypeObject(__thread.loadClass(
					ClassName.fromPrimitiveType(PrimitiveType.FLOAT)));
			
			case "typeOfInteger:()Lcc/squirreljme/jvm/mle/brackets/" +
				"TypeBracket;":
				return new TypeObject(__thread.loadClass(
					ClassName.fromPrimitiveType(PrimitiveType.INTEGER)));
			
			case "typeOfLong:()Lcc/squirreljme/jvm/mle/brackets/TypeBracket;":
				return new TypeObject(__thread.loadClass(
					ClassName.fromPrimitiveType(PrimitiveType.LONG)));
			
			case "typeOfShort:()Lcc/squirreljme/jvm/mle/brackets/TypeBracket;":
				return new TypeObject(__thread.loadClass(
					ClassName.fromPrimitiveType(PrimitiveType.SHORT)));
			
			case "typeToClass:(Lcc/squirreljme/jvm/mle/brackets/" +
				"TypeBracket;)Ljava/lang/Class;":
				return __thread.asVMObject(((TypeObject)__args[0])
					.getSpringClass());
			
			default:
				throw new SpringVirtualMachineException(String.format(
					"Unknown Type MLE native call: %s %s", __func,
					Arrays.asList(__args)));
		}
	}
	
	/**
	 * Returns the stack trace for the given thread.
	 *
	 * @param __thread The thread to trace.
	 * @return The stack trace for the thread.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/06/13
	 */
	public static TracePointObject[] traceTraceStack(
		SpringThreadWorker __thread)
		throws NullPointerException
	{
		if (__thread == null)
			throw new NullPointerException("NARG");
		
		CallTraceElement[] trace = __thread.thread.getStackTrace();
		
		int n = trace.length;
		TracePointObject[] rv = new TracePointObject[n];
		
		for (int i = 0; i < n; i++)
			rv[i] = new TracePointObject(trace[i]);
		
		return rv;
	}
	
	/**
	 * Returns the type that is associated with the given {@link Class} type.
	 *
	 * @param __thread The thread accessing this.
	 * @param __classObj The input object.
	 * @return The type that is associated with the class object.
	 * @since 2020/06/11
	 */
	public static TypeObject typeClassToType(SpringThreadWorker __thread,
		SpringSimpleObject __classObj)
	{
		return (TypeObject)__classObj.fieldByField(
			__thread.resolveClass(new ClassName("java/lang/Class"))
			.lookupField(false, "_type",
				"Lcc/squirreljme/jvm/mle/brackets/TypeBracket;")).get();
	}
	
	/**
	 * Finds a type by the name of that type.
	 *
	 * @param __thread The thread this is working under.
	 * @param __name The name of the class to lookup.
	 * @return The type for this class or {@code null}.
	 * @since 2020/06/02
	 */
	public static TypeObject typeFindType(SpringThreadWorker __thread,
		String __name)
	{
		if (__name == null)
			throw new NullPointerException("NARG");
		
		try
		{
			return new TypeObject(__thread.loadClass(new ClassName(__name)));
		}
		
		// Since the method returns null when not found, we want to return
		// this here
		catch (SpringClassNotFoundException e)
		{
			return null;
		}
	}
	
	/**
	 * Returns the output stream for the given descriptor.
	 *
	 * @param __fd The file descriptor.
	 * @return The output stream for it.
	 * @throws IllegalArgumentException If the descriptor is not valid.
	 * @since 2020/06/13
	 */
	private static OutputStream __fdOutput(int __fd)
		throws IllegalArgumentException
	{
		switch (__fd)
		{
			case StandardPipeType.STDOUT:	return System.out;
			case StandardPipeType.STDERR:	return System.err;
			
			default:
				throw new IllegalArgumentException("Unknown FD: " + __fd);
		}
	}
}
