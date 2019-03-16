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

import java.util.ArrayList;
import java.util.List;

/**
 * Stores temporary field data as it is processed.
 *
 * @since 2019/03/11
 */
final class __TempFields__
{
	/** The fields in the table. */
	final List<MinimizedField> _fields =
		new ArrayList<>();
	
	/** The number of fields in the table. */
	int _count;
	
	/** The current byte size of the field table. */
	int _bytes;
}

