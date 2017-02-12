// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.build.host.javase;

import java.awt.event.KeyEvent;
import java.util.Set;
import java.util.TreeSet;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.KeyListener;
import net.multiphasicapps.squirreljme.lcdui.KeyEventCapableConnector;
import net.multiphasicapps.squirreljme.lcdui.KeyEventType;
import net.multiphasicapps.squirreljme.lcdui.NonStandardKey;

/**
 * This is an engine which is used to convert AWT's rather nasty key event
 * handling across platforms into one which is more sane and matches LCDUI.
 *
 * @since 2017/02/12
 */
public class KeyPressEngine
	implements java.awt.event.KeyListener
{
	/** The connector to send events to. */
	protected final KeyEventCapableConnector connector;
	
	/** Keys which are pressed down. */
	private final Set<Integer> _pressed =
		new TreeSet<>();
	
	/**
	 * Initializes the engine.
	 *
	 * @param __c The connector to send events to.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/02/12
	 */
	public KeyPressEngine(KeyEventCapableConnector __c)
		throws NullPointerException
	{
		// Check
		if (__c == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.connector = __c;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/12
	 */
	@Override
	public void keyPressed(KeyEvent __e)
	{
		// Check
		if (__e == null)
			return;
		
		Set<Integer> pressed = this._pressed;
		int co = __e.getKeyCode();
		
		// Need to determine if the key has already been pressed (for repeat)
		Integer ico = co;
		boolean ispressed = pressed.contains(ico);
		if (!ispressed)
			pressed.add(ico);
		
		// Generate event
		this.connector.keyEvent((ispressed ? KeyEventType.REPEATED :
			KeyEventType.PRESSED), __getKeyCode(__e), __getKeyChar(__e),
			__getModifiers(__e));
	}

	/**
	 * {@inheritDoc}
	 * @since 2017/02/12
	 */
	@Override
	public void keyReleased(KeyEvent __e)
	{
		// Check
		if (__e == null)
			return;
		
		Set<Integer> pressed = this._pressed;
		int co = __e.getKeyCode();
		
		// Release the key
		Integer ico = co;
		boolean ispressed = pressed.contains(ico);
		if (ispressed)
		{
			// Remove pressed state
			pressed.remove(ico);
			
			// Generate event
			this.connector.keyEvent(KeyEventType.RELEASED,
				__getKeyCode(__e), __getKeyChar(__e), __getModifiers(__e));
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/12
	 */
	@Override
	public void keyTyped(KeyEvent __e)
	{
		// These can be ignored because the unicode key can be determined
		// from the event code
	}
	
	/**
	 * Maps Unicode characters to keys.
	 *
	 * @param __e The event.
	 * @return The unicode character.
	 * @since 2017/02/12
	 */
	private char __getKeyChar(KeyEvent __e)
	{
		char rv = __e.getKeyChar();
		if (rv == 0 || rv == KeyEvent.CHAR_UNDEFINED)
			return 0;
		return rv;
	}
	
	/**
	 * Maps AWT keys to LCDUI keys.
	 *
	 * @param __e The event.
	 * @return The key code used.
	 * @since 2017/02/12
	 */
	private int __getKeyCode(KeyEvent __e)
	{
		// Depends on the AWT key
		int code;
		switch ((code = __e.getKeyCode()))
		{
				// Directional keys
			case KeyEvent.VK_UP:	return Canvas.KEY_UP;
			case KeyEvent.VK_DOWN:	return Canvas.KEY_DOWN;
			case KeyEvent.VK_LEFT:	return Canvas.KEY_LEFT;
			case KeyEvent.VK_RIGHT:	return Canvas.KEY_RIGHT;
			
				// Non-standard keys
			case KeyEvent.VK_F1:    return NonStandardKey.F1;
			case KeyEvent.VK_F2:    return NonStandardKey.F2;
			case KeyEvent.VK_F3:    return NonStandardKey.F3;
			case KeyEvent.VK_F4:    return NonStandardKey.F4;
			case KeyEvent.VK_F5:    return NonStandardKey.F5;
			case KeyEvent.VK_F6:    return NonStandardKey.F6;
			case KeyEvent.VK_F7:    return NonStandardKey.F7;
			case KeyEvent.VK_F8:    return NonStandardKey.F8;
			case KeyEvent.VK_F9:    return NonStandardKey.F9;
			case KeyEvent.VK_F10:   return NonStandardKey.F10;
			case KeyEvent.VK_F11:   return NonStandardKey.F11;
			case KeyEvent.VK_F12:   return NonStandardKey.F12;
			case KeyEvent.VK_F13:   return NonStandardKey.F13;
			case KeyEvent.VK_F14:   return NonStandardKey.F14;
			case KeyEvent.VK_F15:   return NonStandardKey.F15;
			case KeyEvent.VK_F16:   return NonStandardKey.F16;
			case KeyEvent.VK_F17:   return NonStandardKey.F17;
			case KeyEvent.VK_F18:   return NonStandardKey.F18;
			case KeyEvent.VK_F19:   return NonStandardKey.F19;
			case KeyEvent.VK_F20:   return NonStandardKey.F20;
			case KeyEvent.VK_F21:   return NonStandardKey.F21;
			case KeyEvent.VK_F22:   return NonStandardKey.F22;
			case KeyEvent.VK_F23:   return NonStandardKey.F23;
			case KeyEvent.VK_F24:   return NonStandardKey.F24;
			
				// Other keys
			case KeyEvent.VK_WINDOWS:	return NonStandardKey.LOGO;
			
				// Unknown, just use the given code, but only in Unicode range
			default:
				if (code > 0 && code <= 65535)
					return code;
				
				// Otherwise emit a non-standard unknown key
				return NonStandardKey.UNKNOWN;
		}
	}
	
	/**
	 * Maps AWT modifiers to LCDUI modifiers.
	 *
	 * @param __e The event.
	 * @return The modifiers used.
	 * @since 2017/02/12
	 */
	private int __getModifiers(KeyEvent __e)
	{
		int raw = __e.getModifiersEx();
		int rv = 0;
		
		// Alt?
		if ((raw & KeyEvent.ALT_DOWN_MASK) != 0)
			rv |= KeyListener.MODIFIER_ALT;
			
		// Ctrl?
		if ((raw & KeyEvent.CTRL_DOWN_MASK) != 0)
			rv |= KeyListener.MODIFIER_CTRL;
			
		// Shift?
		if ((raw & KeyEvent.SHIFT_DOWN_MASK) != 0)
			rv |= KeyListener.MODIFIER_SHIFT;
			
		// Meta?
		// Also consider the logo key being down as the meta key
		if ((raw & KeyEvent.META_DOWN_MASK) != 0 ||
			this._pressed.contains(KeyEvent.VK_WINDOWS))
			rv |= KeyListener.MODIFIER_COMMAND;
		
		// AltGr?
		if ((raw & KeyEvent.ALT_GRAPH_DOWN_MASK) != 0)
			rv |= KeyListener.MODIFIER_CHR;
		
		return rv;
	}
}

