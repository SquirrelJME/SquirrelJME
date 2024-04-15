// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm;

/**
 * This represents the type of feedback to perform for a device.
 *
 * @since 2019/10/05
 */
public interface DeviceFeedbackType
{
	/** Vibrate. */
	byte VIBRATE =
		1;
	
	/** Blink/Pulse LED, be aware of conditions such as epilepsy. */
	byte BLINK_LED =
		2;
	
	/** The number of feedback types. */
	byte NUM_FEEDBACK_TYPES =
		3;
}

