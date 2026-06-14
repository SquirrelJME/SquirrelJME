// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.util;

import java.util.Comparator;

/**
 * Compares any {@link CharSequence} with another, ignoring case.
 *
 * @since 2026/06/10
 */
final class __ComparatorIgnoreCase__
	implements Comparator<CharSequence>
{
	/**
	 * {@inheritDoc}
	 * @since 2026/06/10
	 */
	@Override
	public int compare(CharSequence __a, CharSequence __b)
	{
		return CharSequenceUtils.compareIgnoreCase(__a, __b);
	}
}
