// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.nokia.mid.sound;

import cc.squirreljme.runtime.cldc.annotation.KeepWhenCompacting;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import java.lang.ref.Reference;
import javax.microedition.media.Player;
import javax.microedition.media.PlayerListener;

/**
 * Handles player events.
 *
 * @since 2025/06/03
 */
@KeepWhenCompacting
class __MIDPPlayerListener__
	implements PlayerListener
{
	/** The actual player reference. */
	protected final Reference<Sound> sound;

	/**
	 * Initializes the MIDP listener wrapper.
	 *
	 * @param sound The {@link Sound} instance to forward events to.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/12/24
	 */
	__MIDPPlayerListener__(
		Reference<Sound> __sound)
		throws NullPointerException
	{
		if (__sound == null)
			throw new NullPointerException("NARG");
		
		this.sound = __sound;
	}

	/**
	 * {@inheritDoc}
	 * @since 2025/06/03
	 */
	@Override
	public void playerUpdate(Player __player, String __eventType,
		Object __eventValue)
	{
		Sound sound = this.sound.get();
		if (sound == null)
			return;
		
		SoundListener listener = sound._listener;
		if (listener == null)
			return;
		
		Player player = sound._player;
		if (player == null)
			return;
		
		// Which event is being handled?
		switch (__eventType)
		{
				// Has started playing
			case PlayerListener.STARTED:
				listener.soundStateChanged(sound, Sound.SOUND_PLAYING);
				break;
				
				// Has stopped playing for any known reason
			case PlayerListener.STOPPED:
			case PlayerListener.STOPPED_AT_TIME:
				// End of media also returns a standard STOPPED, Nokia Sound
				// Doesn't exactly specify what should be done by the listener
				// when it reaches EOM.
			case PlayerListener.END_OF_MEDIA:
				listener.soundStateChanged(sound, Sound.SOUND_STOPPED);
				break;
				
				// Player has been closed (released)
			case PlayerListener.CLOSED:
				listener.soundStateChanged(sound, Sound.SOUND_UNINITIALIZED);
				break;
		}
	}
}
