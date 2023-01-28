// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.constants;

import cc.squirreljme.runtime.cldc.annotation.Exported;

/**
 * The type of list to use.
 *
 * @since 2020/10/17
 */
@Exported
public interface UIListType
{
	/** Only one element may be selected at a time. */
	@Exported
	byte EXCLUSIVE =
		0;
	
	/**
	 * The item that is focused is always the only one selected, pressing an
	 * action key (like enter/space) will activate the item.
	 */
	@Exported
	byte IMPLICIT =
		1;
	
	/** Any number of items may be selected. */
	@Exported
	byte MULTIPLE =
		2;
	
	/** The number of list types. */
	@Exported
	byte NUM_LIST_TYPES =
		3;
}
