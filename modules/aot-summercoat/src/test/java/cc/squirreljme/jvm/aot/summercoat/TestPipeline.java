// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot.summercoat;

import cc.squirreljme.jvm.aot.test.BasicTestPipeline;

/**
 * Performs basic tests of all the pipelines for each variant.
 *
 * @since 2022/09/05
 */
public class TestPipeline
	extends BasicTestPipeline
	implements AllSummerCoatTargetBangs
{
	/**
	 * Initializes the test.
	 *
	 * @since 2022/08/14
	 */
	public TestPipeline()
	{
		super(new SummerCoatBackend());
	}
}
