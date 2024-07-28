// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.lcdui;

import cc.squirreljme.runtime.cldc.annotation.Api;
import org.intellij.lang.annotations.MagicConstant;

@Api
@SuppressWarnings("UnusedReturnValue")
public interface Choice
{
	@Api
	int EXCLUSIVE =
		1;
	
	@Api
	int IMPLICIT =
		3;
	
	@Api
	int MULTIPLE =
		2;
	
	@Api
	int POPUP =
		4;
	
	@Api
	int TEXT_WRAP_DEFAULT =
		0;
	
	@Api
	int TEXT_WRAP_OFF =
		2;
	
	@Api
	int TEXT_WRAP_ON =
		1;
	
	/**
	 * Appends the specified item to the list.
	 * 
	 * @param __label The label of item. 
	 * @param __icon The icon of the item.
	 * @return the index of the newly added item.
	 * @throws NullPointerException If no {@code __label} was set.
	 * @since 2020/10/18
	 */
	@Api
	int append(String __label, Image __icon)
		throws NullPointerException;
	
	/**
	 * Deletes the specified item.
	 * 
	 * @param __dx The index to delete.
	 * @throws IndexOutOfBoundsException If the index is not within bounds.
	 * @since 2020/11/21
	 */
	@Api
	void delete(int __dx)
		throws IndexOutOfBoundsException;
	
	@Api
	void deleteAll();
	
	/**
	 * Returns the current list item fitting policy that is in effect, this
	 * might not return the same value passed to {@link #setFitPolicy(int)}
	 * if it is not supported.
	 *
	 * @return One of {@link #TEXT_WRAP_DEFAULT}, {@link #TEXT_WRAP_OFF},
	 * or {@link #TEXT_WRAP_ON}.
	 * @since 2024/07/28
	 */
	@Api
	@MagicConstant(valuesFromClass = Choice.class)
	int getFitPolicy();
	
	@Api
	Font getFont(int __a);
	
	@Api
	Image getImage(int __a);
	
	/**
	 * Gets all of the items which have been selected and stores them into
	 * the boolean array.
	 * 
	 * @param __result The resultant boolean array, if the array is longer
	 * then the extra elements will be set to {@code false}.
	 * @return The number of selected elements.
	 * @throws IllegalArgumentException If the array is shorter than the
	 * number of items in the list.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/10/31
	 */
	@Api
	int getSelectedFlags(boolean[] __result)
		throws IllegalArgumentException, NullPointerException;
	
	/**
	 * Returns the selected element index.
	 * 
	 * For {@link Choice#MULTIPLE} the result will always be {@code -1}.
	 * 
	 * @return The selected index; or {@code -1} if there the list is empty,
	 * no items are selected, or the result cannot be represented as a single
	 * value.
	 * @since 2020/10/31
	 */
	@Api
	int getSelectedIndex();
	
	@Api
	String getString(int __a);
	
	/**
	 * Inserts an item at the given index.
	 * 
	 * @param __at The index to insert at.
	 * @param __str The string of the item.
	 * @param __icon The icon to use for the item.
	 * @throws IndexOutOfBoundsException If the index is not within bounds.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/11/15
	 */
	@Api
	void insert(int __at, String __str, Image __icon)
		throws IndexOutOfBoundsException, NullPointerException;
	
	@Api
	boolean isEnabled(int __i);
	
	@Api
	boolean isSelected(int __a);
	
	/**
	 * Sets the item at the given position.
	 * 
	 * @param __i The index to set.
	 * @param __label The label to use for the item.
	 * @param __icon The icon to use for the item.
	 * @throws IndexOutOfBoundsException If the index is not valid.
	 * @throws NullPointerException If no label was specified.
	 * @since 2020/11/14
	 */
	@Api
	void set(int __i, String __label, Image __icon)
		throws IndexOutOfBoundsException, NullPointerException;
	
	@Api
	void setEnabled(int __atIndex, boolean __enabled);
	
	/**
	 * Sets the fit policy for this item.
	 *
	 * @param __fitPolicy The fit policy to use.
	 * @throws IllegalArgumentException If the fit policy is not valid.
	 * @since 2024/07/28
	 */
	@Api
	void setFitPolicy(
		@MagicConstant(valuesFromClass = Choice.class) int __fitPolicy)
		throws IllegalArgumentException;
	
	@Api
	void setFont(int __a, Font __b);
	
	/**
	 * Sets the selected flags for the choice. For {@link #IMPLICIT} and
	 * {@link #EXCLUSIVE} only a single item will be selected, if no items
	 * were selected then the first item will become selected.
	 * 
	 * @param __flags The flags to set on the list.
	 * @throws IllegalArgumentException If the array is too short.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/11/20
	 */
	@Api
	void setSelectedFlags(boolean[] __flags)
		throws IllegalArgumentException, NullPointerException;
	
	@Api
	void setSelectedIndex(int __atIndex, boolean __selected);
	
	@Api
	int size();
}


