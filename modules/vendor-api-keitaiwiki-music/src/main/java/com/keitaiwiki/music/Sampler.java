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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

/**
 * Sample-generating instance of a {@code Sampler}. Configurations on the
 * sampler object can propagate down to its instances.<br><br>
 * Any unsupported features specified by the methods of this class
 * (such as
 * channel numbers that are beyond what the sampler supports) are silently
 * ignored without throwing any exceptions.<br><br>
 * Default settings for all properties are as follows:
 * <table class="striped" style="margin-left:2em;text-align:left">
 *  <caption style="display:none">X</caption>
 *  <thead>
 *    <tr><th>Scope</th><th>Property</th><th>Default</th></tr>
 *  </thead>
 *  <tbody>
 *    <tr><td>Channel</td><td>Drums enabled</td><td>false</td></tr>
 *    <tr><td>Channel</td><td>Panning</td><td>0.0f</td></tr>
 *    <tr><td>Channel</td><td>Pitch bend</td><td>0.0f</td></tr>
 *    <tr><td>Channel</td><td>Pitch bend range</td><td>2.0f</td></tr>
 *    <tr><td>Channel</td><td>Program bank</td><td>0</td></tr>
 *    <tr><td>Channel</td><td>Program number</td><td>0</td></tr>
 *    <tr><td>Channel</td><td>Volume</td><td>1.0f</td></tr>
 *    <tr><td>Master</td><td>Tuning</td><td>0.0f</td></tr>
 *    <tr><td>Master</td><td>Volume</td><td>1.0f</td></tr>
 *  </tbody>
 * </table>
 *
 * @see SamplerProvider
 * @since 2025/05/05
 */
@SquirrelJMEVendorApi
public interface Sampler
{
	/**
	 * Specify a channel's program bank. The bank expands the total number
	 * of programs available to a channel.<br><br>
	 * The default program bank number on all channels is 0.
	 *
	 * @param __channel The index of the channel to configure.
	 * @param __bank The bank number.
	 * @see #programChange(int, int)
	 * @since 2025/05/05
	 */
	@SquirrelJMEVendorApi
	void bankChange(int __channel, int __bank);
	
	/**
	 * Specify whether a channel should play drum notes. <br><br>
	 * The default drums-enabled setting on all channels is {@code false}.
	 *
	 * @param __channel The index of the channel to configure.
	 * @param __enable Whether to enable drum notes on the channel.
	 * @since 2025/05/05
	 */
	@SquirrelJMEVendorApi
	void drumEnable(int __channel, boolean __enable);
	
	/**
	 * Determine whether or not any notes are producing output.
	 *
	 * @return {@code true} if there are no notes generating any output.
	 * @since 2025/05/05
	 */
	@SquirrelJMEVendorApi
	boolean isFinished();
	
	/**
	 * Deactivate a key that has previoulsy been activated on a channel.
	 * If no key is currently active on the channel, no action is taken.
	 *
	 * @param __channel The index of the channel to configure.
	 * @param __key The number of the key to configure. A value of zero
	 * corresponds to the note A<sub>4</sub>.
	 * @see #keyOn(int, int, float)
	 * @since 2025/05/05
	 */
	@SquirrelJMEVendorApi
	void keyOff(int __channel, int __key);
	
	/**
	 * Activate a key on a channel. If no key is currently active on the
	 * channel, begins a new note. If a key is already active on the
	 * channel, this method may change its frequency and volume without
	 * restarting it, although this behavior is not guaranteed.<br><br>
	 * Frequencies are given by the following formula:<br><br>
	 * <code>&nbsp; &nbsp; &nbsp; frequencyHz = 440 * 2<sup
	 * >((key + pitchBend) / 12)</sup></code><br><br>
	 * 440 Hz is the frequency of the note A<sub>4</sub>, known as
	 * "concert
	 * A" (the A above middle C), and is the reference frequency in most
	 * modern instrument tuning. For regular instruments, {@code key}
	 * specifies the number of semitones relative to A<sub>4</sub>.
	 * Although the number doesn't have to be an integer for the purposes
	 * of the formula, it <i>does</i> have to be an integer for other
	 * contexts such as drum notes, which use the key number to determine
	 * the exact drum sound to play.
	 *
	 * @param __channel The index of the channel to configure.
	 * @param __key The number of the key to configure. A value of zero
	 * corresponds to the note A<sub>4</sub>.
	 * @param __velocity The overall initial volume level of the note, with
	 * 0.0f being silence and 1.0f being full-volume.
	 * @throws IllegalArgumentException if {@code __velocity} is a
	 * non-number or is negative.
	 * @see #keyOff(int, int)
	 * @see #pitchBend(int, float)
	 * @see #pitchBendRange(int, float)
	 * @since 2025/05/05
	 */
	@SquirrelJMEVendorApi
	void keyOn(int __channel, int __key, float __velocity)
		throws IllegalArgumentException;
	
	/**
	 * Specify the global pitch bend. The master tuning is multiplied by
	 * each channel's base tuning to determine the effective tuning on
	 * that
	 * channel.<br><br>
	 * The default master tuning is 0.0f.<br><br>
	 * For informaiton on the underlying mathematics, see
	 * {@link #keyOn(int, int, float)}.
	 *
	 * @param __semitones The number of semitones to detune, where 0.0f
	 * represents no adjustment. Most MIDI specifications express pitch
	 * bend as a number of cents rather than semitones.
	 * @throws IllegalArgumentException if {@code __semitones} is a
	 * non-number.
	 * @see #pitchBend(int, float)
	 * @since 2025/05/05
	 */
	@SquirrelJMEVendorApi
	void masterTune(float __semitones)
		throws IllegalArgumentException;
	
	/**
	 * Specify the global volume. The master volume is multiplied by each
	 * channel's and note's base volume to determine the effective volume
	 * on that note.<br><br>
	 * The default master volume is 1.0f.
	 *
	 * @param __volume The volume level, with 0.0f being silence and 1.0f
	 * being full-volume.
	 * @throws IllegalArgumentException if {@code __volume} is a
	 * non-number or is negative.
	 * @since 2025/05/05
	 */
	@SquirrelJMEVendorApi
	void masterVolume(float __volume)
		throws IllegalArgumentException;
	
	/**
	 * Specify stereo panning on a channel. Panning ranges from -1.0f for
	 * full left channel, to 0.0f for centered, to +1.0f for full right
	 * channel.<br><br>
	 * The default panning on all channels is 0.0f.
	 *
	 * @param __channel The index of the channel to configure.
	 * @param __panpot The panning level.
	 * @throws IllegalArgumentException if {@code __panpot} is a
	 * non-number, is less than -1.0f or is greater than +1.0f.
	 * @since 2025/05/05
	 */
	@SquirrelJMEVendorApi
	void panpot(int __channel, float __panpot)
		throws IllegalArgumentException;
	
	/**
	 * Specify a channel's pitch bend. The master tuning is multiplied by
	 * each channel's base tuning to determine the effective tuning on
	 * that
	 * channel. The number of semitones is multiplied by the channel's
	 * current pitch bend range to calculate the effective tuning.<br><br>
	 * The default pitch bend on all channels is 0.0f.<br><br>
	 * For informaiton on the underlying mathematics, see
	 * {@link #keyOn(int, int, float)}.
	 *
	 * @param __channel The index of the channel to configure.
	 * @param __semitones The number of semitones to detune, where 0.0f
	 * represents no adjustment. Most MIDI specifications express pitch
	 * bend as a number of cents rather than semitones.
	 * @throws IllegalArgumentException if {@code __semitones} is a
	 * non-number.
	 * @see #masterTune(float)
	 * @see #pitchBendRange(int, float)
	 * @since 2025/05/05
	 */
	@SquirrelJMEVendorApi
	void pitchBend(int __channel, float __semitones)
		throws IllegalArgumentException;
	
	/**
	 * Specify the range of a channel's pitch bend. The channel's pitch
	 * bend setting in semitones is multiplied by the range amount to
	 * calculate the effective tuning.<br><br>
	 * The default pitch bend range on all channels is 2.0f.
	 *
	 * @param __channel The index of the channel to configure.
	 * @param __range The magnitude of the maximum pitch bend on the
	 * channel.
	 * @throws IllegalArgumentException if {@code __range} is a non-number
	 * or is negative.
	 * @see #pitchBend(int, float)
	 * @since 2025/05/05
	 */
	@SquirrelJMEVendorApi
	void pitchBendRange(int __channel, float __range)
		throws IllegalArgumentException;
	
	/**
	 * Speicfy a channel's program number. The program corresponds to the
	 * voice or instrument used on the channel.<br><br>
	 * The default program number on all channels is 0.
	 *
	 * @param __channel The index of the channel to configure.
	 * @param __program The program number.
	 * @see #bankChange(int, int)
	 * @since 2025/05/05
	 */
	@SquirrelJMEVendorApi
	void programChange(int __channel, int __program)
		throws IllegalArgumentException;
	
	/**
	 * Generate output samples. This method is equivalent to
	 * {@code render(__samples, __offset, __frames, 1.0f, 1.0f, true, true)}.
	 * <br><br>
	 * For information regarding the operations of this method, see
	 * {@link #render(float[], int, int, float, float, boolean, boolean)}.
	 *
	 * @param __samples Output sample buffer.
	 * @param __offset Index in {@code __samples} of the first audio frame to
	 * output.
	 * @param __frames The number of audio frames to output.
	 * @throws NullPointerException if {@code __samples} is {@code null}.
	 * @throws ArrayIndexOutOfBoundsException if {@code __offset} is
	 * negative, or if {@code __offset + __frames * 2 > __samples.length}.
	 * @throws IllegalArgumentException if {@code __frames} is negative.
	 * @see #render(float[], int, int, float, float, boolean, boolean)
	 * @since 2025/05/05
	 */
	@SquirrelJMEVendorApi
	void render(@NotNull float[] __samples,
		@Range(from = 0, to = Integer.MAX_VALUE) int __offset,
		@Range(from = 0, to = Integer.MAX_VALUE) int __frames)
		throws ArrayIndexOutOfBoundsException, NullPointerException,
		IllegalArgumentException;
	
	/**
	 * Generate output samples. This method is equivalent to
	 * {@code render(__samples, __offset, __frames, __amplitude, __amplitude,
	 * true, true)}.<br><br>
	 * For information regarding the operations of this method, see
	 * {@link #render(float[], int, int, float, float, boolean, boolean)}.
	 *
	 * @param __samples Output sample buffer.
	 * @param __offset Index in {@code __samples} of the first audio frame to
	 * output.
	 * @param __frames The number of audio frames to output.
	 * @param __amplitude A multiplier that is applied to all samples
	 * generated.
	 * @throws NullPointerException if {@code __samples} is {@code null}.
	 * @throws ArrayIndexOutOfBoundsException if {@code __offset} is
	 * negative, or if {@code __offset + __frames * 2 > __samples.length}.
	 * @throws IllegalArgumentException if {@code frames} is negative,
	 * or if {@code __amplitude} is a non-number or is negative.
	 * @see #render(float[], int, int, float, float, boolean, boolean)
	 * @since 2025/05/05
	 */
	@SquirrelJMEVendorApi
	void render(@NotNull float[] __samples,
		@Range(from = 0, to = Integer.MAX_VALUE) int __offset,
		@Range(from = 0, to = Integer.MAX_VALUE) int __frames,
		float __amplitude)
		throws ArrayIndexOutOfBoundsException, NullPointerException,
		IllegalArgumentException;
	
	/**
	 * Generate output samples. This method is equivalent to
	 * {@code render(__samples, __offset, __frames, __left, __right,
	 * true, true)}.<br><br>
	 * For information regarding the operations of this method, see
	 * {@link #render(float[], int, int, float, float, boolean, boolean)}.
	 *
	 * @param __samples Output sample buffer.
	 * @param __offset Index in {@code __samples} of the first audio frame to
	 * output.
	 * @param __frames The number of audio frames to output.
	 * @param __left A multiplier that is applied to all left-stereo samples
	 * generated.
	 * @param __right A multiplier that is applied to all right-stereo
	 * samples generated.
	 * @throws NullPointerException if {@code __samples} is {@code null}.
	 * @throws ArrayIndexOutOfBoundsException if {@code __offset} is
	 * negative, or if {@code __offset + __frames * 2 > __samples.length}.
	 * @throws IllegalArgumentException if {@code frames} is negative,
	 * or if {@code __left} or {@code __right} is a non-number or is negative.
	 * @see #render(float[], int, int, float, float, boolean, boolean)
	 * @since 2025/05/05
	 */
	@SquirrelJMEVendorApi
	void render(@NotNull float[] __samples,
		@Range(from = 0, to = Integer.MAX_VALUE) int __offset,
		@Range(from = 0, to = Integer.MAX_VALUE) int __frames,
		float __left,
		float __right)
		throws ArrayIndexOutOfBoundsException, NullPointerException,
		IllegalArgumentException;
	
	/**
	 * Generate output samples. Sample values range from -1.0f mininum to
	 * +1.0f maximum. Sample buffers are interleaved stereo: even-numbered
	 * indexes are for the left channel and odd-numbered indexes are for
	 * the right channel. One "frame" of audio represents the samples for
	 * both stereo channels, and so occupies two consecutive elements in
	 * the buffer.<br><br>
	 * All generated samples are multiplied by {@code __left} and
	 * {@code __right}.<br><br>
	 * When {@code __erase} is {@code false}, this method will add to the
	 * current contents of the sample buffer in order to interoperate with
	 * other audio sources. When {@code __erase} is {@code true}, the
	 * contents of the sample buffer are replaced with the rendering
	 * output.<br><br>
	 * When {@code __clamp} is {@code false}, the contents of the sample
	 * buffer may not be in the range of -1.0f to +1.0f once this method
	 * returns. When {@code __clamp} is {@code true}, the sample buffer is
	 * limited to the range of -1.0f to +1.0f inclusive upon this method
	 * returning.
	 *
	 * @param __samples Output sample buffer.
	 * @param __offset Index in {@code __samples} of the first audio frame to
	 * output.
	 * @param __frames The number of audio frames to output.
	 * @param __left A multiplier that is applied to all left-stereo samples
	 * generated.
	 * @param __right A multiplier that is applied to all right-stereo
	 * samples generated.
	 * @param __erase Replace the buffer contents when {@code true}, or add
	 * to them when {@code false}
	 * @param __clamp Specifies whether to restrict the sample buffer values
	 * to -1.0f to +1.0f inclusive.
	 * @throws NullPointerException if {@code __samples} is {@code null}.
	 * @throws ArrayIndexOutOfBoundsException if {@code __offset} is
	 * negative, or if {@code __offset + __frames * 2 > __samples.length}.
	 * @throws IllegalArgumentException if {@code __frames} is negative,
	 * or if {@code __left} or {@code __right} is a non-number or is negative.
	 * @see #render(float[], int, int)
	 * @see #render(float[], int, int, float)
	 * @see #render(float[], int, int, float, float)
	 * @since 2025/05/05
	 */
	@SquirrelJMEVendorApi
	void render(@NotNull float[] __samples,
		@Range(from = 0, to = Integer.MAX_VALUE) int __offset,
		@Range(from = 0, to = Integer.MAX_VALUE) int __frames,
		float __left,
		float __right,
		boolean __erase, boolean __clamp)
		throws ArrayIndexOutOfBoundsException, NullPointerException,
		IllegalArgumentException;
	
	/**
	 * Initialize all output state. All currently active notes are
	 * stopped,
	 * and all master, channel and sampler-specific configurations are
	 * reinitialized to their default values.
	 * @since 2025/05/05
	 */
	@SquirrelJMEVendorApi
	void reset();
	
	/**
	 * Returns the sample rate.
	 *
	 * @return The sample rate.
	 * @since 2025/05/05
	 */
	@SquirrelJMEVendorApi
	float sampleRate();
	
	/**
	 * Terminate all active notes. This may be needed depending on a
	 * sequencer's looping logic. This method immediately cancels all notes
	 * without going through key-off processing.
	 * @since 2025/05/05
	 */
	@SquirrelJMEVendorApi
	void stopAll();
	
	/**
	 * Process a SysEx message. Functionality that is not part of the
	 * relevant specificaiton (usually MIDI) is configured through such
	 * messages. The binary format of a SysEx message depends on the
	 * vendor and the feature being configured.
	 *
	 * @param __message The body data of the vendor-exclusive message.
	 * @throws NullPointerException If {@code __message} is {@code null};
	 * @since 2025/05/05
	 */
	@SquirrelJMEVendorApi
	void sysEx(@NotNull byte[] __message)
		throws NullPointerException;
	
	/**
	 * Specify a channel's volume. The master volume is multiplied by each
	 * channel's and note's base volume to determine the effective volume
	 * on that note.<br><br>
	 * The default volume on all channels is 1.0f.
	 *
	 * @param __channel The index of the channel to configure.
	 * @param __volume The volume level, with 0.0f being silence and 1.0f
	 * being full-volume.
	 * @throws IllegalArgumentException if {@code __volume} is a
	 * non-number or is negative.
	 * @see #masterVolume(float)
	 * @since 2025/05/05
	 */
	@SquirrelJMEVendorApi
	void volume(int __channel, float __volume)
		throws IllegalArgumentException;
}
