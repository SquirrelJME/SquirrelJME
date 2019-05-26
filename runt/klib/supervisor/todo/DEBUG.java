// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package todo;

import cc.squirreljme.jvm.Assembly;

/**
 * Contains basic debug output printing.
 *
 * @since 2019/05/26
 */
public final class DEBUG
{
	/**
	 * Not used.
	 *
	 * @since 2019/05/26
	 */
	private DEBUG()
	{
	}
	
	/**
	 * Prints a debug note.
	 *
	 * @param __fmt The string format, compatible with Java except that it
	 * is very limited in the formats it supports.
	 * @param __args Arguments to the note.
	 * @since 2019/05/26
	 */
	public static final void note(String __fmt, Object... __args)
	{
		Assembly.breakpoint();
		throw new todo.TODO();
	}
}

