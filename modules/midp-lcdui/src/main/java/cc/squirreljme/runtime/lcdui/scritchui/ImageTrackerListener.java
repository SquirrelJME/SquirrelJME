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
import javax.microedition.lcdui.Image;
import org.jetbrains.annotations.Async;

/**
 * Listener that is called whenever the image changes.
 *
 * @since 2024/07/20
 */
@SquirrelJMEVendorApi
public interface ImageTrackerListener
{
	/**
	 * This is called whenever the image changes.
	 *
	 * @param __image The image to set.
	 * @since 2024/07/20
	 */
	@SerializedEvent
	@Async.Execute
	void imageUpdated(Image __image);
}
