// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.io;

import cc.squirreljme.runtime.cldc.annotation.Api;
import java.io.IOException;

@Api
public interface ModemConnection
	extends CommConnection
{
	@Api
	int DCD_SIGNAL =
		2;
	
	@Api
	int DSR_SIGNAL =
		4;
	
	@Api
	int DTR_SIGNAL =
		1;
	
	@Api
	int INPUT_MODE =
		0;
	
	@Api
	int OUTPUT_MODE =
		1;
	
	@Api
	int RI_SIGNAL =
		8;
	
	@Api
	int UNSUPPORTED =
		-1;
	
	@Api
	void addListener(ModemListener __a, int __b);
	
	@Api
	int getLineMode(int __a);
	
	@Api
	boolean getLineState(int __a)
		throws IOException;
	
	@Api
	void removeListener(ModemListener __a);
	
	@Api
	void setLineState(int __a, boolean __b)
		throws IOException;
}


