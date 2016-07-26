// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.emulator;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * This is group of emulators which are able to interact with each other using
 * interfaces such as serial or the network. Each {@link EmulatorSystem} in the
 * group is standalone and has its own file-system and memory pool.
 *
 * Emulator groups can be recorded, replayed, save stated, and have their
 * state reloaded.
 *
 * Files on the host system are assumed to be in the current directory.
 *
 * @since 2016/07/25
 */
public final class EmulatorGroup
{
	/** The magic number for the recording files. */
	private static final int _MAGIC_NUMBER =
		0x534A4D45;
	
	/** The replay file does not have an event ready. */
	private static final long _REPLAY_NO_EVENT_TIME =
		-1L;
	
	/** Lock. */
	protected final Object lock =
		new Object();
	
	/** Playback stream. */
	protected final DataInputStream playback;
	
	/** Recording stream. */
	protected final DataOutputStream recording;
	
	/** The rerecord count. */
	protected final long rerecords;
	
	/** Visible internal lock. */
	final Object _lock =
		this.lock;
	
	/** The systems to emulate. */
	protected final List<EmulatorSystem> systems =
		new ArrayList<>();
	
	/** The current time in picoseconds that has passed in the group. */
	private volatile long _picotime;
	
	/** Has playback finished? */
	private volatile boolean _playfinished;
	
	/** The next unspecified event waiting in the replay. */
	private volatile long _nextreplayevent =
		_REPLAY_NO_EVENT_TIME;
	
	/**
	 * Initializes an emulator group which may optionally playback a given
	 * stream and/or record one.
	 *
	 * @param __is An optional stream which is used to resume or just playback
	 * a previously recorded emulation.
	 * @param __os An optional stream which is written to which records the
	 * changing state of the emulator.
	 * @param __flags The flags to use during playback, if no playback is
	 * being performed then this is ignored.
	 * @throws IOException On read/write errors.
	 * @since 2016/07/25
	 */
	public EmulatorGroup(InputStream __is, OutputStream __os,
		EmulatorPlayFlag... __flags)
		throws IOException, NullPointerException
	{
		// Must exist
		if (__flags == null)
			__flags = new EmulatorPlayFlag[0];
		
		// Setup wrapped streams?
		DataInputStream playback = (__is != null ? new DataInputStream(__is) :
			null);
		DataOutputStream recording = (__os != null ?
			new DataOutputStream(__os) : null);
		this.playback = playback;
		this.recording = recording;
		
		// Check replay header
		long rerecords;
		if (playback != null)
		{
			// {@squirreljme.error AR02 Invalid magic number in replay file.}
			if (playback.readInt() != _MAGIC_NUMBER)
				throw new IOException("AR02");
			
			// Read rerecord count
			rerecords = playback.readLong();
		}
		
		// Start rerecords at negative one
		else
			rerecords = -1;
		
		// Write replay header
		if (recording != null)
		{
			// Write magic
			recording.writeInt(_MAGIC_NUMBER);
			
			// Increase rerecords by one
			recording.writeLong(++rerecords);
		}
		
		// Store record count
		this.rerecords = rerecords;
		
		// If not playing back then just say playback finished
		this._playfinished = (playback == null);
	}
	
	/**
	 * Creates a new emulation system.
	 *
	 * @return The newly created emulation system.
	 * @throws IllegalStateException If playback has not finished.
	 * @throws IOException On read/write errors.
	 * @since 2016/07/26
	 */
	public final EmulatorSystem createSystem()
		throws IllegalStateException, IOException
	{
		// Lock
		synchronized (this.lock)
		{
			// {@squirreljme.error AR05 Cannot create a new system because
			// a replay is currently being played.}
			if (!this._playfinished)
				throw new IllegalStateException("AR05");
			
			// Internally create
			return __internalCreateSystem();
		}
	}
	
	/**
	 * Runs emulation for the given amount of picoseconds.
	 *
	 * @param __picos The number of picoseconds to run emulation for.
	 * @throws IllegalArgumentException If the amount is zero or negative.
	 * @throws IOException On read/write errors.
	 * @since 2016/07/25
	 */
	public final void run(long __picos)
		throws IOException, IllegalArgumentException
	{
		// {@squirreljme.error AR01 Cannot run the emulator for zero or a
		// negative number of picoseconds.}
		if (__picos <= 0)
			throw new IllegalArgumentException("AR01");
		
		// Lock
		synchronized (this.lock)
		{
			// The target time to reach
			long now = this._picotime;
			long target = now + __picos;
			
			// {@squirreljme.error AR03 Cannot run the specified number of
			// picoseconds because it overflows the maximum time index which is
			// about 106 days. (The current time; The requested run time; The
			// target time)}
			if (target < now)
				throw new IllegalStateException(String.format("AR03 %d %d %d",
					now, __picos, target));
			
			// Keep running until the target time is reached
			List<EmulatorSystem> systems = this.systems;
			while (now < target)
			{
				// Determine the next event that is to be emulated
				long nextevent = __nextReplayEvent();
				EmulatorSystem nextsys = null;
				
				// Go through all systems and get the lowest next even time
				int n = systems.size();
				for (int i = 0; i < n; i++)
				{
					EmulatorSystem sys = systems.get(i);
					
					// If the system's next even time is lower
					long systime = sys.__nextEventTime();
					if (systime < nextevent)
					{
						// Use this one instead
						nextevent = systime;
						nextsys = sys;
					}
				}
				
				// No events will occur at all, do not bother doing anything
				if (nextevent < 0)
					break;
				
				// Run up to the event time for the given system
				if (nextsys != null)
				{
					if (true)
						throw new Error("TODO");
				}
				
				// Otherwise read a command from the replay and execute it
				else
				{
					if (true)
						throw new Error("TODO");
				}
				
				// Change current time
				now = nextevent;
			}
			
			// Target reached
			this._picotime = target;
		}
	}
	
	/**
	 * Internally creates a new system.
	 *
	 * @return The newly created system.
	 * @throws IOException On read/write errors.
	 * @since 2016/07/26
	 */
	private final EmulatorSystem __internalCreateSystem()
		throws IOException
	{
		// Lock
		synchronized (this.lock)
		{
			// Write packet to output
			long now = this._picotime;
			DataOutputStream recording = this.recording;
			if (recording != null)
			{
				recording.writeLong(now);
				recording.writeByte(
					EmulatorPacketType.CREATE_SYSTEM.ordinal());
			}
			
			// Create
			EmulatorSystem rv = new EmulatorSystem(this);
			
			// Replace a null spot in the list
			List<EmulatorSystem> systems = this.systems;
			int n = systems.size();
			for (int i = 0; i < n; i++)
				if (systems.get(i) == null)
				{
					systems.set(i, rv);
					return rv;
				}
			
			// Add to the end
			systems.add(rv);
			return rv;
		}
	}
	
	/**
	 * This reads from the replay and determins the next time index
	 *
	 * @throws IOException On read/write errors.
	 * @return The time index where the next event in the replay happens.
	 * @since 2016/07/25
	 */
	private final long __nextReplayEvent()
		throws IOException
	{
		// Lock
		synchronized (this.lock)
		{
			// If not playing back, always returns nothing
			DataInputStream playback = this.playback;
			if (this._playfinished || playback == null)
				return _REPLAY_NO_EVENT_TIME;
			
			// Could reach the end of the replay
			try
			{
				// If the replay contains an event already read then return it
				long nextreplayevent = this._nextreplayevent;
				if (nextreplayevent >= 0)
					return nextreplayevent;
				
				// Read from the replay
				long fromreplay = playback.readLong();
				
				// {@squirreljme.error AR04 The time index stored in the replay
				// is in the past. (The time index in the replay; The current
				// time index)}
				long now = this._picotime;
				if (fromreplay < now)
					throw new IllegalStateException(String.format("AR04 %d %d",
						fromreplay, now));
				
				// Set next event time
				this._nextreplayevent = fromreplay;
				
				// Return it
				return fromreplay;
			}
			
			// Reached end of replay
			catch (EOFException e)
			{
				// Set replay as finished
				this._playfinished = true;
				
				// Invalid time
				return _REPLAY_NO_EVENT_TIME;
			}
		}
	}
}

