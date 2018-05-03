// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.javac.syntax.expr;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This is an operation which performs selections on a primary value which is
 * then postfixed accordingly.
 *
 * @since 2018/05/03
 */
public final class PrimarySelectorAndPostfixExpression
	implements ExpressionThreeSyntax
{
	/** The primary. */
	protected final PrimarySyntax primary;
	
	/** The selectors. */
	private final SelectorSyntax[] _selectors;
	
	/** The postfix operations. */
	private final PostfixOperatorType[] _postfixes;
	
	/**
	 * Initializes the primary selection with possible selectors and
	 * postfixing.
	 *
	 * @param __primary The primary expression.
	 * @param __selectors The selectors to perform on it.
	 * @param __postfixes Any postfixes to be done.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/05/03
	 */
	public PrimarySelectorAndPostfixExpression(PrimarySyntax __primary,
		Iterable<SelectorSyntax> __selectors,
		Iterable<PostfixOperatorType> __postfixes)
		throws NullPointerException
	{
		if (__primary == null || __selectors == null || __postfixes == null)
			throw new NullPointerException("NARG");
		
		this.primary = __primary;
		
		// Check selectors
		List<SelectorSyntax> selectors = new ArrayList<>();
		for (SelectorSyntax v : __selectors)
		{
			if (v == null)
				throw new NullPointerException("NARG");
			
			selectors.add(v);
		}
		this._selectors = selectors.<SelectorSyntax>toArray(
			new SelectorSyntax[selectors.size()]);
		
		// Check postfixes
		List<PostfixOperatorType> postfixes = new ArrayList<>();
		for (PostfixOperatorType v : __postfixes)
		{
			if (v == null)
				throw new NullPointerException("NARG");
			
			postfixes.add(v);
		}
		this._postfixes = postfixes.<PostfixOperatorType>toArray(
			new PostfixOperatorType[postfixes.size()]);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/05/03
	 */
	@Override
	public final boolean equals(Object __o)
	{
		if (__o == this)
			return true;
		
		if (!(__o instanceof PrimarySelectorAndPostfixExpression))
			return false;
		
		PrimarySelectorAndPostfixExpression o =
			(PrimarySelectorAndPostfixExpression)__o;
		return this.primary.equals(o.primary) &&
			Arrays.equals(this._selectors, o._selectors) &&
			Arrays.equals(this._postfixes, o._postfixes);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/05/03
	 */
	@Override
	public final int hashCode()
	{
		int hash = 0;
		for (Object v : this._selectors)
			hash ^= v.hashCode();
		for (Object v : this._postfixes)
			hash ^= v.hashCode();
		return this.primary.hashCode() ^ hash;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/05/03
	 */
	@Override
	public final String toString()
	{
		throw new todo.TODO();
	}
}

