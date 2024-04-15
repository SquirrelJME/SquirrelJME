// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package squirreljme.mle;

import cc.squirreljme.jvm.mle.ObjectShelf;
import net.multiphasicapps.tac.TestRunnable;

public class TestArrayLength
	extends TestRunnable
{
	/**
	 * {@inheritDoc}
	 * @since 2020/02/23
	 */
	@Override
	public final void test()
	{
		Object z = new boolean[1],
			b = new byte[2],
			s = new short[3],
			c = new char[4],
			i = new int[5],
			j = new long[6],
			f = new float[7],
			d = new double[8],
			l = new Object[9],
			o = new Object();
		
		// As objects
		this.secondary("z", ObjectShelf.arrayLength(z));
		this.secondary("b", ObjectShelf.arrayLength(b));
		this.secondary("s", ObjectShelf.arrayLength(s));
		this.secondary("c", ObjectShelf.arrayLength(c));
		this.secondary("i", ObjectShelf.arrayLength(i));
		this.secondary("j", ObjectShelf.arrayLength(j));
		this.secondary("f", ObjectShelf.arrayLength(f));
		this.secondary("d", ObjectShelf.arrayLength(d));
		this.secondary("l", ObjectShelf.arrayLength(l));
		this.secondary("o", ObjectShelf.arrayLength(o));
	}
}
