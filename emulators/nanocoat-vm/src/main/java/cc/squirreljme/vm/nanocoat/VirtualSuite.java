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
import cc.squirreljme.emulator.vm.VMSuiteManager;
import cc.squirreljme.jvm.mle.JarPackageShelf;
import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * Provides virtual access to a suite for {@link JarPackageShelf}, this acts
 * as a wrapper around {@link VMSuiteManager}.
 *
 * @since 2023/12/12
 */
public final class VirtualSuite
	implements Pointer
{
	/** The suite manager. */
	protected final VMSuiteManager manager;
	
	static
	{
		__Native__.__loadLibrary();
	}
	
	/**
	 * Initializes the virtual suite manager.
	 *
	 * @param __pool The memory pool to get from.
	 * @param __suiteManager The actual suite manager to wrap.
	 * @throws NullPointerException On null arguments.
	 * @throws VMException If it could not be initialized.
	 * @since 2023/12/13
	 */
	public VirtualSuite(AllocPool __pool, VMSuiteManager __suiteManager)
		throws NullPointerException, VMException
	{
		if (__suiteManager == null)
			throw new NullPointerException("NARG");
		
		// Set the manager used to obtain suites
		this.manager = __suiteManager;
		
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/12/13
	 */
	@Override
	public long pointerAddress()
	{
		throw Debugging.todo();
	}
}
