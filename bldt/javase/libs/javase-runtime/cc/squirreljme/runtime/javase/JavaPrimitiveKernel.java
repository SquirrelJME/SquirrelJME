// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.javase;

import cc.squirreljme.kernel.PrimitiveKernel;
import cc.squirreljme.kernel.PrimitiveProcess;
import cc.squirreljme.kernel.PrimitiveThread;

/**
 * This provides the primitive kernel interface used on host Java SE systems
 * to run the environment on Java SE based systems. This environment is
 * intended to be used for build system uses.
 *
 * @since 2018/03/03
 */
public class JavaPrimitiveKernel
	implements PrimitiveKernel
{
	/**
	 * {@inheritDoc}
	 * @since 2018/03/04
	 */
	@Override
	public final PrimitiveProcess createProcess()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/04
	 */
	@Override
	public final PrimitiveThread createThread()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/15
	 */
	@Override
	public final String[] serviceMap()
	{
		return new String[]{
			};
	}
}

