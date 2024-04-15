// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package lang;

import net.multiphasicapps.tac.TestRunnable;

/**
 * This tests that string interning works properly.
 *
 * @since 2019/05/24
 */
@SuppressWarnings({"ConstantConditions", "StringEquality",
	"StringOperationCanBeSimplified", "NewObjectEquality"})
public class TestStringIntern
	extends TestRunnable
{
	/**
	 * {@inheritDoc}
	 * @since 2019/05/24
	 */
	@Override
	public void test()
	{
		this.secondary("cca", "squirrel" == "acorn");
		this.secondary("ccb", "squirrel" == "squirrel");
		
		this.secondary("cna",
			"squirrel" == new String("acorn"));
		this.secondary("cnb",
			"squirrel" == new String("squirrel"));
		
		this.secondary("cia", "squirrel" == "acorn".intern());
		this.secondary("cib", "squirrel" == "squirrel".intern());
		
		this.secondary("cxa",
			"squirrel" == new String("acorn").intern());
		this.secondary("cxb",
			"squirrel" == new String("squirrel").intern());
			
		this.secondary("iia", "squirrel".intern() ==
			new String("acorn").intern());
		this.secondary("iib", "squirrel".intern() ==
			new String("squirrel").intern());
	}
}

