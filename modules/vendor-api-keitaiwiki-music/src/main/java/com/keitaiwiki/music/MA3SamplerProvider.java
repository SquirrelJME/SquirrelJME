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
import cc.squirreljme.runtime.cldc.util.ExtraMath;
import org.intellij.lang.annotations.MagicConstant;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

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
 *
 * @see SamplerProvider
 * @since 2025/05/05
 */
@SquirrelJMEVendorApi
public class MA3SamplerProvider
	implements SamplerProvider
{
	/**
	 * Specifies the use of MA-2 algorithms for FM synthesis.
	 *
	 * @see MA3SamplerProvider (int,int,int)
	 * @see #setDrumType(int) 
	 * @see #setInstrumentType(int) 
	 */
	@SquirrelJMEVendorApi
	public static final int FM_MA2 = 2;
	
	/**
	 * Specifies the use of 2-operator MA-3 algorithms for FM synthesis.
	 *
	 * @see MA3SamplerProvider (int,int,int)
	 * @see #setDrumType(int)
	 * @see #setInstrumentType(int)
	 */
	@SquirrelJMEVendorApi
	public static final int FM_MA3_2OP = 1;
	
	/**
	 * Specifies the use of 4-operator MA-3 algorithms for FM synthesis.
	 *
	 * @see MA3SamplerProvider(int,int,int)
	 * @see #setDrumType(int) 
	 * @see #setInstrumentType(int) 
	 */
	@SquirrelJMEVendorApi
	public static final int FM_MA3_4OP = 0;
	
	/**
	 * Nominal hardware sampling rate. When rendering samples at this rate,
	 * the
	 * output will have a 1:1 correspondence with what the hardware would
	 * produce.
	 * @see #instance(float)
	 */
	@SquirrelJMEVendorApi
	public static final float SAMPLE_RATE = 33868800.0f / 684;
	
	/**
	 * Specifies the use of MA-3 waves for wave drum synthesis.
	 *
	 * @see MA3SamplerProvider (int,int,int)
	 * @see #setWaveDrumType(int) 
	 */
	@SquirrelJMEVendorApi
	public static final int WAVE_DRUM_MA3 = 0;
	
	/**
	 * Specifies that FM drum algorithms always be used in place of wave
	 * drums.
	 *
	 * @see MA3SamplerProvider (int,int,int)
	 * @see #setWaveDrumType(int) 
	 */
	@SquirrelJMEVendorApi
	public static final int WAVE_DRUM_NONE = -1;
	
	/** Key index bias. */
	@SquirrelJMEVendorApi
	static final int A4 = 81;
	
	/** YAMAHA AICA ADPCM quantization step size lookup table. */
	@SquirrelJMEVendorApi
	static final int[] AICA_STEPS = {230, 230, 230, 230, 307, 409, 512, 614};
	
	/** Amplitude modulation levels. */
	@SquirrelJMEVendorApi
	static final int[] AM_LFO_A;
	
	/** Amplitude modulation LFO phase-advance. */
	@SquirrelJMEVendorApi
	static final int[] AM_LFO_B = {8, 18, 26, 31};
	
	/** Envelope attack stage bit flag. */
	@SquirrelJMEVendorApi
	static final int ENV_ATTACK = 0;
	
	/** Envelope decay stage bit flag. */
	@SquirrelJMEVendorApi
	static final int ENV_DECAY = 1;
	
	/** Envelope finished stage bit flag. */
	@SquirrelJMEVendorApi
	static final int ENV_DONE = 4;
	
	/** Bit flags indicating which FM operators control the final output. */
	@SquirrelJMEVendorApi
	static final int[] ENV_FLAGS =
		{0b10, 0b11, 0b1111, 0b1000, 0b1000, 0b1010, 0b1001, 0b1101};
	
	/** Envelope release stage bit flag. */
	@SquirrelJMEVendorApi
	static final int ENV_RELEASE = 3;
	
	/** Envelope sustain stage bit flag. */
	@SquirrelJMEVendorApi
	static final int ENV_SUSTAIN = 2;
	
	/** Binary exponent. */
	@SquirrelJMEVendorApi
	static final int[] EXP;
	
	/** Wave maximum. */
	@SquirrelJMEVendorApi
	static final int FULL = 0;
	
	/** Envelope attenuation parameters by BLOCK, used with KSL. */
	@SquirrelJMEVendorApi
	static final int[] KSL_B = {0, 2, 1, 4};
	
	/** Envelope attenuation parameters by F_NUMBER, used with KSL. */
	@SquirrelJMEVendorApi
	static final int[] KSL_F =
		{56, 32, 24, 19, 16, 13, 11, 9, 8, 6, 5, 4, 3, 2, 1, 0};
	
	/** Drum algorithms for Yamaha's MA-2. */
	@SquirrelJMEVendorApi
	static final __MA3Algorithm__[] MA2_DRUMS = __MA3Algorithm__.__from(
		RomData.MA2_DRUMS, true, false);
	
	/** Instrument algorithms for Yamaha's MA-2. */
	@SquirrelJMEVendorApi
	static final __MA3Algorithm__[] MA2_INSTRUMENTS = __MA3Algorithm__.__from(
		RomData.MA2_INSTRUMENTS, false, false);
	
	/** 2-Operator FM drum algorithms for Yamaha's MA-3. */
	@SquirrelJMEVendorApi
	static final __MA3Algorithm__[] MA3_DRUMS_2OP = __MA3Algorithm__.__from(
		RomData.MA3_DRUMS_2OP, true, false);
	
	/** 4-Operator FM drum algorithms for Yamaha's MA-3. */
	@SquirrelJMEVendorApi
	static final __MA3Algorithm__[] MA3_DRUMS_4OP = __MA3Algorithm__.__from(
		RomData.MA3_DRUMS_4OP, true, false);
	
	/** Wave drum algorithms for Yamaha's MA-3. */
	@SquirrelJMEVendorApi
	static final __MA3Algorithm__[] MA3_DRUMS_WAVE = __MA3Algorithm__.__from(
		RomData.MA3_DRUMS_WAVE, true, true);
	
	/** 2-Operator FM instrument algorithms for Yamaha's MA-3. */
	@SquirrelJMEVendorApi
	static final __MA3Algorithm__[] MA3_INSTRUMENTS_2OP = __MA3Algorithm__.__from(
		RomData.MA3_INSTRUMENTS_2OP, false, false);
	
	/** 4-Operator FM instrument algorithms for Yamaha's MA-3. */
	@SquirrelJMEVendorApi
	static final __MA3Algorithm__[] MA3_INSTRUMENTS_4OP = __MA3Algorithm__.__from(
		RomData.MA3_INSTRUMENTS_4OP, false, false);
	
	/** Wave synthesis ROM for Yamaha's MA-3. */
	@SquirrelJMEVendorApi
	static final int[][] MA3_WAVEROM = MA3SamplerProvider.__waveRom(
		RomData.MA3_WAVEROM);
	
	/** Magic number for BLOCK / octave index calculations. */
	@SquirrelJMEVendorApi
	static final double MAGIC_B = 12 / ExtraMath.log(2);
	
	/** Magic number for F_NUMBER / frequency divider calculations. */
	@SquirrelJMEVendorApi
	static final double MAGIC_F = 684 / 33868800.0;
	
	/** Frequency multipliers, doubled to implement with a right shift. */
	@SquirrelJMEVendorApi
	static final int[] MULTIS =
		{1, 2, 4, 6, 8, 10, 12, 14, 16, 18, 20, 20, 24, 24, 30, 30};
	
	/** Constant for operator envelope rate offset calculation. */
	static final int NTS = 1;
	
	/** Array of sustain levels. */
	@SquirrelJMEVendorApi
	static final int[] SUSTAINS;
	
	/** Waveform set. */
	@SquirrelJMEVendorApi
	static final int[][] WAVES;
	
	/** Array of Wave drum envelope levels. */
	@SquirrelJMEVendorApi
	static final int[] WAVE_ENV;
	
	/** Wave negative value. */
	@SquirrelJMEVendorApi
	static final int WAVE_MINUS = 0x80000000;
	
	/** Minimum wave value. */
	@SquirrelJMEVendorApi
	static final int ZERO = 0x1000;
	
	/** Array of FM drum algorithms. */
	@SquirrelJMEVendorApi
	__MA3Algorithm__[] _algDrums;
	
	/** Array of FM instrument algorithms. */
	@SquirrelJMEVendorApi
	__MA3Algorithm__[] _algInstruments;
	
	/** Array of wave drum algorithms */
	@SquirrelJMEVendorApi
	__MA3Algorithm__[] _algWaveDrums;
	
	/** FM drum algorithm type. */
	@SquirrelJMEVendorApi
	int _prgDrumType;
	
	/** FM instrument algorithm type. */
	@SquirrelJMEVendorApi
	int _prgInstrumentType;
	
	/** Wave drums algorithm type. */
	@SquirrelJMEVendorApi
	int _prgWaveDrumType;
	
	static
	{
		/*
		 * Compute lookup tables.
		 *
		 * Formulas are courtesy of Gambrell and Niemitalo: "OPLx decapsulated".
		 */
		
		// Lookup memory
		AM_LFO_A = new int[52];
		EXP = new int[256];
		SUSTAINS = new int[16];
		WAVE_ENV = new int[512];
		WAVES = new int[32][1024];
		
		// Named waves
		//  Sawtooth
		int[] saw = MA3SamplerProvider.WAVES[24];
		//  Sine
		int[] sin = MA3SamplerProvider.WAVES[0];
		//  Triangle
		int[] tri = MA3SamplerProvider.WAVES[16];
		//  Trapezoid (clamped 2*triangle)
		int[] trp = MA3SamplerProvider.WAVES[8];
		
		// Quarter-period lookup tables
		for (int x = 0; x < 256; x++)
		{
			
			// Binary exponent table
			MA3SamplerProvider.EXP[x] = 1024 | (int)Math.round(
				(ExtraMath.pow(2, (255 - x) / 256.0) - 1) * 1024);
			
			// Sine table
			int y = (int)Math.round(
				-ExtraMath.log(Math.sin((x + 0.5) * Math.PI / 256 / 2)) /
				ExtraMath.log(2) * 256);

			sin[x] = sin[511 - x] = y;
			sin[512 + x] = sin[1023 - x] = y | MA3SamplerProvider.WAVE_MINUS;
			
			// Triangle table
			y = (int)Math.round(
				-ExtraMath.log((x + 0.5) / 256) / ExtraMath.log(2) * 256);

			tri[x] = tri[511 - x] = y;
			tri[512 + x] = tri[1023 - x] = y | MA3SamplerProvider.WAVE_MINUS;
		}
		
		// Trapezoid table
		for (int x = 0; x < 1024; x++)
		{
			trp[x] = x < 128 ? tri[x << 1] : x < 256 ? MA3SamplerProvider.FULL :
				x < 512 ? trp[511 - x] : trp[1023 - x] |
				MA3SamplerProvider.WAVE_MINUS;
		}
		
		// Sawtooth table
		for (int x = 0; x < 512; x++)
		{
			int y = (int)Math.round(
				-ExtraMath.log((x + 0.5) / 512) / ExtraMath.log(2) * 256);
			saw[x] = y;
			saw[1023 - x] = y | MA3SamplerProvider.WAVE_MINUS;
		}
		
		// Compute other waveforms
		for (int x = 0; x < 1024; x++)
		{
			// WAVES[0] is sin
			MA3SamplerProvider.WAVES[1][x] = x < 512 ? sin[x] :
				MA3SamplerProvider.ZERO;

			MA3SamplerProvider.WAVES[2][x] = sin[x & 511];

			MA3SamplerProvider.WAVES[3][x] =
				(x & 511) < 256 ? sin[x & 255] : MA3SamplerProvider.ZERO;

			MA3SamplerProvider.WAVES[4][x] = x < 512 ? sin[x << 1] :
				MA3SamplerProvider.ZERO;

			MA3SamplerProvider.WAVES[5][x] =
				x < 512 ? sin[x << 1 & 511] : MA3SamplerProvider.ZERO;

			MA3SamplerProvider.WAVES[6][x] =
				x < 512 ? MA3SamplerProvider.FULL :
				MA3SamplerProvider.WAVE_MINUS;

			MA3SamplerProvider.WAVES[7][x] =
				x < 512 ? (MA3SamplerProvider.EXP[255 ^ x >> 1] - 1024) << 1 :
					MA3SamplerProvider.WAVES[7][1023 - x] |
					MA3SamplerProvider.WAVE_MINUS;

			// WAVES[8] is trp
			MA3SamplerProvider.WAVES[9][x] = x < 512 ? trp[x] :
				MA3SamplerProvider.ZERO;

			MA3SamplerProvider.WAVES[10][x] = trp[x & 511];

			MA3SamplerProvider.WAVES[11][x] =
				(x & 511) < 256 ? trp[x & 255] : MA3SamplerProvider.ZERO;

			MA3SamplerProvider.WAVES[12][x] = x < 512 ? trp[x << 1] :
				MA3SamplerProvider.ZERO;

			MA3SamplerProvider.WAVES[13][x] =
				x < 512 ? trp[x << 1 & 511] : MA3SamplerProvider.ZERO;

			MA3SamplerProvider.WAVES[14][x] =
				x < 512 ? MA3SamplerProvider.FULL : MA3SamplerProvider.ZERO;

			//  PCM RAM
			MA3SamplerProvider.WAVES[15][x] = MA3SamplerProvider.ZERO;

			// WAVES[16] is tri
			MA3SamplerProvider.WAVES[17][x] = x < 512 ? tri[x] :
				MA3SamplerProvider.ZERO;

			MA3SamplerProvider.WAVES[18][x] = tri[x & 511];

			MA3SamplerProvider.WAVES[19][x] =
				(x & 511) < 256 ? tri[x & 255] : MA3SamplerProvider.ZERO;

			MA3SamplerProvider.WAVES[20][x] = x < 512 ? tri[x << 1] :
				MA3SamplerProvider.ZERO;
			MA3SamplerProvider.WAVES[21][x] =
				x < 512 ? tri[x << 1 & 511] : MA3SamplerProvider.ZERO;

			MA3SamplerProvider.WAVES[22][x] =
				(x & 511) < 256 ? MA3SamplerProvider.FULL :
				MA3SamplerProvider.ZERO;

			//  PCM RAM
			MA3SamplerProvider.WAVES[23][x] = MA3SamplerProvider.ZERO;

			// WAVES[24] is saw
			MA3SamplerProvider.WAVES[25][x] = x < 512 ? saw[x] :
				MA3SamplerProvider.ZERO;

			MA3SamplerProvider.WAVES[26][x] = saw[x & 511];

			MA3SamplerProvider.WAVES[27][x] = x < 128 ? saw[x] :
				x >= 512 && x < 768 ? saw[x - 512 << 1] :
				MA3SamplerProvider.ZERO;

			MA3SamplerProvider.WAVES[28][x] = x < 512 ? saw[x << 1] :
				MA3SamplerProvider.ZERO;
			MA3SamplerProvider.WAVES[29][x] =
				x < 512 ? saw[x << 1 & 511] : MA3SamplerProvider.ZERO;

			MA3SamplerProvider.WAVES[30][x] =
				x < 256 ? MA3SamplerProvider.FULL : MA3SamplerProvider.ZERO;

			//  PCM RAM
			MA3SamplerProvider.WAVES[31][x] = MA3SamplerProvider.ZERO;
		}
		
		// Compute amplitude modulation LFO
		for (int x = 0; x < 26; x++)
			MA3SamplerProvider.AM_LFO_A[x] =
				MA3SamplerProvider.AM_LFO_A[51 - x] = x;
		
		// Compute sustain levels
		MA3SamplerProvider.SUSTAINS[0] = 0;
		MA3SamplerProvider.SUSTAINS[15] = 511;
		for (int x = 1; x < 15; x++)
		{
			MA3SamplerProvider.SUSTAINS[x] = (int)Math.round(
				16 * ExtraMath.pow(2, ExtraMath.log(x) / ExtraMath.log(2)));
		}
		
		// Compute wave drum envelope levels
		for (int x = 0; x < 512; x++)
		{
			MA3SamplerProvider.WAVE_ENV[x] = (int)Math.round(
				32767 * ExtraMath.pow(10, x * -96.0 / 511 / 20));
		}
		
	}
	
	/**
	 * Create a sampler with default parameters. Same as invoking
	 * {@code MA3Sampler(FM_MA3_4OP, FM_MA3_4OP, WAVE_DRUM_MA3)}.
	 *
	 * @see MA3SamplerProvider (int,int,int)
	 * @since 2025/05/05
	 */
	@SquirrelJMEVendorApi
	public MA3SamplerProvider()
	{
		this(MA3SamplerProvider.FM_MA3_4OP, MA3SamplerProvider.FM_MA3_4OP,
			MA3SamplerProvider.WAVE_DRUM_MA3);
	}
	
	/**
	 * Create a sampler with initial parameters. Equivalent to following the
	 * parameterless constructor with calls to {@code setDrumType()},
	 * {@code setInstrumentType()} and {@code setWaveDrumType()}.
	 *
	 * @param __instrumentType Specifies the data source for FM synthesis
	 * instrument algorithms. Must be one of {@code FM_MA2}, {@code
	 * FM_MA3_2OP}
	 * or {@code FM_MA3_4OP}.
	 * @param __drumType Specifies the data source for FM synthesis drum
	 * algorithms. Must be one of {@code FM_MA2}, {@code FM_MA3_2OP} or
	 * {@code FM_MA3_4OP}.
	 * @param __waveDrumType Specifies the data source for wave synthesis drum
	 * algorithms. Must be either {@code WAVE_DRUM_NONE} or
	 * {@code WAVE_DRUM_MA3}.
	 * @throws IllegalArgumentException if the value of
	 * {@code __instrumentType}, {@code __drumType} or {@code __waveDrumType} is
	 * invalid.
	 * @see #setDrumType(int)
	 * @see #setInstrumentType(int)
	 * @see #setWaveDrumType(int)
	 * @since 2025/05/05
	 */
	@SquirrelJMEVendorApi
	public MA3SamplerProvider(
		@MagicConstant(valuesFromClass = MA3SamplerProvider.class)
			int __instrumentType,
		@MagicConstant(valuesFromClass = MA3SamplerProvider.class)
			int __drumType,
		@MagicConstant(valuesFromClass = MA3SamplerProvider.class)
			int __waveDrumType)
		throws IllegalArgumentException
	{
		super();
		this._algWaveDrums = MA3SamplerProvider.MA3_DRUMS_WAVE;
		this.setInstrumentType(__instrumentType);
		this.setDrumType(__drumType);
		this.setWaveDrumType(__waveDrumType);
	}
	
	/**
	 * Retrieve the current FM synthesis drum algorithm type. This will be the
	 * value most recently used with {@code setDrumType()}.
	 *
	 * @return The current FM synthesis drum algorithm type: {@code FM_MA2},
	 * {@code FM_MA3_2OP} or {@code FM_MA3_4OP}.
	 * @see #setDrumType(int)
	 * @since 2025/05/05
	 */
	@SquirrelJMEVendorApi
	public int getDrumType()
	{
		return this._prgDrumType;
	}
	
	/**
	 * Retrieve the current FM synthesis instrument algorithm type. This will
	 * be the value most recently used with {@code setInstrumentType()}.
	 *
	 * @return The current FM synthesis instrument algorithm type:
	 * {@code FM_MA2}, {@code FM_MA3_2OP} or {@code FM_MA3_4OP}.
	 * @see #setInstrumentType(int)
	 * @since 2025/05/05
	 */
	@SquirrelJMEVendorApi
	public int getInstrumentType()
	{
		return this._prgInstrumentType;
	}
	
	/**
	 * Retrieve the current wave synthesis drum algorithm type. This will be
	 * the value most recently used with {@code setWaveDrumType()}.
	 *
	 * @return The current wave synthesis drum algorithm type:
	 * {@code WAVE_DRUM_NONE} or {@code WAVE_DRUM_MA3}.
	 * @see #setWaveDrumType(int)
	 * @since 2025/05/05
	 */
	@SquirrelJMEVendorApi
	public int getWaveDrumType()
	{
		return this._prgWaveDrumType;
	}
	
	/**
	 * Produces an instance of this sampler that can be used to render
	 * samples.
	 * Calling {@code setDrumType()}, {@code setInstrumentType()} or
	 * {@code setWaveDrumType()} after an instance has been created will
	 * affect
	 * new notes played by the instance.
	 *
	 * @param __sampleRate The output sampling rate of the rendered samples.
	 * @return A new sampler instance that can render samples using the
	 * current
	 * configuration of this sampler itself.
	 * @throws IllegalArgumentException if {@code __sampleRate} is a
	 * non-number or is less than or equal to zero.
	 * @since 2025/05/05
	 */
	@Override
	public Sampler instance(float __sampleRate)
		throws IllegalArgumentException
	{
		if (Float.isInfinite(__sampleRate) || __sampleRate <= 0.0f)
			throw new IllegalArgumentException("Invalid sampling rate.");
		return new MA3Sampler(this, __sampleRate);
	}
	
	/**
	 * Specify a new FM synthesis drum algorithm type. All new FM drum notes
	 * generated by instances of this sampler will use the new setting
	 * .<br><br>
	 * The default FM drum algorithm type is {@code FM_MA3_4OP}.
	 *
	 * @param __type Specifies the data source for FM drum algorithms. Must be
	 * one of {@code FM_MA2}, {@code FM_MA3_2OP} or {@code FM_MA3_4OP}.
	 * @return The value of {@code __type}.
	 * @throws IllegalArgumentException if the value of {@code type} is
	 * invalid.
	 * @see #getDrumType()
	 * @see #setInstrumentType(int)
	 * @see #setWaveDrumType(int)
	 * @since 2025/05/05
	 */
	@SquirrelJMEVendorApi
	public int setDrumType(
		@MagicConstant(valuesFromClass = MA3SamplerProvider.class) int __type)
		throws IllegalArgumentException
	{
		switch (__type)
		{
			case MA3SamplerProvider.FM_MA2:
				this._algDrums = MA3SamplerProvider.MA2_DRUMS;
				break;
			case MA3SamplerProvider.FM_MA3_2OP:
				this._algDrums = MA3SamplerProvider.MA3_DRUMS_2OP;
				break;
			case MA3SamplerProvider.FM_MA3_4OP:
				this._algDrums = MA3SamplerProvider.MA3_DRUMS_4OP;
				break;
			default:
				throw new IllegalArgumentException("Invalid type.");
		}
		return this._prgDrumType = __type;
	}
	
	/**
	 * Specify a new FM synthesis instrument algorithm type. All new FM
	 * instrument notes generated by instances of this sampler will use the
	 * new
	 * setting.<br><br>
	 * The default FM instrument algorithm type is {@code FM_MA3_4OP}.
	 *
	 * @param __type Specifies the data source for FM instrument algorithms.
	 * Must
	 * be one of {@code FM_MA2}, {@code FM_MA3_2OP} or
	 * {@code FM_MA3_4OP}.
	 * @return The value of {@code __type}.
	 * @throws IllegalArgumentException if the value of {@code type} is
	 * invalid.
	 * @see #getInstrumentType()
	 * @see #setDrumType(int)
	 * @see #setWaveDrumType(int)
	 * @since 2025/05/05
	 */
	@SquirrelJMEVendorApi
	public int setInstrumentType(
		@MagicConstant(valuesFromClass = MA3SamplerProvider.class) int __type)
		throws IllegalArgumentException
	{
		switch (__type)
		{
			case MA3SamplerProvider.FM_MA2:
				this._algInstruments = MA3SamplerProvider.MA2_INSTRUMENTS;
				break;
			case MA3SamplerProvider.FM_MA3_2OP:
				this._algInstruments = MA3SamplerProvider.MA3_INSTRUMENTS_2OP;
				break;
			case MA3SamplerProvider.FM_MA3_4OP:
				this._algInstruments = MA3SamplerProvider.MA3_INSTRUMENTS_4OP;
				break;
			default:
				throw new IllegalArgumentException("Invalid type.");
		}
		return this._prgInstrumentType = __type;
	}
	
	/**
	 * Specify a new wave synthesis drum algorithm type. All new wave drum
	 * notes generated by instances of this sampler will use the new
	 * setting.<br><br>
	 * The default wave drum algorithm type is {@code WAVE_DRUM_MA3}.
	 *
	 * @param __type Specifies the data source for wave drum algorithms. Must be
	 * either {@code WAVE_DRUM_NONE} or {@code WAVE_DRUM_MA3}.
	 * @return The value of {@code type}.
	 * @throws IllegalArgumentException if the value of {@code type} is
	 * invalid.
	 * @see #getWaveDrumType()
	 * @see #setDrumType(int)
	 * @see #setInstrumentType(int)
	 * @since 2025/05/05
	 */
	@SquirrelJMEVendorApi
	public int setWaveDrumType(
		@MagicConstant(valuesFromClass = MA3SamplerProvider.class) int __type)
		throws IllegalArgumentException
	{
		switch (__type)
		{
			case MA3SamplerProvider.WAVE_DRUM_NONE:
				this._algWaveDrums = null;
				break;
			case MA3SamplerProvider.WAVE_DRUM_MA3:
				this._algWaveDrums = MA3SamplerProvider.MA3_DRUMS_WAVE;
				break;
			default:
				throw new IllegalArgumentException("Invalid type.");
		}
		return this._prgWaveDrumType = __type;
	}

	/**
	 * Decodes an array of ADPCM samples encoded for YAMAHA YMZ/AICA chips.
	 *
	 * @param __adpcm The data array containing AICA ADPCM samples.
	 * @param __offset The offset from which to start reading the data array
	 * from.
	 * @param __length The Amount of bytes to read from the data array, starting
	 * at {@code __offset}.
	 * @return The decoded samples in INT_S16LE format.
	 * @throws ArrayIndexOutOfBoundsException if {@code __offset} is
	 * negative, or if {@code __offset + __length > __adpcm.length}.
	 * @throws IllegalArgumentException if {@code __length} is negative,
	 * or if {@code __left} or {@code __right} is a non-number or is negative.
	 * @throws NullPointerException If {@code __adpcm} is {@code null}.
	 * @since 2025/05/05
	 */
	@SquirrelJMEVendorApi
	static int[] __decodeAICA(@NotNull byte[] __adpcm,
		@Range(from = 0, to = Integer.MAX_VALUE) int __offset,
		@Range(from = 0, to = Integer.MAX_VALUE) int __length)
		throws ArrayIndexOutOfBoundsException, IllegalArgumentException,
		NullPointerException
	{
		if (__adpcm == null)
			throw new NullPointerException("NARG");

		if (__length < 0)
			throw new IllegalArgumentException("NEGV");

		if (__offset < 0 || __offset + __length > __adpcm.length)
			throw new ArrayIndexOutOfBoundsException("IOOB");

		int[] ret = new int[__length * 2];
		//  Quantization step size
		int An = 127;
		//  Predictor
		int Xn = 0;
		
		// Process all ADPCM bytes
		for (int src = __offset, dest = 0; src < __offset + __length; src++)
		{
			int bits = __adpcm[src] & 0xFF;
			
			// Process both nibbles
			for (int n = 0; n < 2; n++, bits >>= 4, dest++)
			{
				// Compute the next output sample
				ret[dest] = Xn = Math.min(
					Math.max((1 - ((bits & 8) >> 2)) *             // Sign
						Math.min(Math.max((((bits & 7) << 1) | 1) * An >> 3
							// Magnitude
							, 0), 32767) + Xn * 254 / 255
					// Accumulate
					, -32768), 32767);
				
				// Compute the next quantization step size
				An = Math.min(Math.max(MA3SamplerProvider.AICA_STEPS[bits & 7] *
					An >> 8, 127), 24576);
			}
		}
		
		// The hardware will perform interpolation and low-pass filter at this
		// point to smooth out the waveform, but the parameters are not known.
		
		return ret;
	}
	
	/**
	 * Decodes wave rom banks from the given {@link RomData} object.
	 *
	 * @param __roms The {@link RomData} containing wave banks.
	 * @return A set of wave banks.
	 * @throws NullPointerException If {@code __roms} is {@code null}.
	 * @since 2025/05/05
	 */
	private static int[][] __waveRom(@NotNull RomData __roms)
		throws NullPointerException
	{
		if (__roms == null)
			throw new NullPointerException("NARG");

		int[][] ret = new int[8][];
		for (int x = 0, n = __roms.count; x < n; x++)
		{
			byte[] adpcm = __roms.bytes(x);
			ret[x] = MA3SamplerProvider.__decodeAICA(adpcm, 0, adpcm.length);
		}
		return ret;
	}
}
