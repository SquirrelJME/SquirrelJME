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
 * Sequencer event data class
 *
 * @since 2025/05/05
 */
class __MLDEvent__
	implements BasicEvent
{
	/** Program Bank to set with this event, if it is a Bank Change event. */
	int _bank;
	
	/** Normalized channel ID, out of 32/16 depending on the FM Op count. */
	int _channel;
	
	/** Note channel index (0..3 within parent track). */
	int _channelIndex;
	
	/** Cuepoint for this event, if it is a Cuepoint event. */
	int _cuepoint;

	/** Byte array for ext-info and unknown events. */
	byte[] _data;
	
	/** Number of ticks since last event. */
	int _delta;
	
	/** Enable flag for this event, if it is a Drum Enable event. */
	boolean _enable;
	
	/** Number of ticks until note off. */
	int _gateTime;
	
	/** Meta event ID. */
	int _id;
	
	/** Amount of jumps to do, if this is a Jump event. */
	int _jumpCount;
	
	/** ID of the Jump event. */
	int _jumpId;
	
	/** Playback position to jump back to. */
	int _jumpPoint;
	
	/** Normalized key ID, relative to A4. */
	int _key;
	
	/** Base key index. */
	int _keyNumber;
	
	/** Number of octaves to adjust keyNumber by. */
	int _octaveShift;
	
	/** Starting location in MLD resource. */
	int _offset;
	
	/** Panning value, if this is a Panpot event. */
	float _panpot;
	
	/** Event parameter, effectively the value for a wide range of events. */
	int _param;
	
	/** Program to set with this event, if it is a Program Change event. */
	int _program;
	
	/** Range to set with this event, if it's a Pitch Bend Range event. */
	float _range;
	
	/** Semitone value to set with this event, if it is a Pitch Bend event. */
	float _semitones;

	/** Note status. */
	int _status;
	
	/** Tempo to set with this event, if it is a Timebase-Tempo event. */
	int _tempo;
	
	/** Timebase to set with this event, if it is a Timebase-Tempo event. */
	int _timebase;
	
	/** Event category. */
	int _type;
	
	/** Note velocity. */
	float _velocity;
	
	/** Volume to set with this event, if it is a Volume event. */
	float _volume;
}
