// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot.summercoat.target;

/**
 * Represents a banglet.
 *
 * @since 2022/09/05
 */
public interface Banglet
{
	/**
	 * This is the name of the identifier in {@code enum} form, that is
	 * uppercase letters with underscores and numbers.
	 * 
	 * @return The banglet name, matches an enumeration.
	 * @since 2022/09/05
	 */
	String name();
}
