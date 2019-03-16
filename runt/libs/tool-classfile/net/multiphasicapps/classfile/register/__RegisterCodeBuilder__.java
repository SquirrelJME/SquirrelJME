// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classfile.register;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * This is used to build {@link RegisterCode} and add instructions to it.
 *
 * @since 2019/03/16
 */
final class __RegisterCodeBuilder__
{
	/** Temporary instruction layout. */
	final Map<Integer, __TempInstruction__> _instructions =
		new LinkedHashMap<>();
	
	/** Next address to use. */
	int _nextaddr;
}

