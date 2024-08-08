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
import cc.squirreljme.jvm.mle.scritchui.ScritchScrollPanelInterface;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchScrollPanelBracket;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.lang.ref.Reference;
import org.jetbrains.annotations.NotNull;

/**
 * Interface for scroll panels.
 *
 * @since 2024/07/29
 */
public class DylibScrollPanelInterface
	extends DylibBaseInterface
	implements ScritchScrollPanelInterface
{
	/**
	 * Initializes the interface.
	 *
	 * @param __selfApi Reference to our own API.
	 * @param __dyLib The dynamic library interface.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/07/29
	 */
	public DylibScrollPanelInterface(
		Reference<DylibScritchInterface> __selfApi,
		NativeScritchDylib __dyLib)
	{
		super(__selfApi, __dyLib);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/07/29
	 */
	@Override
	public @NotNull ScritchScrollPanelBracket scrollPanelNew()
		throws MLECallError
	{
		long panelP = NativeScritchDylib.__scrollPanelNew(this.dyLib._stateP);
		if (panelP == 0)
			throw new MLECallError("Could not create scroll panel.");
		
		return new DylibScrollPanelObject(panelP);
	}
}
