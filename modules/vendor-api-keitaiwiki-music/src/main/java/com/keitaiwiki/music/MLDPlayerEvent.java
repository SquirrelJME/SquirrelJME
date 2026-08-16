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
import org.intellij.lang.annotations.MagicConstant;

/**
 * Notifies of a scenario that arises during playback. When configured,
 * the
 * {@code render()} methods will terminate early any time an event
 * condition is satisfied. Events are obtained by the caller and
 * acknowledged via {@link MLDPlayer#getEvents()}.
 *
 * @see MLDPlayer#getEvents()
 * @since 2025/05/05
 */
@SquirrelJMEVendorApi
public class MLDPlayerEvent
	implements BasicEvent
{
	/** Event type that notifies when a non-looping sequence finishes. */
	@SquirrelJMEVendorApi
	public static final int EVENT_END = 0;

	/** Event type that notifies when a particular key is played. */
	@SquirrelJMEVendorApi
	public static final int EVENT_KEY = 2;
	
	/** Event type that notifies when a sequence loops. */
	@SquirrelJMEVendorApi
	public static final int EVENT_LOOP = 1;

	/**
	 * Additional event data, if relevant. For {@link #EVENT_KEY} events,
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
	 * Indicates the type of event that was raised: {@link #EVENT_END},
	 * {@link #EVENT_KEY} or {@link #EVENT_LOOP}.
	 */
	@SquirrelJMEVendorApi
	public final int type;
	
	/**
	 * Creates a new player event.
	 *
	 * @param __time The time where the event occurred.
	 * @param __type The event's type. Must be one of: {@link #EVENT_END},
	 * {@link #EVENT_KEY} or {@link #EVENT_LOOP}.
	 * @param __data Additional event data.
	 * @since 2025/05/05
	 */
	@KeepWhenCompacting
	MLDPlayerEvent(double __time,
		@MagicConstant(valuesFromClass = MLDPlayerEvent.class) int __type,
		int __data)
	{
		this.data = __data;
		this.time = __time;
		this.type = __type;
	}
}
