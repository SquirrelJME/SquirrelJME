// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.vodafone.v10.system.device;

import cc.squirreljme.runtime.cldc.annotation.Api;

public interface MailListener
{
	@Api
	int SMS = 1;
	
	@Api
	int MMS = 2;
	
	@Api
	int CBS = 3;
	
	@Api
	int WEB = 4;
	
	@Api
	void received(int var1);
}
