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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import net.multiphasicapps.classfile.ClassIdentifier;
import net.multiphasicapps.classfile.FieldName;
import net.multiphasicapps.classfile.MethodName;
import net.multiphasicapps.javac.token.BufferedTokenSource;
import net.multiphasicapps.javac.token.Token;
import net.multiphasicapps.javac.token.TokenSource;
import net.multiphasicapps.javac.token.TokenType;

/**
 * This class represents the structure of a class, it will contains members
 * such as fields, methods, and other classes.
 *
 * @since 2018/04/21
 */
public final class ClassSyntax
	implements MemberSyntax
{
	/** The type of class this is. */
	protected final ClassSyntaxType type;
	
	/** The modifiers to the class. */
	protected final ModifiersSyntax modifiers;
	
	/** The name of the class. */
	protected final ClassIdentifier name;
	
	/** The type parameters of the class. */
	protected final TypeParametersSyntax typeparams;
	
	/** The extending types. */
	private final TypeSyntax[] _extends;
	
	/** The implementing types. */
	private final TypeSyntax[] _implements;
	
	/** The members of the class. */
	private final MemberSyntax[] _members;
	
	/**
	 * Initializes the class structure information.
	 *
	 * @param __structtype The type of structure used.
	 * @param __mods The modifiers to the class.
	 * @param __name The name of the class.
	 * @param __typeparms The type parameters of the class.
	 * @param __extending The classes this class extends.
	 * @param __implementing The classes this class implements.
	 * @param __members The members of this class.
	 * @throws NullPointerException On null arguments.
	 * @throws SyntaxDefinitionException If the structure is not valid.
	 * @since 2018/04/27
	 */
	public ClassSyntax(ClassSyntaxType __structtype, ModifiersSyntax __mods,
		ClassIdentifier __name, TypeParametersSyntax __typeparms,
		TypeSyntax[] __extending, TypeSyntax[] __implementing,
		MemberSyntax[] __members)
		throws NullPointerException, SyntaxDefinitionException
	{
		if (__structtype == null || __mods == null || __name == null ||
			__typeparms == null || __extending == null ||
			__implementing == null || __members == null)
			throw new NullPointerException();
		
		// {@squirreljme.error AQ4l Illegal class modifiers specified with the
		// the given class type. (The class type; The class modifiers)}
		if (__mods.isNative() || __mods.isVolatile() || __mods.isTransient() ||
			__mods.isSynchronized() ||
			(__structtype == ClassSyntaxType.ENUM &&
				(__mods.isAbstract() || __mods.isFinal())) ||
			(__structtype == ClassSyntaxType.INTERFACE &&
				(__mods.isFinal())))
			throw new SyntaxDefinitionException(String.format("AQ4l %s %s",
				__structtype, __mods));
		
		// Check extends
		Set<TypeSyntax> doext = new LinkedHashSet<>();
		for (TypeSyntax t : __extending)
		{
			if (t == null)
				throw new NullPointerException("NARG");
			
			// {@squirreljme.error AQ4m Duplicate extends specified.
			// (The type)}
			if (doext.contains(t))
				throw new SyntaxDefinitionException(String.format("AQ4m %s",
					t));
			
			doext.add(t);
		}
		
		// {@squirreljme.error AQ4o The specified class of the given type
		// cannot extend the given types. (The class type; The inherited types}
		if (!__structtype.extendsType().isCompatibleCount(doext.size()))
			throw new SyntaxDefinitionException(
				String.format("AQ4o %s %s", __structtype, doext));
		
		// Check implements
		Set<TypeSyntax> doimp = new LinkedHashSet<>();
		for (TypeSyntax t : __implementing)
		{
			if (t == null)
				throw new NullPointerException("NARG");
			
			// {@squirreljme.error AQ4n Duplicate implements specified.
			// (The type)}
			if (doimp.contains(t))
				throw new SyntaxDefinitionException(String.format("AQ4n %s",
					t));
			
			doimp.add(t);
		}
		
		// {@squirreljme.error AQ4p The specified class of the given type
		// cannot implement the given types. (The class type; The inherited
		// types}
		if (!__structtype.implementsType().isCompatibleCount(doimp.size()))
			throw new SyntaxDefinitionException(
				String.format("AQ4p %s %s", __structtype, doext));
		
		// Check members
		List<MemberSyntax> membs = new ArrayList<>();
		for (MemberSyntax member : __members)
		{
			if (member == null)
				throw new NullPointerException("NARG");
			
			// Interfaces
			if (__structtype == ClassSyntaxType.INTERFACE)
			{
				// {@squirreljme.error AQ4q An interface cannot contain a
				// member represented by the given type. (The class type)}
				if (member instanceof ClassInitializerSyntax ||
					member instanceof ClassConstructorSyntax ||
					member instanceof AnnotationMethodSyntax ||
					member instanceof EnumFieldSyntax)
					throw new SyntaxDefinitionException(
						String.format("AQ4q %s", member.getClass()));
			}
			
			// Annotations
			else if (__structtype == ClassSyntaxType.ANNOTATION)
			{
				// {@squirreljme.error AQ4r An annotation cannot contain a
				// member represented by the given type. (The class type)}
				if (!(member instanceof ClassSyntax ||
					member instanceof BasicFieldSyntax ||
					member instanceof AnnotationMethodSyntax))
					throw new SyntaxDefinitionException(
						String.format("AQ4r %s", member.getClass()));
			}
			
			// Normal class or enum
			else
			{
				// {@squirreljme.error AQ4s An class or enum cannot contain a
				// member represented by the given type. (The class type)}
				if (member instanceof AnnotationMethodSyntax)
					throw new SyntaxDefinitionException(
						String.format("AQ4s %s", member.getClass()));
			}
			
			// Is valid
			membs.add(member);
		}
		
		// Set
		this.type = __structtype;
		this.modifiers = __mods;
		this.name = __name;
		this.typeparams = __typeparms;
		this._extends = doext.<TypeSyntax>toArray(
			new TypeSyntax[doext.size()]);
		this._implements = doimp.<TypeSyntax>toArray(
			new TypeSyntax[doimp.size()]);
		this._members = membs.<MemberSyntax>toArray(
			new MemberSyntax[membs.size()]);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/05/08
	 */
	@Override
	public final boolean equals(Object __o)
	{
		throw new todo.TODO();
	}
	
	/**
	 * Returns the types this class extends.
	 *
	 * @return The classes which this extends.
	 * @since 2018/05/08
	 */
	public final TypeSyntax[] extending()
	{
		return this._extends.clone();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/05/08
	 */
	@Override
	public final int hashCode()
	{
		return this.type.hashCode() ^
			this.modifiers.hashCode() ^
			this.name.hashCode() ^
			this.typeparams.hashCode() ^
			Arrays.asList(this._extends).hashCode() ^
			Arrays.asList(this._implements).hashCode() ^
			Arrays.asList(this._members).hashCode();
	}
	
	/**
	 * Returns the types this class implements.
	 *
	 * @return The classes this implements.
	 * @since 2018/05/08
	 */
	public final TypeSyntax[] implementing()
	{
		return this._implements.clone();
	}
	
	/**
	 * Returns the members of this class.
	 *
	 * @return The class members.
	 * @since 2018/05/08
	 */
	public final MemberSyntax[] members()
	{
		return this._members.clone();
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
	public final ClassIdentifier name()
	{
		return this.name;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/05/08
	 */
	@Override
	public final String toString()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Returns the type of class this is.
	 *
	 * @return The class type.
	 * @since 2018/05/08
	 */
	public final ClassSyntaxType type()
	{
		return this.type;
	}
	
	/**
	 * Returns the type parameters for this class.
	 *
	 * @return The type parameters used.
	 * @since 2018/05/08
	 */
	public final TypeParametersSyntax typeParameters()
	{
		return this.typeparams;
	}
	
	/**
	 * Parses the body of the class.
	 *
	 * @param __structtype The structure type of the class.
	 * @param __in The input token source.
	 * @return The parsed members of the class.
	 * @throws NullPointerException On null arguments.
	 * @throws SyntaxParseException If the members of the body are not
	 * correct.
	 * @since 2018/04/27
	 */
	public static MemberSyntax[] parseClassBody(
		ClassSyntaxType __structtype, BufferedTokenSource __in)
		throws NullPointerException, SyntaxParseException
	{
		if (__in == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error AQ3z Expected opening brace at start of class.}
		Token token = __in.next();
		if (token.type() != TokenType.SYMBOL_OPEN_BRACE)
			throw new SyntaxParseException(token, "AQ3z");
		
		// Parse the enumeration constants, their fields and their class
		// values
		if (__structtype == ClassSyntaxType.ENUM)
		{
			throw new todo.TODO();
		}
		
		// Member parsing loop
		List<MemberSyntax> rv = new ArrayList<>();
		for (;;)
		{
			// End of members?
			token = __in.peek();
			if (token.type() == TokenType.SYMBOL_CLOSED_BRACE)
				return rv.<MemberSyntax>toArray(
					new MemberSyntax[rv.size()]);
			
			// Ignore semi-colons just lying around in the class
			else if (token.type() == TokenType.SYMBOL_SEMICOLON)
				continue;
			
			// Parse modifiers since this will modify how things are parsed
			ModifiersSyntax mods = ModifiersSyntax.parse(__in);
			
			// Always try to parse a class since any kind of class can include
			// classes
			try
			{
				// Parse the class
				__in.mark();
				rv.add(ClassSyntax.parseEntireClass(mods, __in));
				
				// Do not try parsing anything else for now
				__in.commit();
				continue;
			}
			catch (SyntaxParseException e)
			{
				__in.reset();
			}
			
			// Try to parse a method
			try
			{
				// Parse method
				__in.mark();
				rv.add(ClassSyntax.parseMethod(__structtype, mods, __in));
				
				// Valid method
				__in.commit();
				continue;
			}
			catch (SyntaxParseException e)
			{
				__in.reset();
			}
			
			// Try to parse a field
			try
			{
				// Parse field
				__in.mark();
				rv.add(ClassSyntax.parseField(__structtype, mods, __in));
				
				// Valid field
				__in.commit();
				continue;
			}
			catch (SyntaxParseException e)
			{
				__in.reset();
			}
			
			// {@squirreljme.error AQ40 The specified token does not
			// represent a member that is a class, field, or method.}
			throw new SyntaxParseException(__in, "AQ40");
		}
	}
	
	/**
	 * Attempts to parse an entire class.
	 *
	 * @param __mods Class modifiers.
	 * @param __in The input token source.
	 * @return The parsed class structure.
	 * @throws NullPointerException On null arguments.
	 * @throws SyntaxParseException If this is not a class.
	 * @since 2018/04/22
	 */
	public static ClassSyntax parseEntireClass(ModifiersSyntax __mods,
		BufferedTokenSource __in)
		throws NullPointerException, SyntaxParseException
	{
		if (__mods == null || __in == null)
			throw new NullPointerException("NARG");
		
		Token token;
		
		// Determine the class type based on the next token
		ClassSyntaxType structtype;
		token = __in.next();
		switch (token.type())
		{
			case KEYWORD_CLASS:
				structtype = ClassSyntaxType.CLASS;
				break;
				
			case KEYWORD_ENUM:
				structtype = ClassSyntaxType.ENUM;
				break;
				
			case KEYWORD_INTERFACE:
				structtype = ClassSyntaxType.INTERFACE;
				break;
			
				// {@squirreljme.error AQ3m Expected interface to follow
				// at symbol for declaring an annotation type.}
			case SYMBOL_AT:
				token = __in.next();
				if (token.type() != TokenType.KEYWORD_INTERFACE)
					throw new SyntaxParseException(token, "AQ3m");
				structtype = ClassSyntaxType.ANNOTATION;
				break;
			
				// {@squirreljme.error AQ3n Unknown token while parsing class.}
			default:
				throw new SyntaxParseException(token, "AQ3n");
		}
		
		// {@squirreljme.error AQ3v Expected identifier to name the class as.}
		token = __in.next();
		if (token.type() != TokenType.IDENTIFIER)
			throw new SyntaxParseException(token, "AQ3v");
		ClassIdentifier name = new ClassIdentifier(token.characters());
		
		// Read type parameters?
		token = __in.peek();
		TypeParametersSyntax typeparms;
		if (structtype.hasTypeParameters() &&
			token.type() == TokenType.COMPARE_LESS_THAN)
			typeparms = TypeParametersSyntax.parse(__in);
		else
			typeparms = new TypeParametersSyntax();
		
		// Read extends
		token = __in.peek();
		TypeSyntax[] extending;
		if (token.type() == TokenType.KEYWORD_EXTENDS)
		{
			// Consume and parse list
			__in.next();
			switch (structtype.extendsType())
			{
				case SINGLE:
					extending = new TypeSyntax[]{TypeSyntax.parseType(__in)};
					break;
					
				case MULTIPLE:
					extending = TypeSyntax.parseTypes(__in);
					break;
					
					// {@squirreljme.error AQ3w This type of class cannot
					// extend other classes.}
				default:
					throw new SyntaxParseException(token, "AQ3w");
			}
		}
		else
			extending = new TypeSyntax[0];
		
		// Read implements
		token = __in.peek();
		TypeSyntax[] implementing;
		if (token.type() == TokenType.KEYWORD_IMPLEMENTS)
		{
			// Consume and parse list
			__in.next();
			switch (structtype.extendsType())
			{
				case MULTIPLE:
					implementing = TypeSyntax.parseTypes(__in);
					break;
					
					// {@squirreljme.error AQ3y This type of class cannot
					// implement interfaces.}
				default:
					throw new SyntaxParseException(token, "AQ3y");
			}
		}
		else
			implementing = new TypeSyntax[0];
		
		// Read class body which contains all the members
		MemberSyntax[] members = ClassSyntax.parseClassBody(structtype,
			__in);
		
		// {@squirreljme.error AQ4t Expected closing brace at end of class.}
		token = __in.next();
		if (token.type() != TokenType.SYMBOL_CLOSED_BRACE)
			throw new SyntaxParseException(token, "AQ4t");
		
		// Build class structure
		return new ClassSyntax(structtype, __mods, name, typeparms,
			extending, implementing, members);
	}
	
	/**
	 * Parses a single field which is appropriate for a given class type.
	 *
	 * @param __ct The structure of the class.
	 * @param __mods The modifiers to the field.
	 * @param __in The input tokens.
	 * @return The parsed field.
	 * @throws NullPointerException On null arguments.
	 * @throws SyntaxParseException If it is not a valid field.
	 * @since 2018/04/28
	 */
	public static FieldSyntax parseField(ClassSyntaxType __ct,
		ModifiersSyntax __mods, BufferedTokenSource __in)
		throws NullPointerException, SyntaxParseException
	{
		if (__ct == null || __mods == null || __in == null)
			throw new NullPointerException("NARG");
		
		// Always parse basic fields
		return BasicFieldSyntax.parse(__mods, __in);
	}
	
	/**
	 * Parses a single method which is appropriate for a given class type.
	 *
	 * @param __ct The structure of the class.
	 * @param __mods The modifiers to the method.
	 * @param __in The input tokens.
	 * @return The parsed method.
	 * @throws NullPointerException On null arguments.
	 * @throws SyntaxParseException If it is not a valid method.
	 * @since 2018/04/28
	 */
	public static MethodSyntax parseMethod(ClassSyntaxType __ct,
		ModifiersSyntax __mods, BufferedTokenSource __in)
		throws NullPointerException, SyntaxParseException
	{
		if (__ct == null || __mods == null || __in == null)
			throw new NullPointerException("NARG");
		
		// Annotations only have a single format
		if (__ct == ClassSyntaxType.ANNOTATION)
			return AnnotationMethodSyntax.parse(__mods, __in);
		
		// Initializer method?
		Token token = __in.peek();
		if ((__ct == ClassSyntaxType.CLASS ||
			__ct == ClassSyntaxType.ENUM) &&
			token.type() == TokenType.SYMBOL_OPEN_BRACE)
			try
			{
				// Might not be one
				__in.mark();
				ClassInitializerSyntax rv = ClassInitializerSyntax.
					parse(__mods, __in);
				
				// Is one
				__in.commit();
				return rv;
			}
			catch (SyntaxParseException e)
			{
				__in.reset();
			}
		
		// Parse any type parameters which are used
		TypeParametersSyntax typeparams;
		token = __in.peek();
		if (__ct != ClassSyntaxType.ANNOTATION &&
			token.type() == TokenType.COMPARE_LESS_THAN)
			typeparams = TypeParametersSyntax.parse(__in);
		else
			typeparams = new TypeParametersSyntax();
		
		// Constructor?
		token = __in.peek();
		if (__ct != ClassSyntaxType.INTERFACE &&
			token.type() == TokenType.IDENTIFIER &&
			__in.peek(1).type() == TokenType.SYMBOL_OPEN_PARENTHESIS)
			try
			{
				// Could be one
				__in.mark();
				ClassConstructorSyntax rv = ClassConstructorSyntax.
					parse(__mods, typeparams, __in);
				
				// Is one
				__in.commit();
				return rv;
			}
			catch (SyntaxParseException e)
			{
				__in.reset();
			}
		
		// General method
		return SimpleMethodSyntax.parse(__mods, typeparams, __in);
	}
}

