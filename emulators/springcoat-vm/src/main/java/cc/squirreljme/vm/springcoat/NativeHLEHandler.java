// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import net.multiphasicapps.classfile.ClassName;

/**
 * This contains the native HLE handler for SpringCoat, all functions that
 * are performed on the MLE layer will be truly implemented here.
 *
 * @since 2020/05/30
 */
public final class NativeHLEHandler
{
	/** Atomic shelf. */
	private static final String _ATOMIC_SHELF_PREFIX =
		"cc/squirreljme/jvm/mle/AtomicShelf::";
	
	/** Atomic prefix length. */
	private static final int _ATOMIC_SHELF_PREFIX_LEN =
		NativeHLEHandler._ATOMIC_SHELF_PREFIX.length();
	
	/** Reference shelf class prefix. */
	private static final String _REFERENCE_SHELF_PREFIX =
		"cc/squirreljme/jvm/mle/ReferenceShelf::";
	
	/** Prefix length. */
	private static final int _REFERENCE_SHELF_PREFIX_LEN =
		NativeHLEHandler._REFERENCE_SHELF_PREFIX.length();
	
	/** Type shelf prefix. */
	private static final String _TYPE_SHELF_PREFIX =
		"cc/squirreljme/jvm/mle/TypeShelf::";
	
	/** Type shelf prefix lenght. */
	private static final int _TYPE_SHELF_PREFIX_LEN =
		NativeHLEHandler._TYPE_SHELF_PREFIX.length();
	
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
	public static void atomicGcUnlock(SpringThreadWorker __thread, int __key)
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
	public static int atomicTick(SpringThreadWorker __thread)
	{
		return NativeHLEHandler._TICKER.decrementAndGet();
	}
	
	/**
	 * Handles the dispatching of the native method.
	 *
	 * @param __thread The current thread this is acting under.
	 * @param __func The function which was called.
	 * @param __args The arguments to the call.
	 * @return The resulting object returned by the dispatcher.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/05/30
	 */
	public static Object dispatch(SpringThreadWorker __thread, String __func,
		Object... __args)
		throws NullPointerException
	{
		if (__thread == null || __func == null)
			throw new NullPointerException("NARG");
		
		// Atomic calls
		if (__func.startsWith(NativeHLEHandler._ATOMIC_SHELF_PREFIX))
			return NativeHLEHandler.dispatchAtomic(__thread,
				__func.substring(NativeHLEHandler._ATOMIC_SHELF_PREFIX_LEN),
				__args);
		
		// Reference calls
		else if (__func.startsWith(NativeHLEHandler._REFERENCE_SHELF_PREFIX))
			return NativeHLEHandler.dispatchReference(__thread,
				__func.substring(NativeHLEHandler._REFERENCE_SHELF_PREFIX_LEN),
				__args);
		
		// Type calls
		else if (__func.startsWith(NativeHLEHandler._TYPE_SHELF_PREFIX))
			return NativeHLEHandler.dispatchType(__thread,
				__func.substring(NativeHLEHandler._TYPE_SHELF_PREFIX_LEN),
				__args);
		
		// Currently Unsupported
		else
			throw new SpringVirtualMachineException(String.format(
				"Unknown MLE native call: %s %s", __func,
				Arrays.asList(__args)));
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
		String __func, Object... __args)
		throws NullPointerException
	{
		if (__thread == null || __func == null)
			throw new NullPointerException("NARG");
		
		switch (__func)
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
		String __func, Object... __args)
		throws NullPointerException
	{
		if (__thread == null || __func == null)
			throw new NullPointerException("NARG");
		
		switch (__func)
		{
			case "linkGetObject:(Lcc/squirreljme/jvm/mle/brackets/" +
				"RefLinkBracket;)Ljava/lang/Object;":
				return ((RefLinkObject)__args[0])._object;
			
			case "linkSetObject:(Lcc/squirreljme/jvm/mle/brackets/" +
				"RefLinkBracket;Ljava/lang/Object;)V":
				((RefLinkObject)__args[0])._object =
					(SpringObject)__args[1];
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
		String __func, Object... __args)
		throws NullPointerException
	{
		if (__thread == null || __func == null)
			throw new NullPointerException("NARG");
		
		switch (__func)
		{
			case "findType:(Ljava/lang/String;)" +
				"Lcc/squirreljme/jvm/mle/brackets/TypeBracket;":
				return NativeHLEHandler.typeFindType(__thread,
					__thread.<String>asNativeObject(String.class, __args[0]));
			
			case "objectType:(Ljava/lang/Object;)" +
				"Lcc/squirreljme/jvm/mle/brackets/TypeBracket;":
				return NativeHLEHandler.typeFindType(__thread,
					((SpringObject)__args[0]).type().name().toString());
			
			case "typeToClass:(Lcc/squirreljme/jvm/mle/brackets/" +
				"TypeBracket;)Ljava/lang/Class;":
				throw Debugging.todo();
			
			default:
				throw new SpringVirtualMachineException(String.format(
					"Unknown Type MLE native call: %s %s", __func,
					Arrays.asList(__args)));
		}
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
			// Still print the trace, just in case for debugging
			e.printStackTrace();
			
			return null;
		}
	}
}
