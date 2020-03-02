// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.boot.lib;

/**
 * This contains properties of class info.
 *
 * @since 2019/11/30
 */
public interface ClassInfoProperty
{
	/** Self pointer. */
	byte INT_SELFPTR =
		0;
	
	/** Magic number used to detect corruption. */
	byte INT_MAGIC =
		1;
	
	/** Class information flags. */
	byte INT_FLAGS =
		2;
	
	/** The pointer to the minimized class file. */
	byte INT_MINIPTR =
		3;
	
	/** The pointer to the class name. */
	byte INT_NAMEP =
		4;
	
	/** The allocation size of this class. */
	byte INT_SIZE =
		5;
	
	/** The base offset for fields in this class. */
	byte INT_BASE =
		6;
	
	/** The number of objects in the instance fields, for GC. */
	byte INT_NUMOBJECTS =
		7;
	
	/** The dimensions this class uses, if it is an array. */
	byte INT_DIMENSIONS =
		8;
	
	/** The cell size of components if this is an array. */
	byte INT_CELLSIZE =
		9;
	
	/** The super class data. */
	byte CLASSINFO_SUPERCLASS =
		10;
	
	/** Interfaces. */
	byte CLASSINFO_ARRAY_INTERFACECLASSES =
		11;
	
	/** The component class. */
	byte CLASSINFO_COMPONENTCLASS =
		12;
	
	/** Pointer to the class object. */
	byte CLASS_CLASSOBJPTR =
		13;
	
	/** Virtual invoke VTable. */
	byte INT_ARRAY_VTABLEVIRTUAL =
		14;
	
	/** Virtual invoke VTable pool entries. */
	byte INT_ARRAY_VTABLEPOOL =
		15;
	
	/** The pointer to the constant pool of this class. */
	byte INT_POOL =
		16;
	
	/** The JAR Index. */
	byte INT_JARDX =
		17;
	
	/** The number of methods the class has. */
	byte INT_NUMMETHODS =
		18;
	
	/** The depth of this class. */
	byte INT_CLASSDEPTH =
		19;
	
	/** Static field offset. */
	byte INT_SFOFFSET =
		20;
	
	/** New instance method. */
	byte INT_DEFAULTNEW =
		21;
	
	/** The number of properties available. */
	byte NUM_PROPERTIES =
		22;
}

