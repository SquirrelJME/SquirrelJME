// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.media;

import java.util.LinkedList;
import java.util.List;
import javax.microedition.media.Manager;
import javax.microedition.media.MediaException;
import javax.microedition.media.Player;
import javax.microedition.media.PlayerListener;
import javax.microedition.media.TimeBase;

/**
 * Common implementation of players.
 *
 * @since 2022/04/24
 */
public abstract class AbstractPlayer
	implements Player
{
	/** The mime type. */
	private final String mime;
	
	/** Listeners available. */
	private final List<PlayerListener> _listeners =
		new LinkedList<>();
	
	/** The default time base. */
	private final TimeBase _defaultTimeBase =
		Manager.getSystemTimeBase();
	
	/** The loop counter which controls how much the audio replays. */
	protected volatile int loopCounter =
		1;
	
	/** The state of the player. */
	private volatile int _state =
		Player.UNREALIZED;
	
	/** The current timebase. */
	private volatile TimeBase _currentTimebase;
	
	/**
	 * Initializes the base player.
	 * 
	 * @param __mime The MIME type.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/04/24
	 */
	protected AbstractPlayer(String __mime)
		throws NullPointerException
	{
		if (__mime == null)
			throw new NullPointerException("NARG");
		
		this.mime = __mime;
	}
	
	/**
	 * This is called when the player is becoming prefetched.
	 * 
	 * @throws MediaException If the player cannot be prefetched.
	 * @since 2022/04/24
	 */
	protected abstract void becomingPrefetched()
		throws MediaException;
	
	/**
	 * This is called when the player is becoming realized.
	 * 
	 * @throws MediaException If the player cannot be realized.
	 * @since 2022/04/24
	 */
	protected abstract void becomingRealized()
		throws MediaException;
	
	/**
	 * Indicates that the media is about to start.
	 * 
	 * @throws MediaException If the player could not be started.
	 * @since 2022/04/24
	 */
	protected abstract void becomingStarted()
		throws MediaException;
	
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
		
		// {@squirreljme.error EA01 Player has been closed.}
		if (this.getState() == Player.CLOSED)
			throw new IllegalStateException("EA01");
		
		// Add unique listener
		List<PlayerListener> listeners = this._listeners;
		synchronized (this)
		{
			if (!listeners.contains(__l))
				listeners.add(__l);
		}
	}
	
	/**
	 * Sends an event to all listeners.
	 *
	 * @param __key The key used.
	 * @param __val The value used.
	 * @since 2019/06/28
	 */
	protected final void broadcastEvent(String __key, Object __val)
	{
		PlayerListener[] poke;
		
		// Get listeners to poke
		List<PlayerListener> listeners = this._listeners;
		synchronized (this)
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
	public final int getState()
	{
		synchronized (this)
		{
			return this._state;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/15
	 */
	@Override
	public final TimeBase getTimeBase()
	{
		// Use the default time base, if there is no current one
		TimeBase rv = this._currentTimebase;
		if (rv == null)
			return this._defaultTimeBase;
		
		return rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/15
	 */
	@Override
	public final void prefetch()
		throws MediaException
	{
		int state = this.getState();
		if (state == Player.CLOSED)
			throw new IllegalStateException("EA0g");
		
		// Ignore when started or already prefetched
		if (state == Player.STARTED ||
			state == Player.PREFETCHED)
			return;
		
		// Implicit realize, if not yet realized
		if (state == Player.UNREALIZED)
			this.realize();
		
		// Now becoming prefetched
		this.becomingPrefetched();
		this.setState(Player.PREFETCHED);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/15
	 */
	@Override
	public final void realize()
		throws MediaException
	{
		// {@squirreljme.error EA04 Player has been closed.}
		int state = this.getState();
		if (state == Player.CLOSED)
			throw new IllegalStateException("EA04");
		
		// Ignore in these states
		if (state == Player.REALIZED ||
			state == Player.PREFETCHED ||
			state == Player.STARTED)
			return;
		
		// Now becoming realized
		this.becomingRealized();
		this.setState(Player.REALIZED);
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
		
		// {@squirreljme.error EA02 Player has been closed.}
		if (this.getState() == Player.CLOSED)
			throw new IllegalStateException("EA02");
		
		// Remove it
		synchronized (this)
		{
			this._listeners.remove(__l);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/04/24
	 */
	@Override
	public void setLoopCount(int __count)
		throws IllegalArgumentException, IllegalStateException
	{
		// {@squirreljme.error EA0g Invalid loop count. (The count)}
		if (__count == 0 || __count < -1)
			throw new IllegalArgumentException("EA0g " + __count);
		
		// {@squirreljme.error EA0h Cannot set the loop count when the
		// player has started or is closed.}
		int state = this.getState();
		if (state == Player.CLOSED || state == Player.STARTED)
			throw new IllegalStateException("EA0h");
		
		// Set the internal loop counter
		this.loopCounter = __count;
	}
	
	/**
	 * Sets the state.
	 * 
	 * @param __state The state to set.
	 * @throws IllegalArgumentException If the state is not valid.
	 * @since 2022/04/24
	 */
	protected final void setState(int __state)
		throws IllegalArgumentException
	{
		switch (__state)
		{
			case Player.CLOSED:
			case Player.PREFETCHED:
			case Player.STARTED:
			case Player.REALIZED:
			case Player.UNREALIZED:
				this._state = __state;
				break;
			
				// {@squirreljme.error EA0e Invalid state. (The state)}
			default:
				throw new IllegalArgumentException("EA0e " + __state);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/04/24
	 */
	@Override
	public final void setTimeBase(TimeBase __timeBase)
	{
		this._currentTimebase = __timeBase;
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
		int state = this.getState();
		if (state == Player.CLOSED)
			throw new IllegalStateException("EA05");
		
		// Ignore when started
		if (state == Player.STARTED)
			return;
		
		// The player needs to be prefetched first?
		if (state == Player.UNREALIZED ||
			state == Player.REALIZED)
			this.prefetch();
		
		// Is being started now
		this.becomingStarted();
		this.setState(Player.STARTED);
		
		// Send event
		this.broadcastEvent(PlayerListener.STARTED,
			this.getTimeBase().getTime());
	}
}
