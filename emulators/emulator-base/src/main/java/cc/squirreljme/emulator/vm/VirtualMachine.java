// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.emulator.vm;

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
	int runVm()
		throws VMException;
}

