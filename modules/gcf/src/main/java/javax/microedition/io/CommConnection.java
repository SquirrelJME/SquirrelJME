// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.io;


import cc.squirreljme.runtime.cldc.annotation.Api;

public interface CommConnection
	extends StreamConnection
{
	@Api
	int getBaudRate();
	
	@Api
	int setBaudRate(int __a);
}


