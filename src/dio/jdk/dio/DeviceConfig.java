// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
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


