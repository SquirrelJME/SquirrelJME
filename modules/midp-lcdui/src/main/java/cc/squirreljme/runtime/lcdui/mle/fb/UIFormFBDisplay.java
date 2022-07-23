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
import cc.squirreljme.jvm.mle.callbacks.UIFormCallback;
import cc.squirreljme.jvm.mle.constants.UIItemPosition;
import cc.squirreljme.jvm.mle.constants.UIItemType;
import cc.squirreljme.jvm.mle.constants.UIWidgetProperty;
import cc.squirreljme.runtime.cldc.SquirrelJME;
import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * A display for {@link FBUIBackend}.
 *
 * @since 2022/07/23
 */
public class UIFormFBDisplay
	extends FBDisplay
{
	/** The display bracket to use. */
	final UIDisplayBracket _uiDisplay;
	
	/** The form that displays the canvas. */
	volatile UIFormBracket _uiForm;
	
	/** The displayed canvas. */
	volatile UIItemBracket _uiCanvas;
	
	/** Shared callback for the display and form. */
	private volatile __UIFormFBDisplayCallback__ _callback;
	
	/**
	 * Initializes the form display.
	 * 
	 * @param __uiDisplay The UI display to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/07/23
	 */
	public UIFormFBDisplay(UIDisplayBracket __uiDisplay)
		throws NullPointerException
	{
		if (__uiDisplay == null)
			throw new NullPointerException("NARG");
		
		this._uiDisplay = __uiDisplay;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/07/23
	 */
	@Override
	protected void performActivate()
	{
		synchronized (this)
		{
			// Setup shared callback for this display, create the callback
			// if missing
			__UIFormFBDisplayCallback__ callback = this._callback;
			if (callback == null)
				this._callback = (callback =
					new __UIFormFBDisplayCallback__(this));
			
			// Setup callback for the display for future events
			UIDisplayBracket uiDisplay = this._uiDisplay;
			UIFormShelf.callback(uiDisplay, callback);
			
			// Setup form to show on the display
			UIFormBracket uiForm = UIFormShelf.formNew();
			this._uiForm = uiForm;
			
			// Set title of the form
			UIFormShelf.widgetProperty(uiForm,
				UIWidgetProperty.STRING_FORM_TITLE, 0,
				"SquirrelJME " + SquirrelJME.RUNTIME_VERSION);
			
			// Setup canvas to be displayed
			UIItemBracket uiCanvas = UIFormShelf.itemNew(UIItemType.CANVAS);
			this._uiCanvas = uiCanvas;
			
			// Put canvas into the form, in fullscreen mode
			UIFormShelf.formItemPosition(uiForm, uiCanvas,
				UIItemPosition.BODY);
				
			// Register callback for the canvas and the form, we do not
			// worry about whether the form is displayed or removed at
			// a later point since only a single form will be associated with
			// a given display 
			UIFormShelf.callback(uiForm, (UIFormCallback)callback);
			
			// Show the form
			UIFormShelf.displayShow(uiDisplay, uiForm);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/07/23
	 */
	@Override
	protected void performLink(FBUIForm __form, boolean __link)
	{
		// No special handling is needed here
	}
}
