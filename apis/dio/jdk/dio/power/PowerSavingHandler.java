// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package jdk.dio.power;

import jdk.dio.Device;

public interface PowerSavingHandler
{
	public abstract <P extends Device<? super P>> void handlePowerStateChange
		(P __a, PowerManaged.Group __b, int __c, int __d, long __e);
	
	public abstract <P extends Device<? super P>> long 
		handlePowerStateChangeRequest(P __a, PowerManaged.Group __b, int __c,
		int __d, long __e);
}


