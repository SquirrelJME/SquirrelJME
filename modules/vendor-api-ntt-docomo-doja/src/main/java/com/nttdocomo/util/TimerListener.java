// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.nttdocomo.util;

import java.util.EventListener;

/**
 * This is a listener for any timer events.
 * 
 * @since 2022/10/10
 */
public interface TimerListener
	extends EventListener
{
	/**
	 * This method is called whenever the timer interval occurs.
	 * 
	 * @param __source The source timer.
	 * @since 2022/10/10
	 */
	void timerExpired(Timer __source);
}
