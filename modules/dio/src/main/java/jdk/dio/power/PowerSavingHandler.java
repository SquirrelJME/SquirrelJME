// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package jdk.dio.power;

import jdk.dio.Device;

public interface PowerSavingHandler
{
	<P extends Device<? super P>> void handlePowerStateChange(P __a,
		PowerManaged.Group __b, int __c, int __d, long __e);
	
	<P extends Device<? super P>> long
		handlePowerStateChangeRequest(P __a, PowerManaged.Group __b, int __c,
		int __d, long __e);
}


