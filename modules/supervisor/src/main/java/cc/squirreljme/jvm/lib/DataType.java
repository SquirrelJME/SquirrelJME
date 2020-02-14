// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.lib;

/**
 * This contains the various data types classes and fields may be.
 *
 * @since 2019/10/12
 */
public interface DataType
{
	/** Object. */
	public static final byte OBJECT =
		0;
	
	/** Byte. */
	public static final byte BYTE =
		1;
	
	/** Short. */
	public static final byte SHORT =
		2;
	
	/** Character. */
	public static final byte CHARACTER =
		3;
	
	/** Integer. */
	public static final byte INTEGER =
		4;
	
	/** Float. */
	public static final byte FLOAT =
		5;
	
	/** Long. */
	public static final byte LONG =
		6;
	
	/** Double. */
	public static final byte DOUBLE =
		7;
}

