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
	public static final byte OBJECT_CLASS_OFFSET =
		0;
	
	/** The offset for the object's reference count. */
	public static final byte OBJECT_COUNT_OFFSET =
		4;
	
	/** Object monitor owner offset. */
	public static final byte OBJECT_MONITOR_OFFSET =
		8;
	
	/** Object monitor count offset. */
	public static final byte OBJECT_MONITOR_COUNT_OFFSET =
		12;
	
	/** Base size for object types. */
	public static final byte OBJECT_BASE_SIZE =
		16;
	
	/** The offset for array length. */
	public static final byte ARRAY_LENGTH_OFFSET =
		16;
	
	/** The base size for arrays. */
	public static final byte ARRAY_BASE_SIZE =
		20;
	
	/** Constant pool cell size. */
	public static final byte POOL_CELL_SIZE =
		4;
	
	/** Bad magic number. */
	public static final int BAD_MAGIC =
		0xE7E5E7E4;
	
	/** Class info flag: Is array type? */
	public static final short CIF_IS_ARRAY =
		0x0001;
	
	/** Class info flag: Is array of objects? */
	public static final short CIF_IS_ARRAY_OF_OBJECTS =
		0x0002;
	
	/** Is this a primitive type? */
	public static final short CIF_IS_PRIMITIVE =
		0x0004;
	
	/** Offset for the configuration key. */
	public static final byte CONFIG_KEY_OFFSET =
		0;
	
	/** Offset for the configuration size. */
	public static final byte CONFIG_SIZE_OFFSET =
		2;
	
	/** Size of the header for configuration items. */
	public static final byte CONFIG_HEADER_SIZE =
		4;
	
	/** The thread ID for out-of-bound IPC events. */
	public static final int OOB_IPC_THREAD =
		0xFFFFFFFF;
}

