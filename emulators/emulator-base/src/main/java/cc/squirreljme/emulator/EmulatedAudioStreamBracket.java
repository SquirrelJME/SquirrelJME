// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.emulator;

import cc.squirreljme.jvm.mle.brackets.AudioStreamBracket;

/**
 * Bracket for emulated audio streams.
 *
 * @since 2025/05/07
 */
public class EmulatedAudioStreamBracket
	implements AudioStreamBracket
{
	/** The state pointer. */
	protected final long statePtr;
	
	/** The stream pointer. */
	protected final long streamPtr;
	
	/**
	 * Initializes the emulated audio stream.
	 *
	 * @param __statePtr The state pointer.
	 * @param __streamPtr The stream pointer.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/05/18
	 */
	public EmulatedAudioStreamBracket(long __statePtr, long __streamPtr)
		throws NullPointerException
	{
		if (__statePtr == 0 || __streamPtr == 0)
			throw new NullPointerException("NARG");
		
		this.statePtr = __statePtr;
		this.streamPtr = __streamPtr;
	}
}
