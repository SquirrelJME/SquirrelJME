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
 * Represents something which may be tokenized.
 *
 * @since 2023/06/11
 */
public interface CTokenizable
{
	/**
	 * Returns the token representation of the type.
	 * 
	 * @param __set The token set to use for the type.
	 * @return The token representation of the type.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/05/29
	 */
	List<String> tokens(CTokenSet __set)
		throws NullPointerException;
}
