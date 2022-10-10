// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.nttdocomo.util;

import com.nttdocomo.ui.ShortTimer;
import com.sun.webkit.Timer;

/**
 * Constants for {@link ShortTimer} and {@link Timer}.
 *
 * @since 2022/10/10
 */
public interface TimerConstants
{
	/** The minimum supported time interval. */
	byte MIN_TIME_INTERVAL =
		1;
	
	/** The minimum supported timer resolution. */
	byte TIMER_RESOLUTION =
		1;
}
