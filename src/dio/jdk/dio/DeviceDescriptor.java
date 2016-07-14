// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package jdk.dio;


public interface DeviceDescriptor<P extends Device<? super P>>
{
	public static final int UNDEFINED_ID =
		-1;
	
	public abstract <C extends DeviceConfig<? super P>> C getConfiguration();
	
	public abstract int getID();
	
	public abstract Class<P> getInterface();
	
	public abstract String getName();
	
	public abstract String[] getProperties();
}


