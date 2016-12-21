// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package javax.microedition.media;


public interface PlayerListener
{
	public static final String BUFFERING_STARTED =
		"bufferingStarted";
	
	public static final String BUFFERING_STOPPED =
		"bufferingStopped";
	
	public static final String CLOSED =
		"closed";
	
	public static final String DEVICE_AVAILABLE =
		"deviceAvailable";
	
	public static final String DEVICE_UNAVAILABLE =
		"deviceUnavailable";
	
	public static final String DURATION_UPDATED =
		"durationUpdated";
	
	public static final String END_OF_MEDIA =
		"endOfMedia";
	
	public static final String ERROR =
		"error";
	
	public static final String RECORD_ERROR =
		"recordError";
	
	public static final String RECORD_STARTED =
		"recordStarted";
	
	public static final String RECORD_STOPPED =
		"recordStopped";
	
	public static final String SIZE_CHANGED =
		"sizeChanged";
	
	public static final String STARTED =
		"started";
	
	public static final String STOPPED =
		"stopped";
	
	public static final String STOPPED_AT_TIME =
		"stoppedAtTime";
	
	public static final String VOLUME_CHANGED =
		"volumeChanged";
	
	public abstract void playerUpdate(Player __a, String __b, Object __c);
}


