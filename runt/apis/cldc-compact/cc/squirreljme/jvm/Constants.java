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
	public static final int OBJECT_CLASS_OFFSET =
		0;
	
	/** The offset for the object's reference count. */
	public static final int OBJECT_COUNT_OFFSET =
		4;
	
	/** Object monitor owner offset. */
	public static final int OBJECT_MONITOR_OFFSET =
		8;
	
	/** Base size for object types. */
	public static final int OBJECT_BASE_SIZE =
		12;
	
	/** The offset for array length. */
	public static final int ARRAY_LENGTH_OFFSET =
		12;
	
	/** The base size for arrays. */
	public static final int ARRAY_BASE_SIZE =
		16;
	
	/** Bad magic number. */
	public static final int BAD_MAGIC =
		0xE7E5E7E4;
	
	/** Class info flag: Is array type? */
	public static final int CIF_IS_ARRAY =
		0x00000001;
}

