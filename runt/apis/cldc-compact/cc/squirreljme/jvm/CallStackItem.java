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
	public static final short CLASS_NAME =
		0;
	
	/** The method name. */
	public static final short METHOD_NAME
		1;
	
	/** The method type. */
	public static final short METHOD_TYPE =
		2;
	
	/** Source line. */
	public static final short SOURCE_LINE =
		3;
	
	/** The PC address. */
	public static final short PC_ADDRESS =
		4;
	
	/** Java operation. */
	public static final short JAVA_OPERATION =
		5;
	
	/** Java PC address. */
	public static final short JAVA_PC_ADDRESS =
		6;
	
	/** The number of supported items. */
	public static final short NUM_ITEMS =
		7;
}

