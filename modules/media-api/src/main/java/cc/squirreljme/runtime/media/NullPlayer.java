// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.media;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import javax.microedition.media.Control;
import javax.microedition.media.MediaException;
import javax.microedition.media.Player;
import javax.microedition.media.PlayerListener;
import javax.microedition.media.control.VolumeControl;

/**
 * This is a player which does nothing.
 *
 * @since 2019/04/15
 */
public final class NullPlayer
	extends AbstractPlayer
{
	/** Null volume control. */
	private final VolumeControl volumeControl =
		new NullVolumeControl();
	
	/**
	 * Initializes the player.
	 *
	 * @param __mime The mime type.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/15
	 */
	public NullPlayer(String __mime)
		throws NullPointerException
	{
		super(__mime);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/04/24
	 */
	@Override
	protected void becomingRealized()
	{
		// Does nothing
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/04/24
	 */
	@Override
	protected void becomingStarted()
		throws MediaException
	{
		// Does nothing
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/04/25
	 */
	@Override
	protected long determineDuration()
	{
		// There is no duration for null media
		return 0;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/04/24
	 */
	@Override
	protected void becomingPrefetched()
		throws MediaException
	{
		// Does nothing
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/15
	 */
	@Override
	public final void close()
	{
		if (this.getState() != Player.CLOSED)
		{
			this.setState(Player.CLOSED);
			
			// Send event
			this.broadcastEvent(PlayerListener.CLOSED, null);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/15
	 */
	@Override
	public final void deallocate()
	{
		// {@squirreljme.error EA03 Null Player has been closed.}
		if (this.getState() == Player.CLOSED)
			throw new IllegalStateException("EA03");
		
		if (this.getState() == Player.STARTED)
		{
			// Implicit stop state
			try
			{
				this.stop();
			}
			catch (MediaException e)
			{
				e.printStackTrace();
			}
			
			// Become realized
			this.setState(Player.REALIZED);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/15
	 */
	@Override
	public final Control getControl(String __control)
	{
		// {@squirreljme.error EA07 No control specified.}
		if (__control == null)
			throw new IllegalArgumentException("EA07");
		
		if (__control.equals("VolumeControl") ||
			__control.equals("javax.microedition.media.control.VolumeControl"))
			return this.volumeControl;
		
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/15
	 */
	@Override
	public final Control[] getControls()
	{
		return new Control[]{this.volumeControl};
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/15
	 */
	@Override
	public final long getMediaTime()
	{
		synchronized (this)
		{
			// {@squirreljme.error EA08 Cannot obtain the media time for a
			// closed null stream.}
			if (this.getState() == Player.CLOSED)
				throw new IllegalStateException("EA08");
			
			return Player.TIME_UNKNOWN;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/15
	 * @param __now
	 */
	@Override
	public final long setMediaTime(long __now)
		throws MediaException
	{
		synchronized (this)
		{
			// {@squirreljme.error EA09 Cannot set the media time on a null
			// stream.}
			if (this.getState() == Player.CLOSED ||
				this.getState() == Player.UNREALIZED)
				throw new IllegalStateException("EA09");
			
			return Player.TIME_UNKNOWN;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/15
	 */
	@Override
	public final void stop()
		throws MediaException
	{
		// {@squirreljme.error EA06 Null Player has been closed.}
		if (this.getState() == Player.CLOSED)
			throw new IllegalStateException("EA06");
		
		if (this.getState() != Player.STARTED)
		{
			this.setState(Player.PREFETCHED);
			
			// Send event
			this.broadcastEvent(PlayerListener.STOPPED,
				this.getTimeBase().getTime());
		}
	}
}

