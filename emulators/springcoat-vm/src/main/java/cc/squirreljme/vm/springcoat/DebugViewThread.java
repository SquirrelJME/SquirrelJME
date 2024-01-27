// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat;

import cc.squirreljme.jdwp.host.JDWPHostState;
import cc.squirreljme.jdwp.host.JDWPHostStepTracker;
import cc.squirreljme.jdwp.host.JDWPHostThreadSuspension;
import cc.squirreljme.jdwp.host.views.JDWPViewThread;
import cc.squirreljme.vm.springcoat.brackets.VMThreadObject;
import java.lang.ref.Reference;
import java.util.Arrays;
import java.util.Collections;

/**
 * Viewer for threads.
 *
 * @since 2021/04/10
 */
public class DebugViewThread
	implements JDWPViewThread
{
	/** The state of the debugger. */
	protected final Reference<JDWPHostState> state;
	
	/**
	 * Initializes the thread viewer.
	 * 
	 * @param __state The state.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/04/10
	 */
	public DebugViewThread(Reference<JDWPHostState> __state)
	{
		if (__state == null)
			throw new NullPointerException("NARG");
		
		this.state = __state;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/04/10
	 */
	@Override
	public boolean isValid(Object __which)
	{
		return (__which instanceof SpringThread);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/04/11
	 */
	@Override
	public Object[] frames(Object __which)
	{
		SpringThreadFrame[] frames = ((SpringThread)__which).frames();
		Object[] rv = new Object[frames.length];
		
		// Filter out any blank frames because it does not make sense to
		// the debugger at all
		int at = 0;
		for (SpringThreadFrame frame : frames)
			if (!frame.isBlank())
				rv[at++] = frame;
		
		// Spring Coat does threads in the reverse order, so it needs to be
		// flipped to be in Java order
		rv = (at == frames.length ? rv : Arrays.<Object>copyOf(rv, at));
		Collections.reverse(Arrays.asList(rv));
		return rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/09/24
	 */
	@Override
	public Object fromBracket(Object __bracket)
	{
		return ((VMThreadObject)__bracket).getThread();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/04/18
	 */
	@Override
	public Object instance(Object __which)
	{
		try
		{
			return ((SpringThread)__which).threadInstance();
		}
		catch (IllegalStateException ignored)
		{
			return null;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/04/30
	 */
	@Override
	public void interrupt(Object __which)
	{
		((SpringThread)__which).hardInterrupt();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/09/23
	 */
	@Override
	public boolean isDebugCallback(Object __thread)
	{
		return ((SpringThread)__thread).noDebugSuspend;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/04/19
	 */
	@Override
	public boolean isTerminated(Object __which)
	{
		return ((SpringThread)__which).isTerminated();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/04/10
	 */
	@Override
	public String name(Object __which)
	{
		return ((SpringThread)__which).name();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/04/10
	 */
	@Override
	public Object parentGroup(Object __which)
	{
		return ((SpringThread)__which).machine();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/04/28
	 */
	@Override
	public JDWPHostStepTracker stepTracker(Object __which)
	{
		SpringThread thread = (SpringThread)__which;
		
		// Is the tracker existing already?
		JDWPHostStepTracker stepTracker = thread._stepTracker;
		if (stepTracker != null)
			return stepTracker;
		
		// Create and store it for later
		thread._stepTracker = (stepTracker = new JDWPHostStepTracker());
		return stepTracker;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/04/10
	 */
	@Override
	public JDWPHostThreadSuspension suspension(Object __which)
	{
		return ((SpringThread)__which).debuggerSuspension;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/04/10
	 */
	@Override
	public int status(Object __which)
	{
		return ((SpringThread)__which)._status;
	}
}
