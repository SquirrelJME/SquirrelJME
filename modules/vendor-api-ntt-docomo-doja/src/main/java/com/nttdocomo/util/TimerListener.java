// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.nttdocomo.util;

import cc.squirreljme.runtime.cldc.annotation.Api;
import java.util.EventListener;

/**
 * This is a listener for any timer events.
 * 
 * @since 2022/10/10
 */
@Api
public interface TimerListener
	extends EventListener
{
	/**
	 * This method is called whenever the timer interval occurs.
	 * 
	 * @param __source The source timer.
	 * @since 2022/10/10
	 */
	@Api
	void timerExpired(Timer __source);
}
