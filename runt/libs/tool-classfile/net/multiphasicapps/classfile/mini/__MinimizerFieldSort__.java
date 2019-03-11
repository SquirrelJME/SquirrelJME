// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classfile.mini;

import java.util.Comparator;
import net.multiphasicapps.classfile.Field;
import net.multiphasicapps.classfile.PrimitiveType;

/**
 * This is used to sort the input fields according to their size and type so
 * that they are grouped together and aligned together.
 *
 * Name sorting is used last.
 *
 * @since 2019/03/11
 */
final class __MinimizerFieldSort__
	implements Comparator<Field>
{
	/**
	 * Compares two fields.
	 *
	 * @param __a The first.
	 * @param __b The second.
	 * @since 2019/03/11
	 */
	public int compare(Field __a, Field __b)
	{
		// Compare sizes first to force alignables to be close to each other
		PrimitiveType pa = __a.type().primitiveType(),
			pb = __b.type().primitiveType();
		int sa = (pa == null ? 4 : pa.bytes()),
			sb = (pb == null ? 4 : pb.bytes());
		int rv = sa - sb;
		if (rv != 0)
			return rv;
		
		// Then sort by the descriptor so like-types are close to each other
		rv = __a.type().compareTo(__b.type());
		if (rv != 0)
			return rv;
		
		// Then fallback to name
		return __a.name().compareTo(__b.name());
	}
}

