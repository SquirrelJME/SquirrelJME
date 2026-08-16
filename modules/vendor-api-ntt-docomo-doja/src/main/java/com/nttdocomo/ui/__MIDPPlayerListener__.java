// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.nttdocomo.ui;

import cc.squirreljme.runtime.cldc.annotation.KeepWhenCompacting;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.media.mld.IMelodyPlayer;
import cc.squirreljme.runtime.midlet.DoJaRuntime;
import com.keitaiwiki.music.MLDPlayerEvent;
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
	/** The player reference. */
	protected final Reference<AudioPresenter> presenter;
	
	/**
	 * Initializes the listener.
	 *
	 * @param __presenter The presenter to forward to.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/06/03
	 */
	__MIDPPlayerListener__(
		Reference<AudioPresenter> __presenter)
		throws NullPointerException
	{
		if (__presenter == null)
			throw new NullPointerException("NARG");
		
		this.presenter = __presenter;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/06/03
	 */
	@Override
	public void playerUpdate(Player __player, String __eventType,
		Object __eventValue)
	{
		// Ignore if GCed
		AudioPresenter presenter = this.presenter.get();
		if (presenter == null)
			return;
		
		// Ignore if there is no listener
		MediaListener listener = presenter._listener;
		if (listener == null)
			return;
		
		// Ignore if there is no player
		Player player = presenter._current;
		if (player == null)
			return;
		
		// Which event is being handled?
		switch (__eventType)
		{
				// Has started playing
			case PlayerListener.STARTED:

				// Are we starting from the beginning? (play() methods)
				if (!presenter.__isPaused())
					listener.mediaAction(presenter,
						AudioPresenter.AUDIO_PLAYING, 0);

				// Or are we resuming from a paused state? (resume method)
				else
					listener.mediaAction(presenter,
						AudioPresenter.AUDIO_RESTARTED, 0);

				break;
				
				// Has stopped playing
			case PlayerListener.STOPPED:
			case PlayerListener.STOPPED_AT_TIME:
				// Are we stopping with a stop() call, or by END-OF-MEDIA?
				if (!presenter.__isPaused())
					listener.mediaAction(presenter,
						AudioPresenter.AUDIO_STOPPED, 0);

				// Or are we stopping due to a pause() call?
				else
					listener.mediaAction(presenter,
						AudioPresenter.AUDIO_PAUSED, 0);
				break;
				
				// End of song was reached
			case PlayerListener.END_OF_MEDIA:
				if (__player instanceof IMelodyPlayer)
				{
					IMelodyPlayer mldPlayer = (IMelodyPlayer)__player;
					
					// If in DoJa 5+ and this media is looping emit a loop
					// event. Note that looped MLDs do not emit loop events
					if (DoJaRuntime.versionLeast(5, 0))
						if (mldPlayer.lastEndType() ==
							MLDPlayerEvent.EVENT_LOOP)
						{
							listener.mediaAction(presenter,
								AudioPresenter.AUDIO_LOOPED, 0);
							return;
						}
				}
				
				// Other media types, or fell out
				listener.mediaAction(presenter,
					AudioPresenter.AUDIO_COMPLETE, 0);
				break;
		}
	}
}
