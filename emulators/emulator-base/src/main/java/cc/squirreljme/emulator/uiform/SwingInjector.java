// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.emulator.uiform;

import cc.squirreljme.jvm.mle.ThreadShelf;
import cc.squirreljme.jvm.mle.brackets.UIDrawableBracket;
import cc.squirreljme.jvm.mle.brackets.UIFormBracket;
import cc.squirreljme.jvm.mle.brackets.UIItemBracket;
import cc.squirreljme.jvm.mle.callbacks.UIFormCallback;
import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * Injector into Swing's code execution.
 *
 * @since 2020/10/03
 */
@Deprecated
public class SwingInjector
	implements UIFormCallback
{
	/**
	 * {@inheritDoc}
	 * @since 2020/10/03
	 */
	@Override
	public void eventKey(UIDrawableBracket __drawable,
		int __event, int __keyCode, int __modifiers)
	{
		UIFormCallback callback = SwingInjector.__drawableCallback(__drawable);
		
		if (callback != null)
			callback.eventKey(__drawable, __event, __keyCode, __modifiers);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/10/03
	 */
	@Override
	public void eventMouse(UIDrawableBracket __drawable,
		int __event, int __button, int __x, int __y, int __modifiers)
	{
		UIFormCallback callback = SwingInjector.__drawableCallback(__drawable);
		
		if (callback != null)
			callback.eventMouse(__drawable, __event, __button, __x, __y,
				__modifiers);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/10/03
	 */
	@Override
	public void exitRequest(UIDrawableBracket __drawable)
	{
		UIFormCallback callback = ((SwingForm)__drawable).callback();
		if (callback != null)
			callback.exitRequest(__drawable);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/07/20
	 */
	@Override
	public void formRefresh(UIFormBracket __form, int __sx, int __sy,
		int __sw, int __sh)
	{
		UIFormCallback callback = ((SwingForm)__form).callback();
		
		if (callback != null)
			callback.formRefresh(__form, __sx, __sy, __sw, __sh);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/10/03
	 */
	@Override
	public void paint(UIDrawableBracket __drawable, int __pf,
		int __bw, int __bh, Object __buf, int __offset, int[] __pal, int __sx,
		int __sy, int __sw, int __sh, int __special)
	{
		UIFormCallback callback = SwingInjector.__drawableCallback(__drawable);
		
		if (callback != null)
			callback.paint(__drawable, __pf, __bw, __bh, __buf, __offset,
				__pal, __sx, __sy, __sw, __sh, __special);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/10/03
	 */
	@Override
	public void propertyChange(UIFormBracket __form, UIItemBracket __item,
		int __intProp, int __sub, int __old, int __new)
	{
		UIFormCallback callback = ((SwingForm)__form).callback();
		if (callback != null)
			callback.propertyChange(__form, __item, __intProp,
				__sub, __old, __new);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/10/03
	 */
	@Override
	public void propertyChange(UIFormBracket __form, UIItemBracket __item,
		int __strProp, int __sub, String __old, String __new)
	{
		UIFormCallback callback = ((SwingForm)__form).callback();
		if (callback != null)
			callback.propertyChange(__form, __item, __strProp,
				__sub, __old, __new);
	}
	
	/**
	 * Returns the drawable for the callback.
	 * 
	 * @param __drawable The drawable to get the callback from.
	 * @return The callback for the drawable.
	 * @since 2023/01/23
	 */
	private static UIFormCallback __drawableCallback(
		UIDrawableBracket __drawable)
	{
		if (__drawable instanceof SwingForm)
			return ((SwingForm)__drawable).callback();
		else if (__drawable instanceof SwingItem)
			return ((SwingItem)__drawable).callback();
		else
			throw Debugging.todo();
	}
}
