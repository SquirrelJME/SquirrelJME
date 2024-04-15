// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.launcher.ui;

import cc.squirreljme.jvm.launch.Application;
import java.util.Comparator;

/**
 * Compares applications by name to sort them in the application list.
 * 
 * @since 2022/01/28
 */
final class __ApplicationComparator__
	implements Comparator<Application>
{
	/**
	 * {@inheritDoc}
	 * @since 2022/01/28
	 */
	@Override
	public int compare(Application __a, Application __b)
	{
		return __a.displayName().compareToIgnoreCase(__b.displayName());
	}
}
