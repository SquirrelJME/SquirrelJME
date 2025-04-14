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
 * Consumes boolean values.
 *
 * @since 2024/01/29
 */
public interface BooleanConsumer
{
	/**
	 * Accepts the given boolean value.
	 *
	 * @param __value The value to accept.
	 * @since 2024/01/29
	 */
	void accept(boolean __value);
}
