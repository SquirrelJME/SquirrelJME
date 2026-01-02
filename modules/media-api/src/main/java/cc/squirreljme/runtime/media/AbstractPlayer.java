// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.media;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import javax.microedition.io.Connection;
import javax.microedition.media.Control;
import javax.microedition.media.Manager;
import javax.microedition.media.MediaException;
import javax.microedition.media.Player;
import javax.microedition.media.PlayerListener;
import javax.microedition.media.TimeBase;
import org.intellij.lang.annotations.Language;
import org.intellij.lang.annotations.MagicConstant;

/**
 * Common implementation of players.
 *
 * @since 2022/04/24
 */
@SquirrelJMEVendorApi
public abstract class AbstractPlayer
	implements Player
{
	/** The current track position. */
	@SquirrelJMEVendorApi
	protected final TrackPosition trackPosition =
		new TrackPosition();
	
	/** The mime type. */
	@Language("mime-type-reference")
	private final String _mime;
	
	/** Listeners available. */
	@SquirrelJMEVendorApi
	private final List<PlayerListener> _listeners =
		new LinkedList<>();
	
	/** The default time base. */
	@SquirrelJMEVendorApi
	private final TimeBase _defaultTimeBase =
		Manager.getSystemTimeBase();
	
	/** The loop counter which controls how much the audio replays. */
	@SquirrelJMEVendorApi
	protected volatile int _loopCounter =
		1;
	
	/** The number of loops left. */
	@SquirrelJMEVendorApi
	protected volatile int _loopLeft =
		0;
	
	/** The currently available controls. */
	@SquirrelJMEVendorApi
	private volatile AbstractControl[] _controls;
	
	/** The state of the player. */
	@SquirrelJMEVendorApi
	private volatile int _state =
		Player.UNREALIZED;
	
	/** The current timebase. */
	@SquirrelJMEVendorApi
	private volatile TimeBase _currentTimebase;
	
	/** The duration of the media. */
	@SquirrelJMEVendorApi
	private volatile long _cachedDurationMicros =
		Long.MIN_VALUE;
	
	/**
	 * Initializes the base player.
	 * 
	 * @param __mime The MIME type.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/04/24
	 */
	@SquirrelJMEVendorApi
	protected AbstractPlayer(@Language("mime-type-reference") String __mime)
		throws NullPointerException
	{
		if (__mime == null)
			throw new NullPointerException("NARG");
		
		this._mime = __mime;
	}

	/**
	 * This is called when the player is becoming deallocated.
	 * 
	 * @throws MediaException If the player cannot be deallocated.
	 * @since 2025/12/28
	 */
	@SquirrelJMEVendorApi
	protected abstract void becomingDeallocated()
		throws MediaException;
	
	/**
	 * This is called when the player is becoming prefetched.
	 * 
	 * @throws MediaException If the player cannot be prefetched.
	 * @since 2022/04/24
	 */
	@SquirrelJMEVendorApi
	protected abstract void becomingPrefetched()
		throws MediaException;
	
	/**
	 * This is called when the player is becoming realized.
	 * 
	 * @throws MediaException If the player cannot be realized.
	 * @since 2022/04/24
	 */
	@SquirrelJMEVendorApi
	protected abstract void becomingRealized()
		throws MediaException;
	
	/**
	 * Indicates that the media is about to start.
	 *
	 * @return If the state should be set.
	 * @throws MediaException If the player could not be started.
	 * @since 2022/04/24
	 */
	@SquirrelJMEVendorApi
	protected abstract boolean becomingStarted()
		throws MediaException;
	
	/**
	 * Indicates that the player is stopping.
	 * 
	 * @throws MediaException If the player could not be stopped.
	 * @since 2022/04/24
	 */
	@SquirrelJMEVendorApi
	protected abstract void becomingStopped()
		throws MediaException;
	
	/**
	 * Returns the current clock in microseconds.
	 *
	 * @return The current clock.
	 * @since 2025/06/15
	 */
	@SquirrelJMEVendorApi
	protected abstract long clockGet();
	
	/**
	 * Sets the current clock.
	 *
	 * @param __micros The microseconds to set at.
	 * @throws MediaException If the clock could not be set.
	 * @since 2025/06/15
	 */
	@SquirrelJMEVendorApi
	protected abstract void clockSet(long __micros)
		throws MediaException;
	
	/**
	 * {@inheritDoc}
	 * @since 2025/12/31
	 */
	@Override
	public final void close()
	{
		synchronized (this)
		{
			// Do nothing if already closed
			if (this.getState() == Player.CLOSED)
				return;
			
			// Always force close to be set after potential deallocation
			try
			{
				// Deallocate if realized
				if (this.getState() >= Player.REALIZED)
					this.deallocate();
			}
			finally
			{
				// Force the closed state to always occur
				this.setState(Player.CLOSED);
			}
			
			// Send the closed event now that everything is closed
			this.dispatchEvent(PlayerListener.CLOSED, null);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/15
	 */
	@Override
	@SquirrelJMEVendorApi
	public final void deallocate()
		throws IllegalStateException
	{
		int state = this.getState();

		if (state == Player.CLOSED)
			throw new IllegalStateException("EA06");
		
		// Do nothing if already in deallocated state
		if (state == Player.UNREALIZED)
			return;

		try
		{
			// Stop playing first, if it is playing at all
			if (state >= Player.STARTED)
				this.stop();

			// Now becoming deallocated (unrealized)
			this.becomingDeallocated();
			this.setState(Player.UNREALIZED);
		}
		catch (MediaException __e)
		{
			__e.printStackTrace();
		}
	}

	/**
	 * Determines the length of the media in microseconds.
	 * 
	 * @return The media length in microseconds.
	 * @since 2022/04/25
	 */
	@SquirrelJMEVendorApi
	protected abstract long determineDuration()
		throws MediaException;
	
	/**
	 * Uses the given volume.
	 *
	 * @param __volume The volume to use.
	 * @since 2025/06/03
	 */
	@SquirrelJMEVendorApi
	protected abstract void useVolume(int __volume);
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/15
	 */
	@Override
	@SquirrelJMEVendorApi
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
	 * Decrement the loop count.
	 *
	 * @return If the loop has reached zero.
	 * @since 2024/02/26
	 */
	@SquirrelJMEVendorApi
	public final boolean decrementLoop()
	{
		int count = this._loopCounter;
		
		if ((--count) <= 0)
		{
			this._loopCounter = 0;
			return true;
		}
		
		this._loopCounter = count;
		return false;
	}
	
	/**
	 * Dispatches an event to the listener if there is one. 
	 *
	 * @param __key The key for the event.
	 * @param __data The data for the event.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/06/01
	 */
	protected final void dispatchEvent(
		@MagicConstant(valuesFromClass = PlayerListener.class) String __key,
		Object __data)
		throws NullPointerException
	{
		if (__key == null)
			throw new NullPointerException("NARG");
		
		// Send to the dispatcher
		ListenerDispatch.dispatch(this, __key, __data);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/15
	 */
	@Override
	@SquirrelJMEVendorApi
	@Language("mime-type-reference")
	public final String getContentType()
	{
		return this._mime;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/06/03
	 */
	@Override
	public final Control getControl(String __control)
		throws IllegalArgumentException
	{
		if (__control == null)
			throw new IllegalArgumentException("NARG");
		
		synchronized (this)
		{
			// Are there no actual controls?
			AbstractControl[] controls = this._controls;
			if (controls == null)
				return null;
			
			// Is this the given control?
			for (AbstractControl<?> control : controls)
				if (control.matches(__control))
					return control;
		}
		
		// Not found
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/06/23
	 */
	@Override
	public final Control[] getControls()
	{
		synchronized (this)
		{
			// Are there no actual controls?
			AbstractControl[] controls = this._controls;
			if (controls == null)
				return new Control[0];
			return Arrays.copyOf(controls, controls.length,
				AbstractControl[].class);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/04/25
	 */
	@Override
	@SquirrelJMEVendorApi
	public final long getDuration()
		throws IllegalStateException
	{
		// {@squirreljme.error EA0g Stream closed, cannot get duration.}
		if (this.getState() == Player.CLOSED)
			throw new IllegalStateException("EA0g");
		
		// Already has been cached?
		long cachedDuration = this._cachedDurationMicros;
		if (cachedDuration != Long.MIN_VALUE)
			return cachedDuration;
		
		// Otherwise determine the duration
		long newDuration;
		try
		{
			newDuration = this.determineDuration();
			this._cachedDurationMicros = newDuration;
			
		}
		catch (MediaException e)
		{
			return Player.TIME_UNKNOWN;
		}
		
		// Indicate the duration is available now
		this.dispatchEvent(PlayerListener.DURATION_UPDATED, newDuration);
		
		return newDuration;
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
			/* {@squirreljme.error EA08 Cannot obtain the media time for a
			closed player.} */
			if (this.getState() == Player.CLOSED)
				throw new IllegalStateException("EA08");
			
			return this.clockGet();
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/15
	 */
	@Override
	@SquirrelJMEVendorApi
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
	@SquirrelJMEVendorApi
	public final TimeBase getTimeBase()
	{
		// Use the default time base, if there is no current one
		TimeBase rv = this._currentTimebase;
		if (rv == null)
			return this._defaultTimeBase;
		
		return rv;
	}
	
	/**
	 * This is called when playback has been looped via media call.
	 *
	 * @throws MediaException If looping could not be indicated.
	 * @since 2025/06/03
	 */
	@SquirrelJMEVendorApi
	public final void loopViaMedia()
		throws MediaException
	{
		// End of media
		this.dispatchEvent(PlayerListener.END_OF_MEDIA,
			this.getTimeBase().getTime());
		
		// Go back to the start
		this.setMediaTime(0);
		
		// Start event gets resent
		this.dispatchEvent(PlayerListener.STARTED,
			this.getTimeBase().getTime());
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/15
	 */
	@Override
	@SquirrelJMEVendorApi
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
	@SquirrelJMEVendorApi
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
	 * Registers the given control.
	 *
	 * @param __control The control to register.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/06/03
	 */
	@SquirrelJMEVendorApi
	protected final void registerControl(AbstractControl<?> __control)
		throws NullPointerException
	{
		if (__control == null)
			throw new NullPointerException("NARG");
		
		synchronized (this)
		{
			// Add control to the end
			AbstractControl[] controls = this._controls;
			if (controls == null)
				controls = new AbstractControl[]{__control};
			else
			{
				controls = Arrays.copyOf(controls,
					controls.length + 1);
				controls[controls.length - 1] = __control;
			}
			
			// Set new controls
			this._controls = controls;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/15
	 */
	@Override
	@SquirrelJMEVendorApi
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
	@SquirrelJMEVendorApi
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
		this._loopCounter = __count;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/15
	 */
	@Override
	@SquirrelJMEVendorApi
	public final long setMediaTime(long __micros)
		throws MediaException
	{
		synchronized (this)
		{
			/* {@squirreljme.error EA09 Cannot set the media time on a closed
			or unrealized player.} */
			if (this.getState() == Player.CLOSED ||
				this.getState() == Player.UNREALIZED)
				throw new IllegalStateException("EA09");
			
			this.clockSet(__micros);
			return this.clockGet();
		}
	}
	
	/**
	 * Sets the state.
	 * 
	 * @param __state The state to set.
	 * @throws IllegalArgumentException If the state is not valid.
	 * @since 2022/04/24
	 */
	@SquirrelJMEVendorApi
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
	@SquirrelJMEVendorApi
	public final void setTimeBase(TimeBase __timeBase)
	{
		this._currentTimebase = __timeBase;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/15
	 */
	@Override
	@SquirrelJMEVendorApi
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
		
		// Set up the track position for starting
		TrackPosition trackPosition = this.trackPosition;
		TimeBase timeBase = this.getTimeBase();
		trackPosition.timeBase = timeBase;
		trackPosition.basisMicros = timeBase.getTime() -
			trackPosition.stoppedMicros;
		
		// Reset the loop count
		this._loopLeft = this._loopCounter;
		
		// Is being started now
		if (this.becomingStarted())
		{
			this.setState(Player.STARTED);
		
			// Send event
			this.dispatchEvent(PlayerListener.STARTED, timeBase.getTime());
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/04/27
	 */
	@Override
	@SquirrelJMEVendorApi
	public final void stop()
		throws MediaException
	{
		// {@squirreljme.error EA06 Null Player has been closed.}
		int state = this.getState();
		if (state == Player.CLOSED)
			throw new IllegalStateException("EA06");
		
		// Ignore these
		if (state == Player.UNREALIZED ||
			state == Player.REALIZED ||
			state == Player.PREFETCHED)
			return;
		
		// Becoming stopped
		this.becomingStopped();
		
		// Make sure the state stays valid
		if (state != Player.CLOSED &&
			state != Player.UNREALIZED &&
			state != Player.REALIZED &&
			state != Player.PREFETCHED)
			this.setState(Player.PREFETCHED);
		
		// Send stop event
		this.dispatchEvent(PlayerListener.STOPPED,
			this.getTimeBase().getTime());
	}
	
	/**
	 * A stop occurred via media playback.
	 *
	 * @throws MediaException On any error.
	 * @since 2024/02/26
	 */
	@SquirrelJMEVendorApi
	public final void stopViaMedia()
		throws MediaException
	{
		// Send end of media
		this.dispatchEvent(PlayerListener.END_OF_MEDIA,
			this.getTimeBase().getTime());
		
		// We stopped via media, so go back to the start
		try
		{
			this.setMediaTime(0);
		}
		catch (MediaException ignored)
		{
		}
		
		// Stop playback
		this.stop();
	}
	
	/**
	 * Handles the given event.
	 *
	 * @param __eventType The type of event this is.
	 * @param __eventValue The event value.
	 * @param __nanoTime The time this event occurred.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/06/03
	 */
	@SquirrelJMEVendorApi
	final void __handleEvent(String __eventType, Object __eventValue,
		long __nanoTime)
		throws NullPointerException
	{
		if (__eventType == null)
			throw new NullPointerException("NARG");
		
		// Send to all listeners
		List<PlayerListener> listeners = this._listeners;
		synchronized (this)
		{
			for (PlayerListener listener : listeners)
			{
				// Skip blanks if they happen to be in here
				if (listener == null)
					continue;
				
				// Otherwise forward to it
				try
				{
					listener.playerUpdate(this, __eventType,
						__eventValue);
				}
				
				// Ignore any normal runtime exceptions
				catch (RuntimeException __e)
				{
					__e.printStackTrace(System.err);
				}
			}
		}
	}
	
	/**
	 * Closes the connection and wraps any {@link IOException} with
	 * a {@link MediaException}.
	 *
	 * @param __in The input connection to close.
	 * @throws MediaException If any {@link IOException} occurred.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/12/31
	 */
	public static final void closeConnection(Connection __in)
		throws MediaException, NullPointerException
	{
		if (__in == null)
			throw new NullPointerException("NARG");
		
		// Close the input connection
		try
		{
			__in.close();
		}
		catch (IOException __e)
		{
			MediaException toss = new MediaException(__e.getMessage());
			toss.initCause(__e);
			throw toss;
		}
	}
}
