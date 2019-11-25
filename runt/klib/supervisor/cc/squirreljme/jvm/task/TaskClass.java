// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.task;

import cc.squirreljme.jvm.Assembly;
import cc.squirreljme.jvm.Constants;
import cc.squirreljme.jvm.lib.ClassFieldsParser;
import cc.squirreljme.jvm.lib.ClassFileParser;
import cc.squirreljme.jvm.lib.ClassNameUtils;
import java.util.Objects;

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
	private int _poolpointer;
	
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
	 * @param __cip The parser for {@code ClassInfo}.
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
		
		// Debug
		todo.DEBUG.note("Loading class `%s`...", __cl);
		
		// We need the parser for class info so that we can initialize the
		// classes, however every variant of the loader can use this.
		ClassFileParser ciparser = __task.classInfoParser();
		
		// All branches require the info
		int infopointer = __task.allocator.allocateObject(
			Constants.OBJECT_BASE_SIZE + ciparser.fieldSize(false));
		this._infopointer = infopointer;
		
		// This object has the class type of ClassInfo so it must always point
		// to the ClassInfo instance of ClassInfo, however if we are loading
		// ClassInfo then we just use our own pointer
		Assembly.memWriteInt(infopointer, Constants.OBJECT_CLASS_OFFSET,
			(ClassNameUtils.isClassInfo(__cl) ? infopointer :
			__task.loadClass("cc/squirreljme/jvm/ClassInfo")._infopointer));
		
		// These objects should never be garbage collected because they
		// contain important class information!
		Assembly.memWriteInt(infopointer, Constants.OBJECT_COUNT_OFFSET,
			9999999);
		
		// If these are special classes, we need to handle them unique because
		// arrays and primitive types do not exist in any form as a class
		if (ClassNameUtils.isArray(__cl))
			return this.__initializeClassInfoArray(__task, __cl, ciparser);
		else if (ClassNameUtils.isPrimitiveType(__cl))
			return this.__initializeClassInfoPrimitive(__task, __cl, ciparser);
		
		// Otherwise initialize a standard class
		return this.__initializeClassInfoClass(__task, __cl, ciparser);
	}
	
	/**
	 * Allocates the constant pool data area.
	 *
	 * @param __task The task owning this.
	 * @param __cfp The class file parser for this class.
	 * @return The pointer to the pool area.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/11/25
	 */
	private final int __allocatePool(Task __task, ClassFileParser __cfp)
		throws NullPointerException
	{
		if (__task == null || __cfp == null)
			throw new NullPointerException("NARG");
		
		// Was this already allocated? Do not do it again
		int rv = this._poolpointer;
		if (rv != 0)
			return rv;
		
		// Allocate memory region
		rv = __task.allocator.allocatePool(__cfp.splitPool(true).count());
		this._poolpointer = rv;
		
		return rv;
	}
	
	/**
	 * Initializes an array class.
	 *
	 * @param __task The creating task.
	 * @param __cl The class name being initialized.
	 * @param __cip The parser for {@code ClassInfo}.
	 * @return {@code this}.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/11/09
	 */
	private final TaskClass __initializeClassInfoArray(Task __task,
		String __cl, ClassFileParser __cip)
		throws NullPointerException
	{
		if (__task == null || __cl == null || __cip == null)
			throw new NullPointerException("NARG");
		
		// We need to handle the component type
		TaskClass comptype = __task.loadClass(
			ClassNameUtils.componentType(__cl));
		
		throw new todo.TODO();
	}
	
	/**
	 * Initializes a standard type class.
	 *
	 * @param __task The creating task.
	 * @param __cl The class name being initialized.
	 * @param __cip The parser for {@code ClassInfo}.
	 * @return {@code this}.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/11/17
	 */
	private final TaskClass __initializeClassInfoClass(Task __task,
		String __cl, ClassFileParser __cip)
		throws NullPointerException
	{
		if (__task == null || __cl == null || __cip == null)
			throw new NullPointerException("NARG");
		
		// First we load the base class because it will contain information
		// about the class that we need
		ClassFileParser thisparser = __task.classpath.classParser(
			this.resourceindex);
		
		// Pointer to self
		int infopointer = this._infopointer;
		
		// The run-time pool is initialized later, but we need to allocate it
		// now!
		int poolpointer = this.__allocatePool(__task, thisparser);
		
		// Load super class if there is one
		String superclassname = Objects.toString(thisparser.superClassName(),
			null);
		TaskClass superclass = (superclassname == null ? null :
			__task.loadClass(superclassname));
		
		// Process every field that is defined within the information structure
		ClassFieldsParser cifs = __cip.fields(false);
		for (int cif = 0, cifn = cifs.count(); cif < cifn; cif++)
		{
			// Read field properties
			int ffl = cifs.flags(cif),
				fof = cifs.offset(cif),
				fsz = cifs.size(cif);
			
			// Determine where we write the data to!
			int wb = infopointer + Constants.OBJECT_BASE_SIZE;
			
			// Debug
			todo.DEBUG.note("fl=%sh of=%d sz=%d",
				Integer.toString(ffl, 16), fof, fsz);
			
			// Depends on the field name and type
			String nat = cifs.name(cif) + ":" + cifs.type(cif);
			switch (nat)
			{
					// Class object pointer, is created dynamically!
				case "classobjptr:java/lang/Class":
					Assembly.memWriteInt(wb, fof,
						0);
					break;
				
					// The component of this class
				case "componentclass:cc/squirreljme/jvm/ClassInfo":
					Assembly.memWriteInt(wb, fof,
						(!ClassNameUtils.isArray(__cl) ? 0 :
						__task.loadClass(ClassNameUtils.componentType(__cl)).
						_infopointer));
					break;
					
					// Super class of this class
				case "superclass:cc/squirreljme/jvm/ClassInfo":
					Assembly.memWriteInt(wb, fof,
						(superclass == null ? 0 : superclass._infopointer));
					break;
				
				default:
					throw new todo.TODO(nat);
			}
		}
		
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
	
	/**
	 * Initializes a primitive type class.
	 *
	 * @param __task The creating task.
	 * @param __cl The class name being initialized.
	 * @return {@code this}.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/11/09
	 */
	private final TaskClass __initializeClassInfoPrimitive(Task __task,
		String __cl, ClassFileParser __cip)
		throws NullPointerException
	{
		if (__task == null || __cl == null || __cip == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
}

