// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.scritchui;

import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchBaseBracket;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchMenuBarBracket;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import org.jetbrains.annotations.NotNull;

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
	 * Creates a new menu item.
	 *
	 * @return The newly created menu item.
	 * @throws MLECallError If the menu item could not be created.
	 * @since 2024/07/21
	 */
	@SquirrelJMEVendorApi
	@NotNull
	ScritchBaseBracket menuItemNew()
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
	ScritchBaseBracket menuNew()
		throws MLECallError;
}
