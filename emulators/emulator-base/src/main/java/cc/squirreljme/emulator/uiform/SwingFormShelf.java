// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.emulator.uiform;

import cc.squirreljme.jvm.mle.UIFormShelf;
import cc.squirreljme.jvm.mle.brackets.UIDisplayBracket;
import cc.squirreljme.jvm.mle.brackets.UIDrawableBracket;
import cc.squirreljme.jvm.mle.brackets.UIFormBracket;
import cc.squirreljme.jvm.mle.brackets.UIItemBracket;
import cc.squirreljme.jvm.mle.brackets.UIWidgetBracket;
import cc.squirreljme.jvm.mle.callbacks.UIDisplayCallback;
import cc.squirreljme.jvm.mle.callbacks.UIFormCallback;
import cc.squirreljme.jvm.mle.constants.UIInputFlag;
import cc.squirreljme.jvm.mle.constants.UIItemPosition;
import cc.squirreljme.jvm.mle.constants.UIItemType;
import cc.squirreljme.jvm.mle.constants.UIMetricType;
import cc.squirreljme.jvm.mle.constants.UIPixelFormat;
import cc.squirreljme.jvm.mle.constants.UIWidgetProperty;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.awt.DisplayMode;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.lang.reflect.InvocationTargetException;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Java Swing implementation of the  form shelf.
 *
 * @since 2020/07/01
 */
public final class SwingFormShelf
{
	/** Alpha transparency mask. */
	private static final int _ALPHA_MASK =
		0xFF_000000;
	
	/** Color mask. */
	private static final int _COLOR_MASK =
		0x00_FFFFFF;
	
	/** Injector into Swing. */
	private static final UIFormCallback _INJECTOR =
		new SwingInjector();
	
	/**
	 * Not used.
	 * 
	 * @since 2020/07/01
	 */
	private SwingFormShelf()
	{
	}
	
	/**
	 * As {@link UIFormShelf#callback(UIDisplayBracket, UIDisplayCallback)}.
	 * 
	 * @param __display The display that the callback will act under.
	 * @param __callback The callback to register.
	 * @throws MLECallError If {@code __display} is {@code null}.
	 * @see UIDisplayCallback
	 * @since 2023/01/14
	 */
	public static void callback(UIDisplayBracket __display,
		UIDisplayCallback __callback)
		throws MLECallError
	{
		if (__display == null)
			throw new MLECallError("No form specified.");
		
		((SwingDisplay)__display).setCallback(__callback);
	}
	
	/**
	 * As {@link UIFormShelf#callback(UIFormBracket, UIFormCallback)}. 
	 * 
	 * @param __form The form that the callback will act under.
	 * @param __callback The callback to register.
	 * @throws MLECallError If {@code __form} is {@code null}.
	 * @since 2020/07/03
	 */
	public static void callback(UIFormBracket __form,
		UIFormCallback __callback)
		throws MLECallError
	{
		if (__form == null)
			throw new MLECallError("No form specified.");
		
		((SwingForm)__form).setCallback(__callback);
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
		// If there is no head, then just return nothing
		try
		{
			Toolkit.getDefaultToolkit().getScreenSize();
		}
		catch (HeadlessException e)
		{
			return new UIDisplayBracket[0];
		}
		
		// Just wrap the instance of the display
		return new UIDisplayBracket[]{SwingDisplay.getInstance()};
	}
	
	/**
	 * As {@link UIFormShelf#displayCurrent(UIDisplayBracket)}. 
	 * 
	 * @param __display The display to query.
	 * @return The form that is currently shown or {@code null}.
	 * @throws MLECallError On null arguments.
	 * @since 2020/07/01
	 */
	public static UIFormBracket displayCurrent(UIDisplayBracket __display)
		throws MLECallError
	{
		if (__display == null)
			throw new MLECallError("Null argument.");
		
		return ((SwingDisplay)__display).current();
	}
	
	/**
	 * Handles {@link UIFormShelf#displayShow(UIDisplayBracket, boolean)}.
	 * 
	 * @param __display The display to show.
	 * @param __show Should the display be shown or hidden?
	 * @throws MLECallError If {@code __display} is {@code null} or there was
	 * an error showing the display.
	 * @since 2023/01/14
	 */
	@Api
	public static void displayShow(UIDisplayBracket __display,
		boolean __show)
		throws MLECallError
	{
		if (__display == null)
			throw new MLECallError("Null display.");
		
		((SwingDisplay)__display).show(__show);
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
	@Api
	public static void displayShow(UIDisplayBracket __display,
		UIFormBracket __form)
		throws MLECallError
	{
		if (__display == null)
			throw new MLECallError("Null display.");
		
		((SwingDisplay)__display).show((SwingForm)__form);
	}
	
	/**
	 * As {@link UIFormShelf#equals(UIDisplayBracket, UIDisplayBracket)}. 
	 * 
	 * @param __a The first.
	 * @param __b The second.
	 * @return If these are the same display.
	 * @throws MLECallError If either is {@code null}.
	 * @since 2020/07/01
	 */
	public static boolean equals(UIDisplayBracket __a,
		UIDisplayBracket __b)
		throws MLECallError
	{
		return __a == __b;
	}
	
	/**
	 * As {@link UIFormShelf#equals(UIDrawableBracket, UIDrawableBracket)}. 
	 * 
	 * @param __a The first.
	 * @param __b The second.
	 * @return If these are the same display.
	 * @throws MLECallError If either is {@code null}.
	 * @since 2023/01/13
	 */
	public static boolean equals(UIDrawableBracket __a,
		UIDrawableBracket __b)
		throws MLECallError
	{
		return __a == __b;
	}
	
	/**
	 * As {@link UIFormShelf#equals(UIFormBracket, UIFormBracket)}. 
	 * 
	 * @param __a The first.
	 * @param __b The second.
	 * @return If these are the same form.
	 * @throws MLECallError If either is {@code null}.
	 * @since 2020/07/01
	 */
	public static boolean equals(UIFormBracket __a,
		UIFormBracket __b)
		throws MLECallError
	{
		return __a == __b;
	}
	
	/**
	 * As {@link UIFormShelf#equals(UIItemBracket, UIItemBracket)}. 
	 * 
	 * @param __a The first.
	 * @param __b The second.
	 * @return If these are the same item.
	 * @throws MLECallError If either is {@code null}.
	 * @since 2020/07/01
	 */
	public static boolean equals(UIItemBracket __a,
		UIItemBracket __b)
		throws MLECallError
	{
		return __a == __b;
	}
	
	/**
	 * As {@link UIFormShelf#equals(UIWidgetBracket, UIWidgetBracket)}. 
	 * 
	 * @param __a The first.
	 * @param __b The second.
	 * @return If these are the same item.
	 * @throws MLECallError If either is {@code null}.
	 * @since 2020/09/20
	 */
	public static boolean equals(UIWidgetBracket __a,
		UIWidgetBracket __b)
		throws MLECallError
	{
		return __a == __b;
	}
	
	/**
	 * As {@link UIFormShelf#flushEvents()}.
	 * 
	 * @throws MLECallError If events could not be flushed.
	 * @since 2020/07/26
	 */
	public static void flushEvents()
		throws MLECallError
	{
		try
		{
			SwingUtilities.invokeAndWait(new NothingRunnable());
		}
		catch (InterruptedException|InvocationTargetException ignored)
		{
		}
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
	 * As {@link UIFormShelf#formItemAtPosition(UIFormBracket, int)}. 
	 * 
	 * @param __form The form to check.
	 * @param __pos The position to check, may be {@link UIItemPosition}.
	 * @return The item at the given position or {@code null} if it is not
	 * there.
	 * @throws MLECallError If the form is not valid or if the position is
	 * not valid.
	 * @since 2020/07/19
	 */
	public static UIItemBracket formItemAtPosition(UIFormBracket __form,
		int __pos)
		throws MLECallError
	{
		if (__form == null)
			throw new MLECallError("Null arguments.");
		
		return ((SwingForm)__form).itemAtPosition(__pos);
	}
	
	/**
	 * As {@link UIFormShelf#formItemCount(UIFormBracket)}. 
	 * 
	 * @param __form The form to get the count from.
	 * @return The number of normal items on the form.
	 * @throws MLECallError If the form is null or not valid.
	 * @since 2020/07/19
	 */
	public static int formItemCount(UIFormBracket __form)
		throws MLECallError
	{
		if (__form == null)
			throw new MLECallError("Null arguments.");
		
		return ((SwingForm)__form).itemCount();
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
	 * Handles {@link UIFormShelf#formRefresh(UIFormBracket)}
	 * 
	 * @param __form The form to refresh.
	 * @throws MLECallError On null arguments.
	 * @since 2022/07/20
	 */
	public static void formNew(UIFormBracket __form)
		throws MLECallError
	{
		if (__form == null)
			throw new MLECallError("No form");
		
		// Does a call later, but does refreshes the form
		((SwingForm)__form).refresh();
	}
	
	/**
	 * As {@link UIFormShelf#injector()}.
	 * 
	 * @return The injector for the shelf.
	 * @throws MLECallError If injecting is not supported.
	 * @since 2020/07/26
	 */
	public static UIFormCallback injector()
		throws MLECallError
	{
		return SwingFormShelf._INJECTOR;
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
	 * As {@link UIFormShelf#itemForm(UIItemBracket)}. 
	 * 
	 * @param __item The item to get.
	 * @return The form the item is on.
	 * @throws MLECallError If {@code __item} is {@code null}.
	 * @since 2021/01/03
	 */
	public static UIFormBracket itemForm(UIItemBracket __item)
		throws MLECallError
	{
		if (__item == null)
			throw new MLECallError("Null item.");
		
		return ((SwingItem)__item)._form;
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
	 * Handles {@link UIFormShelf#later(UIDisplayBracket, int)}.
	 *
	 * @param __display The display identifier.
	 * @param __serialId The serial identifier.
	 * @throws MLECallError If the call is not valid.
	 * @since 2020/10/03
	 */
	public static void later(UIDisplayBracket __display, int __serialId)
		throws MLECallError
	{
		SwingUtilities.invokeLater(
			new CallLater((SwingDisplay)__display, __serialId));
	}
	
	/**
	 * Handles {@link UIFormShelf#metric(UIDisplayBracket, int)}. 
	 * 
	 * @param __display The display used.
	 * @param __metricId The {@link UIMetricType}.
	 * @return Metric value.
	 * @throws MLECallError If the call is not valid.
	 * @since 2020/07/01
	 */
	public static int metric(UIDisplayBracket __display, int __metricId)
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
				
				// CurrentScreen width
			case UIMetricType.DISPLAY_CURRENT_WIDTH:
				return Toolkit.getDefaultToolkit().getScreenSize().width;
				
				// Current Screen height
			case UIMetricType.DISPLAY_CURRENT_HEIGHT:
				return Toolkit.getDefaultToolkit().getScreenSize().height;
			
				// Maximum display width 
			case UIMetricType.DISPLAY_MAX_WIDTH:
				return SwingFormShelf.__maxResolution(false);
			
				// Maximum display height
			case UIMetricType.DISPLAY_MAX_HEIGHT:
				return SwingFormShelf.__maxResolution(true);
			
				// Pixel format
			case UIMetricType.DISPLAY_PIXEL_FORMAT:
				return UIPixelFormat.INT_RGB888;
				
				// The display is not monochromatic
			case UIMetricType.DISPLAY_MONOCHROMATIC:
				return 0;
				
				// Input types supported (this is not a game console)
			case UIMetricType.INPUT_FLAGS:
				return UIInputFlag.KEYBOARD | UIInputFlag.POINTER |
					UIInputFlag.POINTER_MOTION;
			
				// Background for canvases
			case UIMetricType.COLOR_CANVAS_BACKGROUND:
				return UIManager.getColor("Panel.background")
					.getRGB() & SwingFormShelf._COLOR_MASK;
				
				// Vibration not supported
			case UIMetricType.SUPPORTS_VIBRATION:
				return 0;
				
				// Command and list height
			case UIMetricType.COMMAND_BAR_HEIGHT:
			case UIMetricType.LIST_ITEM_HEIGHT:
				return 16;
				
				// Backlight control not supported
			case UIMetricType.SUPPORTS_BACKLIGHT_CONTROL:
				return 0;
			
			default:
				throw new MLECallError("Unknown metric: " + __metricId);
		}
	}
	
	
	/**
	 * As {@link UIFormShelf#widgetProperty(UIWidgetBracket, int, int, int)}.
	 * 
	 * @param __item The item to set.
	 * @param __intProp The {@link UIWidgetProperty}.
	 * @param __sub The sub-index.
	 * @param __newValue The new value to set.
	 * @throws MLECallError If the item is not valid or the property is not
	 * valid or not an integer property.
	 * @since 2020/09/13
	 */
	public static void widgetProperty(UIWidgetBracket __item,
		int __intProp, int __sub, int __newValue)
		throws MLECallError
	{
		if (__item == null)
			throw new MLECallError("Null item.");
		
		// Forward
		((SwingWidget)__item).property(__intProp, __sub, __newValue);
	}
	
	/**
	 * As {@link UIFormShelf#widgetProperty(UIWidgetBracket, int, int, String)}. 
	 * 
	 * @param __item The item to set.
	 * @param __strProp The {@link UIWidgetProperty}.
	 * @param __sub The sub-index.
	 * @param __newValue The new value to set.
	 * @throws MLECallError If the item is not valid or the property is not
	 * valid or not a string property.
	 * @since 2020/09/13
	 */
	public static void widgetProperty(UIWidgetBracket __item,
		int __strProp, int __sub, String __newValue)
		throws MLECallError
	{
		if (__item == null)
			throw new MLECallError("Null item.");
		
		// Forward
		((SwingWidget)__item).property(__strProp, __sub, __newValue);
	}
	
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
	public static int widgetPropertyInt(UIWidgetBracket __widget,
		int __intProp, int __sub)
		throws MLECallError
	{
		if (__widget == null)
			throw new MLECallError("No widget.");
		
		// Special property to get the type of item the widget is
		if (__intProp == UIWidgetProperty.INT_UIITEM_TYPE)
			return ((__widget instanceof SwingForm) ? UIItemType.FORM :
				((SwingItem)__widget).itemType);
		
		// Forward
		return ((SwingWidget)__widget).propertyInt(__intProp, __sub);
	}
	
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
	public static String widgetPropertyStr(UIWidgetBracket __widget,
		int __strProp, int __sub)
		throws MLECallError
	{
		if (__widget == null)
			throw new MLECallError("No widget.");
		
		// Forward
		return ((SwingWidget)__widget).propertyStr(__strProp, __sub);
	}
	
	/**
	 * Returns the max resolution.
	 * 
	 * @param __height Use the height.
	 * @return The resolution.
	 * @since 2020/10/04
	 */
	private static int __maxResolution(boolean __height)
	{
		// Determine the max supported resolution
		int rv = 0;
		for (DisplayMode mode : GraphicsEnvironment
			.getLocalGraphicsEnvironment().getDefaultScreenDevice()
			.getDisplayModes())
			rv = Math.max(rv, (__height ? mode.getHeight() : mode.getWidth()));
		
		// Limit screen size to 320x240 here since when testing large displays
		// cause issues
		if (__height)
			return Math.min(320, rv);
		return Math.min(240, rv);
	}
}
