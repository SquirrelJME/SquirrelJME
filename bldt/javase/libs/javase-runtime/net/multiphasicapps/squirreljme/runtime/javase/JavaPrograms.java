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

import net.multiphasicapps.squirreljme.runtime.cldc.program.Program;
import net.multiphasicapps.squirreljme.runtime.cldc.program.Programs;

/**
 * Provides access to the programs which are available.
 *
 * @since 2017/12/08
 */
public class JavaPrograms
	extends Programs
{
	/** The system program. */
	protected final JavaProgram system =
		new JavaProgram(JavaProgram.class);
	
	/**
	 * {@inheritDoc}
	 * @since 2017/12/08
	 */
	@Override
	public Program systemProgram()
	{
		return this.system;
	}
}

