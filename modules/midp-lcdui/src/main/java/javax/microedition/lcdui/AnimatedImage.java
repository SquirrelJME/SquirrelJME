// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.lcdui;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.debug.Debugging;

public class AnimatedImage
	extends Image
{
	@Api
	public Image getFrame(int __i)
		throws IndexOutOfBoundsException
		
	{
		throw Debugging.todo();
	}
	
	@Api
	public int getFrameDelay(int __i)
		throws IndexOutOfBoundsException
	{
		throw Debugging.todo();
	}
	
	@Api
	public int getLoopCount()
	{
		throw Debugging.todo();
	}
}

