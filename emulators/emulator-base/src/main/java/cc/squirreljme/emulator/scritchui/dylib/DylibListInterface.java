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
import cc.squirreljme.jvm.mle.scritchui.ScritchListInterface;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchListBracket;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.lang.ref.Reference;
import org.jetbrains.annotations.NotNull;

/**
 * Dynamic library interface for ScritchUI lists.
 *
 * @since 2024/07/16
 */
public class DylibListInterface
	extends DylibBaseInterface
	implements ScritchListInterface
{
	/**
	 * Initializes the interface.
	 *
	 * @param __selfApi Reference to our own API.
	 * @param __dyLib The dynamic library interface.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/07/16
	 */
	public DylibListInterface(Reference<DylibScritchInterface> __selfApi,
		NativeScritchDylib __dyLib)
	{
		super(__selfApi, __dyLib);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/07/16
	 */
	@Override
	public ScritchListBracket listNew()
		throws MLECallError
	{
		throw Debugging.todo();
	}
}
