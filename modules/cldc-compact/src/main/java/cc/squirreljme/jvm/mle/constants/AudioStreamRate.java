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
 * Audio stream rates.
 *
 * @since 2025/05/07
 */
@SquirrelJMENativeApi(min = "0.4.0")
public interface AudioStreamRate
{
	/** Automatic. */
	@SquirrelJMENativeApi(min = "0.4.0")
	int AUTOMATIC =
		-1;
	
	/** 8000 Hz. */
	@SquirrelJMENativeApi(min = "0.4.0")
	int HZ_8000 =
		8000;
	
	/** 11025 Hz. */
	@SquirrelJMENativeApi(min = "0.4.0")
	int HZ_11025 =
		11025;
	
	/** 16000 Hz. */
	@SquirrelJMENativeApi(min = "0.4.0")
	int HZ_16000 =
		16000;
	
	/** 22050 Hz. */
	@SquirrelJMENativeApi(min = "0.4.0")
	int HZ_22050 =
		22050;
	
	/** 24000 Hz. */
	@SquirrelJMENativeApi(min = "0.4.0")
	int HZ_24000 =
		24000;
	
	/** 44100 Hz. */
	@SquirrelJMENativeApi(min = "0.4.0")
	int HZ_44100 =
		44100;
	
	/** 48000 Hz. */
	@SquirrelJMENativeApi(min = "0.4.0")
	int HZ_48000 =
		48000;
	
	/** Maximum supported sample rate. */
	@SquirrelJMENativeApi(min = "0.4.0")
	int MAX_SAMPLE_RATE =
		384000;
}
