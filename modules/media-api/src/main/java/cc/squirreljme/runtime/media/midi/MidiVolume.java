// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.media.midi;

/**
 * Stores the master volume of a MIDI player.
 *
 * @since 2026/05/15
 */
public final class MidiVolume
{
	/** The MIDI's current volume value. */
	byte _value;
	
	/**
	 * Initializes the MIDI's master volume.
	 *
	 * @param __volume The MIDI's master volume
	 * @since 2026/05/15
	 */
	public MidiVolume(byte __volume)
	{
		this._value = __volume;
	}
}
