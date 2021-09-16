// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.lcdui;

public interface TextEditorChangeListener
{
	int ACTION_CARET_MOVE =
		4;
	
	int ACTION_CONTENT_CHANGE =
		1;
	
	int ACTION_DIRECTION_CHANGE =
		64;
	
	int ACTION_INPUT_MODE_CHANGE =
		128;
	
	int ACTION_LANGUAGE_CHANGE =
		256;
	
	int ACTION_TRAVERSE_NEXT =
		16;
	
	int ACTION_TRAVERSE_PREVIOUS =
		8;
	
	void inputAction(TextEditor __e, int __act);
}

