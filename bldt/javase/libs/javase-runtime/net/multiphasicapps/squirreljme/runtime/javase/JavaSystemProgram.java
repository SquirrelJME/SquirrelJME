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

import java.io.InputStream;
import net.multiphasicapps.squirreljme.runtime.cldc.SystemProgramType;
import net.multiphasicapps.squirreljme.runtime.kernel.KernelProgram;

/**
 * This is a program which represents the system itself.
 *
 * @since 2017/12/27
 */
public class JavaSystemProgram
	extends KernelProgram
{
	/**
	 * Initializes the system program.
	 *
	 * @since 2017/12/27
	 */
	public JavaSystemProgram()
	{
		super(0);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/12/28
	 */
	@Override
	protected InputStream accessLoadResource(String __name)
		throws NullPointerException
	{
		if (__name == null)
			throw new NullPointerException("NARG");
		
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/12/27
	 */
	@Override
	protected int accessType()
	{
		return SystemProgramType.SYSTEM;
	}
}

