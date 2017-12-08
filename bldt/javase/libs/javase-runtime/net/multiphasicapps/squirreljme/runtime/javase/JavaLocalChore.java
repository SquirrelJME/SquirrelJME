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

import net.multiphasicapps.squirreljme.runtime.cldc.chore.Chore;

/**
 * This is a chore which represents the current process. There should only
 * ever be a single instance of this class.
 *
 * @since 2017/12/08
 */
public class JavaLocalChore
	extends JavaChore
{
	/**
	 * Initializes the local chore.
	 *
	 * @param __group The group the chore is in.
	 * @since 2017/12/08
	 */
	public JavaLocalChore(JavaChoreGroup __group)
	{
		super(__group);
	}
}

