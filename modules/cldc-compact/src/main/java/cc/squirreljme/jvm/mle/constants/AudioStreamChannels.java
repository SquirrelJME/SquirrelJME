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
 * Represents the position of an audio source.
 *
 * @since 2025/05/04
 */
@SquirrelJMEVendorApi
public interface AudioStreamChannels
{
	/** Mono audio. */
	@SquirrelJMEVendorApi
	byte MONO =
		1;
	
	/** Stereo. */
	@SquirrelJMEVendorApi
	byte STEREO =
		2;
	
	/** Basic surround sound. */
	@SquirrelJMEVendorApi
	byte BASIC_SURROUND =
		4;
	
	/** Full surround sound. */
	@SquirrelJMEVendorApi
	byte FULL_SURROUND =
		8;
}
