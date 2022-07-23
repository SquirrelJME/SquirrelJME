// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.mle.fb;

import cc.squirreljme.jvm.mle.brackets.UIDisplayBracket;
import cc.squirreljme.jvm.mle.brackets.UIFormBracket;
import cc.squirreljme.jvm.mle.brackets.UIItemBracket;
import cc.squirreljme.jvm.mle.brackets.UIWidgetBracket;
import cc.squirreljme.jvm.mle.callbacks.UIDisplayCallback;
import cc.squirreljme.jvm.mle.callbacks.UIFormCallback;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.lcdui.mle.UIBackend;

/**
 * This is a virtual user interface form backend which is backed on the
 * raw framebuffer.
 *
 * @since 2020/07/19
 */
public abstract class FBUIBackend
	implements UIBackend
{
	/** The current set of displays. */
	private volatile FBDisplay[] _displays;
	
	/**
	 * Queries the displays that are available to the backend.
	 * 
	 * @return The displays to query.
	 * @since 2022/07/23
	 */
	protected abstract FBDisplay[] queryDisplays();
	
	/**
	 * {@inheritDoc}
	 * @since 2020/07/19
	 */
	@Override
	public final void callback(Object __ref, UIDisplayCallback __dc)
		throws MLECallError
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/07/19
	 */
	@Override
	public final void callback(UIFormBracket __form, UIFormCallback __callback)
		throws MLECallError
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/07/19
	 */
	@Override
	public final UIDisplayBracket[] displays()
		throws MLECallError
	{
		synchronized (this)
		{
			// Have we already cached all the displays?
			FBDisplay[] displays = this._displays;
			if (displays != null)
				return displays.clone();
			
			// Get the available displays
			displays = this.queryDisplays();
			if (displays == null)
				throw Debugging.oops();
			
			// Defensive copy
			displays = displays.clone();
			
			// Check for correctness
			for (FBDisplay display : displays)
				if (display == null)
					throw Debugging.oops();
			
			// Cache it and use it
			this._displays = displays;
			return displays.clone(); 
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/07/19
	 */
	@Override
	public final UIFormBracket displayCurrent(UIDisplayBracket __display)
		throws MLECallError
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/07/19
	 */
	@Override
	public final void displayShow(UIDisplayBracket __display,
		UIFormBracket __form)
		throws MLECallError
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/07/19
	 */
	@Override
	public final boolean equals(UIDisplayBracket __a, UIDisplayBracket __b)
		throws MLECallError
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/07/19
	 */
	@Override
	public final boolean equals(UIFormBracket __a, UIFormBracket __b)
		throws MLECallError
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/07/19
	 */
	@Override
	public final boolean equals(UIItemBracket __a, UIItemBracket __b)
		throws MLECallError
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/20
	 */
	@Override
	public final boolean equals(UIWidgetBracket __a, UIWidgetBracket __b)
		throws MLECallError
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/07/19
	 */
	@Override
	public final void flushEvents()
		throws MLECallError
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/07/19
	 */
	@Override
	public final void formDelete(UIFormBracket __form)
		throws MLECallError
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/07/19
	 */
	@Override
	public final UIItemBracket formItemAtPosition(UIFormBracket __form,
		int __pos)
		throws MLECallError
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/07/19
	 */
	@Override
	public final int formItemCount(UIFormBracket __form)
		throws MLECallError
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/07/19
	 */
	@Override
	public final int formItemPosition(UIFormBracket __form,
		UIItemBracket __item)
		throws MLECallError
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/07/19
	 */
	@Override
	public final void formItemPosition(UIFormBracket __form,
		UIItemBracket __item, int __pos)
		throws MLECallError
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/07/19
	 */
	@Override
	public final UIItemBracket formItemRemove(UIFormBracket __form, int __pos)
		throws MLECallError
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/07/19
	 */
	@Override
	public final UIFormBracket formNew()
		throws MLECallError
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/07/20
	 */
	@Override
	public final void formRefresh(UIFormBracket __form)
		throws MLECallError
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/07/26
	 */
	@Override
	public final UIFormCallback injector()
		throws MLECallError
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/07/19
	 */
	@Override
	public final void itemDelete(UIItemBracket __item)
		throws MLECallError
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/01/03
	 */
	@Override
	public final UIFormBracket itemForm(UIItemBracket __item)
		throws MLECallError
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/07/19
	 */
	@Override
	public final UIItemBracket itemNew(int __type)
		throws MLECallError
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/07/19
	 */
	@Override
	public final void later(int __displayId, int __serialId)
		throws MLECallError
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/07/19
	 */
	@Override
	public final int metric(int __metric)
		throws MLECallError
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/07/19
	 */
	@Override
	public final void widgetProperty(UIWidgetBracket __item, int __intProp,
		int __sub, int __newValue)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/07/19
	 */
	@Override
	public final void widgetProperty(UIWidgetBracket __item, int __strProp,
		int __sub, String __newValue)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/07/19
	 */
	@Override
	public final int widgetPropertyInt(UIWidgetBracket __widget, int __intProp,
		int __sub)
		throws MLECallError
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/07/19
	 */
	@Override
	public final String widgetPropertyStr(UIWidgetBracket __widget,
		int __strProp, int __sub)
		throws MLECallError
	{
		throw Debugging.todo();
	}
}
