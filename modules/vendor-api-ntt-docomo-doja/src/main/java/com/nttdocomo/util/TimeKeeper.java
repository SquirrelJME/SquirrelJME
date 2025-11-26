// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.nttdocomo.util;

import cc.squirreljme.runtime.cldc.annotation.Api;

@Api
public interface TimeKeeper
{
	@Api
	void dispose();
	
	@Api
	int getMinTimeInterval();
	
	@Api
	int getResolution();
	
	@Api
	void start();
	
	@Api
	void stop();
}
