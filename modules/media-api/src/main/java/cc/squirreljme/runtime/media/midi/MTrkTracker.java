// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.media.midi;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.ByteArrayInputStream;
import javax.microedition.media.control.MIDIControl;

/**
 * A tracker for a single MIDI track.
 *
 * @since 2024/02/25
 */
public class MTrkTracker
{
	/** The parser for MIDI tracks. */
	protected final MTrkParser parser;
	
	/** The input stream to read from. */
	protected final ByteArrayInputStream input;
	
	/** Bulk message buffer. */
	private volatile byte[] _bulk;
	
	/** Do we want an event or a delta? */
	private volatile boolean _wantEvent;
	
	/** The timing that is shared for all MIDI tracks. */
	final MidiTimeDiv _timeDiv;
	
	/** Has the track ended? */
	volatile boolean _trackEnded;

	/** Global volume multiplier for MIDI notes. */
	private volatile MidiVolume _volume;
	
	/**
	 * Initializes the tracker for the single track.
	 *
	 * @param __track The track to follow.
	 * @param __timeDiv The time division.
	 * @param __volume The master volume.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/02/25
	 */
	public MTrkTracker(MTrkParser __track, MidiTimeDiv __timeDiv,
		MidiVolume __volume)
		throws NullPointerException
	{
		if (__track == null || __timeDiv == null || __volume == null)
			throw new NullPointerException("NARG");
		
		this.parser = __track;
		
		// Store the time division
		this._timeDiv = __timeDiv;

		this._volume = __volume;
		
		// Load byte array from the input
		ByteArrayInputStream input = __track.inputStream();
		input.mark(0);
		this.input = input;
	}
	
	/**
	 * Duplicate this tracker, generally for duration calculation.
	 *
	 * @return The duplicated tracker, note the time division is copied.
	 * @since 2026/01/01
	 */
	public MTrkTracker duplicate()
	{
		return new MTrkTracker(this.parser, this._timeDiv.duplicate(),
			this._volume);
	}
	
	/**
	 * Plays the next note.
	 *
	 * @param __play The control to play into, if {@code null} then
	 * no events will be sent anywhere.
	 * @param __squelch The control to play into, if not null then
	 * controls will go into here.
	 * @return The delta for the current event.
	 * @since 2024/02/25
	 */
	public int playNext(MIDIControl __play, MIDIControl __squelch)
	{
		// Last tracked if we want an event
		boolean wantEvent = this._wantEvent;
		
		// If we are at the end of the track, there is no delta and we cannot
		// read any more events either
		if (this._trackEnded)
		{
			this._wantEvent = false;
			return 0;
		}
		
		// Read in delta time if we do not want an event
		if (!wantEvent)
		{
			// Read delta time
			int delta = this.readVariable();
			
			// We are at a delta, we need to stop for timing
			this._wantEvent = true;
			return delta;
		}
		
		// Read in event
		int event = this.read();
		
		// End of track, no more events in this track
		if (event == 0xFF)
			this.__eventMeta();
		
		// System Event
		else if (event == 0xF0 || event == 0xF7)
			this.__eventSysEx(event, __play, __squelch);
		
		// Normal MIDI Event
		else
			this.__eventMidi(event, __play, __squelch);
		
		// We do not want an event here, we need to read a delta
		this._wantEvent = false;
		
		// All events have a delta-time of zero, if the next delta ends up
		// being zero as well, then we will continue events without sleeping
		return 0;
	}
	
	/**
	 * Reads a single byte from the track.
	 *
	 * @return The read byte.
	 * @since 2024/02/26
	 */
	public int read()
	{
		ByteArrayInputStream input = this.input;
		
		// Read in single value, if EOF, just zero
		int val = input.read();
		if (val < 0)
			return -1;
		
		// Return value masked to normal byte
		return val & 0xFF;
	}
	
	/**
	 * Reads data in bulk.
	 *
	 * @param __length The number of bytes to read.
	 * @return The bulk data array, this is shared and recycled.
	 * @since 2024/02/26
	 */
	public byte[] readBulk(int __length)
	{
		// Do we need to reallocate the bulk buffer?
		byte[] bulk = this._bulk;
		if (bulk == null || __length > bulk.length)
		{
			bulk = new byte[__length];
			this._bulk = bulk;
		}
		
		// Read into the buffer
		for (int i = 0; i < __length; i++)
			bulk[i] = (byte)this.read();
		
		return bulk;
	}
	
	/**
	 * Reads variable length data.
	 *
	 * @return The read value.
	 * @since 2024/02/26
	 */
	public int readVariable()
	{
		// Read loop
		int result = 0;
		for (;;)
		{
			// Shift up old value
			result <<= 7;
			
			// Shift in new value
			int val = this.read();
			result |= (val & 0x7F);
			
			// If upper bit not set, then stop
			if ((val & 0x80) == 0)
				break;
		}
		
		return result;
	}
	
	/**
	 * Resets the buffer.
	 *
	 * @since 2024/02/26
	 */
	public void reset()
	{
		// Reset buffer to the start
		ByteArrayInputStream input = this.input;
		input.reset();
		input.mark(0);
		
		// Track is not ended
		this._trackEnded = false;
		
		// MIDI always starts at a delta
		this._wantEvent = false;
	}
	
	/**
	 * Handles a meta event, which is ignored.
	 *
	 * @return Will return {@code true} to stop playback.
	 * @since 2024/02/26
	 */
	private boolean __eventMeta()
	{
		// Read in all the data
		int type = this.read();
		int len;
		byte[] bulk;
		
		// Sequence number, the specification says the length is two but
		// it actually has no data following it
		if (type == 0x00)
		{
			this.read();
			
			// Ignore these
			len = 0;
			bulk = this._bulk;
		}
		
		// Read in data
		else
		{
			len = this.readVariable();
			bulk = this.readBulk(len);
		}
		
		// Depends on the type
		switch (type)
		{
				// Text based event
			case 0x01:	// Generic
			case 0x02:	// Copyright
			case 0x03:	// Sequence/Track Name
			case 0x04:	// Instrument name
			case 0x05:	// Lyric
			case 0x06:	// Marker
			case 0x07:	// Cue Point
				Debugging.debugNote("MIDI: %02x %s",
					type, new String(bulk, 0, len));
				break;
				
				// End of track
			case 0x2F:
				this._trackEnded = true;
				return true;
				
				// Set Tempo
			case 0x51:
				{
					long tempo = ((bulk[0] & 0xFF) << 16) |
						((bulk[1] & 0xFF) << 8) |
						(bulk[2] & 0xFF);
					
					// Never divide by zero
					if (tempo == 0)
						tempo = 1;
					
					// We need the original track nanos per tickDiv to
					// recalculate what the tempo should be
					MidiTimeDiv timeDiv = this._timeDiv;
					long tickDivOrig = timeDiv._tickDiv;
					long nanosPerTickDivOrig = timeDiv._nanosPerTickDivOrig;
					
					// Debug
					Debugging.debugNote("MIDI Tempo: " +
						"td=%d ntd=%d tempo=%d",
						tickDivOrig, nanosPerTickDivOrig,
						tempo);
					
					// Set new tempo (nanos / ticks)
					timeDiv._nanosPerTickDiv =
						(tempo * 1_000L) / tickDivOrig;
				}
				break;
			
				// Set Time Signature
			case 0x58:
				{
					// I have no idea what any of this means
					int num = bulk[0];
					int den = bulk[1];
					int clocksPerMetronome = bulk[2];
					int notated32NoteInMidiQuarter = bulk[3];
					
					// TODO: ??????
					Debugging.debugNote("MIDI Time Signature: " +
							"num=%d den=%d cpm=%d nnmd=%d",
						num, den,
						clocksPerMetronome, notated32NoteInMidiQuarter);
				}
				break;
				
				// Do not care
			default:
				break;
		}
		
		// Default continue playing
		return false;
	}
	
	/**
	 * Handles a normal MIDI event.
	 *
	 * @param __event The event.
	 * @param __play The control to send to.
	 * @param __squelch The squelch controller.
	 * @since 2024/02/26
	 */
	private void __eventMidi(int __event, MIDIControl __play,
		MIDIControl __squelch)
	{
		// Should this play when squelched?
		boolean squelchPlay = false;
		
		// Determine which data is to be read in
		int data1 = 0;
		int data2 = 0;
		switch (__event & 0b1111_0000)
		{
				// One-byte (squelchable)
			case 0b1100_0000:	// Program change
			case 0b1101_0000:	// Channel pressure
				squelchPlay = true;
				data1 = this.read();
				break;
			
				// Two-byte
			case 0b1000_0000:	// Note Off
			case 0b1001_0000:	// Note On
				data1 = this.read();

				//data2 is velocity, multiply it by the current global volume
				data2 = this.read() * this._volume._value / 100;
				break;
				
				// Two-byte (squelchable)
			case 0b1010_0000:	// After touch
			case 0b1110_0000:	// Pitch wheel
				squelchPlay = true;
				data1 = this.read();
				data2 = this.read();
				break;
				
				// Control change is special as it may be double byte or
				// single byte depending on the message (squelchable)
			case 0b1011_0000:
				squelchPlay = true;
				data1 = this.read();
				data2 = this.read();
				break;
			
				// Special messages (squelchable)
			case 0b1111_0000:
				squelchPlay = true;
				if (__event == 0b1111_0010)
				{
					data1 = this.read();
					data2 = this.read();
				}
				else if (__event == 0b1111_0011)
					data1 = this.read();
				break;
				
			default:
				// Implied channel zero event
				squelchPlay = true;
				if ((__event & 0x80) == 0)
				{
					__event = 0b1011_0000;
					data1 = this.read();
				}
				break;
		}
		
		// Send event
		if (__play != null)
			__play.shortMidiEvent(__event, data1, data2);
		else if (squelchPlay && __squelch != null)
			__squelch.shortMidiEvent(__event, data1, data2);
	}
	
	/**
	 * Handles a SysEx message.
	 *
	 * @param __event The event.
	 * @param __control The control to send to.
	 * @param __squelch The squelch controller.
	 * @since 2024/02/26
	 */
	private void __eventSysEx(int __event, MIDIControl __control,
		MIDIControl __squelch)
	{
		// Read in variable length
		int length = this.readVariable();
		
		// Read bulk message
		byte[] sysEx = this.readBulk(length);
		
		// Send long message
		if (__control != null)
			__control.longMidiEvent(sysEx, 0, length);
	}
}
