// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.constants;

/**
 * The type of list to use.
 *
 * @since 2020/10/17
 */
public interface UIListType
{
	/** Only one element may be selected at a time. */
	byte EXCLUSIVE =
		0;
	
	/** The item that is focused is always the only one selected. */
	byte IMPLICIT =
		1;
	
	/** Any number of items may be selected. */
	byte MULTIPLE =
		2;
	
	/** The number of list types. */
	byte NUM_LIST_TYPES =
		3;
}
