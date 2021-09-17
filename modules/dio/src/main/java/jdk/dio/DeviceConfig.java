// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
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
	int DEFAULT =
		-1;
	
	int UNASSIGNED =
		-1;
	
	int serialize(OutputStream __a)
		throws IOException;
	
	interface HardwareAddressing
	{
		String getControllerName();
		
		int getControllerNumber();
	}
}


