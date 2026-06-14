// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.media.midi;

import java.io.ByteArrayInputStream;

/**
 * This is a parser for MIDI's {@code MTrk} which is a single MIDI track, it
 * is able to read events and wait for the next one accordingly.
 *
 * @since 2022/04/24
 */
public final class MTrkParser
{
	/** The timing that is shared for all MIDI tracks. */
	final MidiTimeDiv _timeDiv;
	
	/** The MIDI buffer data. */
	private final byte[] _buffer;
	
	/** The offset into the buffer. */
	private final int _offset;
	
	/** The length of the buffer. */
	private final int _length;
	
	/** The calculated duration of this track. */
	private volatile long _duration =
		-1;

	/** Global volume multiplier for MIDI notes. */
	private volatile MidiVolume _volume;
	
	/**
	 * Initializes the parser for MIDI {@code MTrk}.
	 * 
	 * @param __b The buffer to read data from.
	 * @param __o The offset into the buffer.
	 * @param __l The length of the buffer.
	 * @param __timeDiv The time division.
	 * @param __volume The master volume.
	 * @throws IndexOutOfBoundsException If the offset and/or length are
	 * negative or exceed the array bounds.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/04/24
	 */
	public MTrkParser(byte[] __b, int __o, int __l, MidiTimeDiv __timeDiv,
		MidiVolume __volume)
		throws IndexOutOfBoundsException, NullPointerException
	{
		if (__b == null || __timeDiv == null || __volume == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0 || (__o + __l) > __b.length)
			throw new IndexOutOfBoundsException("IOOB");
		
		this._buffer = __b;
		this._offset = __o;
		this._length = __l;
		this._timeDiv = __timeDiv;
		this._volume = __volume;
	}
	
	/**
	 * Calculate the duration of the MIDI track.
	 *
	 * @return The MIDI track duration.
	 * @since 2026/01/01
	 */
	protected long duration()
	{
		// If the duration was already calculated, do not calculate it again
		long rv = this._duration;
		if (rv >= 0)
			return rv;
		
		// We need a copy of these
		MidiTimeDiv timeDiv = this._timeDiv;
		MidiVolume volume = this._volume;
		MTrkTracker tracker = new MTrkTracker(this, timeDiv, volume);
		
		// Go through every single delta, until the track ends
		rv = 0;
		while (!tracker._trackEnded)
		{
			// Read in the delta, zero would just be an event
			int delta = tracker.playNext(null, null);
			if (delta < 0)
				delta = 0;
			
			// Since the tempo can change the nanos/tickdiv, we need to pull
			// the value constantly from the timeDiv storage
			rv += Math.max(0, delta * timeDiv._nanosPerTickDiv);
		}
		
		// Return the calculated time of all the deltas
		return Math.max(0, rv);
	}
	
	/**
	 * Returns an input stream over the track data.
	 *
	 * @return The input stream used.
	 * @since 2024/02/26
	 */
	public ByteArrayInputStream inputStream()
	{
		return new ByteArrayInputStream(this._buffer, this._offset,
			this._length);
	}
	
	/**
	 * Returns the length of the track in bytes.
	 *
	 * @return The length of the track in bytes.
	 * @since 2024/02/25
	 */
	public int length()
	{
		return this._length;
	}
}
