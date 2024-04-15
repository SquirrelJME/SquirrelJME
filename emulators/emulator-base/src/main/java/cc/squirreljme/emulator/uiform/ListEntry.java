// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.emulator.uiform;

import javax.swing.ImageIcon;

/**
 * Represents an entry within a list.
 *
 * @since 2020/10/31
 */
public final class ListEntry
{
	/** The dimension of the icon. */
	int _iconDimension;
	
	/** Is this item selected? */
	boolean _selected;
	
	/** Is this item disabled? */
	boolean _disabled;
	
	/** The description of the font. */
	int _fontDescription;
	
	/** The ID code of this entry. */
	int _idCode;
	
	/** The label of the item. */
	String _label;
	
	/** The icon to use for the item. */
	ImageIcon _icon;
	
	/** Is the icon dirty? */
	boolean _dirtyIcon;
	
	/**
	 * {@inheritDoc}
	 * @since 2020/10/31
	 */
	@Override
	public String toString()
	{
		String label = this._label;
		return (label == null ? "" : label);
	}
}
