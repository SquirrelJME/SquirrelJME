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

/**
 * MLD ADPCM sample data class
 *
 * @since 2025/05/05
 */
class __MLDADPCM__
	implements BasicSampleData
{
	/**
	 * Bit depth of data, can be either 4-bit (standard ADPCM affair), or
	 * 2-bit, which was never found in use, and is supposedly for voice data.
	 */
	int _bitDepth;

	/** Data chunk containing the raw ADPCM data. */
	byte[] _data;

	/** Are the channels interleaved? (never found in use) */
	boolean _isInterleaved;

	/** Must be 1 for mono or 2 for stereo, other values are unknown. */
	int _numChannels;

	/**
	 * Data sample rate, it's read as an unsigned byte, and multiplied by 1000
	 * before being assigned into this variable. Usually it's either of 8000Hz,
	 * 16000Hz, or 32000Hz. No other sample rates have been found in use.
	 */
	int _sampleRate;
}
