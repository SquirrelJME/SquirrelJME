// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.util;

import cc.squirreljme.plugin.multivm.VMTestFrameworkTestClassProcessor;
import java.util.concurrent.atomic.AtomicReference;
import org.gradle.api.internal.tasks.testing.DefaultTestOutputEvent;
import org.gradle.api.internal.tasks.testing.TestResultProcessor;
import org.gradle.api.tasks.testing.TestOutputEvent;

/**
 * Pushes lines to result output.
 *
 * @since 2022/09/11
 */
public class TestResultOutputStream
	extends LinePushOutputStream
{
	/** Line ending character. */
	public static final String LINE_ENDING =
		System.getProperty("line.separator");
	
	/** The destination output. */
	protected final TestOutputEvent.Destination destination;
	
	/** The test ID. */
	protected final Object id;
	
	/** The processor for test results. */
	protected final AtomicReference<TestResultProcessor> processor;
	
	/**
	 * Initializes the output stream for test result output.
	 * 
	 * @param __processor The processor for test results.
	 * @param __id The test ID.
	 * @param __destination The processor for test results.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/09/11
	 */
	public TestResultOutputStream(
		AtomicReference<TestResultProcessor> __processor, Object __id,
		TestOutputEvent.Destination __destination)
		throws NullPointerException
	{
		if (__processor == null || __id == null || __destination == null)
			throw new NullPointerException("NARG");
		
		this.processor = __processor;
		this.id = __id;
		this.destination = __destination;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/09/11
	 */
	@Override
	protected void push(String __string)
	{
		Object id = this.id;
		TestOutputEvent.Destination destination = this.destination;
		
		// Output string and the line ending
		VMTestFrameworkTestClassProcessor.resultAction(this.processor,
			(__rp) -> __rp.output(id, new DefaultTestOutputEvent(destination,
					__string + TestResultOutputStream.LINE_ENDING)));
	}
}
