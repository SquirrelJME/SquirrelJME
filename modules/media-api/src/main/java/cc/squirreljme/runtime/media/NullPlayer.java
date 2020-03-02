// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.media;

import java.util.LinkedList;
import java.util.List;
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
	
	/** Listeners available. */
	private final List<PlayerListener> _listeners =
		new LinkedList<>();
	
	/** The timebase. */
	private TimeBase _timebase =
		Manager.getSystemTimeBase();
	
	/** The state of the player. */
	private volatile int _state = Player.UNREALIZED;
	
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
	public final void addPlayerListener(PlayerListener __l)
	{
		// Ignore?
		if (__l == null)
			return;
		
		// {@squirreljme.error EA01 Null Player has been closed.}
		if (this._state == Player.CLOSED)
			throw new IllegalStateException("EA01");
		
		// Add unique listener
		List<PlayerListener> listeners = this._listeners;
		synchronized (listeners)
		{
			if (!listeners.contains(__l))
				listeners.add(__l);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/15
	 */
	@Override
	public final void close()
	{
		if (this._state != Player.CLOSED)
		{
			this._state = Player.CLOSED;
			
			// Send event
			this.__event(PlayerListener.CLOSED, null);
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
		if (this._state == Player.CLOSED)
			throw new IllegalStateException("EA03");
		
		if (this._state == Player.STARTED)
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
			this._state = Player.REALIZED;
		}
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
	public final Control getControl(String __v)
	{
		// {@squirreljme.error AE07 No control specified.}
		if (__v == null)
			throw new IllegalArgumentException("AE07");
		
		if (__v.equals("VolumeControl") ||
			__v.equals("javax.microedition.media.control.VolumeControl"))
			return new NullVolumeControl();
		return null;
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
		// {@squirreljme.error EA04 Null Player has been closed.}
		if (this._state == Player.CLOSED)
			throw new IllegalStateException("EA04");
		
		if (this._state != Player.UNREALIZED)
			this._state = Player.REALIZED;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/15
	 */
	@Override
	public final void removePlayerListener(PlayerListener __l)
	{
		// Ignore?
		if (__l == null)
			return;
		
		// {@squirreljme.error EA02 Null Player has been closed.}
		if (this._state == Player.CLOSED)
			throw new IllegalStateException("EA02");
		
		// Remove it
		List<PlayerListener> listeners = this._listeners;
		synchronized (listeners)
		{
			listeners.remove(__l);
		}
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
		// {@squirreljme.error EA05 Null Player has been closed.}
		if (this._state == Player.CLOSED)
			throw new IllegalStateException("EA05");
		
		if (this._state != Player.STARTED || this._state == Player.PREFETCHED)
		{
			this._state = Player.STARTED;
			
			// Send event
			this.__event(PlayerListener.STARTED,
				Long.valueOf(this._timebase.getTime()));
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
		if (this._state == Player.CLOSED)
			throw new IllegalStateException("EA06");
		
		if (this._state != Player.STARTED)
		{
			this._state = Player.PREFETCHED;
			
			// Send event
			this.__event(PlayerListener.STOPPED,
				Long.valueOf(this._timebase.getTime()));
		}
	}
	
	/**
	 * Sends an event to all listeners.
	 *
	 * @param __key The key used.
	 * @param __val The value used.
	 * @since 2019/06/28
	 */
	private final void __event(String __key, Object __val)
	{
		PlayerListener[] poke;
		
		// Get listeners to poke
		List<PlayerListener> listeners = this._listeners;
		synchronized (listeners)
		{
			poke = listeners.<PlayerListener>toArray(
				new PlayerListener[listeners.size()]);
		}
		
		// Poke them all
		for (PlayerListener pl : poke)
			try
			{
				pl.playerUpdate(this, __key, __val);
			}
			catch (Throwable t)
			{
				t.printStackTrace();
			}
	}
}

