// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.nokia.mid.sound;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.annotation.ApiDefinedDeprecated;
import javax.microedition.media.Player;
import javax.microedition.media.PlayerListener;

/**
 * This is a listener for any updates to sounds.
 *
 * @see PlayerListener
 * @since 2022/02/03
 */
@Api
@ApiDefinedDeprecated()
public interface SoundListener
{
	/**
	 * Indicates when the state of a sound has changed.
	 *
	 * @param __sound The sound that changed.
	 * @param __event What happened with this sound?
	 * @see PlayerListener#playerUpdate(Player, String, Object)
	 * @since 2022/02/03
	 */
	@Api
	@ApiDefinedDeprecated()
	void soundStateChanged(Sound __sound, int __event);
}
