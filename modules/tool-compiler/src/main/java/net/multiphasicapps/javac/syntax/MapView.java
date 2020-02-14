// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.javac.syntax;

import java.util.Map;

/**
 * This represents syntax which can be represented as a map. This only applies
 * to parts of the syntax tree which have multiple children nodes.
 *
 * @since 2019/01/17
 */
public interface MapView
{
	/**
	 * Returns a representation of this syntax as a map.
	 *
	 * @return The map representing of this syntax.
	 * @since 2019/01/17
	 */
	public abstract Map<String, Object> asMap();
}

