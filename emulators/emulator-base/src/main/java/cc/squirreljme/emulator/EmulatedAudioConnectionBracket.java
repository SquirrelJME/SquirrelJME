// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.emulator;

import cc.squirreljme.jvm.mle.brackets.AudioStreamConnectionBracket;
import cc.squirreljme.jvm.mle.callbacks.AudioStreamRenderer;
import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * Emulated audio stream connection.
 *
 * @since 2025/05/18
 */
public class EmulatedAudioConnectionBracket
	implements AudioStreamConnectionBracket
{
	/** The state pointer. */
	protected final long statePtr;
	
	/** The renderer pointer. */
	protected final long rendererPtr;
	
	/** The audio stream bracket. */
	protected final EmulatedAudioStreamBracket stream;
	
	/** The renderer to call. */
	protected final AudioStreamRenderer renderer;
	
	/**
	 * Initializes the audio connection.
	 *
	 * @param __statePtr The state pointer.
	 * @param __rendererPtr The renderer pointer.
	 * @param __stream The stream this is connected to.
	 * @param __renderer The renderer to call.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/05/25
	 */
	EmulatedAudioConnectionBracket(long __statePtr,
		long __rendererPtr, EmulatedAudioStreamBracket __stream,
		AudioStreamRenderer __renderer)
		throws NullPointerException
	{
		if (__statePtr == 0 || __rendererPtr == 0 || __stream == null ||
			__renderer == null)
			throw new NullPointerException("NARG");
		
		this.statePtr = __statePtr;
		this.rendererPtr = __rendererPtr;
		this.stream = __stream;
		this.renderer = __renderer;
	}
}
