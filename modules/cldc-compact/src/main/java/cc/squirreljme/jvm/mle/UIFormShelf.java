// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle;

import cc.squirreljme.jvm.mle.brackets.UIDisplayBracket;
import cc.squirreljme.jvm.mle.brackets.UIFormBracket;
import cc.squirreljme.jvm.mle.brackets.UIItemBracket;
import cc.squirreljme.jvm.mle.callbacks.UIFormCallback;
import cc.squirreljme.jvm.mle.constants.UIItemPosition;
import cc.squirreljme.jvm.mle.constants.UIItemType;
import cc.squirreljme.jvm.mle.constants.UIMetricType;
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
	 * This is used to register the callback which is called with the user
	 * interface events and otherwise.
	 * 
	 * @param __callback The callback to register.
	 * @throws MLECallError If {@code __callback} is {@code null} or one has
	 * already been registered.
	 * @since 2020/07/03
	 */
	public static native void callback(UIFormCallback __callback)
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
	 * @throws MLECallError On {@code null} arguments.
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
	 * @throws MLECallError If either is {@code null}.
	 * @since 2020/07/01
	 */
	public static native boolean equals(UIDisplayBracket __a,
		UIDisplayBracket __b)
		throws MLECallError;
	
	/**
	 * Checks if the two displays represent the same {@link UIFormBracket}.
	 * 
	 * @param __a The first.
	 * @param __b The second.
	 * @return If these are the same form.
	 * @throws MLECallError If either is {@code null}.
	 * @since 2020/07/01
	 */
	public static native boolean equals(UIFormBracket __a,
		UIFormBracket __b)
		throws MLECallError;
	
	/**
	 * Checks if the two displays represent the same {@link UIItemBracket}.
	 * 
	 * @param __a The first.
	 * @param __b The second.
	 * @return If these are the same item.
	 * @throws MLECallError If either is {@code null}.
	 * @since 2020/07/01
	 */
	public static native boolean equals(UIItemBracket __a,
		UIItemBracket __b)
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
	 * is not valid.
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
}
