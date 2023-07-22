// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.media;


import cc.squirreljme.runtime.cldc.annotation.Api;

@Api
public interface PlayerListener
{
	@Api
	String BUFFERING_STARTED =
		"bufferingStarted";
	
	@Api
	String BUFFERING_STOPPED =
		"bufferingStopped";
	
	@Api
	String CLOSED =
		"closed";
	
	@Api
	String DEVICE_AVAILABLE =
		"deviceAvailable";
	
	@Api
	String DEVICE_UNAVAILABLE =
		"deviceUnavailable";
	
	@Api
	String DURATION_UPDATED =
		"durationUpdated";
	
	@Api
	String END_OF_MEDIA =
		"endOfMedia";
	
	@Api
	String ERROR =
		"error";
	
	@Api
	String RECORD_ERROR =
		"recordError";
	
	@Api
	String RECORD_STARTED =
		"recordStarted";
	
	@Api
	String RECORD_STOPPED =
		"recordStopped";
	
	@Api
	String SIZE_CHANGED =
		"sizeChanged";
	
	@Api
	String STARTED =
		"started";
	
	@Api
	String STOPPED =
		"stopped";
	
	@Api
	String STOPPED_AT_TIME =
		"stoppedAtTime";
	
	@Api
	String VOLUME_CHANGED =
		"volumeChanged";
	
	@Api
	void playerUpdate(Player __player, String __eventType,
		Object __eventValue);
}


