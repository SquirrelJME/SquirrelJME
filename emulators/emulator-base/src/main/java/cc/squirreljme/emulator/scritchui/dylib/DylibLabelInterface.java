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
import cc.squirreljme.jvm.mle.scritchui.ScritchLabelInterface;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchLabelBracket;
import java.lang.ref.Reference;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Dynamic library interface for labels.
 *
 * @since 2024/07/22
 */
public class DylibLabelInterface
	extends DylibBaseInterface
	implements ScritchLabelInterface
{
	/**
	 * Initializes the interface.
	 *
	 * @param __selfApi Reference to our own API.
	 * @param __dyLib The dynamic library interface.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/07/22
	 */
	public DylibLabelInterface(Reference<DylibScritchInterface> __selfApi,
		NativeScritchDylib __dyLib)
	{
		super(__selfApi, __dyLib);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/07/21
	 */
	@Override
	public void setString(@NotNull ScritchLabelBracket __label,
		@Nullable String __string)
		throws MLECallError
	{
		if (__label == null)
			throw new MLECallError("Null arguments.");
		
		NativeScritchDylib.__labelSetString(this.dyLib._stateP,
			((DylibLabelObject)__label).objectPointer(), __string);
	}
}
