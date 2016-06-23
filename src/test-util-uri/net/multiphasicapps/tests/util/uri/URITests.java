// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.tests.util.uri;

import net.multiphasicapps.tests.InvalidTestException;
import net.multiphasicapps.tests.TestChecker;
import net.multiphasicapps.tests.TestInvoker;
import net.multiphasicapps.util.uri.URI;

/**
 * This tests that the URI library operates correctly.
 *
 * @since 2016/06/23
 */
public class URITests
	implements TestInvoker
{
	/**
	 * {@inheritDoc}
	 * @since 2016/03/28
	 */
	@Override
	public String invokerName()
	{
		return "net.multiphasicapps.util.uri.URI";
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/05
	 */
	@Override
	public Iterable<String> invokerTests()
	{
		// After URIs are implemented, the test system will switch to URIs
		return Arrays.<String>asList("general");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/28
	 */
	@Override
	public void runTest(TestChecker __tc, String __st)
		throws NullPointerException, Throwable
	{
		// Check
		if (__tc == null || __st == null)
			throw new NullPointerException();
		
		// These tests are to make sure that URIs operate properly for the
		// most part. When URI support is complete, all tests will transition
		// to using URIs instead of Strings.
	}
}

