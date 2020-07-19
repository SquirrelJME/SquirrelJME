// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.constants;

import cc.squirreljme.jvm.mle.brackets.UIItemBracket;

/**
 * String properties for {@link UIItemBracket}.
 *
 * @since 2020/07/19
 */
public interface UIItemStringProperty
{
	/** Null property. */
	byte STRING_NULL =
		0;
	
	/** The label of the item. */
	byte STRING_LABEL =
		1;
	
	/** The number of properties. */
	byte NUM_STRING_PROPERTIES =
		1;
}
