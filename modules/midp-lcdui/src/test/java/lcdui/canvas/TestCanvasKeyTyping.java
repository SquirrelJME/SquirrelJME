// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package lcdui.canvas;

import cc.squirreljme.jvm.mle.UIFormShelf;
import cc.squirreljme.jvm.mle.brackets.UIFormBracket;
import cc.squirreljme.jvm.mle.brackets.UIItemBracket;
import cc.squirreljme.jvm.mle.callbacks.UIFormCallback;
import cc.squirreljme.jvm.mle.constants.NonStandardKey;
import cc.squirreljme.jvm.mle.constants.UIKeyEventType;
import cc.squirreljme.runtime.lcdui.mle.StaticDisplayState;
import cc.squirreljme.runtime.lcdui.mle.UIBackend;
import cc.squirreljme.runtime.lcdui.mle.UIBackendFactory;
import javax.microedition.lcdui.Display;

/**
 * Tests that canvas typing works.
 *
 * @since 2020/10/16
 */
public class TestCanvasKeyTyping
	extends BaseCanvas
{
	/**
	 * {@inheritDoc}
	 * @since 2020/10/16
	 */
	@Override
	public void test(Display __display, CanvasPlatform __platform)
		throws Throwable
	{
		UIBackend backend = UIBackendFactory.getInstance(true);
		UIFormCallback injector = backend.injector();
		
		// We need to locate the canvas item before we can send events to it
		UIFormBracket form = (UIFormBracket)StaticDisplayState
			.locate(__platform);
		UIItemBracket canvas = UIFormShelf.formItemAtPosition(form, 0);
		
		// Send in keys
		this.__sendKey(injector, form, canvas, 'X');
		this.__sendKey(injector, form, canvas, 'e');
		this.__sendKey(injector, form, canvas, 'r');
		this.__sendKey(injector, form, canvas, NonStandardKey.SHIFT);
		
		// Check the string result
		this.secondary("sequence", __platform.queryKeys());
	}
	
	/**
	 * Sends the specified key.
	 * 
	 * @param __injector The injector.
	 * @param __form The form.
	 * @param __item The item.
	 * @param __c The character to inject.
	 * @since 2020/10/16
	 */
	private void __sendKey(UIFormCallback __injector, UIFormBracket __form,
		UIItemBracket __item, int __c)
	{
		__injector.eventKey(__item, UIKeyEventType.KEY_PRESSED,
			__c, 0);
		__injector.eventKey(__item, UIKeyEventType.KEY_REPEATED,
			__c, 0);
		__injector.eventKey(__item, UIKeyEventType.KEY_RELEASE,
			__c, 0);
	}
}
