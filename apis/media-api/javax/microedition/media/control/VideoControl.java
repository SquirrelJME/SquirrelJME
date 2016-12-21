// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package javax.microedition.media.control;

import javax.microedition.media.MediaException;

public interface VideoControl
	extends GUIControl
{
	public static final int USE_DIRECT_VIDEO =
		1;
	
	public abstract int getDisplayHeight();
	
	public abstract int getDisplayWidth();
	
	public abstract int getDisplayX();
	
	public abstract int getDisplayY();
	
	public abstract byte[] getSnapshot(String __a)
		throws MediaException;
	
	public abstract int getSourceHeight();
	
	public abstract int getSourceWidth();
	
	public abstract Object initDisplayMode(int __a, Object __b);
	
	public abstract void setDisplayFullScreen(boolean __a)
		throws MediaException;
	
	public abstract void setDisplayLocation(int __a, int __b);
	
	public abstract void setDisplaySize(int __a, int __b)
		throws MediaException;
	
	public abstract void setVisible(boolean __a);
}


