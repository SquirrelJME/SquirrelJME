// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.mle.fb;

import cc.squirreljme.jvm.mle.UIFormShelf;
import cc.squirreljme.jvm.mle.brackets.UIDisplayBracket;
import cc.squirreljme.jvm.mle.brackets.UIFormBracket;
import cc.squirreljme.jvm.mle.brackets.UIItemBracket;
import cc.squirreljme.jvm.mle.callbacks.UIDisplayCallback;
import cc.squirreljme.jvm.mle.callbacks.UIFormCallback;
import cc.squirreljme.jvm.mle.constants.UIWidgetProperty;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * This is a callback which handles anything that {@link UIFormShelf} wants
 * to be done so that this may direct and flow into the framebuffer
 * accordingly.
 *
 * @since 2022/07/23
 */
final class __UIFormFBDisplayCallback__
	implements UIDisplayCallback, UIFormCallback
{
	/** The framebuffer display we are linked to. */
	private final Reference<UIFormFBDisplay> _display;
	
	/**
	 * Initializes the callback.
	 * 
	 * @param __display The display to call into.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/07/23
	 */
	public __UIFormFBDisplayCallback__(UIFormFBDisplay __display)
		throws NullPointerException
	{
		if (__display == null)
			throw new NullPointerException("NARG");
		
		this._display = new WeakReference<>(__display);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/07/23
	 */
	@Override
	public void eventKey(UIFormBracket __form, UIItemBracket __item,
		int __event, int __keyCode, int __modifiers)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/07/23
	 */
	@Override
	public void eventMouse(UIFormBracket __form, UIItemBracket __item,
		int __event, int __button, int __x, int __y, int __modifiers)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/07/23
	 */
	@Override
	public void exitRequest(UIFormBracket __form)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/07/23
	 */
	@Override
	public void formRefresh(UIFormBracket __form, int __sx, int __sy,
	 int __sw,
		int __sh)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/07/23
	 */
	@Override
	public void later(int __displayId, int __serialId)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/07/23
	 */
	@Override
	public void paint(UIFormBracket __form, UIItemBracket __item, int __pf,
		int __bw, int __bh, Object __buf, int __offset, int[] __pal, int __sx,
		int __sy, int __sw, int __sh, int __special)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/07/23
	 */
	@Override
	public void paintDisplay(UIDisplayBracket __display, int __pf, int __bw,
		int __bh, Object __buf, int __offset, int[] __pal, int __sx, int __sy,
		int __sw, int __sh, int __special)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/07/23
	 */
	@Override
	public void propertyChange(UIFormBracket __form, UIItemBracket __item,
		int __intProp, int __sub, int __old, int __new)
	{
		UIFormFBDisplay display = this.__display();
		
		// Ignore property changes that happen outside of our form and canvas
		if (!UIFormShelf.equals(__form, display._uiForm) ||
			!UIFormShelf.equals(__item, display._uiCanvas))
			return;
		
		// Which property changed?
		switch (__intProp)
		{
				// Canvas (aka framebuffer) changed size
			case UIWidgetProperty.INT_WIDTH_AND_HEIGHT:
				display.updateSize(__old, __new);
				break;
			
			default:
				Debugging.debugNote("Unhandled property: %d", __intProp);
				break;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/07/23
	 */
	@Override
	public void propertyChange(UIFormBracket __form, UIItemBracket __item,
		int __strProp, int __sub, String __old, String __new)
	{
		UIFormFBDisplay display = this.__display();
		
		// Ignore property changes that happen outside of our form and canvas
		if (!UIFormShelf.equals(__form, display._uiForm) ||
			!UIFormShelf.equals(__item, display._uiCanvas))
			return;
		
		// Which property changed?
		switch (__strProp)
		{
			default:
				Debugging.debugNote("Unhandled property: %d", __strProp);
				break;
		}
	}
	
	/**
	 * Returns the attached display for the callback.
	 * 
	 * @return The display.
	 * @since 2022/07/25
	 */
	private UIFormFBDisplay __display()
	{
		// Check for garbage collection
		UIFormFBDisplay result = this._display.get();
		if (result == null)
			throw new MLECallError("GCGC");
		
		return result;
	}
}
