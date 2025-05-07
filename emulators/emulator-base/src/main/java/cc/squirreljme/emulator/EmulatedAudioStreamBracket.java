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
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.SourceDataLine;
import org.jetbrains.annotations.NotNull;

/**
 * Not Described.
 *
 * @since 2025/05/07
 */
public class EmulatedAudioStreamBracket
	implements AudioStreamBracket
{
	/** The format used. */
	final AudioFormat format;
	
	/** The audio line. */
	final SourceDataLine line;
	
	/** The channel mapping. */
	final int[] channels;
	
	/**
	 * Initializes the native audio.
	 *
	 * @param __format The format used.
	 * @param __line The line used.
	 * @param __channels The channels used.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/05/07
	 */
	public EmulatedAudioStreamBracket(AudioFormat __format,
		SourceDataLine __line, int[] __channels)
		throws NullPointerException
	{
		if (__format == null || __line == null || __channels == null)
			throw new NullPointerException("NARG");
		
		this.format = __format;
		this.line = __line;
		this.channels = __channels;
	}
}
