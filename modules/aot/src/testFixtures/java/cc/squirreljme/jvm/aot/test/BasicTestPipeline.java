// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot.test;

import cc.squirreljme.jvm.aot.Backend;
import cc.squirreljme.jvm.aot.CompilationStatistic;
import cc.squirreljme.jvm.aot.CompilationStatistics;

/**
 * Basic test of the test pipeline.
 *
 * @since 2022/09/05
 */
public abstract class BasicTestPipeline
	extends BaseCompilation
	implements AllExampleClasses
{
	/**
	 * Initializes the base compilation.
	 *
	 * @param __backend The backend to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/09/05
	 */
	public BasicTestPipeline(Backend __backend)
		throws NullPointerException
	{
		super(__backend);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/09/05
	 */
	@Override
	public final void test(SituationParameters __situation,
		String __argument)
		throws Throwable
	{
		ExampleClass example = ExampleClass.valueOf(__argument);
		
		// Compile this class
		this.compileClass(example);
		
		// Check common statistics
		CompilationStatistics statistics = __situation.linkGlob.statistics();
		this.secondary("classes-compiled",
			statistics.getValue(CompilationStatistic.CLASSES_COMPILED));
	}
}
