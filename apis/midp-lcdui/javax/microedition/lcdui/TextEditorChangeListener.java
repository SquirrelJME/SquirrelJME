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

public interface TextEditorChangeListener
{
	public static final int ACTION_CARET_MOVE =
		4;
	
	public static final int ACTION_CONTENT_CHANGE =
		1;
	
	public static final int ACTION_DIRECTION_CHANGE =
		64;
	
	public static final int ACTION_INPUT_MODE_CHANGE =
		128;
	
	public static final int ACTION_LANGUAGE_CHANGE =
		256;
	
	public static final int ACTION_TRAVERSE_NEXT =
		16;
	
	public static final int ACTION_TRAVERSE_PREVIOUS =
		8;
	
	public abstract void inputAction(TextEditor __e, int __act);
}

