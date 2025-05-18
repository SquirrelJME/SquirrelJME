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
 * Not Described.
 *
 * @since 2025/05/18
 */
public class EmulatedAudioConnectionBracket
	implements AudioStreamConnectionBracket
{
	EmulatedAudioConnectionBracket(long __statePtr,
		long __rendererPtr, EmulatedAudioStreamBracket __stream,
		AudioStreamRenderer __renderer)
	{
		throw Debugging.todo();
	}
}
