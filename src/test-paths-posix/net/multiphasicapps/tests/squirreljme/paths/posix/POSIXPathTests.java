// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.tests.squirreljme.paths.posix;

import java.util.Objects;
import net.multiphasicapps.squirreljme.paths.posix.PosixPath;
import net.multiphasicapps.squirreljme.paths.posix.PosixPaths;
import net.multiphasicapps.tests.IndividualTest;
import net.multiphasicapps.tests.InvalidTestException;
import net.multiphasicapps.tests.TestComparison;
import net.multiphasicapps.tests.TestGroupName;
import net.multiphasicapps.tests.TestInvoker;
import net.multiphasicapps.tests.TestFamily;
import net.multiphasicapps.tests.TestFragmentName;

/**
 * This class is used to test that POSIX paths work and operate correctly.
 *
 * @since 2016/08/21
 */
public class POSIXPathTests
	implements TestInvoker
{
	/**
	 * {@inheritDoc}
	 * @since 2016/08/21
	 */
	@Override
	public TestFamily testFamily()
	{
		return new TestFamily(
			"net.multiphasicapps.squirreljme.paths.posix",
			"all");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/08/21
	 */
	@Override
	public void runTest(IndividualTest __t)
		throws NullPointerException, Throwable
	{
		// Check
		if (__t == null)
			throw new NullPointerException("NARG");
		
		// Path system to use
		PosixPaths ink = PosixPaths.instance();
		
		// Single root
		__t.result("singleroot").compareObject(TestComparison.EQUALS,
			Objects.toString(ink.get("/")), "/");
		
		// Double root
		__t.result("doubleroot").compareObject(TestComparison.EQUALS,
			Objects.toString(ink.get("//")), "//");
		
		// Condense triple to single
		__t.result("condenseroot3").compareObject(TestComparison.EQUALS,
			Objects.toString(ink.get("///")), "/");
		
		// Condense quadruple to single
		__t.result("condenseroot4").compareObject(TestComparison.EQUALS,
			Objects.toString(ink.get("////")), "/");
		
		// Condense multiple fragments to single
		__t.result("condenseroot16").compareObject(TestComparison.EQUALS,
			Objects.toString(ink.get("////", "////", "////", "////")), "/");
	}
}

