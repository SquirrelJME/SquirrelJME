// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat;

import cc.squirreljme.jvm.mle.brackets.UIFormBracket;
import cc.squirreljme.jvm.mle.brackets.UIItemBracket;
import cc.squirreljme.jvm.mle.callbacks.UIFormCallback;
import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * This adapter is responsible for when it is called, to call into SpringCoat
 * whenever any events happen.
 *
 * @since 2020/09/13
 */
public class UIFormCallbackAdapter
	implements UIFormCallback
{
	/** The object to call into. */
	private final SpringObject callback;
	
	/** The machine to call for when callbacks occur. */
	private final SpringMachine machine;
	
	/**
	 * Initializes the callback adapter.
	 * 
	 * @param __machine The machine executing under.
	 * @param __cb The SpringCoat object to call.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/09/13
	 */
	public UIFormCallbackAdapter(SpringMachine __machine, SpringObject __cb)
		throws NullPointerException
	{
		if (__machine == null || __cb == null)
			throw new NullPointerException("NARG");
		
		this.machine = __machine;
		this.callback = __cb;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/13
	 */
	@Override
	public void eventKey(UIFormBracket __form, UIItemBracket __item,
		int __event, int __keyCode, int __modifiers)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/13
	 */
	@Override
	public void eventMouse(UIFormBracket __form, UIItemBracket __item,
		int __event, int __button, int __x, int __y, int __modifiers)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/13
	 */
	@Override
	public void exitRequest(UIFormBracket __form)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/13
	 */
	@Override
	public void paint(UIFormBracket __form, UIItemBracket __item)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/13
	 */
	@Override
	public void propertyChange(UIFormBracket __form, UIItemBracket __item,
		int __intProp, int __old, int __new)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/13
	 */
	@Override
	public void propertyChange(UIFormBracket __form, UIItemBracket __item,
		int __strProp, String __old, String __new)
	{
		throw Debugging.todo();
	}
}
