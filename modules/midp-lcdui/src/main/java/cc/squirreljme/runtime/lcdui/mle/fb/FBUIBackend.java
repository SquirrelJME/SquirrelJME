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
import cc.squirreljme.jvm.mle.constants.UIItemType;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.lcdui.mle.UIBackend;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import net.multiphasicapps.collections.Identity;
import net.multiphasicapps.collections.IdentityMap;

/**
 * This is a virtual user interface form backend which is backed on the
 * raw framebuffer.
 *
 * @since 2020/07/19
 */
public abstract class FBUIBackend
	implements UIBackend
{
	/** Forms that are available to the display. */
	private final List<FBUIForm> _forms =
		new ArrayList<>();
	
	/** Items that are available to the display. */
	private final List<BaseFBUIItem> _items =
		new ArrayList<>();
	
	/** Display callbacks. */
	private final Map<Object, UIDisplayCallback> _displayCallbacks =
		new IdentityMap<>(
			new LinkedHashMap<Identity<Object>, UIDisplayCallback>());
	
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
		if (__ref == null || __dc == null)
			throw new MLECallError("NARG");
		
		// Register it
		synchronized (this)
		{
			this._displayCallbacks.put(__ref, __dc);
		}
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
		if (__display == null)
			throw new MLECallError("NARG");
		
		synchronized (this)
		{
			FBDisplay display = this.__checkDisplay(__display);
			
			// Hiding form?
			if (__form == null)
			{
				// If no old form was shown then do nothing
				FBUIForm oldShown = display._shownForm;
				if (oldShown == null)
					return;
					
				// Unlink
				display.link(oldShown, false);
				
				// Done
				return;
			}
			
			// Check the form to make sure it is valid
			FBUIForm form = this.__checkForm(__form);
			
			// If an old form is being shown, hide it first before we display
			// the new one
			FBUIForm oldShown = display._shownForm;
			if (oldShown != null && oldShown != __form)
				this.displayShow(display, null);
			
			// If the display is not yet enabled/displayed then prepare it
			// for showing on the screen surface
			display.activate();
			
			// Link the form to this display
			display.link(form, true);
		}
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
		synchronized (this)
		{
			// Does nothing currently
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/07/19
	 */
	@Override
	public final void formDelete(UIFormBracket __form)
		throws MLECallError
	{
		synchronized (this)
		{
			FBUIForm form = this.__checkForm(__form);
			
			// {@squirreljme.error EB40 Form is currently attached to a
			// display.}
			if (form._display != null)
				throw new MLECallError("EB40");
			
			// Find form to unlink
			for (Iterator<FBUIForm> it = this._forms.iterator();
				it.hasNext();)
			{
				// If this is our form, delete it
				FBUIForm maybe = it.next();
				if (maybe == form)
				{
					it.remove();
					return;
				}
			}
		}
		
		// {@squirreljme.error EB3z Form has already been deleted or is not
		// valid.}
		throw new MLECallError("EB3z");
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
		synchronized (this)
		{
			FBUIForm result = new FBUIForm();
			
			// Store it for later
			this._forms.add(result);
			
			return result;
		}
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
		// Setup new item
		BaseFBUIItem item;
		switch (__type)
		{
			case UIItemType.CANVAS:
				item = new FBUIItemCanvas();
				break;
			
			case UIItemType.LABEL:
				item = new FBUIItemLabel();
				break;
			
				// {@squirreljme.error EB41 Invalid item type.}
			default:
				throw new MLECallError("EB41 " + __type);
		}
		
		// Remember item
		List<BaseFBUIItem> items = this._items;
		synchronized (this)
		{
			items.add(item);
		}
		
		// Return the new item
		return item;
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
	
	/**
	 * Checks that the display is valid and is owned by this framebuffer.
	 * 
	 * @param __display The display to check.
	 * @return Will return a cast {@code __display} if valid.
	 * @throws MLECallError If the display is not valid.
	 * @since 2022/07/23
	 */
	private FBDisplay __checkDisplay(UIDisplayBracket __display)
		throws MLECallError
	{
		if (!(__display instanceof FBDisplay))
			throw new MLECallError("CAST");
		
		synchronized (this)
		{
			// {@squirreljme.error EB33 Displays not yet known.}
			FBDisplay[] displays = this._displays;
			if (displays == null)
				throw new MLECallError("EB33");
			
			// Is the display here?
			for (FBDisplay display : displays)
				if (display == __display)
					return (FBDisplay)__display;
		}
		
		// {@squirreljme.error EB3o Display is not governed by this
		// framebuffer.}
		throw new MLECallError("EB3o");
	}
	
	/**
	 * Checks that the form is valid and is owned by this framebuffer.
	 * 
	 * @param __form The form to check.
	 * @return The cast {@code __form} if valid.
	 * @throws MLECallError If the form is not valid for this framebuffer.
	 * @since 2022/07/23
	 */
	private FBUIForm __checkForm(UIFormBracket __form)
		throws MLECallError
	{
		if (!(__form instanceof FBUIForm))
			throw new MLECallError("CAST");
		
		synchronized (this)
		{
			// Is the form here?
			for (FBUIForm form : this._forms)
				if (form == __form)
					return (FBUIForm)__form;
		}
		
		// {@squirreljme.error EB3w Form is not governed by this
		// framebuffer.}
		throw new MLECallError("EB3w");
	}
}
