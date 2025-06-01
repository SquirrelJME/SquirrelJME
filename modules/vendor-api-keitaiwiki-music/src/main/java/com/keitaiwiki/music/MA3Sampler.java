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
 * Sample generator that mimics YAMAHA MA-3. Supports FM synthesis using MA-2
 * or MA-3 presets as well as wave drums.<br><br>
 * Default settings specific to this sampler are as follows:
 * <table class="striped" style="margin-left:2em;text-align:left">
 *  <caption style="display:none">X</caption>
 *  <thead>
 *    <tr><th>Scope</th><th>Property</th><th>Default</th></tr>
 *  </thead>
 *  <tbody>
 *    <tr><td>Master</td><td>Fade</td><td>0.0f</td></tr>
 *    <tr><td>Master</td><td>Custom FM instruments</td><td>None</td></tr>
 *    <tr><td>Master</td><td>Custom wave drums</td><td>None</td></tr>
 *    <tr><td>Sampler</td><td>Drum type</td>
 *      <td>{@code FM_MA3_4OP}</td></tr>
 *    <tr><td>Sampler</td><td>Instrument type</td>
 *      <td>{@code FM_MA3_4OP}</td></tr>
 *    <tr><td>Sampler</td><td>Wave drum type</td>
 *      <td>{@code WAVE_DRUM_MA3}</td></tr>
 *  </tbody>
 * </table>
 * This class only implements the relevant OPL features that it requires, and
 * is not a general-purpose OPL emulator.
 */
public class MA3Sampler implements Sampler {

    // Instance fields
    private Algorithm[] algDrums;          // FM drum algorithms
    private Algorithm[] algInstruments;    // FM instrument algorithms
    private Algorithm[] algWaveDrums;      // Wave drum algorithms
    private int         prgDrumType;       // FM drum algorithm type
    private int         prgInstrumentType; // FM instrument algorithm type
    private int         prgWaveDrumType;   // Wave drums algorithm type



    //////////////////////////////// Constants ////////////////////////////////

    /**
     * Specifies the use of MA-2 algorithms for FM synthesis.
     * @see MA3Sampler(int,int,int)
     * @see setDrumType
     * @see setInstrumentType
     */
    public static final int FM_MA2 = 2;

    /**
     * Specifies the use of 2-operator MA-3 algorithms for FM synthesis.
     * @see MA3Sampler(int,int,int)
     * @see setDrumType
     * @see setInstrumentType
     */
    public static final int FM_MA3_2OP = 1;

    /**
     * Specifies the use of 4-operator MA-3 algorithms for FM synthesis.
     * @see MA3Sampler(int,int,int)
     * @see setDrumType
     * @see setInstrumentType
     */
    public static final int FM_MA3_4OP = 0;

    /***
     * Nominal hardware sampling rate. When rendering samples at this rate, the
     * output will have a 1:1 correspondence with what the hardware would
     * produce.
     * @see instance(float)
     */
    public static final float SAMPLE_RATE = 33868800.0f / 684;

    /**
     * Specifies the use of MA-3 waves for wave drum synthesis.
     * @see MA3Sampler(int,int,int)
     * @see setWaveDrumType
     */
    public static final int WAVE_DRUM_MA3 = 0;

    /**
     * Specifies that FM drum algorithms always be used in place of wave drums.
     * @see MA3Sampler(int,int,int)
     * @see setWaveDrumType
     */
    public static final int WAVE_DRUM_NONE = -1;



    //////////////////////////// Private Constants ////////////////////////////

    // Lookup tables
    private static final int[]   AM_LFO_A; // Amplitude modulation levels
    private static final int[]   EXP;      // Binary exponent
    private static final int[]   SUSTAINS; // Sustain levels
    private static final int[]   WAVE_ENV; // Wave drum envelope levels
    private static final int[][] WAVES;    // Waveforms

    // Envelope stages
    private static final int ENV_ATTACK  = 0;
    private static final int ENV_DECAY   = 1;
    private static final int ENV_SUSTAIN = 2;
    private static final int ENV_RELEASE = 3;
    private static final int ENV_DONE    = 4;

    // Envelope attenuation parameters by BLOCK and F_NUMBER, used with KSL
    private static final int[] KSL_B = { 0, 2, 1, 4 };
    private static final int[] KSL_F =
        { 56, 32, 24, 19, 16, 13, 11, 9, 8, 6, 5, 4, 3, 2, 1, 0 };

    // Frequency multipliers, doubled to implement with a right shift
    private static final int[] MULTIS =
        { 1, 2, 4, 6, 8, 10, 12, 14, 16, 18, 20, 20, 24, 24, 30, 30 };

    // YAMAHA AICA ADPCM quantization step size lookup table
    private static final int[] AICA_STEPS =
        { 230, 230, 230, 230, 307, 409, 512, 614 };

    // Bit flags indicating which FM operators control the final output
    private static int[] ENV_FLAGS =
        { 0b10, 0b11, 0b1111, 0b1000, 0b1000, 0b1010, 0b1001, 0b1101 };

    // Amplitude modulation LFO phase-advance
    private static final int[] AM_LFO_B = { 8, 18, 26, 31 };

    // Formula constants
    private static final int    A4      = 81;         // Key index bias
    private static final int    FULL    =  0;         // Wave maximum
    private static final int    NTS     =  1;
    private static final double MAGIC_B = 12 / Math.log(2);
    private static final double MAGIC_F = 684 / 33868800.0;
    private static final int    MINUS   = 0x80000000; // Wave negative
    private static final int    ZERO    = 0x1000;     // Wave minimum

    // Compute lookup tables
    // Formulas courtesy of Gambrell and Niemitalo: "OPLx decapsulated"
    static {

        // Lookup memory
        AM_LFO_A = new int[52];
        EXP      = new int[256];
        SUSTAINS = new int[16];
        WAVE_ENV = new int[512];
        WAVES    = new int[32][1024];

        // Named waves
        int[] saw = WAVES[24]; // Sawtooth
        int[] sin = WAVES[ 0]; // Sine
        int[] tri = WAVES[16]; // Triangle
        int[] trp = WAVES[ 8]; // Trapezoid (clamped 2*triangle)

        // Quarter-period lookup tables
        for (int x = 0; x < 256; x++) {

            // Binary exponent table
            EXP[x] = 1024 | (int)
                Math.round((Math.pow(2, (255 - x) / 256.0) - 1 ) * 1024);

            // Sine table
            int y = (int) Math.round(-Math.log(
                Math.sin((x + 0.5) * Math.PI / 256 / 2)
            ) / Math.log(2) * 256);
            sin[      x] = sin[ 511 - x] = y;
            sin[512 + x] = sin[1023 - x] = y | MINUS;

            // Triangle table
            y = (int) Math.round(-Math.log(
                (x + 0.5) / 256
            ) / Math.log(2) * 256);
            tri[      x] = tri[ 511 - x] = y;
            tri[512 + x] = tri[1023 - x] = y | MINUS;
        }

        // Trapezoid table
        for (int x = 0; x < 1024; x++) {
            trp[x] = x < 128 ? tri[x << 1] : x < 256 ? FULL:
                x < 512 ? trp[511 - x] : trp[1023 - x] | MINUS;
        }

        // Sawtooth table
        for (int x = 0; x < 512; x++) {
            int y = (int) Math.round(-Math.log(
                (x + 0.5) / 512
            ) / Math.log(2) * 256);
            saw[       x] = y;
            saw[1023 - x] = y | MINUS;
        }

        // Compute other waveforms
        for (int x = 0; x < 1024; x++) {
         // WAVES[ 0] is sin
            WAVES[ 1][x] = x < 512 ? sin[x] : ZERO;
            WAVES[ 2][x] = sin[x & 511];
            WAVES[ 3][x] = (x & 511) < 256 ? sin[x & 255] : ZERO;
            WAVES[ 4][x] = x < 512 ? sin[x << 1] : ZERO;
            WAVES[ 5][x] = x < 512 ? sin[x << 1 & 511] : ZERO;
            WAVES[ 6][x] = x < 512 ? FULL : MINUS;
            WAVES[ 7][x] = x < 512 ? (EXP[255 ^ x >> 1] - 1024) << 1 :
                WAVES[7][1023 - x] | MINUS;
         // WAVES[ 8] is trp
            WAVES[ 9][x] = x < 512 ? trp[x] : ZERO;
            WAVES[10][x] = trp[x & 511];
            WAVES[11][x] = (x & 511) < 256 ? trp[x & 255] : ZERO;
            WAVES[12][x] = x < 512 ? trp[x << 1] : ZERO;
            WAVES[13][x] = x < 512 ? trp[x << 1 & 511] : ZERO;
            WAVES[14][x] = x < 512 ? FULL : ZERO;
            WAVES[15][x] = ZERO; // PCM RAM
         // WAVES[16] is tri
            WAVES[17][x] = x < 512 ? tri[x] : ZERO;
            WAVES[18][x] = tri[x & 511];
            WAVES[19][x] = (x & 511) < 256 ? tri[x & 255] : ZERO;
            WAVES[20][x] = x < 512 ? tri[x << 1] : ZERO;
            WAVES[21][x] = x < 512 ? tri[x << 1 & 511] : ZERO;
            WAVES[22][x] = (x & 511) < 256 ? FULL : ZERO;
            WAVES[23][x] = ZERO; // PCM RAM
         // WAVES[24] is saw
            WAVES[25][x] = x < 512 ? saw[x] : ZERO;
            WAVES[26][x] = saw[x & 511];
            WAVES[27][x] = x < 128 ? saw[x] :
                x >= 512 && x < 768 ? saw[x - 512 << 1] : ZERO;
            WAVES[28][x] = x < 512 ? saw[x << 1] : ZERO;
            WAVES[29][x] = x < 512 ? saw[x << 1 & 511] : ZERO;
            WAVES[30][x] = x < 256 ? FULL : ZERO;
            WAVES[31][x] = ZERO; // PCM RAM
        }

        // Compute amplitude modulation LFO
        for (int x = 0; x < 26; x++)
            AM_LFO_A[x] = AM_LFO_A[51 - x] = x;

        // Compute sustain levels
        SUSTAINS[ 0] =   0;
        SUSTAINS[15] = 511;
        for (int x = 1; x < 15; x++) {
            SUSTAINS[x] = (int) Math.round(16 *
                Math.pow(2, Math.log(x) / Math.log(2)));
        }

        // Compute wave drum envelope levels
        for (int x = 0; x < 512; x++) {
            WAVE_ENV[x] = (int) Math.round(32767 *
                Math.pow(10, x * -96.0 / 511 / 20));
        }

    }



    ////////////////////////////// Constructors ///////////////////////////////

    /**
     * Create a sampler with default parameters. Same as invoking
     * {@code MA3Sampler(FM_MA3_4OP, FM_MA3_4OP, WAVE_DRUM_MA3)}.
     * @see MA3Sampler(int,int,int)
     */
    public MA3Sampler() {
        this(FM_MA3_4OP, FM_MA3_4OP, WAVE_DRUM_MA3);
    }

    /**
     * Create a sampler with initial parameters. Equivalent to following the
     * parameterless constructor with calls to {@code setDrumType()},
     * {@code setInstrumentType()} and {@code setWaveDrumType()}.
     * @param instrumentType Specifies the data source for FM synthesis
     * instrument algorithms. Must be one of {@code FM_MA2}, {@code FM_MA3_2OP}
     * or {@code FM_MA3_4OP}.
     * @param drumType Specifies the data source for FM synthesis drum
     * algorithms. Must be one of {@code FM_MA2}, {@code FM_MA3_2OP} or
     * {@code FM_MA3_4OP}.
     * @param waveDrumType Specifies the data source for wave synthesis drum
     * algorithms. Must be either {@code WAVE_DRUM_NONE} or
     * {@code WAVE_DRUM_MA3}.
     * @exception IllegalArgumentException if the value of
     * {@code instrumentType}, {@code drumType} or {@code waveDrumType} is
     * invalid.
     * @see setDrumType(int)
     * @see setInstrumentType(int)
     * @see setWaveDrumType(int)
     */
    public MA3Sampler(int instrumentType, int drumType, int waveDrumType) {
        super();
        algWaveDrums = MA3_DRUMS_WAVE;
        setInstrumentType  (instrumentType);
        setDrumType        (drumType);
        setWaveDrumType    (waveDrumType);
    }



    ///////////////////////////// Public Methods //////////////////////////////

    /**
     * Retrieve the current FM synthesis drum algorithm type. This will be the
     * value most recently used with {@code setDrumType()}.
     * @return The current FM synthesis drum algorithm type: {@code FM_MA2},
     * {@code FM_MA3_2OP} or {@code FM_MA3_4OP}.
     * @see setDrumType(int)
     */
    public int getDrumType() {
        return prgDrumType;
    }

    /**
     * Retrieve the current FM synthesis instrument algorithm type. This will
     * be the value most recently used with {@code setInstrumentType()}.
     * @return The current FM synthesis instrument algorithm type:
     * {@code FM_MA2}, {@code FM_MA3_2OP} or {@code FM_MA3_4OP}.
     * @see setInstrumentType(int)
     */
    public int getInstrumentType() {
        return prgInstrumentType;
    }

    /**
     * Retrieve the current wave synthesis drum algorithm type. This will be
     * the value most recently used with {@code setWaveDrumType()}.
     * @return The current wave synthesis drum algorithm type:
     * {@code WAVE_DRUM_NONE} or {@code WAVE_DRUM_MA3}.
     * @see setWaveDrumType(int)
     */
    public int getWaveDrumType() {
        return prgWaveDrumType;
    }

    /**
     * Produces an instance of this sampler that can be used to render samples.
     * Calling {@code setDrumType()}, {@code setInstrumentType()} or
     * {@code setWaveDrumType()} after an instance has been created will affect
     * new notes played by the instance.
     * @param sampleRate The output sampling rate of the rendered samples.
     * @return A new sampler instance that can render samples using the current
     * configuration of this sampler itself.
     * @exception IllegalArgumentException if {@code sampleRate} is a
     * non-number or is less than or equal to zero.
     */
    public Sampler.Instance instance(float sampleRate) {
        if (!Float.isFinite(sampleRate) || sampleRate <= 0.0f)
            throw new IllegalArgumentException("Invalid sampling rate.");
        return new Instance(sampleRate);
    }

    /**
     * Specify a new FM synthesis drum algorithm type. All new FM drum notes
     * generated by instances of this sampler will use the new setting.<br><br>
     * The default FM drum algorithm type is {@code FM_MA3_4OP}.
     * @param type Specifies the data source for FM drum algorithms. Must be
     * one of {@code FM_MA2}, {@code FM_MA3_2OP} or {@code FM_MA3_4OP}.
     * @return The value of {@code type}.
     * @exception IllegalArgumentException if the value of {@code type} is
     * invalid.
     * @see getDrumType()
     * @see setInstrumentType(int)
     * @see setWaveDrumType(int)
     */
    public int setDrumType(int type) {
        switch (type) {
            case FM_MA2    : algDrums = MA2_DRUMS    ; break;
            case FM_MA3_2OP: algDrums = MA3_DRUMS_2OP; break;
            case FM_MA3_4OP: algDrums = MA3_DRUMS_4OP; break;
            default:
                throw new IllegalArgumentException("Invalid type.");
        }
        return prgDrumType = type;
    }

    /**
     * Specify a new FM synthesis instrument algorithm type. All new FM
     * instrument notes generated by instances of this sampler will use the new
     * setting.<br><br>
     * The default FM instrument algorithm type is {@code FM_MA3_4OP}.
     * @param type Specifies the data source for FM instrument algorithms. Must
     * be one of {@code FM_MA2}, {@code FM_MA3_2OP} or
     * {@code FM_MA3_4OP}.
     * @return The value of {@code type}.
     * @exception IllegalArgumentException if the value of {@code type} is
     * invalid.
     * @see getInstrumentType()
     * @see setDrumType(int)
     * @see setWaveDrumType(int)
     */
    public int setInstrumentType(int type) {
        switch (type) {
            case FM_MA2    : algInstruments = MA2_INSTRUMENTS    ; break;
            case FM_MA3_2OP: algInstruments = MA3_INSTRUMENTS_2OP; break;
            case FM_MA3_4OP: algInstruments = MA3_INSTRUMENTS_4OP; break;
            default:
                throw new IllegalArgumentException("Invalid type.");
        }
        return prgInstrumentType = type;
    }

    /**
     * Specify a new wave synthesis drum algorithm type. All new wave drum
     * notes generated by instances of this sampler will use the new
     * setting.<br><br>
     * The default wave drum algorithm type is {@code WAVE_DRUM_MA3}.
     * @param type Specifies the data source for wave drum algorithms. Must be
     * either {@code WAVE_DRUM_NONE} or {@code WAVE_DRUM_MA3}.
     * @return The value of {@code type}.
     * @exception IllegalArgumentException if the value of {@code type} is
     * invalid.
     * @see getWaveDrumType()
     * @see setDrumType(int)
     * @see setInstrumentType(int)
     */
    public int setWaveDrumType(int type) {
        switch (type) {
            case WAVE_DRUM_NONE: algWaveDrums = null          ; break;
            case WAVE_DRUM_MA3 : algWaveDrums = MA3_DRUMS_WAVE; break;
            default:
                throw new IllegalArgumentException("Invalid type.");
        }
        return prgWaveDrumType = type;
    }



    ///////////////////////////// Private Methods /////////////////////////////

    // Decode ADPCM samples encoded as YAMAHA AICA
    static int[] decodeAICA(byte[] adpcm, int offset, int length) {
        int[] ret = new int[length * 2];
        int   An  = 127; // Quantization step size
        int   Xn  =   0; // Predictor

        // Process all ADPCM bytes
        for (int src = offset, dest = 0; src < offset + length; src++) {
            int bits = adpcm[src] & 0xFF;

            // Process both nibbles
            for (int n = 0; n < 2; n++, bits >>= 4, dest++) {

                // Compute the next output sample
                ret[dest] = Xn = Math.min(Math.max(
                    (1 - ((bits & 8) >> 2)) *             // Sign
                    Math.min(Math.max(
                        (((bits & 7) << 1) | 1) * An >> 3 // Magnitude
                    , 0), 32767)
                    + Xn * 254 / 255                      // Accumulate
                , -32768), 32767);

                // Compute the next quantization step size
                An = Math.min(Math.max(
                    AICA_STEPS[bits & 7] * An >> 8, 127), 24576);
            }
        }

        // The hardware will perform interpolation and low-pass filter at this
        // point to smooth out the waveform, but the parameters are not known.

        return ret;
    }

    // Decode initial wave ROM banks
    static int[][] waveRom(String[] roms) {
        Base64.Decoder base64 = Base64.getMimeDecoder();
        int[][]        ret    = new int[8][];
        for (int x = 0; x < roms.length; x++) {
            byte[] adpcm = base64.decode(roms[x]);
            ret[x]       = decodeAICA(adpcm, 0, adpcm.length);
        }
        return ret;
    }



    //////////////////////////////// Algorithm ////////////////////////////////

    // Template algorithm for OPL synthesis
    private static class Algorithm {

        // Instance fields
        int        alg;        // Operator connection algorithm
        int        drumKey;    // Key played for drum notes
        int        ep;         // Wave end point
        float      freqBase;   // Drum frequency base
        int        fs;         // Wave sampling frequency
        boolean    isDrum;     // Is a drum note
        boolean    isForever;  // Envelopes never fully decay
        boolean    isWave;     // Is a wave rum algorithm
        int        lfo;        // Modulation LFO rate multiplier
        int        lp;         // Wave loop point
        Operator[] operators;  // FM operator templates
        int        panpot;     // Stereo balance
        boolean    pe;         // Unknown significance
        boolean    rm;         // Wave ROM select
        float      volLeft;    // Left stereo amplitude
        float      volRight;   // Right stereo amplitude
        float      wavAdvance; // Wave samples to advance per output sample
        int        waveId;     // Wave ROM index



        /////////////////////////// Static Methods ////////////////////////////

        private static Algorithm[] from(String[] defs,
            boolean isDrum, boolean isWave) {
            Base64.Decoder base64 = Base64.getMimeDecoder();
            Algorithm[]    ret    = null;

            // FM presets
            if (!isWave) {
                ret = new Algorithm[defs.length];
                for (int x = 0; x < defs.length; x++)
                    ret[x] = new Algorithm(base64.decode(defs[x]), isDrum);
            }

            // Wave drum presets
            else {
                ret = new Algorithm[61];
                for (int x = 0; x < defs.length; x++) {
                    Algorithm alg = new Algorithm(base64.decode(defs[x]), 0);
                    ret[alg.drumKey - 24] = alg;
                }
            }

            return ret;
        }



        //////////////////////////// Constructors /////////////////////////////

        // FM constructor
        private Algorithm(byte[] bytes, boolean isDrum) {

            // Decode bits
            lfo     = bytes[0]      &   3;
            panpot  = bytes[1] >> 3 &  31;
            alg     = bytes[1]      &   7;
            drumKey = bytes[2]      & 127;

            // Operators
            operators = new Operator[alg < 2 ? 2 : 4];
            for (int x = 0; x < operators.length; x++)
                operators[x] = new Operator(bytes, 3 + x * 7);

            // Instance fields
            freqBase    = (float) (440 * Math.pow(2, (drumKey - 69) / 12.0));
            this.isDrum = isDrum;
            isWave      = false;
            initPost();
        }

        // Wave drum constructor
        private Algorithm(byte[] message, int offset) {
            int bits; // Scratch

            // Parse fields
            drumKey = message[offset++] & 0xFF;
            fs      = (message[offset]&0xFF) << 8 | message[offset + 1]&0xFF;
            offset += 2;
            bits    = message[offset++] & 0xFF;
            panpot  = bits >> 3 & 31;
            pe      =(bits      &  1) != 0;
            bits    = message[offset++] & 0xFF;
            lfo     = bits >> 6 & 3;
            // pcm  = bits >> 1 & 1;
            operators = new Operator[] { new Operator(offset, message) };
            offset += 7; // 5 for operator, 2 unknown (always zero?)
            lp      = (message[offset]&0xFF) << 8 | message[offset + 1]&0xFF;
            offset += 2;
            ep      = (message[offset]&0xFF) << 8 | message[offset + 1]&0xFF;
            offset += 2;
            bits    = message[offset++] & 0xFF;
            rm      =(bits >> 7 & 1) != 0;
            waveId  = bits & 7;

            // Instance fields
            isDrum     = true;
            isWave     = true;
            wavAdvance = fs / SAMPLE_RATE;
            initPost();
        }

        // FM SysEx constructor
        private Algorithm(int offset, byte[] message) {
            int bits;
            int type = message[offset  ] & 0xFF;
            offset  += 4;
            bits     = message[offset++] & 0xFF;
            panpot   = bits >> 3 & 31;
            bits     = message[offset++] & 0xFF;
            lfo      = bits >> 6 &  3;
            pe       =(bits >> 5 &  1) != 0;
            alg      = bits      &  7;
            if (alg > 1 && type == 0x01)
                throw new RuntimeException("Operator count mismatch");
            operators = new Operator[alg < 2 ? 2 : 4];
            for (int x = 0; x < operators.length; x++, offset += 7)
                operators[x] = new Operator(message, offset, true);
            initPost();
        }



        /////////////////////////// Private Methods ///////////////////////////

        // Initialize settings after decoding parameters
        private void initPost() {

            // Test whether the envelopes fully decay
            if (!isWave /*|| lp < ep*/) {
                int flags = isWave ? 1 : ENV_FLAGS[alg];
                for (
                    int x = 0;
                    !isForever && x < operators.length;
                    x++, flags >>= 1
                ) {
                    Operator op = operators[x];
                    isForever   = (flags & 1) != 0 &&
                        (op.xof ? op.sr==0 || op.dr==0 && op.sr!=0 : op.rr==0);
                }
            }

            // Volume levels
            volRight = panpot / (panpot <= 15 ? 30.0f : 31.0f);
            volLeft  = 1 - volRight;
        }

    }



    ///////////////////////////////// Channel /////////////////////////////////

    // Output channel
    private class Channel {

        // Instance fields
        float           bendBase;    // Pitch bend base ratio
        float           bendOut;     // Effective channel frequency ratio
        float           bendRange;   // Pitch bend magnitude
        int             index;       // Index in sampler
        Instance        instance;    // Encapsulating instance
        boolean         isDrum;      // The channel plays drum notes
        Note[]          notesOn;     // All notes currently on keys
        ArrayList<Note> notesOut;    // All notes that are generating output
        int             prgBank;     // Program bank
        int             prgProgram;  // Program index in bank
        float           volLeft;     // Left stereo amplitude
        float           volLeftOut;  // Left stereo output amplitude
        float           volLevel;    // Channel output amplitude
        float           volPanning;  // Stereo level
        float           volRight;    // Right stereo amplitude
        float           volRightOut; // Right stereo output amplitude



        //////////////////////////// Constructors /////////////////////////////

        Channel(Instance instance, int index) {
            this.index    = index;
            this.instance = instance;
            notesOn       = new Note[128]; // C-2 .. G8
            notesOut      = new ArrayList<Note>();
        }



        /////////////////////////// Private Methods ///////////////////////////

        // Frequency has changed
        private void onFrequency() {
            float bend = instance.bendOut * bendOut;
            for (Note note : notesOut)
                note.onFrequency(bendOut);
        }

        // Volume has changed
        private void onVolume() {
            volLeftOut  = instance.volOut * volLeft;
            volRightOut = instance.volOut * volRight;
            for (Note note : notesOut)
                note.onVolume();
        }

        // Render the next input sample
        private void render() {
            for (int x = 0; x < notesOut.size(); x++) {
                if (notesOut.get(x).render())
                    notesOut.remove(x--);
            }
        }

        // Initialize state
        private void reset() {

            // Instance fields
            bendBase    = 0.0f;
            bendOut     = 1.0f;
            bendRange   = 2.0f;
            isDrum      = false;
            prgBank     = 0;
            prgProgram  = 0;
            volLevel    = 1.0f;
            volPanning  = 0.5f;
            volLeft     = 0.5f;
            volLeftOut  = 0.5f;
            volRight    = 0.5f;
            volRightOut = 0.5f;

            // Stop playing all notes (not calling note.onFrequency())
            for (int x = 0; x < notesOn.length; x++)
                notesOn[x] = null;
            for (Note note : notesOut)
                note.stop();
        }

    }



    //////////////////////////////// Instance /////////////////////////////////
     class Instance implements Sampler.Instance {

        // Instance fields
        int         amPhase;     // Amplitude modulator phase
        float       bendOut;     // Global pitch bend
        Channel[]   channels;    // Channel states
        float       sampleRate;  // Output sampling rate
        float[]     smpNext;     // Next input sample
        float       smpPosition; // Position between input samples
        float[]     smpPrev;     // Previous input sample
        float       smpWidth;    // Number of input samples per output sample
        int         vibPhase;    // Frequency modulator phase
        float       volFade;     // Global attenuation
        float       volLevel;    // Global volume
        float       volOut;      // Effective global volume
        float       volRate;     // Automatic volume adjustment rate
        Algorithm[] wavDrums;    // Registered wave drums
        int[]       wavRam;      // Wave RAM, decoded from ADPCM
        HashMap<Integer, Algorithm> fm2ops; // 2-operator instruments
        HashMap<Integer, Algorithm> fm4ops; // 4-operator instruments



        //////////////////////////// Constructors /////////////////////////////

        Instance(float sampleRate) {

            // Instance fields
            channels        = new Channel[10];
            fm2ops          = new HashMap<Integer, Algorithm>();
            fm4ops          = new HashMap<Integer, Algorithm>();
            this.sampleRate = sampleRate;
            smpNext         = new float[2];
            smpPrev         = new float[2];
            smpWidth        = SAMPLE_RATE / sampleRate;
            volRate         = 1 / (sampleRate * 0.01f);
            wavDrums        = new Algorithm[128];

            // Channels
            for (int x = 0; x < channels.length; x++)
                channels[x] = new Channel(this, x);

            // Initialize state
            reset();
        }



        /////////////////////////// Public Methods ////////////////////////////

        // Specify a channel's program bank.
        public void bankChange(int channel, int bank) {
            if (channel < 0 || channel >= channels.length)
                return;
            Channel chan = channels[channel];
            chan.prgBank = bank;
        }

        // Specify whether a channel should play drum notes.
        public void drumEnable(int channel, boolean enable) {
            if (channel < 0 || channel >= channels.length)
                return;
            Channel chan = channels[channel];
            chan.isDrum  = enable;
        }

        // Determine whether or not any notes are producing output.
        public boolean isFinished() {
            for (Channel chan : channels) {
                if (chan.notesOut.size() != 0)
                    return false;
            }
            return true;
        }

        // Deactivate a key that has previoulsy been activated on a channel.
        public void keyOff(int channel, int key) {
            if (
                channel  < 0 || channel  >= channels.length ||
                A4 + key < 0 || A4 + key >= 128
            ) return;
            Channel chan = channels[channel];
            Note    note = chan.notesOn[A4 + key];
            if (note != null)
                note.off();
            chan.notesOn[A4 + key] = null;
        }

        // Activate a key on a channel.
        public void keyOn(int channel, int key, float velocity) {

            // Error checking
            if (!Float.isFinite(velocity) || velocity < 0.0f)
                throw new IllegalArgumentException("Invalid velocity.");
            if (
                channel  < 0 || channel  >= channels.length ||
                A4 + key < 0 || A4 + key >= 128
            ) return;

            // Working variables
            Algorithm algorithm = null;
            Channel   chan      = channels[channel];
            float     freqBase  = 0;
            boolean   isWave    = false;
            Note      note      = chan.notesOn[A4 + key];

            // FM instrument algorithm
            if (!chan.isDrum) {
                algorithm = getFMInstrument(chan.prgBank, chan.prgProgram);
                freqBase  = (float) (440 * Math.pow(2, key / 12.0));
            }

            // Drum algorithm
            else {
                if (prgWaveDrumType != WAVE_DRUM_NONE) {
                    algorithm = getDrumWave(key);
                    isWave    = algorithm != null;
                }
                if (algorithm == null)
                    algorithm = getDrumFM(key);
                if (algorithm == null)
                    return;
                freqBase = algorithm.freqBase;
                isWave   = algorithm.isWave;
            }

            // Stop the previous note if necessary
            if (note != null && (chan.isDrum || note.algorithm != algorithm)) {
                keyOff(channel, key);
                note = null;
            }

            // Spawn a new note if necessary
            if (note == null) {
                note = new Note(chan, key, algorithm);
                chan.notesOn[A4 + key] = note;
                chan.notesOut.add(note);
            }

            // Configure fields
            note.volBase = velocity;
            note.onVolume();
            if (!isWave) {
                note.freqBase = freqBase;
                note.onFrequency(bendOut * chan.bendOut);
            }

        }

        // Specify the global pitch bend.
        public void masterTune(float semitones) {
            if (!Float.isFinite(semitones))
                throw new IllegalArgumentException("Invalid semitones.");
            bendOut = (float) Math.pow(2, semitones);
            for (Channel chan : channels)
                chan.onFrequency();
        }

        // Specify the global volume.
        public void masterVolume(float volume) {
            if (!Float.isFinite(volume) || volume < 0.0f)
                throw new IllegalArgumentException("Invalid volume.");
            volLevel = volume;
            onVolume();
        }

        // Specify stereo panning on a channel.
        public void panpot(int channel, float panpot) {
            if (!Float.isFinite(panpot) || panpot < -1.0f || panpot > 1.0f)
                throw new IllegalArgumentException("Invalid panpot.");
            if (channel < 0 || channel >= channels.length)
                return;
            Channel chan    = channels[channel];
            chan.volPanning = (panpot + 1) / 2;
            chan.volLeft    = (1.0f - chan.volPanning) * chan.volLevel;
            chan.volRight   =         chan.volPanning  * chan.volLevel;
            chan.onVolume();
        }

        // Specify a channel's pitch bend.
        public void pitchBend(int channel, float semitones) {
            if (!Float.isFinite(semitones))
                throw new IllegalArgumentException("Invalid semitones.");
            if (channel < 0 || channel >= channels.length)
                return;
            Channel chan  = channels[channel];
            chan.bendBase = semitones;
            chan.bendOut  = (float) Math.pow(2, chan.bendBase*chan.bendRange);
            chan.onFrequency();
        }

        // Specify the range of a channel's pitch bend.
        public void pitchBendRange(int channel, float range) {
            if (!Float.isFinite(range) || range < 0.0f)
                throw new IllegalArgumentException("Invalid range.");
            if (channel < 0 || channel >= channels.length)
                return;
            Channel chan   = channels[channel];
            chan.bendRange = range;
            chan.bendOut   = (float) Math.pow(2, chan.bendBase*chan.bendRange);
            chan.onFrequency();
        }

        // Speicfy a channel's program number.
        public void programChange(int channel, int program) {
            Channel chan    = channels[channel];
            chan.prgProgram = program;
        }

        // Generate output samples.
        public void render(float[] samples, int offset, int frames) {
            render(samples, offset, frames, 1.0f, 1.0f, true, true);
        }

        // Generate output samples.
        public void render(float[] samples, int offset, int frames,
            float amplitude) {
            render(samples, offset, frames, amplitude, amplitude, true, true);
        }

        // Generate output samples.
        public void render(float[] samples, int offset, int frames,
            float left, float right) {
            render(samples, offset, frames, left, right, true, true);
        }

        // Generate output samples.
        public void render(float[] samples, int offset, int frames,
            float left, float right, boolean erase, boolean clamp) {

            // Error checking
            if (samples == null)
                throw new NullPointerException("A sample buffer is required.");
            if (frames < 0)
                throw new IllegalArgumentException("Invalid frames.");
            if (offset < 0 || offset + frames * 2 > samples.length) {
                throw new ArrayIndexOutOfBoundsException(
                    "Invalid range in sample buffer.");
            }
            if (!Float.isFinite(left ) || left  < 0.0f)
                throw new IllegalArgumentException("Invalid left amplitude.");
            if (!Float.isFinite(right) || right < 0.0f)
                throw new IllegalArgumentException("Invalid right amplitude.");

            // Process all output frames
            float[] frame = new float[2];
            for (int x = 0; x < frames; x++) {
                float l  = smpPosition;
                float r = l + smpWidth;
                float a, b; // Scratch

                // Edge case: need the next input sample
                if (l == 0.0f)
                    sample();

                // Left and right are in the same input sample
                if (l < 1.0f) {
                    a = (l + r) / 2;
                    frame[0] = smpPrev[0] + (smpNext[0] - smpPrev[0]) * a;
                    frame[1] = smpPrev[1] + (smpNext[1] - smpPrev[1]) * a;
                }

                // Left and right span input samples
                else {

                    // First partial
                    a = (l + 1.0f) / 2;
                    b = 1.0f - l;
                    frame[0] = (smpPrev[0]+(smpNext[0]-smpPrev[0])*a) * b;
                    frame[1] = (smpPrev[1]+(smpNext[1]-smpPrev[1])*a) * b;

                    // All wholes
                    for (int y = (int) Math.floor(r) - 1; y > 0; y--) {
                        smpPrev[0] = smpNext[0];
                        smpPrev[1] = smpNext[1];
                        sample();
                        frame[0] += (smpPrev[0] + smpNext[0]) / 2;
                        frame[1] += (smpPrev[1] + smpNext[1]) / 2;
                    }

                    // Record the latest input sample
                    smpPrev[0] = smpNext[0];
                    smpPrev[1] = smpNext[1];

                    // Last partial
                    r %= 1.0f;
                    if (r != 0.0f) {
                        sample();
                        a = r / 2;
                        frame[0] += (smpPrev[0]+(smpNext[0]-smpPrev[0])*a) * r;
                        frame[1] += (smpPrev[1]+(smpNext[1]-smpPrev[1])*a) * r;
                    }

                    // Take the weigted average of all spanned input samples
                    frame[0] /= smpWidth;
                    frame[1] /= smpWidth;
                }

                // Output scaling
                frame[0] *= left;
                frame[1] *= right;

                // Incorporate the existing contents of the buffer
                if (!erase) {
                    frame[0] += samples[offset    ];
                    frame[1] += samples[offset + 1];
                }

                // Constrain the output
                if (clamp) {
                    frame[0] = Math.min(Math.max(frame[0], -1.0f), 1.0f);
                    frame[1] = Math.min(Math.max(frame[1], -1.0f), 1.0f);
                }

                // Output the frame
                samples[offset++] = frame[0];
                samples[offset++] = frame[1];

                // Advance to the next output sample
                smpPosition = r;
            }

        }

        // Initialize all output state.
        public void reset() {
            amPhase     = 0;
            bendOut     = 1.0f;
            smpPosition = 0.0f;
            smpPrev[0]  = smpPrev[1] = 0.0f;
            vibPhase    = 0;
            volFade     = 0.0f;
            volLevel    = 1.0f;
            volOut      = 1.0f;
            wavRam      = null;
            fm2ops.clear();
            fm4ops.clear();
            for (Channel chan : channels)
                chan.reset();
            for (int x = 0; x < wavDrums.length; x++)
                wavDrums[x] = null;
        };

        // Terminate all active notes.
        public void stopAll() {
            for (Channel chan : channels) {
                for (int x = 0; x < chan.notesOn.length; x++)
                    chan.notesOn[x] = null;
                for (Note note : chan.notesOut)
                    note.stop();
            }
        };

        // Process a SysEx message.
        public void sysEx(byte[] message) {

            // Error checking
            if (
                message             == null        ||
                message.length      <  4           ||
                 message[0]         != (byte) 0x11 ||
                 message[1]         != (byte) 0x01 ||
                (message[2] & 0xF0) !=        0xF0
            ) return;

            // Processing by sub-message type
            switch (message[3] & 0xFF) {
                case 0x00: break; // Seen in Smwemu_N.dll at 10028975
                case 0x01: break; // Seen in Smwemu_N.dll at 1002899D
                case 0x02: break; // Seen in Smwemu_N.dll at 100289B4
                case 0x03: // Specify the global fade
                    setMasterFade(message);
                    break;
                case 0x04: // Specify FM instrument algorithms
                    setFMAlgorithms(message);
                    break;
                case 0x05: // Register wave drum algorithms
                    setWaveDrums(message);
                    stopWaveDrums();
                    break;
                case 0x06: // Supply wave drum samples
                    wavRam = decodeAICA(message, 4, message.length - 4);
                    stopWaveDrums();
                    break;
            }

        }

        // Specify a channel's volume.
        public void volume(int channel, float volume) {
            if (!Float.isFinite(volume) || volume < 0.0f)
                throw new IllegalArgumentException("Invalid volume.");
            if (channel < 0 || channel >= channels.length)
                return;
            Channel chan  = channels[channel];
            chan.volLevel = volume;
            chan.volLeft  = (1.0f - chan.volPanning) * chan.volLevel;
            chan.volRight =         chan.volPanning  * chan.volLevel;
            chan.onVolume();
        }



        /////////////////////////// Private Methods ///////////////////////////

        // Retrieve an algorithm for playing an FM drum note
        private Algorithm getDrumFM(int key) {

            // Transform wave drum keys into FM drum keys
            if (key < 0)
                key += 35;

            // Error checking
            if (key < 0 || key >= algDrums.length)
                return null;

            // Select the preset algorithm
            return algDrums[key];
        }

        // Retrieve an algorithm for playing a wave drum note
        private Algorithm getDrumWave(int key) {

            // Error checking
            if (key < -24)
                return null;

            // Select the registered wave algorithm, if available
            Algorithm[] algs = algWaveDrums;
            Algorithm   ret  = null;
            if (key < 0) {
                algs = wavDrums;
                key += 24;
            }
            if (key >= 0 && key < algs.length)
                ret = algs[key];

            // Error checking
            if (ret != null && !ret.rm &&
                (wavRam == null || ret.ep >= wavRam.length)
            ) ret = null;

            return ret;
        }

        // Retrieve an algorithm for playing an FM instrument
        private Algorithm getFMInstrument(int bank, int program) {
            int       hashKey = bank << 8 | program;
            Algorithm ret     = null;

            // Running in 4-algorithm mode
            if (prgInstrumentType == FM_MA3_4OP)
                ret = fm4ops.get(hashKey);

            // Fallback to 2-algorithm mode
            if (ret == null)
                ret = fm2ops.get(hashKey);

            // Fallback to preset
            if (ret == null) {
                ret = algInstruments[bank < 2 ? 0 : // Apparent behavior
                    (bank & 1) << 6 | program & 0x3F];
            }

            return ret;
        }

        // Master volume has changed
        private void onVolume() {
            volOut = (1.0f - volFade) * volLevel;
            for (Channel chan : channels)
                chan.onVolume();
        }

        // Produce one input sample
        private void sample() {
            smpNext[0] = smpNext[1] = 0.0f;
            for (Channel chan : channels)
                chan.render();
            amPhase = (amPhase + 1) % 0x34000;
            vibPhase++;
        }

        // Specify FM algorithms
        private void setFMAlgorithms(byte[] message) {

            // Process all algorithms in the message
            for (int offset = 4; offset < message.length;) {

                // Algorithm type: 1=two-operator, 2=four-operator
                int type = message[offset] & 0xFF;
                if (type != 1 && type != 2)
                    break;

                // Error checking
                int size = type == 1 ? 20 : 34;
                if (offset + size > message.length)
                    break;

                // Decode the algorithm
                Algorithm algorithm;
                try { algorithm = new Algorithm(offset, message); }
                catch (Exception e) { break; }

                // Error checking
                if (type == 1 && algorithm.operators.length == 4)
                    continue;

                // Register the algorithm
                (type == 1 ? fm2ops : fm4ops).put(
                    (message[offset + 1] & 0xFF) << 8 | // Bank
                     message[offset + 2] & 0xFF,        // Program
                    algorithm
                );

                // Advance to the next algorithm
                offset += size;
            }

        }

        // Specify the global fade.
        private void setMasterFade(byte[] message) {
            if (message.length < 5)
                return;
            volFade = (message[4] & 0x7F) / 127.0f;
            onVolume();
        }

        // Decode and register wave drum definitions
        private void setWaveDrums(byte[] message) {

            // De-register existing wave drums
            for (int x = 0; x < wavDrums.length; x++)
                wavDrums[x] = null;

            // Decode wave drums
            int count = (message.length - 4) / 18;
            for (int x = 0, src = 4; x < count; x++, src += 18) {

                // Working variables
                Algorithm drum = new Algorithm(message, src + 1);

                // Error checking
                if (
                    drum.drumKey >= 24 && drum.drumKey <= 91 ||
                    drum.ep < drum.lp ||
                    drum.rm && (
                        drum.waveId == 7 ||
                        drum.ep > MA3_WAVEROM[drum.waveId].length
                    )
                ) continue;

                // Register the wave drum
                wavDrums[drum.drumKey] = drum;
            }

        }

        // Terminate any existing wave drum notes
        private void stopWaveDrums() {
            for (Channel chan : channels)
            for (Note note : chan.notesOut) {
                if (note.algorithm.isWave)
                    note.stop();
            }
        }

    }



    ////////////////////////////////// Note ///////////////////////////////////

    // Audio source
    private class Note {

        // OPL registers
        int block;    // Octave index
        int f_number; // Frequency divider

        // Instance fields
        int        amPhase;     // Amplitude modulator phase
        float      advance;     // Frequency advancement when dissociated
        Algorithm  algorithm;   // FM operator algorithm
        float      ampLeft;     // Effective left stereo amplitude
        float      ampRight;    // Effective right stereo amplitude
        Channel    channel;     // Encapsulating channel
        boolean    envDone;     // All operator envelopes are finished
        float      freqBase;    // Base frequency
        Instance   instance;    // Encapsulating instance
        int        key;         // Key index within channel
        Operator[] operators;   // OPL operators
        boolean    playing;     // Note is generating output
        float      sample;      // Current output sample
        float      volBase;     // Base volume
        float      volLeftOut;  // Left stereo output amplitude
        float      volRightOut; // Right stereo output amplitude



        //////////////////////////// Constructors /////////////////////////////

        private Note(Channel channel, int key, Algorithm algorithm) {

            // Instance fields
            this.algorithm = algorithm;
            envDone        = false;
            ampLeft        = 0.0f;
            ampRight       = 0.0f;
            this.channel   = channel;
            instance       = channel.instance;
            this.key       = key;
            operators      = new Operator[algorithm.operators.length];
            playing        = true;
            sample         = 0.0f;

            // Operators
            for (int x = 0; x < operators.length; x++)
                operators[x] = new Operator(this, algorithm.operators[x]);
        }



        /////////////////////////// Private Methods ///////////////////////////

        // Perform easing on an amplitude controller
        private float ease(float level, float target) {
            return
                level < target ? Math.min(target, level + instance.volRate) :
                level > target ? Math.max(target, level - instance.volRate) :
                level
            ;
        }

        // Key-off processing
        private void off() {

            // A data-supplied FM algorithm never decays
            if (algorithm.isForever) {
                stop();
                return;
            }

            // Ignore key-off for wave drums
            // Should apply to certain hi-hat notes, but needs research
            if (algorithm.isWave)
                return;

            // Regular processing: switch all operators to release stage
            for (Operator op : operators) {
                if (op.envStage == ENV_DONE || op.xof)
                    continue;
                op.envRate  = op.rr;
                op.envStage = ENV_RELEASE;
            }
        }

        // An envelope has finished
        private void onEnvelopeDone() {
            envDone = true;

            // Test all relevant operators
            int flags = algorithm.isWave ? 1 : ENV_FLAGS[algorithm.alg];
            for (int x = 0; x < operators.length; x++, flags >>= 1) {
                if ((flags & 1) != 0)
                    envDone = envDone && operators[x].envStage == ENV_DONE;
            }

            // If all relevant operators are done, shut off the note
            if (envDone)
                playing = false;
        }

        // Frequency has changed
        private void onFrequency(double bend) {

            // Wave notes don't use oscillators
            if (algorithm.isWave)
                return;

            // Compute BLOCK and F_NUMBER
            double freq = algorithm.isDrum ? freqBase : freqBase * bend;
            block       = Math.min(   7, Math.max(0, (int)
                (Math.round(Math.log(freq / 440) * MAGIC_B) + 57) / 12));
            f_number    = Math.min(1023, Math.max(0, (int)
                Math.round(freq * (1 << 20 - block) * MAGIC_F)));

            // Notify operators
            for (Operator op : operators)
                op.onFrequency();
        }

        // Master volume has changed
        private void onVolume() {
            volLeftOut  = volBase * algorithm.volLeft  * channel.volLeftOut;
            volRightOut = volBase * algorithm.volRight * channel.volRightOut;
        }

        // Render the next input sample
        private boolean render() {

            // Compute desired left and right volume levels
            float tgtLeft  = 0.0f;
            float tgtRight = 0.0f;
            if (!envDone) {
                tgtLeft  = volLeftOut;
                tgtRight = volRightOut;
            }

            // Generate the sample
            float sample = !algorithm.isWave ? sampleFM() :
                operators[0].sample(0, false) / 32768.0f;
            instance.smpNext[0] += sample * ampLeft;
            instance.smpNext[1] += sample * ampRight;

            // Adjust stereo levels
            ampLeft  = ease(ampLeft , tgtLeft );
            ampRight = ease(ampRight, tgtRight);

            // Indicate whether the note has finished generating output
            return !playing && ampLeft == 0 && ampRight == 0;
        }

        // Generate an FM sample
        private float sampleFM() {
            int out1, out2, out3, out4;
            int ret = 0;
            switch (algorithm.alg) {
                case 0:
                    out1 = operators[0].sample(0, true);
                    out2 = operators[1].sample(out1, false);
                    ret  = out2;
                    break;
                case 1:
                    out1 = operators[0].sample(0, true);
                    out2 = operators[1].sample(0, false);
                    ret  = out1 + out2;
                    break;
                case 2:
                    out1 = operators[0].sample(0, true);
                    out2 = operators[1].sample(0, false);
                    out3 = operators[2].sample(0, true);
                    out4 = operators[3].sample(0, false);
                    ret  = out1 + out2 + out3 + out4;
                    break;
                case 3:
                    out1 = operators[0].sample(0, true);
                    out2 = operators[1].sample(0, false);
                    out3 = operators[2].sample(out2, false);
                    out4 = operators[3].sample(out1 + out3, false);
                    ret  = out4;
                    break;
                case 4:
                    out1 = operators[0].sample(0, true);
                    out2 = operators[1].sample(out1, false);
                    out3 = operators[2].sample(out2, false);
                    out4 = operators[3].sample(out3, false);
                    ret  = out4;
                    break;
                case 5:
                    out1 = operators[0].sample(0, true);
                    out2 = operators[1].sample(out1, false);
                    out3 = operators[2].sample(0, true);
                    out4 = operators[3].sample(out3, false);
                    ret  = out2 + out4;
                    break;
                case 6:
                    out1 = operators[0].sample(0, true);
                    out2 = operators[1].sample(0, false);
                    out3 = operators[2].sample(out2, false);
                    out4 = operators[3].sample(out3, false);
                    ret  = out1 + out4;
                    break;
                case 7:
                    out1 = operators[0].sample(0, true);
                    out2 = operators[1].sample(0, false);
                    out3 = operators[2].sample(out2, false);
                    out4 = operators[3].sample(0, false);
                    ret  = out1 + out3 + out4;
                    break;
            }
            return ret / 8170.0f; // Twice the max sample value
        }

        // Terminate playback
        private void stop() {
            envDone = true;
            playing = false;
            volBase = 0.0f;
            for (Operator op : operators) {
                op.envLevel = 511;
                op.envStage = ENV_DONE;
            }
        }

    }



    //////////////////////////////// Operator /////////////////////////////////

    // Individual FM algorithm operator
    private static class Operator {

        // OPL registers
        int     ar;    // Envelope attack rate
        int     dam;   // Amplitude modulation depth
        int     dr;    // Envelope decay rate
        int     dt;    // Detune shift
        int     dvb;   // Frequency modulation depth
        boolean eam;   // Enable amplutide modulation
        boolean evb;   // Enable frequency modulation
        int     fb;    // Feedback rate index
        int     ksl;   // Attenuation index per octave
        int     ksr;   // Envelope rate modifier scale
        int     multi; // Frequency multiplier
        int     rr;    // Envelope release rate
        int     sl;    // Envelope sustain level
        int     sr;    // Envelope sustain rate
        boolean sus;   // MIDI Hold 1 is supported
        int     tl;    // Envelope attenuation
        int     ws;    // Wave function index
        boolean xof;   // Ignore key-off response

        // Instance fields
        Algorithm algorithm; //     Encapsulating algorithm
        int       amPhase;   // u14 Amplitude modulation counter
        int       envLevel;  // u9  Current envelope level
        int       envOut;    // u9  Effective envelope output
        int       envPhase;  // u15 Envelope phase counter
        int       envRate;   //     Current envelope rate of change
        int       envRof;    //     Envelope rate offset modifier
        int       envStage;  //     Envelope processing stage
        int       fb0;       //     Most recent output sample
        int       fb1;       //     Second-most recent output sample
        Instance  instance;  //     Encapsulating instance
        boolean   isValid;   //     Wave drum parameters are valid
        int       kslOut;    //     KSL attenuation level
        Note      note;      //     Encapsulating note
        int       oscPhase;  // u10 Oscillator counter
        float     wavSample; //     Current wave source sample



        //////////////////////////// Constructors /////////////////////////////

        // Template constructor
        private Operator(byte[] bytes, int offset) {
            sus   =(bytes[offset + 0] >> 3 &  1) != 0;
            ksr   = bytes[offset + 0] >> 2 &  1;
            eam   =(bytes[offset + 0] >> 1 &  1) != 0;
            evb   =(bytes[offset + 0]      &  1) != 0;
            multi = bytes[offset + 1] >> 4 & 15;
            dt    = bytes[offset + 1] >> 1 &  7;
            xof   =(bytes[offset + 1]      &  1) != 0;
            ar    = bytes[offset + 2] >> 4 & 15;
            dr    = bytes[offset + 2]      & 15;
            sr    = bytes[offset + 3] >> 4 & 15;
            rr    = bytes[offset + 3]      & 15;
            sl    = bytes[offset + 4] >> 4 & 15;
            dam   = bytes[offset + 4] >> 2 &  3;
            dvb   = bytes[offset + 4]      &  3;
            tl    = bytes[offset + 5] >> 2 & 63;
            ksl   = bytes[offset + 5]      &  3;
            fb    = bytes[offset + 6] >> 5 &  7;
            ws    = bytes[offset + 6]      & 31;
        }

        // Wave constructor
        private Operator(int offset, byte[] message) {
            int bits;
            bits = message[offset++] & 0xFF;
            sr   = bits >> 4 & 15;
            xof  =(bits >> 3 &  1) != 0;
            sus  =(bits >> 1 &  1) != 0;
            bits = message[offset++] & 0xFF;
            rr   = bits >> 4 & 15;
            dr   = bits      & 15;
            bits = message[offset++] & 0xFF;
            ar   = bits >> 4 & 15;
            sl   = bits      & 15;
            bits = message[offset++] & 0xFF;
            tl   = bits >> 2 & 63;
            bits = message[offset++] & 0xFF;
            dam  = bits >> 5 &  3;
            eam  =(bits >> 4 &  1) != 0;
            dvb  = bits >> 1 &  3;
            evb  =(bits      &  1) != 0;
        }

        // SysEx constructor
        private Operator(byte[] message, int offset, boolean diff) {
            int bits;
            bits  = message[offset++] & 0xFF;
            sr    = bits >> 4 & 15;
            xof   =(bits >> 3 &  1) != 0;
            sus   =(bits >> 1 &  1) != 0;
            ksr   = bits      &  1;
            bits  = message[offset++] & 0xFF;
            rr    = bits >> 4 & 15;
            dr    = bits      & 15;
            bits  = message[offset++] & 0xFF;
            ar    = bits >> 4 & 15;
            sl    = bits      & 15;
            bits  = message[offset++] & 0xFF;
            tl    = bits >> 2 & 63;
            ksl   = bits      &  3;
            bits  = message[offset++] & 0xFF;
            dam   = bits >> 5 &  3;
            eam   =(bits >> 4 &  1) != 0;
            dvb   = bits >> 1 &  3;
            evb   =(bits      &  1) != 0;
            bits  = message[offset++] & 0xFF;
            multi = bits >> 4 & 15;
            dt    = bits      &  7;
            bits  = message[offset++] & 0xFF;
            ws    = bits >> 3 & 31;
            fb    = bits      &  7;
        }

        // Playback constructor
        private Operator(Note note, Operator o) {

            // OPL registers
            ar    = o.ar;
            dam   = o.dam;
            dr    = o.dr;
            dt    = o.dt;
            dvb   = o.dvb;
            eam   = o.eam;
            evb   = o.evb;
            fb    = o.fb;
            ksl   = o.ksl;
            ksr   = o.ksr;
            multi = o.multi;
            rr    = o.rr;
            sl    = o.sl;
            sr    = o.sr;
            sus   = o.sus;
            tl    = o.tl;
            ws    = o.ws;
            xof   = o.xof;

            // Instance fields
            algorithm = note.algorithm;
            amPhase   = note.instance.amPhase;
            envLevel  = 511;
            envPhase  = 0;
            envRate   = ar;
            envStage  = ENV_ATTACK;
            instance  = note.instance;
            this.note = note;
            oscPhase  = 0;
            wavSample = 0;
        }



        /////////////////////////// Private Methods ///////////////////////////

        // Frequency has changed
        private void onFrequency() {
            envRof = (note.block << 1 | note.f_number >> 8 + NTS & 1) >>
                ((ksr ^ 1) << 1);
            kslOut = Math.max(0, KSL_B[ksl] *
                ((note.block << 3) - KSL_F[note.f_number >> 6]));
        }

        // Generate a sample on an operator
        private int sample(int mod, boolean feedback) {
            int x, y; // Scratch

            // The envelope has finished
            if (envStage == ENV_DONE)
                return 0;

            // FM sample
            if (!algorithm.isWave) {
                if (feedback && fb != 0)
                    mod += fb0 + fb1 >> 9 - fb;
                fb1 = fb0;
                x   = WAVES[ws][(oscPhase >> 9) + mod & 1023] + (envOut << 3);
                fb0 = EXP[x & 0xFF] << 1 >> (x >> 8 & 31) ^ x >> 31;
            }

            // Wave sample
            else {
                int[] samples = !algorithm.rm ? instance.wavRam :
                    MA3_WAVEROM[algorithm.waveId];

                // Select the sample from wave memory
                if (samples != null && wavSample < algorithm.ep) {

                    // Produce the output sample
                    x   = (int) Math.floor(wavSample);
                    fb0 = samples[x] * WAVE_ENV[envOut] / 32767;

                    // Advance to the next sample
                    wavSample += algorithm.wavAdvance;
                    if (wavSample >= algorithm.ep) {
                        if (algorithm.lp < algorithm.ep) {
                            wavSample = (wavSample - algorithm.lp) %
                                (algorithm.ep - algorithm.lp) + algorithm.lp;
                        } else {
                            wavSample = algorithm.ep;
                            note.stop();
                        }
                    }

                }

                // Do not select a sample from wave memory
                else fb0 = 0;
            }

            // Advance the envelope
            x = envRate == 0 ? 0 : Math.min(63, (envRate << 2) + envRof);
            envPhase += envRate == 0 ? 0 : (4 | x & 3) << (x >> 2);
            y = envPhase >> 15;
            envPhase &= 0x7FFF;
            switch (envStage) {
                case ENV_ATTACK:
                    if (y == 0)
                        break;
                    envLevel += ~(envLevel * y >> 3);
                    if (envLevel <= 0) {
                        envLevel = 0;
                        envRate  = dr;
                        envStage = ENV_DECAY;
                    }
                    break;
                case ENV_DECAY:
                case ENV_SUSTAIN:
                case ENV_RELEASE:
                    envLevel += y;
                    if (envStage == ENV_DECAY && envLevel >= SUSTAINS[sl]) {
                        envLevel = SUSTAINS[sl];
                        envRate  = sr;
                        envStage = ENV_SUSTAIN;
                    }
                    if (envLevel >= 511) {
                        envLevel = 511;
                        envStage = ENV_DONE;
                        note.onEnvelopeDone();
                    }
                    break;
                case ENV_DONE:
                    envLevel = 511;
                    break;
            }

            // Attenuate the envelope output
            envOut = envLevel + kslOut + (tl << 2);
            if (eam) {
                envOut += AM_LFO_A[amPhase >> 12] << dam >> 2;
                amPhase = (amPhase + AM_LFO_B[algorithm.lfo]) % (0x34000);
            }
            envOut = Math.min(Math.max(envOut, 0), 511);

            // Wave drums have no oscillator
            if (algorithm.isWave)
                return fb0;

            // Advance the oscillator
            oscPhase += (note.f_number<<note.block>>1) * MULTIS[multi] >> 1;

            // According to available resources, the below algorithm should be
            // correct for vibrato, but no significance has been observed and
            // the output from ATS-MA3-N is no different. It has been disabled
            // pending further reserach. A real MA-3 may be needed.
            //
            // The DVB settings in the MA-2 algorithms are as defined in
            // ATS-MA2-N, with two bits, although the OPL register only uses
            // one bit for DVB. The MA-2 presets may need to be adjusted once
            // the vibrato thing is pinned down.
            //
            // if (evb) {
            //     oscPhase += instance.vibPhase << 19 >> 31 ^ note.f_number >>
            //        (9 - dvb + ((instance.vibPhase >> 10 & 3) == 3 ? 1 : 0));
            // }

            return fb0;
        }

    }



    ///////////////////////////////// Assets //////////////////////////////////

    // Instrument algorithms for MA-2
    private static Algorithm[] MA2_INSTRUMENTS = Algorithm.from(new String[] {
        "AXgAABDyBUo6gAAQ8gZ6AIA=", // GrandPno
        "AXgABBDyBFo+oAAQ8gZaAKA=", // BritePno
        "AXgABBDxBVoZgAAQ8gZ6AIA=", // E.GrandP
        "AXgABhDyCzo4YAEQ8wv6AGA=", // HnkyTonk
        "AXgAABDxiDpNwAAQ8QgqAMA=", // E.Piano1
        "AXgAADDxiDpmgAAQ8QgqAIA=", // E.Piano2
        "AXgAABCiAQoCgABA9QXaAIA=", // Harpsi
        "AXgAABDCiKo6oAAQwohaAKA=", // Clavi
        "AXgAAMD2RFpxAAIQ81W6AAA=", // Celesta
        "AXgAAHD2IjpqQAQQ9TMaAkA=", // Glocken
        "AXgAAHCWRAp0IAAQ8jMKACA=", // MusicBox
        "AXgABLDzZuqlAAIQ8kT6AAA=", // Vibes
        "AXgABID2VVqUAAAQ5QbaAAA=", // Marimba
        "AXgABFD2ZqpEYAAQ9mbqAGA=", // Xylophon
        "AXgAAZDSIopcoAIQojPiAqA=", // TubulBel
        "AXgAADB0VVoVQQIQ81UKAkA=", // Dulcimer
        "AXgABRD2BBpCIAYQ8QcGACA=", // DrawOrgn
        "AXgABUDHCFo4IAQQxwgCACA=", // PercOrgn
        "AXgABRCqCBoRYAYgiggCAGA=", // RockOrgn
        "AXgAADCXAiomQQQQRQMaAEA=", // ChrchOrg
        "AXgAARB3BApAAQQQRQQKAgA=", // ReedOrgn
        "AXgAAECYCiohwQYQRgoaAMA=", // Acordion
        "AXgAATCRBgqAoQAQYQcKAKA=", // Harmnica
        "AXgAABBxBgpMYAIQYQcCJmA=", // TangoAcd
        "AXgAACDzRJpewQEQ84jKAsA=", // NylonGtr
        "AXgAADD4IhpE4QQQ+zMqAOA=", // SteelGtr
        "AXgAADDxCjp94AAQ8gj6AOA=", // JazzGtr
        "AXgAADD3MyoeYQAQkghqAmA=", // CleanGtr
        "AXgAADD5RFo5AAAQhgo6AAA=", // Mute.Gtr
        "AXgAADCVCRohgQAQhAkaAIA=", // Ovrdrive
        "AXgAADDVCRo04QAQ9AkaAOA=", // Dist.Gtr
        "AXgAAJAg/0qygABA0Yj6AoA=", // GtrHarmo
        "AXgAABCUBgpcoAIgwwaqAKA=", // Aco.Bass
        "AXgABBDxCCpAgAQQ8QgaAIA=", // FngrBass
        "AXgABBDxCOoegAQQoQh6AIA=", // PickBass
        "AXgABBBhCCpdwAQg4QhKAMA=", // Fretless
        "AXgAABDhqpoegAAQ8g/aAYA=", // SlapBas1
        "AXgAABCRqpoigAAQ8g/aAYA=", // SlapBas2
        "AXgABBD0COoqoAQQ8Qh6AKA=", // SynBass1
        "AXgABBDxCCpIoAQQ8QgaAKA=", // SynBass2
        "AXgABBDdAxo4oQEQVgYgAKA=", // Violin
        "AngABhDdAxJIoQAQdgYAAKA=", // Viola
        "AngABhDRDBAlgQQQYQwAAIA=", // Cello
        "AXgAABBxAholIQAwcgYKAiA=", // Contrabs
        "AngABBDxASoBIQIQbwYaACA=", // Trem.Str
        "AXgAACCVVXqIAwAQ9VU6AgA=", // Pizz.Str
        "AXgABCDxVXp0AQQQ8UT6AgA=", // Harp
        "AXgABBDxMwodIQQg8jPKACA=", // Timpani
        "AXgAABCxBBJuAAEgcgcLBAQ=", // Strings1
        "AXgAAhB/AwpOAQAQbwcKAAE=", // Strings2
        "AXgAAhBhAhBCQAEQbwUAAEA=", // Syn.Str1
        "AXgAABCBAipwwAEQbwUKAMA=", // Syn.Str2
        "AXgABBD0BRpdwAUgagUKAsA=", // ChoirAah
        "AXgAAhB0CTopAAEQoQdqAAA=", // VoiceOoh
        "AXgABRA0BQpRwAUgagUKAMA=", // SynVoice
        "AXgABhBUM2oAgAEgpVVKAIY=", // Orch.Hit
        "AXgAABCVBxpGwAAQ7wkKAMA=", // Trumpet
        "AXgAABCFBxpOwAAQjwkKAMA=", // Trombone
        "AngAABCGBRpCoAEQsgc7AKA=", // Tuba
        "AXgABHCeBxoVIQAQYgwqACE=", // Mute.Trp
        "AXgAABBhCmpmwAAQfwoKAMA=", // Fr.Horn
        "AXgAARB1DxoigAAgdA8KAIA=", // BrasSect
        "AXgAABDUBEIuQQAQkQcaAEA=", // SynBras1
        "AXgAABBlAXouAAAQxgcaAAA=", // SynBras2
        "AXgABBCTAgpKoQEQcgsKAKA=", // SprnoSax
        "AXgABBCTAwoygQEQcgkKAIA=", // AltoSax
        "AXgABBBzAwoWYQEQcgkKAGA=", // TenorSax
        "AXgABBCTDwoGYQEQcg8KAGA=", // Bari.Sax
        "AngAAhDRBCJyAAEw0gcKAAE=", // Oboe
        "AXgABBB+AhJCYAEQywgAAGU=", // Eng.Horn
        "AngABRDHBRNeAQcwcQgTAAE=", // Bassoon
        "AXgABCCbASpCQAAQcgcaAEA=", // Clarinet
        "AXgAAxClD1pcAAMQZQoaAAA=", // Piccolo
        "AngAABCYD1AZAAIQZQoYAAA=", // Flute
        "AngAAkB1DxKiIAIQdQoCACA=", // Recorder
        "AXgABEDECFqUAAAQdQo6AAA=", // PanFlute
        "AXgAAxBmBlApAAIQZQYmAAA=", // Bottle
        "AngAABBYBoocwAIQVAY2AMA=", // Shakhchi
        "AXkAASBXBwpcoAIQWAcKAKA=", // Whistle
        "AXkAAhB3BwIQYAIQdgcCAGA=", // Ocarina
        "AXgAACD/AwplAgAQ/wgKAAA=", // SquareLd
        "AXgAABD/BQo4AQAQ/wgKAAE=", // Saw.Lead
        "AXgAACCGBVoZAAAQZAgaAgA=", // CaliopLd
        "AXgAABBmAhoVAAIQlggCAAA=", // ChiffLd
        "AXgAABCSBiouAQAgkQgqAAA=", // CharanLd
        "AXgAAjBRBRITIAEQjwcKAiA=", // VoiceLd
        "AXkAADCvBQotpAEQvwYKAKQ=", // FifthLd
        "AXgAABDxCRo+oAAQ9AkKAqA=", // Bass&Ld
        "AXkABSDxBGgCAgAQMAUGPAQ=", // NewAgePd
        "AXgAARC4AVA4YAQQFQMCAGA=", // WarmPad
        "AXgAARCRBDpcwAEQhQYaAMA=", // PolySyPd
        "AXgABBBUAQBoAAQgagMAAAA=", // ChoirPad
        "AXgAAHAhA0pqgAIQMgUwAIA=", // BowedPad
        "AXgAAhChB3JAgQAQQQdKAIE=", // MetalPad
        "AXgAAhAxAzAyYAAQYgUgAGA=", // HaloPad
        "AXgAAhARAUJUAQAQLwYKAAA=", // SweepPad
        "AXgABKD4BvoPIAUQhiIKACA=", // Rain
        "AXkAACBBAyosBAAQQQMaAAU=", // SoundTrk
        "AXgAAGB0VZoboAAQoiJ6AKA=", // Crystal
        "AXgAADDxAYpWwAEQ8gYqAsA=", // Atmosphr
        "AXgAARDxEVoaAQEg8lX6AAA=", // Bright
        "AXgAARARAVBSYQIwEQMQAmU=", // Goblins
        "AXgAABAxATAyYAIQ3QMBAmA=", // Echoes
        "AXgAAGBTA3AxQQAQYQQgAEA=", // Sci-Fi
        "AXgAACDSM1oWYABw8mb6AGE=", // Sitar
        "AXgABBCjERowAQQwolXqAgA=", // Banjo
        "AXgAABD3RHoIoQAQ9WbqAKI=", // Shamisen
        "AXgAADDVRDBGgAAQ5EQQAIE=", // Koto
        "AXgAAED6ZloVwAAQwlUKAMA=", // Kalimba
        "AXgAABB8ACoVoAAgbwwKAKE=", // Bagpipe
        "AXgABBDdAzoWoQAQVgYaAKA=", // Fiddle
        "AXgAAADaBQoQAgAQjwsKBgA=", // Shanai
        "AXgAAIDxVeqdYABQw1XqAmA=", // TnklBell
        "AXgAAHDsZipUoAAg+GYaAKA=", // Agogo
        "AXgAAFBnVTp2gAAQ31UKAIA=", // SteelDrm
        "AXgABID6iCpaoAQg+FXqAKA=", // WoodBlok
        "AXgAAACwdwAKYQAA/TMKAGA=", // TaikoDrm
        "AXgAABD4VUoFQgAA9mYKAEE=", // MelodTom
        "AXgAABDxZgpI4gQA8yIKAOA=", // Syn.Drum
        "AXgAAOAfAAoA4AMAH//6AOM=", // RevCymbl
        "AXgAAGD4RCoC4AAwVkSKIuI=", // FretNoiz
        "AXgAAOD4AAos4AcANEQKFOM=", // BrthNoiz
        "AHgAAOD2AAAA4AMAHyIAAOM=", // Seashore
        "AXgAB1A3M6pWAAegVnc6AQA=", // Tweet
        "AXgABFCyAGpxogRA9DMaAKA=", // Telphone
        "A3gAA/D2AAoA5gcATwUKAOc=", // Helicptr
        "A3gAAGD/AQAA4ANAUgcTAOE=", // Applause
        "AXgAAVDzAPAQ4APw9oiwAOY="  // Gunshot
    }, false, false);

    // Drum algorithms for MA-2
    private static Algorithm[] MA2_DRUMS = Algorithm.from(new String[] {
        "AXhPADD3APoMwQQwqP/6AMY=", // SeqClick H
        "AXg9AAD4ADoA4AQQmkRaIOA=", // Brush Tap
        "AXgzAUBoBgoU4AAAWERKXOA=", // Brush Swirl L
        "AXg4AAD4ADoA4AQwmkRaIOA=", // Brush Slap
        "AXg9AdBoBgoU4AEAWERKSOA=", // Brash Swirl H
        "AXgvAfBoAwoU4AEAWFVqAOA=", // Snare Roll
        "AXhMAHD3APoMwQRQqP/6AMY=", // Castanet
        "AXg8AAD8ABok4gAA93c6AOA=", // Snare L
        "AXhFACD5zPoBYAAQ+4i6AGA=", // Sticks
        "AXg3BBCn3Ro0pQQAcFUKAKA=", // Bass Drum L
        "AXgyAGD4ADoM4gAgmmY6AOY=", // Open Rim Shot
        "AXghABD/d3oAoAQA82YKAKA=", // Bass Drum M
        "AXgnBAC4d3oQggQQ8HcKAIA=", // Bass Drum H
        "AXg+AHD3APoAwQRAqP/6AMY=", // Closed Rim Shot
        "AXhBAACrAAo04gAAqHc6AOY=", // Snare M
        "AXhZAPDwDwoAoATw82YaEKc=", // Hand Clap
        "AXhEAACrAAow4gAAqHc6AOY=", // Snare H
        "AXgOAMD6ZvokYABQ9FV6GGA=", // Floor Tom L
        "AXg+AJDYiAoAoAQQqHdqAKI=", // Hi-Hat Closed
        "AXgOAMD6Zvo8gABg9FV6AIA=", // Floor Tom H
        "AXg+AMD3iAoAoARweHdKBKI=", // Hi-Hat Pedal
        "AXgOAMD5mfo8gACA9GZ6AIA=", // Low Tom
        "AXhDAMD2IgoAoAQgyzNKAKI=", // Hi-Hat Open
        "AXgOAMD6VfosgACg9Yh6AIA=", // Mid Tom L
        "AXgOAMD6mfo8gADA9WZ6AIA=", // Mid Tom H
        "AHhRAOD2EQoA5wTwnyIAAOY=", // Crash Cymbal 1
        "AXgOAND6mfo8gADw9WZ6AIA=", // High Tom
        "AXhbAPD3AEoCpgBw9ETqUKY=", // Ride Cymbal 1
        "AXhiALD1MzoA5QcAcCIAKOY=", // Chinese Cymbal
        "AXhgAODkROooYwBw9VXqAGE=", // Ride Cymbal Cup
        "AXhcAPD2AAoAogQgy0RKAKM=", // Tambourine
        "AHhrAODxAApE4ATwkTMAFOY=", // Splash Cymbal
        "AXhRBBD5d0oA4gQA81UKEOA=", // Cowbell
        "AXhqAOD2AAoG4AfwkSLgBOY=", // Crash Cymbal 2
        "AXgYABDwEQoI5QQQ/0QqAOA=", // Vibraslap
        "AXhaAPD3AEoCpgBw9ETqVKY=", // Ride Cymbal 2
        "AXhDAGD1zAoAYAAg9YgKAGA=", // Bongo H
        "AXk0ABD6RLoAYAAgyGaaAGA=", // Bongo L
        "AXhBABD5u4oNYAAQ+3e6AGY=", // Conga H Mute
        "AXg0ABD63Yo1YAAg+Ii6AGA=", // Conga H Open
        "AXgtABD2/4poYQAg9jPAAGA=", // Conga L
        "AXhRABD5qgoA4wAA+WYKAOA=", // Timbale H
        "AXg5AAD5iIou4QAA9GaqAOA=", // Timbale L
        "AXhRAHDsZipUoAAg+GYaAKA=", // Agogo H
        "AXhMAHDsZipUoAAg+GYaAKA=", // Agogo L
        "AXhiAPCl/0oA4ADwhHcKEOI=", // Cabasa
        "AXhoAKDmiEoA4gAwiGZKAOE=", // Maracas
        "A3g4DIBiACBc4wCAXAcwCOA=", // Samba Whistle H
        "A3gzDIBiACBc4wCAXAcwCOA=", // Samba Whistle L
        "AXhTAhD2dwoA4wRAZ4g6AOI=", // Guiro Short
        "AXggAADxRAAQ4gTwQ2YQJOM=", // Guiro Long
        "AXlFAGAARPr8AARQ91X6AAA=", // Claves
        "AXhEAGAARPr8AwQg91X6AAA=", // Wood Block H
        "AXk/AGAARPr8AAQg91X6AAA=", // Wood Block L
        "AXhRABBVd+pqAAAgZVUKAAA=", // Cuica Mute
        "AXg8ARD4iEolAAEggFUKIAA=", // Cuica Open
        "AXhQAKDgAPptgwTg/1UKAYA=", // Triangle Mute
        "AXhAALDgAPptgwTg/zMKSYA=", // Triangle Open
        "AXhfAPDyZloA5ADwmIhqAOc=", // Shaker
        "A3g1APD0AAAOwwbwh0QwAMQ=", // Jingle Bell
        "AXgyAOA0VeoAYwCQRjMKGWE="  // Belltree
    }, true, false);

    // FM instrument algorithms for MA-3, 2 operators
    private static Algorithm[] MA3_INSTRUMENTS_2OP=Algorithm.from(new String[]{
        "AXgADBD0IyhCZQ0Q8hY4AgA=", // GrandPno
        "AXgADBD0IzhBjQ0Q8hY4EAA=", // BritePno
        "AXgADBDxFTpkwA0Q8SZoIAA=", // E.GrandP
        "A3gACx7zJURBJA4W8zdQAAA=", // HnkyTonk
        "AXgACBDhBFRNoAkQ0ihkAAA=", // E.Piano1
        "AXgACEb9NHgdogkSszj0CAA=", // E.Piano2
        "AXgADDDDHxpRSgkQ0zf4HBU=", // Harpsi
        "AXgADDDyKBmAZQ0Q8iooHAQ=", // Clavi
        "A3gADMD1IcOpAAkQ9UXwCAA=", // Celesta
        "AXkADHD1JWoIAA0Q9CIYAQA=", // Glocken
        "AXgADHCaJFqgKAkQ8yT4AgA=", // MusicBox
        "AXgADXD1IxBZIAoQ8ib0AgA=", // Vibes
        "AXgADFD6VUpYQAwQ4yTaDAA=", // Marimba
        "AXgADHC7cFpIhggQ9kfqAAA=", // Xylophon
        "AXgADHL7EQo60AoQ+TMCMAg=", // TubulBel
        "AXgACD7UNVo+dAkQ8jMoHgA=", // Dulcimer
        "AXkACQDQCgg4JAkg8QkQHAk=", // DrawOrgn
        "AXgACUDJCVB8AAkAoAgAAAA=", // PercOrgn
        "AngACgD7ChBocQsg+gsAFAA=", // RockOrgn
        "AXgACVCnAyg8UQ0QRQQZKAA=", // ChrchOrg
        "AXgACRB3BApAAQwQRAgKFgA=", // ReedOrgn
        "AXgACB6YCgpoJA0mUAoQHBA=", // Acordion
        "AXgACBCRAwpwhQkwYQkIIAA=", // Harmnica
        "AXkACiZhCDEUTAseYQgQNBk=", // TangoAcd
        "AXgADCDzIyuU6A0Q8xgoCAg=", // NylonGtr
        "AXgADTD0FHiQ9Q0Q8hZYCAg=", // SteelGtr
        "AXgADTC2Fihp4AkQwin6AAA=", // JazzGtr
        "AXgACTT3MyAecQkQ8zlADgA=", // CleanGtr
        "AXgACACpJzlGyg0g0ihYGAA=", // Mute.Gtr
        "AngACBDYCSglZAkg1BkYEAQ=", // Ovrdrive
        "AXgACBB4AjkkoQgg0Qk6HgY=", // Dist.Gtr
        "AXgACQCShwhFYAkgs1n4IAg=", // GtrHarmo
        "AXgACBC0EhgTQAkQ1DloAAA=", // Aco.Bass
        "AngADRD5GBhKqA0gdChaAAA=", // FngrBass
        "AngADRCpGCg6qA1A9ChKAAA=", // PickBass
        "AngADRKyGBpgoA1Asij4CAA=", // Fretless
        "AngACTD4KjowaAkQ9T9KEAg=", // SlapBas1
        "AngACUD4Kjg4YAkQ9D9IEgg=", // SlapBas2
        "AXgADRDzEiguoAwQ8RdICAA=", // SynBass1
        "AXgACRD2BCgyJQkS0wlIAAg=", // SynBass2
        "AngACBDVAzhEdAkQVgcAEAA=", // Violin
        "AngACBClARBAgQkgZQcAGAA=", // Viola
        "AngACRBkBBBwpQkQYQYBABA=", // Cello
        "AngACBCRBRhJVQkQkwcIAAg=", // Contrabs
        "A3gADBDxARodlQoQbwYWEAA=", // Trem.Str
        "AXkADBB2VSoAbAgQ1XcqAAQ=", // Pizz.Str
        "AXgABCD1MjiIwQUQ8jL4AAA=", // Harp
        "AXhAACD7VApQ0AIA2UQCAAg=", // Timpani
        "AngACBCxARJuCAsgcgYAEBA=", // Strings1
        "AngACBCxARJaCAsgYgYAEBA=", // Strings2
        "AXgACBqxARJ+KAsgYgUAEBQ=", // Syn.Str1
        "AngACBCBAiqAzQsQbwQAAgg=", // Syn.Str2
        "AXkACRBlBQUYiAkgWgUAJBA=", // ChoirAah
        "AngACRBWAQiUAAkgZQYpAAA=", // VoiceOoh
        "AngACRCCEhFSoAlAYxUQEAA=", // SynVoice
        "AXgABhBURGoAgAEgpWZIGAY=", // Orch.Hit
        "AngACRCVABhCwAgQ7wkIGAA=", // Trumpet
        "AXgACBCFABhOwAkQcBkJAAA=", // Trombone
        "AngACRCmABg6oAkQYgc7AAA=", // Tuba
        "AXgACVB4ABheogkQgwoJIAU=", // Mute.Trp
        "AngACBBiADFWgAkQ0gcEAAA=", // Fr.Horn
        "AngACRB1ABgqqAgQvAgIFAA=", // BrasSect
        "AngACRKEADhQwQgQoAkIFAg=", // SynBras1
        "AngACBBkAChayAkQowYoAAA=", // SynBras2
        "AXgADBCTAApWqQkQggsIFAA=", // SprnoSax
        "AXgADBCTAAo2iQkQggkIGAA=", // AltoSax
        "AXgADBBzAAoWYQkQcgkJHAA=", // TenorSax
        "AXgADBCTAAoqaAsQgg8BDAA=", // Bari.Sax
        "AngAChDRACKCCAkwgggIDAE=", // Oboe
        "AngADBCeADBiaAkQqwgAAAU=", // Eng.Horn
        "AngADBDHABBeAQ8wcQgQAAk=", // Bassoon
        "AngACCCQAAqEQAkQgAgEAAA=", // Clarinet
        "AnkACxCJC/YMcgoQgwwUAAA=", // Piccolo
        "AngACRCIBVAdEQsQZQcFAAA=", // Flute
        "AngACiCWegDMAwsQhQoBEAg=", // Recorder
        "AXgACyDGAEEPwgsQeAgQDAg=", // PanFlute
        "AXgACxBnBqB04woQsAcBAAA=", // Bottle
        "AXgADhD1Rogk5AsQYAkABAA=", // Shakhchi
        "AXkACxD3CABwtAsQiAcAAAA=", // Whistle
        "AXkACzCKF6B5YAoQhgkAAAA=", // Ocarina
        "AXkACRjfCQk1BgkQ3wkIBAg=", // SquareLd
        "AXkACR7wCAgoGAkQ8AgKXBs=", // Saw.Lead
        "AXgACCrHBagsAwsAkAgEABI=", // CaliopLd
        "AXgAChh3AmAhCAsQ9ggADAg=", // ChiffLd
        "AXgACBiSCxgiAQkgkQogJAA=", // CharanLd
        "AngACDphBRAjMAsQ3wcAGAA=", // VoiceLd
        "AXkACTCvBwgNpAkQvwcJCAQ=", // FifthLd
        "AXgACBjUCEIpDQkQ9AgAGBA=", // Bass&Ld
        "AXkADELyBGgKCgsQYAQAABQ=", // NewAgePd
        "AXkACRpQBQoAFA8QUAMMAhA=", // WarmPad
        "AXgAABrRBDBByAMQxQYQDAA=", // PolySyPd
        "AXgADhBDAQCI4A0gegMABgg=", // ChoirPad
        "AXgACnAzA2DOyAkQYgUxAAk=", // BowedPad
        "AXkAAxpShfEAdAkQUQUAABA=", // MetalPad
        "AXkACxpoBTUATAsQcgUkABQ=", // HaloPad
        "AXgACxARAUJoAQMQTwQCDAg=", // SweepPad
        "AXgADKD4BooLIA8QhRIAABA=", // Rain
        "AXkACTJTBJJADAsSUAMCEAk=", // SoundTrk
        "AXgACGD4FUoxoAEQ9EToFAA=", // Crystal
        "AHgAADD4M1JqyAsQ8xRQCAg=", // Atmosphr
        "AXkACxD0FFIACwMU8lTwAAk=", // Bright
        "A3gAChARA1BGaAkQIQMQBgA=", // Goblins
        "AXgACBAxAWBeQAsQ/QMAAgg=", // Echoes
        "AXgAASBUo/BlQQsQYQQhDAQ=", // Sci-Fi
        "AXgAACDSM1oiYAlw8iX4HAE=", // Sitar
        "AXgABBDTERowAQ0w00XoAgA=", // Banjo
        "AXgAABD3RHpArQgQ9UbqAAI=", // Shamisen
        "AXgAADDVRDBGgAEQ5EQQCAE=", // Koto
        "AXgAAED6d1oRwAQQwTMKFAA=", // Kalimba
        "AXgACBB8AipYoAtAbwcALAg=", // Bagpipe
        "AngADBDdADoWwAkQVggYFAA=", // Fiddle
        "AXgACADaAAoQAgkQvwsIJgA=", // Shanai
        "AXkACzr0RUIBrAsQ9UVAABU=", // TnklBell
        "A3gAAHDsZipUqAEg+GYYDAg=", // Agogo
        "AXgAAFCHVTp2iAoQ/1YIAAA=", // SteelDrm
        "AXgABIT6mShKwAQi94j4CAA=", // WoodBlok
        "AXgAAQDwRAAKAAEA/TMKAAA=", // TaikoDrm
        "AXgAAB6YVUYFagAG+WYCABA=", // MelodTom
        "AXgAABDxdwos5AQA80QKAAA=", // Syn.Drum
        "A3gAABJfAA4A4gNGL//xDBg=", // RevCymbl
        "AXgACWD4RioC6AkwhkqKAgI=", // FretNoiz
        "AXgACyCWeoIs4wsQiQryHAg=", // BrthNoiz
        "AHgAAeD2AAAA8gEAHzMAAAM=", // Seashore
        "AXgAB1A3M65WAAegVic+AAA=", // Tweet
        "AXgABFCyAGpxogRA9DMaAAA=", // Telphone
        "A3gAA/D2AAoA7gsAIAUKAAA=", // Helicptr
        "AHgAC2D/AQgA+AtAUgUTEAA=", // Applause
        "AXgAAVBkAPAM4gPw9oiwAAY="  // Gunshot
    }, false, false);

    // FM instrument algorithms for MA-3, 4 operators
    private static Algorithm[] MA3_INSTRUMENTS_4OP=Algorithm.from(new String[]{
        "AXsACBD3BvCdCAxQ4yMgcwAJENEUMFgADRDTJkACAA==", // GrandPno
        "AXsADBDyIlCeAAhQ8iPwcgANEPIi0GYADRDyFUAoAA==", // BritePno
        "AX0ADEDSJGBRwA0Q0RZwIgAMINM04C0ADSDRFfAiAA==", // E.GrandP
        "AX0ADRbxJepowA0u0yo5CgAKHsElMFygDCbTOjoKAA==", // HnkyTonk
        "AnsADjCzKjRtIAgwsilCbwAMIKQUEk0gDxChF4QUAA==", // E.Piano1
        "AX0ADHD0XLqMsgkQ8hj4EgAMEPAbGklADRDyF/gQAA==", // E.Piano2
        "AX4ADBDiJQABhAxg8gUwUwMMcPMBYHIEDRDiJ/EQBQ==", // Harpsi
        "AXsACBDxFvpgpQgQ8RUKdAAMcPM3Km0EDTDyKSggAA==", // Clavi
        "AX0ADJDmZfBWQgkQ1ETgGAAMsOZmwFsFCRDkROAYAA==", // Celesta
        "AX8ADHD5NEokAAxA+yO6PAAMIPMkSkkgCBD0NOoQAA==", // Glocken
        "AX0ADCBVIgCCAQ0W9DIABQAMkKUiAHABDR7yEQAYAA==", // MusicBox
        "AX0ACXDEJChcAA9A2SVoHgAPgMQjKHgACxDSNPQcAA==", // Vibes
        "AX0ADMCnRPCh4AkQtEXwFAAMYLdk8IQADRDUVfAUAA==", // Marimba
        "AX0ACFD5ZtBiQAkQ91fRDAAMUPZmoHYACRD2Z+ECAA==", // Xylophon
        "AX0ACqD0M1RBEAkQ8yMhFAAKdvQzVEHoCS7zIyEWAA==", // TubulBel
        "AX4ACCDqRMAaYQgwszNQUQEIENMzACgACRDERGEYAA==", // Dulcimer
        "A3oACgD0DAQCAAgQ9QwAAQQKMtUMFB4JCiDxDAQeBA==", // DrawOrgn
        "AX8ACgTlChgOgAgg2ABQdAALFuUKEQYACS7mCgEGAA==", // PercOrgn
        "An8AChb/DQwlgAoQvwoYFRULLP8OASQACw7/DgUkEQ==", // RockOrgn
        "AX8ACDCfBQBOAAhwtwIgdAAIEI8FABIACACHBQASBQ==", // ChrchOrg
        "An0ADCB4BRBicA0QXwYAAgAMEGwFMCkFDSBfBwABAA==", // ReedOrgn
        "An0ACDyCABBUUQkecgogCQAIFG8AEEgRCSZ/CgAeAA==", // Acordion
        "AnwACeD/CQOzAAug/wgApAAJEP8IAZAACSBvCAAOAA==", // Harmnica
        "AH0ACiJ8AAQ8jAsicgoAKgAKEH8AAFBlCxB/CgQoEA==", // TangoAcd
        "An0ADBDhFIBVwAkQ8zfwAAAMMLVVQDgBCRDUSfE2AA==", // NylonGtr
        "AXwACJD3FCpqhQ3Q8xhatgANEPIUGl7ACRDTKPYQAA==", // SteelGtr
        "AXsADRD3FzpFAAlQ9RQqSwAJMPIH+n0ACRDCCPoQAA==", // JazzGtr
        "A30ACBD6IhA9AQkQ8inxDgAMMPIjYEEECRDkSGEOAA==", // CleanGtr
        "An0ADADYZ3BE4AkA6TlxAAAIEOM4kBMADRC0OjEAAA==", // Mute.Gtr
        "AXwADA74AvpMTAgmwQEaPQAIELIKGjwACCCxGhooAA==", // Ovrdrive
        "AnwACCC8AgAhgwgQxQoQdAAIIMUKEFwICRDBClE8Bg==", // Dist.Gtr
        "An0ACADyhwBZqAkg0znwGAgIALKHAESgCSCnd/I0Bg==", // GtrHarmo
        "AX0ADBCzOKA5YAkQwziyAAAIEJMxEB8ACRDDOKIUAA==", // Aco.Bass
        "AXsADBCiExpywAzAlDZK6AAMELMjKloADCCxGCoAAA==", // FngrBass
        "AXsADBD3IxpOoAxwy0Z6VAAMIPkmKl4ADBCyaGoAAA==", // PickBass
        "AXsADRTDIxp2gA0SozY6ZgANEJMmGmYADSCxKCoAAA==", // Fretless
        "AXsADBD3Iyo6YAyQ9mZKVAAMEMkmKmIADBDy+PoMAA==", // SlapBas1
        "AXsADBD3Ixo6QAjQtWcqSAAMEJkmKnoADBDyaGoYAA==", // SlapBas2
        "AnsACBDmKFA4oAgg5BhgnQgJEOIYYIwACRDiKJMAAA==", // SynBass1
        "AX0ACCD1eGpQwAgg8XjKAQAIEPN3alDACBDyeMoBAA==", // SynBass2
        "An0ADhBgAwBKTAkaZAcgDAAMQOV6ABlGCRJnd/EOAA==", // Violin
        "An0ADBBgAwAmQQsQZgcRDAAOEOZ3ACFGCRBnd/EOAA==", // Viola
        "AnsACRD2BgFAgQlQ9f75UAALEPUHIbQACzBjBxEGAA==", // Cello
        "AnsAARD2AgBkwAFQ9v75VBEDMPYEMWwAASBjBxACAA==", // Contrabs
        "A30ADhRyAxBadAsgYwYVCAAMGHMEAFlsCxBjBhkIAA==", // Trem.Str
        "An0ACBD7W5BQ4AkQ54cjAAAIEPdl8EUICRDGVvMAAA==", // Pizz.Str
        "AXsACCD4WECkwAhQuHlAhAAIELcyQIagDBD0IhoQAA==", // Harp
        "AXsADBD4QzoRYAwO8iL6hAAMEPczCnIADAD0M/oACA==", // Timpani
        "An8AAhJaBgQuRwAgxgYQYAQBGmYGER4AAyZlBhUUDA==", // Strings1
        "An8AChZqBQAAaQoQxgUUXAIJGlYGAgIBCR5VBhEcGw==", // Strings2
        "An0AChyYAhBsAAkWfwUBHAAOFpsCAFAECx5vBAEBAA==", // Syn.Str1
        "An0AChCYAhBMqQkQZgUBGAAKEIgCACgbCxBXBDEBAA==", // Syn.Str2
        "AX0ACGDAAPBMpwlAYwVhXgAIEH8DAHkICSBfBQEAAA==", // ChoirAah
        "An0ADlDAAPRQ5wlAcwVhUgAKEHcEMGoKCRCRBQIAAA==", // VoiceOoh
        "An0ACBCgBPBYAAkQfwURIgAKEJ8EAGkJCzB/BQECAA==", // SynVoice
        "An8ADkD0RmQAqAgQxTMQAAYJEMdmAQAGCQC3dgEABg==", // Orch.Hit
        "AXsACRCIBRBQwQkwqAVAXgAIEHcGEGoACxCfCAAYAA==", // Trumpet
        "AXsACRB2BxBw6AkQmAVAPgAJEGcHEGoBCRCPCAEUAA==", // Trombone
        "AXsACRBlBiCI6AkQyAtAYgEJEJcJMEYBCSB/CAEAAA==", // Tuba
        "AX0ACjBwB1BoCgkAnQlAAAILUHkGEEwRCRCHCSAAAg==", // Mute.Trp
        "AX0ACBR5AABDCAkengcABgAKHGkCFFqACRauBwAGCA==", // Fr.Horn
        "AX0ACB6GAhpYwAgenwgIIAAJEHcFGFjMCRCYCAgcAA==", // BrasSect
        "AX0ACB52CCpAwAgenwoKKAAJEHYIKEDACRCYCggoAA==", // SynBras1
        "AXsACRBjBBBwwAlglwVwnAEJEHUzsIwICRD/BwEQAA==", // SynBras2
        "AXsADjD5ZjB0AAgQggYAaAAKEIUAADEBCRCGCBEMAA==", // SprnoSax
        "AX0ADBCTAAoqgQkQggkIJAAMEJMACjaJCRCSCQhUAQ==", // AltoSax
        "AX0ADBBzAAoWYQkQcgkJPAgMEHMACiJpCRByCQk2AA==", // TenorSax
        "AX0ADBBzBQBKwA0gcgghGAAOIHUBADkCDRB0CBIUAA==", // Bari.Sax
        "An0ACRCgBAF6BQkwkRkBJAAJELAEJWAACSCgCgE4AA==", // Oboe
        "AX0ACRCgBAGKBQkwkRkRLAAJELAEJWAACSCgGhEsAA==", // Eng.Horn
        "An0ADBDHABBiAQ8wcQgQAgkMEMcAEGIBDzJxCBAMCQ==", // Bassoon
        "AX0ADyByARCW4AsQgggQDAAPQFIBEGkACBByCBAMAA==", // Clarinet
        "An0ACFCsBxAw4gsQlwjxnAAKEIUHEHgBCRCFCgAYAA==", // Piccolo
        "AX0ACjDaARAc4AkweAswlAALEOgJBJzgCxBlCgAEEA==", // Flute
        "An0ACyCWegbo4wsQhQoCEAgJcKlmkDwYCxCFCgaQAA==", // Recorder
        "An0ACNCgBgQB4AmguhoEjAMLII8EArAACRCACQYUAA==", // PanFlute
        "AX0ADFDMBxAw6gkQdwlgbAAKIHgINC8ICRB1CAIEAA==", // Bottle
        "AX0ACFCsBRAY4gsQZwlUXAgKAKgDOAkSCRBlCQIEAA==", // Shakhchi
        "AXoACxBqBgIUAAsQiAcKFAALEGqXCrARCx5oBwoUCA==", // Whistle
        "An0ACyCGeALx4wkQhQkCAAAJEKlmkDwYCxB1CgJoCA==", // Ocarina
        "An0ACBD/B0C7CAkQrwoBDggKIP8CMJsoCRCvCgAOCA==", // SquareLd
        "AX0ACR7wBwho4Ake3wcIKAgJEP8DCFH0CRDvCAgoCA==", // Saw.Lead
        "An0ACkDEB4QA6ApAxgZ6UQgJIIYFUA0AChBkCBQIEA==", // CaliopLd
        "AX0ACxh3AmIRCAsQ9ggCLAgLEncCYg0ICxT2CAIsCA==", // ChiffLd
        "AX0ACRiSBioiAQsgkQgiOAwJEpIGKioBCyKRCCI4DA==", // CharanLd
        "AX0ACHBAAPA0BgkgcwhgTAAKEH8JBHEICRB/CAEIAA==", // VoiceLd
        "AX8ACRDBGBpUGAkSwRYqPAAJKMAICmgACTLBGBoYEA==", // FifthLd
        "AX0ACBCyAwBYAQkQogkBRBAIEMNEUC6gCBDTCWAwAA==", // Bass&Ld
        "AX0ABHD/MwqYoQBQ90QKLgABHmEBAWLBARCBBRECAA==", // NewAgePd
        "AX0ACRKgBQCg4AkQMAQCCAAJFqADALzgCRQwBAIIAA==", // WarmPad
        "AX0AAR5lBBKIAwEspQYSCgABEGMDEoibARSTBQICCA==", // PolySyPd
        "AX0AAR6gAPqESAGAQwcyWgABEHMDAoXAASZvBQIAAA==", // ChoirPad
        "AX0AC3AhA0KqgAsQYgUwAAALciEDQqqACxRiBTAAAA==", // BowedPad
        "AX0ADBDyAwBeygkaVgQADgAMEPYDMB1ACRJXBAERAA==", // MetalPad
        "AX0AABpBAxqUwAEeYgUAAgAAFsUEGnigARCCBQECAA==", // HaloPad
        "AX0AChpIAwB4AAkWOAQBAgAIJDEEAIoACR5yBQECAA==", // SweepPad
        "AX0AD6D4BooLIA8QhRIOABANpPgGigsgDxaFEgoAEA==", // Rain
        "An0ACjBjAzBKYAkwQgQAJQAIEGMDMECwCRBBBBEOAA==", // SoundTrk
        "AX0ACWD4FUpRoAgQwiR6JAAJ5PgVSlGgCBTCJHokAA==", // Crystal
        "An0ACBBjJPBVlAkQ8wQxAAAIJsME8EGUCS6WVDEwAA==", // Atmosphr
        "AXoACxbxFFIeCwsW8lTyHAkLEvEUUh4LCxLyVPIcCQ==", // Bright
        "AX0AAT4RAStKjQIwIQQQJgABECECClCAARgxAwoOAA==", // Goblins
        "An0ACCBDAFCIAAkQogwBOgAKEDMCJIwQCRCvAwEAAA==", // Echoes
        "AX0ACyBTA4BlQQseYRQgGAQLJlMDgGVBCxZhFCAYBA==", // Sci-Fi
        "AX0ACSDSMloqYAlw8mT6IAkJEtIyWw6ICULyZPogEQ==", // Sitar
        "An0ADBDTIRAoBA0w0zXhAgAJYPdCEDwBCRD3iOECAQ==", // Banjo
        "AXsAABDxEypoiAEw+lZ6UAAAUPgzOmAQBTD0RPoECA==", // Shamisen
        "AXsAADD3VSBSyABQ+YhAVhAAEPIj8KigBBDyIvoMAA==", // Koto
        "AX0ACUb6VqohxAkQwjUKIAAJVPhWqjnADRjCRAogAA==", // Kalimba
        "AX0AABC5DBpCKQAyfw0KLAgAEKkJOgAAAECGDQooAQ==", // Bagpipe
        "An0ADBCJAxAeQQkQZgcoAAAMIMZ3ACUECRCTeOJmAA==", // Fiddle
        "AX0ACRCgBAFCBQlgkRkBNAAJELAEJWAACSCgCgE8AA==", // Shanai
        "AX0AAOD2NFpBYAAgxnbqLgAAfsYiWngBAmD1RdoEAA==", // TnklBell
        "AX0AAHDqRCpcIABQ92YaIAAArulkKoQAACD3ZnoQAA==", // Agogo
        "AX8ADSBERSoAEAkgZjQKWAANIOREKgCgDAb0RCAAAg==", // SteelDrm
        "AX0ABFL6mSqGoAQm+ncqAAAApvqoKnAAACD6dyoAAA==", // WoodBlok
        "AXwACAD7Q1Bg5AhA/KMQTAAJMPxDQCwACQD+VQIAAA==", // TaikoDrm
        "AX0ACBD5iFo65AgA81XqAAAMAPNEWjIBDAD0ROoAAA==", // MelodTom
        "A30ABA7riiIATAYAwwTyAAAAAPQEegAYAQD3B/oAFg==", // Syn.Drum
        "AX0ACeBPAAoA4AmQL//2GAgJ4E8ACgDgCeAv//oYAw==", // RevCymbl
        "AX0ACWD4RioC6AkwhkqKPgIJYPYmKgAqCWCIiooCAg==", // FretNoiz
        "AX0ACFCsBRA44gsQeInwFAAIgKwFECTgCxCIefAwAA==", // BrthNoiz
        "AHsACDD/AAAQ4AoA8iLwVAQIAPBE8DABCAASRPAAAA==", // Seashore
        "An0AD1A3M6pWAA+gVnc6AQAPUDczqlYAD65WRD4BAA==", // Tweet
        "AX0ADVCyAGpxog1A9DMaDAANULIAanGiDU70Mxo8AA==", // Telphone
        "A30AC/D2AAoErgsAIAUKEAAIAPAACmAOCAAgBQosAA==", // Helicptr
        "AX0ACAD8AQAA+AgAQgUAIAAKkGAB9AD4CDAwd/AcAA==", // Applause
        "AX0ACVDzAPAs4Avw9oiwEAYLUPIA8AjhC1D2iLBoBg=="  // Gunshot
    }, false, false);

    // FM drum algorithms for MA-3, 2 operators
    private static Algorithm[] MA3_DRUMS_2OP = Algorithm.from(new String[] {
        "AXlYAC3wnwYIUAAl+p8ZAAA=", // SeqClick H
        "AXgdAAHWWQoA+AABiIkmEAA=", // Brush Tap
        "AHg8ABqiAwQA+AIAZQcyKGA=", // Brush Swirl L
        "AXgkAA3aNAIA8AAF2IYqCAA=", // Brush Slap
        "AXgYAEDIBSEA+AAsuQdGBCM=", // Brash Swirl H
        "AHgSAIZwAgY04gEM9QkxABo=", // Snare Roll
        "AUBXAHH3WfoMwQRRqF/6AAY=", // Castanet
        "AXg8AAD8ABok4gAA93c6AOA=", // Snare L (MA-2)
        "AXheAEH5nPoBaQAh+pe6ABY=", // Sticks
        "AXg3BBCn3Ro0pQQAcFUKAKA=", // Bass Drum L (MA-2)
        "A3gnASHzOwsw+gAB94k6AAY=", // Open Rim Shot
        "AXghABD/d3oAoAQA82YKAKA=", // Bass Drum M (MA-2)
        "AXgnBAC4d3oQggQQ8HcKAIA=", // Bass Drum H (MA-2)
        "AXhKAHH3APoAwQQhqP/6AAY=", // Closed Rim Shot
        "AXhBAACrAAo04gAAqHc6AOY=", // Snare M (MA-2)
        "AWhjAPHwDwoAoATxY2YaAAc=", // Hand Clap
        "AXhEAACrAAow4gAAqHc6AOY=", // Snare H (MA-2)
        "AXgOAMD6ZvokYABQ9FV6GGA=", // Floor Tom L (MA-2)
        "AXg+AJDYiAoAoAQQqHdqAKI=", // Hi-Hat Closed (MA-2)
        "AXgOAMD6Zvo8gABg9FV6AIA=", // Floor Tom H (MA-2)
        "AXg+AMD3iAoAoARweHdKBKI=", // Hi-Hat Pedal (MA-2)
        "AXgOAMD5mfo8gACA9GZ6AIA=", // Low Tom (MA-2)
        "AXhDAMD2IgoAoAQgyzNKAKI=", // Hi-Hat Open (MA-2)
        "AXgOAMD6VfosgACg9Yh6AIA=", // Mid Tom L (MA-2)
        "AXgOAMD6mfo8gADA9WZ6AIA=", // Mid Tom H (MA-2)
        "AHhRAOD2EQoA5wTwnyIAAOY=", // Crash Cymbal 1 (MA-2)
        "AXgOAND6mfo8gADw9WZ6AIA=", // High Tom (MA-2)
        "AXhbAPD3AEoCpgBw9ETqUKY=", // Ride Cymbal 1 (MA-2)
        "AXhiALD1MzoA5QcAcCIAKOY=", // Chinese Cymbal (MA-2)
        "AlhvAGf8JUsABwB992UqABA=", // Ride Cymbal Cup
        "AZBtAKGHQSpMzADx53ZaAAk=", // Tambourine
        "AHhrAODxAApE4ATwkTMAFOY=", // Splash Cymbal (MA-2)
        "AahUBBH5d0oA4wQB81UKCAA=", // Cowbell
        "AXhqAOD2AAoG4AfwkSLgBOY=", // Crash Cymbal 2 (MA-2)
        "ATgaABHwEQoI5QQR9UkqGAA=", // Vibraslap
        "AXhaAPD3AEoCpgBw9ETqVKY=", // Ride Cymbal 2 (MA-2)
        "AcArAGH1zAoAYACx9YcKAAA=", // Bongo H
        "AcAhAGH1zAoAYACx9YcKAAA=", // Bongo L
        "A0hMAAXuqhsYgAAR+JoaAAg=", // Conga H Mute
        "AUgTAKGqWMpY4wDBxnlKAAA=", // Conga H Open
        "AVg5BBH5T0ocAAAB9lO6AAA=", // Conga L
        "A6gzA4f6bmU4xgMd9lUiAAw=", // Timbale H
        "AqguAWf6bmUkhgEd9lUmBAQ=", // Timbale L
        "AThNAH/sZipMoAAn+GYaEAA=", // Agogo H
        "AThIAHfsZipM4AAv+GYaJAA=", // Agogo L
        "AThkAPGlb0o89ADxh6sKAAM=", // Cabasa
        "ATBtAPGlb0o89ADxh5sKAAM=", // Maracas
        "A8AyCQDwBwtcQAjwgQwEJAA=", // Samba Whistle H
        "A8AwCQDwBwtcQAjwgQwEGAA=", // Samba Whistle L
        "AbgxAAGJCwBM4gTxh4hAABI=", // Guiro Short
        "AbgwAAGJCwBM4gTxhIhAABI=", // Guiro Long
        "AalFAGH8r/pYAARR94j6AAA=", // Claves
        "AchJBIX6mShKwAQj94j4AAA=", // Wood Block H
        "AchEBIX6mShKwAQj94j4AAA=", // Wood Block L
        "A1lQAR9m7to8oAIXdu5KDAA=", // Cuica Mute
        "A1gQAAGY+PAwMADhdohAAAg=", // Cuica Open
        "ATBYAFn3dxkyAACj15n1AAA=", // Triangle Mute
        "ATBYAFn/AAkyAACjxVX2DAA=", // Triangle Open
        "AbheAKWzMSkC4AD5fZZKGAA=", // Shaker
        "A8FMAfnFZiMCmgP1u10UABM=", // Jingle Bell
        "AdA+APE0VeoAYwCRR0MKGgE="  // Belltree
    }, true, false);

    // FM drum algorithms for MA-3, 4 operators
    private static Algorithm[] MA3_DRUMS_4OP = Algorithm.from(new String[] {
        "AX1IAGHw/wrgxARR+P/KAAAAofv/upwGBFHr//oEAA==", // SeqClick H
        "AX1BAFH4ADoA4AABmIu6AgAAwfhuOpAGABHNiAqyBg==", // Brush Tap
        "AX0sAAD1BgkA8ggAmApvACMAAPAP8ADwABA2P25UAA==", // Brush Swirl L
        "AX1FAEHwAAoA4ADhyGyaAA0AAbpH2iQHAAGIu9oCAA==", // Brush Slap
        "AX0xAFr1AwkA8ggwlgpvACMAkPAP8ADwADA2P25AFA==", // Brash Swirl H
        "AH0SAIZwAgY04gEM9QkxABoAMPqEOiAAAADmd1oAAA==", // Snare Roll
        "AUVXAHH3WfoIwQRRqF/6AAYAIfVlCpwFAFHKmapQAA==", // Castanet
        "AX1DAAH8ABok+AAB93c6AAAAcet1+sDAAAH6YApwAA==", // Snare L
        "AX47APH5yJoB9ACh2ii6AAMAsdglWlUDANHLl7oACA==", // Sticks
        "AX0XACH6tvoAJAAh9mV6QAAAUbl0ejQBABH3hXoADA==", // Bass Drum L
        "AX0SBMHwVQoU4gSx93d6AAAAwfpmigAAAHH3d3oAAg==", // Open Rim Shot
        "AX0SANH9d/oAxgAh93R6AAAAMfl2ekwKACH4proADA==", // Bass Drum M
        "AX0aANH9dvo4xgAh93d6MAAAIfl0eiQCABH3hnoADA==", // Bass Drum H
        "AX00AKHyCFogwQRx2TP6AAYAkbAJ2gDyAAHZuboAEg==", // Closed Rim Shot
        "AX0NBMHwVwoc7QSB96l6AAAAwfV4agDAAHH3dnoEAg==", // Snare M
        "AW0ZAAH0RgoI4AAB9pQaAAUA0/gPGgJbAPH6mFoCAg==", // Hand Clap
        "AX0QAMHxBwoo4gCx97fQAAAAkfpliiT8AHH4phoAEg==", // Snare H
        "AUUKAMHaZvokhgBR+nt6AAAAAfNV+lQBAFH2VnoAAA==", // Floor Tom L
        "Aa1aAPHwADoI4AABuMi6AgAAAfM9OjDuANG6iwoCCw==", // Hi-Hat Closed
        "AV0LAMHFZvoABgCx+Xt6ABMAAfRF+mgBAFH2RloAAA==", // Floor Tom H
        "Aa1RAMHwADoA4AABeIi6PgAAYfNeOiD0AFGDiAoCCw==", // Hi-Hat Pedal
        "AXUFAKHFZvoEBgAR+Hl6ABIAAfNV+kQBAFH2VXoAAA==", // Low Tom
        "Aa1aAPHwADoI4AABt2i6BgAAAfM9OjDuANG5WwomCw==", // Hi-Hat Open
        "AZUUAKHFZvoABgCB+XpqABIAAfQl+nAAAEH2V3oIAA==", // Mid Tom L
        "Aa0cAKHEZfokBgAR+Xd6ABMAEfRV+pwBAEH2Z3oMAA==", // Mid Tom H
        "AK1UANH5Bgo4cACxk0UgDBIA8bMECgDgAPHEVXo0Bg==", // Crash Cymbal 1
        "AcUcAIHEZ/oQBgBh+nh6ABMAEfVW+sgFAFHGVnoAAA==", // High Tom
        "AF1tAPH3AEom4ACR5ETqDA4A4fAASiLgAOH0JdpACw==", // Ride Cymbal 1
        "AV1YAAHzchp84AdR41IAAB4AIfGDajzgAAGSUwokBg==", // Chinese Cymbal
        "AF1NAPH3AEpa8wDx9UTqAAwAwfAASjLzAPH1JdpsCw==", // Ride Cymbal Cup
        "AZVtALGHQSoowABR58taAA0AsYdSKgAIAPHXx2oACA==", // Tambourine
        "AW1hAIHGMwAA+QTRk0PAAAMAUVAzAATNBvHDWGAAEw==", // Splash Cymbal
        "Aa1UBDH5hToCgAQBp1M6AAAEQf+5qgwCBBH8VTqAAA==", // Cowbell
        "AV14AJH2AAoC4AcBlCIAAAAA8fMACgLgAHmWNDogAg==", // Crash Cymbal 2
        "AD0iABXRJgYA/QAN9lkhAAgAYfVECnT9AAH2VAoAHg==", // Vibraslap
        "AV1uAOHwBg8U5wHx5VS6QA4AkfAASh7mAJH1NeoACw==", // Ride Cymbal 2
        "AcUkAGH1zAoAYACx9YcKAAAA8fXMCggAAPH0cwoAAA==", // Bongo H
        "AcUgAGH1zAoAYACx9YcKHAAA8fXMChgAAPH1gwoAAA==", // Bongo L
        "A01MAAXuqhsYgAAR+JoaAAgAAfAACrAAAAH9mhocAA==", // Conga H Mute
        "AU0TAKGqWMqY4wDBxnlKAAAAwfpoigAAACH3eXoAAg==", // Conga H Open
        "AV0OAKGqWMp84wDBxWlKAAAAwfpoigAAACH3eXoACg==", // Conga L
        "AaszACHHCApYwABx+WgKxAAAkdamGmgEAJH5aAoYBg==", // Timbale H
        "AascACHHCApYwABx+WgKxAAAwdamGlwEAJH5aAoQBg==", // Timbale L
        "AT1NAHHsZipUoAAh+GYaKAAAcexlKmgAACH4ZhooAA==", // Agogo H
        "AT1IAHHsZipUoAAh+GYaKAAAcexVKjQAACH4Zhp0AA==", // Agogo L
        "ATtJAHHwNWoA4ATx6ESqYAAA8eQmSgAAAPGLlAoIBg==", // Cabasa
        "ATR0APHwNWoQ4ATxiERqDAAA8ckz+gAGAPF6o4oADA==", // Maracas
        "A8UyAADwBwQooQngwQwUrAAJAPAHC1xACPCBDAQYAA==", // Samba Whistle H
        "A8UwAADwBwQooQngwQwUrAAJAPAHC1xACPCBDAQYAA==", // Samba Whistle L
        "AeM1ABHCYAAw4QAxw2oAAAQA69EGAIDrBAP3z2AABw==", // Guiro Short
        "AeMrABHCYAA44QAxw2oAAAQA0fAGAIDrAAWW7GAUBw==", // Guiro Long
        "AawyAHHXZqosAABx/rOqNAAEAfuAygAABPHziAoACA==", // Claves
        "AcRCAPHmZqr8AABh9mW6AAAEcdwKygACBDH3x0oACA==", // Wood Block H
        "AcQ8APHmZqr8AABh9mW6AAAEofoKykACBDHHzEoAEA==", // Wood Block L
        "AV0wAAHazbo+AABhYGUKGAAAAfiMCjgAAGFgdwoIAA==", // Cuica Mute
        "A10QAAGY+PAwMADhdohAAAgAIfcACpAAALHXu8pIAA==", // Cuica Open
        "ATduAJH8hAoUFgDxtZVqAAQA4fvHOhwGAGHti/oYAA==", // Triangle Mute
        "ATduAJH1VAoMFgDxtZVqAAQA4fmXOiAGAGHlW/oMAA==", // Triangle Open
        "AbwnAHGQNWoA4AQxlkRqEAAAweQmSgQAAPGalIoIBg==", // Shaker
        "AcVcAGF2AApQAgAha1VKAAMOYTACChwCCJF1JvoAAg==", // Jingle Bell
        "AdVkADHEIAtg8wAhQ1VPAAIPcTACDxwCCDFUJm8IDQ=="  // Belltree
    }, true, false);

    // Wave drum algorithms for MA-3
    private static Algorithm[] MA3_DRUMS_WAVE = Algorithm.from(new String[] {
        "Hz6AeQAI8PAQAAAAC5sLm4E=", // Snare L
        "ISMoeQAI8PAQAAAAA6kDqYA=", // Bass Drum L
        "IycQeQAI8PAQAAAAA6kDqYA=", // Bass Drum M
        "JC7geQAI8PAQAAAAA6kDqYA=", // Bass Drum H
        "Jko4eQAI8PAQAAAAC5sLm4E=", // Snare M
        "KFnYeQAI8PAQAAAAC5sLm4E=", // Snare H
        "KR9AQQBY8PAQAAAACtQNxYI=", // Floor Tom L
        "KjqYqQAI8PBAAAAABNcE14M=", // Hi-Hat Closed
        "KycQWQBY8PAQAAAACtQNxYI=", // Floor Tom H
        "LDawqQAI8PBAAAAABNcE14M=", // Hi-Hat Pedal
        "LTDUcQBY8PAQAAAACtQNxYI=", // Low Tom
        "LjyMqQAI8PAwAAAADPsM+4Q=", // Hi-Hat Open
        "LzqYkQBY8PAQAAAACtQNxYI=", // Mid Tom L
        "MEZQqQBY8PAQAAAACtQNxYI=", // Mid Tom H
        "MTOQqQBY8PAAAAAADfkV24Y=", // Crash Cymbal 1
        "MlIIwQBY8PAQAAAACtQNxYI=", // High Tom
        "Mz6AWQBY8PAwAAAABjoSwIU=", // Ride Cymbal 1
        "NCMoWQBY8PAQAAAADfkV24Y=", // Chinese Cymbal
        "N1IIaQBY8PAoAAAADfkV24Y=", // Splash Cymbal
        "OTawWQBY8PAAAAAADfkV24Y=", // Crash Cymbal 2
        "O0ZQWQBY8PAwAAAABjoSwIU="  // Ride Cymbal 2
    }, true, true);

    // Wave synthesis ROM for MA-3
    private static int[][] MA3_WAVEROM = waveRom(new String[] {
        "93cXB1/wgn9PubQYiZEIiIAIiIAIiIAIiIC3cOQ3L9QCaagwCH2JgpkRWwCYAjvwSvGE"+
        "X8BZCxiooBvHQIkBKbmXgYgTW6iVEAgrATsAsAOAAI3UkvEoPaqypigdiAkYkWm7paGi"+
        "OQqZWyiNxSAaCRCYtRIa84AYAKUwCcpoC4EpoZgumacBCbhrgCiBGyoMlCg8SPCBCSmw"+
        "QooUH9EiChGbBDmhELdRmRCxEpBoDLN7uAMbsOAoiqMcCA2IkNM4ATnoA3upEaJwCRAR"+
        "DJQIGpSiKJB6qaMZKHsJktGUKwE8CSu2iBKJG8VRDKKBPIyhTMmzGBCIT4oawCAqghgD"+
        "mod9oJRboBCAEVqQmJMrGMEzL9qkABoZqqG586CYQKuCifATPJukGeMhiDGyQOJAkAAR"+
        "IAwJqNYYiIjRqJN6G5GQgAM+maNIjCm2o5I+CBgbgPQ4DZCQlDmIKYmiCLUAA5cCQglI"+
        "iDNqkJUYkiggu8eRGRyAwYqrtI3SIR2LkC6otAAKmbIighBtoAQIk3EBEhgJ0ZUCEAqQ"+
        "KpuL+IOtsQKYH5m4AcmWKBsIwKFDHpkAGh+xgggKHLqQCMABKBkboDEoIxkoMJWDAAKC"+
        "EQCAiJiIiAiIiAA=",
        "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"+
        "LPMBCCiYAA==",
        "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"+
        "CAgICAgICAg=",
        "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"+
        "gICA",
        "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"
    });

}
