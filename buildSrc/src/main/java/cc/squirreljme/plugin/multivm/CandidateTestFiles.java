// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.multivm;

import cc.squirreljme.plugin.util.FileLocation;
import java.util.Objects;

/**
 * Represents a candidate test to run.
 * 
 * This class is immutable.
 *
 * @since 2020/09/06
 */
public final class CandidateTestFiles
{
	/** Source code for the test. */
	public final FileLocation sourceCode;
	
	/** The expected test results. */
	public final FileLocation expectedResult;
	
	/**
	 * Initializes the test.
	 * 
	 * @param __sourceCode The source code for the test.
	 * @param __expectedResult The expected result file.
	 * @throws NullPointerException If {@code __sourceCode} is null.
	 * @since 2020/09/06
	 */
	public CandidateTestFiles(FileLocation __sourceCode,
		FileLocation __expectedResult)
		throws NullPointerException
	{
		if (__sourceCode == null)
			throw new NullPointerException("NARG");
		
		this.sourceCode = __sourceCode;
		this.expectedResult = __expectedResult;
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
