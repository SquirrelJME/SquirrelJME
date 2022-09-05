// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.aot.java;

import cc.squirreljme.jvm.aot.summercoat.SummerCoatBackend;
import cc.squirreljme.jvm.aot.test.BaseCompilation;
import cc.squirreljme.jvm.aot.test.BasicTestPipeline;
import cc.squirreljme.jvm.aot.test.ExampleClass;
import cc.squirreljme.jvm.aot.test.SituationParameters;
import java.io.IOException;

/**
 * Tests that the class parsing pipe gets initialized and ran.
 *
 * @since 2022/08/04
 */
public class TestClassInitPipe
	extends BasicTestPipeline
{
	/**
	 * Initializes the test.
	 *
	 * @since 2022/08/14
	 */
	public TestClassInitPipe()
	{
		super(new SummerCoatBackend());
	}
}
