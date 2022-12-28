// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle;

import cc.squirreljme.jvm.mle.brackets.UIDisplayBracket;
import cc.squirreljme.jvm.mle.brackets.UIFormBracket;
import cc.squirreljme.jvm.mle.brackets.UIItemBracket;
import cc.squirreljme.jvm.mle.brackets.UIWidgetBracket;
import cc.squirreljme.jvm.mle.callbacks.UIDisplayCallback;
import cc.squirreljme.jvm.mle.callbacks.UIFormCallback;
import cc.squirreljme.jvm.mle.constants.UIItemPosition;
import cc.squirreljme.jvm.mle.constants.UIItemType;
import cc.squirreljme.jvm.mle.constants.UIMetricType;
import cc.squirreljme.jvm.mle.constants.UIWidgetProperty;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;

/**
 * This is the shelf which manages all of the form based user interface that
 * LCDUI uses and such.
 * 
 * Every item on the form has an index, while some have special index numbers
 * which indicate that the element should be placed in a unique position. The
 * special positions are identified in {@link UIItemPosition}.
 *
 * @since 2020/06/30
 */
public final class UIFormShelf
{
	/**
	 * Not used.
	 * 
	 * @since 2020/06/30
	 */
	private UIFormShelf()
	{
	}
	
	/**
	 * Registers a display callback that is to be called when information about
	 * displays changes.
	 * 
	 * @param __ref The display this refers to, if it gets garbage collected
	 * then this becomes invalidated.
	 * @param __dc The display callback to use.
	 * @throws MLECallError On null arguments.
	 * @see UIDisplayCallback
	 * @since 2020/10/03
	 */
	public static native void callback(Object __ref, UIDisplayCallback __dc)
		throws MLECallError;
	
	/**
	 * This is used to register the callback which is called with the user
	 * interface events and otherwise.
	 * 
	 * @param __form The form that the callback will act under.
	 * @param __callback The callback to register, may be {@code null} to
	 * remove the callback from the form.
	 * @throws MLECallError If {@code __form} is {@code null}.
	 * @see UIFormCallback
	 * @since 2020/07/03
	 */
	public static native void callback(UIFormBracket __form,
		UIFormCallback __callback)
		throws MLECallError;
	
	/**
	 * Returns the displays which are attached to the system.
	 * 
	 * @return The display heads which are attached to a system, these objects
	 * here represent physical displays.
	 * @throws MLECallError If there are no displays.
	 * @since 2020/07/01
	 */
	public static native UIDisplayBracket[] displays()
		throws MLECallError;
	
	/**
	 * Returns the form that is currently being shown on the display.
	 * 
	 * @param __display The display to query.
	 * @return The form that is currently shown or {@code null}.
	 * @throws MLECallError On null arguments.
	 * @since 2020/07/01
	 */
	public static native UIFormBracket displayCurrent(
		UIDisplayBracket __display)
		throws MLECallError;
	
	/**
	 * Show the given form on the display.
	 * 
	 * @param __display The form to display on screen.
	 * @param __form The form to display.
	 * @throws MLECallError On {@code __display} is {@code null}.
	 * @since 2020/07/01
	 */
	public static native void displayShow(UIDisplayBracket __display,
		UIFormBracket __form)
		throws MLECallError;
	
	/**
	 * Checks if the two displays represent the same {@link UIDisplayBracket}.
	 * 
	 * @param __a The first.
	 * @param __b The second.
	 * @return If these are the same display.
	 * @since 2020/07/01
	 */
	public static native boolean equals(UIDisplayBracket __a,
		UIDisplayBracket __b);
	
	/**
	 * Checks if the two displays represent the same {@link UIFormBracket}.
	 * 
	 * @param __a The first.
	 * @param __b The second.
	 * @return If these are the same form.
	 * @since 2020/07/01
	 */
	public static native boolean equals(UIFormBracket __a,
		UIFormBracket __b);
	
	/**
	 * Checks if the two displays represent the same {@link UIItemBracket}.
	 * 
	 * @param __a The first.
	 * @param __b The second.
	 * @return If these are the same item.
	 * @since 2020/07/01
	 */
	public static native boolean equals(UIItemBracket __a,
		UIItemBracket __b);
	
	/**
	 * Checks if the two displays represent the same {@link UIWidgetBracket}.
	 * 
	 * @param __a The first.
	 * @param __b The second.
	 * @return If these are the same item.
	 * @since 2020/09/20
	 */
	public static native boolean equals(UIWidgetBracket __a,
		UIWidgetBracket __b);
	
	/**
	 * Flushes all the events and forces them to be processed, waiting until
	 * all of them have been processed.
	 * 
	 * @throws MLECallError If events could not be flushed.
	 * @since 2020/07/26
	 */
	public static native void flushEvents()
		throws MLECallError;
	
	/**
	 * Deletes the given form.
	 * 
	 * @param __form The form to delete.
	 * @throws MLECallError On null arguments or the form could not be deleted.
	 * @since 2020/07/01
	 */
	public static native void formDelete(UIFormBracket __form)
		throws MLECallError;
	
	/**
	 * Returns the item at the given location.
	 * 
	 * @param __form The form to check.
	 * @param __pos The position to check, may be {@link UIItemPosition}.
	 * @return The item at the given position or {@code null} if it is not
	 * there.
	 * @throws MLECallError If the form is not valid or if the position is
	 * not valid.
	 * @since 2020/07/19
	 */
	public static native UIItemBracket formItemAtPosition(UIFormBracket __form,
		int __pos)
		throws MLECallError;
	
	/**
	 * Returns the number of items on the form.
	 * 
	 * @param __form The form to get the count from.
	 * @return The number of normal items on the form.
	 * @throws MLECallError If the form is null or not valid.
	 * @since 2020/07/19
	 */
	public static native int formItemCount(UIFormBracket __form)
		throws MLECallError;
	
	/**
	 * Returns the position of the item on the given form, or if it is not
	 * on the form.
	 * 
	 * @param __form The form to get from.
	 * @param __item The item being queried.
	 * @return The position of the item, may be one of
	 * {@link UIItemPosition} or
	 * {@link UIItemPosition#NOT_ON_FORM} if not on the form.
	 * @throws MLECallError If the form and item are null.
	 * @since 2020/07/18
	 */
	public static native int formItemPosition(UIFormBracket __form,
		UIItemBracket __item)
		throws MLECallError;
	
	/**
	 * Sets the position of a form's item.
	 * 
	 * @param __form The form to set on.
	 * @param __item The item to set.
	 * @param __pos The position to set the item at, may be one of
	 * {@link UIItemPosition}.
	 * @throws MLECallError If the form or item are null, or the position
	 * is not valid, or the item is on a different form than {@code __form}.
	 * @since 2020/07/18
	 */
	public static native void formItemPosition(UIFormBracket __form,
		UIItemBracket __item, int __pos)
		throws MLECallError;
	
	/**
	 * Removes the item at the given position on the form.
	 * 
	 * @param __form The form to remove from.
	 * @param __pos The item to be removed.
	 * @return The item that was removed.
	 * @throws MLECallError If the form or item are null, the position is
	 * not valid, or there was no item at the position.
	 * @since 2020/07/18
	 */
	public static native UIItemBracket formItemRemove(UIFormBracket __form,
		int __pos)
		throws MLECallError;
	
	/**
	 * Creates a new form.
	 * 
	 * @return The newly created form.
	 * @throws MLECallError If the form could not be created.
	 * @since 2020/07/01
	 */
	public static native UIFormBracket formNew()
		throws MLECallError;
	
	/**
	 * Forces a form to be refreshed.
	 * 
	 * @param __form The form to refresh
	 * @throws MLECallError On null arguments or the form is not valid.
	 * @since 2022/07/20
	 */
	public static native void formRefresh(UIFormBracket __form)
		throws MLECallError;
	
	/**
	 * Returns the injector for forms, which is used for testing purposes.
	 * 
	 * @return The injector for the shelf.
	 * @throws MLECallError If injecting is not supported.
	 * @since 2020/07/26
	 */
	public static native UIFormCallback injector()
		throws MLECallError;
	
	/**
	 * Deletes the specified item.
	 * 
	 * @param __item The item to delete.
	 * @throws MLECallError On null arguments, if the item could not be
	 * deleted, if the item was already deleted, or if the item is currently
	 * active within a form.
	 * @since 2020/07/18
	 */
	public static native void itemDelete(UIItemBracket __item)
		throws MLECallError;
	
	/**
	 * Returns the form the item is on.
	 * 
	 * @param __item The item to get.
	 * @return The form the item is on.
	 * @throws MLECallError If {@code __item} is {@code null}.
	 * @since 2021/01/03
	 */
	public static native UIFormBracket itemForm(UIItemBracket __item)
		throws MLECallError;
	
	/**
	 * Creates a new item.
	 * 
	 * @param __type The {@link UIItemType} to create.
	 * @return The newly created item.
	 * @throws MLECallError If the item could not be created or the type was
	 * not valid.
	 * @since 2020/07/17
	 */
	public static native UIItemBracket itemNew(int __type)
		throws MLECallError;
	
	/**
	 * Returns a metric which describes something about the user interface
	 * forms implementation or other details about the system.
	 * 
	 * @param __metric One of {@link UIMetricType}. The metric
	 * {@link UIMetricType#UIFORMS_SUPPORTED} is always a valid metric and
	 * must be supported, even if the implementation lacks forms.
	 * @return The value of the metric.
	 * @throws MLECallError If the metric is out of range or forms are not
	 * supported and the metric is not {@link UIMetricType#UIFORMS_SUPPORTED}.
	 * @since 2020/06/30
	 */
	public static native int metric(int __metric)
		throws MLECallError;
	
	/**
	 * Calls the given method at a later time.
	 * 
	 * @param __displayId The display identifier.
	 * @param __serialId The serial identifier.
	 * @throws MLECallError If the call is not valid.
	 * @since 2020/10/03
	 */
	public static native void later(int __displayId, int __serialId)
		throws MLECallError;
	
	/**
	 * Sets the given item property.
	 * 
	 * @param __widget The item to set.
	 * @param __intProp The {@link UIWidgetProperty}.
	 * @param __sub The sub-index.
	 * @param __newValue The new value to set.
	 * @throws MLECallError If the item is not valid or the property is not
	 * valid or not an integer property.
	 * @since 2020/09/13
	 */
	public static native void widgetProperty(UIWidgetBracket __widget,
		int __intProp, int __sub, int __newValue)
		throws MLECallError;
	
	/**
	 * Sets the given item property.
	 * 
	 * @param __widget The item to set.
	 * @param __strProp The {@link UIWidgetProperty}.
	 * @param __sub The sub-index.
	 * @param __newValue The new value to set.
	 * @throws MLECallError If the item is not valid or the property is not
	 * valid or not a string property.
	 * @since 2020/09/13
	 */
	public static native void widgetProperty(UIWidgetBracket __widget,
		int __strProp, int __sub, String __newValue)
		throws MLECallError;
	
	/**
	 * Gets a property of the given widget.
	 * 
	 * @param __widget The widget to get from.
	 * @param __intProp The {@link UIWidgetProperty}.
	 * @param __sub The sub-index.
	 * @return The value of the property.
	 * @throws MLECallError If the widget or property is not valid.
	 * @since 2020/09/21
	 */
	public static native int widgetPropertyInt(UIWidgetBracket __widget,
		int __intProp, int __sub)
		throws MLECallError;
	
	/**
	 * Gets a property of the given widget.
	 * 
	 * @param __widget The widget to get from.
	 * @param __strProp The {@link UIWidgetProperty}.
	 * @param __sub The sub-index.
	 * @return The value of the property.
	 * @throws MLECallError If the widget or property is not valid.
	 * @since 2020/09/21
	 */
	public static native String widgetPropertyStr(UIWidgetBracket __widget,
		int __strProp, int __sub)
		throws MLECallError;
}
