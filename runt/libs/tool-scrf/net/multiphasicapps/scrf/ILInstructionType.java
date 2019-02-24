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
 * Represents the type of instruction used.
 *
 * @since 2019/02/23
 */
public enum ILInstructionType
{
	/** No operation. */
	NOP,
	
	/** Copy. */
	COPY,
	
	/** Constant. */
	CONST,
	
	/** Invoke something. */
	INVOKE,
	
	/** Return from method. */
	RETURN,
	
	/** Read memory. */
	READ,
	
	/** End. */
	;
}

