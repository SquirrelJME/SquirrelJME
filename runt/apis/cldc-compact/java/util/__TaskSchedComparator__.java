// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.util;

/**
 * Comparator for task scheduling.
 *
 * @since 2018/12/11
 */
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

