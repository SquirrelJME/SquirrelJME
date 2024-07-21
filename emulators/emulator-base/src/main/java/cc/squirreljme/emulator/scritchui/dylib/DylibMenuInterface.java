// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.emulator.scritchui.dylib;

import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.jvm.mle.scritchui.ScritchMenuInterface;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchMenuBarBracket;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchMenuBracket;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchMenuHasChildrenBracket;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchMenuHasLabelBracket;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchMenuHasParentBracket;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchMenuItemBracket;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.lang.ref.Reference;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

/**
 * Dynamic library for ScritchUI menus.
 *
 * @since 2024/07/20
 */
public class DylibMenuInterface
	extends DylibBaseInterface
	implements ScritchMenuInterface
{
	/**
	 * Initializes the interface.
	 *
	 * @param __selfApi Reference to our own API.
	 * @param __dyLib The dynamic library interface.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/07/20
	 */
	public DylibMenuInterface(Reference<DylibScritchInterface> __selfApi,
		NativeScritchDylib __dyLib)
	{
		super(__selfApi, __dyLib);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/07/20
	 */
	@Override
	public ScritchMenuBarBracket menuBarNew()
		throws MLECallError
	{
		long menuBarP = NativeScritchDylib.__menuBarNew(this.dyLib._stateP);
		if (menuBarP == 0)
			throw new MLECallError("Menu bar not created?");
		
		return new DylibMenuBarObject(menuBarP);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/07/21
	 */
	@Override
	public void menuInsert(@NotNull ScritchMenuHasChildrenBracket __into,
		@Range(from = 0, to = Integer.MAX_VALUE) int __at,
		@NotNull ScritchMenuHasParentBracket __item)
		throws MLECallError
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/07/21
	 */
	@Override
	public ScritchMenuItemBracket menuItemNew()
		throws MLECallError
	{
		long menuItemP = NativeScritchDylib.__menuItemNew(this.dyLib._stateP);
		if (menuItemP == 0)
			throw new MLECallError("Menu item not created?");
		
		return new DylibMenuItemObject(menuItemP);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/07/21
	 */
	@Override
	public void menuItemSetKey(@NotNull ScritchMenuItemBracket __item,
		@Range(from = 0, to = 65536) int __key, int __modifier)
		throws MLECallError
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/07/21
	 */
	@Override
	public ScritchMenuBracket menuNew()
		throws MLECallError
	{
		long menuP = NativeScritchDylib.__menuNew(this.dyLib._stateP);
		if (menuP == 0)
			throw new MLECallError("Menu not created?");
		
		return new DylibMenuObject(menuP);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/07/21
	 */
	@Override
	public void menuSetLabel(ScritchMenuHasLabelBracket __item,
		String __label)
		throws MLECallError
	{
		throw Debugging.todo();
	}
}
