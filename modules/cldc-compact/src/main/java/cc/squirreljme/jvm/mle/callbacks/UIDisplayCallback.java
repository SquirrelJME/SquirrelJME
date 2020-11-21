// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.callbacks;

/**
 * This callback is used for any calls the display system makes to applications
 * and otherwise.
 *
 * @since 2020/10/03
 */
public interface UIDisplayCallback
	extends ShelfCallback
{
	/**
	 * This is used to refer to a later invocation, by its ID.
	 * 
	 * @param __displayId The display identifier.
	 * @param __serialId The identity of the serialized call.
	 * @since 2020/10/03
	 */
	void later(int __displayId, int __serialId);
}
