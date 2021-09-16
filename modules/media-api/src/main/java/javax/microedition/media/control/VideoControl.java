// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.media.control;

import javax.microedition.media.MediaException;

public interface VideoControl
	extends GUIControl
{
	int USE_DIRECT_VIDEO =
		1;
	
	int getDisplayHeight();
	
	int getDisplayWidth();
	
	int getDisplayX();
	
	int getDisplayY();
	
	byte[] getSnapshot(String __a)
		throws MediaException;
	
	int getSourceHeight();
	
	int getSourceWidth();
	
	void setDisplayFullScreen(boolean __a)
		throws MediaException;
	
	void setDisplayLocation(int __a, int __b);
	
	void setDisplaySize(int __a, int __b)
		throws MediaException;
	
	void setVisible(boolean __a);
}


