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
 * Constants to identify the kind of memory handle that an object is.
 *
 * @since 2020/12/19
 */
public interface MemHandleKind
{
	/** Undefined. */
	byte UNDEFINED =
		0;
	
	/** Static field storage. */
	byte STATIC_FIELD_DATA =
		1;
	
	/** Class information. */
	byte CLASS_INFO =
		2;
	
	/** A list of classes. */
	byte CLASS_INFO_LIST =
		3;
	
	/** The constant pool. */
	byte POOL =
		4;
	
	/** Instance of an object. */
	byte OBJECT_INSTANCE =
		5;
	
	/** Virtual method VTable. */
	byte VIRTUAL_VTABLE =
		6;
	
	/** Interface method VTable. */
	byte INTERFACE_VTABLE =
		7;
	
	/** The number of kinds used. */
	byte NUM_KINDS =
		8;
}
