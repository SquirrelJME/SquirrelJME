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

import cc.squirreljme.runtime.cldc.annotation.KeepWhenCompacting;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;

/**
 * Notifies of a scenario that arises during playback. When configured,
 * the
 * {@code render()} methods will terminate early any time an event
 * condition is satisfied. Events are obtained by the caller and
 * acknowledged via {@link MLDPlayer#getEvents()}.
 *
 * @see MLDPlayer#getEvents()
 */
@SquirrelJMEVendorApi
public class MLDPlayerEvent
	implements BasicEvent
{
	
	/**
	 * Additional event data, if relevant. For {@code EVENT_KEY} events,
	 * this will be the key number.
	 */
	@SquirrelJMEVendorApi
	public final int data;
	
	/**
	 * Time in seconds since the beginning of playback when the event was
	 * raised.
	 */
	@SquirrelJMEVendorApi
	public final double time;
	
	/**
	 * Indicates the type of event that was raised: {@code EVENT_END},
	 * {@code EVENT_KEY} or {@code EVENT_LOOP}.
	 */
	@SquirrelJMEVendorApi
	public final int type;
	
	/**
	 * Internal constructor
	 */
	@KeepWhenCompacting
	MLDPlayerEvent(double time, int type, int data)
	{
		this.data = data;
		this.time = time;
		this.type = type;
	}
}
