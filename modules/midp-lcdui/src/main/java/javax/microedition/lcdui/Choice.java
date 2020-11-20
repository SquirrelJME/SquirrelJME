// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.lcdui;

@SuppressWarnings("UnusedReturnValue")
public interface Choice
{
	int EXCLUSIVE =
		1;
	
	int IMPLICIT =
		3;
	
	int MULTIPLE =
		2;
	
	int POPUP =
		4;
	
	int TEXT_WRAP_DEFAULT =
		0;
	
	int TEXT_WRAP_OFF =
		2;
	
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
	int append(String __label, Image __icon)
		throws NullPointerException;
	
	void delete(int __a);
	
	void deleteAll();
	
	int getFitPolicy();
	
	Font getFont(int __a);
	
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
	int getSelectedIndex();
	
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
	void insert(int __at, String __str, Image __icon)
		throws IndexOutOfBoundsException, NullPointerException;
	
	boolean isEnabled(int __i);
	
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
	void set(int __i, String __label, Image __icon)
		throws IndexOutOfBoundsException, NullPointerException;
	
	void setEnabled(int __i, boolean __e);
	
	void setFitPolicy(int __a);
	
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
	void setSelectedFlags(boolean[] __flags)
		throws IllegalArgumentException, NullPointerException;
	
	void setSelectedIndex(int __a, boolean __b);
	
	int size();
}


