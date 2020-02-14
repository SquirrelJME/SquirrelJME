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
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.multiphasicapps.classfile.ClassIdentifier;
import net.multiphasicapps.classfile.MethodName;
import net.multiphasicapps.collections.UnmodifiableArrayList;
import net.multiphasicapps.javac.token.BufferedTokenSource;
import net.multiphasicapps.javac.token.Token;
import net.multiphasicapps.javac.token.TokenSource;
import net.multiphasicapps.javac.token.TokenType;

/**
 * This represents a constructor that is available for a class.
 *
 * @since 2018/04/28
 */
public final class ClassConstructorSyntax
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
	
	/** The thrown classes. */
	private final QualifiedIdentifierSyntax[] _thrown;
	
	/** Map view. */
	private Reference<Map<String, Object>> _map;
	
	/** String form. */
	private Reference<String> _string;
	
	/**
	 * Initializes the constructor information.
	 *
	 * @param __mods Constructor modifiers.
	 * @param __tparms TypeSyntax parameters used.
	 * @param __ident The identifier for the constructor.
	 * @param __params TypeSyntax parameters.
	 * @param __thrown Exceptions which are thrown by the constructor.
	 * @param __code The code which makes up the constructor.
	 * @throws NullPointerException On null arguments.
	 * @throws SyntaxDefinitionException If the definition is not valid.
	 * @since 2018/04/29
	 */
	public ClassConstructorSyntax(ModifiersSyntax __mods,
		TypeParametersSyntax __tparms, ClassIdentifier __ident,
		FormalParametersSyntax __params, QualifiedIdentifierSyntax[] __thrown,
		UnparsedExpressions __code)
		throws NullPointerException, SyntaxDefinitionException
	{
		if (__mods == null || __tparms == null || __ident == null ||
			__params == null || __thrown == null || __code == null)
			throw new NullPointerException("NARG");
		
		// Check throwables for null
		Set<QualifiedIdentifierSyntax> thrown = new LinkedHashSet<>();
		for (QualifiedIdentifierSyntax t : (__thrown = __thrown.clone()))
		{
			if (t == null)
				throw new NullPointerException("NARG");
			
			// {@squirreljme.error AQ16 Duplicated throw statement. (The throw
			// statement which was duplicated)}
			if (thrown.contains(t))
				throw new SyntaxDefinitionException(
					String.format("AQ16 %s", t));
			
			thrown.add(t);
		}
		
		// {@squirreljme.error AQ17 Illegal modifiers specified for class
		// constructor. (The modifiers)}
		if (__mods.isStatic() || __mods.isAbstract() || __mods.isFinal() ||
			__mods.isNative() || __mods.isSynchronized() ||
			__mods.isTransient() || __mods.isVolatile() ||
			__mods.isStrictFloatingPoint())
			throw new SyntaxDefinitionException(
				String.format("AQ17 %s", __mods));
		
		this.modifiers = __mods;
		this.typeparameters = __tparms;
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
				add("parameters", this.parameters).
				addList("thrown", (Object[])this._thrown).
				add("code", this.code).build()));
		
		return rv;
	}
	
	/**
	 * Returns the code for this constructor if there is any.
	 *
	 * @return The method code, may be {@code null} if there is none.
	 * @since 2018/05/08
	 */
	public final UnparsedExpressions code()
	{
		return this.code;
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
	 * Returns the formal parameters for the constructor.
	 *
	 * @return The constructor formal parameters.
	 * @since 2018/05/08
	 */
	public final FormalParametersSyntax formalParameters()
	{
		return this.parameters;
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
		return this.modifiers;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/04/29
	 */
	@Override
	public final MethodName name()
	{
		return this.name;
	}
	
	/**
	 * Returns the types which are thrown from this constructor.
	 *
	 * @return The types which are thrown.
	 * @since 2018/05/08
	 */
	public final List<QualifiedIdentifierSyntax> thrownTypes()
	{
		return UnmodifiableArrayList.<QualifiedIdentifierSyntax>of(
			this._thrown);
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
	 * Returns the type parameters for the constructor.
	 *
	 * @return The constructor type parameters.
	 * @since 2018/05/08
	 */
	public final TypeParametersSyntax typeParameters()
	{
		return this.typeparameters;
	}
	
	/**
	 * Parses a single class constrictor.
	 *
	 * @param __mods The modifiers to the method.
	 * @param __typeparams TypeSyntax parameters.
	 * @param __in The input tokens.
	 * @return The parsed constructor.
	 * @throws NullPointerException On null arguments.
	 * @throws SyntaxParseException If it is not a valid constructor.
	 * @since 2018/04/28
	 */
	public static ClassConstructorSyntax parse(ModifiersSyntax __mods,
		TypeParametersSyntax __typeparams, BufferedTokenSource __in)
		throws NullPointerException, SyntaxParseException
	{
		if (__mods == null || __typeparams == null || __in == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error AQ18 Expected identifier at start of
		// constructor.}
		Token token = __in.next();
		if (token.type() != TokenType.IDENTIFIER)
			throw new SyntaxParseException(token, "AQ18");
		ClassIdentifier identifier = new ClassIdentifier(token.characters());
		
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
		
		// Parse constructor block and build
		return new ClassConstructorSyntax(__mods, __typeparams, identifier,
			params, thrown, UnparsedExpressions.parseBlock(__in));
	}
}

