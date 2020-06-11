// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.config;

/**
 * This represents a built-in locale.
 *
 * @since 2020/05/12
 */
@Deprecated
public interface BuiltInLocale
{
	/** Unknown, will be treated as US English. */
	byte UNKNOWN =
		0;
	
	/** English: United States of America. */
	byte ENGLISH_US =
		1;
	
	/** The number of built-in locales. */
	byte NUM_BUILTIN_LOCALES =
		2;
}
