// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.keitaiwiki.music;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Output channel
 */
class MA3Channel
	implements BasicChannel
{
	
	
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
	 * Index in sampler
	 */
	final int index;
	
	/**
	 * Encapsulating instance
	 */
	final MA3SamplerProvider.Instance instance;
	
	/**
	 * The channel plays drum notes
	 */
	boolean isDrum;
	
	/**
	 * All notes currently on keys
	 */
	final MA3Note[] notesOn;
	
	final ArrayList<MA3Note> notesOut;
	// All notes that are generating output
	
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
	
	
	MA3Channel(MA3SamplerProvider.Instance instance, int index)
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
		this.volLeftOut = this.instance.volOut * this.volLeft;
		this.volRightOut = this.instance.volOut * this.volRight;
		for (MA3Note note : this.notesOut)
			note.onVolume();
	}
	
	/**
	 * Render the next input sample
	 */
	void render()
	{
		for (int x = 0; x < this.notesOut.size(); x++)
		{
			if (this.notesOut.get(x).render())
				this.notesOut.remove(x--);
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
