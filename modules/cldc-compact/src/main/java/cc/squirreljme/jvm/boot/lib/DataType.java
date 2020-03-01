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
 * This contains the various data types classes and fields may be.
 *
 * @since 2019/10/12
 */
public interface DataType
{
	/** Object. */
	byte OBJECT =
		0;
	
	/** Byte. */
	byte BYTE =
		1;
	
	/** Short. */
	byte SHORT =
		2;
	
	/** Character. */
	byte CHARACTER =
		3;
	
	/** Integer. */
	byte INTEGER =
		4;
	
	/** Float. */
	byte FLOAT =
		5;
	
	/** Long. */
	byte LONG =
		6;
	
	/** Double. */
	byte DOUBLE =
		7;
}

