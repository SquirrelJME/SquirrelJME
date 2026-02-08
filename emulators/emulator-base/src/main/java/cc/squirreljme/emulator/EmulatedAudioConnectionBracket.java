// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.emulator;

import cc.squirreljme.jvm.mle.brackets.AudioConnectionBracket;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;

/**
 * Emulated audio stream connection.
 *
 * @since 2025/05/18
 */
public abstract class EmulatedAudioConnectionBracket
	implements AudioConnectionBracket
{
	/** The state pointer. */
	protected final long statePtr;
	
	/** The instance pointer. */
	protected final long instancePtr;
	
	/**
	 * Initializes the audio connection.
	 *
	 * @param __statePtr The state pointer.
	 * @param __instancePtr The instance pointer.
	 * @throws MLECallError On null arguments.
	 * @since 2025/05/25
	 */
	EmulatedAudioConnectionBracket(long __statePtr, long __instancePtr)
		throws MLECallError
	{
		if (__statePtr == 0 || __instancePtr == 0)
			throw new MLECallError("NARG");
		
		this.statePtr = __statePtr;
		this.instancePtr = __instancePtr;
	}
}
