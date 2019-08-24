// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.io;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * This stores a single rewrite.
 *
 * @since 2019/08/17
 */
final class __Rewrite__
{
	/** The address. */
	final int _address;
	
	/** The type. */
	final __RewriteType__ _type;
	
	/** The value. */
	final __RewriteValue__ _value;
	
	/** The target section to use. */
	final Reference<TableSectionOutputStream.Section> _section;
	
	/**
	 * Initializes the rewrite info.
	 *
	 * @param __addr The address to replace.
	 * @param __type The value type to rewrite.
	 * @param __value The value of the information.
	 * @param __section The section being targeted, may be {@code null}.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/08/17
	 */
	__Rewrite__(int __addr, __RewriteType__ __type, __RewriteValue__ __value,
		TableSectionOutputStream.Section __section)
		throws NullPointerException
	{
		if (__type == null || __value == null)
			throw new NullPointerException("NARG");
		
		this._address = __addr;
		this._type = __type;
		this._value = __value;
		this._section = (__section == null ? null :
			new WeakReference<>(__section));
	}
}

