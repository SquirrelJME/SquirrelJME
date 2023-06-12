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
 * Basic tokenizable item.
 *
 * @since 2023/06/12
 */
public interface CBasicTokenizable
	extends CTokenizable
{
	/**
	 * Returns the token representation of this.
	 * 
	 * @param __set The token set to use for this.
	 * @return The token representation of this.
	 * @throws NotTokenizableException If this cannot be tokenized this way.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/05/29
	 */
	List<String> tokens(CBasicTokenSet __set)
		throws NotTokenizableException, NullPointerException;
}
