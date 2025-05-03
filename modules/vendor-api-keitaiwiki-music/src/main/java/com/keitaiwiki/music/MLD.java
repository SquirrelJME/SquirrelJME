// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Keitai Wiki Community Music Implementation
//     Originally written and contributed by Guy Perfect
// ---------------------------------------------------------------------------
// This specific file is under the given license:
// This is free and unencumbered software released into the public domain.
// 
// Anyone is free to copy, modify, publish, use, compile, sell, or
// distribute this software, either in source code form or as a compiled
// binary, for any purpose, commercial or non-commercial, and by any
// means.
// 
// In jurisdictions that recognize copyright laws, the author or authors
// of this software dedicate any and all copyright interest in the
// software to the public domain. We make this dedication for the benefit
// of the public at large and to the detriment of our heirs and
// successors. We intend this dedication to be an overt act of
// relinquishment in perpetuity of all present and future rights to this
// software under copyright law.
// 
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
// EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
// MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
// IN NO EVENT SHALL THE AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR
// OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
// ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
// OTHER DEALINGS IN THE SOFTWARE.
// 
// For more information, please refer to <https://unlicense.org/>
// ---------------------------------------------------------------------------

package com.keitaiwiki.music;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

/**
 * Decoder for i-melody MLD sequences.
 */
public class MLD
{
	
	static final int CUEPOINT_END = 1;
	
	/// ///////////////////////////// Constants 
	/// ////////////////////////////////
	
	// Cuepoints
	static final int CUEPOINT_START = 0;
	
	static final int EVENT_BANK_CHANGE = 0xE1;
	
	static final int EVENT_CHANNEL_ASSIGN = 0xE5;
	
	static final int EVENT_CUEPOINT = 0xD0;
	
	static final int EVENT_END_OF_TRACK = 0xDF;
	
	static final int EVENT_JUMP = 0xD1;
	
	static final int EVENT_MASTER_TUNE = 0xB3;
	
	// Event ext-B IDs
	static final int EVENT_MASTER_VOLUME = 0xB0;
	
	static final int EVENT_NOP = 0xDE;
	
	static final int EVENT_PANPOT = 0xE3;
	
	static final int EVENT_PART_CONFIGURATION = 0xB9;
	
	static final int EVENT_PAUSE = 0xBD;
	
	static final int EVENT_PITCHBEND = 0xE4;
	
	static final int EVENT_PITCHBEND_RANGE = 0xE7;
	
	static final int EVENT_PROGRAM_CHANGE = 0xE0;
	
	static final int EVENT_RESET = 0xBF;
	
	static final int EVENT_STOP = 0xBE;
	
	static final int EVENT_TIMEBASE_TEMPO = 0xC0;
	
	static final int EVENT_TYPE_EXT_B = 1;
	
	static final int EVENT_TYPE_EXT_INFO = 2;
	
	static final int EVENT_TYPE_NOTE = 0;
	
	// Event types
	static final int EVENT_TYPE_UNKNOWN = -1;
	
	static final int EVENT_VOLUME = 0xE2;
	
	static final int EVENT_WAVE_CHANNEL_PANPOT = 0xE9;
	
	static final int EVENT_WAVE_CHANNEL_VOLUME = 0xE8;
	
	static final int EVENT_X_DRUM_ENABLE = 0xBA;
	
	// "note" types
	static final int NOTE_3 = 0;
	
	static final int NOTE_4 = 1;
	
	// FourCCs
	private static final int FOURCC_ADAT = 0x61646174; // "adat"
	
	private static final int FOURCC_AINF = 0x61696E66; // "ainf"
	
	private static final int FOURCC_AUTH = 0x61757468; // "auth"
	
	private static final int FOURCC_COPY = 0x636F7079; // "copy"
	
	private static final int FOURCC_CUEP = 0x63756570; // "cuep"
	
	private static final int FOURCC_DATE = 0x64617465; // "date"
	
	private static final int FOURCC_EXST = 0x65787374; // "exst"
	
	private static final int FOURCC_MELO = 0x6D656C6F; // "melo"
	
	private static final int FOURCC_NOTE = 0x6E6F7465; // "note"
	
	private static final int FOURCC_PROT = 0x70726F74; // "prot"
	
	private static final int FOURCC_SORC = 0x736F7263; // "sorc"
	
	private static final int FOURCC_SUPT = 0x73757074; // "supt"
	
	private static final int FOURCC_THRD = 0x74687264; // "thrd"
	
	private static final int FOURCC_TITL = 0x7469746C; // "titl"
	
	private static final int FOURCC_TRAC = 0x74726163; // "trac"
	
	private static final int FOURCC_VERS = 0x76657273; // "vers"
	
	// Instance fields
	ADPCM[] adpcms;   // Sample data
	
	// Header subchunks
	byte[] ainf;
	
	byte[] auth;
	
	// Content type header fields
	int contentType;
	
	String copy;
	
	int[] cuep;
	
	String date;
	
	double duration; // Total runtime in seconds, or POSITIVE_INFINITY
	
	byte[] exst;
	
	boolean hasFemaleVocals;
	
	boolean hasImageData;
	
	boolean hasMaleVocals;
	
	boolean hasMusicEvents;
	
	boolean hasOtherVocals;
	
	boolean hasTextData;
	
	boolean hasWaveData;
	
	byte[] header;   // Encoded header chunk
	
	int note;
	
	String prot;
	
	int sorc;
	
	String supt;
	
	byte[] thrd;
	
	long tickEnd;  // Tick count at the end of the last event
	
	long tickLoop; // Tick count of the loop destination
	
	String titl;
	
	Track[] tracks;   // Event lists
	
	String vers;
	
	
	/**
	 * Decode from a byte array. Same as invoking
	 * {@code MLD(data, 0, data.length)}.
	 *
	 * @param data A byte array contining the MLD resource.
	 * @throws NullPointerException if {@code data} is {@code null}.
	 * @throws RuntimeException if an error occurs during decoding.
	 * @see MLD(byte[],int,int)
	 */
	public MLD(byte[] data)
	{
		this(data, 0, data.length);
	}
	
	/**
	 * Decode from a byte array. If the {@code length} argument specifies 
	 * bytes
	 * beyond the end of the MLD resource, the extra bytes will not be
	 * processed.
	 *
	 * @param data A byte array contining the MLD resource.
	 * @param offset The position in {@code data} of the first byte of the MLD
	 * resource.
	 * @param length The number of bytes to consider when decoding the MLD
	 * resource. Must be greater than or equal to the size of the MLD.
	 * @throws NullPointerException if {@code data} is {@code null}.
	 * @throws IllegalArgumentException if {@code length} is negative.
	 * @throws ArrayIndexOutOfBoundsException if {@code offset} is negative
	 * or {@code offset + length > data.length}.
	 * @throws RuntimeException if an error occurs during decoding.
	 */
	public MLD(byte[] data, int offset, int length)
	{
		
		// Error checking
		if (data == null)
			throw new NullPointerException("A byte buffer is required.");
		if (length < 0)
			throw new IllegalArgumentException("Invalid length.");
		if (offset < 0 || length >= 0 && offset + length > data.length)
		{
			throw new ArrayIndexOutOfBoundsException(
				"Invalid range in byte buffer.");
		}
		
		// Parse the data
		try (ByteArrayInputStream stream = new ByteArrayInputStream(data,
			offset, length))
		{
			this.parse(new DataInputStream(stream));
		}
		catch (IOException e)
		{
			throw new RuntimeException(e.getMessage());
		}
	}
	
	/**
	 * Decode from an input stream. The data at the current position in the
	 * stream must be an MLD resource.<br><br>
	 * After returning, the stream will be at the position of the byte
	 * following the MLD data. If an error occurs during decoding, the stream
	 * position will be indeterminate.
	 *
	 * @param in The stream to decode from.
	 * @throws RuntimeException if an error occurs during decoding.
	 * @throws IOException if a stream access error occurs.
	 */
	public MLD(InputStream in)
		throws IOException
	{
		this.parse(in instanceof DataInputStream ? (DataInputStream)in :
			new DataInputStream(in));
	}
	
	/**
	 * Decode from an {@code Path}. The data at the start of the referenced
	 * file must be an MLD resource.
	 *
	 * @param path The path to decode from.
	 * @throws RuntimeException if an error occurs during decoding.
	 * @throws IOException if a path access error occurs.
	 */
	public MLD(Path path)
		throws IOException
	{
		this.parse(new DataInputStream(Files.newInputStream(path)));
	}
	
	
	////////////////////////////// Constructors 
	///////////////////////////////
	
	/**
	 * Retrieve the copyright of the MLD resource.
	 *
	 * @return The copyright text if available, or {@code null} otherwise.
	 */
	public String getCopyright()
	{
		return this.copy;
	}
	
	/**
	 * Retrieve the date of the MLD resource.
	 *
	 * @return The date text if available, or {@code null} otherwise.
	 */
	public String getDate()
	{
		return this.date;
	}
	
	/**
	 * Determine the total length of the MLD sequence in seconds.
	 *
	 * @param withoutLooping Whether or not to consider looping in the return
	 * value.
	 * @return If the sequence does not loop, the number of seconds in the
	 * sequence. If the sequence loops and {@code withoutLooping} is
	 * {@code false}, returns {@code Double.POSITIVE_INFINITY}. If the 
	 * sequence
	 * loops and {@code withoutLooping} is {@code true}, returns the number of
	 * seconds in the sequence up until the first loop occurs.
	 * @see MLDPlayer#getTime()
	 * @see MLDPlayer#setTime(double)
	 */
	public double getDuration(boolean withoutLooping)
	{
		return withoutLooping || this.tickLoop == -1 ? this.duration :
			Double.POSITIVE_INFINITY;
	}
	
	/**
	 * Retrieve the title of the MLD resource.
	 *
	 * @return The title text if available, or {@code null} otherwise.
	 */
	public String getTitle()
	{
		return this.titl;
	}
	
	
	///////////////////////////// Public Methods 
	//////////////////////////////
	
	/**
	 * Retrieve the version of the MLD resource.
	 *
	 * @return The version text if available, or {@code null} otherwise.
	 */
	public String getVersion()
	{
		return this.vers;
	}
	
	/// ////////////////////////// Private Methods 
	/// /////////////////////////////
	
	// Parse an ADPCM chunk
	private ADPCM adpcm(Reader reader)
	{
		if (reader.u32() != MLD.FOURCC_ADAT)
			throw new RuntimeException("Missing \"adat\" chunk.");
		ADPCM ret = new ADPCM();
		ret.data = reader.bytes(reader.u32());
		return ret;
	}
	
	/// /////////////////////// Event Parsing Methods 
	/// //////////////////////////
	
	// Parse an event
	private Event event(int note, int track, Reader reader)
	{
		Event event = new Event();
		
		// Common fields
		event.offset = reader.offset;
		event.delta = reader.u8();
		event.status = reader.u8();
		
		// Note event
		if ((event.status & 0x3F) != 63)
			return this.eventNote(note, track, event, reader);
		
		// Meta event fields
		event.id = reader.u8();
		
		// ext-info event
		if (event.id >= 0xF0)
			return this.eventExtInfo(event, reader);
		
		// Unknown event
		if (event.id < 0x80)
		{
			event.type = MLD.EVENT_TYPE_UNKNOWN;
			event.data = reader.bytes(2);
			return event;
		}
		
		// Common ext-B processing
		event.type = MLD.EVENT_TYPE_EXT_B;
		event.param = reader.u8();
		event.channelIndex = event.param >> 6;
		event.channel = track << 2 | event.channelIndex;
		
		// timebase-tempo event
		if ((event.id & 0xF0) == MLD.EVENT_TIMEBASE_TEMPO)
			return this.eventTimebaseTempo(event);
		
		// Other event
		switch (event.id)
		{
			
			// Events that need further processing
			case MLD.EVENT_BANK_CHANGE:
				return this.eventBankChange(event);
			case MLD.EVENT_CUEPOINT:
				return this.eventCuepoint(event);
			case MLD.EVENT_JUMP:
				return this.eventJump(event);
			case MLD.EVENT_MASTER_TUNE:
				return this.eventMasterTune(event);
			case MLD.EVENT_MASTER_VOLUME:
				return this.eventMasterVolume(event);
			case MLD.EVENT_PANPOT:
				return this.eventPanPot(event);
			case MLD.EVENT_PITCHBEND:
				return this.eventPitchBend(event);
			case MLD.EVENT_PITCHBEND_RANGE:
				return this.eventPitchBendRange(event);
			case MLD.EVENT_PROGRAM_CHANGE:
				return this.eventProgramChange(event);
			case MLD.EVENT_VOLUME:
				return this.eventVolume(event);
			case MLD.EVENT_X_DRUM_ENABLE:
				return this.eventDrumEnable(event);
			
			// Events that do not need further processing
			case MLD.EVENT_CHANNEL_ASSIGN:      // Not implemented
			case MLD.EVENT_PART_CONFIGURATION:  // Not implemented
			case MLD.EVENT_WAVE_CHANNEL_PANPOT: // Not implemented
			case MLD.EVENT_WAVE_CHANNEL_VOLUME: // Not implemented
			case MLD.EVENT_END_OF_TRACK:
			case MLD.EVENT_NOP:
			case MLD.EVENT_PAUSE:
			case MLD.EVENT_RESET:
			case MLD.EVENT_STOP:
				break;
			
			// Unrecognized events
			default:
		}
		return event;
	}
	
	// Parse a bank-change event
	private Event eventBankChange(Event event)
	{
		event.bank = event.param & 0x3F;
		return event;
	}
	
	// Parse a cuepoint event
	private Event eventCuepoint(Event event)
	{
		event.cuepoint = event.param;
		return event;
	}
	
	// Parse a drum-enable event
	private Event eventDrumEnable(Event event)
	{
		event.channel = event.param >> 3 & 15;
		event.enable = (event.param & 1) != 0;
		return event;
	}
	
	// Parse an ext-info event
	private Event eventExtInfo(Event event, Reader reader)
	{
		event.type = MLD.EVENT_TYPE_EXT_INFO;
		event.data = reader.bytes(reader.u16());
		return event;
	}
	
	// Parse a jump event
	private Event eventJump(Event event)
	{
		event.jumpCount = event.param & 15;
		event.jumpId = event.param >> 4 & 3;
		event.jumpPoint = event.param >> 6;
		return event;
	}
	
	// Parse a master-tune event
	private Event eventMasterTune(Event event)
	{
		event.semitones = ((event.param & 0x7F) - 64) / 64.0f;
		return event;
	}
	
	// Parse a master-volume event
	private Event eventMasterVolume(Event event)
	{
		event.volume = (event.param & 0x7F) / 127.0f;
		return event;
	}
	
	// Parse a note event
	private Event eventNote(int note, int track, Event event, Reader reader)
	{
		
		// Common processing
		event.type = MLD.EVENT_TYPE_NOTE;
		event.channelIndex = event.status >> 6;
		event.gateTime = reader.u8();
		event.keyNumber = event.status & 63;
		
		// Note events are 3 bytes
		if (note == MLD.NOTE_3)
		{
			event.octaveShift = 0;
			event.velocity = 1.0f;
		}
		
		// Note events are 4 bytes
		else
		{
			int bits = reader.u8();
			event.octaveShift = bits << 30 >> 30;
			event.velocity = (bits >> 2) / 63.0f;
		}
		
		// Compute normalized fields
		event.channel = track << 2 | event.channelIndex;
		event.key = event.octaveShift * 12 + event.keyNumber - 24;
		return event;
	}
	
	// Parse a panpot event
	private Event eventPanPot(Event event)
	{
		int param = event.param & 0x3F;
		event.panpot = param < 32 ? param / 32.0f - 1 : (param - 32) / 31.0f;
		return event;
	}
	
	// Parse a pitchbend event
	private Event eventPitchBend(Event event)
	{
		event.semitones = ((event.param & 0x3F) - 32) / 3200.0f;
		return event;
	}
	
	// Parse a pitchbend-range event
	private Event eventPitchBendRange(Event event)
	{
		event.range = event.param & 0x3F;
		return event;
	}
	
	// Parse a program-change event
	private Event eventProgramChange(Event event)
	{
		event.program = event.param & 0x3F;
		return event;
	}
	
	// Parse a timebase-tempo event
	private Event eventTimebaseTempo(Event event)
	{
		event.bank = event.id;
		event.tempo = event.param;
		event.timebase = (event.id & 7) == 7 ? -1 :
			((event.id & 15) > 7 ? 15 : 6) << (event.id & 7);
		event.id = MLD.EVENT_TIMEBASE_TEMPO;
		return event;
	}
	
	// Parse a volume event
	private Event eventVolume(Event event)
	{
		event.volume = (event.param & 0x3F) / 63.0f;
		return event;
	}
	
	/// ////////////////////// Header Parsing Methods 
	/// //////////////////////////
	
	// Parse the file header
	private void header(Reader reader)
	{
		reader = reader.reader(reader.u16());
		this.header = reader.bytes(reader.length);
		reader.offset -= reader.length;
		
		// Content type
		this.contentType = reader.u16();
		if ((this.contentType & 0xFF00) == 0x0200)
		{
			int bits = this.contentType & 0x00FF;
			this.hasMusicEvents = (bits & 0x01) != 0;
			this.hasWaveData = (bits & 0x02) != 0;
			this.hasTextData = (bits & 0x04) != 0;
			this.hasImageData = (bits & 0x08) != 0;
			this.hasFemaleVocals = (bits & 0x10) != 0;
			this.hasMaleVocals = (bits & 0x20) != 0;
			this.hasOtherVocals = (bits & 0x40) != 0;
		}
		
		// Error checking
		if (this.contentType != 0x0101)
		{
			throw new RuntimeException(
				String.format("Unsupported content type: 0x%04X",
					this.contentType));
		}
		
		// Number of tracks
		int numTracks = reader.u8();
		if (numTracks > 4)
			throw new RuntimeException("Invalid track count: " + numTracks);
		this.cuep = new int[numTracks];
		this.tracks = new Track[numTracks];
		
		// Header subchunks
		while (!reader.isEOF())
		{
			int id = reader.u32();
			Reader chunk = reader.reader(reader.u16());
			switch (id)
			{
				case MLD.FOURCC_AINF:
					this.headerAINF(chunk);
					break;
				case MLD.FOURCC_AUTH:
					this.headerAUTH(chunk);
					break;
				case MLD.FOURCC_COPY:
					this.headerCOPY(chunk);
					break;
				case MLD.FOURCC_CUEP:
					this.headerCUEP(chunk);
					break;
				case MLD.FOURCC_DATE:
					this.headerDATE(chunk);
					break;
				case MLD.FOURCC_EXST:
					this.headerEXST(chunk);
					break;
				case MLD.FOURCC_NOTE:
					this.headerNOTE(chunk);
					break;
				case MLD.FOURCC_PROT:
					this.headerPROT(chunk);
					break;
				case MLD.FOURCC_SORC:
					this.headerSORC(chunk);
					break;
				case MLD.FOURCC_SUPT:
					this.headerSUPT(chunk);
					break;
				case MLD.FOURCC_THRD:
					this.headerTHRD(chunk);
					break;
				case MLD.FOURCC_TITL:
					this.headerTITL(chunk);
					break;
				case MLD.FOURCC_VERS:
					this.headerVERS(chunk);
					break;
			}
		}
		
	}
	
	// Parse a header "ainf" subchunk
	private void headerAINF(Reader reader)
	{
		this.ainf = reader.bytes(reader.length);
		if (this.ainf.length > 0)
			this.adpcms = new ADPCM[this.ainf[0] & 0xFF];
	}
	
	// Parse a header "auth" subchunk
	private void headerAUTH(Reader reader)
	{
		this.auth = reader.bytes(reader.length);
	}
	
	// Parse a header "copy" subchunk
	private void headerCOPY(Reader reader)
	{
		this.copy = this.shiftJIS(reader.bytes(reader.length));
	}
	
	// Parse a header "cuep" subchunk
	private void headerCUEP(Reader reader)
	{
		for (int x = 0; x < this.cuep.length; x++)
			this.cuep[x] = reader.u32();
	}
	
	// Parse a header "date" subchunk
	private void headerDATE(Reader reader)
	{
		this.date = this.shiftJIS(reader.bytes(reader.length));
	}
	
	// Parse a header "exst" subchunk
	private void headerEXST(Reader reader)
	{
		this.exst = reader.bytes(reader.length);
	}
	
	// Parse a header "note" subchunk
	private void headerNOTE(Reader reader)
	{
		this.note = reader.u16();
		if (this.note >> 1 == 0)
			return;
		throw new RuntimeException(
			String.format("Invalid \"note\": 0x%04X", this.note));
	}
	
	// Parse a header "prot" subchunk
	private void headerPROT(Reader reader)
	{
		this.prot = this.shiftJIS(reader.bytes(reader.length));
	}
	
	// Parse a header "sorc" subchunk
	private void headerSORC(Reader reader)
	{
		this.sorc = reader.u8();
	}
	
	// Parse a header "supt" subchunk
	private void headerSUPT(Reader reader)
	{
		this.supt = this.shiftJIS(reader.bytes(reader.length));
	}
	
	// Parse a header "thrd" subchunk
	private void headerTHRD(Reader reader)
	{
		this.thrd = reader.bytes(reader.length);
	}
	
	// Parse a header "titl" subchunk
	private void headerTITL(Reader reader)
	{
		this.titl = this.shiftJIS(reader.bytes(reader.length));
	}
	
	// Parse a header "vers" subchunk
	private void headerVERS(Reader reader)
	{
		this.vers = this.shiftJIS(reader.bytes(reader.length));
	}
	
	// Measure the duration and tick counters
	private void inspect()
	{
		double tempo = 60.0 / (48 * 128);
		long tickNow = 0;
		int[] trkPos = new int[this.tracks.length];
		int[] trkUntil = new int[this.tracks.length];
		
		// Initialize instance fields
		this.duration = 0.0;
		this.tickEnd = 0;
		this.tickLoop = -1;
		
		// Record the start time of each track's first event
		for (int x = 0; x < this.tracks.length; x++)
		{
			Track track = this.tracks[x];
			if (track.size() != 0)
			{
				trkPos[x] = 0;
				trkUntil[x] = track.get(0).delta;
			}
			else
				trkUntil[x] = -1;
		}
		
		// Inspect all events
		for (; ; )
		{
			
			// Determine the number of ticks until the next event
			int until = -1;
			for (int x = 0; x < this.tracks.length; x++)
			{
				int tu = trkUntil[x];
				if (tu != -1 && (until == -1 || tu < until))
					until = tu;
			}
			
			// All tracks have finished
			if (until == -1)
				break;
			
			// Advance to the next event
			this.duration += until * tempo;
			tickNow += until;
			this.tickEnd = Math.max(this.tickEnd, tickNow);
			for (int x = 0; x < this.tracks.length; x++)
			{
				if (trkUntil[x] != -1)
					trkUntil[x] -= until;
			}
			
			// Process all relevant events that happen right now
			for (int x = 0; x < this.tracks.length; x++)
			{
				
				// No more events right now on this track
				if (trkUntil[x] != 0)
					continue;
				
				// Retrieve the next event
				Track track = this.tracks[x];
				Event event = track.get(trkPos[x]++);
				
				// Additional events on this track
				if (trkPos[x] < track.size())
					trkUntil[x] = track.get(trkPos[x]).delta;
					
					// No more events ever on this track
				else
					trkUntil[x] = -1;
				
				// end-of-track
				if (event.type == MLD.EVENT_TYPE_EXT_B && event.id == MLD.EVENT_END_OF_TRACK)
				{
					trkUntil[x] = -1;
					continue;
				}
				
				// Check this track again next iteration
				x--;
				
				// note
				if (event.type == MLD.EVENT_TYPE_NOTE)
				{
					this.tickEnd = Math.max(this.tickEnd, tickNow + event.gateTime);
					continue;
				}
				
				// Next must be ext-B
				if (event.type != MLD.EVENT_TYPE_EXT_B)
					continue;
				
				// timebase-tempo
				if ((event.id & 0xF0) == MLD.EVENT_TIMEBASE_TEMPO)
				{
					tempo = 60.0 / (event.timebase * event.tempo);
					continue;
				}
				
				// Next must be cuepoint
				if (event.id != MLD.EVENT_CUEPOINT)
					continue;
				
				// cuepoint start
				if (event.cuepoint == MLD.CUEPOINT_START)
				{
					this.tickLoop = tickNow;
					continue;
				}
				
				// cuepoint end, but the loop point isn't set
				if (this.tickLoop == -1)
					continue;
				
				// If a cuepoint end and note both happen on the
				// same tick and the cuepoint end is "first", does
				// it still play the note?
				
				// cuepoint end
				this.tickEnd = tickNow;
				return;
			}
			
		}
		
		// The entire sequence was scanned
		this.tickLoop = -1;
	}
	
	// Parse an MLD file
	private void parse(DataInputStream stream)
		throws IOException
	{
		
		// File signature
		if (stream.readInt() != MLD.FOURCC_MELO)
			throw new RuntimeException("Missing \"melo\" signature.");
		
		// File length
		int length = stream.readInt();
		if (length < 0)
			throw new RuntimeException("Unsupported file length.");
		
		// Read the file into a byte array
		byte[] data = new byte[8 + length];
		int offset = 8;
		while (offset < data.length)
		{
			int readed = stream.read(data, offset, data.length - offset);
			if (readed == -1)
				throw new RuntimeException("Unexpected EOF.");
			offset += readed;
		}
		
		// Default fields
		this.adpcms = new ADPCM[0];
		this.note = MLD.NOTE_3;
		
		// Working variables
		Reader reader = new Reader(data, 8, length);
		
		// Parse the file
		this.header(reader);
		for (int x = 0; x < this.adpcms.length; x++)
			this.adpcms[x] = this.adpcm(reader);
		for (int x = 0; x < this.tracks.length; x++)
			this.tracks[x] = this.track(this.note, x, reader);
		
		// Measure the duration and tick counters
		this.inspect();
	}
	
	// Decode a string as Shift_JIS
	private String shiftJIS(byte[] bytes)
	{
		try
		{
			return new String(bytes, "Shift_JIS");
		}
		catch (Exception e)
		{
			return null;
		}
	}
	
	// Parse a track
	private Track track(int note, int index, Reader reader)
	{
		
		// Error checking
		if (reader.u32() != MLD.FOURCC_TRAC)
			throw new RuntimeException("Missing \"trac\" chunk.");
		
		// Working variables
		Track ret = new Track();
		ret.index = index;
		reader = reader.reader(reader.u32());
		int cue = reader.offset + this.cuep[index];
		
		// Parse events
		while (!reader.isEOF())
		{
			if (reader.offset == cue)
				ret.cue = ret.size();
			ret.add(this.event(note, index, reader));
		}
		return ret;
	}
	
	/// ////////////////////////////// Classes 
	/// /////////////////////////////////
	
	// ADPCM sample data class
	class ADPCM
	{
		byte[] data; // Significance not yet known
	}
	
	// Sequencer event data class
	class Event
	{
		
		// ext-B fields
		int bank;
		
		// Instance fields
		int channel; // Normalized channel ID, out of 16
		
		// Note fields
		int channelIndex; // Channel index 0..3 within parent track
		
		int cuepoint;
		
		byte[] data;    // ext-info and unknown event data
		
		int delta;   // Time delta: number of ticks since last event
		
		boolean enable;
		
		int gateTime;     // Number of ticks until note off
		
		int id;      // Meta event ID
		
		int jumpCount;
		
		int jumpId;
		
		int jumpPoint;
		
		int key;     // Normalized key ID, relative to A4
		
		int keyNumber;    // Base key index
		
		int octaveShift;  // Number of octaves to adjust keyNumber
		
		int offset;  // Location in MLD asset
		
		float panpot;
		
		int param;   // Event parameter bits
		
		int program;
		
		float range;
		
		float semitones;
		
		int status;  // note-status, second byte of event data
		
		int tempo;
		
		int timebase;
		
		int type;    // Event category
		
		float velocity;     // Base volume
		
		float volume;
	}
	
	// Utility class for reading binary data
	class Reader
	{
		final byte[] data;   // Backing data store
		
		final int length; // Length of current segment
		
		int offset; // Current input offset
		
		final int start;  // Offset of start of current segment
		
		// Constructor
		Reader(byte[] data, int start, int length)
		{
			this.data = data;
			this.length = length;
			this.offset = start;
			this.start = start;
		}
		
		// Read a byte array
		byte[] bytes(int length)
		{
			if (this.offset + length > this.start + this.length)
				throw new RuntimeException("Unexpected EOF.");
			byte[] ret = new byte[length];
			System.arraycopy(this.data, this.offset, ret, 0, length);
			this.offset += length;
			return ret;
		}
		
		// Determine whether the stream has reached its end
		boolean isEOF()
		{
			return this.offset == this.start + this.length;
		}
		
		// Produce a new Reader to access a subset of this one
		Reader reader(int length)
		{
			Reader ret = new Reader(this.data, this.offset, length);
			this.skip(length);
			return ret;
		}
		
		// Advance the input
		void skip(int length)
		{
			if (this.offset + length > this.start + this.length)
				throw new RuntimeException("Unexpected EOF.");
			this.offset += length;
		}
		
		// Read a 16-bit unsigned integer
		int u16()
		{
			int ret = this.u8() << 8;
			return ret | this.u8();
		}
		
		// Read a 32-bit unsigned integer
		int u32()
		{
			int ret = this.u16() << 16;
			if (ret < 0)
				throw new RuntimeException("Unsupported U32 value.");
			return ret | this.u16();
		}
		
		// Read an 8-bit unsigned integer
		int u8()
		{
			if (this.offset == this.start + this.length)
				throw new RuntimeException("Unexpected EOF.");
			return this.data[this.offset++] & 0xFF;
		}
		
	}
	
	// Event list
	class Track
		extends ArrayList<Event>
	{
		int cue;   // Initial event offset on reset
		
		int index; // Channel index base
	}
	
}
