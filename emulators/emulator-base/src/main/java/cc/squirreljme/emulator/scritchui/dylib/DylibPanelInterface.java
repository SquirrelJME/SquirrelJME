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
import cc.squirreljme.jvm.mle.scritchui.ScritchPanelInterface;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchPanelBracket;
import cc.squirreljme.jvm.mle.scritchui.callbacks.ScritchInputListener;
import java.lang.ref.Reference;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Not Described.
 *
 * @since 2024/04/02
 */
public class DylibPanelInterface
	extends DylibBaseInterface
	implements ScritchPanelInterface
{
	/**
	 * Initializes the interface.
	 *
	 * @param __selfApi Reference to our own API.
	 * @param __dyLib The dynamic library interface.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/04/02
	 */
	public DylibPanelInterface(Reference<DylibScritchInterface> __selfApi,
		NativeScritchDylib __dyLib)
		throws NullPointerException
	{
		super(__selfApi, __dyLib);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/04/02
	 */
	@Override
	public void enableFocus(ScritchPanelBracket __panel,
		boolean __enabled, boolean __default)
		throws MLECallError
	{
		if (__panel == null)
			throw new MLECallError("NARG");
		
		if ((DylibPanelObject)__panel == null)
			throw new MLECallError("NARG");
		
		NativeScritchDylib.__panelEnableFocus(this.dyLib._stateP,
			((DylibPanelObject)__panel).objectP, __enabled, __default);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/04/02
	 */
	@Override
	public ScritchPanelBracket newPanel()
	{
		long panelP = NativeScritchDylib.__panelNew(this.dyLib._stateP);
		if (panelP == 0)
			throw new MLECallError("Could not create panel.");
		
		return new DylibPanelObject(panelP);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/06/30
	 */
	@Override
	public void setInputListener(@NotNull ScritchPanelBracket __component,
		@Nullable ScritchInputListener __listener)
		throws MLECallError
	{
		if (__component == null)
			throw new MLECallError("Null arguments.");
		
		if ((DylibComponentObject)__component == null)
			throw new MLECallError("Null arguments.");
		
		NativeScritchDylib.__componentSetInputListener(this.dyLib._stateP,
			((DylibComponentObject)__component).objectP, __listener);
	}
}
