// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.media.control;

import cc.squirreljme.runtime.cldc.annotation.Api;
import javax.microedition.media.Control;

/**
 * Allows for the control of media volume.
 *
 * @since 2025/06/03
 */
@Api
public interface VolumeControl
	extends Control
{
	/**
	 * Returns the current volume level.
	 *
	 * @return The volume level.
	 * @since 2025/06/03
	 */
	@Api
	int getLevel();
	
	/**
	 * Checks if the current volume level is muted.
	 *
	 * @return If the volume level is muted.
	 * @since 2025/06/03
	 */
	@Api
	boolean isMuted();
	
	/**
	 * Sets the volume level.
	 *
	 * @param __volume The volume level to set, if out of range
	 * of {@code [0, 100]} then it will be clamped.
	 * @return The actual volume that was set.
	 * @since 2025/06/03
	 */
	@Api
	int setLevel(int __volume);
	
	/**
	 * Sets whether the media is to be muted.
	 *
	 * @param __muted If the media should be muted or not.
	 * @since 2025/06/03
	 */
	@Api
	void setMute(boolean __muted);
}


