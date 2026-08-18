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

import java.util.ArrayList;
import java.util.Arrays;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

/**
 * Represents a Yamaha MA-3 Output Channel.
 *
 * @since 2025/05/05
 */
class __MA3Channel__
	implements BasicChannel
{
	/** Index in sampler */
	final int _index;
	
	/** Encapsulating instance */
	final MA3Sampler _instance;
	
	/** All notes currently on keys */
	final __MA3Note__[] _notesOn;
	
	/** Notes to be rendered. */
	final ArrayList<__MA3Note__> _notesOut;
	
	/** Pitch bend base ratio */
	float _bendBase;
	
	/** Effective channel frequency ratio */
	float _bendOut;
	
	/** Pitch bend magnitude */
	float _bendRange;
	
	/** The channel plays drum notes */
	boolean _isDrum;
	
	/** Program bank */
	int _prgBank;
	
	/** Program index in bank */
	int _prgProgram;
	
	/** Left stereo amplitude */
	float _volLeft;
	
	/** Left stereo output amplitude */
	float _volLeftOut;
	
	/** Channel output amplitude */
	float _volLevel;
	
	/** Stereo level */
	float _volPanning;
	
	/** Right stereo amplitude */
	float _volRight;
	
	/** Right stereo output amplitude */
	float _volRightOut;
	
	/**
	 * Creates a new MA-3 Output Channel with the specified channel index and
	 * {@link MA3Sampler} instnce.
	 *
	 * @param __instance The {@link MA3Sampler} instance to use for rendering.
	 * @param __index The channel index this new channel will occupy.
	 * @since 2025/05/05
	 */
	__MA3Channel__(@NotNull MA3Sampler __instance,
		@Range(from = 0, to = MA3Sampler.NUM_CHANNELS) int __index)
	{
		this._index = __index;
		this._instance = __instance;
		//  C-2 .. G8
		this._notesOn = new __MA3Note__[128];
		this._notesOut = new ArrayList<>();
	}
	
	/**
	 * This function is called whenever there must be a note frequency change
	 * in this channel.
	 *
	 * @since 2025/05/05
	 */
	void __onFrequency()
	{
		float bend = this._instance._bendOut * this._bendOut;
		for (__MA3Note__ note : this._notesOut)
			note.__onFrequency(this._bendOut);
	}
	
	/**
	 * This function is called whenever there must be a volume change in this
	 * channel.
	 *
	 * @since 2025/05/05
	 */
	void __onVolume()
	{
		MA3Sampler instance = this._instance;
		
		this._volLeftOut = instance._volOut * this._volLeft;
		this._volRightOut = instance._volOut * this._volRight;
		for (__MA3Note__ note : this._notesOut)
			note.__onVolume();
	}
	
	/**
	 * Renders the next audio sample.
	 *
	 * @since 2025/05/05
	 */
	void __render()
	{
		ArrayList<__MA3Note__> _notesOut = this._notesOut;
		for (int x = 0; x < _notesOut.size(); x++)
		{
			if (_notesOut.get(x).__render())
				_notesOut.remove(x--);
		}
	}

	/**
	 * Resets this channel to its initial state.
	 *
	 * @since 2025/05/05
	 */
	void __reset()
	{
		this._bendBase = 0.0f;
		this._bendOut = 1.0f;
		this._bendRange = 2.0f;
		this._isDrum = false;
		this._prgBank = 0;
		this._prgProgram = 0;
		this._volLevel = 1.0f;
		this._volPanning = 0.5f;
		this._volLeft = 0.5f;
		this._volLeftOut = 0.5f;
		this._volRight = 0.5f;
		this._volRightOut = 0.5f;
		
		// Stop playing all notes (not calling note.__onFrequency())
		Arrays.fill(this._notesOn, null);
		for (__MA3Note__ note : this._notesOut)
			note.__stop();
	}
}
