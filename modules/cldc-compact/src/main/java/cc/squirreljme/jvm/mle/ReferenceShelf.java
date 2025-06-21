// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle;

import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This provides the interface for references which are used to weakly refer
 * to them, so that they may be collected or act as a cache.
 *
 * @since 2020/05/30
 */
@SquirrelJMEVendorApi
public final class ReferenceShelf
{
	/**
	 * Returns the value this weak reference points to, assuming it is still
	 * valid.
	 *
	 * @param <T> The value to return.
	 * @param __ref The reference.
	 * @return The returned value, will be {@code null} if it has been
	 * garbage collected.
	 * @throws MLECallError On null arguments.
	 * @since 2025/06/21
	 */
	@SquirrelJMEVendorApi
	public native static <T> T weakGet(
		@NotNull Reference<T> __ref)
		throws MLECallError;
	
	/**
	 * Initializes the weak reference.
	 *
	 * @param __ref The reference to initialize.
	 * @param __value The value to point to.
	 * @param __queue The optional queue to reference, may be {@code null}
	 * if no queue is desired to be pushed to.
	 * @throws MLECallError On null arguments, or if the weak reference has
	 * already been initialized.
	 * @since 2025/06/21
	 */
	@SquirrelJMEVendorApi
	public native static void weakInit(
		@NotNull Reference<?> __ref,
		@NotNull Object __value,
		@Nullable ReferenceQueue<?> __queue)
		throws MLECallError;
	
	/**
	 * Returns whether this reference has been enqueued already.
	 *
	 * @param __ref The reference to check.
	 * @return If this was ever enqueued.
	 * @throws MLECallError On null arguments.
	 * @since 2025/06/21
	 */
	@SquirrelJMEVendorApi
	public native static boolean weakIsEnqueued(
		@NotNull Reference<?> __ref)
		throws MLECallError;
	
	/**
	 * Unlinks and clears this reference.
	 *
	 * @param <T> The type of reference this is.
	 * @param __ref The reference to clear.
	 * @return The queue this reference should enqueue to.
	 * @throws MLECallError On null arguments.
	 * @since 2025/06/21
	 */
	@SquirrelJMEVendorApi
	public native static <T> ReferenceQueue<? super T> weakUnlinkAndClear(
		@NotNull Reference<T> __ref)
		throws MLECallError;
}
