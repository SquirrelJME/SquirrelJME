// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package jdk.dio;


import cc.squirreljme.runtime.cldc.annotation.Api;

@Api
public interface DeviceDescriptor<P extends Device<? super P>>
{
	@Api
	int UNDEFINED_ID =
		-1;
	
	@Api
	<C extends DeviceConfig<? super P>> C getConfiguration();
	
	@Api
	int getID();
	
	@Api
	Class<P> getInterface();
	
	@Api
	String getName();
	
	@Api
	String[] getProperties();
}


