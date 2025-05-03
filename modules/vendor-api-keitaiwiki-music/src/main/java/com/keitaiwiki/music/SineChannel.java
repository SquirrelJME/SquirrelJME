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

/**
 * Output channel
 */
class SineChannel
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
	int index;
	
	/**
	 * All notes currently on keys
	 */
	SineNote[] notesOn;
	
	/**
	 * All notes that are generating output
	 */
	ArrayList<SineNote> notesOut;
	
	/**
	 * Left stereo amplitude
	 */
	float volLeft;
	
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
}
