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
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchLabelBracket;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Interface for labeled items.
 *
 * @since 2024/07/22
 */
@SquirrelJMEVendorApi
public interface ScritchLabelInterface
	extends ScritchApiInterface
{
	/**
	 * Sets the string of the given label.
	 *
	 * @param __label The label to set the string of.
	 * @param __string The string to set, may be {@code null} to clear it.
	 * @throws MLECallError If the label is not valid.
	 * @since 2024/07/21
	 */
	@SquirrelJMEVendorApi
	void setString(@NotNull ScritchLabelBracket __label,
		@Nullable String __string)
		throws MLECallError;
}
