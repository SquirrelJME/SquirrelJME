// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import net.multiphasicapps.classfile.ClassFile;
import net.multiphasicapps.classfile.mini.Minimizer;
import net.multiphasicapps.tac.TestRunnable;

/**
 * Tests that minimizing is performed properly.
 *
 * @since 2019/03/10
 */
public class TestMinimizer
	extends TestRunnable
{
	/**
	 * {@inheritDoc}
	 * @since 2019/03/10
	 */
	@Override
	public void test()
		throws Throwable
	{
		for (String x : new String[]{"ByteDeque.class",
			"InflaterInputStream.class"})
			try (InputStream in = TestClassLoad.class.getResourceAsStream(x);
				ByteArrayOutputStream out = new ByteArrayOutputStream())
			{
				Minimizer.minimize(ClassFile.decode(in), out);
			}
	}
}

