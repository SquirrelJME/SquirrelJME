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

/**
 * This contains accessors for object information.
 *
 * @since 2018/09/22
 */
@Deprecated
public final class ObjectAccess
{
	/** Monitor is not owned by this thread. */
	@Deprecated
	public static final int MONITOR_NOT_OWNED =
		-1;
	
	/** Monitor did not interrupt. */
	@Deprecated
	public static final int MONITOR_NOT_INTERRUPTED =
		0;
	
	/** Monitor did interrupt. */
	@Deprecated
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
	 * Returns the class data which is attached to the given class object.
	 *
	 * @param __cl The class to get the data from.
	 * @return The resulting class data.
	 * @since 2018/12/04
	 */
	@Deprecated
	@Api(ApiLevel.LEVEL_SQUIRRELJME_0_2_0_20181225)
	public static final native ClassData classData(Class<?> __cl);
	
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
	@Deprecated
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
	@Deprecated
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
	@Deprecated
	@Api(ApiLevel.LEVEL_SQUIRRELJME_0_2_0_20181225)
	public static final native int monitorWait(Object __o, long __ms,
		int __ns);
}

