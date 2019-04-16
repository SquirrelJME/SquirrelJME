// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.media;

import javax.microedition.media.Control;
import javax.microedition.media.Manager;
import javax.microedition.media.MediaException;
import javax.microedition.media.Player;
import javax.microedition.media.PlayerListener;
import javax.microedition.media.TimeBase;

/**
 * This is a player which does nothing.
 *
 * @since 2019/04/15
 */
public final class NullPlayer
	implements Player
{
	/** The mime type. */
	private final String mime;
	
	/** The timebase. */
	private TimeBase _timebase =
		Manager.getSystemTimeBase();
	
	/** The state of the player. */
	private volatile int _state =
		UNREALIZED;
	
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
		if (__mime == null)
			throw new NullPointerException("NARG");
		
		this.mime = __mime;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/15
	 */
	@Override
	public final void addPlayerListener(PlayerListener __a)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/15
	 */
	@Override
	public final void close()
	{
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/15
	 */
	@Override
	public final void deallocate()
	{
		if (this._state == STARTED)
			this._state = REALIZED;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/15
	 */
	@Override
	public final String getContentType()
	{
		return this.mime;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/15
	 */
	@Override
	public final Control getControl(String __a)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/15
	 */
	@Override
	public final Control[] getControls()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/15
	 */
	@Override
	public final long getDuration()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/15
	 */
	@Override
	public final long getMediaTime()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/15
	 */
	@Override
	public final int getState()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/15
	 */
	@Override
	public final TimeBase getTimeBase()
	{
		return this._timebase;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/15
	 */
	@Override
	public final void prefetch()
		throws MediaException
	{
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/15
	 */
	@Override
	public final void realize()
		throws MediaException
	{
		if (this._state != UNREALIZED)
			this._state = REALIZED;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/15
	 */
	@Override
	public final void removePlayerListener(PlayerListener __a)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/15
	 */
	@Override
	public final void setLoopCount(int __a)
	{
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/15
	 */
	@Override
	public final long setMediaTime(long __a)
		throws MediaException
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/15
	 */
	@Override
	public final void setTimeBase(TimeBase __a)
		throws MediaException
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/15
	 */
	@Override
	public final void start()
		throws MediaException
	{
		if (this._state != STARTED)
			this._state = STARTED;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/15
	 */
	@Override
	public final void stop()
		throws MediaException
	{
	}
}

