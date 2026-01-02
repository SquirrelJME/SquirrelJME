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
 * Stores the time division because MIDI decided that every channel should
 * use the same timing setup, so that when a meta-message happens in any
 * track it will affect every single track.
 *
 * @since 2026/01/01
 */
public final class MidiTimeDiv
{
	/** The original tick division. */
	final long _tickDiv;
	
	/** The original nanoseconds per tick division. */
	final long _nanosPerTickDivOrig;
	
	/** The current nanoseconds per tick division, for ALL tracks. */
	volatile long _nanosPerTickDiv;
	
	/**
	 * Initializes the time division storage.
	 *
	 * @param __tickDiv The original tick division.
	 * @param __nanosPerTickDiv The original nanoseconds per tick division.
	 * @since 2026/01/01
	 */
	public MidiTimeDiv(long __tickDiv, long __nanosPerTickDiv)
	{
		this._tickDiv = __tickDiv;
		this._nanosPerTickDivOrig = __nanosPerTickDiv;
		this._nanosPerTickDiv = __nanosPerTickDiv;
	}
}
