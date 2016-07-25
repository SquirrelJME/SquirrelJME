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
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;

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
	/** Lock. */
	protected final Object lock =
		new Object();
	
	/** Playback stream. */
	protected final DataInputStream playback;
	
	/** Recording stream. */
	protected final DataOutputStream recording;
	
	/** The current time in picoseconds that has passed in the group. */
	private volatile long _picotime;
	
	/** Has playback finished? */
	private volatile boolean _playfinished;
	
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
		
		throw new Error("TODO");
	}
	
	/**
	 * Runs emulation for the given amount of picoseconds.
	 *
	 * @param __picos The number of picoseconds to run emulation for.
	 * @throws IllegalArgumentException If the amount is zero or negative.
	 * @since 2016/07/25
	 */
	public final void run(long __picos)
		throws IllegalArgumentException
	{
		// {@squirreljme.error AR01 Cannot run the emulator for zero or a
		// negative number of picoseconds.}
		if (__picos <= 0)
			throw new IllegalArgumentException("AR01");
		
		throw new Error("TODO");
	}
}

