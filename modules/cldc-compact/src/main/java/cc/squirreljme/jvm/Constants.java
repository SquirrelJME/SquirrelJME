// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm;

/**
 * Virtual machine constants.
 *
 * @since 2019/05/26
 */
public interface Constants
{
	/** The offset for the object's class type. */
	@Deprecated
	byte OBJECT_CLASS_OFFSET =
		0;
	
	/** The offset for the object's reference count. */
	@Deprecated
	byte OBJECT_COUNT_OFFSET =
		4;
	
	/** Object monitor owner offset. */
	@Deprecated
	byte OBJECT_MONITOR_OFFSET =
		8;
	
	/** Object monitor count offset. */
	@Deprecated
	byte OBJECT_MONITOR_COUNT_OFFSET =
		12;
	
	/** Base size for object types. */
	@Deprecated
	byte OBJECT_BASE_SIZE =
		16;
	
	/** The offset for array length. */
	@Deprecated
	byte ARRAY_LENGTH_OFFSET =
		16;
	
	/** The base size for arrays. */
	@Deprecated
	byte ARRAY_BASE_SIZE =
		20;
	
	/** Constant pool cell size. */
	@Deprecated
	byte POOL_CELL_SIZE =
		4;
	
	/** Bad magic number. */
	int BAD_MAGIC =
		0xE7E5E7E4;
	
	/** Class info flag: Is array type? */
	short CIF_IS_ARRAY =
		0x0001;
	
	/** Class info flag: Is array of objects? */
	short CIF_IS_ARRAY_OF_OBJECTS =
		0x0002;
	
	/** Is this a primitive type? */
	short CIF_IS_PRIMITIVE =
		0x0004;
	
	/** Offset for the configuration key. */
	@Deprecated
	byte CONFIG_KEY_OFFSET =
		0;
	
	/** Offset for the configuration size. */
	@Deprecated
	byte CONFIG_SIZE_OFFSET =
		2;
	
	/** Size of the header for configuration items. */
	@Deprecated
	byte CONFIG_HEADER_SIZE =
		4;
	
	/** Stop debugging information from printing. */
	byte DEBUG_SQUELCH_PRINT =
		0b0000_0001;
	
	/** Do not exit on oops? */
	byte DEBUG_NO_OOPS_EXIT =
		0b0000_0010;
	
	/** Do not exit on TO-DO? */
	byte DEBUG_NO_TODO_EXIT =
		0b0000_0100;
	
	/** The thread ID for out-of-bound IPC events. */
	int OOB_IPC_THREAD =
		0xFFFFFFFF;
	
	/** This is the API level that is used for "new" SquirrelJME versions. */
	int API_LEVEL_2020_05_10 =
		4_0_20131;
}

