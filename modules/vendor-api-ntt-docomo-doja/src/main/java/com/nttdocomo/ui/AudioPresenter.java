// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.nttdocomo.ui;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.nttdocomo.ui.NullAudioPresenter;

@Api
public class AudioPresenter
	implements MediaPresenter
{
	@Api
	public int getCurrentTime()
	{
		throw Debugging.todo();
	}
	
	@Api
	@Override
	public MediaResource getMediaResource()
	{
		throw Debugging.todo();
	}
	
	@Api
	@Override
	public void play()
	{
		throw Debugging.todo();
	}
	
	@Api
	@Override
	public void setAttribute(int __attribute, int __value)
	{
		throw Debugging.todo();
	}
	
	@Api
	public void setData(MediaData __data)
	{
		throw Debugging.todo();
	}
	
	@Api
	@Override
	public void setMediaListener(MediaListener __listener)
	{
		Debugging.todoNote("Impl DoJa setMediaListener().");
		/*throw Debugging.todo();*/
	}
	
	@Api
	public void setSound(MediaSound __data)
	{
		throw Debugging.todo();
	}
	
	@Api
	@Override
	public void stop()
	{
		throw Debugging.todo();
	}
	
	/**
	 * Initializes a presenter that is capable of playing audio files.
	 *
	 * @return The resultant audio presenter.
	 * @throws UIException If no resources are available for playback.
	 * @since 2025/05/04
	 */
	@Api
	public static AudioPresenter getAudioPresenter()
		throws UIException
	{
		return AudioPresenter.getAudioPresenter(0);
	}
	
	/**
	 * Initializes a presenter that is capable of playing audio files.
	 *
	 * @param __port The port to play audio under.
	 * @return The resultant audio presenter.
	 * @throws IllegalArgumentException If the port number is negative or
	 * exceeds the number of supported ports.
	 * @throws UIException If no resources are available for playback.
	 * @since 2025/05/04
	 */
	@Api
	public static AudioPresenter getAudioPresenter(int __port)
		throws IllegalArgumentException, UIException
	{
		if (__port < 0)
			throw new IllegalArgumentException("NEGV");
		
		
		throw Debugging.todo();
	}
}
