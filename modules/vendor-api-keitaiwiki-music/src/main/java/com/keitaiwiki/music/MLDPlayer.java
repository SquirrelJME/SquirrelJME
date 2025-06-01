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

import java.util.*;

/**
 * i-melody MLD sequence player. Uses a {@code Sampler} to generate output to a
 * sample buffer.
 * @see MLD
 * @see Sampler
 */
public class MLDPlayer {

    // Instance fields
    private Channel[]        channels;      // Playback channels
    private ArrayList<Event> events;        // Pending events
    private HashSet<Integer> evtKeys;       // Key events enabled by key
    private boolean          evtPlayback;   // Playback events are enabled
    private boolean          finished;      // Sequencer has no more events
    private float            framesPerTick; // Output frames in one tick
    private boolean          loopEnabled;   // Looping is enabled
    private boolean          loopStopAll;   // Stop all notes when looping
    private MLD              mld;           // Sequence resource
    private float            pendingFrames; // Output frames to process
    private int              pendingTicks;  // Sequencer ticks to process
    private long             position;      // Sequencer position in frames
    public Sampler.Instance sampler;       // Sample generator
    private float            sampleRate;    // Output sampling rate
    private boolean          seeking;       // Processing setTime()
    private long             tickNow;       // Sequencer position in ticks
    private Track[]          tracks;        // Sequencer state



    //////////////////////////////// Constants ////////////////////////////////

    /**
     * Event type that notifies when a non-looping sequence finishes.
     * @see Event
     */
    public static final int EVENT_END = 0;

    /**
     * Event type that notifies when a sequence loops.
     * @see Event
     */
    public static final int EVENT_LOOP = 1;

    /**
     * Event type that notifies when a particular key is played.
     * @see Event
     */
    public static final int EVENT_KEY = 2;



    ////////////////////////////////// Event //////////////////////////////////

    /**
     * Notifies of a scenario that arises during playback. When configured, the
     * {@code render()} methods will terminate early any time an event
     * condition is satisfied. Events are obtained by the caller and
     * acknowledged via {@link #getEvents()}.
     * @see #getEvents()
     */
    public class Event {

        /**
         * Additional event data, if relevant. For {@code EVENT_KEY} events,
         * this will be the key number.
         */
        public int data;

        /**
         * Time in seconds since the beginning of playback when the event was
         * raised.
         */
        public double time;

        /**
         * Indicates the type of event that was raised: {@code EVENT_END},
         * {@code EVENT_KEY} or {@code EVENT_LOOP}.
         */
        public int type;

        // Internal constructor
        private Event(double time, int type, int data) {
            this.data = data;
            this.time = time;
            this.type = type;
        }

    }



    //////////////////////////// Private Constants ////////////////////////////

    private static final int A4 = 48; // Key index bias



    ///////////////////////////////// Classes /////////////////////////////////

    // Playback channel
    private class Channel {
        Note[]          notesOn;  // All notes currently on keys
        ArrayList<Note> notesOut; // All notes that are generating output
    }

    // Music note
    private class Note {
        int channel;  // Output channel
        int gateTime; // Ticks before note expires
        int key;      // Key index
    }

    // Event list state
    private class Track {
        int       cuepoint; // Starting cuepoint
        boolean   finished; // Track has no more events
        int       index;    // Index within sequencer
        MLD.Track mld;      // Event list
        int       offset;   // Current event offset
        int       ticks;    // Event ticks until next event
    }



    ////////////////////////////// Constructors ///////////////////////////////

    /**
     * Begin MLD playback. Instances of a {@code Sampler} are used in
     * conjunction with the given sampling rate to render the sequence to a
     * sample buffer.
     * @param mld The MLD sequence to play.
     * @param sampler A {@code Sampler} from which instances will be taken to
     * generate output.
     * @param sampleRate The samples per second of the output.
     * @exception NullPointerException if {@code mld} or {@code sampler} is
     * {@code null}.
     * @exception IllegalArgumentException if {@code sampleRate} is a
     * non-number or is less than or equal to zero.
     * @see MLD
     * @see Sampler
     */
    public MLDPlayer(MLD mld, Sampler sampler, float sampleRate) {

        // Error checking
        if (mld == null)
            throw new NullPointerException("An MLD is required.");
        if (sampler == null)
            throw new NullPointerException("A sampler is required.");
        if (!__Bridge__.floatIsFinite(sampleRate) || sampleRate <= 0.0f)
            throw new IllegalArgumentException("Invalid sampling rate.");

        // Instance fields
        channels        = new Channel[16];
        events          = new ArrayList<Event>();
        evtKeys         = new HashSet<Integer>();
        evtPlayback     = false;
        loopEnabled     = true;
        loopStopAll     = true;
        this.mld        = mld;
        this.sampler    = sampler.instance(sampleRate);
        this.sampleRate = sampleRate;
        seeking         = false;
        tracks          = new Track[mld.tracks.length];

        // Channels
        for (int x = 0; x < channels.length; x++) {
            Channel chan  = channels[x] = new Channel();
            chan.notesOn  = new Note[99]; // A0 .. C6
            chan.notesOut = new ArrayList<Note>();
        }

        // Tracks
        for (int x = 0; x < tracks.length; x++) {
            Track track = tracks[x] = new Track();
            track.index = x;
            track.mld   = mld.tracks[x];
        }

        // Prepare for playback
        reset();
    }



    ///////////////////////////// Public Methods //////////////////////////////

    /**
     * Registers a key to raise events for during rendering. Key number 0 is
     * the note A<sub>4</sub>.
     * @param key A key number to register.
     * @see Event
     * @see #getEvents()
     */
    public void addEventKey(int key) {
        evtKeys.add(key);
    }

    /**
     * Registers multiple keys to raise events for during rendering. Key number
     * 0 is the note A<sub>4</sub>.
     * @param keys A list of key numbers to register.
     * @exception NullPointerException if {@code keys} is {@code null}.
     * @see Event
     * @see #getEvents()
     */
    public void addEventKeys(int[] keys) {
        if (keys == null)
            throw new NullPointerException("Key array is required.");
        for (int key : keys)
            evtKeys.add(key);
    }

    /**
     * Determine the total length of the sequence in seconds. Equivalent to
     * invoking {@code getDuration(withoutLoops)} on the underlying {@code MLD}
     * object.
     * @param withoutLooping Whether or not to consider looping in the return
     * value.
     * @return If the sequence does not loop, the number of seconds in the
     * sequence. If the sequence loops and {@code withoutLooping} is
     * {@code false}, returns {@code Double.POSITIVE_INFINITY}. If the sequence
     * loops and {@code withoutLooping} is {@code true}, returns the number of
     * seconds in the sequence up until the first loop occurs.
     * @see MLD#getDuration(boolean)
     */
    public double getDuration(boolean withoutLooping) {
        return mld.getDuration(withoutLooping);
    }

    /**
     * Retrieve and acknowledge all pending events. If this method is not
     * called, events will remain in the queue and prevent samples from being
     * rendered.
     * @return An array of all pending events, now acknowledged.
     * @see Event
     * @see #addEventKey(int)
     * @see #addEventKeys(int[])
     * @see #setPlaybackEventsEnabled(boolean)
     */
    public Event[] getEvents() {
        Event[] ret = events.toArray(new Event[events.size()]);
        events.clear();
        return ret;
    }

    /**
     * Determine whether looping is enabled.
     * @return {@code true} if looping is enabled.
     * @see #setLoopEnabled(boolean)
     */
    public boolean getLoopEnabled() {
        return loopEnabled;
    }

    /**
     * Determine whether notes are stopped when looping.
     * @return {@code true} if all notes are stopped when looping.
     * @see #setLoopStopAll(boolean)
     */
    public boolean getLoopStopAll() {
        return loopStopAll;
    }

    /**
     * Retrieve the current playback position in the sequence. The range of
     * values represents the start of the sequence at 0.0 and either the end of
     * the sequence or the point where looping occurs at 1.0.
     * @return The proportion of the total sequence for the current playback
     * position.
     */
    public double getPosition() {
        return (double) tickNow / mld.tickEnd;
    }

    /**
     * Retrieve the total number of seconds played back so far.
     * @return The number of seconds processed, relative to the start of the
     * sequence.
     * @see #setTime(double)
     * @see MLD#getDuration(boolean)
     */
    public double getTime() {
        return (double) position / sampleRate;
    }

    /**
     * Determine whether playback has completed. The sequence is considered
     * finished when all of its events have been processed and the last note
     * has stopped generating samples.
     * @return {@code true} if all playback has completed.
     */
    public boolean isFinished() {
        if (!sampler.isFinished())
            return false;
        for (Track track : tracks) {
            if (!track.finished)
                return false;
        }
        return true;
    }

    /**
     * Unregisters a keys from raising events during rendering.
     * @param key A key number to unregister.
     * @see Event
     * @see #getEvents()
     */
    public void removeEventKey(int key) {
        evtKeys.remove(key);
    }

    /**
     * Unregisters multiple keys from raising events during rendering.
     * @param keys A list of key numbers to unregister.
     * @exception NullPointerException if {@code keys} is {@code null}.
     * @see Event
     * @see #getEvents()
     */
    public void removeEventKeys(int[] keys) {
        if (keys == null)
            throw new NullPointerException("Key array is required.");
        for (int key : keys)
            evtKeys.remove(key);
    }

    /**
     * Generate output samples. This method is equivalent to
     * {@code render(samples, offset, frames, 1.0f, 1.0f, true, true)}.<br><br>
     * For information regarding the operations of this method, see
     * {@link Sampler.Instance#render(float[],int,int,float,float,boolean,boolean)}.
     * @param samples Output sample buffer.
     * @param offset Index in {@code samples} of the first audio frame to
     * output.
     * @param frames The number of audio frames to output.
     * @exception NullPointerException if {@code samples} is {@code null}.
     * @exception ArrayIndexOutOfBoundsException if {@code offset} is
     * negative, or if {@code offset + frames * 2 > samples.length}.
     * @exception IllegalArgumentException if {@code frames} is negative.
     * @return The number of samples generated, or -1 if playback has finished.
     * May be less than {@code frames} if playback of the underlying sequence
     * completes before all frames have been processed.
     * @see #render(float[],int,int,float,float,boolean,boolean)
     * @see Sampler.Instance#render(float[],int,int,float,float,boolean,boolean)
     */
    public int render(float[] samples, int offset, int frames) {
        return render(samples, offset, frames, 1.0f, 1.0f, true, true);
    }

    /**
     * Generate output samples. This method is equivalent to
     * {@code render(samples, offset, frames, amplitude, amplitude,
     * true, true)}.<br><br>
     * For information regarding the operations of this method, see
     * {@link Sampler.Instance#render(float[],int,int,float,float,boolean,boolean)}.
     * @param samples Output sample buffer.
     * @param offset Index in {@code samples} of the first audio frame to
     * output.
     * @param frames The number of audio frames to output.
     * @param amplitude A multiplier that is applied to all samples
     * generated.
     * @return The number of samples generated, or -1 if playback has finished.
     * May be less than {@code frames} if playback of the underlying sequence
     * completes before all frames have been processed.
     * @exception NullPointerException if {@code samples} is {@code null}.
     * @exception ArrayIndexOutOfBoundsException if {@code offset} is
     * negative, or if {@code offset + frames * 2 > samples.length}.
     * @exception IllegalArgumentException if {@code frames} is negative, or if
     * {@code amplitude} is a non-number or is negative.
     * @see #render(float[],int,int,float,float,boolean,boolean)
     * @see Sampler.Instance#render(float[],int,int,float,float,boolean,boolean)
     */
    public int render(float[] samples,int offset,int frames,float amplitude) {
        return render(samples,offset,frames,amplitude,amplitude,true,true);
    }

    /**
     * Generate output samples. This method is equivalent to
     * {@code render(samples, offset, frames, left, right, true, true)}.
     * <br><br>
     * For information regarding the operations of this method, see
     * {@link Sampler.Instance#render(float[],int,int,float,float,boolean,boolean)}.
     * @param samples Output sample buffer.
     * @param offset Index in {@code samples} of the first audio frame to
     * output.
     * @param frames The number of audio frames to output.
     * @param left A multiplier that is applied to all left-stereo samples
     * generated.
     * @param right A multiplier that is applied to all right-stereo samples
     * generated.
     * @return The number of samples generated, or -1 if playback has finished.
     * May be less than {@code frames} if playback of the underlying sequence
     * completes before all frames have been processed.
     * @exception NullPointerException if {@code samples} is {@code null}.
     * @exception ArrayIndexOutOfBoundsException if {@code offset} is
     * negative, or if {@code offset + frames * 2 > samples.length}.
     * @exception IllegalArgumentException if {@code frames} is negative, or if
     * {@code left} or {@code right} is a non-number or is negative.
     * @see #render(float[],int,int,float,float,boolean,boolean)
     * @see Sampler.Instance#render(float[],int,int,float,float,boolean,boolean)
     */
    public int render(float[] samples, int offset, int frames,
        float left, float right) {
        return render(samples, offset, frames, left, right, true, true);
    }

    /**
     * Generate output samples. <br><br>
     * For information regarding the operations of this method, see
     * {@link Sampler.Instance#render(float[],int,int,float,float,boolean,boolean)}.
     * <br><br>
     * If an event is raised during playback, rendering will stop and return
     * before generating any more samples. When this happens, the return value
     * may be less than {@code frames}. {@link #getEvents()} should be called
     * after every call to {@code render()} while events are enabled.
     * @param samples Output sample buffer.
     * @param offset Index in {@code samples} of the first audio frame to
     * output.
     * @param frames The number of audio frames to output.
     * @param left A multiplier that is applied to all left-stereo samples
     * generated.
     * @param right A multiplier that is applied to all right-stereo samples
     * generated.
     * @param erase Replace the buffer contents when {@code true}, or add
     * to them when {@code false}
     * @param clamp Specifies whether to restrict the sample buffer values
     * to -1.0f to +1.0f inclusive.
     * @return The number of samples generated, or -1 if playback has finished.
     * May be less than {@code frames} if playback of the underlying sequence
     * completes before all frames have been processed.
     * @exception NullPointerException if {@code samples} is {@code null}.
     * @exception ArrayIndexOutOfBoundsException if {@code offset} is
     * negative, or if {@code offset + frames * 2 > samples.length}.
     * @exception IllegalArgumentException if {@code frames} is negative, or if
     * {@code left} or {@code right} is a non-number or is negative.
     * @see Sampler.Instance#render(float[],int,int,float,float,boolean,boolean)
     * @see #getEvents()
     * @see #render(float[],int,int)
     * @see #render(float[],int,int,float)
     * @see #render(float[],int,int,float,float)
     */
    public int render(float[] samples, int offset, int frames,
        float left, float right, boolean erase, boolean clamp) {
        int ret = 0; // Total frames output so far

        // Error checking
        if (!seeking) {
            if (samples == null)
                throw new NullPointerException("A sample buffer is required.");
            if (frames < 0)
                throw new IllegalArgumentException("Invalid frames.");
            if (offset < 0 || offset + frames * 2 > samples.length) {
                throw new ArrayIndexOutOfBoundsException(
                    "Invalid range in sample buffer.");
            }
            if (!__Bridge__.floatIsFinite(left ) || left  < 0.0f)
                throw new IllegalArgumentException("Invalid left amplitude.");
            if (!__Bridge__.floatIsFinite(right) || right < 0.0f)
                throw new IllegalArgumentException("Invalid right amplitude.");
        }

        // Sequencer is not playing
        if (finished)
            pendingFrames = frames;

        // Process all output frames
        while (frames > 0) {

            // Events are pending
            if (events.size() != 0)
                return ret;

            // Process output frames
            while (pendingFrames > 0) {

                // Render the samples
                int f = Math.min(frames, (int) Math.floor(pendingFrames));
                if (!seeking)
                    sampler.render(samples,offset,f,left,right,erase,clamp);

                // State management
                frames        -= f;
                offset        += f * 2;
                pendingFrames -= f;
                position      += f;
                ret           += f;

                // All output frames have been processed
                if (frames == 0)
                    return finished ? -1 : ret;
            }

            // Process event ticks
            if (pendingTicks > 0) {

                // Sequencer
                tickNow += pendingTicks;

                // Notes
                for (Channel chan : channels)
                for (Note note : chan.notesOut)
                    note.gateTime -= pendingTicks;

                // Tracks
                for (Track track : tracks)
                    process(track, pendingTicks);

                // Remove expired notes
                for (Channel chan : channels)
                for (int x = 0; x < chan.notesOut.size(); x++) {
                    Note note = chan.notesOut.get(x);
                    if (note.gateTime != 0)
                        continue;
                    sampler.keyOff(note.channel, note.key);
                    chan.notesOut.remove(x--);
                    chan.notesOn[A4 + note.key] = null;
                }

            }

            // Determine how many ticks and frames can be processed next
            int untilTrack = untilTrack();
            if (untilTrack == -1) {
                finished = true;
                return ret;
            }
            int untilNote  = untilNote();
            pendingTicks   = untilNote == -1 ?
                untilTrack : Math.min(untilTrack, untilNote);
            pendingFrames += (float) Math.floor(pendingTicks * framesPerTick);
        }

        return ret;
    }

    /**
     * Initialize state in preparation for playback. All notes are stopped and
     * all sequencer state is reset to the beginning of the sequence.
     */
    public void reset() {

        // Instance fields
        pendingFrames = 0;
        pendingTicks  = 0;
        position      = 0;
        tickNow       = 0;
        setTempo(48, 125);
        events.clear();

        // Initialize sampler
        sampler.reset();

        // Channels
        for (Channel chan : channels) {
            for (int x = 0; x < chan.notesOn.length; x++)
                chan.notesOn[x] = null;
            chan.notesOut.clear();
        }

        // Tracks
        for (Track track : tracks) {
            track.cuepoint = -1;
            track.offset   = track.mld.cue;
            track.ticks    =  0;
            track.finished = track.offset >= track.mld.size();
        }

        // Initialize playback
        finished = true;
        for (Track track : tracks) {
            process(track, 0);
            finished = finished && track.finished;
        }

    }

    /**
     * Specify whether to enable looping. When disabled, loop points defined in
     * the sequence data will not be processed.
     * @param enabled If {@code true}, looping will be enabled.
     * @return the value of {@code enabled}
     * @see #getLoopEnabled()
     */
    public boolean setLoopEnabled(boolean enabled) {
        return loopEnabled = enabled;
    }

    /**
     * Specify whether to stop all notes when looping. If notes are not
     * stopped, it is possible for adjustments to volume or pitch-bend to
     * affect ongoing notes in undesirable ways. If notes <i>are</i> stopped,
     * it is possible for ongoing notes to be truncated in undesirable ways.
     * @param stopAll If {@code true}, all notes will be stopped when looping.
     * @return the value of {@code stopAll}
     * @see #getLoopStopAll()
     */
    public boolean setLoopStopAll(boolean stopAll) {
        return loopStopAll = stopAll;
    }

    /**
     * Specify whether or not to raise playback events. Playback events include
     * {@code EVENT_END} and {@code EVENT_LOOP}.
     * @param enabled Whether or not playback events can be raised during
     * rendering.
     * @see Event
     * @see #getEvents()
     */
    public void setPlaybackEventsEnabled(boolean enabled) {
        evtPlayback = enabled;
    }

    /**
     * Specify the playback position of the sequence in seconds. The resulting
     * position in the sequence will be the earliest internal time at or after
     * {@code seconds}.<br><br>
     * If the end of the sequence is encountered during seeking, this method
     * will return {@code true}. When this happens, it is possible that the
     * position in the sequence retrieved by subsequent calls to
     * {@code getTime()} may be less than {@code seconds}.
     * @param seconds The number of seconds from the beginning of the sequence.
     * @return {@code true} if the end of the sequence was encountered during
     * the operation.
     * @exception IllegalArgumentException if {@code seconds} is a non-number
     * or is negative.
     * @see #getTime()
     * @see MLD#getDuration(boolean)
     */
    public boolean setTime(double seconds) {

        // Error checking
        if (!__Bridge__.doubleIsFinite(seconds) || seconds < 0)
            throw new IllegalArgumentException("Invalid seconds.");

        // Compute the target number of frames
        long target = (long) Math.ceil(seconds * sampleRate);

        // Already at the target
        if (target == position)
            return isFinished();

        // Target is earlier than the current frame
        if (target < position)
            reset();

        // Seek forward to the target time
        seeking = true;
        render((float[]) null, 0,
            (int) (target - position), 0.0f, 0.0f, false, false);
        seeking = false;
        return isFinished();
    }



    ///////////////////////////// Private Methods /////////////////////////////

    // Process events on a track
    private void process(Track track, int ticks) {

        // The track has finished
        if (track.finished)
            return;

        // Update state
        track.ticks -= ticks;
        if (track.ticks > 0)
            return;

        // Process all events this tick
        while (track.ticks == 0) {
            MLD.Event event = track.mld.get(track.offset);

            // Process the event
            switch (event.type) {
                case MLD.EVENT_TYPE_NOTE    : evtNote   (track, event); break;
                case MLD.EVENT_TYPE_EXT_B   : evtExtB   (track, event); break;
                case MLD.EVENT_TYPE_EXT_INFO: evtExtInfo(track, event); break;
                default: setTrackOffset(track, track.offset + 1);
            }

            // Stop processing events
            if (track.finished)
                return;

            // Schedule the next event
            track.ticks = track.mld.get(track.offset).delta;
        }

    }

    // Compute the number of output frames in one event tick
    private void setTempo(int timebase, int tempo) {
        framesPerTick = (60 * sampleRate) / (timebase * tempo);
    }

    // Specify the event offset of a track
    private void setTrackOffset(Track track, int offset) {

        // Configure the track
        track.offset   = offset;
        track.finished = offset >= track.mld.size();

        // Raise an event
        if (!track.finished || !evtPlayback)
            return;
        boolean finished = true;
        for (Track other : tracks)
            finished = finished && other.finished;
        if (finished)
            events.add(new Event(getTime(), EVENT_END, 0));
    }

    // Determine how many ticks can be processed until a note expires
    private int untilNote() {
        int ret = -1;
        for (Channel chan : channels)
        for (Note note : chan.notesOut) {
            if (ret == -1 || note.gateTime < ret)
                ret = note.gateTime;
        }
        return ret;
    }

    // Determine how many ticks can be processed until the next event
    private int untilTrack() {
        int ret = -1;
        for (Track track : tracks) {
            if (track.finished)
                continue;
            if (ret == -1 || track.ticks < ret)
                ret = track.ticks;
        }
        return ret;
    }



    ////////////////////////////// Event Methods //////////////////////////////

    // bank-change
    private void evtBankChange(Track track, MLD.Event event) {
        sampler.bankChange(event.channel, event.bank);
        setTrackOffset(track, track.offset + 1);
    }

    // cuepoint
    private void evtCuepoint(Track track, MLD.Event event) {

        // cuepoint-end
        if (event.cuepoint == MLD.CUEPOINT_END && tracks[0].cuepoint != -1) {

            // Process only if looping is enabled
            if (loopEnabled) {
                if (loopStopAll)
                    sampler.stopAll();
                for (Track t : tracks)
                    setTrackOffset(t, t.cuepoint);
                if (evtPlayback)
                    events.add(new Event(getTime(), EVENT_LOOP, 0));
            }

            // Looping is disabled
            else setTrackOffset(track, track.offset + 1);

            return;
        }

        // Common processing
        setTrackOffset(track, track.offset + 1);

        // cuepoint-start
        if (event.cuepoint == MLD.CUEPOINT_START) {
            for (Track t : tracks)
                t.cuepoint = t.offset;
        }

    }

    // drum-enable
    private void evtDrumEnable(Track track, MLD.Event event) {
        sampler.drumEnable(event.channel, event.enable);
        setTrackOffset(track, track.offset + 1);
    }

    // end-of-track
    private void evtEndOfTrack(Track track, MLD.Event event) {
        track.finished = true;
    }

    // ext-B event
    private void evtExtB(Track track, MLD.Event e) {
        switch (e.id) {
            case MLD.EVENT_BANK_CHANGE    : evtBankChange   (track, e); break;
            case MLD.EVENT_CUEPOINT       : evtCuepoint     (track, e); break;
            case MLD.EVENT_END_OF_TRACK   : evtEndOfTrack   (track, e); break;
            case MLD.EVENT_MASTER_VOLUME  : evtMasterVolume (track, e); break;
            case MLD.EVENT_MASTER_TUNE    : evtMasterTune   (track, e); break;
            case MLD.EVENT_PANPOT         : evtPanPot       (track, e); break;
            case MLD.EVENT_PITCHBEND      : evtPitchBend    (track, e); break;
            case MLD.EVENT_PITCHBEND_RANGE: evtPitchRange   (track, e); break;
            case MLD.EVENT_PROGRAM_CHANGE : evtProgramChange(track, e); break;
            case MLD.EVENT_TIMEBASE_TEMPO : evtTimebaseTempo(track, e); break;
            case MLD.EVENT_VOLUME         : evtVolume       (track, e); break;
            case MLD.EVENT_X_DRUM_ENABLE  : evtDrumEnable   (track, e); break;

            // Not implemented
            //case EVENT_JUMP:
            //case EVENT_CHANNEL_ASSIGN:
            //case EVENT_NOP:
            //case EVENT_PART_CONFIGURATION:
            //case EVENT_PAUSE:
            //case EVENT_RESET:
            //case EVENT_STOP:
            //case EVENT_WAVE_CHANNEL_VOLUME:
            //case EVENT_WAVE_CHANNEL_PANPOT:

            // Unrecognized events
            default: setTrackOffset(track, track.offset + 1);
        }
    }

    // ext-info event
    private void evtExtInfo(Track track, MLD.Event e) {
        sampler.sysEx(e.data);
        setTrackOffset(track, track.offset + 1);
    }

    // note
    private void evtNote(Track track, MLD.Event event) {
        Channel chan = channels[event.channel];
        Note    note = chan.notesOn[A4 + event.key];

        // Common processing
        setTrackOffset(track, track.offset + 1);

        // Raise an event
        if (evtKeys.contains(event.key))
            events.add(new Event(getTime(), EVENT_KEY, event.key));

        // Velocity 0 is regarded as key-off
        if (event.velocity == 0) {
            sampler.keyOff(event.channel, event.key);
            if (note != null) {
                chan.notesOn[A4 + event.key] = null;
                chan.notesOut.remove(note);
            }
            return;
        }

        // Velocity not zero is regarded as key-on
        if (!seeking)
            sampler.keyOn(event.channel, event.key, event.velocity);

        // Get or create the note for this key
        if (note == null) {
            note = new Note();
            note.channel = event.channel;
            note.key     = event.key;
            chan.notesOn[A4 + event.key] = note;
            chan.notesOut.add(note);
        }

        // Reconfigure the note
        note.gateTime = event.gateTime;
    }

    // master-volume
    private void evtMasterVolume(Track track, MLD.Event event) {
        sampler.masterVolume(event.volume);
        setTrackOffset(track, track.offset + 1);
    }

    // master-tune
    private void evtMasterTune(Track track, MLD.Event event) {
        sampler.masterTune(event.semitones);
        setTrackOffset(track, track.offset + 1);
    }

    // panpot
    private void evtPanPot(Track track, MLD.Event event) {
        sampler.panpot(event.channel, event.panpot);
        setTrackOffset(track, track.offset + 1);
    }

    // pitchbend
    private void evtPitchBend(Track track, MLD.Event event) {
        sampler.pitchBend(event.channel, event.semitones);
        setTrackOffset(track, track.offset + 1);
    }

    // pitchbend-range
    private void evtPitchRange(Track track, MLD.Event event) {
        sampler.pitchBendRange(event.channel, event.range);
        setTrackOffset(track, track.offset + 1);
    }

    // program-change
    private void evtProgramChange(Track track, MLD.Event event) {
        sampler.programChange(event.channel, event.program);
        setTrackOffset(track, track.offset + 1);
    }

    // timebase-tempo
    private void evtTimebaseTempo(Track track, MLD.Event event) {
        if (event.timebase == -1)
            return;
        float prev = framesPerTick;
        setTempo(event.timebase, event.tempo);
        pendingFrames = pendingFrames * framesPerTick / prev;
        setTrackOffset(track, track.offset + 1);
    }

    // volume
    private void evtVolume(Track track, MLD.Event event) {
        sampler.volume(event.channel, event.volume);
        setTrackOffset(track, track.offset + 1);
    }

}
