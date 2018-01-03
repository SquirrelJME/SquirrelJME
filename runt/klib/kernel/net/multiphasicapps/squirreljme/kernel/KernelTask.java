// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.kernel;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import net.multiphasicapps.squirreljme.runtime.clsyscall.PacketTypes;
import net.multiphasicapps.squirreljme.runtime.packets.Packet;
import net.multiphasicapps.squirreljme.runtime.packets.PacketStream;

/**
 * This represents a task which is running within SquirrelJME.
 *
 * @since 2017/12/10
 */
public abstract class KernelTask
{
	/** Reference to the owning task manager. */
	protected final Reference<KernelTasks> tasks;
	
	/** The task index. */
	protected final int index;
	
	/** Packet stream to use when communicating with the client. */
	protected final PacketStream stream;
	
	/** Permissions granted by the kernel. */
	private volatile int _simpleperms;
	
	/** Got hello from the client? */
	volatile boolean _gothello;
	
	/** Got initialization complete from task? */
	volatile boolean _gotinitcomplete;
	
	/**
	 * Initializes the task with the initial basic permissions and the
	 * streams used for the packet interface.
	 *
	 * @param __ref Reference to the owning task manager, this may be null if
	 * this is the system task.
	 * @param __dx The index of the task.
	 * @param __sp The basic permissions to start with.
	 * @param __in The input stream from the process.
	 * @param __out The output steam to the process.
	 * @throws NullPointerException If this is not the system task and the
	 * streams were not specified.
	 * @since 2017/12/12
	 */
	public KernelTask(Reference<KernelTasks> __ref, int __dx, int __sp,
		InputStream __in, OutputStream __out)
		throws NullPointerException
	{
		if (__dx != 0 && (__ref == null || __in == null || __out == null))
			throw new NullPointerException("NARG");
		
		this.tasks = __ref;
		this.index = __dx;
		this._simpleperms = __sp;
		
		// The kernel process does not have a packet stream because everything
		// is always local
		PacketStream stream = (__dx == 0 ? null : new PacketStream(__in, __out,
			new __TaskResponseHandler__(new WeakReference<>(this))));;
		this.stream = stream;
		
		// If this is the kernel task, set these always
		if (__dx == 0)
		{
			this._gothello = true;
			this._gotinitcomplete = true;
		}
		
		// Send hello packet to the other end
		else
			try (Packet p = stream.farm().create(PacketTypes.HELLO, 0))
			{
				stream.send(p);
			}
	}
	
	/**
	 * Returns the current flags for the task.
	 *
	 * @return The task flags.
	 * @since 2017/12/27
	 */
	protected abstract int accessFlags();
	
	/**
	 * Obtains the specified metric.
	 *
	 * @param __m The metric to obtain.
	 * @return The value of the given metric or {@code Long#MIN_VALUE} if it is
	 * not known.
	 * @since 2017/12/27
	 */
	protected abstract long accessMetric(int __m);
	
	/**
	 * This is called when the task has been terminated.
	 *
	 * @since 2018/01/01
	 */
	protected abstract void accessTerminated();
	
	/**
	 * {@inheritDoc}
	 * @since 2017/12/27
	 */
	@Override
	public final boolean equals(Object __o)
	{
		return this == __o;
	}
	
	/**
	 * Returns the flags which represent the current task.
	 *
	 * @param __by The task requesting the flags.
	 * @return The task flags.
	 * @throws NullPointerException On null arguments.
	 * @throws SecurityException If getting the task flags is not permitted.
	 * @since 2017/12/27
	 */
	public final int flags(KernelTask __by)
		throws NullPointerException, SecurityException
	{
		if (__by == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error ZZ0l The specified task is not permitted to
		// obtain the task flags. (The task requesting the task flags)}
		if (!__by.hasSimplePermissions(__by,
			KernelSimplePermission.GET_TASK_PROPERTY))
			throw new SecurityException(
				String.format("ZZ0l %s", __by));
		
		return this.accessFlags();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/12/27
	 */
	@Override
	public final int hashCode()
	{
		return this.index;
	}
	
	/**
	 * Checks whether all of the given permissions are set.
	 *
	 * @param __by The task which is requesting this task's permissions.
	 * @param __mask The permission mask to check.
	 * @return {@code true} if all permissions are set.
	 * @throws IllegalArgumentException If the mask is empty.
	 * @throws NullPointerException On null arguments.
	 * @throws SecurityException If this operation is not permitted.
	 * @since 2017/12/12
	 */
	public final boolean hasSimplePermissions(KernelTask __by, int __mask)
		throws IllegalArgumentException, NullPointerException,
			SecurityException
	{
		if (__by == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error ZZ0e Cannot get permissions with an empty mask.}
		if (__mask == 0)
			throw new IllegalArgumentException("ZZ0e");
		
		// {@squirreljme.error ZZ0c Permissions cannot be read from this task
		// by the requesting task. (This task; The requesting task)}
		// Prevent infinite recursion by allowing the current task to always
		// get permissions
		if (this != __by && !__by.hasSimplePermissions(__by,
			KernelSimplePermission.GET_SIMPLE_PERMISSION))
			throw new SecurityException(
				String.format("ZZ0c %s %s", this, __by));
		
		return (__mask == (this._simpleperms & __mask));
	}
	
	/**
	 * Returns the index of this task.
	 *
	 * @return The task index.
	 * @since 2017/12/27
	 */
	public final int index()
	{
		return this.index;
	}
	
	/**
	 * Checks if the task has finished initializing and it succeeded.
	 *
	 * @return {@code true} if initialization completed.
	 * @since 2018/01/01
	 */
	public final boolean isInitializationComplete()
	{
		return this._gotinitcomplete;
	}
	
	/**
	 * Obtains a given metric for the given task.
	 *
	 * @param __by The task reading the metric.
	 * @param __metric The metric to read.
	 * @return The value of the given metric or {@code Long#MIN_VALUE} if it is
	 * not known.
	 * @throws NullPointerException On null arguments.
	 * @throws SecurityException If the specified task cannot read metrics.
	 * @since 2017/12/27
	 */
	public final long metric(KernelTask __by, int __metric)
		throws NullPointerException, SecurityException
	{
		if (__by == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error ZZ0m The specified task is not permitted to
		// obtain the task metrics. (The task requesting the task metrics)}
		if (!__by.hasSimplePermissions(__by,
			KernelSimplePermission.GET_TASK_PROPERTY))
			throw new SecurityException(
				String.format("ZZ0m %s", __by));
		
		return this.accessMetric(__metric);
	}
	
	/**
	 * Sets the simple permissions for a task.
	 *
	 * @param __by The task which is setting permissions.
	 * @param __mask The permission mask.
	 * @param __enable Whether to enable or disable a given permission. 
	 * @return The old state of the permission.
	 * @throws IllegalArgumentException If the mask is empty or contains
	 * more than one bit.
	 * @throws NullPointerException On null arguments.
	 * @throws SecurityException If this operation is not permitted.
	 * @since 2017/12/12
	 */
	public final boolean setSimplePermission(KernelTask __by, int __mask,
		boolean __enable)
		throws IllegalArgumentException, NullPointerException,
			SecurityException
	{
		if (__by == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error ZZ0d Cannot set permissions with an empty mask
		// or with multiple bits.}
		if (__mask == 0 || Integer.bitCount(__mask) != 1)
			throw new IllegalArgumentException("ZZ0d");
		
		// {@squirreljme.error ZZ0b Permissions cannot be set on this task by
		// the requested task. (This task; The requesting task)}
		if (!__by.hasSimplePermissions(__by,
			KernelSimplePermission.SET_SIMPLE_PERMISSION))
			throw new SecurityException(
				String.format("ZZ0b %s %s", this, __by));
		
		// Set new permissions
		int simpleperms = this._simpleperms;
		boolean was = (__mask == (simpleperms & __mask));
		if (__enable)
			simpleperms |= __mask;
		else
			simpleperms &= (~__mask);
		this._simpleperms = simpleperms;
		
		return was;
	}
	
	/**
	 * Returns the simple permissions of the task.
	 *
	 * @return The task simple permissions.
	 * @since 2017/12/31
	 */
	final int __simplePermissions()
	{
		return this._simpleperms;
	}
	
	/**
	 * Returns the task manager.
	 *
	 * @return The task manager.
	 * @throws RuntimeException If it was garbage collected.
	 * @since 2018/01/01
	 */
	final KernelTasks __tasks()
		throws RuntimeException
	{
		// {@squirreljme.error AP0f The task manager was garbage collected.}
		KernelTasks rv = this.tasks.get();
		if (rv == null)
			throw new RuntimeException("AP0f");
		return rv;
	}
	
	/**
	 * This is called when the task has been terminated and that it should
	 * be removed from the kernel.
	 *
	 * @since 2017/08/01
	 */
	final void __terminated()
	{
		// Tell the task itself it was terminated
		this.accessTerminated();
		
		// Then tell the task manager the task is gone now
		this.__tasks().__terminated(this);
	}
}

