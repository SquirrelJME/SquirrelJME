// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.lcdui;

import cc.squirreljme.jvm.mle.brackets.UIFormBracket;
import cc.squirreljme.jvm.mle.brackets.UIItemBracket;
import cc.squirreljme.jvm.mle.callbacks.UIFormCallback;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.lcdui.mle.DisplayWidget;
import cc.squirreljme.runtime.lcdui.mle.PencilGraphics;
import cc.squirreljme.runtime.lcdui.mle.StaticDisplayState;
import cc.squirreljme.runtime.lcdui.mle.UIBackend;
import cc.squirreljme.runtime.lcdui.mle.UIBackendFactory;
import cc.squirreljme.runtime.midlet.ActiveMidlet;
import javax.microedition.midlet.MIDlet;

/**
 * This thread is responsible for handling graphics operations.
 *
 * @since 2020/09/12
 */
final class __MLEUIThread__
	implements Runnable, UIFormCallback
{
	/** The time to sleep for between periodic checks. */
	private static final long _SLEEP_TIME =
		5_000;
	
	/** Are we within a paint? */
	private boolean _inPaint;
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/12
	 */
	@Override
	public void eventKey(UIFormBracket __form, UIItemBracket __item,
		int __event, int __keyCode, int __modifiers)
	{
		// Debug
		/*Debugging.debugNote("eventKey(%08x, %08x, %d, %d, %x)",
			System.identityHashCode(__form), System.identityHashCode(__item),
			__event, __keyCode, __modifiers);*/
		
		DisplayWidget widget = StaticDisplayState.locate(__item);
		
		// Commands are special in that they are not actually displayables
		// so they get special handling
		if (widget instanceof __CommandWidget__)
			((__CommandWidget__)widget).__activate();
		
		// Canvas with key events
		else if (widget instanceof Canvas)
			throw Debugging.todo();
		
		// CustomItem with key events
		else if (widget instanceof CustomItem)
			throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/12
	 */
	@Override
	public void eventMouse(UIFormBracket __form, UIItemBracket __item,
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
	@Override
	public void exitRequest(UIFormBracket __form)
	{
		// Debug
		/*Debugging.debugNote("exitRequest(%08x) @ %s",
			System.identityHashCode(__form), Thread.currentThread());*/
		
		// Terminate the user interface
		StaticDisplayState.terminate();
		
		// Have the MIDlet destroy itself
		try
		{
			MIDlet midlet = ActiveMidlet.get();
			
			// Destroy the midlet
			midlet.notifyDestroyed();
		}
		
		// No active MIDlet, ignore
		catch (IllegalStateException ignored)
		{
			Debugging.debugNote("No current MIDlet?");
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/12
	 */
	@Override
	public void paint(UIFormBracket __form, UIItemBracket __item, int __pf,
		int __bw, int __bh, Object __buf, int __offset, int[] __pal, int __sx,
		int __sy, int __sw, int __sh)
	{
		// Debug
		/*Debugging.debugNote("paint(%08x, %08x, %d, " +
			"%d, %d, %s, %d, %s, " +
			"%d, %d, %d, %d)",
			System.identityHashCode(__form), System.identityHashCode(__item),
			__pf, __bw, __bh, __buf, __offset, __pal, __sx, __sy, __sw, __sh);
		*/
		
		// Since painting is a heavy operation, only allow single paints to
		// happen at a time. This should generally never be called
		// concurrently ever.
		synchronized (this)
		{
			if (this._inPaint)
				return;
			this._inPaint = true;
		}
		
		// Perform the painting, always clear the flag out
		try
		{
			// Try to use hardware accelerated graphics where possible
			Graphics gfx = PencilGraphics.hardwareGraphics(__pf,
				__bw, __bh, __buf, __offset, __pal, __sx, __sy, __sw, __sh);
			
			// Forward to one of the items that draws
			DisplayWidget widget = StaticDisplayState.locate(__item);
			if (widget instanceof Canvas)
				((Canvas)widget).__paint(gfx, __sw, __sh);
			else if (widget instanceof CustomItem)
				((CustomItem)widget).__paint(gfx, __sw, __sh);
		}
		finally
		{
			this._inPaint = false;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/12
	 */
	@Override
	public void propertyChange(UIFormBracket __form, UIItemBracket __item,
		int __intProp, int __old, int __new)
	{
		// Debug
		/*Debugging.debugNote("propertyChange(%08x, %08x, %d, %d, %d)",
			System.identityHashCode(__form), System.identityHashCode(__item),
			__intProp, __old, __new);*/
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/12
	 */
	@Override
	public void propertyChange(UIFormBracket __form, UIItemBracket __item,
		int __strProp, String __old, String __new)
	{
		// Debug
		/*Debugging.debugNote("propertyChange(%08x, %08x, %d, %s, %s)",
			System.identityHashCode(__form), System.identityHashCode(__item),
			__strProp, __old, __new);*/
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/12
	 */
	@Override
	public void run()
	{
		// Get the backend calls will occur on
		UIBackend backend = UIBackendFactory.getInstance();
		
		// Infinite UI loop, which performs periodic probes and background
		// actions
		for (;;)
		{
			// Stop if terminating
			if (StaticDisplayState.isTerminating())
				break;
			
			// Wait for the next run
			try
			{
				Thread.sleep(__MLEUIThread__._SLEEP_TIME);
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
}
