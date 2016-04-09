// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classprogram;

import net.multiphasicapps.descriptors.ClassNameSymbol;

/**
 * This is a compute machine which does nothing.
 *
 * @since 2016/04/09
 */
class __NullComputeMachine__
	implements CPComputeMachine<Object>
{
	/**
	 * {@inheritDoc}
	 * @since 2016/04/09
	 */
	@Override
	public void allocateObject(Object __pa, int __dest,
		ClassNameSymbol __cl)
	{
	}
}

