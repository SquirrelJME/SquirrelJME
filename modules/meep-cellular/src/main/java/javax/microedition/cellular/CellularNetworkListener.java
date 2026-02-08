// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.cellular;

import cc.squirreljme.runtime.cldc.annotation.Api;

@Api
public interface CellularNetworkListener
{
	@Api
	int	NETWORK_CELL_CHANGED = 3;
	
	@Api
	int	NETWORK_CONNECTED = 0;
	
	@Api
	int	NETWORK_DISCONNECTED = 1;
	
	@Api
	int	NETWORK_PROTOCOL_CHANGED = 2;
	
	@Api
	void notifyNetworkEvent(CellularNetwork __net, int __event);
}
