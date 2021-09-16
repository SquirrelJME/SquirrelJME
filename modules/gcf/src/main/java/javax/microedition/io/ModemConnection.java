// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.io;

import java.io.IOException;

public interface ModemConnection
	extends CommConnection
{
	int DCD_SIGNAL =
		2;
	
	int DSR_SIGNAL =
		4;
	
	int DTR_SIGNAL =
		1;
	
	int INPUT_MODE =
		0;
	
	int OUTPUT_MODE =
		1;
	
	int RI_SIGNAL =
		8;
	
	int UNSUPPORTED =
		-1;
	
	void addListener(ModemListener __a, int __b);
	
	int getLineMode(int __a);
	
	boolean getLineState(int __a)
		throws IOException;
	
	void removeListener(ModemListener __a);
	
	void setLineState(int __a, boolean __b)
		throws IOException;
}


