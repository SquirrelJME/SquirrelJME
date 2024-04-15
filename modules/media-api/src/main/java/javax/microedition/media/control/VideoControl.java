// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.media.control;

import cc.squirreljme.runtime.cldc.annotation.Api;
import javax.microedition.media.MediaException;

@Api
public interface VideoControl
	extends GUIControl
{
	@Api
	int USE_DIRECT_VIDEO =
		1;
	
	@Api
	int getDisplayHeight();
	
	@Api
	int getDisplayWidth();
	
	@Api
	int getDisplayX();
	
	@Api
	int getDisplayY();
	
	@Api
	byte[] getSnapshot(String __a)
		throws MediaException;
	
	@Api
	int getSourceHeight();
	
	@Api
	int getSourceWidth();
	
	@Api
	void setDisplayFullScreen(boolean __a)
		throws MediaException;
	
	@Api
	void setDisplayLocation(int __a, int __b);
	
	@Api
	void setDisplaySize(int __a, int __b)
		throws MediaException;
	
	@Api
	void setVisible(boolean __a);
}


