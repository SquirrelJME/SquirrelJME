// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.emulator.scritchui.dylib;

import cc.squirreljme.emulator.MLECallWouldFail;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.jvm.mle.scritchui.ScritchMenuInterface;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchMenuBarBracket;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.lang.ref.Reference;
import org.jetbrains.annotations.NotNull;

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
}
