// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.lcdui;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.lcdui.SerializedEvent;
import org.jetbrains.annotations.Async;

@Api
public interface TextEditorChangeListener
{
	@Api
	int ACTION_CARET_MOVE =
		4;
	
	@Api
	int ACTION_CONTENT_CHANGE =
		1;
	
	@Api
	int ACTION_DIRECTION_CHANGE =
		64;
	
	@Api
	int ACTION_INPUT_MODE_CHANGE =
		128;
	
	@Api
	int ACTION_LANGUAGE_CHANGE =
		256;
	
	@Api
	int ACTION_TRAVERSE_NEXT =
		16;
	
	@Api
	int ACTION_TRAVERSE_PREVIOUS =
		8;
	
	@Api
	@SerializedEvent
	@Async.Execute
	void inputAction(TextEditor __e, int __act);
}

