// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.emulator.uiform;

import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;

/**
 * Renderer for list items.
 *
 * @since 2020/11/17
 */
@Deprecated
public class ListRenderer
	extends DefaultListCellRenderer
{
	/**
	 * {@inheritDoc}
	 * @since 2020/11/17
	 */
	@Override
	public Component getListCellRendererComponent(
		JList<?> __list, Object __entry, int __index,
		boolean __isSelected, boolean __cellHasFocus)
	{
		ListEntry entry = (ListEntry)__entry;
		JLabel label = (JLabel)super.getListCellRendererComponent(__list,
			__entry, __index, entry._selected, __cellHasFocus);
		
		label.setIcon(entry._icon);
		label.setText(entry.toString());
		label.setEnabled(!entry._disabled);
		
		return label;
	}
}
