// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm;

/**
 * This interface represents the virtual machine which may be executed
 * accordingly.
 *
 * @since 2018/11/17
 */
public interface VirtualMachine
{
	/**
	 * Runs the virtual machine.
	 *
	 * @return The exit code of the virtual machine.
	 * @throws VMException If the VM threw an exception.
	 * @since 2018/11/17
	 */
	public abstract int runVm()
		throws VMException;
}

