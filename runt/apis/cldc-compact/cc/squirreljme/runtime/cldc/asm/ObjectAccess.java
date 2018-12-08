// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.asm;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.lang.ApiLevel;
import cc.squirreljme.runtime.cldc.lang.ClassData;
import cc.squirreljme.runtime.cldc.ref.PrimitiveReference;
import cc.squirreljme.runtime.cldc.ref.PrimitiveWeakReference;

/**
 * This contains accessors for object information.
 *
 * @since 2018/09/22
 */
public final class ObjectAccess
{
	/** Monitor is not owned by this thread. */
	public static final int MONITOR_NOT_OWNED =
		-1;
	
	/** Monitor did not interrupt. */
	public static final int MONITOR_NOT_INTERRUPTED =
		0;
	
	/** Monitor did interrupt. */
	public static final int MONITOR_INTERRUPTED =
		1;
	
	/**
	 * Not used.
	 *
	 * @since 2018/09/22
	 */
	private ObjectAccess()
	{
	}
	
	/**
	 * Allocates an object but does not construct it
	 *
	 * @param __cl The class to allocate.
	 * @return An object for the class, it is not initialized with a
	 * constructor. Returns {@code null} if no more memory is available.
	 * @since 2018/12/04
	 */
	@Api(ApiLevel.LEVEL_SQUIRRELJME_0_2_0_20181225)
	public static final native Object allocateObject(String __cl);
	
	/**
	 * Returns the length of the given array.
	 *
	 * @param __a The array to get the length of.
	 * @return The length of the array or {@code -1} if it is not an
	 * array.
	 * @since 2018/09/25
	 */
	@Api(ApiLevel.LEVEL_SQUIRRELJME_0_2_0_20181225)
	public static final native int arrayLength(Object __a);
	
	/**
	 * Creates a new array of the given type, this is the actual array and
	 * not the component.
	 *
	 * @param __t The array type, not the component type.
	 * @param __l The array length.
	 * @return An array allocated to the given length.
	 * @since 2018/09/25
	 */
	@Api(ApiLevel.LEVEL_SQUIRRELJME_0_2_0_20181225)
	public static final native Object arrayNew(Class<?> __t, int __l);
	
	/**
	 * Returns the class object for the specified class by its binary name.
	 *
	 * @param __s The class to lookup, the binary name is used.
	 * @return The class for the given binary name, or {@code null} if it
	 * does not exist.
	 * @since 2018/09/23 
	 */
	@Api(ApiLevel.LEVEL_SQUIRRELJME_0_2_0_20181225)
	public static final native Class<?> classByName(String __s);
	
	/**
	 * Returns the class data which is attached to the given class object.
	 *
	 * @param __cl The class to get the data from.
	 * @return The resulting class data.
	 * @since 2018/12/04
	 */
	@Api(ApiLevel.LEVEL_SQUIRRELJME_0_2_0_20181225)
	public static final native ClassData classData(Class<?> __cl);
	
	/**
	 * Returns the class object for the given object.
	 *
	 * @param __v The object to get the class of.
	 * @return The class of the given object, or {@code null} if it has no
	 * class.
	 * @since 2018/09/22
	 */
	@Api(ApiLevel.LEVEL_SQUIRRELJME_0_2_0_20181225)
	public static final native Class<?> classOf(Object __v);
	
	/**
	 * Checks if the given thread holds the given object in a lock.
	 *
	 * @param __ntid The native thread ID.
	 * @param __o The object to check.
	 * @return If the lock is held.
	 * @since 2018/11/21
	 */
	@Api(ApiLevel.LEVEL_SQUIRRELJME_0_2_0_20181225)
	public static final native boolean holdsLock(int __ntid, Object __o);
	
	/**
	 * Returns the identity hashcode of the object.
	 *
	 * @return The identity hashcode.
	 * @since 2018/10/14
	 */
	@Api(ApiLevel.LEVEL_SQUIRRELJME_0_2_0_20181225)
	public static final native int identityHashCode(Object __o);
	
	/**
	 * Invokes the specified static method.
	 *
	 * @param __m The method to invoke.
	 * @param __args Arguments to the method, the parameters will be passed
	 * as-is and will not be unboxed, so the method must accept boxed values.
	 * @return The value to return from the method, {@code void} will return
	 * {@code null}.
	 * @since 2018/11/20
	 */
	@Api(ApiLevel.LEVEL_SQUIRRELJME_0_2_0_20181225)
	public static final native Object invokeStatic(StaticMethod __m,
		Object... __args);
	
	/**
	 * Notifies threads waiting on the monitor.
	 *
	 * @param __o The object to notify.
	 * @param __all Notify all threads?
	 * @return If the monitor was a success or not.
	 * @since 2018/11/20
	 */
	@Api(ApiLevel.LEVEL_SQUIRRELJME_0_2_0_20181225)
	public static final native int monitorNotify(Object __o, boolean __all);
	
	/**
	 * Waits for a notification on a monitor.
	 *
	 * @param __o The object to wait on.
	 * @param __ms The milliseconds.
	 * @param __ns The nanoseconds.
	 * @return The wait status.
	 * @since 2018/11/21
	 */
	@Api(ApiLevel.LEVEL_SQUIRRELJME_0_2_0_20181225)
	public static final native int monitorWait(Object __o, long __ms,
		int __ns);
	
	/**
	 * Creates a new primitive weak reference. Note that it is not valid to
	 * operate on this object as a normal object, it is a special
	 * representation.
	 *
	 * @return The primitive weak reference.
	 * @since 2018/09/23
	 */
	@Api(ApiLevel.LEVEL_SQUIRRELJME_0_2_0_20181225)
	public static final native PrimitiveReference newWeakReference();
	
	/**
	 * Gets the given reference.
	 *
	 * @param __r The reference to read from.
	 * @return The reference value, may be {@code null} if the input reference
	 * is not valid, it was garbage collected, or it was never set.
	 * @since 2018/09/23
	 */
	@Api(ApiLevel.LEVEL_SQUIRRELJME_0_2_0_20181225)
	public static final native Object referenceGet(PrimitiveReference __r);
	
	/**
	 * Sets the given reference to the given value.
	 *
	 * @param __r The reference to set.
	 * @param __v The value to set.
	 * @since 2018/09/23
	 */
	@Api(ApiLevel.LEVEL_SQUIRRELJME_0_2_0_20181225)
	public static final native void referenceSet(PrimitiveReference __r,
		Object __v);
	
	/**
	 * Returns the class object for the specified class by its binary name.
	 *
	 * @param <C> The class to type this as.
	 * @param __s The class to lookup, the binary name is used.
	 * @return The class for the given binary name, or {@code null} if it
	 * does not exist.
	 * @since 2018/09/23 
	 */
	@SuppressWarnings({"unchecked"})
	public static final <C> Class<C> classByNameType(String __s)
	{
		return (Class<C>)((Object)ObjectAccess.classByName(__s));
	}
}

