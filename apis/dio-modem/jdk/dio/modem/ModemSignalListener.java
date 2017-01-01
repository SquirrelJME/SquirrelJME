// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package jdk.dio.modem;

import jdk.dio.Device;
import jdk.dio.DeviceEventListener;

public interface ModemSignalListener<P extends Device<? super P>>
	extends DeviceEventListener
{
	public abstract void signalStateChanged(ModemSignalEvent<P> __a);
}


