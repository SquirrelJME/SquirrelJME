// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.mle.fb;

import cc.squirreljme.jvm.mle.brackets.UIDisplayBracket;
import cc.squirreljme.jvm.mle.brackets.UIDrawableBracket;
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
public class FBUIBackend
	implements UIBackend
{
	/**
	 * Initializes the framebuffer backend with the given attachment.
	 * 
	 * @param __a The attachment to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/10/09
	 */
	public FBUIBackend(FBAttachment __a)
		throws NullPointerException
	{
		if (__a == null)
			throw new NullPointerException("NARG");
		
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/01/14
	 */
	@Override
	public void callback(UIDisplayBracket __display,
		UIDisplayCallback __callback)
		throws MLECallError
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/07/19
	 */
	@Override
	public void callback(UIFormBracket __form, UIFormCallback __callback)
		throws MLECallError
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/07/19
	 */
	@Override
	public UIDisplayBracket[] displays()
		throws MLECallError
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/07/19
	 */
	@Override
	public UIFormBracket displayCurrent(UIDisplayBracket __display)
		throws MLECallError
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/01/14
	 */
	@Override
	public void displayShow(UIDisplayBracket __display, boolean __show)
		throws MLECallError
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/07/19
	 */
	@Override
	public void displayShow(UIDisplayBracket __display, UIFormBracket __form)
		throws MLECallError
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/07/19
	 */
	@Override
	public boolean equals(UIDisplayBracket __a, UIDisplayBracket __b)
		throws MLECallError
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/01/13
	 */
	@Override
	public boolean equals(UIDrawableBracket __a, UIDrawableBracket __b)
		throws MLECallError
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/07/19
	 */
	@Override
	public boolean equals(UIFormBracket __a, UIFormBracket __b)
		throws MLECallError
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/07/19
	 */
	@Override
	public boolean equals(UIItemBracket __a, UIItemBracket __b)
		throws MLECallError
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/20
	 */
	@Override
	public boolean equals(UIWidgetBracket __a, UIWidgetBracket __b)
		throws MLECallError
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/07/19
	 */
	@Override
	public void flushEvents()
		throws MLECallError
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/07/19
	 */
	@Override
	public void formDelete(UIFormBracket __form)
		throws MLECallError
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/07/19
	 */
	@Override
	public UIItemBracket formItemAtPosition(UIFormBracket __form, int __pos)
		throws MLECallError
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/07/19
	 */
	@Override
	public int formItemCount(UIFormBracket __form)
		throws MLECallError
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/07/19
	 */
	@Override
	public int formItemPosition(UIFormBracket __form, UIItemBracket __item)
		throws MLECallError
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/07/19
	 */
	@Override
	public void formItemPosition(UIFormBracket __form, UIItemBracket __item,
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
	public UIItemBracket formItemRemove(UIFormBracket __form, int __pos)
		throws MLECallError
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/07/19
	 */
	@Override
	public UIFormBracket formNew()
		throws MLECallError
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/07/20
	 */
	@Override
	public void formRefresh(UIFormBracket __form)
		throws MLECallError
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/07/26
	 */
	@Override
	public UIFormCallback injector()
		throws MLECallError
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/07/19
	 */
	@Override
	public void itemDelete(UIItemBracket __item)
		throws MLECallError
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/01/03
	 */
	@Override
	public UIFormBracket itemForm(UIItemBracket __item)
		throws MLECallError
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/07/19
	 */
	@Override
	public UIItemBracket itemNew(int __type)
		throws MLECallError
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/07/19
	 */
	@Override
	public void later(UIDisplayBracket __display, int __serialId)
		throws MLECallError
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/07/19
	 */
	@Override
	public int metric(UIDisplayBracket __display, int __metric)
		throws MLECallError
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/07/19
	 */
	@Override
	public void widgetProperty(UIWidgetBracket __item, int __intProp,
		int __sub, int __newValue)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/07/19
	 */
	@Override
	public void widgetProperty(UIWidgetBracket __item, int __strProp,
		int __sub, String __newValue)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/07/19
	 */
	@Override
	public int widgetPropertyInt(UIWidgetBracket __widget, int __intProp,
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
	public String widgetPropertyStr(UIWidgetBracket __widget, int __strProp,
		int __sub)
		throws MLECallError
	{
		throw Debugging.todo();
	}
}
