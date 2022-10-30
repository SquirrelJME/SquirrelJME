// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot.interpreter;

import cc.squirreljme.jvm.mle.ReflectionShelf;
import cc.squirreljme.jvm.mle.TypeShelf;
import net.multiphasicapps.tac.TestRunnable;

/**
 * Tests that an interpreted abstract class works.
 *
 * @since 2022/09/11
 */
public class TestRunnableAbstractClass
	extends TestRunnable
{
	/**
	 * {@inheritDoc}
	 * @since 2022/09/11
	 */
	@SuppressWarnings("ConstantConditions")
	@Override
	public void test()
		throws Throwable
	{
		// Load the class
		Class<?> interpretedClass = TypeShelf.typeToClass(
			TypeShelf.findType("InterpretedAbstract"));
		
		// Setup new instance
		Object instance = interpretedClass.newInstance();
		
		// Should be this
		this.secondary("is-instance",
			(instance instanceof AbstractClass));
		
		// Cast
		AbstractClass of = (AbstractClass)instance;
		
		// Perform some math operations accordingly...
		this.secondary("abstract", of.abstractMethod(3, 7));
		this.secondary("super", of.superMethod(4, 8));
		this.secondary("final", of.finalMethod(5, 9));
	}
}
