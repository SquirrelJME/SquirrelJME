// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.task;

import cc.squirreljme.jvm.lib.ClassNameUtils;

/**
 * This represents a class of a task.
 *
 * @since 2019/10/19
 */
public final class TaskClass
{
	/** The index of the class in the resource table. */
	protected final int resourceindex;
	
	/** The allocated class information. */
	private int _infopointer;
	
	/** The run-time constant pool pointer. */
	private int _pool;
	
	/**
	 * Initializes the class container.
	 *
	 * @param __cldx The class path resource index.
	 * @since 2019/10/19
	 */
	public TaskClass(int __cldx)
	{
		this.resourceindex = __cldx;
	}
	
	/**
	 * Returns the info pointer.
	 *
	 * @return The info pointer.
	 * @throws TaskVirtualMachineError If the info pointer was not set.
	 * @since 2019/10/27
	 */
	public final int infoPointer()
		throws TaskVirtualMachineError
	{
		int rv = this._infopointer;
		
		// {@squirreljme.error SV0n Class information pointer not set.}
		if (rv == 0)
			throw new TaskVirtualMachineError("SV0n");
		
		return rv;
	}
	
	/**
	 * Performs the main initialization of this class.
	 *
	 * @param __task The creating task.
	 * @param __cl The class name being initialized.
	 * @return {@code this}.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/10/27
	 */
	public final TaskClass initializeClassInfo(Task __task, String __cl)
		throws NullPointerException
	{
		if (__task == null || __cl == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * Initializes an array class.
	 *
	 * @param __task The creating task.
	 * @param __cl The class name being initialized.
	 * @return {@code this}.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/11/09
	 */
	public final TaskClass initializeClassInfoArray(Task __task, String __cl)
		throws NullPointerException
	{
		if (__task == null || __cl == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * Initializes a primitive type class.
	 *
	 * @param __task The creating task.
	 * @param __cl The class name being initialized.
	 * @return {@code this}.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/11/09
	 */
	public final TaskClass initializeClassInfoPrimitive(Task __task,
		String __cl)
		throws NullPointerException
	{
		if (__task == null || __cl == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * Performs enough setup of the class info to rever to it when the
	 * class table is not locked.
	 *
	 * @param __task The creating task.
	 * @param __cl The class name being initialized.
	 * @return {@code this}.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/10/27
	 */
	public final TaskClass initializeClassInfoSetup(Task __task, String __cl)
		throws NullPointerException
	{
		if (__task == null || __cl == null)
			throw new NullPointerException("NARG");
		
		// If these are special classes, we need to handle them unique because
		// arrays and primitive types do not exist in any form as a class
		if (ClassNameUtils.isArray(__cl))
			return this.initializeClassInfoArray(__task, __cl);
		else if (ClassNameUtils.isPrimitiveType(__cl))
			return this.initializeClassInfoPrimitive(__task, __cl);
		
		throw new todo.TODO();
		
		/*
		// {@squirreljme.error SV0l Task does not have ClassInfo in its
		// class path.}
		int cidx = classpath.resourceClassFind("cc/squirreljme/jvm/ClassInfo");
		if (cidx < 0)
			throw new TaskVirtualMachineError("SV0l");
		
		// Get parser for the class info, because we need its info
		ClassFileParser ciparser = new ClassFileParser(
			classpath.resourceData(cidx));
		
		// Need to allocate class data
		TaskAllocator allocator = this.allocator;
		
		// Allocate the space needed to store the class information
		int infopointer = allocator.allocateObject(Constants.OBJECT_BASE_SIZE +
			ciparser.fieldSize(false));
		rv._infopointer = infopointer;
		
		throw new todo.TODO();
		*/
	}
}

