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
 * Represents a tokenizable item which takes a name such as a variable,
 * function argument, or structure type.
 *
 * @since 2023/06/12
 */
public interface CNamedTokenizable
	extends CTokenizable
{
	/**
	 * Returns the token representation of this.
	 * 
	 * @param __set The token set to use for this.
	 * @param __name The name of the token to pivot on.
	 * @return The token representation of this.
	 * @throws NotTokenizableException If this cannot be tokenized in this way.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/06/12
	 */
	List<String> tokens(CNamedTokenSet __set, CIdentifier __name)
		throws NotTokenizableException, NullPointerException;
}
