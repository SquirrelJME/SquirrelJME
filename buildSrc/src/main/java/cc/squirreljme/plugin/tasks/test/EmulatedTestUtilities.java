// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.tasks.test;

import org.gradle.api.internal.tasks.testing.DefaultTestOutputEvent;
import org.gradle.api.internal.tasks.testing.TestCompleteEvent;
import org.gradle.api.internal.tasks.testing.TestDescriptorInternal;
import org.gradle.api.internal.tasks.testing.TestStartEvent;
import org.gradle.api.tasks.testing.TestOutputEvent;
import org.gradle.api.tasks.testing.TestResult;

/**
 * Utilities for tests.
 *
 * @since 2020/03/06
 */
public final class EmulatedTestUtilities
{
	/**
	 * Not used.
	 *
	 * @since 2020/03/06
	 */
	private EmulatedTestUtilities()
	{
	}
	
	/**
	 * Returns a failing completion.
	 *
	 * @return An event.
	 * @since 2020/03/06
	 */
	public static TestCompleteEvent failNow()
	{
		return new TestCompleteEvent(System.currentTimeMillis(),
			TestResult.ResultType.FAILURE);
	}
	
	/**
	 * Returns a passing completion.
	 *
	 * @return An event.
	 * @since 2020/03/06
	 */
	public static TestCompleteEvent passNow()
	{
		return new TestCompleteEvent(System.currentTimeMillis(),
			TestResult.ResultType.SUCCESS);
	}
	
	/**
	 * Returns a skipping completion.
	 *
	 * @return An event.
	 * @since 2020/03/06
	 */
	public static TestCompleteEvent skipNow()
	{
		return new TestCompleteEvent(System.currentTimeMillis(),
			TestResult.ResultType.SKIPPED);
	}
	
	/**
	 * Returns a test starting now.
	 *
	 * @param __test The test information.
	 * @return An event
	 * @since 2020/03/06
	 */
	public static TestStartEvent startNow(TestDescriptorInternal __test)
	{
		return new TestStartEvent(System.currentTimeMillis(),
			(__test == null ? null : __test.getId()));
	}
	
	/**
	 * Outputs a message.
	 *
	 * @param __dest The destination.
	 * @param __msg The message.
	 * @return The message event.
	 * @since 2020/03/06
	 */
	public static TestOutputEvent output(TestOutputEvent.Destination __dest,
		String __msg)
	{
		return new DefaultTestOutputEvent(__dest, __msg);
	}
	
	/**
	 * Outputs a message.
	 *
	 * @param __msg The message.
	 * @return The message event.
	 * @since 2020/03/06
	 */
	public static TestOutputEvent output(String __msg)
	{
		return EmulatedTestUtilities.output(
			TestOutputEvent.Destination.StdOut, __msg);
	}
	
	/**
	 * Outputs a message.
	 *
	 * @param __msg The message.
	 * @return The message event.
	 * @since 2020/03/06
	 */
	public static TestOutputEvent outputErr(String __msg)
	{
		return EmulatedTestUtilities.output(
			TestOutputEvent.Destination.StdErr, __msg);
	}
}
