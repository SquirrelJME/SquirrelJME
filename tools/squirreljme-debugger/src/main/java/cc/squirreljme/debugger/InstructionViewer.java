// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.debugger;

/**
 * A viewer for instruction data.
 *
 * @since 2024/01/21
 */
public interface InstructionViewer
{
	/**
	 * Returns the address of the instruction.
	 *
	 * @return The instruction address.
	 * @since 2024/01/21
	 */
	int address();
	
	/**
	 * Returns the mnemonic of this instruction.
	 *
	 * @return The instruction mnemonic.
	 * @since 2024/01/21
	 */
	String mnemonic();
}
