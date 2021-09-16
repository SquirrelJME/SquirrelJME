// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.media.control;

import javax.microedition.media.Control;

public interface ToneControl
	extends Control
{
	byte BLOCK_END =
		-6;
	
	byte BLOCK_START =
		-5;
	
	byte C4 =
		60;
	
	byte PLAY_BLOCK =
		-7;
	
	byte REPEAT =
		-9;
	
	byte RESOLUTION =
		-4;
	
	byte SET_VOLUME =
		-8;
	
	byte SILENCE =
		-1;
	
	byte TEMPO =
		-3;
	
	byte VERSION =
		-2;
	
	void setSequence(byte[] __a);
}


