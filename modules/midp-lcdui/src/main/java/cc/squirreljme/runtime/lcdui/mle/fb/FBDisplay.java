// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.mle.fb;

import cc.squirreljme.jvm.mle.brackets.UIDisplayBracket;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * Represents a single display within a framebuffer.
 *
 * @since 2022/07/23
 */
public abstract class FBDisplay
	implements UIDisplayBracket
{
	/** The currently shown form. */
	volatile FBUIForm _shownForm;
	
	/** Has this been activated? */
	private volatile boolean _hasActivated;
	
	/** Last known width. */
	private volatile int _lastWidth =
		-1;
	
	/** Last known height. */
	private volatile int _lastHeight =
		-1;
	
	/**
	 * Performs activation of the display.
	 * 
	 * @since 2022/07/23
	 */
	protected abstract void performActivate();
	
	/**
	 * Performs the linking or unlinking of the form to or from the display.
	 * 
	 * @param __form The form to link or unlink.
	 * @param __link If {@code true} then this will link the form, otherwise
	 * it will unlink it.
	 * @since 2022/07/23
	 */
	protected abstract void performLink(FBUIForm __form, boolean __link);
	
	/**
	 * Activates the display, this does nothing if it is already activated.
	 * 
	 * @since 2022/07/23
	 */
	public void activate()
	{
		synchronized (this)
		{
			// Do nothing if this already happened
			if (this._hasActivated)
				return;
			
			// Activate it
			this.performActivate();
			this._hasActivated = true;
		}
	}
	
	/**
	 * Links the form to this display.
	 * 
	 * @param __form The form to link or unlink.
	 * @param __link If {@code true} then this will link the form, otherwise
	 * it will unlink it.
	 * @throws MLECallError If this display cannot link or unlink the given
	 * form.
	 * @since 2022/07/23
	 */
	public void link(FBUIForm __form, boolean __link)
		throws MLECallError
	{
		synchronized (this)
		{
			// Link
			if (__link)
			{
				// {@squirreljme.error EB3y Cannot link a form on a display
				// that is already showing a form.}
				if (this._shownForm != null)
					throw new MLECallError("EB3y");
				
				// Perform link logic
				this.performLink(__form, true);
				
				// Link references
				this._shownForm = __form;
				__form._display = this;
			}
			
			// Unlink
			else
			{
				// {@squirreljme.error EB3x Cannot unlink a form that is not
				// the currently displayed form.}
				if (this._shownForm != __form)
					throw new MLECallError("EB3x");
				
				// Perform link logic
				this.performLink(__form, false);
				
				// Unlink references
				this._shownForm = null;
				__form._display = null;
			}
		}
	}
	
	/**
	 * Indicates that the size was updated on the display.
	 * 
	 * @param __w The new width.
	 * @param __h The new height.
	 * @since 2022/07/23
	 */
	public void updateSize(int __w, int __h)
	{
		synchronized (this)
		{
			// Update
			this._lastWidth = __w;
			this._lastHeight = __h;
			
			// Send resize event to the UI
			Debugging.todoNote("Send new size (%d, %d)", __w, __h);
		}
	}
}
