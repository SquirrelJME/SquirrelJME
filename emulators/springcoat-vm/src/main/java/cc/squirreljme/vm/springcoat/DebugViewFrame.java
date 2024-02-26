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
import cc.squirreljme.jdwp.host.JDWPHostValue;
import cc.squirreljme.jdwp.host.views.JDWPViewFrame;
import java.lang.ref.Reference;

/**
 * Viewer for frames.
 *
 * @since 2021/04/11
 */
public class DebugViewFrame
	implements JDWPViewFrame
{
	/** The state of the debugger. */
	protected final Reference<JDWPHostState> state;
	
	/**
	 * Initializes the frame viewer.
	 * 
	 * @param __state The state.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/04/11
	 */
	public DebugViewFrame(Reference<JDWPHostState> __state)
	{
		if (__state == null)
			throw new NullPointerException("NARG");
		
		this.state = __state;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/04/11
	 */
	@Override
	public Object atClass(Object __which)
	{
		return ((SpringThreadFrame)__which).springClass;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/04/11
	 * @return
	 */
	@Override
	public long atCodeIndex(Object __which)
	{
		return ((SpringThreadFrame)__which).pcIndex();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/04/28
	 */
	@Override
	public long atLineIndex(Object __which)
	{
		return ((SpringThreadFrame)__which).pcSourceLine();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/04/11
	 */
	@Override
	public int atMethodIndex(Object __which)
	{
		SpringThreadFrame which = (SpringThreadFrame)__which;
		return which.method().methodIndex;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/04/11
	 */
	@Override
	public boolean isValid(Object __which)
	{
		return (__which instanceof SpringThreadFrame);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/04/14
	 */
	@Override
	public boolean readValue(Object __which, int __index, JDWPHostValue __out)
	{
		__out.set(((SpringThreadFrame)__which).loadLocal(
			Object.class, __index));
		return true;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/04/14
	 */
	@Override
	public int numValues(Object __which)
	{
		return ((SpringThreadFrame)__which).numLocals();
	}
}
