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

import java.lang.ref.Reference;
import java.util.Map;
import net.multiphasicapps.squirreljme.runtime.kernel.Kernel;
import net.multiphasicapps.squirreljme.runtime.kernel.KernelProgram;
import net.multiphasicapps.squirreljme.runtime.kernel.KernelTask;
import net.multiphasicapps.squirreljme.runtime.kernel.KernelTasks;

/**
 * This is the task manager which runs on the Java SE host system.
 *
 * @since 2017/12/27
 */
public class JavaTasks
	extends KernelTasks
{
	/**
	 * Initializes the task manager.
	 *
	 * @param __rk Owning kernel reference.
	 * @param __st The system task.
	 * @since 2017/12/27
	 */
	public JavaTasks(Reference<Kernel> __rk, KernelTask __st)
	{
		super(__rk, __st);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/12/31
	 */
	@Override
	protected KernelTask accessLaunch(KernelProgram[] __cp,
		KernelProgram __program, String __mainclass, int __perms,
		Map<String, String> __properties)
		throws NullPointerException
	{
		if (__cp == null || __program == null || __mainclass == null ||
			__properties == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
}

