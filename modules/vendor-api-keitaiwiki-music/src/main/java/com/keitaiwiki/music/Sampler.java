package com.keitaiwiki.music;

/**
 * Sample generator for music sequencer players. Invoking {@code instance()}
 * will produce an object that can be used to render the actual samples of
 * audio.
 */
public interface Sampler {

    /////////////////////////////// Interfaces ////////////////////////////////

    /**
     * Sample-generating instance of a {@code Sampler}. Configurations on the
     * sampler object can propagate down to its instances.<br><br>
     * Any unsupported features specified by the methods of this class (such as
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
     * @see Sampler
     */
    public interface Instance {

        /**
         * Specify a channel's program bank. The bank expands the total number
         * of programs available to a channel.<br><br>
         * The default program bank number on all channels is 0.
         * @param channel The index of the channel to configure.
         * @param bank The bank number.
         * @see programChange(int,int)
         */
        void bankChange(int channel, int bank);

        /**
         * Specify whether a channel should play drum notes. <br><br>
         * The default drums-enabled setting on all channels is {@code false}.
         * @param channel The index of the channel to configure.
         * @param enable Whether to enable drum notes on the channel.
         */
        void drumEnable(int channel, boolean enable);

        /**
         * Determine whether or not any notes are producing output.
         * @return {@code true} if there are no notes generating any output.
         */
        boolean isFinished();

        /**
         * Deactivate a key that has previoulsy been activated on a channel.
         * If no key is currently active on the channel, no action is taken.
         * @param channel The index of the channel to configure.
         * @param key The number of the key to configure. A value of zero
         * corresponds to the note A<sub>4</sub>.
         * @see keyOn(int,int,float)
         */
        void keyOff(int channel, int key);

        /**
         * Activate a key on a channel. If no key is currently active on the
         * channel, begins a new note. If a key is already active on the
         * channel, this method may change its frequency and volume without
         * restarting it, although this behavior is not guaranteed.<br><br>
         * Frequencies are given by the following formula:<br><br>
         * <code>&nbsp; &nbsp; &nbsp; frequencyHz = 440 * 2<sup
         * >((key + pitchBend) / 12)</sup></code><br><br>
         * 440 Hz is the frequency of the note A<sub>4</sub>, known as "concert
         * A" (the A above middle C), and is the reference frequency in most
         * modern instrument tuning. For regular instruments, {@code key}
         * specifies the number of semitones relative to A<sub>4</sub>.
         * Although the number doesn't have to be an integer for the purposes
         * of the formula, it <i>does</i> have to be an integer for other
         * contexts such as drum notes, which use the key number to determine
         * the exact drum sound to play.
         * @param channel The index of the channel to configure.
         * @param key The number of the key to configure. A value of zero
         * corresponds to the note A<sub>4</sub>.
         * @param velocity The overall initial volume level of the note, with
         * 0.0f being silence and 1.0f being full-volume.
         * @exception IllegalArgumentException if {@code velocity} is a
         * non-number or is negative.
         * @see keyOff(int,int)
         * @see pitchBend(int,float)
         * @see pitchBendRange(int,float)
         */
        void keyOn(int channel, int key, float velocity);

        /**
         * Specify the global pitch bend. The master tuning is multiplied by
         * each channel's base tuning to determine the effective tuning on that
         * channel.<br><br>
         * The default master tuning is 0.0f.<br><br>
         * For informaiton on the underlying mathematics, see
         * {@link keyOn(int,int,int)}.
         * @param semitones The number of semitones to detune, where 0.0f
         * represents no adjustment. Most MIDI specifications express pitch
         * bend as a number of cents rather than semitones.
         * @exception IllegalArgumentException if {@code semitones} is a
         * non-number.
         * @see pitchBend(int,float)
         */
        void masterTune(float semitones);

        /**
         * Specify the global volume. The master volume is multiplied by each
         * channel's and note's base volume to determine the effective volume
         * on that note.<br><br>
         * The default master volume is 1.0f.
         * @param volume The volume level, with 0.0f being silence and 1.0f
         * being full-volume.
         * @exception IllegalArgumentException if {@code volume} is a
         * non-number or is negative.
         */
        void masterVolume(float volume);

        /**
         * Specify stereo panning on a channel. Panning ranges from -1.0f for
         * full left channel, to 0.0f for centered, to +1.0f for full right
         * channel.<br><br>
         * The default panning on all channels is 0.0f.
         * @param channel The index of the channel to configure.
         * @param panpot The panning level.
         * @exception IllegalArgumentException if {@code panpot} is a
         * non-number, is less than -1.0f or is greater than +1.0f.
         */
        void panpot(int channel, float panpot);

        /**
         * Specify a channel's pitch bend. The master tuning is multiplied by
         * each channel's base tuning to determine the effective tuning on that
         * channel. The number of semitones is multiplied by the channel's
         * current pitch bend range to calculate the effective tuning.<br><br>
         * The default pitch bend on all channels is 0.0f.<br><br>
         * For informaiton on the underlying mathematics, see
         * {@link keyOn(int,int,int)}.
         * @param channel The index of the channel to configure.
         * @param semitones The number of semitones to detune, where 0.0f
         * represents no adjustment. Most MIDI specifications express pitch
         * bend as a number of cents rather than semitones.
         * @exception IllegalArgumentException if {@code semitones} is a
         * non-number.
         * @see masterTune(float)
         * @see pitchBendRange(int,float)
         */
        void pitchBend(int channel, float semitones);

        /**
         * Specify the range of a channel's pitch bend. The channel's pitch
         * bend setting in semitones is multiplied by the range amount to
         * calculate the effective tuning.<br><br>
         * The default pitch bend range on all channels is 2.0f.
         * @param channel The index of the channel to configure.
         * @param range The magnitude of the maximum pitch bend on the channel.
         * @exception IllegalArgumentException if {@code range} is a non-number
         * or is negative.
         * @see pitchBend(int,float)
         */
        void pitchBendRange(int channel, float range);

        /**
         * Speicfy a channel's program number. The program corresponds to the
         * voice or instrument used on the channel.<br><br>
         * The default program number on all channels is 0.
         * @param channel The index of the channel to configure.
         * @param program The program number.
         * @see bankChange(int,int)
         */
        void programChange(int channel, int program);

        /**
         * Generate output samples. This method is equivalent to
         * {@code render(samples, offset, frames, 1.0f, 1.0f, true, true)}.
         * <br><br>
         * For information regarding the operations of this method, see
         * {@link render(float[],int,int,float,float,boolean,boolean)}.
         * @param samples Output sample buffer.
         * @param offset Index in {@code samples} of the first audio frame to
         * output.
         * @param frames The number of audio frames to output.
         * @exception NullPointerException if {@code samples} is {@code null}.
         * @exception ArrayIndexOutOfBoundsException if {@code offset} is
         * negative, or if {@code offset + frames * 2 > samples.length}.
         * @exception IllegalArgumentException if {@code frames} is negative.
         * @see render(float[],int,int,float,float,boolean,boolean)
         */
        void render(float[] samples, int offset, int frames);

        /**
         * Generate output samples. This method is equivalent to
         * {@code render(samples, offset, frames, amplitude, amplitude,
         * true, true)}.<br><br>
         * For information regarding the operations of this method, see
         * {@link render(float[],int,int,float,float,boolean,boolean)}.
         * @param samples Output sample buffer.
         * @param offset Index in {@code samples} of the first audio frame to
         * output.
         * @param frames The number of audio frames to output.
         * @param amplitude A multiplier that is applied to all samples
         * generated.
         * @exception NullPointerException if {@code samples} is {@code null}.
         * @exception ArrayIndexOutOfBoundsException if {@code offset} is
         * negative, or if {@code offset + frames * 2 > samples.length}.
         * @exception IllegalArgumentException if {@code frames} is negative,
         * or if {@code amplitude} is a non-number or is negative.
         * @see render(float[],int,int,float,float,boolean,boolean)
         */
        void render(float[] samples, int offset, int frames, float amplitude);

        /**
         * Generate output samples. This method is equivalent to
         * {@code render(samples, offset, frames, left, right, true, true)}.
         * <br><br>
         * For information regarding the operations of this method, see
         * {@link render(float[],int,int,float,float,boolean,boolean)}.
         * @param samples Output sample buffer.
         * @param offset Index in {@code samples} of the first audio frame to
         * output.
         * @param frames The number of audio frames to output.
         * @param left A multiplier that is applied to all left-stereo samples
         * generated.
         * @param right A multiplier that is applied to all right-stereo
         * samples generated.
         * @exception NullPointerException if {@code samples} is {@code null}.
         * @exception ArrayIndexOutOfBoundsException if {@code offset} is
         * negative, or if {@code offset + frames * 2 > samples.length}.
         * @exception IllegalArgumentException if {@code frames} is negative,
         * or if {@code left} or {@code right} is a non-number or is negative.
         * @see render(float[],int,int,float,float,boolean,boolean)
         */
        void render(float[] samples, int offset, int frames,
            float left, float right);

        /**
         * Generate output samples. Sample values range from -1.0f mininum to
         * +1.0f maximum. Sample buffers are interleaved stereo: even-numbered
         * indexes are for the left channel and odd-numbered indexes are for
         * the right channel. One "frame" of audio represents the samples for
         * both stereo channels, and so occupies two consecutive elements in
         * the buffer.<br><br>
         * All generated samples are multiplied by {@code left} and
         * {@code right}.<br><br>
         * When {@code erase} is {@code false}, this method will add to the
         * current contents of the sample buffer in order to interoperate with
         * other audio sources. When {@code erase} is {@code true}, the
         * contents of the sample buffer are replaced with the rendering
         * output.<br><br>
         * When {@code clamp} is {@code false}, the contents of the sample
         * buffer may not be in the range of -1.0f to +1.0f once this method
         * returns. When {@code clamp} is {@code true}, the sample buffer is
         * limited to the range of -1.0f to +1.0f inclusive upon this method
         * returning.
         * @param samples Output sample buffer.
         * @param offset Index in {@code samples} of the first audio frame to
         * output.
         * @param frames The number of audio frames to output.
         * @param left A multiplier that is applied to all left-stereo samples
         * generated.
         * @param right A multiplier that is applied to all right-stereo
         * samples generated.
         * @param erase Replace the buffer contents when {@code true}, or add
         * to them when {@code false}
         * @param clamp Specifies whether to restrict the sample buffer values
         * to -1.0f to +1.0f inclusive.
         * @exception NullPointerException if {@code samples} is {@code null}.
         * @exception ArrayIndexOutOfBoundsException if {@code offset} is
         * negative, or if {@code offset + frames * 2 > samples.length}.
         * @exception IllegalArgumentException if {@code frames} is negative,
         * or if {@code left} or {@code right} is a non-number or is negative.
         * @see render(float[],int,int)
         * @see render(float[],int,int,float)
         * @see render(float[],int,int,float,float)
         */
        void render(float[] samples, int offset, int frames,
            float left, float right, boolean erase, boolean clamp);

        /**
         * Initialize all output state. All currently active notes are stopped,
         * and all master, channel and sampler-specific configurations are
         * reinitialized to their default values.
         */
        void reset();

        /**
         * Terminate all active notes. This may be needed depending on a
         * sequencer's looping logic. This method immediately cancels all notes
         * without going through key-off processing.
         */
        void stopAll();

        /**
         * Process a SysEx message. Functionality that is not part of the
         * relevant specificaiton (usually MIDI) is configured through such
         * messages. The binary format of a SysEx message depends on the
         * vendor and the feature being configured.
         * @param message The body data of the vendor-exclusive message.
         */
        void sysEx(byte[] message);

        /**
         * Specify a channel's volume. The master volume is multiplied by each
         * channel's and note's base volume to determine the effective volume
         * on that note.<br><br>
         * The default volume on all channels is 1.0f.
         * @param channel The index of the channel to configure.
         * @param volume The volume level, with 0.0f being silence and 1.0f
         * being full-volume.
         * @see masterVolume(float)
         */
        void volume(int channel, float volume);
    }



    ///////////////////////////////// Methods /////////////////////////////////

    /**
     * Produces an instance of this sampler that can be used to render samples.
     * @param sampleRate The output sampling rate of the rendered samples.
     * @return A new sampler instance that can render samples using the current
     * configuration of this sampler itself.
     * @exception IllegalArgumentException if {@code sampleRate} is a
     * non-number or is less than or equal to zero.
     */
    Instance instance(float sampleRate);

}
