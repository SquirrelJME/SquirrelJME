// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.doclet;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.util.Comparator;

/**
 * Compares processed classes based on their name.
 *
 * @since 2022/08/24
 */
public final class ProcessedClassComparator
	implements Comparator<ProcessedClass>
{
	/**
	 * {@inheritDoc}
	 * @since 2022/08/24
	 */
	@Override
	public int compare(ProcessedClass __a, ProcessedClass __b)
	{
		return __a.name.compareTo(__b.name);
	}
}
