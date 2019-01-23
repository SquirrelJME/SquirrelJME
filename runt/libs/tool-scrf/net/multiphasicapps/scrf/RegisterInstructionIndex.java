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
 * The index of register instruction operations.
 *
 * @since 2019/01/23
 */
public interface RegisterInstructionIndex
{
	/** No-operation. */
	public static final int NOP =
		0;
	
	/** Copy. */
	public static final int COPY =
		1;
	
	/** The number of valid instructions. */
	public static final int NUM_INSTRUCTIONS =
		2;
}

