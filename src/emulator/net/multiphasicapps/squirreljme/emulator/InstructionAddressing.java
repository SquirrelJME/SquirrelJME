// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.emulator;

/**
 * This interface is used by the CPU so it can access instructions for
 * execution.
 *
 * @since 2016/08/06
 */
public interface InstructionAddressing
	extends CommonAddressing
{
	/**
	 * Returns the address manager which is used for storing instructions.
	 *
	 * @return The instruction address manager.
	 * @since 2016/08/07
	 */
	public abstract AddressManager getInstructionAddressManager();
}

