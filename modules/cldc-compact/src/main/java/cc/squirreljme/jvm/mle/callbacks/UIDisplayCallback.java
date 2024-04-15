// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.callbacks;

import cc.squirreljme.jvm.mle.brackets.UIDisplayBracket;
import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import org.jetbrains.annotations.Async;
import org.jetbrains.annotations.NotNull;

/**
 * This callback is used for any calls the display system makes to applications
 * and otherwise.
 *
 * @since 2020/10/03
 */
@SquirrelJMEVendorApi
public interface UIDisplayCallback
	extends ShelfCallback, UIDrawableCallback
{
	/**
	 * This is used to refer to a later invocation, by its ID.
	 *
	 * @param __display The display identifier.
	 * @param __serialId The identity of the serialized call.
	 * @since 2020/10/03
	 */
	@SquirrelJMEVendorApi
	@Async.Execute
	void later(@NotNull UIDisplayBracket __display, int __serialId);
}
