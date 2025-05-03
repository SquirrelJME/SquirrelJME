// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.keitaiwiki.music;

/**
 * Music note
 */
class SineNote
	implements BasicNote
{
	/**
	 * Amount to increment phase per frame
	 */
	float advance;
	
	/**
	 * Encapsulating channel
	 */
	SineChannel channel;
	
	/**
	 * Base frequency
	 */
	float freqBase;
	
	/**
	 * Note is currently active on its key
	 */
	boolean playing;
	
	/**
	 * Base volume
	 */
	float volBase;
	
	/**
	 * Current left stereo volume
	 */
	float volLeftLevel;
	
	/**
	 * Target left stereo volume
	 */
	float volLeftTarget;
	
	/**
	 * Current right stereo volume
	 */
	float volRightLevel;
	
	/**
	 * Target right stereo volume
	 */
	float volRightTarget;
	
	/**
	 * Position in wave period
	 */
	float wavPhase;
}
