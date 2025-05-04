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
public interface AudioPositionType
{
	/** Mono audio. */
	@SquirrelJMEVendorApi
	byte MONO =
		0;
	
	/** Front left. */
	@SquirrelJMEVendorApi
	byte FRONT_LEFT =
		1;
	
	/** Front center. */
	@SquirrelJMEVendorApi
	byte FRONT_CENTER =
		2;
	
	/** Front right. */
	@SquirrelJMEVendorApi
	byte FRONT_RIGHT =
		3;
	
	/** Rear left. */
	@SquirrelJMEVendorApi
	byte REAR_LEFT =
		4;
	
	/** Rear center. */
	@SquirrelJMEVendorApi
	byte REAR_CENTER =
		5;
	
	/** Rear right. */
	@SquirrelJMEVendorApi
	byte REAR_RIGHT =
		6;
	
	/** Side left. */
	@SquirrelJMEVendorApi
	byte SIDE_LEFT =
		7;
	
	/** Side right. */
	@SquirrelJMEVendorApi
	byte SIDE_RIGHT =
		8;
	
	/** The number of audio positions. */
	@SquirrelJMEVendorApi
	byte NUM_POSITIONS =
		9;
}
