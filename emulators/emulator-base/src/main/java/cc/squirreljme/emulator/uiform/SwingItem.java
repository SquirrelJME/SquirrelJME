// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.emulator.uiform;

import cc.squirreljme.jvm.mle.brackets.UIItemBracket;
import cc.squirreljme.jvm.mle.constants.UIWidgetProperty;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import javax.swing.JComponent;

/**
 * This is the base call for all of the item types that are implementing on
 * Swing.
 *
 * @since 2020/07/18
 */
public abstract class SwingItem
	implements UIItemBracket, SwingWidget
{
	/** The form the item is on. */
	SwingForm _form;
	
	/** Has this item been deleted? */
	boolean _isDeleted;
	
	/**
	 * Returns the component item.
	 * 
	 * @return The component item.
	 * @since 2020/07/18
	 */
	public abstract JComponent component();
	
	/**
	 * Deletes the item.
	 * 
	 * @since 2020/07/18
	 */
	public abstract void deletePost();
	
	/**
	 * Deletes the given item.
	 * 
	 * @throws MLECallError If it could not deleted.
	 * @since 2020/07/19
	 */
	public final void delete()
		throws MLECallError
	{
		synchronized (this)
		{
			if (this._isDeleted)
				throw new MLECallError("Item already deleted.");
			
			if (this._form != null)
				throw new MLECallError("Item is part of a form.");
			
			// Mark as deleted
			this._isDeleted = true;
			
			// Call post deletion handler for cleanup
			this.deletePost();
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/21
	 */
	@Override
	public int propertyInt(int __intProp)
		throws MLECallError
	{
		switch (__intProp)
		{
				// Form width
			case UIWidgetProperty.INT_WIDTH:
				return this.component().getWidth();
				
				// Form height
			case UIWidgetProperty.INT_HEIGHT:
				return this.component().getHeight();
			
			default:
				throw new MLECallError("Unknown IntProp: " + __intProp);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/21
	 */
	@Override
	public String propertyStr(int __strProp)
		throws MLECallError
	{
		switch (__strProp)
		{
			default:
				throw new MLECallError("Unknown IntProp: " + __strProp);
		}
	}
}
