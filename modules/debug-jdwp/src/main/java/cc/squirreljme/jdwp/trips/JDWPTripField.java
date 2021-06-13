// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp.trips;

import cc.squirreljme.jdwp.JDWPValue;

/**
 * Trip on fields.
 *
 * @since 2021/04/30
 */
public interface JDWPTripField
	extends JDWPTrip
{
	/**
	 * Trip on field access.
	 * 
	 * @param __thread Which thread is this coming from?
	 * @param __type Which type?
	 * @param __fieldDx The field index.
	 * @param __write Is this a write, or a read?
	 * @param __instance The instance of the field, may be {@code null} for
	 * {@code static}.
	 * @param __jVal The value to use for the new value, if this is being
	 * changed to one.
	 * @since 2021/04/30
	 */
	void field(Object __thread, Object __type, int __fieldDx, boolean __write,
		Object __instance, JDWPValue __jVal);
}
