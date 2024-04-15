// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package mleui.forms;

import cc.squirreljme.jvm.mle.brackets.UIDisplayBracket;
import cc.squirreljme.jvm.mle.brackets.UIFormBracket;
import cc.squirreljme.jvm.mle.constants.UIItemType;
import cc.squirreljme.jvm.mle.constants.UIWidgetProperty;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.runtime.lcdui.mle.UIBackend;
import mleui.FailingExecution;

/**
 * Tests that form properties are correct.
 *
 * @since 2020/10/11
 */
public class TestFormProperties
	extends BaseUIForm
{
	/** Invalid string. */
	private static final String _INVALID_STRING =
		"\0\0\0\0INVALID\0STRING\0\0\0\0";
	
	/**
	 * {@inheritDoc}
	 * @since 2020/10/11
	 */
	@Override
	protected void test(UIBackend __backend, UIDisplayBracket __display,
		UIFormBracket __form)
	{
		// Add a button to give the form some size
		__backend.formItemPosition(__form,
			__backend.itemNew(UIItemType.BUTTON), 0);
		__backend.flushEvents();
		
		// Test each individual metric
		for (int prop = -1; prop <= UIWidgetProperty.NUM_PROPERTIES; prop++)
			try
			{
				this.testProperty(__backend, __form, prop);
				
				// Should have failed
				if (prop <= 0 || prop >= UIWidgetProperty.NUM_PROPERTIES)
					this.secondary("bad-neg-pos", true);
			}
			catch (MLECallError e)
			{
				// Is okay to fail here
				if (prop < 0)
					this.secondary("neg-fails", true);
				else if (prop == 0)
					this.secondary("null-fails", true);
				else if (prop >= UIWidgetProperty.NUM_PROPERTIES)
					this.secondary("pos-fails", true);
				
				// Fails here
				else
				{
					e.printStackTrace();
					
					this.secondary("fail-" + prop, true);
				}
			}
		
		this.secondary("total", (int)UIWidgetProperty.NUM_PROPERTIES);
	}
	
	/**
	 * Tests that the given property are correctly implemented.
	 * 
	 * @param __backend The backend to test.
	 * @param __property The {@link UIWidgetProperty} to test.
	 * @since 2020/10/11
	 */
	private void testProperty(UIBackend __backend, UIFormBracket __form,
		int __property)
	{
		MLECallError[] exceptions = new MLECallError[2];
		
		// Try to obtain the integer value
		Integer iVal = null;
		try
		{
			iVal = __backend.widgetPropertyInt(__form, __property, 0);
		}
		catch (MLECallError e)
		{
			exceptions[0] = e;
		}
		
		// Try to obtain the string value
		String sVal = TestFormProperties._INVALID_STRING;
		try
		{
			sVal = __backend.widgetPropertyStr(__form, __property, 0);
		}
		catch (MLECallError e)
		{
			exceptions[1] = e;
		}
		
		// This may be a possible failure, if both could not be gotten
		// noinspection StringEquality
		boolean noEitherGet = (iVal == null &&
			sVal == TestFormProperties._INVALID_STRING);
		
		// Do actions based on whatever the property is...
		switch (__property)
		{
				// These are just failing properties
			case -1:
			case UIWidgetProperty.NULL:
			case UIWidgetProperty.NUM_PROPERTIES:
				break;
			
			case UIWidgetProperty.STRING_LABEL:
				this.secondary("no-label", noEitherGet);
				return;
			
			case UIWidgetProperty.INT_SIGNAL_REPAINT:
				this.secondary("no-repaint", noEitherGet);
				return;
			
			case UIWidgetProperty.INT_WIDTH:
				if (iVal != null)
					this.secondary("positive-width", iVal > 0);
				break;
			
			case UIWidgetProperty.INT_HEIGHT:
				if (iVal != null)
					this.secondary("positive-height", iVal > 0);
				break;
			
			case UIWidgetProperty.INT_X_POSITION:
				this.secondary("has-x", !noEitherGet);
				break;
			
			case UIWidgetProperty.INT_Y_POSITION:
				this.secondary("has-y", !noEitherGet);
				break;
			
			case UIWidgetProperty.INT_IS_SHOWN:
				this.secondary("has-shown", !noEitherGet);
				break;
			
			case UIWidgetProperty.INT_WIDTH_AND_HEIGHT:
				this.secondary("no-wah", noEitherGet);
				return; 
			
			case UIWidgetProperty.INT_LIST_TYPE:
				this.secondary("no-list-type", noEitherGet);
				return;
			
			case UIWidgetProperty.INT_LIST_ITEM_DISABLED:
				this.secondary("no-list-enabled", noEitherGet);
				return;
			
			case UIWidgetProperty.INT_LIST_ITEM_ICON_DIMENSION:
				this.secondary("no-list-paint", noEitherGet);
				return;
			
			case UIWidgetProperty.INT_LIST_ITEM_SELECTED:
				this.secondary("no-list-selected", noEitherGet);
				return;
			
			case UIWidgetProperty.INT_NUM_ELEMENTS:
				this.secondary("no-num-elements", noEitherGet);
				return;
			
			case UIWidgetProperty.STRING_LIST_ITEM_LABEL:
				this.secondary("no-list-label", noEitherGet);
				return;
			
			case UIWidgetProperty.INT_LIST_ITEM_ID_CODE:
				this.secondary("no-list-idcode", noEitherGet);
				return;
			
			case UIWidgetProperty.INT_LIST_ITEM_FONT:
				this.secondary("no-list-font", noEitherGet);
				return;
			
			case UIWidgetProperty.INT_UIITEM_TYPE:
				this.secondary("is-form", iVal == UIItemType.FORM);
				break;
			
			case UIWidgetProperty.INT_UPDATE_LIST_SELECTION_LOCK:
				this.secondary("no-selection-lock", noEitherGet);
				return;
			
			case UIWidgetProperty.STRING_FORM_TITLE:
				this.secondary("has-form-title", !noEitherGet);
				return;
				
			case UIWidgetProperty.INT_SIGNAL_FOCUS:
				this.secondary("no-signal-focus", noEitherGet);
				return;
			
			default:
				throw new FailingExecution("Missing " + __property);
		}
		
		// Could not get either of the two values, so fail
		if (noEitherGet)
		{
			MLECallError fail = new MLECallError("No properties.");
			for (MLECallError e : exceptions)
				if (e != null)
					fail.addSuppressed(e);
			
			throw fail;
		}
	}
}
