// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.javac.syntax;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import net.multiphasicapps.classfile.MethodName;
import net.multiphasicapps.javac.token.BufferedTokenSource;
import net.multiphasicapps.javac.token.Token;
import net.multiphasicapps.javac.token.TokenType;

/**
 * This represents a simple method.
 *
 * @since 2018/04/28
 */
public final class SimpleMethodSyntax
	implements MapView, MethodSyntax
{
	/** ModifiersSyntax to the constructor. */
	protected final ModifiersSyntax modifiers;
	
	/** TypeSyntax parameters used. */
	protected final TypeParametersSyntax typeparameters;
	
	/** The identifier of the constructor. */
	protected final MethodName name;
	
	/** The code which makes up the constructor. */
	protected final UnparsedExpressions code;
	
	/** The formal parameters. */
	protected final FormalParametersSyntax parameters;
	
	/** The return type. */
	protected final TypeSyntax returntype;
	
	/** The thrown classes. */
	private final QualifiedIdentifierSyntax[] _thrown;
	
	/** Map view. */
	private Reference<Map<String, Object>> _map;
	
	/** String form. */
	private Reference<String> _string;
	
	/**
	 * Initializes simple method.
	 *
	 * @param __mods The modifiers.
	 * @param __rtype The return type.
	 * @param __ident The method name.
	 * @param __params The parameters.
	 * @param __thrown The thrown methods.
	 * @param __code The code block, may be {@code null} if abstract or native.
	 * @throws NullPointerException On null arguments except for
	 * {@code __code}.
	 * @throws SyntaxDefinitionException If the structure is not correct.
	 * @since 2018/04/30
	 */
	public SimpleMethodSyntax(ModifiersSyntax __mods,
		TypeParametersSyntax __tparms, TypeSyntax __rtype, MethodName __ident,
		FormalParametersSyntax __params, QualifiedIdentifierSyntax[] __thrown,
		UnparsedExpressions __code)
		throws NullPointerException, SyntaxDefinitionException
	{
		if (__mods == null || __tparms == null || __rtype == null ||
			__ident == null || __params == null || __thrown == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error AQ2c Mismatch between the method having code
		// or not and it being abstract and/or native.}
		if ((__mods.isAbstract() || __mods.isNative()) != (__code == null))
			throw new SyntaxDefinitionException("AQ2c");
		
		// Check throwables for null
		Set<QualifiedIdentifierSyntax> thrown = new LinkedHashSet<>();
		for (QualifiedIdentifierSyntax t : (__thrown = __thrown.clone()))
		{
			if (t == null)
				throw new NullPointerException("NARG");
			
			// {@squirreljme.error AQ2d Duplicated throw statement. (The throw
			// statement which was duplicated)}
			if (thrown.contains(t))
				throw new SyntaxDefinitionException(
					String.format("AQ2d %s", t));
			
			thrown.add(t);
		}
		
		// {@squirreljme.error AQ2e Illegal modifiers specified for simple
		// method. (The modifiers)}
		if ((__mods.isAbstract() && (__mods.isStatic() || __mods.isNative() ||
			__mods.isFinal())) || __mods.isVolatile() || __mods.isTransient())
			throw new SyntaxDefinitionException(
				String.format("AQ2e %s", __mods));
		
		this.modifiers = __mods;
		this.typeparameters = __tparms;
		this.returntype = __rtype;
		this.name = new MethodName(__ident.toString());
		this.parameters = __params;
		this.code = __code;
		this._thrown = __thrown;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/01/17
	 */
	@Override
	public Map<String, Object> asMap()
	{
		Reference<Map<String, Object>> ref = this._map;
		Map<String, Object> rv;
		
		if (ref == null || null == (rv = ref.get()))
			this._map = new WeakReference<>((rv = new __TreeBuilder__().
				add("modifiers", this.modifiers).
				add("typeparams", this.typeparameters).
				add("name", this.name).
				add("return", this.returntype).
				add("parameters", this.parameters).
				addList("thrown", (Object[])this._thrown).
				add("code", this.code).build()));
		
		return rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/04/28
	 */
	@Override
	public final boolean equals(Object __o)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/04/28
	 */
	@Override
	public final int hashCode()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/04/29
	 */
	@Override
	public final ModifiersSyntax modifiers()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/04/29
	 */
	@Override
	public final MethodName name()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/04/28
	 */
	@Override
	public final String toString()
	{
		Reference<String> ref = this._string;
		String rv;
		
		if (ref == null || null == (rv = ref.get()))
			this._string = new WeakReference<>((rv = this.asMap().toString()));
		
		return rv;
	}
	
	/**
	 * Parses a simple method.
	 *
	 * @param __mods The modifiers to the method.
	 * @param __typeparams TypeSyntax parameters.
	 * @param __in The input tokens.
	 * @return The parsed method.
	 * @throws NullPointerException On null arguments.
	 * @throws SyntaxParseException If it is not a valid method.
	 * @since 2018/04/28
	 */
	public static SimpleMethodSyntax parse(ModifiersSyntax __mods,
		TypeParametersSyntax __typeparams, BufferedTokenSource __in)
		throws NullPointerException, SyntaxParseException
	{
		if (__mods == null || __typeparams == null || __in == null)
			throw new NullPointerException("NARG");
		
		// Read return type
		Token token = __in.peek();
		TypeSyntax returntype;
		if (token.type() == TokenType.KEYWORD_VOID)
		{
			__in.next();
			returntype = TypeSyntax.VOID;
		}
		else
			returntype = TypeSyntax.parseType(__in);
		
		// {@squirreljme.error AQ2f Expected identifier to name the method.}
		token = __in.next();
		if (token.type() != TokenType.IDENTIFIER)
			throw new SyntaxParseException(token, "AQ2f");
		MethodName identifier = new MethodName(token.characters());
		
		// Parse formal parameters
		FormalParametersSyntax params = FormalParametersSyntax.parse(__in);
		
		// Parse throws
		QualifiedIdentifierSyntax[] thrown;
		token = __in.peek();
		if (token.type() == TokenType.KEYWORD_THROWS)
		{
			__in.next();
			thrown = QualifiedIdentifierSyntax.parseList(__in);
		}
		else
			thrown = new QualifiedIdentifierSyntax[0];
		
		// Parse potential code block?
		UnparsedExpressions code;
		if (__mods.isAbstract() || __mods.isNative())
		{
			// No code
			code = null;
			
			// {@squirreljme.error AQ2g Expected semicolon to follow abstract
			// or native method.}
			token = __in.next();
			if (token.type() != TokenType.SYMBOL_SEMICOLON)
				throw new SyntaxParseException(token, "AQ2g");
		}
		else
			code = UnparsedExpressions.parseBlock(__in);
		
		// Initialize simple method
		return new SimpleMethodSyntax(__mods, __typeparams, returntype,
			identifier, params, thrown, code);
	}
}

