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

/**
 * Basic test of the test pipeline.
 *
 * @since 2022/09/05
 */
public abstract class BasicTestPipeline
	extends BaseCompilation
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
	public final void test(SituationParameters __parameters)
		throws Throwable
	{
		byte[] data = this.compileClass(ExampleClass.BLANK);
		
		this.secondary("len", data.length > 0);
	}
}
