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
import net.multiphasicapps.squirreljme.runtime.kernel.KernelProgram;
import net.multiphasicapps.squirreljme.runtime.kernel.KernelPrograms;
import net.multiphasicapps.squirreljme.runtime.kernel.KernelTask;

/**
 * This implements the kernel which is used on the initial Java SE
 * process and not the client processes.
 *
 * @since 2017/12/08
 */
public final class JavaKernel
	extends Kernel
{
	/**
	 * Initializes the kernel to run on Java systems.
	 *
	 * @param __kt The output kernel task array.
	 * @since 2017/12/08
	 */
	JavaKernel(KernelTask[] __kt)
	{
		super(__kt, new JavaInitializerFactory());
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/12/31
	 */
	@Override
	public String mapService(String __sv)
		throws NullPointerException
	{
		if (__sv == null)
			throw new NullPointerException("NARG");
		
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/12/28
	 */
	@Override
	public void setDaemonThread(Thread __t)
		throws IllegalThreadStateException, NullPointerException
	{
		if (__t == null)
			throw new NullPointerException("NARG");
		
		__t.setDaemon(true);
	}
}

