// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.media.mld;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.media.AbstractPlayer;
import java.io.InputStream;
import javax.microedition.media.Control;
import javax.microedition.media.MediaException;

/**
 * Player for i-Melody files.
 *
 * @since 2025/05/05
 */
@SquirrelJMEVendorApi
public class IMelodyPlayer
	extends AbstractPlayer
{
	/** The unrealized data input. */
	private volatile InputStream _unrealizedIn;
	
	/**
	 * Initializes the MLD player.
	 *
	 * @param __in The input MLD data.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/05/05
	 */
	public IMelodyPlayer(InputStream __in)
		throws NullPointerException
	{
		super("audio/x-mld");
		
		if (__in == null)
			throw new NullPointerException("NARG");
		
		// For later realization
		this._unrealizedIn = __in;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/05/05
	 */
	@Override
	protected void becomingPrefetched()
		throws MediaException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/05/05
	 */
	@Override
	protected void becomingRealized()
		throws MediaException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/05/05
	 */
	@Override
	protected void becomingStarted()
		throws MediaException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/05/05
	 */
	@Override
	protected void becomingStopped()
		throws MediaException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/05/05
	 */
	@Override
	public void close()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/05/05
	 */
	@Override
	public void deallocate()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/05/05
	 */
	@Override
	protected long determineDuration()
		throws MediaException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/05/05
	 */
	@Override
	public Control getControl(String __control)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/05/05
	 */
	@Override
	public Control[] getControls()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/05/05
	 */
	@Override
	public long getMediaTime()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/05/05
	 */
	@Override
	public long setMediaTime(long __now)
		throws MediaException
	{
		throw Debugging.todo();
	}
}
