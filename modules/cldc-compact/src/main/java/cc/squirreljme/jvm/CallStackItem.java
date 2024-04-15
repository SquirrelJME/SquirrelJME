// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
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
	byte CLASS_NAME =
		0;
	
	/** The method name. */
	byte METHOD_NAME =
		1;
	
	/** The method type. */
	byte METHOD_TYPE =
		2;
	
	/** The current file. */
	byte SOURCE_FILE =
		3;
	
	/** Source line. */
	byte SOURCE_LINE =
		4;
	
	/** The PC address. */
	byte PC_ADDRESS =
		5;
	
	/** Java operation. */
	byte JAVA_OPERATION =
		6;
	
	/** Java PC address. */
	byte JAVA_PC_ADDRESS =
		7;
	
	/** The current task ID. */
	byte TASK_ID =
		8;
	
	/** The number of supported items. */
	byte NUM_ITEMS =
		9;
}

