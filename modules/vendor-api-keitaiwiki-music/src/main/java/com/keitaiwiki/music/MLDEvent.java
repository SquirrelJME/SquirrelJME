// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Keitai Wiki Community Music Implementation
//     Originally written and contributed by Guy Perfect
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

/**
 * Sequencer event data class
 */
@SquirrelJMEVendorApi
class MLDEvent
	implements BasicEvent
{
	/**
	 * ext-B fields
	 */
	@SquirrelJMEVendorApi
	int bank;
	
	/**
	 * Normalized channel ID, out of 16
	 */
	@SquirrelJMEVendorApi
	int channel;
	
	/**
	 * Note fields
	 * Channel index 0..3 within parent track
	 */
	@SquirrelJMEVendorApi
	int channelIndex;
	
	@SquirrelJMEVendorApi
	int cuepoint;
	
	/**
	 * ext-info and unknown event data
	 */
	@SquirrelJMEVendorApi
	byte[] data;
	
	/**
	 * Time delta: number of ticks since last event
	 */
	@SquirrelJMEVendorApi
	int delta;
	
	@SquirrelJMEVendorApi
	boolean enable;
	
	/**
	 * Number of ticks until note off
	 */
	@SquirrelJMEVendorApi
	int gateTime;
	
	/**
	 * Meta event ID
	 */
	@SquirrelJMEVendorApi
	int id;
	
	@SquirrelJMEVendorApi
	int jumpCount;
	
	@SquirrelJMEVendorApi
	int jumpId;
	
	@SquirrelJMEVendorApi
	int jumpPoint;
	
	/**
	 * Normalized key ID, relative to A4
	 */
	@SquirrelJMEVendorApi
	int key;
	
	/**
	 * Base key index
	 */
	@SquirrelJMEVendorApi
	int keyNumber;
	
	/**
	 * Number of octaves to adjust keyNumber
	 */
	@SquirrelJMEVendorApi
	int octaveShift;
	
	/**
	 * Location in MLD asset
	 */
	@SquirrelJMEVendorApi
	int offset;
	
	@SquirrelJMEVendorApi
	float panpot;
	
	/**
	 * Event parameter bits
	 */
	@SquirrelJMEVendorApi
	int param;
	
	@SquirrelJMEVendorApi
	int program;
	
	@SquirrelJMEVendorApi
	float range;
	
	@SquirrelJMEVendorApi
	float semitones;
	
	/**
	 * note-status, second byte of event data
	 */
	@SquirrelJMEVendorApi
	int status;
	
	@SquirrelJMEVendorApi
	int tempo;
	
	@SquirrelJMEVendorApi
	int timebase;
	
	/**
	 * Event category
	 */
	@SquirrelJMEVendorApi
	int type;
	
	/**
	 * Base volume
	 */
	@SquirrelJMEVendorApi
	float velocity;
	
	@SquirrelJMEVendorApi
	float volume;
}
