// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.jblend.util;

import com.jblend.io.SimpleObjectInputStream;
import com.jblend.io.SimpleObjectOutputStream;
import com.jblend.io.SimpleSerializable;
import java.io.IOException;

public class Vector
	extends java.util.Vector
	implements SimpleSerializable
{
    public Vector() {
        super(0, 0);
        
        throw new todo.TODO();
    }

    public Vector(int var1) {
        super(0, 0);
        
        throw new todo.TODO();
    }

    public void writeObject(SimpleObjectOutputStream var1) throws IOException
	{
		throw new todo.TODO();
	}

    public void readObject(SimpleObjectInputStream var1) throws IOException
	{
		throw new todo.TODO();
	}
}

