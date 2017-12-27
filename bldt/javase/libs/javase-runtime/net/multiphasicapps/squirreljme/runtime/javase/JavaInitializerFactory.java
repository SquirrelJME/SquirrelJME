// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.runtime.javase;

import net.multiphasicapps.squirreljme.runtime.kernel.Kernel;
import net.multiphasicapps.squirreljme.runtime.kernel.KernelInitializerFactory;
import net.multiphasicapps.squirreljme.runtime.kernel.KernelPrograms;
import net.multiphasicapps.squirreljme.runtime.kernel.KernelTask;

/**
 * This factory is used to initialize the other parts of the kernel.
 *
 * @since 2017/12/27
 */
public class JavaInitializerFactory
	implements KernelInitializerFactory
{
	/**
	 * {@inheritDoc}
	 * @since 2017/12/27
	 */
	@Override
	public KernelTask initializeKernelTask(Kernel __k)
	{
		return new JavaSelfKernelTask();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/12/27
	 */
	@Override
	public KernelPrograms initializePrograms(Kernel __k)
	{
		return new JavaPrograms();
	}
}

