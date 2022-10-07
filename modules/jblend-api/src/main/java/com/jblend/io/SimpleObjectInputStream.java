// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.jblend.io;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public class SimpleObjectInputStream
	extends DataInputStream
{
    public SimpleObjectInputStream(InputStream var1) {
        super((InputStream)null);
        
        throw Debugging.todo();
    }

    public String readString() throws IOException
	{
		throw Debugging.todo();
	}

    public SimpleSerializable read(SimpleSerializable var1) throws IOException
	{
		throw Debugging.todo();
	}

    public Object dispatchReadCommand(Class var1) throws IOException
	{
		throw Debugging.todo();
	}
}

