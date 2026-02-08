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
import cc.squirreljme.jvm.mle.exceptions.MLECallError;

/**
 * Bracket for emulated audio streams.
 *
 * @since 2025/05/07
 */
public class EmulatedAudioStreamBracket
	extends EmulatedAudioConnectionBracket
	implements AudioStreamBracket
{
	/**
	 * Initializes the emulated audio stream.
	 *
	 * @param __statePtr The state pointer.
	 * @param __streamPtr The stream pointer.
	 * @throws MLECallError On null arguments.
	 * @since 2025/05/18
	 */
	public EmulatedAudioStreamBracket(long __statePtr, long __streamPtr)
		throws MLECallError
	{
		super(__statePtr, __streamPtr);
	}
}
