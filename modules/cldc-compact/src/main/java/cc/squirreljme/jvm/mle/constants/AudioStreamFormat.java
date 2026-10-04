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
 * Represents the format of an audio stream.
 *
 * @since 2025/05/04
 */
@SquirrelJMENativeApi(min = "0.4.0")
public interface AudioStreamFormat
{
	/** Automatic. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte AUTOMATIC =
		-1;
	
	/** Unsigned 8-bit PCM. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte BYTE_U8 =
		0;
	
	/** Signed 16-bit. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte SHORT_S16 =
		1;
	
	/** Signed 32-bit. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte INT_S32 =
		2;
	
	/** 32-bit floating point. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte FLOAT_F32 =
		3;
	
	/** The number of audio formats. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte NUM_FORMATS =
		4;
}
