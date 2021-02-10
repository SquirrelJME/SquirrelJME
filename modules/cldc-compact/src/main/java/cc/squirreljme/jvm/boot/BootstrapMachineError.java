// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.boot;

/**
 * This is thrown when the bootstrap has errors.
 *
 * @since 2020/03/01
 */
@Deprecated
public class BootstrapMachineError
	extends VirtualMachineError
{
	/**
	 * Initializes the exception with no message or cause.
	 *
	 * @since 2020/03/01
	 */
	@Deprecated
	public BootstrapMachineError()
	{
	}
	
	/**
	 * Initializes the exception with the given message and no cause.
	 *
	 * @param __m The message.
	 * @since 2020/03/01
	 */
	@Deprecated
	public BootstrapMachineError(String __m)
	{
		super(__m);
	}
}
