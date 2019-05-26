// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.lang;

import cc.squirreljme.jvm.Assembly;

/**
 * This represents a string.
 *
 * @since 2019/05/25
 */
public final class String
{
	/** The backing array. */
	transient final char[] _chars;
	
	/**
	 * Initializes an empty string.
	 *
	 * @since 2019/05/26
	 */
	public String()
	{
		this._chars = new char[0];
	}
	
	/**
	 * Initializes string decoded from the given UTF-8 byte.
	 *
	 * @param __b The UTF-8 bytes to decode.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/05/26
	 */
	public String(byte[] __b)
	{
		Assembly.breakpoint();
		throw new todo.TODO();
	}
	
	/**
	 * Returns a string which is a unique internal representation of a string.
	 *
	 * @return The unique interned string.
	 * @since 2019/05/26
	 */
	public final String intern()
	{
		Assembly.breakpoint();
		throw new todo.TODO();
	}
}

