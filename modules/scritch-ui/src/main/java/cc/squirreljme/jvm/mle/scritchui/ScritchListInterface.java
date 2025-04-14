// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.scritchui;

import cc.squirreljme.jvm.mle.constants.UIListType;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchListBracket;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import org.intellij.lang.annotations.MagicConstant;
import org.jetbrains.annotations.NotNull;

/**
 * Interface for the manipulation of lists.
 *
 * @since 2024/07/16
 */
@SquirrelJMEVendorApi
public interface ScritchListInterface
	extends ScritchApiInterface
{
	/**
	 * Returns a newly created list.
	 *
	 * @param __type The type of list this is.
	 * @return The newly created list.
	 * @throws MLECallError If the type is not valid.
	 * @since 2024/07/16
	 */
	@SquirrelJMEVendorApi
	@NotNull
	ScritchListBracket listNew(
		@MagicConstant(valuesFromClass = UIListType.class) int __type)
		throws MLECallError;
}
