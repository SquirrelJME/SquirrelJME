// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.media.control;

import cc.squirreljme.runtime.cldc.annotation.Api;
import javax.microedition.media.Control;

@Api
public interface ToneControl
	extends Control
{
	@Api
	byte BLOCK_END =
		-6;
	
	@Api
	byte BLOCK_START =
		-5;
	
	@Api
	byte C4 =
		60;
	
	@Api
	byte PLAY_BLOCK =
		-7;
	
	@Api
	byte REPEAT =
		-9;
	
	@Api
	byte RESOLUTION =
		-4;
	
	@Api
	byte SET_VOLUME =
		-8;
	
	@Api
	byte SILENCE =
		-1;
	
	@Api
	byte TEMPO =
		-3;
	
	@Api
	byte VERSION =
		-2;
	
	@Api
	void setSequence(byte[] __a);
}


