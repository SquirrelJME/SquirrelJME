// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.c.std;

import cc.squirreljme.c.CFunctionType;

/**
 * Provider for C functions.
 *
 * @since 2023/06/23
 */
public interface CFunctionProvider
{
	/**
	 * Returns the calculated function.
	 * 
	 * @return The function.
	 * @since 2023/06/23
	 */
	CFunctionType function();
}
