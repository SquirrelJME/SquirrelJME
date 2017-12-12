// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.runtime.kernel;

/**
 * This represents a task which is running within SquirrelJME.
 *
 * @since 2017/12/10
 */
public abstract class KernelTask
{
	/** Permissions granted by the kernel. */
	private volatile int _simpleperms;
	
	/**
	 * Default constructor.
	 *
	 * @since 2017/12/12
	 */
	public KernelTask()
	{
	}
	
	/**
	 * Initializes the task with the initial basic permissions.
	 *
	 * @param __sp The basic permissions to start with.
	 * @since 2017/12/12
	 */
	public KernelTask(int __sp)
	{
		this._simpleperms = __sp;
	}
	
	/**
	 * Checks whether all of the given permissions are set.
	 *
	 * @param __task The task which is requesting this task's permissions.
	 * @param __mask The permission mask to check.
	 * @return {@code true} if all permissions are set.
	 * @throws IllegalArgumentException If the mask is empty.
	 * @throws NullPointerException On null arguments.
	 * @throws SecurityException If this operation is not permitted.
	 * @since 2017/12/12
	 */
	public final boolean hasSimplePermissions(KernelTask __task, int __mask)
		throws IllegalArgumentException, NullPointerException,
			SecurityException
	{
		if (__task == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error ZZ0e Cannot get permissions with an empty mask.}
		if (__mask == 0)
			throw new IllegalArgumentException("ZZ0e");
		
		// {@squirreljme.error ZZ0c Permissions cannot be read from this task
		// by the requesting task. (This task; The requesting task)}
		// Prevent infinite recursion by allowing the current task to always
		// get permissions
		if (this != __task && !__task.hasSimplePermissions(__task,
			KernelSimplePermission.GET_SIMPLE_PERMISSION))
			throw new SecurityException(
				String.format("ZZ0c %s %s", this, __task));
		
		return (__mask == (this._simpleperms & __mask));
	}
	
	/**
	 * Sets the simple permissions for a task.
	 *
	 * @param __task The task which is setting permissions.
	 * @param __mask The permission mask.
	 * @param __enable Whether to enable or disable a given permission. 
	 * @return The old state of the permission.
	 * @throws IllegalArgumentException If the mask is empty or contains
	 * more than one bit.
	 * @throws NullPointerException On null arguments.
	 * @throws SecurityException If this operation is not permitted.
	 * @since 2017/12/12
	 */
	public final boolean setSimplePermission(KernelTask __task, int __mask,
		boolean __enable)
		throws IllegalArgumentException, NullPointerException,
			SecurityException
	{
		if (__task == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error ZZ0d Cannot set permissions with an empty mask
		// or with multiple bits.}
		if (__mask == 0 || Integer.bitCount(__mask) != 1)
			throw new IllegalArgumentException("ZZ0d");
		
		// {@squirreljme.error ZZ0b Permissions cannot be set on this task by
		// the requested task. (This task; The requesting task)}
		if (!__task.hasSimplePermissions(__task,
			KernelSimplePermission.SET_SIMPLE_PERMISSION))
			throw new SecurityException(
				String.format("ZZ0b %s %s", this, __task));
		
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
}

