// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.constants;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;

/**
 * Represents the format of an audio stream.
 *
 * @since 2025/05/04
 */
@SquirrelJMEVendorApi
public interface AudioStreamFormat
{
	/** Unsigned 8-bit PCM. */
	@SquirrelJMEVendorApi
	byte U8_BYTE =
		0;
	
	/** 8-bit a-law. */
	@SquirrelJMEVendorApi
	byte ALAW_BYTE =
		1;
	
	/** 8-bit mu-law. */
	@SquirrelJMEVendorApi
	byte ULAW_BYTE =
		2;
	
	/** Signed 16-bit. */
	@SquirrelJMEVendorApi
	byte S16_SHORT =
		3;
	
	/** Signed 24-bit, as integer type. */
	@SquirrelJMEVendorApi
	byte S24_INT =
		4;
	
	/** Signed 32-bit. */
	@SquirrelJMEVendorApi
	byte S32_INT =
		5;
	
	/** 32-bit floating point. */
	@SquirrelJMEVendorApi
	byte F32_FLOAT =
		6;
	
	/** The number of audio formats. */
	@SquirrelJMEVendorApi
	byte NUM_FORMATS =
		7;
}
