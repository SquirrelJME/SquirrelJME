// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.vodafone.v10.system.media;

import cc.squirreljme.runtime.cldc.annotation.Api;

public interface ResourceOperator
{
	@Api
	int getResourceType();
	
	@Api
	int getResourceCount();
	
	@Api
	int getResourceID(int var1);
	
	@Api
	String getResourceName(int var1);
	
	@Api
	String[] getResourceNames();
	
	@Api
	void setResourceByID(MediaPlayer var1, int var2);
	
	@Api
	void setResourceByTitle(MediaPlayer var1, String var2);
	
	@Api
	void setResource(MediaPlayer var1, int var2);
	
	@Api
	int getIndexOfResource(int var1);
}
