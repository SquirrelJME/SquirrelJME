// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

import dev.shadowtail.classfile.mini.Minimizer;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import net.multiphasicapps.classfile.ClassFile;
import net.multiphasicapps.tac.TestRunnable;
import net.multiphasicapps.tac.UntestableException;

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
		for (String x : new String[]{"ByteDeque.data",
			"InflaterInputStream.data", "__LinkedListListIterator__.data"})
			try (InputStream in = TestClassLoad.class.getResourceAsStream(x);
				ByteArrayOutputStream out = new ByteArrayOutputStream())
			{
				System.err.printf("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@%n");
				System.err.printf("@@@ %s%n", x);
				System.err.printf("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@%n");
				
				Minimizer.minimize(ClassFile.decode(in), out);
				
				System.err.printf("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@%n");
				System.err.println();
			}
	}
}

