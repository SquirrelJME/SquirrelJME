// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.nttdocomo.ui;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.debug.Debugging;

@Api
public class MediaManager
{
	@SuppressWarnings("FinalStaticMethod")
	@Api
	public static final MediaData getData(String __uri)
	{
		throw Debugging.todo();
	}
	
	@SuppressWarnings("FinalStaticMethod")
	@Api
	public static final MediaImage getImage(String __uri)
		throws NullPointerException
	{
		if (__uri == null)
			throw new NullPointerException("NARG");
		
		return new __MIDPImage__(__uri);
	}
	
	@SuppressWarnings("FinalStaticMethod")
	@Api
	public static final MediaSound getSound(String __uri)
	{
		throw Debugging.todo();
	}
}
