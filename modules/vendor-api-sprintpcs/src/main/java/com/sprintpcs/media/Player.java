// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.sprintpcs.media;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.debug.Debugging;

@Api
public class Player
{
	@Api
	public static void addPlayerListener(PlayerListener __listener)
	{
		throw Debugging.todo();
	}
	
	@Api
	public static void play(Clip __clip, int __repeat)
		throws IllegalArgumentException
	{
		throw Debugging.todo();
	}
		
	@Api
	public static void play(DualTone __tone, int __repeat)
		throws IllegalArgumentException
	{
		Debugging.todoNote("play(%s, %d)", __tone, __repeat);
	}
	
	@Api
	public static void playBackground(Clip __clip, int __repeat)
		throws IllegalArgumentException
	{
		throw Debugging.todo();
	}
	
	@Api
	public static void resume()
	{
		throw Debugging.todo();
	}
	
	@Api
	public static void stop()
	{
		throw Debugging.todo();
	}
}
