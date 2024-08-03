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
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

/**
 * Interface for generic containers.
 *
 * @since 2024/03/16
 */
@SquirrelJMEVendorApi
public interface ScritchContainerInterface
	extends ScritchApiInterface
{
	/**
	 * Adds the given component to the container.
	 *
	 * @param __container The container to add to.
	 * @param __component The container to add.
	 * @throws MLECallError On null arguments.
	 * @since 2024/03/17
	 */
	@SquirrelJMEVendorApi
	void containerAdd(@NotNull ScritchContainerBracket __container,
		@NotNull ScritchComponentBracket __component)
		throws MLECallError;
	
	/**
	 * Removes all items from the container.
	 *
	 * @param __container The container to remove from.
	 * @throws MLECallError On null arguments.
	 * @since 2024/03/17
	 */
	@SquirrelJMEVendorApi
	void containerRemoveAll(@NotNull ScritchContainerBracket __container)
		throws MLECallError;
	
	/**
	 * Sets the bounds of the given component in the container.
	 *
	 * @param __container The container to set within.
	 * @param __component The component to change the size and position of.
	 * @param __x The X position.
	 * @param __y The Y position.
	 * @param __w The width.
	 * @param __h The height.
	 * @throws MLECallError On null arguments or the size and/or position
	 * are not valid.
	 * @since 2024/03/26
	 */
	@SquirrelJMEVendorApi
	void containerSetBounds(@NotNull ScritchContainerBracket __container,
		@NotNull ScritchComponentBracket __component,
		int __x, int __y,
		@Range(from = 0, to = Integer.MAX_VALUE) int __w,
		@Range(from = 0, to = Integer.MAX_VALUE) int __h)
		throws MLECallError;
}
