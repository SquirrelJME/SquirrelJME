// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.emulator.uiform;

import cc.squirreljme.jvm.mle.UIFormShelf;
import cc.squirreljme.jvm.mle.brackets.UIDisplayBracket;
import cc.squirreljme.jvm.mle.brackets.UIFormBracket;
import cc.squirreljme.jvm.mle.brackets.UIItemBracket;
import cc.squirreljme.jvm.mle.constants.UIItemPosition;
import cc.squirreljme.jvm.mle.constants.UIItemType;
import cc.squirreljme.jvm.mle.constants.UIMetricType;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.awt.HeadlessException;
import java.awt.Toolkit;

/**
 * Java Swing implementation of the  form shelf.
 *
 * @since 2020/07/01
 */
public final class SwingFormShelf
{
	/**
	 * Not used.
	 * 
	 * @since 2020/07/01
	 */
	private SwingFormShelf()
	{
	}
	
	/**
	 * Handles {@link UIFormShelf#displays()}.
	 * 
	 * @return The display heads which are attached to a system, these objects
	 * here represent physical displays.
	 * @throws MLECallError If there are no displays.
	 * @since 2020/07/01
	 */
	public static UIDisplayBracket[] displays()
		throws MLECallError
	{
		// Just wrap the instance of the display
		return new UIDisplayBracket[]{SwingDisplay.getInstance()};
	}
	
	/**
	 * Handles
	 * {@link UIFormShelf#displayShow(UIDisplayBracket, UIFormBracket)}. 
	 * 
	 * @param __display The form to display on screen.
	 * @param __form The form to display.
	 * @throws MLECallError On {@code null} arguments.
	 * @since 2020/07/01
	 */
	public static void displayShow(UIDisplayBracket __display,
		UIFormBracket __form)
		throws MLECallError
	{
		if (__display == null || __form == null)
			throw new MLECallError("Null display or form.");
		
		((SwingDisplay)__display).show((SwingForm)__form);
	}
	
	/**
	 * Handles {@link UIFormShelf#formDelete(UIFormBracket)}. 
	 * 
	 * @param __bracket The form to delete.
	 * @throws MLECallError On null arguments or the form could not be deleted.
	 * @since 2020/07/01
	 */
	public static void formDelete(UIFormBracket __bracket)
		throws MLECallError
	{
		if (__bracket == null)
			throw new MLECallError("Null form.");
		
		((SwingForm)__bracket).delete();
	}
	
	/**
	 * As {@link UIFormShelf#formItemPosition(UIFormBracket, UIItemBracket)}. 
	 * 
	 * @param __form The form to get from.
	 * @param __item The item being queried.
	 * @return The position of the item, may be one of
	 * {@link UIItemPosition} or
	 * {@link UIItemPosition#NOT_ON_FORM} if not on the form.
	 * @throws MLECallError If the form and item are null.
	 * @since 2020/07/18
	 */
	public static int formItemPosition(UIFormBracket __form,
		UIItemBracket __item)
		throws MLECallError
	{
		if (__form == null || __item == null)
			throw new MLECallError("Null arguments.");
		
		return ((SwingForm)__form).itemPosition((SwingItem)__item);
	}
	
	/**
	 * As {@link
	 * UIFormShelf#formItemPosition(UIFormBracket, UIItemBracket, int)}. 
	 * 
	 * @param __form The form to set on.
	 * @param __item The item to set.
	 * @param __pos The position to set the item at, may be one of
	 * {@link UIItemPosition}.
	 * @throws MLECallError If the form or item are null, or the position
	 * is not valid.
	 * @since 2020/07/18
	 */
	public static void formItemPosition(UIFormBracket __form,
		UIItemBracket __item, int __pos)
		throws MLECallError
	{
		if (__form == null || __item == null)
			throw new MLECallError("Null arguments.");
		
		if (__pos < UIItemPosition.MIN_VALUE)
			throw new MLECallError("Invalid special position: " + __pos); 
		
		((SwingForm)__form).itemPosition((SwingItem)__item, __pos);
	}
	
	/**
	 * As {@link UIFormShelf#formItemRemove(UIFormBracket, int)}. 
	 * 
	 * @param __form The form to remove from.
	 * @param __pos The item to be removed.
	 * @return The item that was removed.
	 * @throws MLECallError If the form or item are null, the position is
	 * not valid, or there was no item at the position.
	 * @since 2020/07/18
	 */
	public static UIItemBracket formItemRemove(UIFormBracket __form, int __pos)
		throws MLECallError
	{
		if (__form == null)
			throw new MLECallError("Null arguments.");
		
		return ((SwingForm)__form).itemRemove(__pos);
	}
	
	/**
	 * Handles {@link UIFormShelf#formNew()}.
	 * 
	 * @return The newly created form.
	 * @throws MLECallError If the form could not be created.
	 * @since 2020/07/01
	 */
	public static UIFormBracket formNew()
		throws MLECallError
	{
		return new SwingForm();
	}
	
	/**
	 * As {@link UIFormShelf#itemDelete(UIItemBracket)}.
	 * 
	 * @param __item The item to delete.
	 * @throws MLECallError On null arguments or if the item could not be
	 * deleted.
	 * @since 2020/07/18
	 */
	public static void itemDelete(UIItemBracket __item)
		throws MLECallError
	{
		if (__item == null)
			throw new MLECallError("Null item.");
		
		((SwingItem)__item).delete();
	}
	
	/**
	 * As {@link UIFormShelf#itemNew(int)}. 
	 * 
	 * @param __type The {@link UIItemType} to create.
	 * @return The newly created item.
	 * @throws MLECallError If the item could not be created or the type was
	 * not valid.
	 * @since 2020/07/17
	 */
	public static UIItemBracket itemNew(int __type)
		throws MLECallError
	{
		if (__type < 0 || __type >= UIItemType.NUM_TYPES)
			throw new MLECallError("Out of range type: " + __type);
		
		switch (__type)
		{
			case UIItemType.CANVAS:
				return new SwingItemCanvas();
			
			case UIItemType.LABEL:
				return new SwingItemLabel();
			
			case UIItemType.HYPERLINK:
				return new SwingItemHyperlink();
			
			case UIItemType.BUTTON:
				return new SwingItemButton();
			
			case UIItemType.SINGLE_LINE_TEXT_BOX:
				return new SwingItemSingleLineTextBox();
			
			case UIItemType.MULTI_LINE_TEXT_BOX:
				return new SwingItemMultiLineTextBox();
			
			case UIItemType.SPACER:
				return new SwingItemSpacer();
			
			case UIItemType.ADJUSTABLE_GAUGE:
				return new SwingItemAdjustableGauge();
			
			case UIItemType.PROGRESS_INDICATOR:
				return new SwingItemProgressIndicator();
			
			case UIItemType.DATE:
				return new SwingItemDate();
			
			case UIItemType.TIME:
				return new SwingItemTime();
			
			case UIItemType.CHECK_BOX:
				return new SwingItemCheckBox();
			
			case UIItemType.RADIO_BUTTON:
				return new SwingItemRadioButton();
			
			case UIItemType.LIST:
				return new SwingItemList();
			
			default:
				throw Debugging.todo(__type);
		}
	}
	
	/**
	 * Handles {@link UIFormShelf#metric(int)}. 
	 * 
	 * @param __metricId The {@link UIMetricType}.
	 * @return Metric value.
	 * @throws MLECallError If the call is not valid.
	 * @since 2020/07/01
	 */
	public static int metric(int __metricId)
		throws MLECallError
	{
		if (__metricId < 0 || __metricId >= UIMetricType.NUM_METRICS)
			throw new MLECallError("Invalid metric.");
		
		switch (__metricId)
		{
				// Simplest way to check if forms are supported is if
				// the display is headless or not
			case UIMetricType.UIFORMS_SUPPORTED:
				try
				{
					// Just try to read the screen size
					if (null == Toolkit.getDefaultToolkit()
						.getScreenSize())
						return 0;
					
					return 1;
				}
				catch (HeadlessException e)
				{
					return 0;
				}
			
			default:
				throw new MLECallError("Unknown metric: " + __metricId);
		}
	}
}
