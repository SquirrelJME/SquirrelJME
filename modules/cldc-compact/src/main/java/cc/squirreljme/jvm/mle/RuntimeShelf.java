// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle;

import cc.squirreljme.jvm.config.LineEndingType;
import cc.squirreljme.jvm.mle.constants.BuiltInEncodingType;
import cc.squirreljme.jvm.mle.constants.BuiltInLocaleType;

/**
 * Run-time shelf which contains system information.
 *
 * @since 2020/06/09
 */
public final class RuntimeShelf
{
	/**
	 * Not used.
	 *
	 * @since 2020/06/09
	 */
	private RuntimeShelf()
	{
	}
	
	/**
	 * Returns the encoding of the system.
	 *
	 * @return The encoding of the system, see {@link BuiltInEncodingType}.
	 * @see BuiltInEncodingType
	 * @since 2020/06/11
	 */
	public static native int encoding();
	
	/**
	 * Returns the line ending type of the system.
	 *
	 * @return The line ending type of the system, see {@link LineEndingType}.
	 * @see LineEndingType
	 * @since 2020/06/09
	 */
	public static native int lineEnding();
	
	/**
	 * The built-in locate to use.
	 *
	 * @return The built-in locale, see {@link BuiltInLocaleType}.
	 * @see BuiltInLocaleType
	 * @since 2020/06/11
	 */
	public static native int locale();
}
