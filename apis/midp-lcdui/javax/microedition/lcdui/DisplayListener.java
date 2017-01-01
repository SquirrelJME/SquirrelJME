// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.lcdui;

public interface DisplayListener
{
	public abstract void displayAdded(Display __d);
	
	public abstract void displayStateChanged(Display __d, int __ns);
	
	public abstract void hardwareStateChanged(Display __d, int __ns);
	
	public abstract void orientationChanged(Display __d, int __no);
	
	public abstract void sizeChanged(Display __d, int __w, int __h);
}

