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
 * Not Described.
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
	
	/** The name of the stream. */
	protected final String name;
	
	/** The format. */
	protected final int format;
	
	/** The rate. */
	protected final int rate;
	
	/** The number of channels. */
	protected final int channels;
	
	/**
	 * Initializes the emulated audio stream.
	 *
	 * @param __statePtr The state pointer.
	 * @param __streamPtr The stream pointer.
	 * @param __name The stream name.
	 * @param __format The stream format.
	 * @param __rate The stream rate.
	 * @param __channels The stream channels.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/05/18
	 */
	public EmulatedAudioStreamBracket(long __statePtr, long __streamPtr,
		String __name, int __format, int __rate, int __channels)
		throws NullPointerException
	{
		if (__statePtr == 0 || __streamPtr == 0 || __name == null)
			throw new NullPointerException("NARG");
		
		this.statePtr = __statePtr;
		this.streamPtr = __streamPtr;
		this.name = __name;
		this.format = __format;
		this.rate = __rate;
		this.channels = __channels;
	}
}
