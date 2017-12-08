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
 * This represents a chore which is associated with a given process rather
 * than being purely local.
 *
 * @since 2017/12/08
 */
public class JavaProcessChore
	extends JavaChore
{
	/**
	 * Initializes the process backed chore.
	 *
	 * @param __group The group the chore is in.
	 * @since 2017/12/08
	 */
	public JavaProcessChore(JavaChoreGroup __group)
	{
		super(__group);
	}
}

