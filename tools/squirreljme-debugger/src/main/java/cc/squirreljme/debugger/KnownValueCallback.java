// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.debugger;

/**
 * When a known value has been updated, this will be called accordingly to
 * the interested party.
 *
 * @param <T> The type of value to update.
 * @since 2024/01/26
 */
public interface KnownValueCallback<T>
{
	/**
	 * Indicates that the value has been updated.
	 *
	 * @param __state The state.
	 * @param __value The value which was updated.
	 * @since 2024/01/26
	 */
	void sync(DebuggerState __state, KnownValue<T> __value);
}
