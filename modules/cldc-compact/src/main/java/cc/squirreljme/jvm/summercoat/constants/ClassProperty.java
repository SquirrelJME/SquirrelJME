// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.summercoat.constants;

/**
 * This contains properties of class info.
 * 
 * Static properties are declared in {@link StaticClassProperty}.
 *
 * @since 2019/11/30
 */
public interface ClassProperty
	extends StaticClassProperty
{
	/** Magic number used to detect corruption. */
	@Deprecated
	byte INT_MAGIC =
		StaticClassProperty.NUM_STATIC_PROPERTIES + 1;
	
	/** The pointer to the minimized class file. */
	@Deprecated
	byte INT_MINIPTR =
		StaticClassProperty.NUM_STATIC_PROPERTIES + 3;
	
	/** The pointer to the class name. */
	@Deprecated
	byte INT_NAMEP =
		StaticClassProperty.NUM_STATIC_PROPERTIES + 4;
	
	/** The allocation size of this class. */
	@Deprecated
	byte INT_ALLOCATION_SIZE =
		StaticClassProperty.NUM_STATIC_PROPERTIES + 5;
	
	/** The base offset for fields in this class. */
	@Deprecated
	byte INT_BASE =
		StaticClassProperty.NUM_STATIC_PROPERTIES + 6;
	
	/** The super class data. */
	@Deprecated
	byte CLASSINFO_SUPERCLASS =
		StaticClassProperty.NUM_STATIC_PROPERTIES + 10;
	
	/** Interfaces. */
	@Deprecated
	byte CLASSINFO_ARRAY_INTERFACECLASSES =
		StaticClassProperty.NUM_STATIC_PROPERTIES + 11;
	
	/** The component class. */
	@Deprecated
	byte CLASSINFO_COMPONENTCLASS =
		StaticClassProperty.NUM_STATIC_PROPERTIES + 12;
	
	/** Pointer to the class object. */
	@Deprecated
	byte CLASS_CLASSOBJPTR =
		StaticClassProperty.NUM_STATIC_PROPERTIES + 13;
	
	/** Virtual invoke VTable. */
	@Deprecated
	byte INT_ARRAY_VTABLEVIRTUAL =
		StaticClassProperty.NUM_STATIC_PROPERTIES + 14;
	
	/** Virtual invoke VTable pool entries. */
	@Deprecated
	byte INT_ARRAY_VTABLEPOOL =
		StaticClassProperty.NUM_STATIC_PROPERTIES + 15;
	
	/** The pointer to the constant pool of this class. */
	@Deprecated
	byte INT_POOL =
		StaticClassProperty.NUM_STATIC_PROPERTIES + 16;
	
	/** The JAR Index. */
	@Deprecated
	byte INT_JARDX =
		StaticClassProperty.NUM_STATIC_PROPERTIES + 17;
	
	/** The depth of this class. */
	@Deprecated
	byte INT_CLASSDEPTH =
		StaticClassProperty.NUM_STATIC_PROPERTIES + 19;
	
	/** Static field offset. */
	@Deprecated
	byte INT_SFOFFSET =
		StaticClassProperty.NUM_STATIC_PROPERTIES + 20;
	
	/** Self pointer. */
	@Deprecated
	byte INT_SELFPTR =
		StaticClassProperty.NUM_STATIC_PROPERTIES + 22;
	
	/** The number of properties available. */
	byte NUM_PROPERTIES =
		StaticClassProperty.NUM_STATIC_PROPERTIES + 23;
}

