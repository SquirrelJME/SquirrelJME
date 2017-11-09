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

import java.io.IOException;
import java.io.OutputStream;

public interface DeviceConfig<P extends Device<? super P>>
{
	@Deprecated
	public static final int DEFAULT =
		-1;
	
	public static final int UNASSIGNED =
		-1;
	
	public abstract int serialize(OutputStream __a)
		throws IOException;
	
	public static interface HardwareAddressing
	{
		public abstract String getControllerName();
		
		public abstract int getControllerNumber();
	}
}


