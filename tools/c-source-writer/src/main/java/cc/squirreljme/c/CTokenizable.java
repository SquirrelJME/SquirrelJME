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
 * Basic tokenizable item, with nothing special regarding it.
 *
 * @since 2023/06/12
 */
public interface CTokenizable
{
	/**
	 * Returns the token representation of this.
	 *
	 * @return The token representation of this.
	 * @since 2023/05/29
	 */
	List<String> tokens();
}
