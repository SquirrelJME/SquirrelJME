// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.jblend.io;

import cc.squirreljme.runtime.cldc.annotation.Api;
import java.io.IOException;

public interface SimpleSerializable
{
	@Api
	void writeObject(SimpleObjectOutputStream var1)
		throws IOException;
	
	@Api
	void readObject(SimpleObjectInputStream var1)
		throws IOException;
}

