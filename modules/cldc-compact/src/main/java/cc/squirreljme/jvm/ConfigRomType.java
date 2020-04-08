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
 * The type of value to store into the configuration.
 *
 * @since 2020/04/07
 */
public interface ConfigRomType
{
	/** Java Modified UTF. */
	byte UTF =
		1;
	
	/** Key/value pair. */
	byte KEY_VALUE_PAIR =
		2;
	
	/** UTF List. */
	byte UTF_LIST =
		3;
	
	/** 32-bit integer. */
	byte INTEGER =
		4;
	
	/** 64-bit integer. */
	byte LONG =
		5;
	
	/** Boolean (Same as {@link #INTEGER} except {@code 0 == false)}. */
	byte BOOLEAN =
		6;
	
	/** The number of types. */
	byte NUM_TYPES =
		7;
}
