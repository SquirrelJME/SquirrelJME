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

/**
 * Sequencer event data class
 *
 * @since 2025/05/05
 */
@SquirrelJMEVendorApi
class __MLDEvent__
	implements BasicEvent
{
	/** Program Bank to set with this event, if it is a Bank Change event. */
	@SquirrelJMEVendorApi
	int bank;
	
	/** Normalized channel ID, out of 32/16 depending on the FM Op count. */
	@SquirrelJMEVendorApi
	int channel;
	
	/** Note channel index (0..3 within parent track). */
	@SquirrelJMEVendorApi
	int channelIndex;
	
	/** Cuepoint for this event, if it is a Cuepoint event. */
	@SquirrelJMEVendorApi
	int cuepoint;

	/** Byte array for ext-info and unknown events. */
	@SquirrelJMEVendorApi
	byte[] data;
	
	/** Number of ticks since last event. */
	@SquirrelJMEVendorApi
	int delta;
	
	/** Enable flag for this event, if it is a Drum Enable event. */
	@SquirrelJMEVendorApi
	boolean enable;
	
	/** Number of ticks until note off. */
	@SquirrelJMEVendorApi
	int gateTime;
	
	/** Meta event ID. */
	@SquirrelJMEVendorApi
	int id;
	
	/** Amount of jumps to do, if this is a Jump event. */
	@SquirrelJMEVendorApi
	int jumpCount;
	
	/** ID of the Jump event. */
	@SquirrelJMEVendorApi
	int jumpId;
	
	/** Playback position to jump back to. */
	@SquirrelJMEVendorApi
	int jumpPoint;
	
	/** Normalized key ID, relative to A4. */
	@SquirrelJMEVendorApi
	int key;
	
	/** Base key index. */
	@SquirrelJMEVendorApi
	int keyNumber;
	
	/** Number of octaves to adjust keyNumber by. */
	@SquirrelJMEVendorApi
	int octaveShift;
	
	/** Starting location in MLD resource. */
	@SquirrelJMEVendorApi
	int offset;
	
	/** Panning value, if this is a Panpot event. */
	@SquirrelJMEVendorApi
	float panpot;
	
	/** Event parameter, effectively the value for a wide range of events. */
	@SquirrelJMEVendorApi
	int param;
	
	/** Program to set with this event, if it is a Program Change event. */
	@SquirrelJMEVendorApi
	int program;
	
	/** Range to set with this event, if it's a Pitch Bend Range event. */
	@SquirrelJMEVendorApi
	float range;
	
	/** Semitone value to set with this event, if it is a Pitch Bend event. */
	@SquirrelJMEVendorApi
	float semitones;

	/** Note status. */
	@SquirrelJMEVendorApi
	int status;
	
	/** Tempo to set with this event, if it is a Timebase-Tempo event. */
	@SquirrelJMEVendorApi
	int tempo;
	
	/** Timebase to set with this event, if it is a Timebase-Tempo event. */
	@SquirrelJMEVendorApi
	int timebase;
	
	/** Event category. */
	@SquirrelJMEVendorApi
	int type;
	
	/** Note velocity. */
	@SquirrelJMEVendorApi
	float velocity;
	
	/** Volume to set with this event, if it is a Volume event. */
	@SquirrelJMEVendorApi
	float volume;
}
