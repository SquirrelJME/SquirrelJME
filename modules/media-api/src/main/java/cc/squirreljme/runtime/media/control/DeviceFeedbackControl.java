// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.media.control;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import javax.microedition.media.Control;

/**
 * A control which provides device feedback.
 *
 * @since 2026/06/10
 */
@SquirrelJMEVendorApi
public interface DeviceFeedbackControl
	extends Control
{
	/**
	 * Adds a listener for feedback events.
	 *
	 * @param __listener The listener.
	 * @throws NullPointerException On null arguments.
	 * @since 2026/06/10
	 */
	@SquirrelJMEVendorApi
	void addListener(DeviceFeedbackListener __listener)
		throws NullPointerException;
	
	/**
	 * Emit a light event.
	 *
	 * @param __large Is this a large light, or a small light?
	 * @param __ms The duration of the event.
	 * @throws IllegalArgumentException If the event length is negative.
	 * @since 2026/06/10
	 */
	@SquirrelJMEVendorApi
	void emitLight(boolean __large, int __ms)
		throws IllegalArgumentException;
	
	/**
	 * Emit a vibration event.
	 *
	 * @param __ms The duration of the event.
	 * @throws IllegalArgumentException If the event length is negative.
	 * @since 2026/06/10
	 */
	@SquirrelJMEVendorApi
	void emitVibrate(int __ms)
		throws IllegalArgumentException;
	
	/**
	 * Removes a listener for device feedback.
	 *
	 * @param __listener The listener.
	 * @throws NullPointerException On null arguments.
	 * @since 2026/06/10
	 */
	@SquirrelJMEVendorApi
	void removeListener(DeviceFeedbackListener __listener)
		throws NullPointerException;
}
