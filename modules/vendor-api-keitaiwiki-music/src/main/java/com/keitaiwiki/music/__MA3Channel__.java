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
import java.util.ArrayList;
import java.util.Arrays;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

/**
 * Represents a Yamaha MA-3 Output Channel.
 *
 * @since 2025/05/05
 */
@SquirrelJMEVendorApi
class __MA3Channel__
	implements BasicChannel
{
	/** Index in sampler */
	@SquirrelJMEVendorApi
	final int index;
	
	/** Encapsulating instance */
	@SquirrelJMEVendorApi
	final MA3Sampler instance;
	
	/** All notes currently on keys */
	@SquirrelJMEVendorApi
	final __MA3Note__[] notesOn;
	
	/** Notes to be rendered. */
	@SquirrelJMEVendorApi
	final ArrayList<__MA3Note__> notesOut;
	
	/** Pitch bend base ratio */
	@SquirrelJMEVendorApi
	float bendBase;
	
	/** Effective channel frequency ratio */
	@SquirrelJMEVendorApi
	float bendOut;
	
	/** Pitch bend magnitude */
	@SquirrelJMEVendorApi
	float bendRange;
	
	/** The channel plays drum notes */
	@SquirrelJMEVendorApi
	boolean isDrum;
	
	/** Program bank */
	@SquirrelJMEVendorApi
	int prgBank;
	
	/** Program index in bank */
	@SquirrelJMEVendorApi
	int prgProgram;
	
	/** Left stereo amplitude */
	@SquirrelJMEVendorApi
	float volLeft;
	
	/** Left stereo output amplitude */
	@SquirrelJMEVendorApi
	float volLeftOut;
	
	/** Channel output amplitude */
	@SquirrelJMEVendorApi
	float volLevel;
	
	/** Stereo level */
	@SquirrelJMEVendorApi
	float volPanning;
	
	/** Right stereo amplitude */
	@SquirrelJMEVendorApi
	float volRight;
	
	/** Right stereo output amplitude */
	@SquirrelJMEVendorApi
	float volRightOut;
	
	/**
	 * Creates a new MA-3 Output Channel with the specified channel index and
	 * {@link MA3Sampler} instnce.
	 *
	 * @param __instance The {@link MA3Sampler} instance to use for rendering.
	 * @param __index The channel index this new channel will occupy.
	 * @since 2025/05/05
	 */
	@SquirrelJMEVendorApi
	__MA3Channel__(@NotNull MA3Sampler __instance,
		@Range(from = 0, to = MA3Sampler.NUM_CHANNELS) int __index)
	{
		this.index = __index;
		this.instance = __instance;
		//  C-2 .. G8
		this.notesOn = new __MA3Note__[128];
		this.notesOut = new ArrayList<>();
	}
	
	/**
	 * This function is called whenever there must be a note frequency change
	 * in this channel.
	 *
	 * @since 2025/05/05
	 */
	@SquirrelJMEVendorApi
	void onFrequency()
	{
		float bend = this.instance.bendOut * this.bendOut;
		for (__MA3Note__ note : this.notesOut)
			note.onFrequency(this.bendOut);
	}
	
	/**
	 * This function is called whenever there must be a volume change in this
	 * channel.
	 *
	 * @since 2025/05/05
	 */
	@SquirrelJMEVendorApi
	void onVolume()
	{
		MA3Sampler instance = this.instance;
		
		this.volLeftOut = instance.volOut * this.volLeft;
		this.volRightOut = instance.volOut * this.volRight;
		for (__MA3Note__ note : this.notesOut)
			note.onVolume();
	}
	
	/**
	 * Renders the next audio sample.
	 *
	 * @since 2025/05/05
	 */
	@SquirrelJMEVendorApi
	void render()
	{
		ArrayList<__MA3Note__> _notesOut = this.notesOut;
		for (int x = 0; x < _notesOut.size(); x++)
		{
			if (_notesOut.get(x).render())
				_notesOut.remove(x--);
		}
	}

	/**
	 * Resets this channel to its initial state.
	 *
	 * @since 2025/05/05
	 */
	@SquirrelJMEVendorApi
	void reset()
	{
		this.bendBase = 0.0f;
		this.bendOut = 1.0f;
		this.bendRange = 2.0f;
		this.isDrum = false;
		this.prgBank = 0;
		this.prgProgram = 0;
		this.volLevel = 1.0f;
		this.volPanning = 0.5f;
		this.volLeft = 0.5f;
		this.volLeftOut = 0.5f;
		this.volRight = 0.5f;
		this.volRightOut = 0.5f;
		
		// Stop playing all notes (not calling note.onFrequency())
		Arrays.fill(this.notesOn, null);
		for (__MA3Note__ note : this.notesOut)
			note.stop();
	}
}
