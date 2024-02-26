// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.media;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
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
@SquirrelJMEVendorApi
public final class NullPlayer
	extends AbstractPlayer
{
	/** Null volume control. */
	@SquirrelJMEVendorApi
	private final VolumeControl volumeControl =
		new NullVolumeControl();
	
	/**
	 * Initializes the player.
	 *
	 * @param __mime The mime type.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/15
	 */
	@SquirrelJMEVendorApi
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
	@SquirrelJMEVendorApi
	protected void becomingRealized()
	{
		// Does nothing
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/04/24
	 */
	@Override
	@SquirrelJMEVendorApi
	protected void becomingStarted()
		throws MediaException
	{
		// Does nothing
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/04/27
	 */
	@Override
	@SquirrelJMEVendorApi
	protected void becomingStopped()
		throws MediaException
	{
		// Does nothing
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/04/25
	 */
	@Override
	@SquirrelJMEVendorApi
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
	@SquirrelJMEVendorApi
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
	@SquirrelJMEVendorApi
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
	@SquirrelJMEVendorApi
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
	@SquirrelJMEVendorApi
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
	@SquirrelJMEVendorApi
	public final Control[] getControls()
	{
		return new Control[]{this.volumeControl};
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/15
	 */
	@Override
	@SquirrelJMEVendorApi
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
	 */
	@Override
	@SquirrelJMEVendorApi
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
}

