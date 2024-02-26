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
import cc.squirreljme.runtime.cldc.util.ByteIntegerArray;
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
	
	/** The current read head. */
	private volatile int _at;
	
	/** Bulk message buffer. */
	private volatile byte[] _bulk;
	
	/**
	 * Initializes the tracker for the single track.
	 *
	 * @param __track The track to follow.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/02/25
	 */
	public MTrkTracker(MTrkParser __track)
		throws NullPointerException
	{
		if (__track == null)
			throw new NullPointerException("NARG");
		
		this.parser = __track;
		
		// Load byte array from the input
		ByteArrayInputStream input = __track.inputStream();
		input.mark(0);
		this.input = input;
	}
	
	/**
	 * Plays the next note.
	 *
	 * @param __control The control to play into.
	 * @return The delta for the current event.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/02/25
	 */
	public int playNext(MIDIControl __control)
		throws NullPointerException
	{
		if (__control == null)
			throw new NullPointerException("NARG");
		
		// Read in delta time
		int delta = this.readVariable();
		
		// Read in event and handle accordingly
		int event = this.read();
		if (event == 0xFF)
			this.__eventMeta();
		else if (event == 0xF0 || event == 0xF7)
			this.__eventSysEx(event, __control);
		else
			this.__eventMidi(event, __control);
		
		// Return the read in delta
		return delta;
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
		
		// Read in single value
		int val = input.read();
		if (val < 0)
		{
			// Reset to beginning
			this.reset();
			
			// Read again
			return input.read() & 0xFF;
		}
		
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
		ByteArrayInputStream input = this.input;
		
		input.reset();
		input.mark(0);
	}
	
	/**
	 * Handles a meta event, which is ignored.
	 *
	 * @since 2024/02/26
	 */
	private void __eventMeta()
	{
		// Read in all the data
		int type = this.read();
		int len = this.readVariable();
		byte[] bulk = this.readBulk(len);
		
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
				Debugging.debugNote("MIDI: %s",
					new String(bulk, 0, len));
				break;
				
				// End of track
			case 0x2F:
				this.reset();
				break;
				
				// Do not care
			default:
				break;
		}
	}
	
	/**
	 * Handles a normal MIDI event.
	 *
	 * @param __event The event.
	 * @param __control The control to send to.
	 * @since 2024/02/26
	 */
	private void __eventMidi(int __event, MIDIControl __control)
	{
		// Determine which data is to be read in
		int data1 = 0;
		int data2 = 0;
		switch (__event & 0b1111_0000)
		{
				// One-byte
			case 0b1100_0000:	// Program change
			case 0b1101_0000:	// Channel pressure
				data1 = this.read();
				break;
			
				// Two-byte
			case 0b1000_0000:	// Note Off
			case 0b1001_0000:	// Note On
			case 0b1010_0000:	// After touch
			case 0b1011_0000:	// Control change
			case 0b1110_0000:	// Pitch wheel
				data1 = this.read();
				data2 = this.read();
				break;
			
				// Special messages
			case 0b1111_0000:
				if (__event == 0b1111_0010)
				{
					data1 = this.read();
					data2 = this.read();
				}
				else if (__event == 0b1111_0011)
					data1 = this.read();
				break;
		}
		
		// Send event
		__control.shortMidiEvent(__event, data1, data2);
	}
	
	/**
	 * Handles a SysEx message.
	 *
	 * @param __event The event.
	 * @param __control The control to send to.
	 * @since 2024/02/26
	 */
	private void __eventSysEx(int __event, MIDIControl __control)
	{
		// Read in variable length
		int length = this.readVariable();
		
		// Read bulk message
		byte[] sysEx = this.readBulk(length);
		
		
		// Send long message
		__control.longMidiEvent(sysEx, 0, length);
	}
}
