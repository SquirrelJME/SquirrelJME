// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.constants;

/**
 * Represents the format of an audio stream.
 *
 * @since 2025/05/04
 */
public interface AudioStreamFormat
{
	/** Automatic. */
	byte AUTOMATIC =
		-1;
	
	/** Unsigned 8-bit PCM. */
	byte BYTE_U8 =
		0;
	
	/** Signed 16-bit. */
	byte SHORT_S16 =
		1;
	
	/** Signed 32-bit. */
	byte INT_S32 =
		2;
	
	/** 32-bit floating point. */
	byte FLOAT_F32 =
		3;
	
	/** The number of audio formats. */
	byte NUM_FORMATS =
		4;
}
