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
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/12
	 */
	@Override
	public void eventKey(UIFormBracket __form, UIItemBracket __item,
		int __event, int __keyCode, int __modifiers)
	{
		// Debug
		Debugging.debugNote("eventKey(%08x, %08x, %d, %d, %x)",
			System.identityHashCode(__form), System.identityHashCode(__item),
			__event, __keyCode, __modifiers);
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
		Debugging.debugNote("eventMouse(%08x, %08x, %d, %d, %d, %d, %x)",
			System.identityHashCode(__form), System.identityHashCode(__item),
			__event, __button, __x, __y, __modifiers);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/12
	 */
	@Override
	public void exitRequest(UIFormBracket __form)
	{
		// Debug
		Debugging.debugNote("exitRequest(%08x) @ %s",
			System.identityHashCode(__form), Thread.currentThread());
		
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
	public void paint(UIFormBracket __form, UIItemBracket __item)
	{
		// Debug
		Debugging.debugNote("paint(%08x, %08x)",
			System.identityHashCode(__form), System.identityHashCode(__item));
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
		Debugging.debugNote("propertyChange(%08x, %08x, %d, %d, %d)",
			System.identityHashCode(__form), System.identityHashCode(__item),
			__intProp, __old, __new);
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
		Debugging.debugNote("propertyChange(%08x, %08x, %d, %s, %s)",
			System.identityHashCode(__form), System.identityHashCode(__item),
			__strProp, __old, __new);
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
			// Debug
			Debugging.debugNote("UI Loop...");
		
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
