// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.system;

/**
 * This class contains static methods which perform system calls with the
 * documented and correct set of arguments for system calls.
 *
 * @since 2018/02/27
 */
public final class MnemonicCall
{
	/**
	 * Not used.
	 *
	 * @since 2018/02/27
	 */
	private MnemonicCall()
	{
	}
	
	/**
	 * Specifies that the client was successfully initialized.
	 *
	 * @since 2018/03/01
	 */
	public static final void clientInitializationComplete()
	{
		SystemCall.voidCall(SystemFunction.CLIENT_INITIALIZATION_COMPLETE);
	}
}

