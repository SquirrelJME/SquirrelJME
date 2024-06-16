// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.media;

import javax.microedition.media.TimeBase;

/**
 * The position within a track for a song or otherwise.
 *
 * @since 2022/04/27
 */
public final class TrackPosition
{
	/** The time base to use. */
	public volatile TimeBase timeBase;
	
	/** The base time within the time base in which the track started. */
	public volatile long basisMicros;
	
	/** The time the track was stopped at. */
	public volatile long stoppedMicros;
}
