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
import cc.squirreljme.jvm.mle.constants.VMDescriptionType;
import cc.squirreljme.jvm.mle.constants.VMStatisticType;
import cc.squirreljme.jvm.mle.constants.VMType;
import cc.squirreljme.runtime.cldc.SquirrelJME;
import cc.squirreljme.runtime.cldc.debug.CallTraceElement;
import cc.squirreljme.runtime.cldc.lang.LineEndingUtils;
import cc.squirreljme.vm.VMClassLibrary;
import cc.squirreljme.vm.springcoat.brackets.JarPackageObject;
import cc.squirreljme.vm.springcoat.brackets.RefLinkObject;
import cc.squirreljme.vm.springcoat.brackets.TracePointObject;
import cc.squirreljme.vm.springcoat.brackets.TypeObject;
import cc.squirreljme.vm.springcoat.brackets.VMThreadObject;
import cc.squirreljme.vm.springcoat.exceptions.SpringClassNotFoundException;
import cc.squirreljme.vm.springcoat.exceptions.SpringVirtualMachineException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import net.multiphasicapps.classfile.ClassName;
import net.multiphasicapps.classfile.MethodDescriptor;
import net.multiphasicapps.classfile.MethodNameAndType;
import net.multiphasicapps.classfile.PrimitiveType;

/**
 * This contains the native HLE handler for SpringCoat, all functions that
 * are performed on the MLE layer will be truly implemented here.
 *
 * @since 2020/05/30
 */
@SuppressWarnings({"OverlyComplexClass", "OverlyCoupledClass"})
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
		
		return (SpringArrayObjectGeneric)__throwable.fieldByNameAndType(
			false, "_stack",
			"[Lcc/squirreljme/jvm/mle/brackets/TracePointBracket;")
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
				
				// Jar calls
			case "cc/squirreljme/jvm/mle/JarPackageShelf":
				return NativeHLEHandler.dispatchJar(__thread, __method,
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
				
				// Thread calls
			case "cc/squirreljme/jvm/mle/ThreadShelf":
				return NativeHLEHandler.dispatchThread(__thread, __method,
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
			
			case "pointAddress:(Lcc/squirreljme/jvm/mle/brackets/" +
				"TracePointBracket;)J":
				return ((TracePointObject)__args[0]).getTrace().address();
				
			case "pointClass:(Lcc/squirreljme/jvm/mle/brackets/" +
				"TracePointBracket;)Ljava/lang/String;":
				return __thread.asVMObject(((TracePointObject)__args[0])
					.getTrace().className());
				
			case "pointFile:(Lcc/squirreljme/jvm/mle/brackets/" +
				"TracePointBracket;)Ljava/lang/String;":
				return __thread.asVMObject(((TracePointObject)__args[0])
					.getTrace().file());
				
			case "pointJavaAddress:(Lcc/squirreljme/jvm/mle/brackets/" +
				"TracePointBracket;)I":
				return ((TracePointObject)__args[0]).getTrace()
					.byteCodeAddress();
				
			case "pointJavaOperation:(Lcc/squirreljme/jvm/mle/brackets/" +
				"TracePointBracket;)I":
				return ((TracePointObject)__args[0]).getTrace()
					.byteCodeInstruction();
				
			case "pointLine:(Lcc/squirreljme/jvm/mle/brackets/" +
				"TracePointBracket;)I":
				return ((TracePointObject)__args[0]).getTrace()
					.line();
				
			case "pointMethodName:(Lcc/squirreljme/jvm/mle/brackets/" +
				"TracePointBracket;)Ljava/lang/String;":
				return __thread.asVMObject(((TracePointObject)__args[0])
					.getTrace().methodName());
				
			case "pointMethodType:(Lcc/squirreljme/jvm/mle/brackets/" +
				"TracePointBracket;)Ljava/lang/String;":
				return __thread.asVMObject(((TracePointObject)__args[0])
					.getTrace().methodDescriptor());
			
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
	 * Handles the dispatching of JAR shelf native methods.
	 *
	 * @param __thread The current thread this is acting under.
	 * @param __func The function which was called.
	 * @param __args The arguments to the call.
	 * @return The resulting object returned by the dispatcher.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/06/17
	 */
	public static Object dispatchJar(SpringThreadWorker __thread,
		MethodNameAndType __func, Object... __args)
	{
		if (__thread == null || __func == null)
			throw new NullPointerException("NARG");
		
		switch (__func.toString())
		{
			case "classPath:()[Lcc/squirreljme/jvm/mle/brackets/" +
				"JarPackageBracket;":
				return NativeHLEHandler.jarClasspath(__thread);
			
			case "openResource:(Lcc/squirreljme/jvm/mle/brackets/" +
				"JarPackageBracket;Ljava/lang/String;)Ljava/io/InputStream;":
				return NativeHLEHandler.jarOpenResource(__thread,
					(JarPackageObject)__args[0],
					__thread.<String>asNativeObject(String.class, __args[1]));
			
			default:
				throw new SpringVirtualMachineException(String.format(
					"Unknown Jar MLE native call: %s %s", __func,
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
			
			case "identityHashCode:(Ljava/lang/Object;)I":
				return System.identityHashCode(__args[0]);
			
			case "newInstance:(Lcc/squirreljme/jvm/mle/brackets/" +
				"TypeBracket;)Ljava/lang/Object;":
				return __thread.newInstance(
					((TypeObject)__args[0]).getSpringClass(),
					new MethodDescriptor("()V"));
			
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
			
			case "systemProperty:(Ljava/lang/String;)Ljava/lang/String;":
				return __thread.asVMObject(
					NativeHLEHandler.runtimeSystemProperty(__thread,
						__thread.<String>asNativeObject(
							String.class, __args[0])));
			
			case "vmDescription:(I)Ljava/lang/String;":
				return __thread.asVMObject(
					NativeHLEHandler.runtimeVmDescription((int)__args[0]));
			
			case "vmStatistic:(I)J":
				return NativeHLEHandler.runtimeVmStatistic(__thread,
					(int)__args[0]);
			
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
	public static Object dispatchTerminal(SpringThreadWorker __thread,
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
	 * Handles the dispatching of type thread native methods.
	 *
	 * @param __thread The current thread this is acting under.
	 * @param __func The function which was called.
	 * @param __args The arguments to the call.
	 * @return The resulting object returned by the dispatcher.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/06/17
	 */
	public static Object dispatchThread(SpringThreadWorker __thread,
		MethodNameAndType __func, Object... __args)
		throws NullPointerException
	{
		if (__thread == null || __func == null)
			throw new NullPointerException("NARG");
		
		switch (__func.toString())
		{
			case "aliveThreadCount:(ZZ)I":
				return NativeHLEHandler.threadAliveThreadCount(__thread,
					((int)__args[0] != 0), ((int)__args[1] != 0));
			
			case "createVMThread:(Ljava/lang/Thread;)Lcc/squirreljme/jvm/" +
				"mle/brackets/VMThreadBracket;":
				return NativeHLEHandler.threadCreateVMThread(__thread,
					(SpringObject)__args[0]);
			
			case "currentExitCode:()I":
				return __thread.machine._exitcode;
			
			case "currentJavaThread:()Ljava/lang/Thread;":
				return __thread.thread.threadInstance();
			
			case "javaThreadFlagStarted:(Ljava/lang/Thread;)V":
				NativeHLEHandler.threadJavaThreadFlagStarted(
					__thread, (SpringSimpleObject)__args[0]);
				return null;
			
			case "javaThreadSetAlive:(Ljava/lang/Thread;Z)V":
				NativeHLEHandler.threadJavaThreadSetAlive(
					__thread, (SpringSimpleObject)__args[0],
					(int)__args[1] != 0);
				return null;
			
			case "runProcessMain:()V":
				__thread.runProcessMain();
				return null;
			
			case "toVMThread:(Ljava/lang/Thread;)Lcc/squirreljme/" +
				"jvm/mle/brackets/VMThreadBracket;":
				return NativeHLEHandler.threadToVMThread(__thread,
					(SpringSimpleObject)__args[0]);
			
			case "vmThreadIsMain:(Lcc/squirreljme/jvm/mle/brackets/" +
				"VMThreadBracket;)Z":
				return ((VMThreadObject)__args[0]).getThread().isMain();
			
			default:
				throw new SpringVirtualMachineException(String.format(
					"Unknown Thread MLE native call: %s %s", __func,
					Arrays.asList(__args)));
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
			case "binaryPackageName:(Lcc/squirreljme/jvm/mle/brackets/" +
				"TypeBracket;)Ljava/lang/String;":
				return __thread.asVMObject(((TypeObject)__args[0])
					.getSpringClass().name().binaryName().inPackage()
					.toString());
			
			case "classToType:(Ljava/lang/Class;)" +
				"Lcc/squirreljme/jvm/mle/brackets/TypeBracket;":
				return NativeHLEHandler.typeClassToType(__thread,
					((SpringSimpleObject)__args[0]));
			
			case "equals:(Lcc/squirreljme/jvm/mle/brackets/TypeBracket;" +
				"Lcc/squirreljme/jvm/mle/brackets/TypeBracket;)Z":
				return Objects.equals(
					((TypeObject)__args[0]).getSpringClass(),
					((TypeObject)__args[1]).getSpringClass());
			
			case "findType:(Ljava/lang/String;)" +
				"Lcc/squirreljme/jvm/mle/brackets/TypeBracket;":
				return NativeHLEHandler.typeFindType(__thread,
					__thread.<String>asNativeObject(String.class, __args[0]));
				
			case "inJar:(Lcc/squirreljme/jvm/mle/brackets/TypeBracket;)" +
				"Lcc/squirreljme/jvm/mle/brackets/JarPackageBracket;":
				return new JarPackageObject(((TypeObject)__args[0])
					.getSpringClass().inJar());
			
			case "isArray:(Lcc/squirreljme/jvm/mle/brackets/TypeBracket;)Z":
				return ((TypeObject)__args[0]).getSpringClass().isArray();
			
			case "isInterface:(Lcc/squirreljme/jvm/mle/brackets/" +
				"TypeBracket;)Z":
				return ((TypeObject)__args[0]).getSpringClass().flags()
					.isInterface();
			
			case "isPrimitive:(Lcc/squirreljme/jvm/mle/brackets/" +
				"TypeBracket;)Z":
				return ((TypeObject)__args[0]).getSpringClass().name()
					.isPrimitive();
			
			case "objectType:(Ljava/lang/Object;)" +
				"Lcc/squirreljme/jvm/mle/brackets/TypeBracket;":
				return NativeHLEHandler.typeFindType(__thread,
					((SpringObject)__args[0]).type().name().toString());
				
			case "runtimeName:(Lcc/squirreljme/jvm/mle/brackets/" +
				"TypeBracket;)Ljava/lang/String;":
				return ((TypeObject)__args[0]).getSpringClass()
					.name().toRuntimeString();
			
			case "superClass:(Lcc/squirreljme/jvm/mle/brackets/" +
				"TypeBracket;)Lcc/squirreljme/jvm/mle/brackets/TypeBracket;":
				return NativeHLEHandler.typeSuperClass(
					(TypeObject)__args[0]);
			
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
	 * Returns the classpath of the JAR.
	 *
	 * @param __thread The thread to get the classpath of.
	 * @return The classpath of the JAR.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/06/17
	 */
	public static SpringArrayObjectGeneric jarClasspath(
		SpringThreadWorker __thread)
		throws NullPointerException
	{
		if (__thread == null)
			throw new NullPointerException("NARG");
		
		VMClassLibrary[] springPath = __thread.machine.classloader.classPath();
		
		// Wrap the classpath in package objects
		int n = springPath.length;
		SpringObject[] rv = new SpringObject[n];
		for (int i = 0; i < n; i++)
			rv[i] = new JarPackageObject(springPath[i]);
		
		// Wrap
		return __thread.asVMObjectArray(
			__thread.resolveClass(
				"[Lcc/squirreljme/jvm/mle/brackets/JarPackageBracket;"),
			rv);
	}
	
	/**
	 * Opens the JAR resource.
	 *
	 * @param __thread The thread calling this.
	 * @param __jar The JAR.
	 * @param __rcName The resource.
	 * @return The input stream or {@code null} if there is nothing.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/06/17
	 */
	public static SpringObject jarOpenResource(SpringThreadWorker __thread,
		JarPackageObject __jar, String __rcName)
		throws NullPointerException
	{
		if (__thread == null || __jar == null || __rcName == null)
			throw new NullPointerException("NARG");
		
		// Locate the resource
		try (InputStream in = __jar.library().resourceAsStream(__rcName))
		{
			// Not found
			if (in == null)
				return SpringNullObject.NULL;
			
			// Copy everything to the a byte array, since it is easier to
			// handle resources without juggling special resource streams
			// and otherwise
			try (ByteArrayOutputStream baos = new ByteArrayOutputStream(
				Math.max(1024, in.available())))
			{
				// Copy all the data
				byte[] copy = new byte[4096];
				for (;;)
				{
					int rc = in.read(copy);
					
					if (rc < 0)
						break;
					
					baos.write(copy, 0, rc);
				}
				
				// Use this as the stream input
				return __thread.newInstance(
					__thread.loadClass("java/io/ByteArrayInputStream"),
					new MethodDescriptor("([B)V"),
					__thread.asVMObject(baos.toByteArray()));
			}
		}
		
		// Could not read it
		catch (IOException e)
		{
			throw new SpringVirtualMachineException(
				"Failed to read resource", e);
		}
	}
	
	/**
	 * Returns the given system property.
	 *
	 * @param __thread The thread to get properties from.
	 * @param __key The property to get.
	 * @return The system property or {@code null} if not set.
	 * @since 2020/06/17
	 */
	public static String runtimeSystemProperty(SpringThreadWorker __thread,
		String __key)
	{
		if (__thread == null || __key == null)
			throw new NullPointerException("NARG");
		
		return __thread.machine._sysproperties.get(__key);
	}
	
	/**
	 * Returns a VM statistic.
	 *
	 * @param __thread The thread to get the statistic from.
	 * @param __key The {@link VMStatisticType} to get.
	 * @return The value of the statistic, will be {@code 0L} if not any.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/06/17
	 */
	public static long runtimeVmStatistic(SpringThreadWorker __thread,
		int __key)
		throws NullPointerException
	{
		if (__thread == null)
			throw new NullPointerException("NARG");
		
		switch (__key)
		{
			case VMStatisticType.MEM_FREE:
				return Runtime.getRuntime().freeMemory();
				
			case VMStatisticType.MEM_MAX:
				return Runtime.getRuntime().maxMemory();
			
			case VMStatisticType.MEM_USED:
				return Runtime.getRuntime().totalMemory();
		}
		
		return 0L;
	}
	
	/**
	 * Returns a description of the VM.
	 *
	 * @param __key The {@link VMDescriptionType}.
	 * @return The description.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/06/17
	 */
	public static String runtimeVmDescription(int __key)
		throws NullPointerException
	{
		switch (__key)
		{
			case VMDescriptionType.EXECUTABLE_PATH:
				return null;
			
			case VMDescriptionType.OS_ARCH:
				return "springcoat/" + System.getProperty("os.arch");
				
			case VMDescriptionType.OS_NAME:
				return System.getProperty("os.name");
			
			case VMDescriptionType.OS_VERSION:
				return System.getProperty("os.version");
			
			case VMDescriptionType.VM_EMAIL:
				return "xer@multiphasicapps.net";
				
			case VMDescriptionType.VM_NAME:
				return "SquirrelJME SpringCoat";
			
			case VMDescriptionType.VM_URL:
				return "https://squirreljme.cc/";
				
			case VMDescriptionType.VM_VENDOR:
				return "Stephanie Gawroriski";
			
			case VMDescriptionType.VM_VERSION:
				return SquirrelJME.RUNTIME_VERSION;
		}
		
		return null;
	}
	
	/**
	 * Flushes the output.
	 *
	 * @param __thread The thread that is writing.
	 * @param __fd The file descriptor.
	 * @return The error status.
	 * @since 2020/06/14
	 */
	public static int terminalFlush(
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
	public static int terminalWrite(
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
	public static int terminalWrite(
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
	 * Returns the number of alive threads.
	 *
	 * @param __thread The thread that is checking.
	 * @param __includeMain Include main threads?
	 * @param __includeDaemon Include daemon threads?
	 * @return The number of alive threads.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/06/17
	 */
	@SuppressWarnings("SynchronizationOnLocalVariableOrMethodParameter")
	public static int threadAliveThreadCount(SpringThreadWorker __thread,
		boolean __includeMain, boolean __includeDaemon)
		throws NullPointerException
	{
		if (__thread == null)
			throw new NullPointerException("NARG");
		
		// Count every thread
		int count = 0;
		SpringMachine machine = __thread.machine;
		synchronized (machine)
		{
			for (SpringThread thread : machine.getThreads())
			{
				boolean isMain = thread.isMain();
				boolean isDaemon = thread.isDaemon();
				
				if ((__includeMain && isMain) ||
					(__includeDaemon && isDaemon) ||
					(!isMain && !isDaemon))
					count++;
			}
		}
		
		return count;
	}
	
	/**
	 * Creates a VM thread for the given thread.
	 *
	 * If there is no bound hardware thread, then one will be created that
	 * is attached to the given thread.
	 *
	 * @param __thread The thread this is called by.
	 * @param __javaThread The Java thread to attach to.
	 * @return The thread object for the given thread.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/06/17
	 */
	@SuppressWarnings("SynchronizationOnLocalVariableOrMethodParameter")
	public static VMThreadObject threadCreateVMThread(
		SpringThreadWorker __thread, SpringObject __javaThread)
		throws NullPointerException
	{
		if (__thread == null || __javaThread == null)
			throw new NullPointerException("NARG");
		
		// Find the thread which the given passed object is bound to, this
		// is the target thread
		SpringThread target = null;
		SpringMachine machine = __thread.machine;
		synchronized (machine)
		{
			// Search through every thread
			SpringThread[] threads = machine.getThreads();
			for (SpringThread thread : threads)
			{
				SpringObject instance;
				try
				{
					instance = thread.threadInstance();
				}
				catch (IllegalStateException ignored)
				{
					continue;
				}
				
				// If this is the thread for this, then use that!
				if (__javaThread == instance)
				{
					target = thread;
					break;
				}
			}
			
			// If there is exactly one thread, we can rather get into a bit
			// of a loop where our main thread is created outside of normal
			// means by the VM and not by any other thread
			if (threads.length == 1)
				target = threads[0];
			
			// No actual thread exists that the object is bound to, so oops!
			// We need to actually create one here and bind it accordingly
			if (target == null)
				target = machine.createThread(null, false);
		}
		
		// Create object with this attached thread
		VMThreadObject vmThread = new VMThreadObject(target);
		
		// The thread gets these as well
		target.setThreadInstance(__javaThread);
		target.setVMThread(vmThread);
		
		return vmThread;
	}
	
	/**
	 * Flags the thread as being started.
	 *
	 * @param __thread The calling thread.
	 * @param __javaThread The thread to flag.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/06/17
	 */
	public static void threadJavaThreadFlagStarted(
		SpringThreadWorker __thread, SpringSimpleObject __javaThread)
		throws NullPointerException
	{
		if (__thread == null || __javaThread == null)
			throw new NullPointerException("NARG");
		
		// Just set the started field to true
		__javaThread.fieldByNameAndType(
			false, "_started", "Z").set(true);
	}
	
	/**
	 * Marks the thread as being alive or not.
	 *
	 * @param __thread The calling thread.
	 * @param __javaThread The thread to flag.
	 * @param __alive Is this thread alive?
	 * @throws NullPointerException On null arguments.
	 * @since 2020/06/17
	 */
	public static void threadJavaThreadSetAlive(SpringThreadWorker __thread,
		SpringSimpleObject __javaThread, boolean __alive)
		throws NullPointerException
	{
		if (__thread == null || __javaThread == null)
			throw new NullPointerException("NARG");
		
		// Just set the started field to true
		__javaThread.fieldByNameAndType(
			false, "_isAlive", "Z").set(__alive);
	}
	
	/**
	 * Returns the VM Thread of the given thread.
	 *
	 * @param __thread The calling thread.
	 * @param __javaThread The Java thread instance.
	 * @return The VM thread for the given thread.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/06/17
	 */
	public static VMThreadObject threadToVMThread(SpringThreadWorker __thread,
		SpringSimpleObject __javaThread)
		throws NullPointerException
	{
		if (__thread == null || __javaThread == null)
			throw new NullPointerException("NARG");
		
		return (VMThreadObject)__javaThread.fieldByField(
			__thread.resolveClass("java/lang/Thread")
			.lookupField(false, "_vmThread",
			"Lcc/squirreljme/jvm/mle/brackets/VMThreadBracket;")).get();
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
	 * Returns the superclass of the type.
	 *
	 * @param __type The type.
	 * @return The superclass or {@code null}.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/06/17
	 */
	private static SpringObject typeSuperClass(TypeObject __type)
		throws NullPointerException
	{
		if (__type == null)
			throw new NullPointerException("NARG");
		
		SpringClass superClass = __type.getSpringClass().superClass();
		if (superClass == null)
			return SpringNullObject.NULL;
		return new TypeObject(superClass);
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
