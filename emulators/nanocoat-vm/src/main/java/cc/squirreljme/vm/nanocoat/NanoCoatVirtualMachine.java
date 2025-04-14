// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.nanocoat;

import cc.squirreljme.emulator.vm.VMException;
import cc.squirreljme.emulator.vm.VirtualMachine;
import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * This is the NanoCoat virtual machine interface implementation on the
 * Java side of the emulator.
 *
 * @since 2023/12/05
 */
public class NanoCoatVirtualMachine
	implements VirtualMachine
{
	/**
	 * Initializes the base virtual machine.
	 *
	 * @since 2023/12/05
	 */
	public NanoCoatVirtualMachine()
	{
		__Native__.__loadLibrary();
		
		// Load native virtual machine
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/12/05
	 */
	@Override
	public int runVm()
		throws VMException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/01/14
	 */
	@Override
	public void setTraceBits(boolean __or, int __bits)
	{
		throw Debugging.todo();
	}
}
