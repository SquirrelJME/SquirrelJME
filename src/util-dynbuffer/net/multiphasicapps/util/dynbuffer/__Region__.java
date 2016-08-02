// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.util.dynbuffer;

import java.util.ArrayList;
import java.util.List;

/**
 * This is a region which contains multiple data partitions.
 *
 * @since 2016/08/02
 */
final class __Region__
{
	/** Physical partitions in this region. */
	final List<__Partition__> _physical =
		new ArrayList<>();
}

