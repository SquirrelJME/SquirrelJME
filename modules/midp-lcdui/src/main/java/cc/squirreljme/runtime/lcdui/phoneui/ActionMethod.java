// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.phoneui;

import cc.squirreljme.jvm.mle.constants.NonStandardKey;
import cc.squirreljme.runtime.cldc.asm.NativeDisplayEventCallback;
import cc.squirreljme.runtime.lcdui.event.EventTranslate;
import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.FileSelector;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.List;
import javax.microedition.lcdui.TabbedPane;
import javax.microedition.lcdui.TextBox;

/**
 * This represents an action method which when a key is pressed or similar
 * then something is done.
 *
 * @since 2019/05/18
 */
@Deprecated
public enum ActionMethod
{
	/** Alert. */
	@Deprecated
	ALERT
	{
	},
	
	/** Canvas. */
	@Deprecated
	CANVAS
	{
		/**
		 * {@inheritDoc}
		 * @since 2019/05/18
		 */
		@Override
		public final boolean keyEvent(Displayable __d, State __s, int __ty,
			int __kc, int __ch, int __time)
		{
			// Normalize all key types to mobile cell phone format, if not
			// specified
			if (__ch == '#')
				__kc = Canvas.KEY_POUND;
			else if (__ch == '*')
				__kc = Canvas.KEY_STAR;
			else if (__ch >= '0' && __ch <= '9')
				__kc = Canvas.KEY_NUM0 + (__ch - '0');
				
			// If the key-code is not valid then ignore
			if (__kc == NonStandardKey.UNKNOWN)
				return false;
			
			// Depends on the action
			switch (__ty)
			{
				case NativeDisplayEventCallback.KEY_PRESSED:
					__d.keyPressed(__kc);
					break;
				
				case NativeDisplayEventCallback.KEY_REPEATED:
					__d.keyRepeated(__kc);
					break;
				
				case NativeDisplayEventCallback.KEY_RELEASED:
					__d.keyReleased(__kc);
					break;
				
				default:
					break;
			}
			
			// Do not force a repaint, up to the canvas
			return false;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2019/05/18
		 */
		@Override
		public final boolean pointerEvent(Displayable __d, State __s, int __ty,
			int __x, int __y, int __time)
		{
			ExposedDisplayable displayable = (ExposedDisplayable)__d;
			
			// Depends on the type
			switch (__ty)
			{
				case NativeDisplayEventCallback.POINTER_PRESSED:
					displayable.pointerPressed(__x, __y);
					break;
				
				case NativeDisplayEventCallback.POINTER_RELEASED:
					displayable.pointerReleased(__x, __y);
					break;
				
				case NativeDisplayEventCallback.POINTER_DRAGGED:
					displayable.pointerDragged(__x, __y);
					break;
				
				default:
					break;
			}
			
			// Do not force a repaint, up to the canvas
			return false;
		}
	},
	
	/** File selector. */
	@Deprecated
	FILE_SELECTOR
	{
	},
	
	/** Form. */
	@Deprecated
	FORM
	{
		/**
		 * {@inheritDoc}
		 * @since 2019/12/09
		 */
		@Override
		public final boolean keyEvent(Displayable __d, State __s, int __ty,
			int __kc, int __ch, int __time)
		{
			return false;
		}
	},
	
	/** List. */
	@Deprecated
	LIST
	{
		/**
		 * {@inheritDoc}
		 * @since 2019/05/18
		 */
		@Override
		public final boolean keyEvent(Displayable __d, State __s, int __ty,
			int __kc, int __ch, int __time)
		{
			List list = (List)__d;
			int n = list.size();
			int ltype = list.getType();
			
			// Ignore released keys
			if (__ty == NativeDisplayEventCallback.KEY_RELEASED)
				return false;
			
			// Quickly focus an item?
			int focusdx = __s.focusdx,
				wantdx = focusdx;
			if (__kc >= Canvas.KEY_NUM1 && __kc <= Canvas.KEY_NUM9)
				wantdx = __kc - Canvas.KEY_NUM1;
			
			// Handle key
			switch (EventTranslate.keyCodeToGameAction(__kc))
			{
					// Move up, ignore number input
				case Canvas.UP:
					wantdx = focusdx - 1;
					break;
				
					// Move down, ignore number input
				case Canvas.DOWN:
					wantdx = focusdx + 1;
					break;
					
					// Select item
				case Canvas.FIRE:
					// Only allow enabled items to have their selection state
					// adjusted
					if (focusdx >= 0 && focusdx < n && list.isEnabled(focusdx))
					{
						// Set new selection state, exclusive lists always
						// set a new element
						boolean nowsel = (!list.isSelected(focusdx) ||
							ltype == Choice.EXCLUSIVE ||
							ltype == Choice.IMPLICIT);
						
						// Set new state
						list.setSelectedIndex(focusdx, nowsel);
						
						// Implicit lists need their item to be specified
						if (ltype == Choice.IMPLICIT)
						{
							// Execute command when selection was changed?
							Command sc = list.getSelectCommand();
							if (sc != null)
							{
								CommandListener l = ((ExposedDisplayable)__d).
									getCommandListener();
								if (l != null)
									l.commandAction(sc, __d);
							}
						}
						
						// Should be repainted
						return true;
					}
					
					// Not to be repainted
					return false;
			}
			
			// Change of focus and it is valid?
			if (focusdx != wantdx && wantdx >= 0 && wantdx < n)
			{
				// Set new focus
				__s.focusdx = wantdx;
				
				// If this is an implicit list then automatically select
				// the given focused item
				if (list.getType() == Choice.IMPLICIT)
					list.setSelectedIndex(wantdx, true);
				
				// Repaint
				return true;
			}
			
			// Nothing happened
			return false;
		}
	},
	
	/** Tabbed Pane. */
	@Deprecated
	TABBED_PANE
	{
	},
	
	/** Text Box. */
	@Deprecated
	TEXT_BOX
	{
	},
	
	/** End. */
	;
	
	
	/**
	 * Executes the numbered command on the given display.
	 *
	 * @param __d The displayable.
	 * @param __s The state.
	 * @param __c The command to execute.
	 * @return {@code true} if the command was handled.
	 * @since 2019/05/18
	 */
	@Deprecated
	public boolean command(Displayable __d, State __s, int __c)
	{
		return false;
	}
	
	/**
	 * Key action has been performed.
	 *
	 * @param __d The displayable.
	 * @param __s The state.
	 * @param __ty The type of key event.
	 * @param __kc The key code.
	 * @param __ch The key character, {@code -1} is not valid.
	 * @param __time Timecode.
	 * @return {@code true} if the display should be repainted.
	 * @since 2019/05/18
	 */
	@Deprecated
	public boolean keyEvent(Displayable __d, State __s, int __ty, int __kc,
		int __ch, int __time)
	{
		return false;
	}
	
	/**
	 * Pointer event has occured.
	 *
	 * @param __d The displayable.
	 * @param __s The state.
	 * @param __ty The type of pointer event.
	 * @param __x The X coordinate.
	 * @param __y The Y coordinate.
	 * @param __time Timecode.
	 * @return {@code true} if the display should be repainted.
	 * @since 2019/05/18
	 */
	@Deprecated
	public boolean pointerEvent(Displayable __d, State __s, int __ty, int __x,
		int __y, int __time)
	{
		return false;
	}
	
	/**
	 * Returns the action method for the given class.
	 *
	 * @param __cl The class to act for.
	 * @return The action method.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/05/18
	 */
	@Deprecated
	public static final ActionMethod of(Class<?> __cl)
		throws NullPointerException
	{
		if (Alert.class.isAssignableFrom(__cl))
			return ActionMethod.ALERT;
		else if (Canvas.class.isAssignableFrom(__cl))
			return ActionMethod.CANVAS;
		else if (FileSelector.class.isAssignableFrom(__cl))
			return ActionMethod.FILE_SELECTOR;
		else if (Form.class.isAssignableFrom(__cl))
			return ActionMethod.FORM;
		else if (List.class.isAssignableFrom(__cl))
			return ActionMethod.LIST;
		else if (TabbedPane.class.isAssignableFrom(__cl))
			return ActionMethod.TABBED_PANE;
		else if (TextBox.class.isAssignableFrom(__cl))
			return ActionMethod.TEXT_BOX;
		
		// {@squirreljme.error EB17 Could not get the action method of the
		// given class. (The class)}
		throw new IllegalArgumentException("EB17 " + __cl);
	}
}

