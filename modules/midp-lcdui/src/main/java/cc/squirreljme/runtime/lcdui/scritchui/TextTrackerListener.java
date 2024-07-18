// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.scritchui;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.lcdui.SerializedEvent;
import org.jetbrains.annotations.Async;

/**
 * Listener that is called whenever the text changes.
 *
 * @since 2024/07/18
 */
@SquirrelJMEVendorApi
public interface TextTrackerListener
{
	/**
	 * This is called whenever the text changes.
	 *
	 * @param __t The text to set.
	 * @since 2024/07/18
	 */
	@SerializedEvent
	@Async.Execute
	void textUpdated(String __t);
}
