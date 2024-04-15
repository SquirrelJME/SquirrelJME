// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
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
	 * Returns the tokens that make up the modifier.
	 * 
	 * @return The modifier tokens.
	 * @since 2023/06/05
	 */
	List<String> tokens();
}
