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
import cc.squirreljme.jvm.aot.test.ExampleClass;
import java.io.IOException;

/**
 * Tests that the class parsing pipe gets initialized and ran.
 *
 * @since 2022/08/04
 */
public class TestClassInitPipe
	extends BaseCompilation
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
	
	/**
	 * {@inheritDoc}
	 * @since 2022/08/04
	 */
	@Override
	public void test()
		throws IOException
	{
		byte[] data = this.compileClass(ExampleClass.BLANK);
		
		this.secondary("len", data.length > 0);
	}
}
