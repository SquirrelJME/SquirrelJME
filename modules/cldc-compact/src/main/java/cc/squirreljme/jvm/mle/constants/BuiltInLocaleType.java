// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.constants;

import cc.squirreljme.runtime.cldc.annotation.Exported;

/**
 * Represents a locale which is built-in.
 *
 * @since 2020/06/11
 */
@Exported
public interface BuiltInLocaleType
{
	/** Unspecified. */
	@Exported
	byte UNSPECIFIED =
		0;
	
	/** English, US. */
	@Exported
	byte ENGLISH_US =
		1;
}
