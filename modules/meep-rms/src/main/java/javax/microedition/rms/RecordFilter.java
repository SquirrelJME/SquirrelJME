// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.rms;

/**
 * This is used to filter through records which may exist within an enumerated
 * record set.
 *
 * @see RecordComparator
 * @see RecordEnumeration
 * @since 2017/02/26
 */
public interface RecordFilter
{
	/**
	 * Checks whether the given record is a match for the criteria of this
	 * filter.
	 *
	 * @param __b The record data to check, the array data must not be
	 * modified.
	 * @return {@code true} if the specified record is a match.
	 * @since 2017/02/26
	 */
	public abstract boolean matches(byte[] __b);
}

