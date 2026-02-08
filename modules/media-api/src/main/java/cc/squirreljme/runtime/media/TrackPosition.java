// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.media;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import javax.microedition.media.Player;
import javax.microedition.media.TimeBase;

/**
 * The position within a track for a song or otherwise.
 *
 * @since 2022/04/27
 */
@SquirrelJMEVendorApi
public final class TrackPosition
{
	/** The time base to use. */
	@SquirrelJMEVendorApi
	public volatile TimeBase timeBase;
	
	/** The base time within the time base in which the track started. */
	@SquirrelJMEVendorApi
	public volatile long basisMicros;
	
	/** The time the track was stopped at. */
	@SquirrelJMEVendorApi
	public volatile long stoppedMicros =
		Player.TIME_UNKNOWN;
	
	/** The currently tracked microseconds. */
	@SquirrelJMEVendorApi
	public volatile long trackMicros =
		Player.TIME_UNKNOWN;
}
