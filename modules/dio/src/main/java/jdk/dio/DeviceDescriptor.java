// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package jdk.dio;


public interface DeviceDescriptor<P extends Device<? super P>>
{
	int UNDEFINED_ID =
		-1;
	
	<C extends DeviceConfig<? super P>> C getConfiguration();
	
	int getID();
	
	Class<P> getInterface();
	
	String getName();
	
	String[] getProperties();
}


