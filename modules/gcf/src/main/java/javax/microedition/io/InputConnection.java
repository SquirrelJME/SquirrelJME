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
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

@Api
public interface InputConnection
	extends Connection
{
	@Api
	DataInputStream openDataInputStream()
		throws IOException;
	
	@Api
	InputStream openInputStream()
		throws IOException;
}


