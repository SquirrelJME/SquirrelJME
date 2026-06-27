// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.media.ericsson;

import cc.squirreljme.jvm.mle.ObjectShelf;
import cc.squirreljme.jvm.mle.constants.AudioStreamChannels;
import cc.squirreljme.jvm.mle.constants.AudioStreamFormat;
import cc.squirreljme.jvm.mle.constants.AudioStreamRate;
import cc.squirreljme.runtime.cldc.annotation.KeepWhenCompacting;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.cldc.util.CharSequenceUtils;
import cc.squirreljme.runtime.media.control.AbstractDeviceFeedbackControl;
import cc.squirreljme.runtime.media.control.MetaDataValues;
import org.intellij.lang.annotations.MagicConstant;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;
import javax.microedition.media.MediaException;
import javax.microedition.media.control.MetaDataControl;

/**
 * Decodes Ericsson eMelody/iMelody files into audio samples to be sent over
 * scritchaudio, as well as controlling device's backlight, vibration and LED.
 * 
 * @since 2026/05/26
 */
@SquirrelJMEVendorApi
public class EricssonMelodyDecoder
{
	/** Array of note frequencies. Ericsson Melody maps to MIDI. */
	private static final short[] NOTE_FREQS = {
		8, 9, 9, 10, 10, 11, 12, 13, 13, 14, 15, 16, 17, 18,
		19, 21, 21, 24, 26, 27, 29, 30, 31, 33, 34, 36, 38,
		41, 43, 46, 49, 51, 55, 58, 61, 65, 69, 73, 77, 82,
		87, 92, 98, 104, 110, 116, 123, 130, 138, 146, 155,
		165, 175, 185, 196, 207, 220, 233, 247, 261, 277,
		294, 311, 330, 349, 370, 392, 415, 440, 466, 494,
		523, 554, 587, 622, 659, 698, 740, 784, 831, 880,
		932, 988, 1047, 1109, 1175, 1245, 1319, 1397, 1480,
		1568, 1661, 1760, 1865, 1976, 2093, 2217, 2349,
		2489, 2637, 2794, 2960, 3136, 3322, 3520, 3729,
		3951, 4186, 4435, 4699, 4978, 5274, 5588, 5919,
		6272, 6645, 7040, 7459, 7902, 8372, 8869, 9397,
		9956, 10548, 11175, 11840, 12544};

	/** Base volume for Ericsson Melody, goes from 0 to 15. */
	private byte _baseVolume;

	/** Volume multiplier for rendered samples (0-100%). */
	private byte _masterVolumeMult;

	/** Current position of the decoder in the data array. */
	private int _decodePos;

	/** Data array position where melody data actually begins. */
	private int _melodyStartPos;

	/** Data array position where a block loop must jump back to. */
	private int _melodyLoopStartPos;

	/** Data array position where a block loop ends. */
	private int _melodyLoopEndPos;

	/** Parsing position to restore after looping a block. */
	private int _melodyRestorePos;

	/** Volume modifier that is applied at each loop start. */
	private char _loopVolModifier;

	/** Current note style used by the melody. */
	private int _style;

	/** Current tempo used by the melody. */
	private int _tempo;

	/** How many milliseconds a quarter note takes in the melody. */
	private int _msPerQuarterNote;

	/** How many times we should loop the current block. */
	private int _loopValue;

	/** Indicates whether parsing is underway or not. */
	private boolean _parsing;

	/** Indicates if this is an eMelody. */
	private boolean _isEMelody;

	/** A note's duration, in samples. */
	private int _noteDurationSmp;

	/** Duration of the rest between the last note and the next, in samples. */
	private int _restDurationSmp;

	/** The sound output's sampling rate. */
	private int _sampleRate;

	/** The sound output's amount of channels. */
	private int _channels;

	/** The generated square wave's period. */
	private int _sqWavePeriod;

	/** The input stream containing melody data. */
	private byte[] _input;

	/* Device control instance, for LED, backlight and vibration events. */
	private AbstractDeviceFeedbackControl _device;

	/** Holds the Melody's metadata. */
	private MetaDataValues _metadata;

	/**
	 * Creates a new EricssonMelodyDecoder instance.
	 * 
	 * @param __input The byte array containing melody data.
	 * @param __control An {@link AbstractDeviceFeedbackControl} instance that
	 * allows this decoder to manage the device's LEDs, backlight and vibrator.
	 * @param __metadata A {@link MetaDataValues} instance to place Melody
	 * metadata on.
	 * @throws NullPointerException On null arguments.
	 * @since 2026/05/26
	 */
	@SquirrelJMEVendorApi
	public EricssonMelodyDecoder(@NotNull byte[] __input,
		@NotNull AbstractDeviceFeedbackControl __control,
		@NotNull MetaDataValues __metadata)
		throws NullPointerException
	{
		if (__input == null || __control == null || __metadata == null)
			throw new NullPointerException("NARG");
		
		this._input = __input;
		this._device = __control;
		this._metadata = __metadata;
		this._masterVolumeMult = 100;
		this._melodyStartPos = 0;
		this._melodyLoopStartPos = 0;
		this._isEMelody = false;
		this._style = 0; // Default style is natural style
		this._baseVolume = 7; // Default volume is 7 if not specified
		this._tempo = 120; // Default tempo is 120 if not defined
		this.reset();
	}

	/**
	 * Checks whether Melody parsing has finished or not.
	 * 
	 * @return {@code true} if parsing has finished.
	 * @since 2026/05/26
	 */
	@SquirrelJMEVendorApi
	public boolean hasFinished()
		throws NullPointerException
	{
		return this._decodePos >= this._input.length;
	}

	/**
	 * Begins parsing the received Melody data, filling the output buffer with
	 * audio data.
	 * 
	 * @param __fmt The output audio format (see {@link AudioStreamFormat}).
	 * @param __rate The format's sampling rate (see {@link AudioStreamRate}).
	 * @param __ch The format's channels (see {@link AudioStreamChannels}).
	 * @param __buf The output audio buffer.
	 * @param __off The offset from which to start filling the output buffer.
	 * @param __len The amount of samples that can be placed into the buffer.
	 * @throws IllegalArgumentException If any of the lower level parse methods
	 * receive invalid data from the Melody array, {@code __off} is negative,
	 * {@code __len} is negative, or {@code (__off + __len > __buf.length)}.
	 * @throws MediaException If the Melody data is corrupt and
	 * one of its multi-byte events is malformed.
	 * @since 2026/05/26
	 */
	@SquirrelJMEVendorApi
	public void parseMelody(
		@MagicConstant(valuesFromClass = AudioStreamFormat.class) int __fmt,
		@MagicConstant(valuesFromClass = AudioStreamRate.class) int __rate,
		@MagicConstant(valuesFromClass = AudioStreamChannels.class) int __ch,
		@Nullable Object __buf,
		@Range(from = 0, to = Integer.MAX_VALUE) int __off,
		@Range(from = 0, to = Integer.MAX_VALUE) int __len)
		throws MediaException, IllegalArgumentException
	{
		// If output buffer is null, we do nothing until it is a valid array.
		if (__buf == null)
			return;

		if (__off < 0 || __len < 0)
			throw new IllegalArgumentException("NEGV");

		if ((__off + __len > ObjectShelf.arrayLength(__buf)))
			throw new IllegalArgumentException("IOOB");

		// If we're not parsing yet (i.e. we didn't begin parsing and had to
		// stop in order to wait for a new buffer to arrive after filling the
		// previous one with audio data), parse from the start of the Melody.
		if (!this._parsing)
		{
			this._sampleRate = __rate;
			this._channels = __ch;
			this._parsing = true;
		}

		// If any of the note or rest durations are more than 0, that means we
		// couldn't fit an entire note duration into the buffer, and must thus
		// populate it with the duration remainder.
		if (this._noteDurationSmp > 0 || this._restDurationSmp > 0)
			if (this.__generateSamples(__fmt, __buf, __off, __len))
				return;

		// If we didn't get the melody start position yet, that means we must
		// read the header first
		if (this._melodyStartPos == 0)
			this.__decodeMelodyHeader();

		// Otherwise, move on to decoding melody blocks.
		this.__decodeMelodyBlocks(__fmt, __buf, __off, __len);
	}

	/**
	 * Resets the Melody parser, setting all flags so that the data can be
	 * parsed from the beginning.
	 * 
	 * @since 2026/05/26
	 */
	@SquirrelJMEVendorApi
	public void reset()
	{
		// Reset all variables to parse back from the melody's beginning
		this._decodePos = this._melodyStartPos;
		this._melodyLoopStartPos = this._melodyStartPos;
		this._melodyLoopEndPos = 0;
		this._melodyRestorePos = 0;
		this._loopVolModifier = ' ';
		this._parsing = false;
	}

	/**
	 * Sets the master volume multiplier for generated notes, where the MIDP's
	 * range of {@code [0, 100]} results in a multiplier range of
	 * {@code [0, 1]}. Values outside this range will be clamped.
	 * 
	 * @param __volume The volume to set.
	 * @since 2026/05/26
	 */
	@SquirrelJMEVendorApi
	public void setMasterVolume(
		@Range(from = 0, to = 100) int __volume)
	{
		if (__volume < 0)
			__volume = 0;

		if (__volume > 100)
			__volume = 100;

		this._masterVolumeMult = (byte)__volume;
	}

	/**
	 * Decodes an Ericsson Melody header to setup tempo, volume, note style,
	 * media info, among others.
	 * 
	 * @throws MediaException If the Melody data is corrupt and/or one of its
	 * multi-byte events is malformed.
	 * @since 2026/05/26
	 */
	@KeepWhenCompacting
	private void __decodeMelodyHeader()
		throws MediaException
	{
		byte[] input = this._input;
		char c;
		StringBuilder nextString = new StringBuilder();

		if (Debugging.VERBOSE)
			Debugging.debugNote("------------EMS Header------------");

		// Parse the header. Blocks are separated by a newline.
		for (;;)
		{
			do
			{
				c = this.__seqNext(false);
				nextString.append(c);
			} while (c >= 0 && c != '\n' &&
				!CharSequenceUtils.startsWith(nextString, "MELODY:", 0));

			// Print debug lines for the whole header, if allowed.
			if (Debugging.VERBOSE)
				Debugging.debugNote(nextString.toString());

			// Only name, composer and copyright are actual media metadata
			if (CharSequenceUtils.startsWith(nextString, "NAME", 0) ||
				CharSequenceUtils.startsWith(nextString, "COMPOSER", 0) ||
				CharSequenceUtils.startsWith(nextString, "COPYRIGHT", 0))
			{
				String metadataStr = nextString.toString().trim();

				if (CharSequenceUtils.startsWith(nextString, "NAME", 0))
					this._metadata.set(MetaDataControl.TITLE_KEY,
						metadataStr.substring(metadataStr.indexOf(':') + 1));

				if (CharSequenceUtils.startsWith(nextString, "COMPOSER", 0))
					this._metadata.set(MetaDataControl.AUTHOR_KEY,
						metadataStr.substring(metadataStr.indexOf(':') + 1));

				if (CharSequenceUtils.startsWith(nextString, "COPYRIGHT", 0))
					this._metadata.set(MetaDataControl.COPYRIGHT_KEY,
						metadataStr.substring(metadataStr.indexOf(':') + 1));
			}
				
			// Is this an eMelody?
			if (CharSequenceUtils.equals(nextString, "BEGIN:EMELODY\n"))
				this._isEMelody = true;

			if (CharSequenceUtils.startsWith(nextString, "BEAT", 0))
			{
				String tempoStr = nextString.toString().trim();
				tempoStr = tempoStr.substring(tempoStr.indexOf(':') + 1);
				
				// Some Melody files do concatenate the volume level on the
				// BEAT block, even though the specification doesn't have any
				// notes about this being allowed or not.
				if (tempoStr.contains(","))
				{
					String volumeStr = tempoStr.substring(
						tempoStr.lastIndexOf('=') + 1);

					this._baseVolume = Byte.parseByte(volumeStr);

					tempoStr = tempoStr.substring(0, tempoStr.indexOf(','));
				}

				this._tempo = Integer.parseInt(tempoStr);
			}

			if (CharSequenceUtils.startsWith(nextString, "VOLUME", 0))
			{
				String volumeStr = nextString.toString().trim();

				this._baseVolume = Byte.parseByte(volumeStr.substring(
					volumeStr.lastIndexOf('V') + 1));
			}
				
			if (CharSequenceUtils.startsWith(nextString, "STYLE", 0))
			{
				String styleStr = nextString.toString().trim();

				this._style = Integer.parseInt(styleStr.substring(
					styleStr.lastIndexOf('S') + 1));
			}

			// We reached the melody block. Get out of the loop.
			if (nextString.toString().equals("MELODY:"))
				break;
				
			nextString.setLength(0);
		}

		// Set up the quarter-note's reference ms duration.
		this._msPerQuarterNote = 60000 / this._tempo;

		// We are at the point where the melody actually starts.
		this._melodyStartPos = this._decodePos;
		this._melodyLoopStartPos = this._decodePos;
	}

	/**
	 * Decodes Ericsson Melody blocks, which contain the actual reproducible
	 * media data, as well as vibration, LED, and Backlight control.
	 * 
	 * @param __fmt The output audio format (see {@link AudioStreamFormat}).
	 * @param __buf The output audio buffer.
	 * @param __off The offset from which to start filling the output buffer.
	 * @param __len The amount of samples that can be placed into the buffer.
	 * @throws MediaException If the Melody data is corrupt and/or one of its
	 * multi-byte events is malformed.
	 * @since 2026/05/26
	 */
	@KeepWhenCompacting
	private void __decodeMelodyBlocks(
		@MagicConstant(valuesFromClass = AudioStreamFormat.class) int __fmt,
		@Nullable Object __buf,
		@Range(from = 0, to = Integer.MAX_VALUE) int __off,
		@Range(from = 0, to = Integer.MAX_VALUE) int __len)
		throws MediaException
	{
		boolean eMelody = this._isEMelody;
		byte[] input = this._input;
		int octave = 4;
		int noteDuration = 0;
		int noteModifier = 0;
		int noteValue = 0;
		StringBuilder loopStr;
		char currentChar = ' ';

		for (; this._decodePos < input.length; this.__seqNext(false))
		{
			// If we finished decoding the melody data, skip over "END:MELODY"
			if (this.__seqCheck(0, "END")) 
			{
				this.__seqSet(input.length);

				if (Debugging.VERBOSE)
					Debugging.debugNote("REACHED END:MELODY");

				return;
			}

			currentChar = this.__seqPeek(0);

			// If this is an eMelody, parsing is simpler and very different from
			// iMelody.
			if (eMelody)
			{
				// Higher octave modifier
				if (currentChar == '+') 
				{ 
					octave += 1; 
					continue; 
				}

				// Sharp note -> Increase next Midi note value by 1
				if (currentChar == '#') 
				{ 
					noteModifier = 1; 
					continue; 
				}

				// eMelody note specifier
				if (EricssonMelodyDecoder.__isNoteCharacter(currentChar)) 
				{
					noteValue = EricssonMelodyDecoder.__getNoteFreq(
						Character.toLowerCase(currentChar), octave,
						noteModifier);

					this._sqWavePeriod = (noteValue > 0 ? this._sampleRate /
						noteValue * this._channels : 0);
					
					// In eMelody, it appears that uppercase notes are longer
					// half notes, while lowercase ones are eigth notes.
					// Reference: https://web.archive.org/web/
					// 20260309055027/https://www.fmjsoft.com/fmt/emy.htm

					// There are apparently "two" eMelody formats, but only the
					// simpler one (supported here) is text-based and manually
					// manipulatable, so it is probably the only eMelody type
					// we'll be able to find on Ericsson apps and the web.
					this._noteDurationSmp = ((this.__getNoteDuration(
						Character.isUpperCase(currentChar) ? 1 : 3) *
						this._sampleRate) * this._channels) / 1000;

					// Set to the CONTINUOUS style rest duration first
					this._restDurationSmp = 0;

					// Apply the note style to note and rest durations
					this.__noteStyle();

					// We must now restart the note modifier and octave for
					// eMelody
					noteModifier = 0;
					octave = 4;

					if (this.__generateSamples(__fmt, __buf, __off, __len))
					{
						// If we can't fit all samples in a single call, we must
						// return outright (and increment decodePos beforehand,
						// otherwise the decoder will read duplicate chars)
						this.__seqSkip(1);
						return;
					}
					
					continue;
				}
			}

			// Not a simple eMelody, thus it must be parsed as an iMelody.

			if (this.__seqCheck(0, "led"))
			{
				if (this.__seqCheck(3, "on"))
				{
					this._device.emitLight(false, Integer.MAX_VALUE);
					this.__seqSkip(4);
					continue;
				}

				else if (this.__seqCheck(3, "off"))
				{
					this._device.emitLight(false, 0);
					this.__seqSkip(5);
					continue;
				}
			}

			if (this.__seqCheck(0, "vibe"))
			{
				if (this.__seqCheck(4, "on"))
				{
					this._device.emitVibrate(Integer.MAX_VALUE);
					this.__seqSkip(5);
					continue;
				}

				else if (this.__seqCheck(4, "off"))
				{
					this._device.emitVibrate(0);
					this.__seqSkip(6);
					continue;
				}
			}

			if (this.__seqCheck(0, "back"))
			{
				if (this.__seqCheck(4, "on"))
				{
					this._device.emitLight(true, Integer.MAX_VALUE);
					this.__seqSkip(5);
					continue;
				}

				else if (this.__seqCheck(4, "off"))
				{
					this._device.emitLight(true, 0);
					this.__seqSkip(6);
					continue;
				}
			}

			// note volume modifier
			if (currentChar == 'V')
			{
				this.__updateVolume(this.__seqNext(true));
				continue;
			}

			// Flat note -> Decrease next note value by 1
			// Sharp note -> Increase next note value by 1
			if (currentChar == '&' || currentChar == '#') 
			{ 
				noteModifier = (currentChar == '&') ? -1 : 1; 
				continue; 
			}

			// Note octave change
			if (currentChar == '*') 
			{
				octave = this.__seqNext(true) - '0';
				continue;
			}

			// Silence event (appears to not be affected by Style)
			if (currentChar == 'r') 
			{
				this._restDurationSmp = ((this.__getNoteDuration(
					this.__seqNext(true) - '0') * this._sampleRate) *
					this._channels) / 1000;

				// Check if the next char is a note duration specifier.
				this._restDurationSmp = this.__getDurationSpecifier(
					this.__seqPeek(1), this._restDurationSmp);

				if (this.__generateSamples(__fmt, __buf, __off, __len))
				{
					// If we can't fit all samples in a single call, we must
					// return outright (and increment decodePos beforehand,
					// otherwise the decoder will read duplicate chars)
					this.__seqSkip(1);
					return;
				}

				continue;
			}
			
			// Actual note specifier
			if (EricssonMelodyDecoder.__isNoteCharacter(currentChar)) 
			{
				// Uppercase note values will throw an exception here as only
				// eMelody supports them.
				noteValue = EricssonMelodyDecoder.__getNoteFreq(currentChar,
					octave, noteModifier);

				this._sqWavePeriod = (noteValue > 0 ? this._sampleRate /
					noteValue * this._channels : 0);

				this._noteDurationSmp = ((this.__getNoteDuration(
					this.__seqNext(true) - '0') * this._sampleRate) *
					this._channels) / 1000;

				// Check if the next char is a note duration specifier.
				this._noteDurationSmp = this.__getDurationSpecifier(
					this.__seqPeek(1), this._noteDurationSmp);

				// Set to the CONTINUOUS style rest duration first
				this._restDurationSmp = 0;

				// Apply the note style to note and rest durations
				this.__noteStyle();

				// Restore the note modifier
				noteModifier = 0; 

				if (this.__generateSamples(__fmt, __buf, __off, __len))
				{
					// If we can't fit all samples in a single call, we must
					// return outright (and increment decodePos beforehand,
					// otherwise the decoder will read duplicate chars)
					this.__seqSkip(1);
					return;
				}

				continue;
			}

			// '@' is a special character denoting how many times a block should
			// repeat
			if (currentChar == '@') 
			{
				// Check if we're already looping a block
				if (this._decodePos == this._melodyLoopEndPos)
				{
					// Only decrement if this is not an infinite loop
					if (this._loopValue != Integer.MAX_VALUE)
						this._loopValue--;

					// If the decremented value is now 0, that means we looped
					// the amount of times we needed, so jump out of the block.
					if (this._loopValue <= 0)
					{
						this._decodePos = this._melodyRestorePos - 1;
						this._melodyLoopStartPos = this._decodePos;
					}

					// Anything else, we must do another loop, jump back to the
					// loop block's beginning.
					else
						this._decodePos = this._melodyLoopStartPos - 1;

					// Update volume before looping
					this.__updateVolume(this._loopVolModifier);

					continue;
				}

				// If we aren't looping yet, prepare the block's loop.

				// Loop ends right here, so we can use the '@' for control.
				this._melodyLoopEndPos = this._decodePos++;

				// Get the amount of loops to be done, iMelody doesn't specify
				// a maximum amount of decimal places for repeat values. So we
				// must read until the next character is no longer a digit.
				loopStr = new StringBuilder();

				do
				{
					loopStr.append(this.__seqNext(false));
				} 
				while (Character.isDigit((char) input[this._decodePos]));

				this._loopValue = Integer.parseInt(loopStr.toString());
				
				// 0 means infinite looping of a block
				if (this._loopValue == 0)
					this._loopValue = Integer.MAX_VALUE;

				this._loopVolModifier = ' ';

				// After the '@' character and its immediately following loop
				// count, there may be a volume modifier that should be applied
				// to increase/decrease volume one step at each loop start.
				if ((char) input[this._decodePos] == 'V')
				{
					this._loopVolModifier = this.__seqNext(true);
					this.__seqSkip(1);
				}
					
				// Update volume before looping
				this.__updateVolume(this._loopVolModifier);

				// After looping ends, we must restore out of the looped block.
				this._melodyRestorePos = this._decodePos;

				// Begin the loop now
				this._decodePos = this._melodyLoopStartPos - 1;

				continue;
			}

			// Usually a block will be delimited by parenthesis, however,
			// we can just skip over them as the '@' specifier is what indicates
			// how many times a block must loop, and it is always at the very
			// end of a block with all the parameters it needs.
			//
			// The same can be done about carriage returns, new lines and
			// spaces, as the spec dictates they can be used to break up long
			// melody lines.
			if (currentChar == '(' || currentChar == ')' ||
				currentChar == '\n' || currentChar == '\r' ||
				currentChar == ' ')
				continue;

			throw new MediaException("Unknown character:" + currentChar);
		}
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
	 * @since 2026/05/26
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
		int noteVolume = (this._baseVolume * 8 + 7) * masterVol / 100;

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
			switch (__fmt)
			{
				case AudioStreamFormat.BYTE_U8:
					byte[] bbuf = (byte[])__buf;

					byte byteValMax = (byte)(noteVolume & 0xFF);

					for (int i = 0; i < genNoteSamples; i++) 
						bbuf[__off + i] = ((i % (sqWavePeriod * 2) <
							sqWavePeriod) ? byteValMax : 0);
					
					break;
				
				case AudioStreamFormat.SHORT_S16:
					short[] sbuf = (short[])__buf;

					short shortValMin = (short)(-((noteVolume << 8) + 256));
					short shortValMax = (short)((noteVolume << 8) + 255);

					for (int i = 0; i < genNoteSamples; i++) 
						sbuf[__off + i] = ((i % (sqWavePeriod * 2) <
							sqWavePeriod) ? shortValMax : shortValMin);
					
					break;

				case AudioStreamFormat.INT_S32:
					int[] ibuf = (int[])__buf;

					int intValMin = -((noteVolume << 24) + 16777216);
					int intValMax = (noteVolume << 24) + 16777215;
						
					for (int i = 0; i < genNoteSamples; i++) 
						ibuf[__off + i] = ((i % (sqWavePeriod * 2) <
							sqWavePeriod) ? intValMax : intValMin);
					
					break;

				case AudioStreamFormat.FLOAT_F32:
					float[] fbuf = (float[])__buf;

					float floatValMin = -((float)noteVolume / 255.0f);
					float floatValMax = (float)noteVolume / 255.0f;

					for (int i = 0; i < genNoteSamples; i++) 
						fbuf[__off + i] = ((i % (sqWavePeriod * 2) <
							sqWavePeriod) ? floatValMax : floatValMin);
					
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
	 * Updates a note or rest duration if the received {@code __char} argument
	 * matches one of the valid specifiers, which may be dotted note ('.'),
	 * double-dotted note (';'), or a 2/3 length note (':').
	 * 
	 * @param __char The character to be checked against the valid specifiers.
	 * @param __duration The base note or rest duration to update.
	 * @return The updated duration value if {@code __char} matches any of the
	 * valid specifiers.
	 * @throws MediaException If {@code __duration} is negative.
	 * @since 2026/06/10
	 */
	@KeepWhenCompacting
	private int __getDurationSpecifier(char __char,
		@Range(from = 0, to = Integer.MAX_VALUE) int __duration)
		throws MediaException
	{
		if (__duration < 0)
			throw new MediaException("Invalid duration value");

		switch (__char)
		{
				// dotted note
			case '.':
				this.__seqSkip(1);
				return (__duration * 15) / 10;

				// double dotted note
			case ';':
				this.__seqSkip(1);
				return (__duration * 175) / 100;

				// 2/3 length note
			case ':':
				this.__seqSkip(1);
				return (__duration * 2) / 3;

			default:
				return __duration;
		}
	}

	/**
	 * Gets the proper value of a note's duration based on its parsed character.
	 * 
	 * @param __duration The duration value.
	 * @return The resulting note duration based on the duration value.
	 * @throws MediaException If the duration is invalid.
	 * @since 2026/05/26
	 */
	@KeepWhenCompacting
	private int __getNoteDuration(
		@Range(from = 0, to = 5) int __duration)
		throws MediaException
	{
		// Calculate duration in ticks based on the duration value
		switch (__duration) 
		{
			case 0: return this._msPerQuarterNote * 4; // Full-note
			case 1: return this._msPerQuarterNote * 2; // 1/2-note
			case 2: return this._msPerQuarterNote;     // 1/4-note
			case 3: return this._msPerQuarterNote / 2; // 1/8-note
			case 4: return this._msPerQuarterNote / 4; // 1/16-note
			case 5: return this._msPerQuarterNote / 8; // 1/32-note
			default:
				throw new MediaException("Invalid Note Duration: " +
					__duration);
		}
	}

	/**
	 * Applies the current note style to the parsed note's duration. The
	 * note-to-rest ratios are as follows:
	 * 
	 * For Staccato Style, the note:rest ratio is 1:1, which means half of the
	 * total duration is reserved for the note, and half for rest.
	 * 
	 * For Natural Style, the ratio is 20:1.
	 * 
	 * For Continuous style, there is no rest, thus this method does nothing.
	 * 
	 * @since 2026/06/10
	 */
	@KeepWhenCompacting
	private void __noteStyle()
	{
		switch (this._style)
		{
				// Natural Style, 20:1 ratio of note:rest
			case 0:
				this._restDurationSmp = this._noteDurationSmp / 20;
				this._noteDurationSmp = (this._noteDurationSmp * 20) / 21;
				break;

				// Continuous style, no rest.
			case 1:
				break;

				// Staccato Style, 1:1 ratio of note:rest
			case 2: 
				this._noteDurationSmp /= 2;    
				this._restDurationSmp = this._noteDurationSmp;
		}
	}

	/**
	 * Checks if the next Melody character, from the given offset, matches the
	 * given specified character.
	 * 
	 * @param __off The position offset from which to read the Melody character.
	 * @param __what The character to check against.
	 * @return Whether the Melody character matches the given character.
	 * @throws MediaException If {@code __off} is a value that,
	 * when added to the current pointer position, goes out of bounds in regards
	 * to the Melody data array.
	 * @since 2026/06/10
	 */
	@KeepWhenCompacting
	private boolean __seqCheck(
		@Range(from = 0, to = Integer.MAX_VALUE) int __off, char __what)
		throws MediaException
	{
		if (this._decodePos + __off < 0 || this._decodePos + __off >=
			this._input.length)
			throw new MediaException("IOOB");

		return ((char)this._input[this._decodePos + __off] == __what);
	}

	/**
	 * Checks if the next chain of characters, from the given offset, are equal
	 * to the specified string.
	 * 
	 * @param __off The position offset from which to read Melody characters.
	 * @param __what The string to check against.
	 * @return Whether the Melody character sequence matches the given string.
	 * @throws MediaException If {@code __num} is a value that,
	 * when added to the current pointer position and {@code __what}'s length,
	 * goes out of bounds in regards to the Melody data array.
	 * @since 2026/06/10
	 */
	@KeepWhenCompacting
	private boolean __seqCheck(
		@Range(from = 0, to = Integer.MAX_VALUE) int __off, String __what)
		throws MediaException
	{
		if (this._decodePos + __off < 0 ||
			this._decodePos + __off + __what.length() >= this._input.length)
			throw new MediaException("IOOB");

		for (int n = __what.length(), i = 0; i < n; i++)
			if (!this.__seqCheck(__off + i, __what.charAt(i)))
				return false;

		return true;
	}

	/**
	 * Returns the current character in the Melody sequence and moves the
	 * pointer to the next position. Moving can be done before or after
	 * returning the current character, based on the {@code __preIncrement}
	 * argument.
	 * 
	 * @param __preIncrement Dictates whether the position is incremented before
	 * of after returning the character.
	 * @return The current character in the Melody, pre or post increment.
	 * @throws MediaException If the read pointer is already at
	 * the end of the Melody data array.
	 * @since 2026/06/10
	 */
	@KeepWhenCompacting
	private char __seqNext(boolean __preIncrement)
		throws MediaException
	{
		if (this._decodePos >= this._input.length)
			throw new MediaException("IOOB");

		return (char)(__preIncrement ? this._input[++this._decodePos] :
			this._input[this._decodePos++]);
	}

	/**
	 * Returns the character in the Melody sequence at the specified offsef from
	 * the current pointer posirion.
	 * 
	 * @param __off The position offset from which to read Melody characters.
	 * @return The current character in the Melody, from the offset.
	 * @throws MediaException If {@code __off} is a value that,
	 * when added to the current pointer position, goes out of bounds in regards
	 * to the Melody data array.
	 * @since 2026/06/10
	 */
	@KeepWhenCompacting
	private char __seqPeek(
		@Range(from = 0, to = Integer.MAX_VALUE) int __off)
		throws MediaException
	{ 
		if (this._decodePos + __off < 0 || this._decodePos + __off >=
			this._input.length)
			throw new MediaException("IOOB");

		return (char)this._input[this._decodePos + __off];
	}

	/**
	 * Sets the decoder to the specified position in the Melody data array.
	 * 
	 * @param __pos The position to move the decoder's pointer to.
	 * @throws MediaException If {@code __pos} is less than zero
	 * or larger than the Melody array's length.
	 * @since 2026/06/10
	 */
	@KeepWhenCompacting
	private void __seqSet(
		@Range(from = 0, to = Integer.MAX_VALUE) int __pos)
		throws MediaException
	{
		if (__pos < 0 || __pos > this._input.length)
			throw new MediaException("IOOB");

		this._decodePos = __pos;
	}

	/**
	 * Skips the given number of characters from the Melody data array.
	 * 
	 * @param __num The amount of characters to skip.
	 * @throws IllegalArgumentException If {@code __num} is {@code <= 0}.
	 * @throws MediaException If {@code __num} is a value that,
	 * when added to the current pointer position, goes out of bounds in regards
	 * to the Melody data array.
	 * @since 2026/06/10
	 */
	@KeepWhenCompacting
	private void __seqSkip(
		@Range(from = 0, to = Integer.MAX_VALUE) int __num)
		throws MediaException
	{
		if (__num <= 0)
			throw new IllegalArgumentException("INVL");

		if (this._decodePos + __num > this._input.length)
			throw new MediaException("IOOB");

		this.__seqSet(this._decodePos + __num);
	}

	/**
	 * Increases or decreases the melody's base volume by one step depending on
	 * the received modifier. Values lower than 0 or higher than 15 are clamped
	 * to said range.
	 * 
	 * @param __modifier The volume modifier to apply, can be either {@code '+'}
	 * or {@code '-'}. Any other characters are ignored.
	 * @since 2026/06/07
	 */
	@KeepWhenCompacting
	private void __updateVolume(char __modifier)
	{
		switch (__modifier)
		{
				// V+ increases volume by one step
			case '+':
				this._baseVolume = (byte)Math.min(this._baseVolume + 1, 15);
				break;

				// V- decreases volume by one step
			case '-':
				this._baseVolume = (byte)Math.max(this._baseVolume - 1, 0);
		}
	}

	/**
	 * Retrieves the proper frequency for a given combination of note, octave
	 * and sharp/flat modifier.
	 * 
	 * @param __note The base note character.
	 * @param __octave The current octave to use.
	 * @param __modifier The note modifier (sharp or flat).
	 * @return The frequency that matches the combination of note, octave and
	 * modifier.
	 * @throws MediaException If the base note character is invalid.
	 * @since 2026/05/26
	 */
	@KeepWhenCompacting
	private static int __getNoteFreq(char __note,
		@Range(from = 0, to = 8) int __octave,
		@Range(from = -1, to = 1) int __modifier)
		throws MediaException
	{
		int baseNote = 0;

		switch (__note) 
		{
			case 'c': baseNote = 12; break;
			case 'd': baseNote = 14; break; // D4
			case 'e': baseNote = 16; break; // E4
			case 'f': baseNote = 17; break; // F4
			case 'g': baseNote = 19; break; // G4
			case 'a': baseNote = 21; break; // A4
			case 'b': baseNote = 23; break; // B4
			default:
				throw new MediaException("Invalid Note:" + __note);
		};

		baseNote = baseNote + (__octave * 12) + __modifier;
		return EricssonMelodyDecoder.NOTE_FREQS[baseNote];
	}

	/**
	 * Checks if the given character is a valid Ericsson Melody Note.
	 * 
	 * @param __c The character to check.
	 * @return {@code true} If the parsed character is a note.
	 * @since 2026/05/26
	 */
	@KeepWhenCompacting
	private static boolean __isNoteCharacter(char __c)
	{ 
		return (__c >= 'a' && __c <= 'g') || (__c >= 'A' && __c <= 'G');
	}
}
