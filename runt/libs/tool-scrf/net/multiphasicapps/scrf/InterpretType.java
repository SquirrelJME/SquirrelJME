// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.scrf;

/**
 * This determines how an interpreted value is to be interpreted such as if
 * it should be determined as a value or an index.
 *
 * @since 2019/03/03
 */
public enum InterpretType
{
	/** Interpret value as purely a constant value. */
	VALUE,
	
	/** Interpret as a register which contains a value. */
	REGISTER_VALUE,
	
	/** Interpret as a value contained within the given DynTable index. */
	DYNTABLE_VALUE,
	
	/** Interpret as the index of a an entry in the DynTable. */
	DYNTABLE_INDEX,
	
	/** Interpret as an index to an instruction. */
	INSTRUCTION_INDEX,
	
	/** End. */
	;
}

