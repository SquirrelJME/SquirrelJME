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
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchComponentBracket;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchContainerBracket;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchPanelBracket;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchWindowBracket;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import org.jetbrains.annotations.NotNull;

/**
 * Interface for generic containers.
 *
 * @since 2024/03/16
 */
@SquirrelJMEVendorApi
public interface ScritchContainerInterface
{
	/**
	 * Removes all items from the container.
	 *
	 * @param __container The container to remove from.
	 * @throws MLECallError On null arguments.
	 * @since 2024/03/17
	 */
	@SquirrelJMEVendorApi
	void removeAll(@NotNull ScritchContainerBracket __container)
		throws MLECallError;
	
	/**
	 * Adds the given component to the container.
	 *
	 * @param __container The container to add to.
	 * @param __component The container to add.
	 * @throws MLECallError On null arguments.
	 * @since 2024/03/17
	 */
	@SquirrelJMEVendorApi
	void add(@NotNull ScritchContainerBracket __container,
		@NotNull ScritchComponentBracket __component)
		throws MLECallError;
	
	/**
	 * Adds the given component to the container, with layout information.
	 *
	 * @param __container The container to add to.
	 * @param __component The container to add.
	 * @param __layoutInfo The integer based layout information, which will
	 * vary depending on the used layout.
	 * @throws MLECallError On null arguments.
	 * @since 2024/03/17
	 */
	@SquirrelJMEVendorApi
	void add(@NotNull ScritchContainerBracket __container,
		@NotNull ScritchComponentBracket __component, int __layoutInfo)
		throws MLECallError;
}
