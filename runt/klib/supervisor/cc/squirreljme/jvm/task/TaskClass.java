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
import cc.squirreljme.jvm.lib.ClassPath;
import cc.squirreljme.jvm.lib.PoolClassName;
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
	 * Builds the VTable for this class.
	 *
	 * @param __task The owning task.
	 * @param __cfp The class file parser for this class.
	 * @param __rvpool Should the pool be returned?
	 * @return The method or pool vtable array depending on {@code __rvpool}.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/11/28
	 */
	@Deprecated
	private final int __buildVTable(Task __task, ClassFileParser __cfp,
		boolean __rvpool)
		throws NullPointerException
	{
		if (__task == null)
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
			this._vtpoolpointer = (vtmp = tc._vtpoolpointer);
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
		
		// Set the JAR index
		ciutil.setJarIndex(this, ClassPath.resourceIndexToJarIndex(
			this.resourceindex));
		
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
		int poolpointer = __task.allocator.allocatePool(
			thisparser.splitPool(true).count());
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
		
		// Number of methods the class has
		int methodcount = (superclass != null ? ciutil.
			methodCount(superclass) : 0) + thisparser.methodCount(false);
		ciutil.setMethodCount(this, methodcount);
		
		// Pre-allocate the class VTables since we may need to refer to them!
		int vtvirtual = __task.allocator.allocateArrayIntEmpty(methodcount),
			vtpool = __task.allocator.allocateArrayIntEmpty(methodcount);
		ciutil.setVTableVirtual(this, vtvirtual);
		ciutil.setVTablePool(this, vtpool);
		
		// Allocation size of this class
		ciutil.setClassAllocationSize(this,
			basesize + thisparser.fieldSize(false));
		
		// Set number of objects this has, for garbage collection
		ciutil.setObjectCount(this, thisparser.objectCount(false));
		
		// Initialize interfaces
		PoolClassName[] interfacenames = thisparser.interfaceNames();
		int numints = interfacenames.length;
		int[] ifps = new int[numints];
		for (int i = 0; i < numints; i++)
			ifps[i] = __task.loadClass(interfacenames[i].toString()).
				infoPointer();
		
		// Allocate and store
		int ifacespointer = __task.allocator.allocateArrayInt(ifps);
		ciutil.setInterfaces(this, ifacespointer);
		
		// Initialize the VTables for the class now
		if (true)
		{
			// INT_ARRAY_VTABLEVIRTUAL
			// INT_ARRAY_VTABLEPOOL
			throw new todo.TODO();
		}
		
		// Initialize the actual pool constants now
		if (true)
		{
			throw new todo.TODO();
		}
		
		// Set interfaces array type
		Assembly.memWriteInt(ifacespointer, Constants.OBJECT_CLASS_OFFSET,
			__task.loadClass("[Lcc/squirreljme/jvm/ClassInfo;").infoPointer());
		
		// Set the class type for the vtable array last, since everything
		// is now setup with it!
		TaskClass intarrayclass = __task.loadClass("[I");
		Assembly.memWriteInt(vtvirtual, Constants.OBJECT_CLASS_OFFSET,
			intarrayclass.infoPointer());
		Assembly.memWriteInt(vtpool, Constants.OBJECT_CLASS_OFFSET,
			intarrayclass.infoPointer());
		
		// All done! This class should hopefully work!
		return this;
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

