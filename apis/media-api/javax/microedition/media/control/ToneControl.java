// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.media.control;

import javax.microedition.media.Control;

public interface ToneControl
	extends Control
{
	public static final byte BLOCK_END =
		-6;
	
	public static final byte BLOCK_START =
		-5;
	
	public static final byte C4 =
		60;
	
	public static final byte PLAY_BLOCK =
		-7;
	
	public static final byte REPEAT =
		-9;
	
	public static final byte RESOLUTION =
		-4;
	
	public static final byte SET_VOLUME =
		-8;
	
	public static final byte SILENCE =
		-1;
	
	public static final byte TEMPO =
		-3;
	
	public static final byte VERSION =
		-2;
	
	public abstract void setSequence(byte[] __a);
}


