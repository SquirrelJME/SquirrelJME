// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.multivm;

import java.io.Serializable;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Results for all of the test runs.
 *
 * @since 2020/09/07
 */
public class VMTestRunResults
	implements Serializable
{
	/** The total number of test runs. */
	public final AtomicInteger totalCount =
		new AtomicInteger();
	
	/** The number of test passes. */
	public final AtomicInteger passCount =
		new AtomicInteger();
	
	/** The number of test failures. */
	public final AtomicInteger failCount =
		new AtomicInteger();
	
	/** The number of test skips. */
	public final AtomicInteger skipCount =
		new AtomicInteger();
	
	/** The tests which failed. */
	public final Set<String> failures =
		new ConcurrentSkipListSet<>();
}
