// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.multivm;

import cc.squirreljme.plugin.util.FileLocation;
import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Represents a candidate test to run.
 * 
 * This class is immutable.
 *
 * @since 2020/09/06
 */
public final class CandidateTestFiles
	implements Serializable
{
	/** Source code for the test. */
	public final FileLocation sourceCode;
	
	/** The expected test results. */
	public final FileLocation expectedResult;
	
	/** Expected values. */
	public final Map<String, String> expectedValues;
	
	/** Is this a primary test source? */
	public final boolean primary;
	
	/**
	 * Initializes the test.
	 *
	 * @param __primary Is this a primary test source?
	 * @param __sourceCode The source code for the test.
	 * @param __expectedResult The expected result file.
	 * @throws NullPointerException If {@code __sourceCode} is null.
	 * @since 2020/09/06
	 */
	public CandidateTestFiles(boolean __primary, FileLocation __sourceCode,
		FileLocation __expectedResult)
		throws NullPointerException
	{
		this(__primary, __sourceCode, __expectedResult, null);
	}
	
	/**
	 * Initializes the test.
	 * 
	 * @param __primary Is this a primary test source?
	 * @param __sourceCode The source code for the test.
	 * @param __expectedResult The expected result file.
	 * @param __expectedValues The expected values.
	 * @throws NullPointerException If {@code __sourceCode} is null.
	 * @since 2020/09/06
	 */
	public CandidateTestFiles(boolean __primary, FileLocation __sourceCode,
		FileLocation __expectedResult, Map<String, String> __expectedValues)
		throws NullPointerException
	{
		if (__sourceCode == null)
			throw new NullPointerException("NARG");
		
		this.primary = __primary;
		this.sourceCode = __sourceCode;
		this.expectedResult = __expectedResult;
		this.expectedValues = (__expectedValues == null ||
			__expectedValues.isEmpty() ? Collections.emptyMap() :
			Collections.unmodifiableMap(
				new LinkedHashMap<>(__expectedValues)));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/06
	 */
	@Override
	public final boolean equals(Object __o)
	{
		if (__o == this)
			return true;
		
		if (!(__o instanceof CandidateTestFiles))
			return false;
		
		CandidateTestFiles o = (CandidateTestFiles)__o;
		return this.sourceCode.equals(o.sourceCode) &&
			Objects.equals(this.expectedResult, o.expectedResult);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/06
	 */
	@Override
	public final int hashCode()
	{
		return this.sourceCode.hashCode() ^
			Objects.hashCode(this.expectedResult.hashCode());
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/02/28
	 */
	@Override
	public final String toString()
	{
		return String.format("{sourceCode=%s, expectedResult=%s}",
			this.sourceCode, this.expectedResult);
	}
}
