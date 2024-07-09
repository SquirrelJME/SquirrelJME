// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.lcdui;

import cc.squirreljme.jvm.mle.UIFormShelf;
import cc.squirreljme.jvm.mle.brackets.UIDisplayBracket;
import cc.squirreljme.jvm.mle.brackets.UIDrawableBracket;
import cc.squirreljme.jvm.mle.brackets.UIFormBracket;
import cc.squirreljme.jvm.mle.brackets.UIItemBracket;
import cc.squirreljme.jvm.mle.brackets.UIWidgetBracket;
import cc.squirreljme.jvm.mle.callbacks.UIDisplayCallback;
import cc.squirreljme.jvm.mle.callbacks.UIFormCallback;
import cc.squirreljme.jvm.mle.constants.NonStandardKey;
import cc.squirreljme.jvm.mle.constants.UIItemPosition;
import cc.squirreljme.jvm.mle.constants.UIKeyEventType;
import cc.squirreljme.jvm.mle.constants.UIKeyModifier;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.lcdui.SerializedEvent;
import cc.squirreljme.runtime.lcdui.event.EventTranslate;
import cc.squirreljme.runtime.lcdui.event.KeyCodeTranslator;
import cc.squirreljme.runtime.lcdui.mle.DisplayWidget;
import cc.squirreljme.runtime.lcdui.mle.PencilGraphics;
import cc.squirreljme.runtime.lcdui.mle.StaticDisplayState;
import cc.squirreljme.runtime.midlet.ApplicationHandler;
import cc.squirreljme.runtime.midlet.ApplicationInterface;
import org.jetbrains.annotations.Async;

/**
 * This thread is responsible for handling graphics operations.
 *
 * @since 2020/09/12
 */
final class __MLEUIThread__
	implements Runnable, UIDisplayCallback, UIFormCallback
{
	/** The time to sleep for between periodic checks. */
	private static final long _SLEEP_TIME =
		30_000;
	
	/** Are we within a paint? */
	private boolean _inPaint;
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/12
	 */
	@Override
	public void eventKey(UIDrawableBracket __drawable,
		int __event, int __keyCode, int __modifiers)
	{
		// Debug
		/*Debugging.debugNote("eventKey(%08x, %08x, %d, %d, %s)",
			Objects.toString(__drawable),
			__event, __keyCode, __modifiers);*/
		
		if (!(__drawable instanceof UIWidgetBracket))
			return;
		
		DisplayWidget widget = StaticDisplayState.locate(
			(UIItemBracket)__drawable);
		
		// Commands are special key events
		if (__event == UIKeyEventType.COMMAND_ACTIVATED)
		{
			// Command button widgets will activate the given command
			if (widget instanceof __CommandWidget__)
				((__CommandWidget__)widget).__activate();
			
			// List activations will activate the given list item
			else if (widget instanceof List)
				((List)widget).__selectCommand(__keyCode);
			
			return;
		}
		
		// Get J2ME specific modifiers since we have extra ones undefined by
		// J2ME
		int javaMods = __modifiers & UIKeyModifier.J2ME_MASK;
		
		// If both modifier keys are held down, treat it as the center key
		// This is kind of a shortcut way since SquirrelJME only knows about
		// the Left/Right and there are just not enough buttons available.
		// Note that the F keys count as special menu keys!
		// So this way F1/Soft1 + F2/Soft2 == F3/Soft3!
		if (((__modifiers & UIKeyModifier.MODIFIER_LEFT_RIGHT_COMMANDS) ==
			UIKeyModifier.MODIFIER_LEFT_RIGHT_COMMANDS) &&
			((__keyCode == NonStandardKey.F1 ||
				__keyCode == NonStandardKey.VGAME_COMMAND_LEFT) ||
			(__keyCode == NonStandardKey.F2 ||
				__keyCode == NonStandardKey.VGAME_COMMAND_RIGHT)))
			__keyCode = NonStandardKey.VGAME_COMMAND_CENTER;
		
		// Is this a special activation of command keys? 
		if ((__keyCode >= NonStandardKey.F1 &&
			__keyCode <= NonStandardKey.F24) ||
			__keyCode == NonStandardKey.VGAME_COMMAND_LEFT ||
			__keyCode == NonStandardKey.VGAME_COMMAND_CENTER ||
			__keyCode == NonStandardKey.VGAME_COMMAND_RIGHT)
		{
			// Do not multiply activate these just in case
			if (__event != UIKeyEventType.KEY_PRESSED)
				return;
			
			// Which position will be looked at?
			int pos = UIItemPosition.NOT_ON_FORM;
			switch (__keyCode)
			{
				case NonStandardKey.F1:
				case NonStandardKey.VGAME_COMMAND_LEFT:
					pos = UIItemPosition.LEFT_COMMAND;
					break;
				
				case NonStandardKey.F2:
				case NonStandardKey.VGAME_COMMAND_RIGHT:
					pos = UIItemPosition.RIGHT_COMMAND;
					break;
				
				default:
					break;
			}
			
			// Provided the position is valid, try to find an item to activate
			if (pos != UIItemPosition.NOT_ON_FORM &&
				(widget instanceof Displayable))
			{
				// Locate the item and try to execute the command if it is one
				UIItemBracket item = UIFormShelf.formItemAtPosition(
					((Displayable)widget).__state(
						Displayable.__DisplayableState__.class)._uiForm, pos);
				if (item != null)
				{
					// If this is mapped to a command then activate it
					DisplayWidget attempt = StaticDisplayState.locate(
						(UIItemBracket)__drawable);
					if (attempt instanceof __CommandWidget__)
						((__CommandWidget__)attempt).__activate();
					
					// Stop here
					return;
				}
			}
			
			// Some APIs such as Nokia expose certain keys and actions as
			// physical keys that can be pressed such that the left command
			// key will emit itself as a keycode.
			for (KeyCodeTranslator adapter : EventTranslate.translators())
			{
				int result = adapter.normalizeKeyCode(__keyCode);
				if (result != 0)
				{
					__keyCode = result;
					break;
				}
			}
		}
		
		// Open the LCDUI inspector?
		if (__event == UIKeyEventType.KEY_PRESSED &&
			(__keyCode == NonStandardKey.F12 ||
			__keyCode == NonStandardKey.VGAME_LCDUI_INSPECTOR))
		{
			if (true)
				throw Debugging.todo();
			
			// Consume this key
			return;
		}
		
		// Any Displayable which have standard key access
		if (widget instanceof Canvas)
			this.__eventKey((Canvas)widget, null,
				__event, __keyCode, javaMods);
		else if (widget instanceof CustomItem)
			this.__eventKey(null, (CustomItem)widget,
				__event, __keyCode, javaMods);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/12
	 */
	@Override
	public void eventMouse(UIDrawableBracket __drawable,
		int __event, int __button, int __x, int __y, int __modifiers)
	{
		// Debug
		/*Debugging.debugNote("eventMouse(%08x, %08x, %d, %d, %d, %d, %x)",
			System.identityHashCode(__form), System.identityHashCode(__item),
			__event, __button, __x, __y, __modifiers);*/
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/12
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void exitRequest(UIDrawableBracket __drawable)
	{
		// Debug
		Debugging.debugNote("exitRequest(%08x) @ %s",
			System.identityHashCode(__drawable), Thread.currentThread());
			
		// Obtain the application we are actually running, since this
		// could be done different for different ones
		Object currentInstance = ApplicationHandler.currentInstance();
		ApplicationInterface<Object> currentInterface =
			(ApplicationInterface<Object>)
				ApplicationHandler.currentInterface();
		if (currentInstance == null || currentInterface == null)
		{
			Debugging.debugNote("No current Application?");
			return;
		}
		
		// Destroy the application
		try
		{
			currentInterface.destroy(currentInstance, null);
		}
		catch (Throwable t)
		{
			/* {@squirreljme.error EB0k Failed to terminate application.} */
			throw new Error("EB0k", t);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/07/20
	 */
	@Override
	public void formRefresh(UIFormBracket __form, int __sx, int __sy,
	 int __sw,
		int __sh)
	{
		DisplayWidget widget = StaticDisplayState.locate(__form);
		if (widget instanceof Form)
			((Form)widget).__performLayout();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/10/03
	 */
	@Override
	public void later(UIDisplayBracket __display, int __serialId)
	{
		DisplayWidget widget = StaticDisplayState.locate(__display);
		if (!(widget instanceof Display))
			return;
		
		// Call the display callback
		((Display)widget).__serialRun(__serialId);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/12
	 */
	@Override
	public void paint(UIDrawableBracket __drawable, int __pf,
		int __bw, int __bh, Object __buf, int[] __pal, int __sx,
		int __sy, int __sw, int __sh, int __special)
	{
		// Debug
		/*Debugging.debugNote("paint(%08x, %08x, %d, " +
			"%d, %d, %s, %d, %s, " +
			"%d, %d, %d, %d)",
			System.identityHashCode(__form), System.identityHashCode(__item),
			__pf, __bw, __bh, __buf, __offset, __pal, __sx, __sy, __sw, __sh);
		*/
		
		// Does nothing as at this point, software implementations of the UI
		// would have handled the display callback here so no action is
		// needed to be performed.
		if (__drawable instanceof UIDisplayBracket)
			return;
		
		// Assume otherwise that drawing is done for widgets
		DisplayWidget widget =
			StaticDisplayState.locate((UIWidgetBracket)__drawable);
		if (widget instanceof __CommonWidget__)
		{
			// Ignore widgets which do not want the paint event
			__CommonWidget__ common = (__CommonWidget__)widget;
			if (!common.__isPainted())
				return;
				
			// Since painting is a heavy operation, only allow single paints to
			// happen at a time. This should generally never be called
			// concurrently ever.
			synchronized (this)
			{
				if (this._inPaint)
					return;
				this._inPaint = true;
			}
				
			// The paint flag always needs to be cleared when finished
			try
			{
				// Try to use hardware accelerated graphics where possible
				Graphics gfx = PencilGraphics.hardwareGraphics(__pf,
					__bw, __bh, __buf, __pal, __sx, __sy,
					__sw, __sh);
				
				// Forward the paint call
				common.__paint(gfx, __sw, __sh, __special);
			}
			finally
			{
				this._inPaint = false;
			}
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/12
	 */
	@Override
	public void propertyChange(UIFormBracket __form, UIItemBracket __item,
		int __intProp, int __sub, int __old, int __new)
	{
		// Debug
		/*Debugging.debugNote("propertyChange(%08x, %08x, %d, %d, %d)",
			System.identityHashCode(__form), System.identityHashCode(__item),
			__intProp, __old, __new);*/
			
		// Forward to handler
		DisplayWidget widget = StaticDisplayState.locate(__form);
		if (widget instanceof __CommonWidget__)
			((__CommonWidget__)widget).__propertyChange(__form, __item,
				__intProp, __sub, __old, __new);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/12
	 */
	@Override
	public void propertyChange(UIFormBracket __form, UIItemBracket __item,
		int __strProp, int __sub, String __old, String __new)
	{
		// Debug
		/*Debugging.debugNote("propertyChange(%08x, %08x, %d, %s, %s)",
			System.identityHashCode(__form), System.identityHashCode(__item),
			__strProp, __old, __new);*/
		
		// Forward to handler
		DisplayWidget widget = StaticDisplayState.locate(__form);
		if (widget instanceof __CommonWidget__)
			((__CommonWidget__)widget).__propertyChange(__form, __item,
				__strProp, __sub, __old, __new);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/12
	 */
	@Override
	public void run()
	{
		// Infinite UI loop, which performs periodic probes and background
		// actions
		for (;;)
			synchronized (StaticDisplayState.class)
			{
				// Stop if terminating
				if (StaticDisplayState.isTerminating())
					break;
				
				// Wait for the signal on the next run
				try
				{
					StaticDisplayState.class.wait(__MLEUIThread__._SLEEP_TIME);
				}
				catch (InterruptedException ignored)
				{
				}
			}
		
		// Debug
		Debugging.debugNote("UI loop terminated.");
		
		// Destroy everything possible
		StaticDisplayState.destroy();
	}
	
	/**
	 * Performs the actual handling of key events with the common canvas and
	 * custom items.
	 * 
	 * @param __canvas The Canvas used
	 * @param __cItem The custom item.
	 * @param __event The {@link UIKeyEventType}.
	 * @param __keyCode The key code, may be a character or special key.
	 * @param __modifiers The modifiers for the key.
	 * @throws IllegalArgumentException If both arguments are null or not
	 * null.
	 * @since 2020/10/16
	 */
	@SerializedEvent
	@Async.Execute
	private void __eventKey(Canvas __canvas, CustomItem __cItem, int __event,
		int __keyCode, int __modifiers)
		throws IllegalArgumentException, NullPointerException
	{
		if ((__canvas == null) == (__cItem == null))
			throw new IllegalArgumentException("NARG");
		
		// Potential key listener that is shared between both
		KeyListener defaultKL, customKL;
		if (__canvas != null)
		{
			defaultKL = __canvas.__defaultKeyListener();
			customKL = __canvas._keyListener;
		}
		
		// Otherwise custom items are used
		else
		{
			defaultKL = __cItem.__defaultKeyListener();
			customKL = __cItem._keyListener;
		}
		
		// Forward the event accordingly
		switch (__event)
		{
			case UIKeyEventType.KEY_PRESSED:
				defaultKL.keyPressed(__keyCode, __modifiers);
				if (customKL != null)
					customKL.keyPressed(__keyCode, __modifiers);
				break;
			
			case UIKeyEventType.KEY_RELEASE:
				defaultKL.keyReleased(__keyCode, __modifiers);
				if (customKL != null)
					customKL.keyReleased(__keyCode, __modifiers);
				break;
			
			case UIKeyEventType.KEY_REPEATED:
				defaultKL.keyRepeated(__keyCode, __modifiers);
				if (customKL != null)
					customKL.keyRepeated(__keyCode, __modifiers);
				break;
		}
	}
}
