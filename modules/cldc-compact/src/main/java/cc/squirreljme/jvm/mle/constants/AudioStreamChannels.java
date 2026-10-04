// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.constants;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMENativeApi;

/**
 * Represents the position of an audio source.
 *
 * @since 2025/05/04
 */
@SquirrelJMENativeApi(min = "0.4.0")
public interface AudioStreamChannels
{
	/** Automatic. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte AUTOMATIC =
		-1;
	
	/** Mono audio. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte MONO =
		1;
	
	/** Stereo. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte STEREO =
		2;
	
	/** Basic surround sound. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte BASIC_SURROUND =
		4;
	
	/** Full surround sound. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte FULL_SURROUND =
		8;
}
