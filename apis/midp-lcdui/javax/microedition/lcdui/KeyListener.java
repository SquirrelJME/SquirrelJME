// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package javax.microedition.lcdui;

public interface KeyListener
{
	public static final int MODIFIER_ALT =
		65536;

	public static final int MODIFIER_CHR =
		8388608;

	public static final int MODIFIER_COMMAND =
		4194304;

	public static final int MODIFIER_CTRL =
		262144;

	public static final int MODIFIER_MASK =
		13041664;

	public static final int MODIFIER_SHIFT =
		131072;
	
	public abstract void keyPressed(int __kc, int __km);
	
	public abstract void keyReleased(int __kc, int __km);
	
	public abstract void keyRepeated(int __kc, int __km);
}

