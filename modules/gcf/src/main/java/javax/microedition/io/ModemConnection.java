// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.io;

import java.io.IOException;

public interface ModemConnection
	extends CommConnection
{
	public static final int DCD_SIGNAL =
		2;
	
	public static final int DSR_SIGNAL =
		4;
	
	public static final int DTR_SIGNAL =
		1;
	
	public static final int INPUT_MODE =
		0;
	
	public static final int OUTPUT_MODE =
		1;
	
	public static final int RI_SIGNAL =
		8;
	
	public static final int UNSUPPORTED =
		-1;
	
	public abstract void addListener(ModemListener __a, int __b);
	
	public abstract int getLineMode(int __a);
	
	public abstract boolean getLineState(int __a)
		throws IOException;
	
	public abstract void removeListener(ModemListener __a);
	
	public abstract void setLineState(int __a, boolean __b)
		throws IOException;
}


