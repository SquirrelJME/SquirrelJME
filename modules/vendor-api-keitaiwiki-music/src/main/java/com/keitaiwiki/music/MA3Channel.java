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

/**
 * Output channel
 */
class MA3Channel
	implements BasicChannel
{
	/**
	 * Index in sampler
	 */
	final int index;
	
	/**
	 * Encapsulating instance
	 */
	final MA3Sampler instance;
	
	/**
	 * All notes currently on keys
	 */
	final MA3Note[] notesOn;
	
	final ArrayList<MA3Note> notesOut;
	
	/**
	 * Pitch bend base ratio
	 */
	float bendBase;
	
	/**
	 * Effective channel frequency ratio
	 */
	float bendOut;
	
	/**
	 * Pitch bend magnitude
	 */
	float bendRange;
	
	/**
	 * The channel plays drum notes
	 */
	boolean isDrum;
	
	/**
	 * Program bank
	 */
	int prgBank;
	
	/**
	 * Program index in bank
	 */
	int prgProgram;
	
	/**
	 * Left stereo amplitude
	 */
	float volLeft;
	
	/**
	 * Left stereo output amplitude
	 */
	float volLeftOut;
	
	/**
	 * Channel output amplitude
	 */
	float volLevel;
	
	/**
	 * Stereo level
	 */
	float volPanning;
	
	/**
	 * Right stereo amplitude
	 */
	float volRight;
	
	/**
	 * Right stereo output amplitude
	 */
	float volRightOut;
	
	MA3Channel(MA3Sampler instance, int index)
	{
		this.index = index;
		this.instance = instance;
		//  C-2 .. G8
		this.notesOn = new MA3Note[128];
		this.notesOut = new ArrayList<>();
	}
	
	/**
	 * Frequency has changed
	 */
	void onFrequency()
	{
		float bend = this.instance.bendOut * this.bendOut;
		for (MA3Note note : this.notesOut)
			note.onFrequency(this.bendOut);
	}
	
	/**
	 * Volume has changed
	 */
	void onVolume()
	{
		MA3Sampler instance = this.instance;
		
		this.volLeftOut = instance.volOut * this.volLeft;
		this.volRightOut = instance.volOut * this.volRight;
		for (MA3Note note : this.notesOut)
			note.onVolume();
	}
	
	/**
	 * Render the next input sample
	 */
	void render()
	{
		ArrayList<MA3Note> notesOut = this.notesOut;
		for (int x = 0; x < notesOut.size(); x++)
		{
			if (notesOut.get(x).render())
				notesOut.remove(x--);
		}
	}
	
	/**
	 * Initialize state
	 */
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
		for (MA3Note note : this.notesOut)
			note.stop();
	}
	
}
