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

import net.multiphasicapps.squirreljme.runtime.cldc.APIAccessor;
import net.multiphasicapps.squirreljme.runtime.cldc.chore.ChoreGroup;

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
	 * This should return the same value as if {@code list()[0]} were called.
	 *
	 * @return The system program.
	 * @since 2017/12/08
	 */
	public abstract Program systemProgram();
	
	/**
	 * Returns the list of every program available on the system.
	 *
	 * The first entry is always the system suite.
	 *
	 * @return The list of all available programs.
	 * @throws SecurityException If the list of suites cannot be obtained due
	 * to lack of permissions.
	 * @since 2017/12/08
	 */
	public final Program[] list()
		throws SecurityException
	{
		// {@squirreljme.error ZZ0h The current chore is not permitted to
		// access programs.}
		if (0 == (APIAccessor.currentChoreGroup().basicPermissions() &
			(ChoreGroup.BASIC_PERMISSION_CLIENT_MANAGE_SUITES |
			ChoreGroup.BASIC_PERMISSION_CROSSCLIENT_MANAGE_SUITES)))
			throw new SecurityException("ZZ0h");
		
		throw new todo.TODO();
	}
}

