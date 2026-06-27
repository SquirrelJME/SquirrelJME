// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Keitai Wiki Community Music Implementation
//     Originally written and contributed by Guy Perfect
//     Continued maintenance and upkeep by SquirrelJME/Stephanie Gawroriski
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

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.cldc.util.ExtraMath;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Decoder for i-melody MLD sequences.
 */
@SquirrelJMEVendorApi
public class MLD
{
	static final int CUEPOINT_END = 1;
	
	/**
	 * Cuepoints
	 */
	static final int CUEPOINT_START = 0;
	
	static final int EVENT_BANK_CHANGE = 0xE1;
	
	static final int EVENT_CHANNEL_ASSIGN = 0xE5;
	
	static final int EVENT_CUEPOINT = 0xD0;
	
	static final int EVENT_END_OF_TRACK = 0xDF;
	
	static final int EVENT_JUMP = 0xD1;
	
	static final int EVENT_MASTER_TUNE = 0xB3;
	
	/**
	 * Event ext-B IDs
	 */

	/** Ext-B Command depicting PCM audio's 3D information. */
	static final int EVENT_AUDIO_CHANNEL_3D_INF = 0xF0;

	/** Ext-B Command depicting PCM audio's 3D positioning. */
	static final int EVENT_AUDIO_CHANNEL_3D_POS = 0x90;

	/** Ext-B Command depicting PCM audio panning. */
	static final int EVENT_AUDIO_CHANNEL_PANPOT = 0x81;

	/** Ext-B Command depicting PCM audio volume. */
	static final int EVENT_AUDIO_CHANNEL_VOLUME = 0x80;

	/** Ext-B Command depicting an expression change event. */
	static final int EVENT_EXPRESSION_CHANGE = 0xE6;

	/** Ext-B Command depicting the global MLD panning. */
	static final int EVENT_MASTER_BALANCE = 0xB1;

	/** Ext-B Command depicting the global MLD volume. */
	static final int EVENT_MASTER_VOLUME = 0xB0;
	
	/** Ext-B Command depicting an modulation depth event. */
	static final int EVENT_MODULATION_DEPTH = 0xEA;

	/** Ext-B Command depicting a NOP (no-op) event. */
	static final int EVENT_NOP = 0xDE;
	
	/** Ext-B Command depicting another kind of NOP event. */
	static final int EVENT_NOP_2 = 0xDC;

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
	
	/**
	 * Event types
	 */
	static final int EVENT_TYPE_UNKNOWN = -1;

	static final int EVENT_VOLUME = 0xE2;
	
	static final int EVENT_WAVE_CHANNEL_PANPOT = 0xE9;
	
	static final int EVENT_WAVE_CHANNEL_VOLUME = 0xE8;
	
	static final int EVENT_X_DRUM_ENABLE = 0xBA;
	
	/**
	 * FourCCs
	 * "adat"
	 */
	static final int FOURCC_ADAT = 0x61646174;

	/**
	 * FourCCs
	 * "adpm"
	 */
	static final int FOURCC_ADPM = 0x6164706D;
	
	/**
	 * "ainf"
	 */
	static final int FOURCC_AINF = 0x61696E66;
	
	/**
	 * "auth"
	 */
	static final int FOURCC_AUTH = 0x61757468;
	
	/**
	 * "copy"
	 */
	static final int FOURCC_COPY = 0x636F7079;
	
	/**
	 * "cuep"
	 */
	static final int FOURCC_CUEP = 0x63756570;
	
	/**
	 * "date"
	 */
	static final int FOURCC_DATE = 0x64617465;
	
	/**
	 * "exst"
	 */
	static final int FOURCC_EXST = 0x65787374;
	
	/**
	 * "melo"
	 */
	static final int FOURCC_MELO = 0x6D656C6F;
	
	/**
	 * "note"
	 */
	static final int FOURCC_NOTE = 0x6E6F7465;
	
	/**
	 * "prot"
	 */
	static final int FOURCC_PROT = 0x70726F74;
	
	/**
	 * "sorc"
	 */
	static final int FOURCC_SORC = 0x736F7263;
	
	/**
	 * "supt"
	 */
	static final int FOURCC_SUPT = 0x73757074;
	
	/**
	 * "thrd"
	 */
	static final int FOURCC_THRD = 0x74687264;
	
	/**
	 * "titl"
	 */
	static final int FOURCC_TITL = 0x7469746C;
	
	/**
	 * "trac"
	 */
	static final int FOURCC_TRAC = 0x74726163;
	
	/**
	 * "vers"
	 */
	static final int FOURCC_VERS = 0x76657273;
	
	/**
	 * "note" types
	 */
	static final int NOTE_3 = 0;
	
	static final int NOTE_4 = 1;
	
	/**
	 * Sample data
	 */
	MLDADPCM[] adpcms;
	
	/**
	 * Header subchunks
	 */
	byte[] ainf;
	
	byte[] auth;
	
	/**
	 * Content type header fields
	 */
	int contentType;
	
	String copy;
	
	int[] cuep;

	/**
	 * Flag that indicates if cuepoint play mode must be used. Enabled if the
	 * MLD has a CUEPOINT_START event.
	 */
	boolean cuepointPlayMode;

	String date;
	
	/**
	 * Total runtime in seconds, or POSITIVE_INFINITY
	 */
	double duration;
	
	byte[] exst;
	
	boolean hasFemaleVocals;
	
	boolean hasImageData;
	
	boolean hasMaleVocals;
	
	boolean hasMusicEvents;
	
	boolean hasOtherVocals;
	
	boolean hasTextData;
	
	boolean hasWaveData;
	
	/**
	 * Encoded header chunk
	 */
	byte[] header;
	
	int note;
	
	String prot;
	
	int sorc;
	
	String supt;
	
	byte[] thrd;
	
	/**
	 * Tick count at the end of the last event or cuepoint-end.
	 */
	long tickEnd;
	
	/**
	 * Tick count at the start of the first playback event. Usually 0 unless a
	 * cuepoint-start event is set at a different tick.
	 */
	long tickStart;
	
	String titl;
	
	/**
	 * Event lists
	 */
	MLDTrack[] tracks;
	
	String vers;

	/** This MLD file's master volume (0.0f, 1.0f). */
	float masterVolume = 1.0f;
	
	/** This MLD file's master volume (-1.0f, 1.0f). */
	float masterPan = 0.0f;

	/** This MLD file's master volume (0.0f, 1.0f). */
	float masterTune = 1.0f;

	
	/**
	 * Decode from a byte array. Same as invoking
	 * {@code MLD(data, 0, data.length)}.
	 *
	 * @param data A byte array contining the MLD resource.
	 * @throws NullPointerException if {@code data} is {@code null}.
	 * @throws RuntimeException if an error occurs during decoding.
	 * @see MLD(byte[],int,int)
	 */
	@SquirrelJMEVendorApi
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
	@SquirrelJMEVendorApi
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
	@SquirrelJMEVendorApi
	public MLD(InputStream in)
		throws IOException
	{
		this.parse(in instanceof DataInputStream ? (DataInputStream)in :
			new DataInputStream(in));
	}
	
	
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
	@SquirrelJMEVendorApi
	public double getDuration(boolean withoutLooping)
	{
		// TODO: JUMP events tell if partial or complete track looping is used
		// TODO: inside MLD, thus we should process them and see if any of its blocks
		// TODO: have infinite repeat values in order to return POSITIVE_INFINITY.
		Debugging.todoNote("MLD getDuration()");
		return (withoutLooping ? this.duration :
			Double.POSITIVE_INFINITY);
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
	
	
	/**
	 * Retrieve the version of the MLD resource.
	 *
	 * @return The version text if available, or {@code null} otherwise.
	 */
	public String getVersion()
	{
		return this.vers;
	}
	
	
	/**
	 * Parse an ADPCM chunk
	 */
	MLDADPCM adpcm(MLDBinaryReader reader)
	{
		if (reader.u32() != MLD.FOURCC_ADAT)
			throw new RuntimeException("Missing \"adat\" chunk.");

		// Parse "adat" chunk data
		int adatChunkSize = reader.u32();

		// NOTE: this length includes the next two fields, which are NOT in the
		// ADPM header.
		int adpmHeaderLen = reader.u16();
		Debugging.todoNote("MLD adpmHeaderLen");
		
		// TODO: No idea what these mean yet
		int dataFormat = reader.u8();
		int dataAttribute = reader.u8();
		Debugging.todoNote("MLD dF %d, dA %d",
			dataFormat, dataAttribute);

		// Parse "adpm" chunk data
		if (reader.u32() != MLD.FOURCC_ADPM)
			throw new RuntimeException("Missing \"adpm\" chunk.");

		int adpmChunkSize = reader.u16();
		Debugging.todoNote("MLD adpmChunkSize");

		// Now Read the actual ADPCM data

		MLDADPCM ret = new MLDADPCM();

		ret.sampleRate = reader.u8() * 1000;
		ret.bitDepth = reader.u8();

		int channelData = reader.u8();
		ret.numChannels = channelData & 0x07;
		ret.isInterleaved = (channelData & 0x08) != 0;

		// Here, the size of the ADPCM data is equal to the ADAT chunk size
		// (as ADPM header's size is ONLY for the three fields above), minus 13
		// bytes, which are:
		//
		// 2 bytes for adpmHeaderLen (parsed in ADAT header)
		// 1 byte for dataFormat (parsed in ADAT header)
		// 1 byte for dataAttribute (parsed in ADAT header)
		// 9 bytes for the entire "adpm" header, of which:
		// 	4 bytes are FOURCC,
		// 	2 bytes are the chunk size,
		// 	1 byte for sampleRate
		// 	1 byte for bitDepth
		// 	1 byte for channelData
		ret.data = reader.bytes(adatChunkSize - 13);

		return ret;
	}
	
	
	/**
	 * Parse an event
	 */
	MLDEvent event(int note, int track, MLDBinaryReader reader)
	{
		MLDEvent event = new MLDEvent();
		
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
			case MLD.EVENT_EXPRESSION_CHANGE:
				return this.eventExpression(event);
			case MLD.EVENT_JUMP:
				return this.eventJump(event);
			case MLD.EVENT_MASTER_BALANCE:
				return this.eventMasterBalance(event);
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
			case MLD.EVENT_CHANNEL_ASSIGN:       // Not implemented
			case MLD.EVENT_PART_CONFIGURATION:   // Chip-specific configuration
			case MLD.EVENT_MODULATION_DEPTH:     // Not implemented
			case MLD.EVENT_WAVE_CHANNEL_PANPOT:  // Not implemented
			case MLD.EVENT_WAVE_CHANNEL_VOLUME:  // Not implemented
			case MLD.EVENT_AUDIO_CHANNEL_VOLUME: // Not implemented
			case MLD.EVENT_AUDIO_CHANNEL_PANPOT: // Not implemented
			case MLD.EVENT_AUDIO_CHANNEL_3D_POS: // Not implemented
			case MLD.EVENT_AUDIO_CHANNEL_3D_INF: // Not implemented
			case MLD.EVENT_END_OF_TRACK:
			case MLD.EVENT_NOP:
			case MLD.EVENT_NOP_2:
			case MLD.EVENT_PAUSE:
			case MLD.EVENT_RESET:
			case MLD.EVENT_STOP:
				break;
			
			// Unrecognized events
			default:
		}
		return event;
	}
	
	/**
	 * Parse a bank-change event
	 */
	MLDEvent eventBankChange(MLDEvent event)
	{
		event.bank = event.param & 0x3F;
		return event;
	}
	
	/**
	 * Parse a cuepoint event
	 */
	MLDEvent eventCuepoint(MLDEvent event)
	{
		event.cuepoint = event.param;
		return event;
	}
	
	/**
	 * Parse a drum-enable event
	 */
	MLDEvent eventDrumEnable(MLDEvent event)
	{
		event.channel = event.param >> 3 & 15;
		event.enable = (event.param & 1) != 0;
		return event;
	}
	
	/**
	 * Parse an ext-info event
	 */
	MLDEvent eventExtInfo(MLDEvent event, MLDBinaryReader reader)
	{
		event.type = MLD.EVENT_TYPE_EXT_INFO;
		event.data = reader.bytes(reader.u16());
		return event;
	}
	
	/**
	 * Parse a jump event
	 */
	MLDEvent eventJump(MLDEvent event)
	{
		event.jumpCount = event.param & 15;
		event.jumpId = event.param >> 4 & 3;
		event.jumpPoint = event.param >> 6;
		return event;
	}
	
	/**
	 * Parse a Master Balance event, which is just a panpot event but applied to
	 * all tracks and subsequent events.
	 *
	 * @param __event The Master Balance event to parse.
	 * @throws NullPointerException On null arguments.
	 * @since 2026/04/18
	 */
	MLDEvent eventMasterBalance(MLDEvent __event)
		throws NullPointerException
	{
		if(__event == null)
			throw new NullPointerException("NARG");

		this.masterPan = (__event.param < 64 ? __event.param / 64.0f - 1 :
			(__event.param - 64) / 63.0f);

		__event.panpot = this.masterPan;
		return __event;
	}

	/**
	 * Parse a Master Tune event, which is just a pitch bend event but applied
	 * to all tracks and subsequent events.
	 *
	 * @param __event The Master Tune event to parse.
	 * @throws NullPointerException On null arguments.
	 * @since 2026/04/18
	 */
	MLDEvent eventMasterTune(MLDEvent __event)
		throws NullPointerException
	{
		if (__event == null)
			throw new NullPointerException("NARG");

		this.masterTune = ((__event.param & 0x7F) - 64) / 64.0f;

		__event.semitones = this.masterTune;
		return __event;
	}

	/**
	 * Parse a Master Volume event, which is just a volume event but applied to
	 * all tracks and subsequent events.
	 *
	 * @param __event The Master Volume event to parse.
	 * @throws NullPointerException On null arguments.
	 * @since 2026/04/18
	 */
	MLDEvent eventMasterVolume(MLDEvent __event)
		throws NullPointerException
	{
		if (__event == null)
			throw new NullPointerException("NARG");

		int vol = __event.param;

		// According to the CMF specification, a value of 100 is a 0dB
		// adjustment, so anything higher (up to 127) is a boost, which we allow
		// up to a 27% increase in amplitude.
		// TODO: Values lower than 100 still need to be tweaked.
		Debugging.todoNote("MLD volume adjust");
		this.masterVolume = (vol <= 100 ?
			this.volumeToAmplitude((vol) / 100.0f) :
			this.volumeToAmplitude((vol) / 100.0f));

		__event.volume = this.masterVolume;
		return __event;
	}

	/**
	 * Parse a note event
	 */
	MLDEvent eventNote(int note, int track, MLDEvent event,
		MLDBinaryReader reader)
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
	
	/**
	 * Parse a panpot event
	 */
	MLDEvent eventPanPot(MLDEvent event)
	{
		int param = event.param & 0x3F;
		event.panpot = (param < 32 ? param / 32.0f - 1 : (param - 32) / 31.0f) *
			this.masterPan;
		return event;
	}
	
	/**
	 * Parse a pitchbend event
	 */
	MLDEvent eventPitchBend(MLDEvent event)
	{
		event.semitones = ((event.param & 0x3F) - 32) / 3200.0f *
			this.masterTune;
		return event;
	}
	
	/**
	 * Parse a pitchbend-range event
	 */
	MLDEvent eventPitchBendRange(MLDEvent event)
	{
		event.range = event.param & 0x3F;
		return event;
	}
	
	/**
	 * Parse a program-change event
	 */
	MLDEvent eventProgramChange(MLDEvent event)
	{
		event.program = event.param & 0x3F;
		return event;
	}
	
	/**
	 * Parse a timebase-tempo event
	 */
	MLDEvent eventTimebaseTempo(MLDEvent event)
	{
		event.bank = event.id;
		event.tempo = event.param;
		event.timebase = (event.id & 7) == 7 ? -1 :
			((event.id & 15) > 7 ? 15 : 6) << (event.id & 7);
		event.id = MLD.EVENT_TIMEBASE_TEMPO;
		return event;
	}

	/**
	 * Parse an expression event.
	 *
	 * @param __event The expression event to parse.
	 * @throws NullPointerException On null arguments.
	 * @since 2026/04/18
	 */
	MLDEvent eventExpression(MLDEvent __event)
		throws NullPointerException
	{
		if (__event == null)
			throw new NullPointerException("NARG");

		__event.volume *= this.volumeToAmplitude(
			(__event.param & 0x3F) / 63.0f);
		return __event;
	}
	
	/**
	 * Parse a volume event
	 */
	MLDEvent eventVolume(MLDEvent event)
	{
		event.volume = this.volumeToAmplitude((event.param & 0x3F) / 63.0f) *
			this.masterVolume;
		return event;
	}
	
	
	/**
	 * Parse the file header
	 */
	void header(MLDBinaryReader reader)
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
		this.tracks = new MLDTrack[numTracks];
		
		// Header subchunks
		while (!reader.isEOF())
		{
			int id = reader.u32();
			MLDBinaryReader chunk = reader.reader(reader.u16());
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
	
	/**
	 * Parse a header "ainf" subchunk
	 */
	void headerAINF(MLDBinaryReader reader)
	{
		this.ainf = reader.bytes(reader.length);
		if (this.ainf.length > 0)
			this.adpcms = new MLDADPCM[this.ainf[0] & 0xFF];
	}
	
	/**
	 * Parse a header "auth" subchunk
	 */
	void headerAUTH(MLDBinaryReader reader)
	{
		this.auth = reader.bytes(reader.length);
	}
	
	/**
	 * Parse a header "copy" subchunk
	 */
	void headerCOPY(MLDBinaryReader reader)
	{
		this.copy = this.shiftJIS(reader.bytes(reader.length));
	}
	
	/**
	 * Parse a header "cuep" subchunk
	 */
	void headerCUEP(MLDBinaryReader reader)
	{
		for (int x = 0; x < this.cuep.length; x++)
			this.cuep[x] = reader.u32();
	}
	
	/**
	 * Parse a header "date" subchunk
	 */
	void headerDATE(MLDBinaryReader reader)
	{
		this.date = this.shiftJIS(reader.bytes(reader.length));
	}
	
	/**
	 * Parse a header "exst" subchunk
	 */
	void headerEXST(MLDBinaryReader reader)
	{
		this.exst = reader.bytes(reader.length);
	}
	
	/**
	 * Parse a header "note" subchunk
	 */
	void headerNOTE(MLDBinaryReader reader)
	{
		this.note = reader.u16();
		if (this.note >> 1 == 0)
			return;
		throw new RuntimeException(
			String.format("Invalid \"note\": 0x%04X", this.note));
	}
	
	/**
	 * Parse a header "prot" subchunk
	 */
	void headerPROT(MLDBinaryReader reader)
	{
		this.prot = this.shiftJIS(reader.bytes(reader.length));
	}
	
	/**
	 * Parse a header "sorc" subchunk
	 */
	void headerSORC(MLDBinaryReader reader)
	{
		this.sorc = reader.u8();
	}
	
	/**
	 * Parse a header "supt" subchunk
	 */
	void headerSUPT(MLDBinaryReader reader)
	{
		this.supt = this.shiftJIS(reader.bytes(reader.length));
	}
	
	/**
	 * Parse a header "thrd" subchunk
	 */
	void headerTHRD(MLDBinaryReader reader)
	{
		this.thrd = reader.bytes(reader.length);
	}
	
	/**
	 * Parse a header "titl" subchunk
	 */
	void headerTITL(MLDBinaryReader reader)
	{
		this.titl = this.shiftJIS(reader.bytes(reader.length));
	}
	
	/**
	 * Parse a header "vers" subchunk
	 */
	void headerVERS(MLDBinaryReader reader)
	{
		this.vers = this.shiftJIS(reader.bytes(reader.length));
	}
	
	/**
	 * Measure the duration and tick counters
	 */
	void inspect()
	{
		double tempo = 60.0 / (48 * 128);
		long tickNow = 0;
		int[] trkPos = new int[this.tracks.length];
		int[] trkUntil = new int[this.tracks.length];
		
		// Initialize instance fields
		this.cuepointPlayMode = false;
		this.duration = 0.0;
		this.tickEnd = 0;
		this.tickStart = 0;
		
		// Record the start time of each track's first event
		for (int x = 0; x < this.tracks.length; x++)
		{
			MLDTrack track = this.tracks[x];
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
				MLDTrack track = this.tracks[x];
				MLDEvent event = track.get(trkPos[x]++);
				
				// Additional events on this track
				if (trkPos[x] < track.size())
					trkUntil[x] = track.get(trkPos[x]).delta;
					
					// No more events ever on this track
				else
					trkUntil[x] = -1;
				
				// end-of-track
				if (event.type == MLD.EVENT_TYPE_EXT_B &&
					event.id == MLD.EVENT_END_OF_TRACK)
				{
					trkUntil[x] = -1;
					continue;
				}
				
				// Check this track again next iteration
				x--;
				
				// note
				if (event.type == MLD.EVENT_TYPE_NOTE)
				{
					this.tickEnd = Math.max(this.tickEnd,
						tickNow + event.gateTime);
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
					// Decoder must use cue-point play mode
					this.cuepointPlayMode = true;
					this.tickStart = tickNow;
					continue;
				}
				
				// If we get a cuepoint end, but the start point isn't set,
				// ignore, as it's not a valid cue-point play mode.
				if (event.cuepoint == MLD.CUEPOINT_END && !this.cuepointPlayMode)
					continue;
				
				// TODO: If a cuepoint-end and note both happen on the same tick
				// and the cuepoint end is "first", does it still play the note?
				Debugging.todoNote("MLD cuepoint?");
				
				// cuepoint end
				this.tickEnd = tickNow;
				return;
			}
		}
	}
	
	/**
	 * Parse an MLD file
	 */
	void parse(DataInputStream stream)
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
		this.adpcms = new MLDADPCM[0];
		this.note = MLD.NOTE_3;
		
		// Working variables
		MLDBinaryReader reader = new MLDBinaryReader(data, 8, length);
		
		// Parse the file
		this.header(reader);
		for (int x = 0; x < this.adpcms.length; x++)
			this.adpcms[x] = this.adpcm(reader);
		for (int x = 0; x < this.tracks.length; x++)
			this.tracks[x] = this.track(this.note, x, reader);
		
		// Measure the duration and tick counters
		this.inspect();
	}
	
	/**
	 * Decode a string as Shift_JIS
	 */
	String shiftJIS(byte[] bytes)
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
	
	/**
	 * Parse a track
	 */
	MLDTrack track(int note, int index, MLDBinaryReader reader)
	{
		
		// Error checking
		if (reader.u32() != MLD.FOURCC_TRAC)
			throw new RuntimeException("Missing \"trac\" chunk.");
		
		// Working variables
		MLDEvent event;
		MLDTrack ret = new MLDTrack();
		ret.index = index;
		reader = reader.reader(reader.u32());
		int cue = reader.offset + this.cuep[index];
		
		// Parse events
		while (!reader.isEOF())
		{
			event = this.event(note, index, reader);

			// Do we have a "cuep" header that defines the cuepoint start of
			// this track, or have we just parsed a CUEPOINT_START event? If so,
			// the player will switch to cue-point play mode.
			//
			// As per Atarius' CMF draft on IETF (Section 7.4.1):
			// https://web.archive.org/web/20240417112627/https://www.ietf.org/
			// archive/id/draft-atarius-cmf-00.txt
			//
			// "Cuepoints are used to provide an alternative play mode for CMF
			// files. When in cue-point play mode, the decoder SHOULD jump to
			// the cue start point when starting playback. All rules for setup
			// that are observed for normal playback at the beginning of the
			// file SHOULD be observed. For example, an encoder is required to
			// insert all configuration events in between cuepoint boundaries
			// even if those events are redundant with configuration events
			// outside cue-point boundaries."
			//
			// Thus, it should be safe to just skip everything before a
			// CUEPOINT_START in this case. We do this by just setting the
			// track's cue to the current position, as MLDPlayer will then begin
			// from the cue offset.
			if (reader.offset == cue || (event.id == MLD.EVENT_CUEPOINT &&
				event.cuepoint == CUEPOINT_START))
				ret.cue = ret.size();

			ret.add(event);
		}
		return ret;
	}
	
	/** Convert a volume parameter to a linear amplitude. */
	float volumeToAmplitude(float param)
	{
		return param == 0.0f ? 0.0f : (float)ExtraMath.pow(2,
			(1 - param) * -96 / 20);
	}
}
