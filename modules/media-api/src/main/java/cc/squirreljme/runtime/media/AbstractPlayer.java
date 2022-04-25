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
import javax.microedition.media.Player;
import javax.microedition.media.PlayerListener;

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
	
	/** The state of the player. */
	private volatile int _state =
		Player.UNREALIZED;
	
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
			return this.getState();
		}
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
}
