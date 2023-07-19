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

/**
 * This is the key listener interface which is used for a slightly more
 * advanced means of listening for and responding to key events.
 *
 * Unlike {@link Canvas} and {@link CustomItem}, the key code that is passed
 * to these methods will be modified by the key modifiers (this means that
 * if shift is held down then letters will be uppercase).
 *
 * @since 2017/02/12
 */
@Api
public interface KeyListener
{
	/** Alt key modifier. */
	@Api
	int MODIFIER_ALT =
		65536;
		
	/** Function (Fn/Chr) key modifier. */
	@Api
	int MODIFIER_CHR =
		8388608;
	
	/** Command key modifier. */
	@Api
	int MODIFIER_COMMAND =
		4194304;
	
	/** Ctrl key modifier. */
	@Api
	int MODIFIER_CTRL =
		262144;
	
	/** Mask for all the modifier keys. */
	@Api
	int MODIFIER_MASK =
		13041664;
	
	/** Shift key modifier. */
	@Api
	int MODIFIER_SHIFT =
		131072;
	
	/**
	 * Called when a key is pressed.
	 *
	 * @param __kc The key code.
	 * @param __km The modifiers to the key.
	 * @since 2017/02/12
	 */
	@Api
	@SerializedEvent
	@Async.Execute
	void keyPressed(int __kc, int __km);
	
	/**
	 * Called when a key is released.
	 *
	 * @param __kc The key code.
	 * @param __km The modifiers to the key.
	 * @since 2017/02/12
	 */
	@Api
	@SerializedEvent
	@Async.Execute
	void keyReleased(int __kc, int __km);
	
	/**
	 * Called when a key is repeated.
	 *
	 * @param __kc The key code.
	 * @param __km The modifiers to the key.
	 * @since 2017/02/12
	 */
	@Api
	@SerializedEvent
	@Async.Execute
	void keyRepeated(int __kc, int __km);
}

