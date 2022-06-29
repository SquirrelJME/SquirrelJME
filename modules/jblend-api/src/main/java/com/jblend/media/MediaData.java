// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.jblend.media;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.IOException;

public abstract class MediaData
{
    public MediaData()
	{
		throw Debugging.todo();
	}

    public MediaData(String var1)
    	throws IOException
	{
		throw Debugging.todo();
	}

    public MediaData(byte[] var1)
	{
		throw Debugging.todo();
	}

    public abstract String getMediaType();

    public abstract void setData(byte[] var1);
}
