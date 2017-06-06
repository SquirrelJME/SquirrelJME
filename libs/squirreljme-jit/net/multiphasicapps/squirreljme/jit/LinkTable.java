// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit;

import java.util.Map;
import net.multiphasicapps.util.sorted.SortedTreeMap;

/**
 * This is the link table which contains every exported and imported class,
 * field, method, and resource.
 *
 * This class is not thread safe.
 *
 * @since 2017/05/29
 */
public class LinkTable
{
	/** Clusters which are available for resource usage. */
	private final Map<ClusterIdentifier, ResourceCluster> _clusters =
		new SortedTreeMap<>();
}

