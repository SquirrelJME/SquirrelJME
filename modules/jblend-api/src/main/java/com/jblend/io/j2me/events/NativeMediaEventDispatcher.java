// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.jblend.io.j2me.events;

public interface NativeMediaEventDispatcher
{
	@Api
	void stateChanged(int var1);
	
	@Api
	void repeated(int var1);
	
	@Api
	int getMediaType();
}

