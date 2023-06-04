// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.c;

import java.util.List;

/**
 * Represents a modifier.
 *
 * @since 2023/05/29
 */
public interface CModifier
{
	/**
	 * Returns the tokens used for the modifier.
	 * 
	 * @return The tokens used for the modifier.
	 * @since 2023/05/29
	 */
	List<String> modifierTokens();
}
