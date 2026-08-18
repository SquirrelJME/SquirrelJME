// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.media;

import cc.squirreljme.jvm.mle.AudioStreamShelf;
import cc.squirreljme.jvm.mle.brackets.AudioStreamBracket;
import cc.squirreljme.jvm.mle.callbacks.AudioStreamSnoop;
import cc.squirreljme.jvm.mle.constants.AudioStreamChannels;
import cc.squirreljme.jvm.mle.constants.AudioStreamFormat;
import cc.squirreljme.jvm.mle.constants.AudioStreamRate;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.ServiceLoader;
import javax.microedition.io.Connection;
import javax.microedition.media.Control;
import javax.microedition.media.Manager;
import javax.microedition.media.MediaException;
import javax.microedition.media.Player;
import javax.microedition.media.PlayerListener;
import javax.microedition.media.TimeBase;
import net.multiphasicapps.collections.UnmodifiableArrayList;
import org.intellij.lang.annotations.Language;
import org.intellij.lang.annotations.MagicConstant;

/**
 * Common base implementation for any MIDP {@link Player}.
 *
 * @since 2022/04/24
 */
@SquirrelJMEVendorApi
public abstract class AbstractPlayer
	implements Player
{
	/** Cached player service providers. */
	private static volatile PlayerProvider[] _providers;
	
	/** Single sourced audio stream. */
	private static volatile AudioStreamBracket _stream;
	
	/** Global audio snoop. */
	private static volatile AudioStreamSnoop _snoop;
	
	/** Stream format. */
	@MagicConstant(valuesFromClass = AudioStreamFormat.class)
	private static volatile int _streamFormat =
		AudioStreamFormat.AUTOMATIC;
	
	/** Stream rate. */
	@MagicConstant(valuesFromClass = AudioStreamRate.class)
	private static volatile int _streamRate =
		AudioStreamRate.AUTOMATIC;
	
	/** Stream channels. */
	@MagicConstant(valuesFromClass = AudioStreamChannels.class)
	private static volatile int _streamChannels =
		AudioStreamChannels.AUTOMATIC;
	
	/** The current track position. */
	@SquirrelJMEVendorApi
	protected final TrackPosition trackPosition =
		new TrackPosition();
	
	/** The mime type. */
	@Language("mime-type-reference")
	private final String _mime;
	
	/** Listeners available. */
	private final List<PlayerListener> _listeners =
		new LinkedList<>();
	
	/** The default time base. */
	private final TimeBase _defaultTimeBase =
		Manager.getSystemTimeBase();
	
	/** The loop counter which controls how much the audio replays. */
	private volatile int _loopCounter =
		1;
	
	/** The currently available controls. */
	private volatile AbstractControl<?>[] _controls;
	
	/** Cancel dispatch of events during fast-forwarding. */
	volatile boolean _ffNoDispatch;
	
	/** The state of the player. */
	@MagicConstant(valuesFromClass = Player.class)
	private volatile int _state =
		Player.UNREALIZED;
	
	/** The current timebase. */
	private volatile TimeBase _currentTimebase;
	
	/** The duration of the media. */
	private volatile long _cachedDurationMicros =
		Long.MIN_VALUE;
	
	/** Is the audio stream primed? */
	private volatile boolean _isPrimed;
	
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
	 * @see #becomingRealized()
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
	 * This is called to open the underlying around stream so that any calls
	 * to {@link Player#start()} and {@link Player#stop()} do not need to
	 * attach to or disconnect from the sound card.
	 *
	 * @throws MediaException If priming failed.
	 * @see #becomingSolvent()
	 * @since 2026/01/03
	 */
	@SquirrelJMEVendorApi
	protected abstract void becomingPrimed()
		throws MediaException;
	
	/**
	 * This is called when the player is becoming realized.
	 * 
	 * @throws MediaException If the player cannot be realized.
	 * @see #becomingDeallocated()
	 * @since 2022/04/24
	 */
	@SquirrelJMEVendorApi
	protected abstract void becomingRealized()
		throws MediaException;
	
	/**
	 * This is called to close the underlying around stream.
	 *
	 * @throws MediaException If priming failed.
	 * @see #becomingPrimed()
	 * @since 2026/01/03
	 */
	@SquirrelJMEVendorApi
	protected abstract void becomingSolvent()
		throws MediaException;
	
	/**
	 * Indicates that the media is about to start.
	 *
	 * @return If the state should be set.
	 * @throws MediaException If the player could not be started.
	 * @see #becomingStopped()
	 * @since 2022/04/24
	 */
	@SquirrelJMEVendorApi
	protected abstract boolean becomingStarted()
		throws MediaException;
	
	/**
	 * Indicates that the player is stopping.
	 * 
	 * @throws MediaException If the player could not be stopped.
	 * @see #becomingStarted()
	 * @since 2022/04/24
	 */
	@SquirrelJMEVendorApi
	protected abstract void becomingStopped()
		throws MediaException;
	
	/**
	 * Sets the current clock via fast-forwarding, this should generally
	 * not output any audio. While this method is being called no events
	 * will be dispatched.
	 * 
	 * If fast-forwarding is not needed then this should just
	 * call {@link #clockSet(long)}.
	 *
	 * @param __micros The microseconds to fast-forward to.
	 * @throws MediaException If the clock could not be set.
	 * @since 2026/01/01
	 */
	@SquirrelJMEVendorApi
	protected abstract void clockFastForward(long __micros)
		throws MediaException;
	
	/**
	 * Returns the current clock in microseconds.
	 *
	 * @return The current clock, or {@link Player#TIME_UNKNOWN} if the time is
	 * not valid.
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
	 * Returns whether the player/media currently requires the reset and
	 * fast-forward method for {@link #setMediaTime(long)} to function.
	 *
	 * @return If reset then fast-forward is required for this media to
	 * properly play.
	 * @since 2026/01/02
	 */
	@SquirrelJMEVendorApi
	protected abstract boolean resetFastForward();
	
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
			if (this.getState() <= Player.CLOSED)
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
			
			// Does the sound card need to become solvent?
			if (this._isPrimed)
				try
				{
					this._isPrimed = false;
					this.becomingSolvent();
				}
				catch (MediaException __e)
				{
					__e.printStackTrace();
				}
		}
		
		// Send the closed event now that everything is closed
		this.dispatchEvent(PlayerListener.CLOSED, null);
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
		synchronized (this)
		{
			int state = this.getState();
			if (state <= Player.CLOSED)
				throw new IllegalStateException("EA06");
			
			// Do nothing if already in deallocated state
			if (state <= Player.UNREALIZED)
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
		if (this.getState() <= Player.CLOSED)
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
		synchronized (this)
		{
			// Infinite loop?
			int count = this._loopCounter;
			if (count == -1)
				return false;
			
			// Otherwise stop when the counter reaches zero
			if ((--count) <= 0)
			{
				this._loopCounter = 0;
				return true;
			}
			
			this._loopCounter = count;
			return false;
		}
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
		
		// Player is not permitted to dispatch events
		synchronized (this)
		{
			if (this._ffNoDispatch)
				return;
		}
		
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
			AbstractControl<?>[] controls = this._controls;
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
			AbstractControl<?>[] controls = this._controls;
			if (controls == null)
				return new Control[0];
			
			// Need to use the base return type as the caller may be modifying
			// the array
			return Arrays.copyOf(controls, controls.length,
				Control[].class);
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
		if (this.getState() <= Player.CLOSED)
			throw new IllegalStateException("EA0g");
		
		// Lock
		long newDuration;
		synchronized (this)
		{
			// Already has been cached?
			long cachedDuration = this._cachedDurationMicros;
			if (cachedDuration != Long.MIN_VALUE)
				return cachedDuration;
			
			// Otherwise determine the duration
			try
			{
				// Prefetch needs to happen for the duration to be known
				this.prefetch();
				
				// Determine the duration
				newDuration = this.determineDuration();
				this._cachedDurationMicros = newDuration;
			}
			catch (MediaException e)
			{
				return Player.TIME_UNKNOWN;
			}
		}
		
		// Indicate the duration is available now
		this.dispatchEvent(PlayerListener.DURATION_UPDATED, newDuration);
		
		// Return the calculated duration
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
			int state = this.getState();
			if (state <= Player.CLOSED)
				throw new IllegalStateException("EA08");
			
			// Update the cached time and use the up-to-date one, or if the
			// time is unknown and we knew it before return the previously
			// cached time
			long internalClock = this.clockGet();
			if (internalClock != Player.TIME_UNKNOWN)
				this.trackPosition.trackMicros = internalClock;
			else
			{
				// If the player is not started, prefer the time it stopped at
				if (state < Player.STARTED)
					internalClock = this.trackPosition.stoppedMicros;
				
				// Otherwise return the time that it is currently tracked at
				else
					internalClock = this.trackPosition.trackMicros;
			}
			
			// Return the time
			return internalClock;
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
		synchronized (this)
		{
			int state = this.getState();
			if (state <= Player.CLOSED)
				throw new IllegalStateException("EA0g");
			
			// Do nothing if already prefetched
			if (state >= Player.PREFETCHED)
				return;
			
			// Implicit realize, if not yet realized
			if (state < Player.REALIZED)
				this.realize();
			
			// Now becoming prefetched
			this.becomingPrefetched();
			this.setState(Player.PREFETCHED);
		}
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
		synchronized (this)
		{
			// {@squirreljme.error EA04 Player has been closed.}
			int state = this.getState();
			if (state <= Player.CLOSED)
				throw new IllegalStateException("EA04");
			
			// Do nothing if already realized
			if (state >= Player.REALIZED)
				return;
			
			// Now becoming realized
			this.becomingRealized();
			this.setState(Player.REALIZED);
		}
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
			AbstractControl<?>[] controls = this._controls;
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
		if (this.getState() <= Player.CLOSED)
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
	public final void setLoopCount(int __count)
		throws IllegalArgumentException, IllegalStateException
	{
		// {@squirreljme.error EA0g Invalid loop count. (The count)}
		if (__count == 0 || __count < -1)
			throw new IllegalArgumentException("EA0g " + __count);
		
		synchronized (this)
		{
			// {@squirreljme.error EA0h Cannot set the loop count when the
			// player has started or is closed.}
			int state = this.getState();
			if (state <= Player.CLOSED || state >= Player.STARTED)
				throw new IllegalStateException("EA0h");
			
			// Set the internal loop counter
			this._loopCounter = __count;
		}
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
			int state = this.getState();
			if (state <= Player.UNREALIZED)
				throw new IllegalStateException("EA09");
			
			// If the duration is unknown, then all we can really do is move the
			// media back to the start. Assuming the player doesn't support this
			// call, we'll get the proper MediaException anyway.
			long duration = this.getDuration();
			if (duration == Player.TIME_UNKNOWN)
				__micros = 0;
			
			// If the clock is set at exactly the end of the track or past it,
			// set it to just before the track ends otherwise fast-forward
			// and other sets may not be able to find the end of track as it
			// extends to the bound
			if (__micros >= duration)
				__micros = duration - 1;

			// Negative microsecond values effectively set media time to 0
			// according to JSR-135.
			if (__micros < 0)
				__micros = 0;
			
			// If not started, update the stopped and track time
			if (state < Player.STARTED)
				this.trackPosition.stoppedMicros = __micros;
			
			// If this does not require reset then fast-forward, just set
			// the clock directly
			if (!this.resetFastForward())
				this.clockSet(__micros);
			
			// Otherwise reset, then fast-forward
			else
				try
				{
					// Do not dispatch events during fast forwarding, as
					// this will very much break everything
					this._ffNoDispatch = true;
					
					// Fast-forward the clock
					this.clockFastForward(__micros);
				}
				finally
				{
					this._ffNoDispatch = false;
				}
			
			// Return the set clock time
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
	protected final void setState(
		@MagicConstant(valuesFromClass = Player.class) int __state)
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
	@SuppressWarnings("AssignmentUsedAsCondition")
	@Override
	@SquirrelJMEVendorApi
	public final void start()
		throws MediaException
	{
		boolean dispatch = false;
		TimeBase timeBase = this.getTimeBase();
		
		synchronized (this)
		{
			// {@squirreljme.error EA05 Null Player has been closed.}
			int state = this.getState();
			if (state <= Player.CLOSED)
				throw new IllegalStateException("EA05");
			
			// Ignore when started
			if (state >= Player.STARTED)
				return;
			
			// The player needs to be prefetched first?
			if (state < Player.PREFETCHED)
				this.prefetch();
			
			// Does the sound card need to be primed?
			if (!this._isPrimed)
			{
				this.becomingPrimed();
				this._isPrimed = true;
			}
			
			// If the loop count is zero or invalid, force it to be valid
			int loopCounter = this._loopCounter;
			if (loopCounter == 0)
			{
				loopCounter = 1;
				this._loopCounter = loopCounter;
			}
			
			// Set up the track position for starting
			TrackPosition trackPosition = this.trackPosition;
			trackPosition.timeBase = timeBase;
			trackPosition.basisMicros =
				timeBase.getTime() - trackPosition.stoppedMicros;
			
			// Is being started now
			if (dispatch = this.becomingStarted())
				this.setState(Player.STARTED);
		}
		
		// Send event
		if (dispatch)
			this.dispatchEvent(PlayerListener.STARTED,
				timeBase.getTime());
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
		synchronized (this)
		{
			// {@squirreljme.error EA06 Null Player has been closed.}
			int state = this.getState();
			if (state <= Player.CLOSED)
				throw new IllegalStateException("EA06");
			
			// Ignore if already stopped
			if (state <= Player.PREFETCHED)
				return;
			
			// Request the media time before stopping so that it is kept
			// around, additionally record the time of stopping
			long micros = this.getMediaTime();
			if (micros > Player.TIME_UNKNOWN)
				this.trackPosition.stoppedMicros = micros;
			
			// Becoming stopped
			this.becomingStopped();
			this.setState(Player.PREFETCHED);
		}
		
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
			// Set the media time
			this.setMediaTime(0);
		}
		catch (IllegalStateException | MediaException ignored)
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
	@SquirrelJMEVendorApi
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
	
	/**
	 * Returns the set of player providers.
	 *
	 * @return The set of player providers.
	 * @since 2026/06/27
	 */
	@SquirrelJMEVendorApi
	public static Iterable<PlayerProvider> providers()
	{
		// Do we need to load in the service providers?
		PlayerProvider[] providers = AbstractPlayer._providers;
		if (providers == null)
		{
			// Load in
			List<PlayerProvider> all = new ArrayList<>();
			for (PlayerProvider provider :
				ServiceLoader.load(PlayerProvider.class))
				all.add(provider);
			
			// Setup static copy
			providers = all.toArray(new PlayerProvider[all.size()]);
			synchronized (AbstractPlayer.class)
			{
				AbstractPlayer._providers = providers; 
			}
		}
		
		// Iterate over the providers
		return UnmodifiableArrayList.<PlayerProvider>of(providers);
	}
	
	/**
	 * Returns the current audio snoop.
	 *
	 * @return The current audio snoop.
	 * @since 2026/01/08
	 */
	@SquirrelJMEVendorApi
	public static AudioStreamSnoop snoop()
	{
		return AbstractPlayer._snoop;
	}
	
	/**
	 * Sets the audio snoop.
	 *
	 * @param __snoop The snoop to set, {@code null} clears it.
	 * @since 2026/01/08
	 */
	@SquirrelJMEVendorApi
	public static void snoop(AudioStreamSnoop __snoop)
	{
		// Clear
		synchronized (AbstractPlayer.class)
		{
			AbstractPlayer._snoop = null;
		}
	}
	
	/**
	 * This will open the audio stream with the specified format, if it is
	 * better, otherwise attempts to open another stream.
	 *
	 * @param __format The format used, if {@code -1} this will use the
	 * preferred format specified by the {@link AudioStreamBracket}.
	 * @param __rate The rate, if {@code -1} this will use the
	 * preferred rate specified by the {@link AudioStreamBracket}.
	 * @param __channels The channels, if {@code -1} this will use the
	 * preferred channels specified by the {@link AudioStreamBracket}.
	 * @return The audio stream.
	 * @throws MediaException If the stream could not be opened.
	 * @since 2026/01/08
	 */
	@SquirrelJMEVendorApi
	public static AudioStreamBracket stream(
		@MagicConstant(valuesFromClass = AudioStreamFormat.class)
			int __format,
		@MagicConstant(valuesFromClass = AudioStreamRate.class)
			int __rate,
		@MagicConstant(valuesFromClass = AudioStreamChannels.class)
			int __channels)
		throws MediaException
	{
		synchronized (AbstractPlayer.class)
		{
			// Is there already a stream?
			AudioStreamBracket common = AbstractPlayer._stream;
			if (common != null)
			{
				// Get the existing format
				int format = AbstractPlayer._streamFormat;
				int rate = AbstractPlayer._streamRate;
				int channels = AbstractPlayer._streamChannels;
				
				// If the common stream is better, use it
				if (format >= __format || rate >= __rate ||
					channels >= __channels)
					return common;
			}
			
			// Otherwise set up a new stream
			try
			{
				// Open stream
				AudioStreamBracket rv = AudioStreamShelf.stream(__format,
					__rate, __channels);
				
				// If no common stream is open yet, make this stream the
				// common stream
				if (common == null)
				{
					// Cache it
					AbstractPlayer._stream = rv;
					
					// Set format
					AbstractPlayer._streamFormat = __format;
					AbstractPlayer._streamRate = __rate;
					AbstractPlayer._streamChannels = __channels;
				}
				
				// Use this stream
				return rv;
			}
			catch (MLECallError __e)
			{
				if (Debugging.ENABLED)
					__e.printStackTrace();
				
				MediaException toss = new MediaException(__e.getMessage());
				toss.initCause(__e);
				throw toss;
			}
		}
	}
	
	/**
	 * Disconnects the stream given from {@link #stream(int, int, int)}.
	 *
	 * @param __stream The stream to disconnect.
	 * @param __force Force close of the common stream.
	 * @throws MediaException If the stream could not be closed.
	 * @throws NullPointerException On null arguments.
	 * @since 2026/01/08
	 */
	@SquirrelJMEVendorApi
	public static void streamDisconnect(AudioStreamBracket __stream,
		boolean __force)
		throws MediaException, NullPointerException
	{
		if (__stream == null)
			throw new NullPointerException("NARG");
		
		synchronized (AbstractPlayer.class)
		{
			// Never close the common stream, unless forced
			AudioStreamBracket common = AbstractPlayer._stream;
			if (__stream != common || __force)
				try
				{
					if (__force && __stream == common)
						AbstractPlayer._stream = null;
					AudioStreamShelf.disconnect(__stream);
				}
				catch (MLECallError __e)
				{
					if (Debugging.ENABLED)
						__e.printStackTrace();
					
					MediaException toss = new MediaException(__e.getMessage());
					toss.initCause(__e);
					throw toss;
				}
		}
	}
}
