// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.sprintpcs.util;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.debug.Debugging;

@Api
public class Muglet
{
	@Api
	public String getMediaType()
	{
		throw Debugging.todo();
	}
	
	@Api
	public String getReferringURI()
	{
		throw Debugging.todo();
	}
	
	@Api
	public String getURI()
	{
		throw Debugging.todo();
	}
	
	@Api
	public static Muglet getMuglet()
	{
		throw Debugging.todo();
	}
}
