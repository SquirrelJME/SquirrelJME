// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.i18n;

/**
 * This class represents the interface used to perform locale based operations.
 *
 * @since 2018/09/20
 */
public interface Locale
{
	/**
	 * Converts the specified character to lowercase.
	 *
	 * @param __c The input character.
	 * @return The lowercased character.
	 * @since 2018/09/20
	 */
	public abstract char toLowerCase(char __c);
	
	/**
	 * Converts the specified character to uppercase.
	 *
	 * @param __c The input character.
	 * @return The uppercased character.
	 * @since 2018/09/28
	 */
	public abstract char toUpperCase(char __c);
}

