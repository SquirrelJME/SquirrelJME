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
import cc.squirreljme.jvm.mle.scritchui.callbacks.ScritchActivateListener;
import cc.squirreljme.jvm.mle.scritchui.callbacks.ScritchSizeListener;
import cc.squirreljme.jvm.mle.scritchui.callbacks.ScritchValueUpdateListener;
import cc.squirreljme.jvm.mle.scritchui.callbacks.ScritchVisibleListener;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

/**
 * Generic interface for ScritchUI components.
 *
 * @since 2024/03/16
 */
@SquirrelJMEVendorApi
public interface ScritchComponentInterface
	extends ScritchApiInterface
{
	/**
	 * Returns the parent of the given component.
	 *
	 * @param __component The component to get the parent of.
	 * @return The parent of this component or {@code null}.
	 * @throws MLECallError On null arguments.
	 * @since 2024/07/29
	 */
	@SquirrelJMEVendorApi
	@Nullable
	ScritchComponentBracket componentGetParent(
		@NotNull ScritchComponentBracket __component)
		throws MLECallError;
	
	/**
	 * Returns the height of the component.
	 *
	 * @param __component The component to access.
	 * @return The height of the component.
	 * @throws MLECallError On null arguments.
	 * @since 2024/03/18
	 */
	@SquirrelJMEVendorApi
	@Range(from = 0, to = Integer.MAX_VALUE)
	int componentGetHeight(@NotNull ScritchComponentBracket __component)
		throws MLECallError;
	
	/**
	 * Revalidates the given component.
	 *
	 * @param __component The component to revalidate.
	 * @throws MLECallError On null arguments.
	 * @since 2024/03/17
	 */
	@SquirrelJMEVendorApi
	void componentRevalidate(@NotNull ScritchComponentBracket __component)
		throws MLECallError;
	
	/**
	 * Sets the listener to be called when a component is activated.
	 *
	 * @param __component The component to set the listener to.
	 * @param __listener The listener to use, may be {@code null} to clear.
	 * @throws MLECallError On null arguments or the component is not valid.
	 * @since 2024/07/17
	 */
	@SquirrelJMEVendorApi
	void componentSetActivateListener(ScritchComponentBracket __component,
		ScritchActivateListener __listener)
		throws MLECallError;
	
	/**
	 * Sets the listener to be called when the size of the component changes.
	 *
	 * @param __component The component to set the listener to.
	 * @param __listener The listener to use, may be {@code null} to clear.
	 * @throws MLECallError On null arguments or the component is not valid.
	 * @since 2024/04/28
	 */
	@SquirrelJMEVendorApi
	void componentSetSizeListener(ScritchComponentBracket __component,
		ScritchSizeListener __listener)
		throws MLECallError;
	
	/**
	 * Sets the listener to be called when a component value has changed.
	 *
	 * @param __component The component to set the listener to.
	 * @param __listener The listener to use, may be {@code null} to clear.
	 * @throws MLECallError On null arguments or the component is not valid.
	 * @since 2024/07/17
	 */
	@SquirrelJMEVendorApi
	void componentSetValueUpdateListener(ScritchComponentBracket __component,
		ScritchValueUpdateListener __listener)
		throws MLECallError;
	
	/**
	 * Sets the visibility listener to use for the widget.
	 *
	 * @param __component The component to set the listener for.
	 * @param __listener The listener to set or clear.
	 * @throws MLECallError If the component is not valid.
	 * @since 2024/06/28
	 */
	@SquirrelJMEVendorApi
	void componentSetVisibleListener(ScritchComponentBracket __component,
		ScritchVisibleListener __listener)
		throws MLECallError;
	
	/**
	 * Returns the width of the component.
	 *
	 * @param __component The component to access.
	 * @return The width of the component.
	 * @throws MLECallError On null arguments.
	 * @since 2024/03/18
	 */
	@SquirrelJMEVendorApi
	@Range(from = 0, to = Integer.MAX_VALUE)
	int componentWidth(@NotNull ScritchComponentBracket __component)
		throws MLECallError;
}
