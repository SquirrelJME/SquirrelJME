// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.lib;

import cc.squirreljme.jvm.Constants;

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
	 * Returns the allocation size of instances of this class.
	 *
	 * @return The allocation size of this.
	 * @since 2019/11/30
	 */
	public final int allocationSize()
	{
		return this.allocationsize;
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

