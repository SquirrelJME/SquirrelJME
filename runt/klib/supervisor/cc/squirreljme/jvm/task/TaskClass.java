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
import cc.squirreljme.jvm.io.BinaryBlob;
import cc.squirreljme.jvm.io.MemoryBlob;
import cc.squirreljme.jvm.lib.ClassFieldsParser;
import cc.squirreljme.jvm.lib.ClassFileParser;
import cc.squirreljme.jvm.lib.ClassInfoProperty;
import cc.squirreljme.jvm.lib.ClassInfoUtility;
import cc.squirreljme.jvm.lib.ClassMethodsParser;
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
	@Deprecated
	private int _poolpointer;
	
	/** Method VTables for this class. */
	@Deprecated
	private int _vtablemethodpointer;
	
	/** Pool VTables for this class. */
	@Deprecated
	private int _vtablepoolpointer;
	
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
		ClassInfoUtility ciutil = __task.classInfoUtility();
		
		// All branches require the info
		int infopointer = __task.allocator.allocateObject(
			ciutil.classInfoAllocationSize());
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
			return this.__initializeClassInfoArray(__task, __cl);
		else if (ClassNameUtils.isPrimitiveType(__cl))
			return this.__initializeClassInfoPrimitive(__task, __cl);
		
		// Otherwise initialize a standard class
		return this.__initializeClassInfoClass(__task, __cl);
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
	 * Builds the VTable for this class.
	 *
	 * @param __task The owning task.
	 * @param __cfp The class file parser for this class.
	 * @param __rvpool Should the pool be returned?
	 * @return The method or pool vtable array depending on {@code __rvpool}.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/11/28
	 */
	private final int __buildVTable(Task __task, ClassFileParser __cfp,
		boolean __rvpool)
		throws NullPointerException
	{
		if (__task == null)
			throw new NullPointerException("NARG");
		
		// Pre-cached?
		int vtmp = this._vtablemethodpointer,
			vtpp = this._vtablepoolpointer;
		if (vtmp > 0 && vtpp > 0)
			return (__rvpool ? vtpp : vtmp);
		
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
	private final TaskClass __initializeClassInfoArray(Task __task,
		String __cl)
		throws NullPointerException
	{
		if (__task == null || __cl == null)
			throw new NullPointerException("NARG");
		
		// We need to handle the component type
		TaskClass comptype = __task.loadClass(
			ClassNameUtils.componentType(__cl));
		
		throw new todo.TODO();
		
		/*
		INT_CELLSIZE
		CLASSINFO_COMPONENTCLASS
		INT_DIMENSIONS
		
		// If this is an array or primitive type, just use the vtables for
		// the Object class because these are purely virtual!
		if (ClassNameUtils.isArray(__cl) || ClassNameUtils.isPrimitive(__cl))
		{
			// Load object class
			TaskClass tc = __task.loadClass("java/lang/Object");
			
			// Just use these directly
			this._vtablepoolpointer = (vtmp = tc._vtablepoolpointer);
			this._vtablemethodpointer = (vtpp = tc._vtablemethodpointer);
			
			// Return whatever was requested
			return (__rvpool ? vtpp : vtmp);
		}*/
	}
	
	/**
	 * Initializes a standard type class.
	 *
	 * @param __task The creating task.
	 * @param __cl The class name being initialized.
	 * @return {@code this}.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/11/17
	 */
	private final TaskClass __initializeClassInfoClass(Task __task,
		String __cl)
		throws NullPointerException
	{
		if (__task == null || __cl == null)
			throw new NullPointerException("NARG");
		
		// We need the parser for class info so that we can initialize the
		// classes
		ClassInfoUtility ciutil = __task.classInfoUtility();
		
		// First we load the base class because it will contain information
		// about the class that we need
		ClassFileParser thisparser = __task.classpath.classParser(
			this.resourceindex);
		
		// Pointer to self
		int infopointer = this._infopointer;
		ciutil.setSelfPointer(this, infopointer);
		
		// Set pointer to the mini-class which may or may not be valid at all
		ciutil.setMiniClassPointer(this, thisparser.baseAddress());
		
		// Set magic number
		ciutil.setMagicNumber(this);
		
		// Set flags
		ciutil.setFlags(this, thisparser.flags());
		
		// Set self name
		BinaryBlob name = thisparser.thisNameAsBinaryBlob();
		if (name instanceof MemoryBlob)
			ciutil.setNamePointer(this, ((MemoryBlob)name).baseAddress() + 4);
		
		// Need to store the name elsewhere, since we do not have a direct
		// pointer to the name
		else
			throw new todo.TODO();
		
		// The run-time pool is initialized later, but we need to allocate it
		// now!
		int poolpointer = this.__allocatePool(__task, thisparser);
		ciutil.setPoolPointer(this, poolpointer);
		
		// Load super class if there is one
		String superclassname = Objects.toString(thisparser.superClassName(),
			null);
		TaskClass superclass = (superclassname == null ? null :
			__task.loadClass(superclassname));
		if (superclass != null)
			ciutil.setSuperClass(this, superclass);
		
		// The base for this class
		int basesize = (superclass == null ? 0 :
			ciutil.classAllocationSize(superclass));
		ciutil.setBaseSize(this, basesize);
		
		// Allocation size of this class
		ciutil.setClassAllocationSize(this,
			basesize + thisparser.fieldSize(false));
		
		// Set number of objects this has, for garbage collection
		ciutil.setObjectCount(this, thisparser.objectCount(false));
		
		/*
		CLASSINFO_ARRAY_INTERFACECLASSES
		INT_ARRAY_VTABLEVIRTUAL
		INT_ARRAY_VTABLEPOOL
		*/
		
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
		String __cl)
		throws NullPointerException
	{
		if (__task == null || __cl == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
}

