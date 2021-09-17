// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.io;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

@SuppressWarnings("InterfaceWithOnlyOneDirectInheritor")
public interface OutputConnection
	extends Connection
{
	DataOutputStream openDataOutputStream()
		throws IOException;
	
	OutputStream openOutputStream()
		throws IOException;
}


