// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.jblend.media;

import cc.squirreljme.runtime.cldc.annotation.Api;

public interface MediaPlayerListener
{
	@Api
	void playerStateChanged(MediaPlayer var1);
	
	@Api
	void playerRepeated(MediaPlayer var1);
}

