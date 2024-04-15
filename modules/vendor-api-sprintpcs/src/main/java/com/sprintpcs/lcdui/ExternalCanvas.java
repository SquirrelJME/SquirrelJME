// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.sprintpcs.lcdui;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.debug.Debugging;

@Api
public class ExternalCanvas
{
	@Api
	public static final int AUTO_LCD =
		27;
	
	@Api
	public static final int EXTERNAL_A =
		82;
	
	@Api
	public static final int EXTERNAL_B =
		83;
	
	@Api
	public static final int EXTERNAL_C =
		84;
	
	@Api
	public static final int EXTERNAL_D =
		85;
	
	@Api
	public static final int KEY_FAST_FORWARD =
		86;
	
	@Api
	public static final int KEY_MUTE =
		87;
	
	@Api
	public static final int KEY_PAUSE =
		88;
	
	@Api
	public static final int KEY_PLAY =
		89;
	
	@Api
	public static final int KEY_PLAY_PAUSE =
		90;
	
	@Api
	public static final int KEY_REWIND =
		91;
	
	@Api
	public static final int KEY_STOP =
		92;
	
	@Api
	public static final int KEY_TRACK_BACK =
		94;
	
	@Api
	public static final int KEY_TRACK_FORWARD =
		93;
	
	@Api
	public static final int PRIMARY_LCD =
		28;
	
	@Api
	public static final int SECONDARY_LCD =
		29;

	@Api
	public void setLCD(int __mode)
		throws IllegalArgumentException
	{
		throw Debugging.todo();
	}
}
