// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.emulator.uiform;

import cc.squirreljme.jvm.mle.brackets.UIFormBracket;
import cc.squirreljme.jvm.mle.brackets.UIItemBracket;
import cc.squirreljme.jvm.mle.callbacks.UIFormCallback;
import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * Injector into Swing's code execution.
 *
 * @since 2020/10/03
 */
public class SwingInjector
	implements UIFormCallback
{
	/**
	 * {@inheritDoc}
	 * @since 2020/10/03
	 */
	@Override
	public void eventKey(UIFormBracket __form, UIItemBracket __item,
		int __event, int __keyCode, int __modifiers)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/10/03
	 */
	@Override
	public void eventMouse(UIFormBracket __form, UIItemBracket __item,
		int __event, int __button, int __x, int __y, int __modifiers)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/10/03
	 */
	@Override
	public void exitRequest(UIFormBracket __form)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/10/03
	 */
	@Override
	public void paint(UIFormBracket __form, UIItemBracket __item, int __pf,
		int __bw, int __bh, Object __buf, int __offset, int[] __pal, int __sx,
		int __sy, int __sw, int __sh)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/10/03
	 */
	@Override
	public void propertyChange(UIFormBracket __form, UIItemBracket __item,
		int __intProp, int __old, int __new)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/10/03
	 */
	@Override
	public void propertyChange(UIFormBracket __form, UIItemBracket __item,
		int __strProp, String __old, String __new)
	{
		throw Debugging.todo();
	}
}
