// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.runtime.cldc.program;

/**
 * This class is used to manage programs which are installed on the system.
 *
 * @since 2017/12/08
 */
public abstract class Programs
{
	/**
	 * Returns the program which represents the system.
	 *
	 * @return The system program.
	 * @since 2017/12/08
	 */
	public abstract Program systemProgram();
}

