// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.rms;

import cc.squirreljme.runtime.cldc.annotation.Api;

/**
 * This is used to filter through records which may exist within an enumerated
 * record set.
 *
 * @see RecordComparator
 * @see RecordEnumeration
 * @since 2017/02/26
 */
@Api
public interface RecordFilter
{
	@Api
	/**
	 * Checks whether the given record is a match for the criteria of this
	 * filter.
	 *
	 * @param __b The record data to check, the array data must not be
	 * modified.
	 * @return {@code true} if the specified record is a match.
	 * @since 2017/02/26
	 */
	boolean matches(byte[] __b);
}

