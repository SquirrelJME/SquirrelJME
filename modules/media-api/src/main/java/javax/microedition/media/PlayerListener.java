// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.media;


public interface PlayerListener
{
	String BUFFERING_STARTED =
		"bufferingStarted";
	
	String BUFFERING_STOPPED =
		"bufferingStopped";
	
	String CLOSED =
		"closed";
	
	String DEVICE_AVAILABLE =
		"deviceAvailable";
	
	String DEVICE_UNAVAILABLE =
		"deviceUnavailable";
	
	String DURATION_UPDATED =
		"durationUpdated";
	
	String END_OF_MEDIA =
		"endOfMedia";
	
	String ERROR =
		"error";
	
	String RECORD_ERROR =
		"recordError";
	
	String RECORD_STARTED =
		"recordStarted";
	
	String RECORD_STOPPED =
		"recordStopped";
	
	String SIZE_CHANGED =
		"sizeChanged";
	
	String STARTED =
		"started";
	
	String STOPPED =
		"stopped";
	
	String STOPPED_AT_TIME =
		"stoppedAtTime";
	
	String VOLUME_CHANGED =
		"volumeChanged";
	
	void playerUpdate(Player __player, String __eventType, Object __eventValue);
}


