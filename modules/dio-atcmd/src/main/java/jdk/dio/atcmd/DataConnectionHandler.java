// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package jdk.dio.atcmd;


import cc.squirreljme.runtime.cldc.annotation.Api;

@Api
public interface DataConnectionHandler
{
	@Api
	void handleClosedDataConnection(ATDevice __a, DataConnection __b);
	
	@Api
	void handleOpenedDataConnection(ATDevice __a, DataConnection __b);
}


