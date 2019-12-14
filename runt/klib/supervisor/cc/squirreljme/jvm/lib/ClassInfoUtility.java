// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.lib;

import cc.squirreljme.jvm.Assembly;
import cc.squirreljme.jvm.Constants;
import cc.squirreljme.jvm.task.TaskClass;

/**
 * This is a utility which allows access to the various fields within
 * {@link cc.squirreljme.jvm.ClassInfo}.
 *
 * @since 2019/11/30
 */
public final class ClassInfoUtility
{
	/** The allocation size. */
	protected final int allocationsize;
	
	/** Class info properties. */
	protected final int[] properties;
	
	/**
	 * Initializes the class info utility.
	 *
	 * @param __as The allocation size.
	 * @param __props Class info properties.
	 * @since 2019/11/30
	 */
	public ClassInfoUtility(int __as, int[] __props)
	{
		this.allocationsize = __as;
		
		int[] properties = new int[ClassInfoProperty.NUM_PROPERTIES];
		for (int i = 0, lim = Math.min(__props.length,
			ClassInfoProperty.NUM_PROPERTIES); i < lim; i++)
			properties[i] = __props[i];
		this.properties = properties;
	}
	
	/**
	 * Returns the class allocation size.
	 *
	 * @param __cl The class.
	 * @return The class allocation size.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/12/01
	 */
	public final int classAllocationSize(TaskClass __cl)
		throws NullPointerException
	{
		return this.property(__cl, ClassInfoProperty.INT_SIZE);
	}
	
	/**
	 * Returns the class depth.
	 *
	 * @param __cl The class.
	 * @return The class depth.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/12/01
	 */
	public final int classDepth(TaskClass __cl)
		throws NullPointerException
	{
		return this.property(__cl, ClassInfoProperty.INT_CLASSDEPTH);
	}
	
	/**
	 * Returns the allocation size of instances of this class.
	 *
	 * @return The allocation size of this.
	 * @since 2019/11/30
	 */
	public final int classInfoAllocationSize()
	{
		return this.allocationsize;
	}
	
	/**
	 * Returns the ClassInfo flags.
	 *
	 * @param __cl The class to get.
	 * @return The resulting value.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/12/08
	 */
	public final int flags(TaskClass __cl)
		throws NullPointerException
	{
		return this.property(__cl, ClassInfoProperty.INT_FLAGS);
	}
	
	/**
	 * Returns the JAR Index.
	 *
	 * @param __cl The class to get.
	 * @return The resulting value.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/12/08
	 */
	public final int jarIndex(TaskClass __cl)
		throws NullPointerException
	{
		return this.property(__cl, ClassInfoProperty.INT_JARDX);
	}
	
	/**
	 * Returns the method count of the class.
	 *
	 * @param __cl The class.
	 * @return The class method count.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/12/01
	 */
	public final int methodCount(TaskClass __cl)
		throws NullPointerException
	{
		return this.property(__cl, ClassInfoProperty.INT_NUMMETHODS);
	}
	
	/**
	 * Returns the object count of the class.
	 *
	 * @param __cl The class.
	 * @return The class object count.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/12/01
	 */
	public final int objectCount(TaskClass __cl)
		throws NullPointerException
	{
		return this.property(__cl, ClassInfoProperty.INT_NUMOBJECTS);
	}
	
	/**
	 * Returns the pool pointer.
	 *
	 * @param __cl The class to get.
	 * @return The resulting value.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/12/08
	 */
	public final int poolPointer(TaskClass __cl)
		throws NullPointerException
	{
		return this.property(__cl, ClassInfoProperty.INT_POOL);
	}
	
	/**
	 * Returns the specified property.
	 *
	 * @param __cl The class.
	 * @param __prop The property to return.
	 * @return The property value.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/12/01
	 */
	public final int property(TaskClass __cl, int __prop)
		throws NullPointerException
	{
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		return Assembly.memReadInt(__cl.infoPointer(),
			this.properties[__prop]);
	}
	
	/**
	 * Sets the base of the class.
	 *
	 * @param __cl The class.
	 * @param __v The value to set.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/12/01
	 */
	public final void setBaseSize(TaskClass __cl, int __v)
		throws NullPointerException
	{
		this.setProperty(__cl, ClassInfoProperty.INT_BASE,
			__v);
	}
	
	/**
	 * Sets the cell size.
	 *
	 * @param __cl The class.
	 * @param __v The value to set.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/12/08
	 */
	public final void setCellSize(TaskClass __cl, int __v)
		throws NullPointerException
	{
		this.setProperty(__cl, ClassInfoProperty.INT_CELLSIZE,
			__v);
	}
	
	/**
	 * Sets the allocation size.
	 *
	 * @param __cl The class.
	 * @param __v The value to set.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/12/01
	 */
	public final void setClassAllocationSize(TaskClass __cl, int __v)
		throws NullPointerException
	{
		this.setProperty(__cl, ClassInfoProperty.INT_SIZE,
			__v);
	}
	
	/**
	 * Sets the class depth, the number of super classes ahead of this.
	 *
	 * @param __cl The class.
	 * @param __v The value to set.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/12/01
	 */
	public final void setClassDepth(TaskClass __cl, int __v)
		throws NullPointerException
	{
		this.setProperty(__cl, ClassInfoProperty.INT_CLASSDEPTH,
			__v);
	}
	
	/**
	 * Sets the component type class.
	 *
	 * @param __cl The class.
	 * @param __co The component type.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/12/08
	 */
	public final void setComponentType(TaskClass __cl, TaskClass __co)
		throws NullPointerException
	{
		if (__co == null)
			throw new NullPointerException("NARG");
		
		this.setProperty(__cl, ClassInfoProperty.CLASSINFO_COMPONENTCLASS,
			__co.infoPointer());
	}
	
	/**
	 * Sets the number of dimensions the class has.
	 *
	 * @param __cl The class.
	 * @param __v The value to set.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/12/08
	 */
	public final void setDimensions(TaskClass __cl, int __v)
		throws NullPointerException
	{
		this.setProperty(__cl, ClassInfoProperty.INT_DIMENSIONS,
			__v);
	}
	
	/**
	 * Sets the flags.
	 *
	 * @param __cl The class.
	 * @param __v The value to set.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/12/01
	 */
	public final void setFlags(TaskClass __cl, int __v)
		throws NullPointerException
	{
		this.setProperty(__cl, ClassInfoProperty.INT_FLAGS,
			__v);
	}
	
	/**
	 * Sets the interfaces array of the class.
	 *
	 * @param __cl The class.
	 * @param __v The value to set.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/12/01
	 */
	public final void setInterfaces(TaskClass __cl, int __v)
		throws NullPointerException
	{
		this.setProperty(__cl,
			ClassInfoProperty.CLASSINFO_ARRAY_INTERFACECLASSES, __v);
	}
	
	/**
	 * Sets the JAR index.
	 *
	 * @param __cl The class.
	 * @param __v The value to set.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/12/01
	 */
	public final void setJarIndex(TaskClass __cl, int __v)
		throws NullPointerException
	{
		this.setProperty(__cl, ClassInfoProperty.INT_JARDX,
			__v);
	}
	
	/**
	 * Sets the class magic number.
	 *
	 * @param __cl The class.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/12/01
	 */
	public final void setMagicNumber(TaskClass __cl)
		throws NullPointerException
	{
		this.setProperty(__cl, ClassInfoProperty.INT_MAGIC,
			ClassFileConstants.MAGIC_NUMBER);
	}
	
	/**
	 * Sets the method count.
	 *
	 * @param __cl The class.
	 * @param __v The value to set.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/12/01
	 */
	public final void setMethodCount(TaskClass __cl, int __v)
		throws NullPointerException
	{
		this.setProperty(__cl, ClassInfoProperty.INT_NUMMETHODS,
			__v);
	}
	
	/**
	 * Sets the mini class pointer.
	 *
	 * @param __cl The class.
	 * @param __v The value to set.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/12/01
	 */
	public final void setMiniClassPointer(TaskClass __cl, int __v)
		throws NullPointerException
	{
		this.setProperty(__cl, ClassInfoProperty.INT_MINIPTR,
			__v);
	}
	
	/**
	 * Sets the name pointer.
	 *
	 * @param __cl The class.
	 * @param __v The value to set.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/12/01
	 */
	public final void setNamePointer(TaskClass __cl, int __v)
		throws NullPointerException
	{
		this.setProperty(__cl, ClassInfoProperty.INT_NAMEP,
			__v);
	}
	
	/**
	 * Sets the object count.
	 *
	 * @param __cl The class.
	 * @param __v The value to set.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/12/01
	 */
	public final void setObjectCount(TaskClass __cl, int __v)
		throws NullPointerException
	{
		this.setProperty(__cl, ClassInfoProperty.INT_NUMOBJECTS,
			__v);
	}
	
	/**
	 * Sets the pool pointer.
	 *
	 * @param __cl The class.
	 * @param __v The value to set.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/12/01
	 */
	public final void setPoolPointer(TaskClass __cl, int __v)
		throws NullPointerException
	{
		this.setProperty(__cl, ClassInfoProperty.INT_POOL,
			__v);
	}
	
	/**
	 * Sets the value of the given property.
	 *
	 * @param __cl The class to set.
	 * @param __prop The property to set.
	 * @param __v The value to set.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/12/01
	 */
	public final void setProperty(TaskClass __cl, int __prop, int __v)
		throws NullPointerException
	{
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		// Write value
		Assembly.memWriteInt(__cl.infoPointer(),
			this.properties[__prop], __v);
	}
	
	/**
	 * Sets the self pointer value.
	 *
	 * @param __cl The class.
	 * @param __v The value to set.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/12/01
	 */
	public final void setSelfPointer(TaskClass __cl, int __v)
		throws NullPointerException
	{
		this.setProperty(__cl, ClassInfoProperty.INT_SELFPTR,
			__v);
	}
	
	/**
	 * Sets the super class.
	 *
	 * @param __cl The class.
	 * @param __su The super class.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/12/01
	 */
	public final void setSuperClass(TaskClass __cl, TaskClass __su)
		throws NullPointerException
	{
		if (__su == null)
			throw new NullPointerException("NARG");
		
		this.setProperty(__cl, ClassInfoProperty.CLASSINFO_SUPERCLASS,
			__su.infoPointer());
	}
	
	/**
	 * Sets the VTable pool array.
	 *
	 * @param __cl The class.
	 * @param __v The value to set.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/12/01
	 */
	public final void setVTablePool(TaskClass __cl, int __v)
		throws NullPointerException
	{
		this.setProperty(__cl, ClassInfoProperty.INT_ARRAY_VTABLEPOOL,
			__v);
	}
	
	/**
	 * Sets the VTable method array.
	 *
	 * @param __cl The class.
	 * @param __v The value to set.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/12/01
	 */
	public final void setVTableVirtual(TaskClass __cl, int __v)
		throws NullPointerException
	{
		this.setProperty(__cl, ClassInfoProperty.INT_ARRAY_VTABLEVIRTUAL,
			__v);
	}
	
	/**
	 * Returns the VTable for pools.
	 *
	 * @param __cl The class to get.
	 * @return The resulting value.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/12/08
	 */
	public final int vTablePool(TaskClass __cl)
		throws NullPointerException
	{
		return this.property(__cl, ClassInfoProperty.INT_ARRAY_VTABLEPOOL);
	}
	
	/**
	 * Returns the VTable for method pointers.
	 *
	 * @param __cl The class to get.
	 * @return The resulting value.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/12/08
	 */
	public final int vTableVirtual(TaskClass __cl)
		throws NullPointerException
	{
		return this.property(__cl, ClassInfoProperty.INT_ARRAY_VTABLEVIRTUAL);
	}
	
	/**
	 * Initializes the {@code ClassInfo} utility.
	 *
	 * @param __cfp The class info parser to use.
	 * @return The utility for {@code ClassInfo}.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/11/30
	 */
	public static final ClassInfoUtility of(ClassFileParser __cfp)
		throws NullPointerException
	{
		if (__cfp == null)
			throw new NullPointerException("NARG");
		
		// Allocation size
		int as = Constants.OBJECT_BASE_SIZE + __cfp.fieldSize(false);
		
		// Properties
		int[] props = new int[ClassInfoProperty.NUM_PROPERTIES];
		
		// Extract properties from fields
		ClassFieldsParser cifs = __cfp.fields(false);
		for (int cif = 0, cifn = cifs.count(); cif < cifn; cif++)
		{
			// Determine actual offset of this property
			int offset = Constants.OBJECT_BASE_SIZE + cifs.offset(cif);
			
			// The property index
			int pdx;
			
			// Depends on the field name and type
			String nat = cifs.name(cif) + ":" + cifs.type(cif);
			switch (nat)
			{
					// Self pointer.
				case "selfptr:int":
					pdx = ClassInfoProperty.INT_SELFPTR;
					break;

					// Magic number used to detect corruption.
				case "magic:int":
					pdx = ClassInfoProperty.INT_MAGIC;
					break;

					// Class information flags.
				case "flags:int":
					pdx = ClassInfoProperty.INT_FLAGS;
					break;

					// The pointer to the minimized class file.
				case "miniptr:int":
					pdx = ClassInfoProperty.INT_MINIPTR;
					break;

					// The pointer to the class name.
				case "namep:int":
					pdx = ClassInfoProperty.INT_NAMEP;
					break;

					// The allocation size of this class.
				case "size:int":
					pdx = ClassInfoProperty.INT_SIZE;
					break;

					// The base offset for fields in this class.
				case "base:int":
					pdx = ClassInfoProperty.INT_BASE;
					break;

					// The number of objects in the instance fields, for GC.
				case "numobjects:int":
					pdx = ClassInfoProperty.INT_NUMOBJECTS;
					break;

					// The dimensions this class uses, if it is an array.
				case "dimensions:int":
					pdx = ClassInfoProperty.INT_DIMENSIONS;
					break;

					// The cell size of components if this is an array.
				case "cellsize:int":
					pdx = ClassInfoProperty.INT_CELLSIZE;
					break;

					// The super class data.
				case "superclass:cc/squirreljme/jvm/ClassInfo":
					pdx = ClassInfoProperty.CLASSINFO_SUPERCLASS;
					break;

					// Interfaces.
				case "interfaceclasses:[Lcc/squirreljme/jvm/ClassInfo;":
					pdx = ClassInfoProperty.CLASSINFO_ARRAY_INTERFACECLASSES;
					break;

					// The component class.
				case "componentclass:cc/squirreljme/jvm/ClassInfo":
					pdx = ClassInfoProperty.CLASSINFO_COMPONENTCLASS;
					break;

					// Pointer to the class object.
				case "classobjptr:java/lang/Class":
					pdx = ClassInfoProperty.CLASS_CLASSOBJPTR;
					break;

					// Virtual invoke VTable.
				case "vtablevirtual:[I":
					pdx = ClassInfoProperty.INT_ARRAY_VTABLEVIRTUAL;
					break;

					// Virtual invoke VTable pool entries.
				case "vtablepool:[I":
					pdx = ClassInfoProperty.INT_ARRAY_VTABLEPOOL;
					break;

					// The pointer to the constant pool of this class.
				case "pool:int":
					pdx = ClassInfoProperty.INT_POOL;
					break;
					
					// The JAR Index
				case "jardx:int":
					pdx = ClassInfoProperty.INT_JARDX;
					break;
					
					// The number of methods in the class
				case "nummethods:int":
					pdx = ClassInfoProperty.INT_NUMMETHODS;
					break;
					
					// The class depth
				case "classdepth:int":
					pdx = ClassInfoProperty.INT_CLASSDEPTH;
					break;
					
					// The static field offset
				case "sfoffset:int":
					pdx = ClassInfoProperty.INT_SFOFFSET;
					break;
				
				default:
					throw new todo.TODO(nat);
			}
			
			// Store
			props[pdx] = offset;
		}
		
		// Initialize now
		return new ClassInfoUtility(as, props);
	}
}

