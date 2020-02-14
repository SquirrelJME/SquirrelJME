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
 * This represents a single item within the call stack request.
 *
 * @since 2019/06/16
 */
public interface CallStackItem
{
	/** The class name. */
	public static final byte CLASS_NAME =
		0;
	
	/** The method name. */
	public static final byte METHOD_NAME =
		1;
	
	/** The method type. */
	public static final byte METHOD_TYPE =
		2;
	
	/** The current file. */
	public static final byte SOURCE_FILE =
		3;
	
	/** Source line. */
	public static final byte SOURCE_LINE =
		4;
	
	/** The PC address. */
	public static final byte PC_ADDRESS =
		5;
	
	/** Java operation. */
	public static final byte JAVA_OPERATION =
		6;
	
	/** Java PC address. */
	public static final byte JAVA_PC_ADDRESS =
		7;
	
	/** The current task ID. */
	public static final byte TASK_ID =
		8;
	
	/** The number of supported items. */
	public static final byte NUM_ITEMS =
		9;
}

