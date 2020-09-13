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
import cc.squirreljme.jvm.mle.constants.UIItemProperty;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import javax.swing.JComponent;

/**
 * This is the base call for all of the item types that are implementing on
 * Swing.
 *
 * @since 2020/07/18
 */
public abstract class SwingItem
	implements UIItemBracket
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
	 * Sets the given property.
	 * 
	 * @param __id The {@link UIItemProperty} to set.
	 * @param __newValue The new value to set.
	 * @throws MLECallError If the property is not valid.
	 * @since 2020/09/13
	 */
	public abstract void property(int __id, int __newValue)
		throws MLECallError;
	
	/**
	 * Sets the given property.
	 * 
	 * @param __id The {@link UIItemProperty} to set.
	 * @param __newValue The new value to set.
	 * @throws MLECallError If the property is not valid.
	 * @since 2020/09/13
	 */
	public abstract void property(int __id, String __newValue);
	
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
}
