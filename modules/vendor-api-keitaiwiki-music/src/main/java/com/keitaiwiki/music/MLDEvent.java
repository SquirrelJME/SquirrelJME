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
 * Sequencer event data class
 */
class MLDEvent
	implements BasicEvent
{
	
	/**
	 * ext-B fields
	 */
	int bank;
	
	
	/**
	 * Normalized channel ID, out of 16
	 */
	int channel;
	
	/**
	 * Note fields
	 * Channel index 0..3 within parent track
	 */
	int channelIndex;
	
	int cuepoint;
	
	/**
	 * ext-info and unknown event data
	 */
	byte[] data;
	
	/**
	 * Time delta: number of ticks since last event
	 */
	int delta;
	
	boolean enable;
	
	/**
	 * Number of ticks until note off
	 */
	int gateTime;
	
	/**
	 * Meta event ID
	 */
	int id;
	
	int jumpCount;
	
	int jumpId;
	
	int jumpPoint;
	
	/**
	 * Normalized key ID, relative to A4
	 */
	int key;
	
	/**
	 * Base key index
	 */
	int keyNumber;
	
	/**
	 * Number of octaves to adjust keyNumber
	 */
	int octaveShift;
	
	/**
	 * Location in MLD asset
	 */
	int offset;
	
	float panpot;
	
	/**
	 * Event parameter bits
	 */
	int param;
	
	int program;
	
	float range;
	
	float semitones;
	
	/**
	 * note-status, second byte of event data
	 */
	int status;
	
	int tempo;
	
	int timebase;
	
	/**
	 * Event category
	 */
	int type;
	
	/**
	 * Base volume
	 */
	float velocity;
	
	float volume;
}
