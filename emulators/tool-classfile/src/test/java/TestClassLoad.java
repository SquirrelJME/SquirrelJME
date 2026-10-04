// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

import java.io.InputStream;
import net.multiphasicapps.classfile.ClassFile;
import net.multiphasicapps.tac.TestRunnable;

/**
 * Tests that classes load properly.
 *
 * @since 2019/03/10
 */
public class TestClassLoad
	extends TestRunnable
{
	/** The classes to compile. */
	public static final String[] CLASS_LIST = new String[]{
		"ByteDeque.data",
		"InflaterInputStream.data",
		"__LinkedListListIterator__.data",
		"AbstractReadableMemory.data"};
	
	/**
	 * {@inheritDoc}
	 * @since 2019/03/10
	 */
	@Override
	public void test()
		throws Throwable
	{
		for (String x : TestClassLoad.CLASS_LIST)
			try (InputStream in = TestClassLoad.class.getResourceAsStream(x))
			{
				ClassFile.decode(in);
			}
	}
}

