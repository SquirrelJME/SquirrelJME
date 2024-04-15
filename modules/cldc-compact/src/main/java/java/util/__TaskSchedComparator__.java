// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.util;

/**
 * Comparator for task scheduling.
 *
 * @since 2018/12/11
 */
@Deprecated
final class __TaskSchedComparator__
	implements Comparator<TimerTask>
{
	/**
	 * {@inheritDoc}
	 * @since 2018/12/11
	 */
	@Override
	public final int compare(TimerTask __a, TimerTask __b)
	{
		long diff = (__a._schedtime - __b._schedtime);
		if (diff == 0)
			return 0;
		else if (diff < 0)
			return -1;
		return 1;
	}
}

