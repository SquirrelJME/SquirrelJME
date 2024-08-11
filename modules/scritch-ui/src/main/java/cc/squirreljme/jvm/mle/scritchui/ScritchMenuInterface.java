// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.scritchui;

import cc.squirreljme.jvm.mle.constants.NonStandardKey;
import cc.squirreljme.jvm.mle.constants.UIKeyModifier;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchMenuBarBracket;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchMenuBracket;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchMenuHasChildrenBracket;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchMenuHasParentBracket;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchMenuItemBracket;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import org.intellij.lang.annotations.MagicConstant;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

/**
 * Interface for the manipulation of menu bars, menus, and menu items.
 *
 * @since 2024/07/20
 */
public interface ScritchMenuInterface
	extends ScritchApiInterface
{
	/**
	 * Creates a new menu bar.
	 *
	 * @return The newly created menu bar.
	 * @throws MLECallError If the menu bar could not be created.
	 * @since 2024/07/20
	 */
	@SquirrelJMEVendorApi
	@NotNull
	ScritchMenuBarBracket menuBarNew()
		throws MLECallError;
	
	/**
	 * Inserts the given menu item to a parent menu.
	 *
	 * @param __into The menu to place into.
	 * @param __at Where to add the item.
	 * @param __item The item to add.
	 * @throws MLECallError On null arguments; if the index is not valid; or
	 * the item already has a parent.
	 * @since 2024/07/21
	 */
	@SquirrelJMEVendorApi
	void menuInsert(@NotNull ScritchMenuHasChildrenBracket __into,
		@Range(from = 0, to = Integer.MAX_VALUE) int __at,
		@NotNull ScritchMenuHasParentBracket __item)
		throws MLECallError;
	
	/**
	 * Creates a new menu item.
	 *
	 * @return The newly created menu item.
	 * @throws MLECallError If the menu item could not be created.
	 * @since 2024/07/20
	 */
	@SquirrelJMEVendorApi
	@NotNull
	ScritchMenuItemBracket menuItemNew()
		throws MLECallError;
	
	/**
	 * Sets the activation key for the given menu item.
	 *
	 * @param __item The item to set.
	 * @param __key The key to set.
	 * @param __modifier The modifier that must be held.
	 * @throws MLECallError If the item is not valid, or one of the keys
	 * or modifiers are not valid.
	 * @since 2024/07/21
	 */
	@SquirrelJMEVendorApi
	void menuItemSetKey(@NotNull ScritchMenuItemBracket __item,
		@MagicConstant(valuesFromClass = NonStandardKey.class)
			@Range(from = 0, to = 65536) int __key,
		@MagicConstant(flagsFromClass = UIKeyModifier.class)
			int __modifier)
		throws MLECallError;
	
	/**
	 * Creates a new menu.
	 *
	 * @return The newly created menu.
	 * @throws MLECallError If the menu could not be created.
	 * @since 2024/07/21
	 */
	@SquirrelJMEVendorApi
	@NotNull
	ScritchMenuBracket menuNew()
		throws MLECallError;
	
	/**
	 * Removes all items from the menu.
	 *
	 * @param __menuKind The menu to remove from.
	 * @throws MLECallError If the menu is not valid.
	 * @since 2024/07/23
	 */
	@SquirrelJMEVendorApi
	void menuRemoveAll(@NotNull ScritchMenuHasChildrenBracket __menuKind)
		throws MLECallError;
}
