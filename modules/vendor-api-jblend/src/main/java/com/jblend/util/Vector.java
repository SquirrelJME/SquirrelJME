// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.jblend.util;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import com.jblend.io.SimpleObjectInputStream;
import com.jblend.io.SimpleObjectOutputStream;
import com.jblend.io.SimpleSerializable;
import java.io.IOException;

@Api
public class Vector
	extends java.util.Vector
	implements SimpleSerializable
{
	@Api
    public Vector() {
        super(0, 0);
        
        throw Debugging.todo();
    }

	@Api
    public Vector(int var1) {
        super(0, 0);
        
        throw Debugging.todo();
    }

    @Override
	public void writeObject(SimpleObjectOutputStream var1)
		throws IOException
	{
		throw Debugging.todo();
	}

    @Override
	public void readObject(SimpleObjectInputStream var1)
		throws IOException
	{
		throw Debugging.todo();
	}
}

