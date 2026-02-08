// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.media;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.media.midi.MidiControlPlayer;
import javax.microedition.media.MediaException;
import javax.microedition.media.Player;
import org.intellij.lang.annotations.Language;

/**
 * This is a player which does nothing.
 *
 * @since 2019/04/15
 */
@SquirrelJMEVendorApi
public final class NullPlayer
	extends AbstractPlayer
{
	/**
	 * Initializes the player.
	 *
	 * @param __mime The mime type.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/15
	 */
	@SquirrelJMEVendorApi
	public NullPlayer(@Language("mime-type-reference") String __mime)
		throws NullPointerException
	{
		super(__mime);
		
		this.registerControl(new AbstractVolumeControl(this));
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
	 * @since 2026/01/03
	 */
	@Override
	protected void becomingPrimed()
		throws MediaException
	{
		// Does nothing
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
	 * @since 2026/01/03
	 */
	@Override
	protected void becomingSolvent()
		throws MediaException
	{
		// Does nothing
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/04/24
	 */
	@Override
	@SquirrelJMEVendorApi
	protected boolean becomingStarted()
		throws MediaException
	{
		// Set the playing state
		return true;
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
	 * @since 2026/01/02
	 */
	@Override
	protected void clockFastForward(long __micros)
		throws MediaException
	{
		// Does nothing
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/06/15
	 */
	@Override
	protected long clockGet()
	{
		return 0;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/06/15
	 */
	@Override
	protected void clockSet(long __micros)
		throws MediaException
	{
		// Does nothing
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/12/28
	 */
	@Override
	@SquirrelJMEVendorApi
	public final void becomingDeallocated()
	{
		// There is nothing to be done here, as NullPlayer allocates nothing.
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
		return Player.TIME_UNKNOWN;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2026/01/02
	 */
	@Override
	protected boolean resetFastForward()
	{
		// Everything is always zero length and there is no special
		// fast-forward logic needed
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/06/03
	 */
	@Override
	protected void useVolume(int __volume)
	{
	}
}

