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
	/** Automatic. */
	@SquirrelJMEVendorApi
	byte AUTOMATIC =
		-1;
	
	/** Unsigned 8-bit PCM. */
	@SquirrelJMEVendorApi
	byte BYTE_U8 =
		0;
	
	/** Signed 16-bit. */
	@SquirrelJMEVendorApi
	byte SHORT_S16 =
		1;
	
	/** Signed 32-bit. */
	@SquirrelJMEVendorApi
	byte INT_S32 =
		2;
	
	/** 32-bit floating point. */
	@SquirrelJMEVendorApi
	byte FLOAT_F32 =
		3;
	
	/** The number of audio formats. */
	@SquirrelJMEVendorApi
	byte NUM_FORMATS =
		4;
}
