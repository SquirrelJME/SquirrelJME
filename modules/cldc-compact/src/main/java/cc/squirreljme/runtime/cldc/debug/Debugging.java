// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.debug;

import todo.OOPS;
import todo.TODO;

/**
 * This contains static method forwarders.
 *
 * @since 2020/03/21
 */
public final class Debugging
{
	/**
	 * Not used.
	 *
	 * @since 2020/03/21
	 */
	private Debugging()
	{
	}
	
	/**
	 * Emits an oops error.
	 *
	 * @param __args Argument to the error.
	 * @return The generated error.
	 * @since 2020/03/22
	 */
	@SuppressWarnings("deprecation")
	public static Error oops(Object... __args)
	{
		return OOPS.OOPS(__args);
	}
	
	/**
	 * Emits a To-Do error.
	 *
	 * @param __args Arguments to the error.
	 * @return The generated error.
	 * @since 2020/03/21
	 */
	@SuppressWarnings("deprecation")
	public static Error todo(Object... __args)
	{
		return TODO.TODO(__args);
	}
	
	/**
	 * Emits a To-Do note.
	 *
	 * @param __fmt Format string.
	 * @param __args Arguments.
	 * @since 2020/03/31
	 */
	@SuppressWarnings("deprecation")
	public static void todoNote(String __fmt, Object... __args)
	{
		TODO.note(__fmt, __args);
	}
}
