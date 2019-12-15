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
import cc.squirreljme.jvm.lib.ClassDualPoolParser;
import cc.squirreljme.jvm.lib.ClassFieldsParser;
import cc.squirreljme.jvm.lib.ClassFileParser;
import cc.squirreljme.jvm.lib.ClassInfoProperty;
import cc.squirreljme.jvm.lib.ClassInfoUtility;
import cc.squirreljme.jvm.lib.ClassMethodsParser;
import cc.squirreljme.jvm.lib.ClassNameUtils;
import cc.squirreljme.jvm.lib.ClassPath;
import cc.squirreljme.jvm.lib.ClassPoolConstants;
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
		
		// Right now nothing needs to actually be done here
		return this;
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
	 * Builds the constant pool for the given class.
	 *
	 * @param __task The owning task.
	 * @param __ciu The class info utility.
	 * @param __cfp The class file parser for this class.
	 * @param __poolp The pool pointer.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/12/08
	 */
	private final void __buildPool(Task __task, ClassInfoUtility __ciu,
		ClassFileParser __cfp, int __poolp)
		throws NullPointerException
	{
		if (__task == null || __ciu == null || __cfp == null)
			throw new NullPointerException("NARG");
		
		// Initialize individual entries
		ClassDualPoolParser pool = __cfp.pool();
		for (int i = 1, n = pool.count(true); i < n; i++)
		{
			// The value to write into the slot
			int slotv;
			
			// Depends on the type
			int type = pool.entryType(true, i);
			switch (type)
			{
					// A class information pointer
				case ClassPoolConstants.TYPE_CLASS_INFO_POINTER:
					String cip = pool.entryAsClassInfoPointer(true, i).
						toString();
					
					// The given class may be deferred loaded in which case
					// we do not really care about it right now
					slotv = (ClassNameUtils.isDeferredLoad(cip) ? 0 :
						__task.loadClass(cip)._infopointer);
					break;
					
					// A string which as been noted, not interned
				case ClassPoolConstants.TYPE_NOTED_STRING:
					slotv = ((MemoryBlob)pool.entryAsNotedString(true, i).
						blob()).baseAddress() + 4;
					break;
				
				default:
					todo.DEBUG.note("TODO -- Load of pool type %d?", type);
					continue;
			}
			
			// Store slot value
			Assembly.memWriteInt(__poolp, i * Constants.POOL_CELL_SIZE, slotv);
		}
	}
	
	/**
	 * Builds the VTable for this class.
	 *
	 * @param __task The owning task.
	 * @param __ciu The class info utility.
	 * @param __cfp The class file parser for this class.
	 * @param __vtvirtual The virtual method pointer table.
	 * @param __vtpool The pool used for that method.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/11/28
	 */
	private final void __buildVTable(Task __task, ClassInfoUtility __ciu,
		ClassFileParser __cfp, int __vtvirtual, int __vtpool)
		throws NullPointerException
	{
		if (__task == null || __ciu == null || __cfp == null)
			throw new NullPointerException("NARG");
		
		todo.DEBUG.note("TODO -- Build VTables.");
		//throw new todo.TODO();
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
		
		// We just need the utility to access the class info
		ClassInfoUtility ciutil = __task.classInfoUtility();
		
		// Always points to self
		ciutil.setSelfPointer(this, this._infopointer);
		
		// The size is always the base array size
		ciutil.setClassAllocationSize(this, Constants.ARRAY_BASE_SIZE);
		
		// The base of this class is always after object
		ciutil.setBaseSize(this, Constants.OBJECT_BASE_SIZE);
		
		// The number of dimensions this class has
		ciutil.setDimensions(this, ClassNameUtils.dimensions(__cl));
		
		// Set name of our class
		ciutil.setNamePointer(this, this.__makeString(__task, __cl));
		
		// The class depth is always one because this extends object
		ciutil.setClassDepth(this, 1);
		
		// The super-class is always object, so load that and set
		TaskClass object = __task.loadClass("java/lang/Object");
		ciutil.setSuperClass(this, object);
		
		// The VTables, pools, and method counts always use Object's
		ciutil.setVTableVirtual(this, ciutil.vTableVirtual(object));
		ciutil.setVTablePool(this, ciutil.vTablePool(object));
		ciutil.setPoolPointer(this, ciutil.poolPointer(object));
		ciutil.setMethodCount(this, ciutil.methodCount(object));
		
		// We need to handle the component type
		TaskClass comptype = __task.loadClass(
			ClassNameUtils.componentType(__cl));
		
		// Is this component type a primitive?
		boolean compisprim =
			(ciutil.flags(comptype) & Constants.CIF_IS_PRIMITIVE) != 0;
		
		// Set our own flags, note that if our component type is not primitive
		// it is a bunch of objects which is needed for garbage collection
		// of object arrays
		ciutil.setFlags(this, Constants.CIF_IS_ARRAY |
			(compisprim ? 0 : Constants.CIF_IS_ARRAY_OF_OBJECTS));
		
		// The component class is set here
		ciutil.setComponentType(this, comptype);
		
		// The JAR this came from is always the component's JAR
		ciutil.setJarIndex(this, ciutil.jarIndex(comptype));
		
		// The cell size is always 4 unless this is a primitive
		ciutil.setCellSize(this, (compisprim ?
			ciutil.classAllocationSize(comptype) : 4));
		
		// Done
		return this;
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
		
		// Set flags, note that these are not class flags but VM flags
		ciutil.setFlags(this, 0);
		
		// Set self name
		BinaryBlob name = thisparser.thisNameAsBinaryBlob();
		if (name instanceof MemoryBlob)
			ciutil.setNamePointer(this, ((MemoryBlob)name).baseAddress() + 4);
		
		// Need to store the name elsewhere, since we do not have a direct
		// pointer to the name
		else
			ciutil.setNamePointer(this, this.__makeString(__task, __cl));
		
		// The run-time pool is initialized later, but we need to allocate it
		// now!
		int poolpointer = __task.allocator.allocatePool(
			thisparser.splitPool(true).count());
		ciutil.setPoolPointer(this, poolpointer);
		
		// Allocate static field space
		todo.DEBUG.note("TODO -- Allocate static field space.");
		
		// Set default constructor
		BinaryBlob defnew = thisparser.methodCodeBytes("<init>", "()V");
		if (defnew != null)
			ciutil.setDefaultNew(this, ((MemoryBlob)defnew).baseAddress());
		
		// Load super class if there is one
		String superclassname = Objects.toString(thisparser.superClassName(),
			null);
		TaskClass superclass = (superclassname == null ? null :
			__task.loadClass(superclassname));
		if (superclass != null)
			ciutil.setSuperClass(this, superclass);
		
		// Set the class depth
		int classdepth = (superclass == null ? 0 :
			ciutil.classDepth(superclass) + 1);
		ciutil.setClassDepth(this, classdepth);
		
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
		
		// Initialize the VTables for the class now, it is a bit complicated
		// so it is in another method
		this.__buildVTable(__task, ciutil, thisparser, vtvirtual, vtpool);
		
		// Initialize the actual pool constants now
		this.__buildPool(__task, ciutil, thisparser, poolpointer);
		
		// Load static field constant values
		todo.DEBUG.note("TODO -- Load static field constant values.");
		
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
		
		// Call static initializer for class, if one exists
		BinaryBlob clinit = thisparser.methodCodeBytes("<clinit>", "()V");
		if (clinit != null)
			__task.contextThread().execute(((MemoryBlob)clinit).baseAddress(),
				poolpointer);
		
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
		
		// We just need the utility to access the class info
		ClassInfoUtility ciutil = __task.classInfoUtility();
		
		// Always points to self
		ciutil.setSelfPointer(this, this._infopointer);
		
		// Determine the size of this type
		int size;
		switch (__cl)
		{
			case "boolean":
			case "byte":
				size = 1;
				break;
			
			case "short":
			case "char":
				size = 2;
				break;
			
			case "int":
			case "float":
				size = 4;
				break;
			
			case "long":
			case "double":
				size = 8;
				break;
			
				// {@squirreljme.error SV0z Invalid primitive type.}
			default:
				throw new RuntimeException("SV0z");
		}
		
		// Set as primitive
		ciutil.setFlags(this, Constants.CIF_IS_PRIMITIVE);
		
		// Set size of type
		ciutil.setClassAllocationSize(this, size);
		ciutil.setCellSize(this, size);
		
		// Set name of our class
		ciutil.setNamePointer(this, this.__makeString(__task, __cl));
		
		// Comes from no JAR so is invalid
		ciutil.setJarIndex(this, -1);
		
		// The VTables, pools, and method counts always use Object's, even
		// if it is not the super-class of object
		TaskClass object = __task.loadClass("java/lang/Object");
		ciutil.setVTableVirtual(this, ciutil.vTableVirtual(object));
		ciutil.setVTablePool(this, ciutil.vTablePool(object));
		ciutil.setPoolPointer(this, ciutil.poolPointer(object));
		ciutil.setMethodCount(this, ciutil.methodCount(object));
		
		// Done
		return this;
	}
	
	/**
	 * Makes a UTF-8 string of the given string.
	 *
	 * @param __task The task.
	 * @param __s The string to encode.
	 * @return The raw string pointer.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/12/08
	 */
	private final int __makeString(Task __task, String __s)
		throws NullPointerException
	{
		if (__task == null || __s == null)
			throw new NullPointerException("NARG");
		
		// Count the number of bytes this will take up
		int bytes = 2;
		for (int i = 0, n = __s.length(); i < n; i++)
		{
			char c = __s.charAt(i);
			
			if (c >= 0x0001 && c <= 0x007F)
				bytes += 1;
			else if (c == 0x0000 || (c >= 0x0080 && c <= 0x07FF))
				bytes += 2;
			else
				bytes += 3;
		}
		
		// Allocate and set the length of the string
		int rv = __task.allocator.allocate(0, bytes);
		Assembly.memWriteJavaShort(rv, 0, bytes);
		
		// Write character data within
		int base = rv + 2;
		for (int i = 0, o = 0, n = __s.length(); i < n; i++)
		{
			char c = __s.charAt(i);
			
			// Single byte
			if (c >= 0x0001 && c <= 0x007F)
				Assembly.memWriteByte(base, o++, c);
			
			// Two byte
			else if (c == 0x0000 || (c >= 0x0080 && c <= 0x07FF))
			{
				Assembly.memWriteByte(base, o++, 0b110_00000 |
					((c >>> 6) & 0b11111));
				Assembly.memWriteByte(base, o++, 0b10_000000 |
					(c & 0b111111));
			}
			
			// Three byte
			else
			{
				Assembly.memWriteByte(base, o++, 0b1110_0000 |
					(c >>> 12) & 0b1111);
				Assembly.memWriteByte(base, o++, 0b10_000000 |
					((c >>> 6) & 0b111111));
				Assembly.memWriteByte(base, o++, 0b10_000000 |
					(c & 0b111111));
			}
		}
		
		// All done!
		return rv;
	}
}

