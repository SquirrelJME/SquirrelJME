// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.c;

import java.util.ArrayList;
import java.util.List;

/**
 * A pointer onion, since they are very layered.
 *
 * @since 2023/06/25
 */
final class __PointerOnion__
{
	/** Left of the onion. */
	final List<String> left =
		new ArrayList<>();
	
	/** Right of the onion. */
	final List<String> right =
		new ArrayList<>();
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/25
	 */
	@Override
	public String toString()
	{
		return String.format("onion(%s | %s)",
			this.left,
			this.right);
	}
}
