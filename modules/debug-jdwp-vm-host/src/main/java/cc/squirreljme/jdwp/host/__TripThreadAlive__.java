// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp.host;

import cc.squirreljme.jdwp.JDWPEventKind;
import cc.squirreljme.jdwp.host.trips.JDWPTripThread;
import cc.squirreljme.jdwp.host.views.JDWPViewFrame;
import cc.squirreljme.jdwp.host.views.JDWPViewThread;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.lang.ref.Reference;

/**
 * Trip when a thread changes state or has an action or otherwise.
 *
 * @since 2021/04/11
 */
final class __TripThreadAlive__
	extends __TripBase__
	implements JDWPTripThread
{
	/**
	 * Initializes the trip.
	 * 
	 * @param __controller The controller.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/04/11
	 */
	__TripThreadAlive__(Reference<JDWPHostController> __controller)
		throws NullPointerException
	{
		super(__controller);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/04/11
	 */
	@Override
	public void alive(Object __thread, boolean __isAlive)
	{
		JDWPHostController controller = this.__controller();
		JDWPHostState state = controller.getState();
		
		// Is this the first thread ever called? If it is then implicitly
		// say that the virtual machine is alive now
		if (__isAlive && state.latchFirstThread())
			controller.signal(__thread, JDWPEventKind.VM_START, __thread);
		
		// Register this thread for later use
		state.items.put(__thread);
		
		// Forward generic event
		controller.signal(__thread, (__isAlive ? JDWPEventKind.THREAD_START :
			JDWPEventKind.THREAD_DEATH), __thread);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/04/28
	 */
	@Override
	public void step(Object __which, JDWPHostStepTracker __stepTracker)
	{
		// Do we report this?
		if (!this.__checkReport(__which))
			return;
		
		JDWPHostController controller = this.__controller();
		JDWPViewThread viewThread = controller.viewThread();
		JDWPViewFrame viewFrame = controller.viewFrame();
		JDWPHostState state = controller.getState();
		
		// Register this thread for later use
		state.items.put(__which);
		
		// Signal the step, if no events are found we likely are no longer
		// going to step so just stop
		JDWPHostStepTracker stepTracker = viewThread.stepTracker(__which);
		if (!controller.signal(__which, JDWPEventKind.SINGLE_STEP,
			stepTracker))
			stepTracker.clear();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/04/18
	 */
	@Override
	public void suspension(Object __thread, boolean __isSuspended)
	{
		// There are currently no triggers on this at all
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/07/05
	 */
	@Override
	public void unconditionalBreakpoint(Object __thread)
	{
		// Do we report this?
		if (!this.__checkReport(__thread))
			return;
		
		if (JDWPHostController._DEBUG)
			Debugging.debugNote("UNCONDITIONAL BREAKPOINT!");
		
		JDWPHostController controller = this.__controller();
		
		// Make sure this thread is registered
		controller.getState().items.put(__thread);
		
		// Send the signal
		controller.signal(__thread, JDWPEventKind.UNCONDITIONAL_BREAKPOINT);
	}
	
	/**
	 * Do we report on this thread?
	 * 
	 * @param __thread The thread to check.
	 * @return Do we report on this thread?
	 * @since 2022/09/23
	 */
	private final boolean __checkReport(Object __thread)
	{
		JDWPViewThread threadView = this.__controller().viewThread();
		
		return !threadView.isTerminated(__thread) &&
			!threadView.isDebugCallback(__thread) &&
			threadView.frames(__thread).length > 0;
	}
}
