// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.nttdocomo.ui;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.nttdocomo.media.AbstractMediaSound;
import com.nttdocomo.io.ConnectionException;
import java.io.IOException;
import java.io.InputStream;
import javax.microedition.io.InputConnection;
import javax.microedition.media.Manager;
import javax.microedition.media.MediaException;
import javax.microedition.media.Player;

/**
 * Wraps a MIDP {@link Player} as a DoJa {@link MediaSound}.
 *
 * @since 2025/05/05
 */
final class __MIDPPlayer__
	extends AbstractMediaSound
{
	/** The currently loaded player. */
	volatile Player _player;
	
	/**
	 * Initializes the source player.
	 *
	 * @param __source The data source.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/05/05
	 */
	__MIDPPlayer__(InputConnection __source)
		throws NullPointerException
	{
		super(__source);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/05/05
	 */
	@Override
	protected void becomingRealized(InputStream __in, MediaResource __copy)
		throws NullPointerException, UIException
	{
		synchronized (this)
		{
			try
			{
				// Load in the player data
				this._player = Manager.createPlayer(__in, null);
			}
			catch (IOException|MediaException __e)
			{
				UIException toss = new UIException(
					UIException.UNSUPPORTED_FORMAT, __e.getMessage());
				toss.initCause(__e);
				throw toss;
			}
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/05/05
	 */
	@Override
	protected void becomingUnrealized()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/05/05
	 */
	@Override
	protected boolean validKey(String __key)
		throws NullPointerException
	{
		if (__key == null)
			throw new NullPointerException("NARG");
		
		// No keys are valid currently
		return false;
	}
}
