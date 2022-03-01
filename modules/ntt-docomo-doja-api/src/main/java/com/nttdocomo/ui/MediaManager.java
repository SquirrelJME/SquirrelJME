// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.nttdocomo.ui;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.IOException;
import java.io.InputStream;
import javax.microedition.io.Connector;

public class MediaManager
{
	@SuppressWarnings("FinalStaticMethod")
	public static final MediaData getData(String __uri)
	{
		throw Debugging.todo();
	}
	
	@SuppressWarnings("FinalStaticMethod")
	public static final MediaImage getImage(String __uri)
		throws NullPointerException
	{
		if (__uri == null)
			throw new NullPointerException("NARG");
		
		return new __MIDPImage__(__uri);
	}
	
	@SuppressWarnings("FinalStaticMethod")
	public static final MediaSound getSound(String __uri)
	{
		throw Debugging.todo();
	}
}
