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

public interface MediaImageOperator
{
	@Api
	int getX();
	
	@Api
	int getY();
	
	@Api
	int getWidth();
	
	@Api
	int getHeight();
	
	@Api
	void setBounds(int var1, int var2, int var3, int var4);
	
	@Api
	int getOriginX();
	
	@Api
	int getOriginY();
	
	@Api
	void setOrigin(int var1, int var2);
	
	@Api
	int getMediaWidth();
	
	@Api
	int getMediaHeight();
}
