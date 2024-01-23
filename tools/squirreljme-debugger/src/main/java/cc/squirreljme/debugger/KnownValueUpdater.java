// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.debugger;

import java.util.function.BiFunction;

/**
 * Updater for known values.
 *
 * @param <T> The value type.
 * @since 2024/01/22
 */
public interface KnownValueUpdater<T>
{
	/**
	 * Updates the given value.
	 *
	 * @param __state The state.
	 * @param __value The value to update.\
	 * @since 2024/01/22
	 */
	void update(DebuggerState __state, KnownValue<T> __value);
}
