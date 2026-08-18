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
import cc.squirreljme.runtime.media.control.MetaDataValues;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.microedition.media.MediaException;
import javax.microedition.media.control.MetaDataControl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

/**
 * Decoder for i-melody MLD sequences.
 *
 * @since 2025/05/05
 */
@SquirrelJMEVendorApi
public class MLD
{
	/** Ext-B Command depicting PCM audio's 3D information. */
	static final int EVENT_AUDIO_CHANNEL_3D_INF = 0xF0;

	/** Ext-B Command depicting PCM audio's 3D positioning. */
	static final int EVENT_AUDIO_CHANNEL_3D_POS = 0x90;

	/** Ext-B Command depicting PCM audio panning. */
	static final int EVENT_AUDIO_CHANNEL_PANPOT = 0x81;

	/** Ext-B Command depicting PCM audio volume. */
	static final int EVENT_AUDIO_CHANNEL_VOLUME = 0x80;

	/** Ext-B Command depicting a Bank Change event. */
	static final int EVENT_BANK_CHANGE = 0xE1;

	/** Ext-B Command depicting a Channel Assign event. */
	static final int EVENT_CHANNEL_ASSIGN = 0xE5;

	/** Ext-B Command depicting a Cuepoint start or end. */
	static final int EVENT_CUEPOINT = 0xD0;

	/** EVENT_CUEPOINT parameter indicating a cuepoint end. */
	static final int EVENT_CUEPOINT_END = 1;

	/** EVENT_CUEPOINT parameter indicating a cuepoint start. */
	static final int EVENT_CUEPOINT_START = 0;

	/** Ext-B Command depicting End Of Track. */
	static final int EVENT_END_OF_TRACK = 0xDF;

	/** Ext-B Command depicting an expression change event. */
	static final int EVENT_EXPRESSION_CHANGE = 0xE6;

	/** Ext-B Command depicting a Jump event. */
	static final int EVENT_JUMP = 0xD1;

	/** Ext-B Command depicting the global MLD panning. */
	static final int EVENT_MASTER_BALANCE = 0xB1;

	/** Ext-B Command depicting a Master Tune event. */
	static final int EVENT_MASTER_TUNE = 0xB3;

	/** Ext-B Command depicting the global MLD volume. */
	static final int EVENT_MASTER_VOLUME = 0xB0;

	/** Ext-B Command depicting an modulation depth event. */
	static final int EVENT_MODULATION_DEPTH = 0xEA;

	/** Ext-B Command depicting a NOP (no-op) event. */
	static final int EVENT_NOP = 0xDE;

	/** Ext-B Command depicting another kind of NOP event. */
	static final int EVENT_NOP_2 = 0xDC;

	/** Ext-B Command depicting a Panpot event. */
	static final int EVENT_PANPOT = 0xE3;

	/** Ext-B Command depicting a chip-specific configuration event. */
	static final int EVENT_PART_CONFIGURATION = 0xB9;

	/** Ext-B Command depicting a Pause event. */
	static final int EVENT_PAUSE = 0xBD;

	/** Ext-B Command depicting a Pitch Bend event. */
	static final int EVENT_PITCHBEND = 0xE4;

	/** Ext-B Command depicting a Pitch Bend Range event. */
	static final int EVENT_PITCHBEND_RANGE = 0xE7;

	/** Ext-B Command depicting a Program Change event. */
	static final int EVENT_PROGRAM_CHANGE = 0xE0;

	/** Ext-B Command depicting a Reset event. */
	static final int EVENT_RESET = 0xBF;

	/** Ext-B Command depicting a Stop event. */
	static final int EVENT_STOP = 0xBE;

	/** Ext-B Command depicting a Timebase-Tempo event. */
	static final int EVENT_TIMEBASE_TEMPO = 0xC0;

	/** Ext-B event identifier. */
	static final int EVENT_TYPE_EXT_B = 1;

	/** Ext-Info event identifier. */
	static final int EVENT_TYPE_EXT_INFO = 2;

	/** Note event identifier. */
	static final int EVENT_TYPE_NOTE = 0;

	/** Unknown event type. */
	static final int EVENT_TYPE_UNKNOWN = -1;

	/** Ext-B Command depicting a Volume event. */
	static final int EVENT_VOLUME = 0xE2;

	/** Ext-B Command depicting a PCM Panpot event. */
	static final int EVENT_WAVE_CHANNEL_PANPOT = 0xE9;

	/** Ext-B Command depicting a PCM Volume event. */
	static final int EVENT_WAVE_CHANNEL_VOLUME = 0xE8;

	/** Ext-B Command depicting a Drum Enable event. */
	static final int EVENT_X_DRUM_ENABLE = 0xBA;

	/** FourCCs "adat" */
	static final int FOURCC_ADAT = 0x61646174;

	/** FourCCs "adpm" */
	static final int FOURCC_ADPM = 0x6164706D;

	/** FourCCs "ainf" */
	static final int FOURCC_AINF = 0x61696E66;

	/** FourCCs "auth" */
	static final int FOURCC_AUTH = 0x61757468;

	/** FourCCs "copy" */
	static final int FOURCC_COPY = 0x636F7079;

	/** FourCCs "cuep" */
	static final int FOURCC_CUEP = 0x63756570;

	/** FourCCs "date" */
	static final int FOURCC_DATE = 0x64617465;

	/** FourCCs "exst" */
	static final int FOURCC_EXST = 0x65787374;

	/** FourCCs "melo" */
	static final int FOURCC_MELO = 0x6D656C6F;

	/** FourCCs "note" */
	static final int FOURCC_NOTE = 0x6E6F7465;

	/** FourCCs "prot" */
	static final int FOURCC_PROT = 0x70726F74;

	/** FourCCs "sorc" */
	static final int FOURCC_SORC = 0x736F7263;

	/** FourCCs "supt" */
	static final int FOURCC_SUPT = 0x73757074;

	/** FourCCs "thrd" */
	static final int FOURCC_THRD = 0x74687264;

	/** FourCCs "titl" */
	static final int FOURCC_TITL = 0x7469746C;

	/** FourCCs "trac" */
	static final int FOURCC_TRAC = 0x74726163;

	/** FourCCs "vers" */
	static final int FOURCC_VERS = 0x76657273;

	/** 3-Byte note event identifier. */
	static final int NOTE_3 = 0;

	/** 4-Byte note event identifier. */
	static final int NOTE_4 = 1;

	/** Sample data. */
	__MLDADPCM__[] _adpcms;

	/** Ainf header. */
	byte[] _ainf;

	/** Auth header. */
	byte[] _auth;

	/** Content type header field. */
	int _contentType;

	/** MLD file copy subchunk. */
	String _copy;

	/** Array of cuepoints if "cuep" chunk is present. */
	int[] _cuep;

	/**
	 * Flag that indicates if cuepoint play mode must be used. Enabled if the
	 * MLD has a CUEPOINT_START event.
	 */
	boolean _cuepointPlayMode;

	/** MLD file date subchunk */
	String _date;

	/** Total runtime in seconds, or POSITIVE_INFINITY. */
	double _duration;

	/** MLD file exst subchunk. */
	byte[] _exst;

	/** Does this MLD have female vocals? */
	boolean _hasFemaleVocals;

	/** Does this MLD have image data? */
	boolean _hasImageData;

	/** Does this MLD have male vocals? */
	boolean _hasMaleVocals;

	/** Does this MLD have music events? */
	boolean _hasMusicEvents;

	/** Does this MLD have other vocals? */
	boolean _hasOtherVocals;

	/** Does this MLD have text data? */
	boolean _hasTextData;

	/** Does this MLD have wave data? */
	boolean _hasWaveData;

	/** Encoded header chunk */
	byte[] _header;

	/** This MLD file's master volume (-1.0f, 1.0f). */
	float _masterPan = 0.0f;

	/** This MLD file's master volume (0.0f, 1.0f). */
	float _masterTune = 1.0f;

	/** This MLD file's master volume (0.0f, 1.0f). */
	float _masterVolume = 1.0f;

	/** This MLD's metadata. */
	private MetaDataValues _metadata;

	/** The note byte-size indicator. May be either 3 bytes or 4 bytes long. */
	int _note;

	/** MLD file prot subchunk */
	String _prot;

	/** MLD file sorc subchunk */
	int _sorc;

	/** MLD file supt subchunk */
	String _supt;

	/** MLD file thrd subchunk */
	byte[] _thrd;

	/** Tick count at the end of the last event or cuepoint-end. */
	long _tickEnd;

	/**
	 * Tick count at the start of the first playback event. Usually 0 unless a
	 * cuepoint-start event is set at a different tick.
	 */
	long _tickStart;

	/** MLD file titl subchunk */
	String _titl;

	/** Event lists. */
	__MLDTrack__[] _tracks;

	/** MLD file vers subchunk */
	String _vers;

	/**
	 * Decode from a byte array. Same as invoking
	 * {@code MLD(__data, 0, __data.length)}.
	 *
	 * @param __data A byte array contining the MLD resource.
	 * @param __metadata A {@link MetaDataValues} instance to place MLD
	 * metadata on.
	 * @throws NullPointerException On null arguments.
	 * @throws MediaException if an error occurs during decoding.
	 * @see MLD(byte[],int,int)
	 * @since 2025/05/05
	 */
	@SquirrelJMEVendorApi
	public MLD(@NotNull byte[] __data,
		@NotNull MetaDataValues __metadata)
		throws NullPointerException, MediaException
	{
		this(__data, 0, __data.length, __metadata);
	}

	/**
	 * Decode from a byte array. If the {@code length} argument specifies
	 * bytes beyond the end of the MLD resource, the extra bytes will not be
	 * processed.
	 *
	 * @param __data A byte array contining the MLD resource.
	 * @param __offset The position in {@code data} of the first byte of the MLD
	 * resource.
	 * @param __length The number of bytes to consider when decoding the MLD
	 * resource. Must be greater than or equal to the size of the MLD.
	 * @param __metadata A {@link MetaDataValues} instance to place MLD
	 * metadata on.
	 * @throws NullPointerException On null arguments.
	 * @throws IllegalArgumentException if {@code __length} is negative.
	 * @throws ArrayIndexOutOfBoundsException if {@code __offset} is negative
	 * or {@code __offset + __length > __data.length}.
	 * @throws MediaException if an error occurs during decoding.
	 * @since 2025/05/05
	 */
	@SquirrelJMEVendorApi
	public MLD(@NotNull byte[] __data,
		@Range(from = 0, to = Integer.MAX_VALUE) int __offset,
		@Range(from = 0, to = Integer.MAX_VALUE) int __length,
		@NotNull MetaDataValues __metadata)
		throws ArrayIndexOutOfBoundsException, IllegalArgumentException,
		NullPointerException, MediaException
	{
		// Error checking
		if (__data == null)
			throw new NullPointerException("A byte buffer is required.");

		if (__length < 0)
			throw new IllegalArgumentException("Invalid length.");
		if (__offset < 0 || __length >= 0 &&
			__offset + __length > __data.length)
		{
			throw new ArrayIndexOutOfBoundsException(
				"Invalid range in byte buffer.");
		}

		this._metadata = __metadata;

		// Parse the data
		try (ByteArrayInputStream stream = new ByteArrayInputStream(__data,
			__offset, __length))
		{
			this.__parse(new DataInputStream(stream));
		}
		catch (IOException e)
		{
			throw new MediaException(e.getMessage());
		}
	}

	/**
	 * Decode from an input stream. The data at the current position in the
	 * stream must be an MLD resource.<br><br>
	 * After returning, the stream will be at the position of the byte
	 * following the MLD data. If an error occurs during decoding, the stream
	 * position will be indeterminate.
	 *
	 * @param __in The stream to decode from.
	 * @param __metadata A {@link MetaDataValues} instance to place MLD
	 * metadata on.
	 * @throws NullPointerException On null arguments.
	 * @throws MediaException if an error occurs during decoding.
	 * @throws IOException if a stream access error occurs.
	 * @since 2025/05/05
	 */
	@SquirrelJMEVendorApi
	public MLD(@NotNull InputStream __in,
		@NotNull MetaDataValues __metadata)
		throws IOException, NullPointerException, MediaException
	{
		if (__in == null)
			throw new NullPointerException("NARG");

		this._metadata = __metadata;

		this.__parse(__in instanceof DataInputStream ? (DataInputStream) __in :
			new DataInputStream(__in));
	}


	/**
	 * Retrieve the copyright of the MLD resource.
	 *
	 * @return The copyright text if available, or {@code null} otherwise.
	 * @since 2025/05/05
	 */
	@SquirrelJMEVendorApi
	public String getCopyright()
	{
		return this._copy;
	}

	/**
	 * Retrieve the date of the MLD resource.
	 *
	 * @return The date text if available, or {@code null} otherwise.
	 * @since 2025/05/05
	 */
	@SquirrelJMEVendorApi
	public String getDate()
	{
		return this._date;
	}

	/**
	 * Determine the total length of the MLD sequence in seconds.
	 *
	 * @param __withoutLooping Whether or not to consider looping in the return
	 * value.
	 * @return If the sequence does not loop, the number of seconds in the
	 * sequence. If the sequence loops and {@code __withoutLooping} is
	 * {@code false}, returns {@code Double.POSITIVE_INFINITY}. If the
	 * sequence
	 * loops and {@code __withoutLooping} is {@code true}, returns the number of
	 * seconds in the sequence up until the first loop occurs.
	 * @see MLDPlayer#getTime()
	 * @see MLDPlayer#setTime(double)
	 * @since 2025/05/05
	 */
	@SquirrelJMEVendorApi
	public double getDuration(boolean __withoutLooping)
	{
		// TODO: JUMP events tell if partial or complete track looping is used
		// TODO: inside MLD, thus we should process them and see if any of its blocks
		// TODO: have infinite repeat values in order to return POSITIVE_INFINITY.
		Debugging.todoNote("MLD getDuration()");
		return (__withoutLooping ? this._duration :
			Double.POSITIVE_INFINITY);
	}

	/**
	 * Retrieve the title of the MLD resource.
	 *
	 * @return The title text if available, or {@code null} otherwise.
	 * @since 2025/05/05
	 */
	@SquirrelJMEVendorApi
	public String getTitle()
	{
		return this._titl;
	}


	/**
	 * Retrieve the version of the MLD resource.
	 *
	 * @return The version text if available, or {@code null} otherwise.
	 */
	@SquirrelJMEVendorApi
	public String getVersion()
	{
		return this._vers;
	}

	/**
	 * Decode a string as Shift_JIS.
	 *
	 * @param __bytes The byte array to convert.
	 * @return The converted String.
	 * @since 2025/05/05
	 */
	private String __ShiftJIS(byte[] __bytes)
	{
		try
		{
			return new String(__bytes, "Shift_JIS");
		}
		catch (Exception e)
		{
			return null;
		}
	}

	/**
	 * Parse an ADPCM chunk
	 * Parses a Yamaha AICA ADPCM chunk.
	 *
	 * @param __reader The binary reader to use for parsing.
	 * @return A {@link __MLDADPCM__} object containing the ADPCM stream.
	 * @throws NullPointerException If {@code __reader} is {@code null};
	 * @throws MediaException If the ADPCM chunk is malformed.
	 * @since 2025/05/05
 	 */

	private __MLDADPCM__ __adpcm(__MLDBinaryReader__ __reader)
		throws NullPointerException, MediaException
	{
		if (__reader == null)
			throw new NullPointerException("NARG");

		if (__reader.__u32() != MLD.FOURCC_ADAT)
			throw new MediaException("Missing \"adat\" chunk.");

		// Parse "adat" chunk data
		int adatChunkSize = __reader.__u32();

		// NOTE: this length includes the next two fields, which are NOT in the
		// ADPM header.
		int adpmHeaderLen = __reader.__u16();
		Debugging.todoNote("MLD adpmHeaderLen");

		// TODO: No idea what these mean yet
		int dataFormat = __reader.__u8();
		int dataAttribute = __reader.__u8();
		Debugging.todoNote("MLD dF %d, dA %d",
			dataFormat, dataAttribute);

		// Parse "adpm" chunk data
		if (__reader.__u32() != MLD.FOURCC_ADPM)
			throw new MediaException("Missing \"adpm\" chunk.");

		int adpmChunkSize = __reader.__u16();
		Debugging.todoNote("MLD adpmChunkSize");

		// Now Read the actual ADPCM data

		__MLDADPCM__ ret = new __MLDADPCM__();

		ret._sampleRate = __reader.__u8() * 1000;
		ret._bitDepth = __reader.__u8();

		int channelData = __reader.__u8();
		ret._numChannels = channelData & 0x07;
		ret._isInterleaved = (channelData & 0x08) != 0;

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
		ret._data = __reader.__bytes(adatChunkSize - 13);

		return ret;
	}

	/**
	 * Parses a MLD event.
	 *
	 * @param __note The note event's byte size (may either be 3 bytes or 4
	 * bytes long).
	 * @param __track The track that the event pertains to.
	 * @param __reader The binary reader to use for parsing.
	 * @throws MediaException If there was an issue parsing the event data.
	 * @return The parsed event.
	 * @since 2025/05/05
	 */
	private __MLDEvent__ __event(int __note, int __track,
		@NotNull __MLDBinaryReader__ __reader)
		throws MediaException
	{
		__MLDEvent__ event = new __MLDEvent__();

		// Common fields
		event._offset = __reader._offset;
		event._delta = __reader.__u8();
		event._status = __reader.__u8();

		// Note event
		if ((event._status & 0x3F) != 63)
			return this.__eventNote(__note, __track, event, __reader);

		// Meta event fields
		event._id = __reader.__u8();

		// ext-info event
		if (event._id >= 0xF0)
			return this.__eventExtInfo(event, __reader);

		// Unknown event
		if (event._id < 0x80)
		{
			event._type = MLD.EVENT_TYPE_UNKNOWN;
			event._data = __reader.__bytes(2);
			return event;
		}

		// Common ext-B processing
		event._type = MLD.EVENT_TYPE_EXT_B;
		event._param = __reader.__u8();
		event._channelIndex = event._param >> 6;
		event._channel = __track << 2 | event._channelIndex;

		// timebase-tempo event
		if ((event._id & 0xF0) == MLD.EVENT_TIMEBASE_TEMPO)
			return this.__eventTimebaseTempo(event);

		// Other event
		switch (event._id)
		{
			// Events that need further processing
			case MLD.EVENT_BANK_CHANGE:
				return this.__eventBankChange(event);
			case MLD.EVENT_CUEPOINT:
				return this.__eventCuepoint(event);
			case MLD.EVENT_EXPRESSION_CHANGE:
				return this.__eventExpression(event);
			case MLD.EVENT_JUMP:
				return this.__eventJump(event);
			case MLD.EVENT_MASTER_BALANCE:
				return this.__eventMasterBalance(event);
			case MLD.EVENT_MASTER_TUNE:
				return this.__eventMasterTune(event);
			case MLD.EVENT_MASTER_VOLUME:
				return this.__eventMasterVolume(event);
			case MLD.EVENT_PANPOT:
				return this.__eventPanPot(event);
			case MLD.EVENT_PITCHBEND:
				return this.__eventPitchBend(event);
			case MLD.EVENT_PITCHBEND_RANGE:
				return this.__eventPitchBendRange(event);
			case MLD.EVENT_PROGRAM_CHANGE:
				return this.__eventProgramChange(event);
			case MLD.EVENT_VOLUME:
				return this.__eventVolume(event);
			case MLD.EVENT_X_DRUM_ENABLE:
				return this.__eventDrumEnable(event);

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
	 * Parse a Bank Change event.
	 *
	 * @param __event The Bank Change event to parse.
	 * @return The constructed event.
	 * @since 2025/05/05
	 */
	private __MLDEvent__ __eventBankChange(__MLDEvent__ __event)
	{
		__event._bank = __event._param & 0x3F;
		return __event;
	}

	/**
	 * Parse a Cuepoint event.
	 *
	 * @param __event The Cuepoint event to parse.
	 * @return The constructed event.
	 * @since 2025/05/05
	 */
	private __MLDEvent__ __eventCuepoint(__MLDEvent__ __event)
	{
		__event._cuepoint = __event._param;
		return __event;
	}

	/**
	 * Parse a Drum Enable event.
	 *
	 * @param __event The Drum Enable event to parse.
	 * @return The constructed event.
	 * @since 2025/05/05
	 */
	private __MLDEvent__ __eventDrumEnable(__MLDEvent__ __event)
	{
		__event._channel = __event._param >> 3 & 15;
		__event._enable = (__event._param & 1) != 0;
		return __event;
	}

	/**
	 * Parse an Expression event.
	 *
	 * @param __event The expression event to parse.
	 * @return The constructed event.
	 * @since 2026/04/18
	 */
	private __MLDEvent__ __eventExpression(__MLDEvent__ __event)
		throws NullPointerException
	{
		if (__event == null)
			throw new NullPointerException("NARG");

		__event._volume *= this.__volumeToAmplitude(
			(__event._param & 0x3F) / 63.0f);
		return __event;
	}

	/**
	 * Parses an Ext-info event.
	 *
	 * @param __event The event data instance.
	 * @param __reader The binary reader to use for parsing.
	 * @throws MediaException If there was an error parsing the event data.
	 * @return The constructed ext-info event.
	 * @since 2025/05/05
	 */
	private __MLDEvent__ __eventExtInfo(__MLDEvent__ __event, __MLDBinaryReader__ __reader)
		throws MediaException
	{
		__event._type = MLD.EVENT_TYPE_EXT_INFO;
		__event._data = __reader.__bytes(__reader.__u16());
		return __event;
	}

	/**
	 * Parse a Jump event.
	 *
	 * @param __event The Jump event to parse.
	 * @return The constructed event.
	 * @since 2025/05/05
	 */
	private __MLDEvent__ __eventJump(__MLDEvent__ __event)
	{
		__event._jumpCount = __event._param & 15;
		__event._jumpId = __event._param >> 4 & 3;
		__event._jumpPoint = __event._param >> 6;
		return __event;
	}

	/**
	 * Parse a Master Balance event, which is just a panpot event but applied to
	 * all tracks and subsequent events.
	 *
	 * @param __event The Master Balance event to parse.
	 * @return The constructed event.
	 * @since 2026/04/18
	 */
	private __MLDEvent__ __eventMasterBalance(__MLDEvent__ __event)
	{
		this._masterPan = (__event._param < 64 ? __event._param / 64.0f - 1 :
			(__event._param - 64) / 63.0f);

		__event._panpot = this._masterPan;
		return __event;
	}

	/**
	 * Parse a Master Tune event, which is just a pitch bend event but applied
	 * to all tracks and subsequent events.
	 *
	 * @param __event The Master Tune event to parse.
	 * @return The constructed event.
	 * @since 2026/04/18
	 */
	private __MLDEvent__ __eventMasterTune(__MLDEvent__ __event)
	{
		this._masterTune = ((__event._param & 0x7F) - 64) / 64.0f;

		__event._semitones = this._masterTune;
		return __event;
	}

	/**
	 * Parse a Master Volume event, which is just a volume event but applied to
	 * all tracks and subsequent events.
	 *
	 * @param __event The Master Volume event to parse.
	 * @return The constructed event.
	 * @since 2026/04/18
	 */
	private __MLDEvent__ __eventMasterVolume(__MLDEvent__ __event)
	{
		int vol = __event._param;

		// According to the CMF specification, a value of 100 is a 0dB
		// adjustment, so anything higher (up to 127) is a boost, which we allow
		// up to a 27% increase in amplitude.
		// TODO: Values lower than 100 still need to be tweaked.
		Debugging.todoNote("MLD volume adjust");
		this._masterVolume = (vol <= 100 ?
			this.__volumeToAmplitude((vol) / 100.0f) :
			this.__volumeToAmplitude((vol) / 100.0f));

		__event._volume = this._masterVolume;
		return __event;
	}

	/**
	 * Parses a Note event.
	 *
	 * @param __note The note's byte size, may be either 3 or 4 bytes long.
	 * @param __track The track this note belongs to.
	 * @param __event The event data instance.
	 * @param __reader The binary reader to use for parsing.
	 * @return The constructed note event.
	 * @since 2025/05/05
	 */
	private __MLDEvent__ __eventNote(int __note, int __track, __MLDEvent__ __event,
		__MLDBinaryReader__ __reader)
	{
		// Common processing
		__event._type = MLD.EVENT_TYPE_NOTE;
		__event._channelIndex = __event._status >> 6;
		__event._gateTime = __reader.__u8();
		__event._keyNumber = __event._status & 63;

		// Note events are 3 bytes
		if (__note == MLD.NOTE_3)
		{
			__event._octaveShift = 0;
			__event._velocity = 1.0f;
		}

		// Note events are 4 bytes
		else
		{
			int bits = __reader.__u8();
			__event._octaveShift = bits << 30 >> 30;
			__event._velocity = (bits >> 2) / 63.0f;
		}

		// Compute normalized fields
		__event._channel = __track << 2 | __event._channelIndex;
		__event._key = __event._octaveShift * 12 + __event._keyNumber - 24;
		return __event;
	}

	/**
	 * Parse a Panpot event.
	 *
	 * @param __event The Panpot event to parse.
	 * @return The constructed event.
	 * @since 2025/05/05
	 */
	private __MLDEvent__ __eventPanPot(__MLDEvent__ __event)
	{
		int param = __event._param & 0x3F;
		__event._panpot = (param < 32 ? param / 32.0f - 1 :
			(param - 32) / 31.0f) * this._masterPan;
		return __event;
	}

	/**
	 * Parse a Pitch Bend event.
	 *
	 * @param __event The Pitch Bend event to parse.
	 * @return The constructed event.
	 * @since 2025/05/05
	 */
	private __MLDEvent__ __eventPitchBend(__MLDEvent__ __event)
	{
		__event._semitones = ((__event._param & 0x3F) - 32) / 3200.0f *
			this._masterTune;
		return __event;
	}

	/**
	 * Parse a Pitch Bend Range event.
	 *
	 * @param __event The Pitch Bend Range event to parse.
	 * @return The constructed event.
	 * @since 2025/05/05
	 */
	private __MLDEvent__ __eventPitchBendRange(__MLDEvent__ __event)
	{
		__event._range = __event._param & 0x3F;
		return __event;
	}

	/**
	 * Parse a Program Change event.
	 *
	 * @param __event The Program Change event to parse.
	 * @return The constructed event.
	 * @since 2025/05/05
	 */
	private __MLDEvent__ __eventProgramChange(__MLDEvent__ __event)
	{
		__event._program = __event._param & 0x3F;
		return __event;
	}

	/**
	 * Parse a Timebase-Tempo event.
	 *
	 * @param __event The Timebase-Tempo event to parse.
	 * @return The constructed event.
	 * @since 2025/05/05
	 */
	private __MLDEvent__ __eventTimebaseTempo(__MLDEvent__ __event)
	{
		__event._bank = __event._id;
		__event._tempo = __event._param;
		__event._timebase = (__event._id & 7) == 7 ? -1 :
			((__event._id & 15) > 7 ? 15 : 6) << (__event._id & 7);
		__event._id = MLD.EVENT_TIMEBASE_TEMPO;
		return __event;
	}

	/**
	 * Parse a Volume event.
	 *
	 * @param __event The Volume event to parse.
	 * @return The constructed event.
	 * @since 2025/05/05
	 */
	private __MLDEvent__ __eventVolume(__MLDEvent__ __event)
	{
		__event._volume = this.__volumeToAmplitude(
			(__event._param & 0x3F) / 63.0f) * this._masterVolume;
		return __event;
	}

	/**
	 * Parse the MLD file's header.
	 *
	 * @param __reader The binary reader to use for parsing.
	 * @throws MediaException If the format is unsupported or the file is
	 * invalid.
	 * @since 2025/05/05
	 */
	private void __header(__MLDBinaryReader__ __reader)
		throws MediaException
	{
		__reader = __reader.__reader(__reader.__u16());
		this._header = __reader.__bytes(__reader._length);
		__reader._offset -= __reader._length;

		// Content type
		this._contentType = __reader.__u16();
		if ((this._contentType & 0xFF00) == 0x0200)
		{
			int bits = this._contentType & 0x00FF;
			this._hasMusicEvents = (bits & 0x01) != 0;
			this._hasWaveData = (bits & 0x02) != 0;
			this._hasTextData = (bits & 0x04) != 0;
			this._hasImageData = (bits & 0x08) != 0;
			this._hasFemaleVocals = (bits & 0x10) != 0;
			this._hasMaleVocals = (bits & 0x20) != 0;
			this._hasOtherVocals = (bits & 0x40) != 0;
		}

		// Error checking
		if (this._contentType != 0x0101)
		{
			throw new MediaException(
				String.format("Unsupported content type: 0x%04X",
					this._contentType));
		}

		// Number of tracks
		int numTracks = __reader.__u8();
		if (numTracks > 4)
			throw new MediaException("Invalid track count: " + numTracks);
		this._cuep = new int[numTracks];
		this._tracks = new __MLDTrack__[numTracks];

		// Header subchunks
		while (!__reader.__isEOF())
		{
			int id = __reader.__u32();
			__MLDBinaryReader__ chunk = __reader.__reader(__reader.__u16());
			switch (id)
			{
				case MLD.FOURCC_AINF:
					this._ainf = chunk.__bytes(chunk._length);
					if (this._ainf.length > 0)
						this._adpcms = new __MLDADPCM__[this._ainf[0] & 0xFF];
					break;

				case MLD.FOURCC_AUTH:
					this._auth = chunk.__bytes(chunk._length);
					this._metadata.set(MetaDataControl.AUTHOR_KEY,
						new String(this._auth));
					break;

				case MLD.FOURCC_COPY:
					this._copy = this.__ShiftJIS(chunk.__bytes(chunk._length));
					this._metadata.set(MetaDataControl.COPYRIGHT_KEY,
						this._copy);
					break;

				case MLD.FOURCC_CUEP:
					for (int x = 0; x < this._cuep.length; x++)
						this._cuep[x] = chunk.__u32();
					break;

				case MLD.FOURCC_DATE:
					this._date = this.__ShiftJIS(chunk.__bytes(chunk._length));
					this._metadata.set(MetaDataControl.DATE_KEY, this._date);
					break;

				case MLD.FOURCC_EXST:
					this._exst = chunk.__bytes(chunk._length);
					break;

				case MLD.FOURCC_NOTE:
					this._note = chunk.__u16();
					if (this._note >> 1 == 0)
						break;
					throw new MediaException(
						String.format("Invalid \"note\": 0x%04X", this._note));

				case MLD.FOURCC_PROT:
					this._prot = this.__ShiftJIS(chunk.__bytes(chunk._length));
					break;

				case MLD.FOURCC_SORC:
					this._sorc = chunk.__u8();
					break;

				case MLD.FOURCC_SUPT:
					this._supt = this.__ShiftJIS(chunk.__bytes(chunk._length));
					break;

				case MLD.FOURCC_THRD:
					this._thrd = chunk.__bytes(chunk._length);
					break;

				case MLD.FOURCC_TITL:
					this._titl = this.__ShiftJIS(chunk.__bytes(chunk._length));
					this._metadata.set(MetaDataControl.TITLE_KEY, this._titl);
					break;

				case MLD.FOURCC_VERS:
					this._vers = this.__ShiftJIS(chunk.__bytes(chunk._length));
					break;
			}
		}

	}

	/**
	 * Measure the MLD file's duration and tick counters.
	 *
	 * @since 2025/05/05
	 */
	private void __inspect()
	{
		double tempo = 60.0 / (48 * 128);
		long tickNow = 0;
		int[] trkPos = new int[this._tracks.length];
		int[] trkUntil = new int[this._tracks.length];

		// Initialize instance fields
		this._cuepointPlayMode = false;
		this._duration = 0.0;
		this._tickEnd = 0;
		this._tickStart = 0;

		// Record the start time of each track's first event
		for (int x = 0; x < this._tracks.length; x++)
		{
			__MLDTrack__ track = this._tracks[x];
			if (track.size() != 0)
			{
				trkPos[x] = 0;
				trkUntil[x] = track.get(0)._delta;
			}
			else
				trkUntil[x] = -1;
		}

		// Inspect all events
		for (;;)
		{

			// Determine the number of ticks until the next event
			int until = -1;
			for (int x = 0; x < this._tracks.length; x++)
			{
				int tu = trkUntil[x];
				if (tu != -1 && (until == -1 || tu < until))
					until = tu;
			}

			// All tracks have finished
			if (until == -1)
				break;

			// Advance to the next event
			this._duration += until * tempo;
			tickNow += until;
			this._tickEnd = Math.max(this._tickEnd, tickNow);
			for (int x = 0; x < this._tracks.length; x++)
			{
				if (trkUntil[x] != -1)
					trkUntil[x] -= until;
			}

			// Process all relevant events that happen right now
			for (int x = 0; x < this._tracks.length; x++)
			{

				// No more events right now on this track
				if (trkUntil[x] != 0)
					continue;

				// Retrieve the next event
				__MLDTrack__ track = this._tracks[x];
				__MLDEvent__ event = track.get(trkPos[x]++);

				// Additional events on this track
				if (trkPos[x] < track.size())
					trkUntil[x] = track.get(trkPos[x])._delta;

					// No more events ever on this track
				else
					trkUntil[x] = -1;

				// end-of-track
				if (event._type == MLD.EVENT_TYPE_EXT_B &&
					event._id == MLD.EVENT_END_OF_TRACK)
				{
					trkUntil[x] = -1;
					continue;
				}

				// Check this track again next iteration
				x--;

				// note
				if (event._type == MLD.EVENT_TYPE_NOTE)
				{
					this._tickEnd = Math.max(this._tickEnd,
						tickNow + event._gateTime);
					continue;
				}

				// Next must be ext-B
				if (event._type != MLD.EVENT_TYPE_EXT_B)
					continue;

				// timebase-tempo
				if ((event._id & 0xF0) == MLD.EVENT_TIMEBASE_TEMPO)
				{
					tempo = 60.0 / (event._timebase * event._tempo);
					continue;
				}

				// Next must be cuepoint
				if (event._id != MLD.EVENT_CUEPOINT)
					continue;

				// cuepoint start
				if (event._cuepoint == MLD.EVENT_CUEPOINT_START)
				{
					// Decoder must use cue-point play mode
					this._cuepointPlayMode = true;
					this._tickStart = tickNow;
					continue;
				}

				// If we get a cuepoint end, but the start point isn't set,
				// ignore, as it's not a valid cue-point play mode.
				if (event._cuepoint == MLD.EVENT_CUEPOINT_END && !this._cuepointPlayMode)
					continue;

				// TODO: If a cuepoint-end and note both happen on the same tick
				// and the cuepoint end is "first", does it still play the note?
				Debugging.todoNote("MLD cuepoint?");

				// cuepoint end
				this._tickEnd = tickNow;
				return;
			}
		}
	}

	/**
	 * Parse an MLD file
	 */
	private void __parse(DataInputStream __stream)
		throws IOException, MediaException
	{

		// File signature
		if (__stream.readInt() != MLD.FOURCC_MELO)
			throw new MediaException("Missing \"melo\" signature.");

		// File length
		int length = __stream.readInt();
		if (length < 0)
			throw new MediaException("Unsupported file length.");

		// Read the file into a byte array
		byte[] data = new byte[8 + length];
		int offset = 8;
		while (offset < data.length)
		{
			int readed = __stream.read(data, offset, data.length - offset);
			if (readed == -1)
				throw new MediaException("Unexpected EOF.");
			offset += readed;
		}

		// Default fields
		this._adpcms = new __MLDADPCM__[0];
		this._note = MLD.NOTE_3;

		// Working variables
		__MLDBinaryReader__ reader = new __MLDBinaryReader__(data, 8, length);

		// Parse the file
		this.__header(reader);
		for (int x = 0; x < this._adpcms.length; x++)
			this._adpcms[x] = this.__adpcm(reader);
		for (int x = 0; x < this._tracks.length; x++)
			this._tracks[x] = this.__track(this._note, x, reader);

		// Measure the duration and tick counters
		this.__inspect();
	}

	/**
	 * Parse a MLD track.
	 *
	 * @param __note The note byte size (either 3 bytes or 4 bytes long).
	 * @param __index The track index for generated events.
	 * @param __reader The binary reader to use for parsing.
	 * @throws MediaException If track data is invalid or corrupted.
	 * @return The constructed track.
	 * @since 2025/05/05
	 */
	private __MLDTrack__ __track(int __note, int __index,
		@NotNull __MLDBinaryReader__ __reader)
		throws MediaException
	{
		// Error checking
		if (__reader.__u32() != MLD.FOURCC_TRAC)
			throw new MediaException("Missing \"trac\" chunk.");

		// Working variables
		__MLDEvent__ event;
		__MLDTrack__ ret = new __MLDTrack__();
		ret._index = __index;
		__reader = __reader.__reader(__reader.__u32());
		int cue = __reader._offset + this._cuep[__index];

		// Parse events
		while (!__reader.__isEOF())
		{
			event = this.__event(__note, __index, __reader);

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
			if (__reader._offset == cue || (event._id == MLD.EVENT_CUEPOINT &&
				event._cuepoint == MLD.EVENT_CUEPOINT_START))
				ret._cue = ret.size();

			ret.add(event);
		}
		return ret;
	}

	/**
	 * Convert a volume parameter to a linear amplitude.
	 *
	 * @param __param The volume parameter to convert.
	 * @return The resulting linear amplitude.
	 * @since 2025/05/05
	 */
	private float __volumeToAmplitude(float __param)
	{
		return __param == 0.0f ? 0.0f : (float)ExtraMath.pow(2,
			(1 - __param) * -96 / 20);
	}
}
