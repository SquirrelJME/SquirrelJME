// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.media.nokia;

import cc.squirreljme.jvm.mle.ObjectShelf;
import cc.squirreljme.jvm.mle.constants.AudioStreamChannels;
import cc.squirreljme.jvm.mle.constants.AudioStreamFormat;
import cc.squirreljme.jvm.mle.constants.AudioStreamRate;
import cc.squirreljme.runtime.cldc.annotation.KeepWhenCompacting;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.cldc.util.ExtraMath;
import org.intellij.lang.annotations.MagicConstant;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

/**
 * Decodes Nokia OTA files into audio samples to be sent over scritchaudio.
 *
 * @since 2026/05/26
 */
@SquirrelJMEVendorApi
public class NokiaOTADecoder 
{
	/** Natural style, small rest between between notes (Default style). */
	private static final int NATURAL_STYLE =
		0;

	/** Continuous style, notes flow into each other with no rest. */
	private static final int CONTINUOUS_STYLE =
		1;

	/** 
	 * Staccato style, longer rest between notes compared to
	 * {@link NokiaOTADecoder#NATURAL_STYLE}.
	 */
	private static final int STACCATO_STYLE =
		2;

	/** Debugging array for printing readable note values */
	private static final String[] NOTE_STRINGS = (Debugging.ENABLED ?
		new String[] {"Pause", "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#",
		"A", "A#", "H", "RESERVED", "RESERVED", "RESERVED"} : null);

	/** Reference for the received data array */
	private byte[] _data;

	/** Marker for the decoder's current position in {@code _data} */
	private int _curPos;

	/** Marker for the decoder's current bit position in {@code _curPos}. */
	private byte _curBitPos;

	/** The note scale multiplier, defaults to 880Hz (1x) */
	private byte _noteScale;

	/** The note Style, default is {@link NokiaOTADecoder#NATURAL_STYLE} */
	private byte _noteStyle;

	/** The note volume, defaults to 160 (slightly above average volume). */
	private byte _noteVolume;

	/** 
	 * How long a quarterNote takes to play with the current BPM in use.
	 * Defaults to 952ms, which is the equivalent to the default BPM of 63.
	 */
	private int _quarterNoteMs;

	/** Position of the last parsed pattern (0x0 patterns will reuse it) */
	private int _lastPatternPos;

	/** Bit position of the last parsed pattern (0x0 patterns will reuse it) */
	private byte _lastPatternBitPos;

	/** Length of the last parsed pattern (0x0 patterns will reuse it) */
	private int _lastPatternLen;

	/** Where the current pattern starts, used for looping the pattern. */
	private int _loopPatternPos;

	/** Bit where the current pattern starts, used for looping the pattern. */
	private byte _loopCurBitPos;

	/** Parsing position to restore after using {@code _lastPatternPos}. */
	private int _restorePatternPos;

	/** Bit position to restore after using {@code _lastPatternBitPos}. */
	private byte _restorePatternBitPos;

	/** Indicates whether parsing is underway or not. */
	private boolean _parsing;

	/** How many pattern instruction are left to complete a pattern loop. */
	private int _instructionsLeft;

	/** How many commands are left to complete the OTA's parsing. */
	private int _commandLength;

	/** How many times we should loop the current pattern. */
	private byte _loopValue;

	/** How many patterns the current Sound block has. */
	private int _songSequenceLength;

	/** The note's duration, in samples. */
	private int _noteDurationSmp;

	/** Duration of the rest between notes, in samples. */
	private int _restDurationSmp;

	/** The sound output's sampling rate. */
	private int _sampleRate;

	/** The sound output's amount of audio channels. */
	private int _channels;

	/** The generated square wave's period. */
	private int _sqWavePeriod;

	/** Master Volume multiplier for parsed notes (0-100% range) */
	private byte _masterVolumeMult = 100;

	/**
	 * Creates a new NokiaOTADecoder instance
	 * 
	 * @since 2025/12/24
	 */
	public NokiaOTADecoder()
	{
		this.reset();
	}

	/**
	 * Checks whether OTA parsing has finished or not
	 * 
	 * @return {@code true} if parsing has finished.
	 * @since 2025/12/24
	 */
	public boolean hasFinished()
	{
		return this._commandLength == 0 && this._instructionsLeft == 0 &&
			this._loopValue == -1 && this._songSequenceLength == 0 &&
			this._noteDurationSmp == 0 && this._restDurationSmp == 0;
	}

	/**
	 * Begins parsing the received OTA data, filling the output buffer with
	 * audio data.
	 * 
	 * @param __fmt The output audio format (see {@link AudioStreamFormat}).
	 * @param __rate The format's sampling rate (see {@link AudioStreamRate}).
	 * @param __ch The format's channels (see {@link AudioStreamChannels}).
	 * @param __buf The output audio buffer.
	 * @param __off The offset from which to start filling the output buffer.
	 * @param __len The amount of samples that can be placed into the buffer.
	 * @param __data The input OTA data array.
	 * @throws NullPointerException If {@code __data} is null.
	 * @throws IllegalArgumentException If any of the lower level parse methods
	 * receive invalid data from the OTA array, {@code __off} is negative,
	 * {@code __len} is negative, or {@code (__off + __len > __buf.length)}.
	 * @since 2025/12/24
	 */
	public void parseOTA(
		@MagicConstant(valuesFromClass = AudioStreamFormat.class) int __fmt,
		@MagicConstant(valuesFromClass = AudioStreamRate.class) int __rate,
		@MagicConstant(valuesFromClass = AudioStreamChannels.class) int __ch,
		@Nullable Object __buf,
		@Range(from = 0, to = Integer.MAX_VALUE) int __off,
		@Range(from = 0, to = Integer.MAX_VALUE) int __len,
		@NotNull byte[] __data)
		throws NullPointerException, IllegalArgumentException
	{
		if (__data == null)
			throw new NullPointerException("NARG");

		// If output buffer is null, we do nothing until it is a valid array.
		if (__buf == null)
			return;

		if (__off < 0 || __len < 0 || __off + __len < 0)
			throw new IllegalArgumentException("NEGV");

		if ((__off + __len > ObjectShelf.arrayLength(__buf)))
			throw new IllegalArgumentException("IOOB");

		// If we're not parsing yet (i.e. we didn't begin parsing and had to
		// stop in order to wait for a new buffer to arrive after filling the
		// previous one with audio data), parse from the start of the OTA data.
		if (!this._parsing)
		{
			// Validate command length (8 bits)
			this._sampleRate = __rate;
			this._channels = __ch;
			this._data = __data;
			this._commandLength = this.__readBits(8);
			this.__parseMainCommand(__fmt, __buf, __off, __len);
			this._parsing = true;
		}

		// If we're already parsing, we have a chain of stages to check for,
		// ordered by increasing scope. Doing so allows us to know where we 
		// last stopped on the parsing process. We can then skip the higher
		// levels and handling backtracking altogether, since we can assume
		// that decoder was left in the proper state to call these directly.

		// If any of the note or rest durations are more than 0, that means we
		// couldn't fit an entire note duration into the buffer, and must thus
		// populate it with its duration remainder.
		if (this._noteDurationSmp > 0 || this._restDurationSmp > 0)
		{
			if (this.__generateSamples(__fmt, __buf, __off, __len))
				return;
		}

		// If we have instructions left to parse, that means we could fit some
		// of the notes into the audio buffer, but not all, so resume parsing
		// them again until either all notes are parsed, or the buffer is
		// filled again.
		if (this._instructionsLeft > 0)
		{
			for (int i = 0; i < this._instructionsLeft; i++)
				if (this.__parsePatternInstruction(__fmt, __buf, __off, __len))
					return;
		}

		// loop Value is >= 0 means we stopped after one pass of the pattern
		// and need to do more loops.
		if (this._loopValue >= 0)
		{
			this.__parseSongPattern(__fmt, __buf, __off, __len);
			return;
		}

		// If song sequence length is > zero, we still have other patterns to
		// process, so do that.
		if (this._songSequenceLength > 0)
		{
			this.__parseSongPatternHeader(__fmt, __buf, __off, __len);
			return;
		}

		// If command length is > zero, we still have other commands to parse,
		// which means we'handling a more complex OTA ringtone.
		if (this._commandLength > 0)
		{
			this.__parseMainCommand(__fmt, __buf, __off, __len);
			return;
		}
	}

	/**
	 * Resets the OTA parser, setting all flags so that the data can be parsed
	 * from the beginning.
	 * 
	 * @since 2025/12/24
	 */
	public void reset()
	{
		// Reset all variables to parse back from the beginning
		this._instructionsLeft = 0;
		this._commandLength = 0;
		this._loopValue = -1;
		this._songSequenceLength = 0;
		this._noteDurationSmp = 0;
		this._restDurationSmp = 0;
		this._noteVolume = (byte) 160;
		this._quarterNoteMs = 952;
		this._curPos = 0;
		this._curBitPos = 0;
		this._noteScale = 10;
		this._noteStyle = NokiaOTADecoder.NATURAL_STYLE;
		this._data = null;
		this._parsing = false;
	}

	/**
	 * Sets the master volume multiplier for generated notes, where the MIDP's
	 * range of {@code [0, 100]} results in a multiplier range of
	 * {@code [0, 1]}.
	 * 
	 * @param __volume The volume to set
	 * @since 2025/12/24
	 */
	public void setMasterVolume(int __volume)
	{
		this._masterVolumeMult = (byte) __volume;
	}

	/**
	 * Parses the main OTA command, which can be either a Unicode (cancel
	 * command), a Ringing Tone or a Sound command.
	 * 
	 * @param __fmt The output audio format (see {@link AudioStreamFormat}).
	 * @param __buf The output audio buffer.
	 * @param __off The offset from which to start filling the output buffer.
	 * @param __len The amount of samples that can be placed into the buffer.
	 * @throws IllegalArgumentException If the cancel command specifier is
	 * invalid, or the OTA contains an invalid command type in its data (might
	 * be corrupted).
	 * @since 2025/12/24
	 */
	@KeepWhenCompacting
	private void __parseMainCommand(
		@MagicConstant(valuesFromClass = AudioStreamFormat.class) int __fmt,
		@Nullable Object __buf,
		@Range(from = 0, to = Integer.MAX_VALUE) int __off,
		@Range(from = 0, to = Integer.MAX_VALUE) int __len)
		throws IllegalArgumentException
	{
		// Command type (first 7 bits + filler bit which is always 0)
		int commandType = this.__readBits(8);

		switch (commandType)
		{
				// Ringing tone programming
			case 0x4A:
				this.__parseRingingTone(__fmt, __buf, __off, __len);
				break;

				// Unicode
			case 0x44:
				this.__parseUnicode();
				break;

				// Sound
			case 0x3A: 
				this.__parseSound(__fmt, __buf, __off, __len);
				break;

				// Cancel command, the specification says nothing about
				// what it really does... Does any actual OTA ringtone use it?
			case 0xA: 
				if (this.__readBits(7) == 0x05)
					this.__parseUnicode();
				else 
					throw new IllegalArgumentException("Invalid cancel" +
						" command specifier");
				break;

				// Indicates the end of every parsing procedure.
			case 0x0:
				if (Debugging.VERBOSE)
					Debugging.debugNote("OTA: Parsing finished!");
				break;

				// Invalid command type, throw exception
			default:
				throw new IllegalArgumentException("Invalid command" +
					" specifier:" + commandType);
		}

		this._commandLength--;
	}

	/**
	 * Parses a Ringing Tone command, which contains either a Sound command
	 * or a Unicode character (which is always placed before a Sound command).
	 * 
	 * @param __fmt The output audio format (see {@link AudioStreamFormat}).
	 * @param __buf The output audio buffer.
	 * @param __off The offset from which to start filling the output buffer.
	 * @param __len The amount of samples that can be placed into the buffer.
	 * @throws IllegalArgumentException If the parsed command is not valid.
	 * @since 2025/12/24
	 */
	@KeepWhenCompacting
	private void __parseRingingTone(
		@MagicConstant(valuesFromClass = AudioStreamFormat.class) int __fmt,
		@Nullable Object __buf,
		@Range(from = 0, to = Integer.MAX_VALUE) int __off,
		@Range(from = 0, to = Integer.MAX_VALUE) int __len)
		throws IllegalArgumentException
	{
		/*
			* If we found a <ringing-tone-programming> bit string, that means that
			* up next it's either a <unicode> or a <sound> bit string
			*/
		int nextCheck = this.__readBits(7);
		
		if (nextCheck == 0x1D) 
			this.__parseSound(__fmt, __buf, __off, __len);

		// 0x22 means we must read a unicode, which is placed before any Sound
		else if (nextCheck == 0x22) 
			this.__parseUnicode();
		else
			throw new IllegalArgumentException("Invalid set of bits for" +
				" ringing-tone-programming");

		// Ringing tone has internal commands, so we must also decrement the
		// counter when these get processed.
		this._commandLength--;
	}

	/**
	 * Parses a Unicode character.
	 * 
	 * @since 2025/12/24
	 */
	@KeepWhenCompacting
	private void __parseUnicode()
	{
		// A unicode is defined in the spec as a 16-bit UCS-2 encoded char
		short unicode = (short) this.__readBits(16);
		if (Debugging.VERBOSE)
			Debugging.debugNote("Unicode:%s", unicode);
	}

	/**
	 * Parses a Sound command, which contains patterns with audio data.
	 * 
	 * @param __fmt The output audio format (see {@link AudioStreamFormat}).
	 * @param __buf The output audio buffer.
	 * @param __off The offset from which to start filling the output buffer.
	 * @param __len The amount of samples that can be placed into the buffer.
	 * @throws IllegalArgumentException If the parsed pattern type is not
	 * valid, or is not yet supported.
	 * @since 2025/12/24
	 */
	@KeepWhenCompacting
	private void __parseSound(
		@MagicConstant(valuesFromClass = AudioStreamFormat.class) int __fmt,
		@Nullable Object __buf,
		@Range(from = 0, to = Integer.MAX_VALUE) int __off,
		@Range(from = 0, to = Integer.MAX_VALUE) int __len)
		throws IllegalArgumentException
	{
		// Read song type, 3 bits are used
		int songType = this.__readBits(3);

		switch (songType) 
		{
				// Basic song type
			case 0x1:
				this.__parseBasicSong(__fmt, __buf, __off, __len);
				return;

				// Temporary song type
			case 0x2:
				// Read song sequence length (8 bits) before parsing pattern
				this._songSequenceLength = this.__readBits(8);
				this.__parseSongPatternHeader(__fmt, __buf, __off, __len);
				return;

				// Below are all 'reserved for future extension' in the latest
				// spec revision i have (v3.0.0), likely never actually used.
				
				// MIDI song type
			case 0x3: 
				throw new IllegalArgumentException("MIDI song unsupported");
			
				// Digitized song type
			case 0x4:
				throw new IllegalArgumentException("Digital song unsupported");

				// Polyphonic song type
			case 0x5: 
				throw new IllegalArgumentException("Poly song unsupported");

				// Invalid song type
			default:
				throw new IllegalArgumentException("Invalid song type");
		}
	}

	/**
	 * Parses a Basic Song, which contains a title header followed by song
	 * patterns.
	 * 
	 * @param __fmt The output audio format (see {@link AudioStreamFormat}).
	 * @param __buf The output audio buffer.
	 * @param __off The offset from which to start filling the output buffer.
	 * @param __len The amount of samples that can be placed into the buffer.
	 * @since 2025/12/24
	 */
	@KeepWhenCompacting
	private void __parseBasicSong(
		@MagicConstant(valuesFromClass = AudioStreamFormat.class) int __fmt,
		@Nullable Object __buf,
		@Range(from = 0, to = Integer.MAX_VALUE) int __off,
		@Range(from = 0, to = Integer.MAX_VALUE) int __len)
	{
		// Read title length (upper 4 bits)
		int titleLength = this.__readBits(4);

		StringBuilder title = new StringBuilder();
		for (int i = 0; i < titleLength; i++) 
		{
			char character = (char) this.__readBits(8);
			title.append(character);
		}
		
		if (Debugging.VERBOSE)
			Debugging.debugNote("Title Length:%d | Basic Song Title: %s",
					titleLength, title);
		
		// Read song sequence length (8 bits)
		this._songSequenceLength = this.__readBits(8);
		this.__parseSongPatternHeader(__fmt, __buf, __off, __len);
	}

	/**
	 * Parses a pattern header, useful for setting the point from which pattern
	 * loops will be done, and getting the amount of loops that have to be
	 * done.
	 * 
	 * Note that if the parsed loop value is 0xF, we must loop the pattern
	 * indefinitely.
	 * 
	 * @param __fmt The output audio format (see {@link AudioStreamFormat}).
	 * @param __buf The output audio buffer.
	 * @param __off The offset from which to start filling the output buffer.
	 * @param __len The amount of samples that can be placed into the buffer.
	 * @since 2025/12/24
	 */
	@KeepWhenCompacting
	private void __parseSongPatternHeader(
		@MagicConstant(valuesFromClass = AudioStreamFormat.class) int __fmt,
		@Nullable Object __buf,
		@Range(from = 0, to = Integer.MAX_VALUE) int __off,
		@Range(from = 0, to = Integer.MAX_VALUE) int __len)
	{
		// Read the pattern header
		// 3 bits for Pattern Header's beginning
		int patternHeader = this.__readBits(3);
		// 2 bits for pattern ID
		int patternId = this.__readBits(2);
		// 4 bits for loop value
		this._loopValue = (byte) this.__readBits(4);

		// 0xF value for looping means an infinite pattern loop was requested.
		if (this._loopValue == 0xF)
			this._loopValue = Byte.MAX_VALUE;

		// Marker for the current pattern start position, as we'll re-read it
		// as many times as there are loops.
		this._loopPatternPos = this._curPos; 
		this._loopCurBitPos = this._curBitPos;

		// We can now parse one loop of the pattern
		this.__parseSongPattern(__fmt, __buf, __off, __len);
	}

	/**
	 * Parses one loop of the current song pattern, handling its instructions.
	 * 
	 * @param __fmt The output audio format (see {@link AudioStreamFormat}).
	 * @param __buf The output audio buffer.
	 * @param __off The offset from which to start filling the output buffer.
	 * @param __len The amount of samples that can be placed into the buffer.
	 * @since 2025/12/24
	 */
	@KeepWhenCompacting
	private void __parseSongPattern(
		@MagicConstant(valuesFromClass = AudioStreamFormat.class) int __fmt,
		@Nullable Object __buf,
		@Range(from = 0, to = Integer.MAX_VALUE) int __off,
		@Range(from = 0, to = Integer.MAX_VALUE) int __len)
	{
		// LoopValue == 0 still means the pattern has to be entirely parsed at
		// least one time.
		this._curPos = this._loopPatternPos;
		this._curBitPos = this._loopCurBitPos;

		// Read the pattern specifier
		int patternSpecifier = this.__readBits(8);
		
		// For specifier 0b00000000, we must reuse the prior pattern
		if (patternSpecifier == 0x0)
		{
			// Well restore back to this position after reusing a pattern
			this._restorePatternPos = this._curPos;
			this._restorePatternBitPos = this._curBitPos;

			this._instructionsLeft = this._lastPatternLen;
			this._curPos = this._lastPatternPos;
			this._curBitPos = this._lastPatternBitPos;

			for (int i = 0; i < this._instructionsLeft; i++)
				// Parse one instructions until the audio buffer fills up
				if (this.__parsePatternInstruction(__fmt, __buf, __off, __len))
					break;

			// Only decrement if this is not an infinite loop
			if (this._loopValue < Byte.MAX_VALUE)
				this._loopValue--;

			// We can now continue reading the next bits
			this._curPos = this._restorePatternPos;
			this._curBitPos = this._restorePatternBitPos;

			if (this._loopValue < 0)
				this._songSequenceLength--;
			return;
		}

		// This means we have a new pattern length
		else 
		{
			// The number of instructions to read
			this._instructionsLeft = patternSpecifier;
			
			this._lastPatternLen = this._instructionsLeft;
			this._lastPatternPos = this._curPos;
			this._lastPatternBitPos = this._curBitPos;

			// Reset note Style and Scale, otherwise they will carry over
			// from the last pattern (which is incorrect despite the Smart
			// Message API not disclosing it)
			this._noteStyle = NokiaOTADecoder.NATURAL_STYLE;
			this._noteScale = 10;

			for (int i = 0; i < this._instructionsLeft; i++)
				if (this.__parsePatternInstruction(__fmt, __buf, __off, __len))
					break;
		}
		// Only decrement if this is not an infinite loop
		if (this._loopValue < Byte.MAX_VALUE)
			this._loopValue--;

		if (this._loopValue < 0)
			this._songSequenceLength--;
	}

	/**
	 * Parses one pattern instruction, which can be a Note, Scale, Style, Tempo
	 * or Volume instruction.
	 * 
	 * @param __fmt The output audio format (see {@link AudioStreamFormat}).
	 * @param __buf The output audio buffer.
	 * @param __off The offset from which to start filling the output buffer.
	 * @param __len The amount of samples that can be placed into the buffer.
	 * @return {@code true} if the output buffer has been filled, signifying
	 * that OTA parsing must be paused until the next call.
	 * @throws IllegalArgumentException If the parsed instruction is invalid.
	 * @since 2025/12/24
	 */
	@KeepWhenCompacting
	private boolean __parsePatternInstruction(
		@MagicConstant(valuesFromClass = AudioStreamFormat.class) int __fmt,
		@Nullable Object __buf,
		@Range(from = 0, to = Integer.MAX_VALUE) int __off,
		@Range(from = 0, to = Integer.MAX_VALUE) int __len)
		throws IllegalArgumentException
	{
		this._instructionsLeft--;

		// Read the instruction type (could be a note, scale, style,
		// tempo, or volume). 3 bits long
		int instructionType = this.__readBits(3);

		switch (instructionType) 
		{
				// Note Instruction
			case 0x1:
				return this.__parseNoteInstruction(__fmt, __buf, __off, __len);

				// Scale Instruction
			case 0x2: 
				this.__parseScaleInstruction();
				return false;

				// Style Instruction
			case 0x3: 
				this.__parseStyleInstruction();
				return false;

				// Tempo Instruction
			case 0x4: 
				this.__parseTempoInstruction();
				return false;

				// Volume Instruction
			case 0x5: 
				this.__parseVolumeInstruction();
				return false;

				// Invalid Instruction
			default:
				throw new IllegalArgumentException("Invalid Instruction type");
		}
	}

	/**
	 * Parses a note instruction.
	 * 
	 * @param __fmt The output audio format (see {@link AudioStreamFormat}).
	 * @param __buf The output audio buffer.
	 * @param __off The offset from which to start filling the output buffer.
	 * @param __len The amount of samples that can be placed into the buffer.
	 * @return {@code true} if the output buffer has been filled, signifying
	 * that OTA parsing must be paused until the next call.
	 * @since 2025/12/24
	 */
	@KeepWhenCompacting
	private boolean __parseNoteInstruction(
		@MagicConstant(valuesFromClass = AudioStreamFormat.class) int __fmt,
		@Nullable Object __buf,
		@Range(from = 0, to = Integer.MAX_VALUE) int __off,
		@Range(from = 0, to = Integer.MAX_VALUE) int __len)
	{
		// read 4 bits for note value
		int noteValue = this.__readBits(4);

		// Convert note value to the proper frequency
		int noteFreq = this.__getNoteFrequency(noteValue);

		// Period of the square wave
		this._sqWavePeriod = (noteFreq > 0 ? this._sampleRate / noteFreq *
			this._channels : 0);

		// Now read its duration and specifier
		// 3 bits for duration
		int duration = this.__readBits(3);
		// 2 bits for duration specifier
		int durSpecifier = this.__readBits(2);

		// Calculate final duration in samples
		this._noteDurationSmp = ((this.__durationToMs(duration, durSpecifier) *
			this._sampleRate) * this._channels) / 1000;

		// Check which style is currently being used in order to calculate the
		// note and rest proportions. Nokia's Smart Messaging documentation does
		// not disclose the note:rest ratios for the styles, so the values found
		// in Sony Ericsson's I-Melody are used here, as they do have some
		// similarities.

		// Set to the CONTINUOUS style rest duration first
		this._restDurationSmp = 0;

		// STACCATO has shorter notes with longer rest by making a note
		// end way before the next note begins to play.
		if (this._noteStyle == NokiaOTADecoder.STACCATO_STYLE)
		{
			this._noteDurationSmp /= 2;
			this._restDurationSmp = this._noteDurationSmp;
		}

		// NATURAL has a small rest between notes
		else if (this._noteStyle == NokiaOTADecoder.NATURAL_STYLE)
		{
			this._restDurationSmp = this._noteDurationSmp / 20;
			this._noteDurationSmp = (this._noteDurationSmp * 20) / 21;
		}

		return this.__generateSamples(__fmt, __buf, __off, __len);
	}

	/**
	 * Parses a scale instruction. The following scales are available:
	 * 
	 * 0x0 -> 440Hz
	 * 0x1 -> 880Hz
	 * 0x2 -> 1.76Khz
	 * 0x3 -> 3.52Khz
	 * 
	 * @since 2025/12/24
	 */
	@KeepWhenCompacting
	private void __parseScaleInstruction() 
	{
		int scaleValue = this.__readBits(2); // 2 bits are used for scale value

		switch (scaleValue) 
		{
				// Scale-1: A = 440 Hz
			case 0x0:
				this._noteScale = 5;
				break;

				// Scale-2: A = 880 Hz (default)
			case 0x1:
				this._noteScale = 10;
				break;

				// Scale-3: A = 1.76 kHz
			case 0x2:
				this._noteScale = 20;
				break;

				// Scale-4: A = 3.52 kHz
			case 0x3:
				this._noteScale = 40;
				break;

				// Invalid Scale
			default:
				throw new IllegalArgumentException("Invalid Note Scale Value");
		}
	}

	/**
	 * Parses a style instruction, which can resolve to either
	 * {@link NokiaOTADecoder#NATURAL_STYLE},
	 * {@link NokiaOTADecoder#CONTINUOUS_STYLE} or
	 * {@link NokiaOTADecoder#STACCATO_STYLE}.
	 * 
	 * @since 2025/12/24
	 */
	@KeepWhenCompacting
	private void __parseStyleInstruction() 
	{
		int styleValue = this.__readBits(2); // 2 bits for style value

		switch (styleValue) 
		{
				// Natural style (small rest between notes)
			case 0x0:
				this._noteStyle = NokiaOTADecoder.NATURAL_STYLE;
				break;

				// Continuous style (no rest between notes)
			case 0x1:
				this._noteStyle = NokiaOTADecoder.CONTINUOUS_STYLE;
				break;

				// Staccato style (larger rest between notes)
			case 0x2:
				this._noteStyle = NokiaOTADecoder.STACCATO_STYLE;
				break;

				// Invalid Style
			case 0x3:
			default:
				throw new IllegalArgumentException("Invalid Note Style Value");
		}
	}

	/**
	 * Parses a tempo (BPM) instruction, which will be in the 25 to 900 range.
	 * 
	 * @since 2025/12/24
	 */
	@KeepWhenCompacting
	private void __parseTempoInstruction() 
	{
		// Read 5 bits for BPM
		int bpmValue = this.__readBits(5);
		int bpm = 0;

		// Map the binary value to actual millisecond values based on the table
		// provided by Smart Messaging v3.0.0
		switch (bpmValue) 
		{
			case 0x00:
				this._quarterNoteMs = 2400;
				break;

			case 0x01:
				this._quarterNoteMs = 2143;
				break;

			case 0x02:
				this._quarterNoteMs = 1935;
				break;

			case 0x03:
				this._quarterNoteMs = 1714;
				break;

			case 0x04:
				this._quarterNoteMs = 1500;
				break;

			case 0x05:
				this._quarterNoteMs = 1333;
				break;

			case 0x06:
				this._quarterNoteMs = 1200;
				break;

			case 0x07:
				this._quarterNoteMs = 1071;
				break;

				// Default BPM
			case 0x08:
				this._quarterNoteMs = 952;
				break;

			case 0x09:
				this._quarterNoteMs = 857;
				break;

			case 0x0A:
				this._quarterNoteMs = 750;
				break;

			case 0x0B:
				this._quarterNoteMs = 667;
				break;

			case 0x0C:
				this._quarterNoteMs = 600;
				break;

			case 0x0D:
				this._quarterNoteMs = 536;
				break;

			case 0x0E:
				this._quarterNoteMs = 480;
				break;

			case 0x0F:
				this._quarterNoteMs = 429;
				break;

			case 0x10:
				this._quarterNoteMs = 375;
				break;

			case 0x11:
				this._quarterNoteMs = 333;
				break;

			case 0x12:
				this._quarterNoteMs = 300;
				break;

			case 0x13:
				this._quarterNoteMs = 267;
				break;

			case 0x14:
				this._quarterNoteMs = 240;
				break;

			case 0x15:
				this._quarterNoteMs = 211;
				break;

			case 0x16:
				this._quarterNoteMs = 187;
				break;

			case 0x17:
				this._quarterNoteMs = 169;
				break;

			case 0x18:
				this._quarterNoteMs = 150;
				break;

			case 0x19:
				this._quarterNoteMs = 133;
				break;

			case 0x1A:
				this._quarterNoteMs = 120;
				break;

			case 0x1B:
				this._quarterNoteMs = 106;
				break;

			case 0x1C:
				this._quarterNoteMs = 94;
				break;

			case 0x1D:
				this._quarterNoteMs = 84;
				break;

			case 0x1E:
				this._quarterNoteMs = 75;
				break;

			case 0x1F:
				this._quarterNoteMs = 67;
				break;

			default:
				throw new IllegalArgumentException("Invalid BPM Value");
		}
	}

	/**
	 * Parses a volume instruction, which will be in the 0 to 15 range.
	 * 
	 * @since 2025/12/24
	 */
	@KeepWhenCompacting
	private void __parseVolumeInstruction() 
	{
		int noteVolume = this.__readBits(4); // 4 bits for volume level

		// Approximately map the parsed volume value range (0-15) to the value
		// range of BYTE_U8 PCM (0-255)
		switch (noteVolume) 
		{
				// tone-off
			case 0x0:
				this._noteVolume = (byte) 0;
				break;

			case 0x1:
				this._noteVolume = (byte) 64;
				break;

			case 0x2:
				this._noteVolume = (byte) 80;
				break;

			case 0x3:
				this._noteVolume = (byte) 96;
				break;

			case 0x4:
				this._noteVolume = (byte) 112;
				break;

			case 0x5:
				this._noteVolume = (byte) 128;
				break;

			case 0x6:
				this._noteVolume = (byte) 144;
				break;

				// This is the default volume level (7)
			case 0x7:
				this._noteVolume = (byte) 160;
				break;

			case 0x8:
				this._noteVolume = (byte) 176;
				break;

			case 0x9:
				this._noteVolume = (byte) 184;
				break;

			case 0xA:
				this._noteVolume = (byte) 192;
				break;

			case 0xB:
				this._noteVolume = (byte) 200;
				break;

			case 0xC:
				this._noteVolume = (byte) 208;
				break;

			case 0xD:
				this._noteVolume = (byte) 216;
				break;

			case 0xE:
				this._noteVolume = (byte) 224;
				break;

			case 0xF:
			default:
				this._noteVolume = (byte) 255;
				break;
		}
	}

	/**
	 * Converts a parsed note value (standard musical C to B range) into its
	 * equivalent frequency.
	 * 
	 * Note that the returned value is based on the default scale of 880Hz,
	 * and thus, the A note in said scale will return 880Hz.
	 * 
	 * @param __noteValue The note value to convert into a frequency.
	 * @return The frequency of the received note.
	 * @since 2025/12/24
	 */
	@KeepWhenCompacting
	private int __getNoteFrequency(int __noteValue) 
	{
		short baseFrequency = 0;

		// Get the base frequency from the frequency table starting from C1
		switch (__noteValue) 
		{
				// Pause (no MIDI note)
			case 0x0: 
				return 0;

				// C1
			case 0x1:
				baseFrequency = 523;
				break;

				// C#1 (D1b)
			case 0x2:
				baseFrequency = 554;
				break;

				// D1
			case 0x3:
				baseFrequency = 587;
				break;

				// D#1 (E1b, so on)
			case 0x4:
				baseFrequency = 622;
				break;

				// E1
			case 0x5:
				baseFrequency = 659;
				break;

				// F1
			case 0x6:
				baseFrequency = 698;
				break;

				// F#1
			case 0x7:
				baseFrequency = 740;
				break;

				// G1
			case 0x8:
				baseFrequency = 784;
				break;

				// G#1
			case 0x9:
				baseFrequency = 831;
				break;

				// A1
			case 0xA:
				baseFrequency = 880;
				break;

				// A#1
			case 0xB:
				baseFrequency = 932;
				break;

				// B(or H)1
			case 0xC:
				baseFrequency = 988;
				break;

				// Invalid note, but CaveCab tries to add notes with reserved
				// values. Let's just return a pause instead of causing issues
				// for OTA playback.
			default:
				if (Debugging.VERBOSE)
					Debugging.debugNote(
						"Parsed Note: %s. Returning a pause instead.",
						NokiaOTADecoder.NOTE_STRINGS[__noteValue]);
				return 0; 
		}

		// Convert the frequency back to a MIDI note using the current note
		// scale factor. 
		//
		// In short: 
		// Scale-1 (0.5):           A1 -> A0
		// Scale-2 (1.0 - default): A1 -> A1
		// Scale-3 (2.0):           A1 -> A2
		// Scale-4 (4.0):           A1 -> A3
		baseFrequency = (short) (baseFrequency * this._noteScale / 10);

		// Let's only spend time with this calculation if we really need to
		// print it for debugging
		if (Debugging.VERBOSE) 
		{
			int octave = (int) Math.floor(ExtraMath.log(this._noteScale) /
				ExtraMath.log(2));
			if (octave < 0)
				octave = 0;

			Debugging.debugNote("Parsed Note: %s%d",
				NokiaOTADecoder.NOTE_STRINGS[__noteValue], octave + 1);
		}

		return baseFrequency;
	}

	/**
	 * Converts a parsed duration and specifier value into its equivalent ms.
	 * 
	 * The following values are expected for {@code __duration}:
	 * 0x0 -> full note (base duration * 4)
	 * 0x1 -> 1/2 note (base duration * 2)
	 * 0x2 -> 1/4 note (default)
	 * 0x3 -> 1/8 note (base duration / 2)
	 * 0x4 -> 1/16 note (base duration / 4)
	 * 0x5 -> 1/32 note (base duration / 8)
	 * 
	 * Whereas {@code __durSpecifier} expects the following values:
	 * 0x0 -> standar note (default)
	 * 0x1 -> dotted note (base duration * 1.5)
	 * 0x2 -> double dotted note (base duration * 1.75)
	 * 0x3 -> 2/3 note (base duration * (2 / 3))
	 * 
	 * Reserved values will use the defaults.
	 * 
	 * @param __duration The parsed note duration.
	 * @return The resulting ms duration.
	 * @since 2025/12/24
	 */
	@KeepWhenCompacting
	private int __durationToMs(int __duration, int __durSpecifier) 
	{
		// Base quarter note duration in ms
		int baseDuration = this._quarterNoteMs;

		switch (__duration) 
		{
				// Full note
			case 0x0:
				baseDuration *= 4;
				break; 

				// 1/2 note
			case 0x1:
				baseDuration *= 2;
				break; 

				// 1/8 note
			case 0x3:
				baseDuration /= 2;
				break; 

				// 1/16 note
			case 0x4:
				baseDuration /= 4;
				break; 

				// 1/32 note
			case 0x5:
				baseDuration /= 8;
				break; 

				// 1/4 note (default)
			case 0x2:

				// Default to 1/4 if reserved
			default:
				break;
		}

		// Now adjust above duration based on duration specifier
		switch (__durSpecifier) 
		{
				// Dotted note, increase duration by 50%
			case 0x1:
				baseDuration = (baseDuration * 15) / 10;
				break;

				// Double dotted note, increase duration by 75%
			case 0x2: 
				baseDuration = (baseDuration * 175) / 100;
				break;

				// 2/3 length note, reduce duration to about 2/3
			case 0x3: 
				baseDuration = (baseDuration * 2) / 3;
				break;

				// No special duration specifier
			case 0x0:

				// This case should not happen, just ignore if it does 
			default:
				break;
		}

		return baseDuration;
	}

	/**
	 * Generates square wave samples for parsed notes. Generation (and thus
	 * OTA parsing) continue until the output buffer is filled.
	 * 
	 * @param __fmt The output audio format (see {@link AudioStreamFormat}).
	 * @param __buf The output audio buffer.
	 * @param __off The offset from which to start filling the output buffer.
	 * @param __len The amount of samples that can be placed into the buffer.
	 * @return {@code true} if the output buffer has been filled, signifying
	 * that OTA parsing must be paused until the next call.
	 * @since 2025/12/24
	 */
	@KeepWhenCompacting
	private boolean __generateSamples(
		@MagicConstant(valuesFromClass = AudioStreamFormat.class) int __fmt,
		@Nullable Object __buf,
		@Range(from = 0, to = Integer.MAX_VALUE) int __off,
		@Range(from = 0, to = Integer.MAX_VALUE) int __len)
	{
		boolean filledBuffer = false;

		int genNoteSamples = this._noteDurationSmp;
		int genRestSamples = this._restDurationSmp;

		int sqWavePeriod = this._sqWavePeriod;
		byte masterVol = this._masterVolumeMult;

		// Final noteVolume will always fit in a byte, it uses int first such
		// as to not overflow when multiplying by the master volume.
		int noteVolume = this._noteVolume * masterVol / 100;

		if (genNoteSamples > __len)
		{
			genNoteSamples = __len;
			filledBuffer = true;
		}

		// Generate wave for note (duration - rest) duration, only if we have
		// an actual wave period and buffer to write.
		if (sqWavePeriod > 0 && __buf != null)
		{
			// A square wave is just an alternating HIGH-LOW value at equal
			// time lengths (50% duty cycle). A quick way of generating is
			// by making half of the samples alternate between high and low
			// values based on the wave's period. 
			switch(__fmt)
			{
				case AudioStreamFormat.BYTE_U8:
					byte[] bbuf = (byte[])__buf;

					byte byteValMax = (byte) (noteVolume & 0xFF);

					for (int i = 0; i < genNoteSamples; i++) 
						bbuf[__off + i] = (i % (sqWavePeriod * 2) <
							sqWavePeriod) ? byteValMax : 0;
					
					break;
				
				case AudioStreamFormat.SHORT_S16:
					short[] sbuf = (short[])__buf;

					short shortValMin = (short) -((noteVolume << 7) + 128);

					short shortValMax = (short) ((noteVolume << 7) + 127);

					for (int i = 0; i < genNoteSamples; i++) 
						sbuf[__off + i] = (i % (sqWavePeriod * 2) <
							sqWavePeriod) ? shortValMax : shortValMin;
					
					break;

				case AudioStreamFormat.INT_S32:
					int[] ibuf = (int[])__buf;

					int intValMin = (int) -((noteVolume << 23) + 8388608);

					int intValMax = (int) ((noteVolume << 23) + 8388607);
						
					for (int i = 0; i < genNoteSamples; i++) 
						ibuf[__off + i] = (i % (sqWavePeriod * 2) <
							sqWavePeriod) ? intValMax : intValMin;
					
					break;

				case AudioStreamFormat.FLOAT_F32:
					float[] fbuf = (float[])__buf;

					float floatValMin = -((float) noteVolume / 255.0f);

					float floatValMax = (float) noteVolume / 255.0f;

					for (int i = 0; i < genNoteSamples; i++) 
						fbuf[__off + i] = (i % (sqWavePeriod * 2) <
							sqWavePeriod) ? floatValMax : floatValMin;
					
					break;
			}
		}

		this._noteDurationSmp -= genNoteSamples;

		// Only bother with rest duration if there is space left in the buffer
		if (genNoteSamples < __len) 
		{
			// Calculate how many rest samples we can 'fill' into the buffer
			if (genRestSamples > __len - genNoteSamples) 
			{
				genRestSamples = __len - genNoteSamples;
				filledBuffer = true;
			}

			// 'Fill' the rest duration with silence (we actually do nothing,
			// as the audio buffer comes filled with silence by default, for
			// all formats)
			
			// Decrement rest duration
			this._restDurationSmp -= genRestSamples;
		}
		
		return filledBuffer;
	}

	/**
	 * Reads the specified amount of bits from the OTA data array.
	 * 
	 * @param __numBits The amount of bits to read.
	 * @return An integer representing the read bits.
	 * @since 2025/12/24
	 */
	@KeepWhenCompacting
	private int __readBits(
		@Range(from = 0, to = Integer.MAX_VALUE) int __numBits) 
	{
		int value = 0;

		int curPos = this._curPos;
		byte curBitPos = this._curBitPos;
		byte[] data = this._data;

		try 
		{
			for (int i = 0; i < __numBits; i++) 
			{
				// We should throw an IndexOutOfBoundsException here, but it breaks
				// the MIDP version of Mappy with its improperly formatted OTA.
				if (curPos >= data.length)
					return 0;
				

				value = (value << 1) | ((data[curPos] >> (7 - curBitPos)) & 1);

				// If current bit value is 8, we reached a new byte. Wrap to 0.
				if (++curBitPos == 8) 
				{
					curBitPos = 0;
					curPos++;
				}
			}
		} 
		finally 
		{
			this._curPos = curPos;
			this._curBitPos = curBitPos;
		}

		return value;
	}
}
