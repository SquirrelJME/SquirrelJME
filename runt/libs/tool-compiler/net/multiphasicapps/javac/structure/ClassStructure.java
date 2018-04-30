// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.javac.structure;

import java.util.ArrayList;
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
public final class ClassStructure
	implements MemberStructure
{
	/**
	 * Initializes the class structure information.
	 *
	 * @param __structtype The type of structure used.
	 * @param __name The name of the class.
	 * @param __typeparms The type parameters of the class.
	 * @param __extending The classes this class extends.
	 * @param __implementing The classes this class implements.
	 * @param __members The members of this class.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/04/27
	 */
	public ClassStructure(ClassStructureType __structtype,
		ClassIdentifier __name, TypeParameter[] __typeparms,
		Type[] __extending, Type[] __implementing, MemberStructure[] __members)
		throws NullPointerException
	{
		if (__structtype == null || __name == null || __typeparms == null ||
			__extending == null || __implementing == null || __members == null)
			throw new NullPointerException();
		
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/04/29
	 */
	@Override
	public final Modifiers modifiers()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/04/29
	 */
	@Override
	public final ClassIdentifier name()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Parses the body of the class.
	 *
	 * @param __structtype The structure type of the class.
	 * @param __in The input token source.
	 * @return The parsed members of the class.
	 * @throws NullPointerException On null arguments.
	 * @throws StructureParseException If the members of the body are not
	 * correct.
	 * @since 2018/04/27
	 */
	public static MemberStructure[] parseClassBody(
		ClassStructureType __structtype, BufferedTokenSource __in)
		throws NullPointerException, StructureParseException
	{
		if (__in == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error AQ3z Expected opening brace at start of class.}
		Token token = __in.next();
		if (token.type() != TokenType.SYMBOL_OPEN_BRACE)
			throw new StructureParseException(token, "AQ3z");
		
		// Parse the enumeration constants, their fields and their class
		// values
		if (__structtype == ClassStructureType.ENUM)
		{
			throw new todo.TODO();
		}
		
		// Member parsing loop
		List<MemberStructure> rv = new ArrayList<>();
		for (;;)
		{
			// End of members?
			token = __in.peek();
			if (token.type() == TokenType.SYMBOL_CLOSED_BRACE)
				return rv.<MemberStructure>toArray(
					new MemberStructure[rv.size()]);
			
			// Ignore semi-colons just lying around in the class
			else if (token.type() == TokenType.SYMBOL_SEMICOLON)
				continue;
			
			// Parse modifiers since this will modify how things are parsed
			Modifiers mods = Modifiers.parse(__in);
			
			// Always try to parse a class since any kind of class can include
			// classes
			try
			{
				// Parse the class
				__in.mark();
				rv.add(ClassStructure.parseEntireClass(mods, __in));
				
				// Do not try parsing anything else for now
				__in.commit();
				continue;
			}
			catch (StructureParseException e)
			{
				__in.reset();
			}
			
			// Try to parse a method
			try
			{
				// Parse method
				__in.mark();
				rv.add(ClassStructure.parseMethod(__structtype, mods, __in));
				
				// Valid method
				__in.commit();
				continue;
			}
			catch (StructureParseException e)
			{
				__in.reset();
			}
			
			// Try to parse a field
			try
			{
				// Parse field
				__in.mark();
				rv.add(ClassStructure.parseField(__structtype, mods, __in));
				
				// Valid field
				__in.commit();
				continue;
			}
			catch (StructureParseException e)
			{
				__in.reset();
			}
			
			// {@squirreljme.error AQ40 The specified token does not
			// represent a member that is a class, field, or method.}
			throw new StructureParseException(__in, "AQ40");
		}
	}
	
	/**
	 * Attempts to parse an entire class.
	 *
	 * @param __mods Class modifiers.
	 * @param __in The input token source.
	 * @return The parsed class structure.
	 * @throws NullPointerException On null arguments.
	 * @throws StructureParseException If this is not a class.
	 * @since 2018/04/22
	 */
	public static ClassStructure parseEntireClass(Modifiers __mods,
		BufferedTokenSource __in)
		throws NullPointerException, StructureParseException
	{
		if (__mods == null || __in == null)
			throw new NullPointerException("NARG");
		
		Token token;
		
		// Determine the class type based on the next token
		ClassStructureType structtype;
		token = __in.next();
		switch (token.type())
		{
			case KEYWORD_CLASS:
				structtype = ClassStructureType.CLASS;
				break;
				
			case KEYWORD_ENUM:
				structtype = ClassStructureType.ENUM;
				break;
				
			case KEYWORD_INTERFACE:
				structtype = ClassStructureType.INTERFACE;
				break;
			
				// {@squirreljme.error AQ3m Expected interface to follow
				// at symbol for declaring an annotation type.}
			case SYMBOL_AT:
				token = __in.next();
				if (token.type() != TokenType.KEYWORD_INTERFACE)
					throw new StructureParseException(token, "AQ3m");
				structtype = ClassStructureType.ANNOTATION;
				break;
			
				// {@squirreljme.error AQ3n Unknown token while parsing class.}
			default:
				throw new StructureParseException(token, "AQ3n");
		}
		
		// {@squirreljme.error AQ3v Expected identifier to name the class as.}
		token = __in.next();
		if (token.type() != TokenType.IDENTIFIER)
			throw new StructureParseException(token, "AQ3v");
		ClassIdentifier name = new ClassIdentifier(token.characters());
		
		// Read type parameters?
		token = __in.peek();
		TypeParameter[] typeparms;
		if (structtype.hasTypeParameters() &&
			token.type() == TokenType.COMPARE_LESS_THAN)
			typeparms = TypeParameter.parseTypeParameters(__in);
		else
			typeparms = new TypeParameter[0];
		
		// Read extends
		token = __in.peek();
		Type[] extending;
		if (token.type() == TokenType.KEYWORD_EXTENDS)
		{
			// Consume and parse list
			__in.next();
			switch (structtype.extendsType())
			{
				case SINGLE:
					extending = new Type[]{Type.parseType(__in)};
					break;
					
				case MULTIPLE:
					extending = Type.parseTypes(__in);
					break;
					
					// {@squirreljme.error AQ3w This type of class cannot
					// extend other classes.}
				default:
					throw new StructureParseException(token, "AQ3w");
			}
		}
		else
			extending = new Type[0];
		
		// Read implements
		token = __in.peek();
		Type[] implementing;
		if (token.type() == TokenType.KEYWORD_IMPLEMENTS)
		{
			// Consume and parse list
			__in.next();
			switch (structtype.extendsType())
			{
				case MULTIPLE:
					implementing = Type.parseTypes(__in);
					break;
					
					// {@squirreljme.error AQ3y This type of class cannot
					// implement interfaces.}
				default:
					throw new StructureParseException(token, "AQ3y");
			}
		}
		else
			implementing = new Type[0];
		
		// Read class body which contains all the members
		MemberStructure[] members = ClassStructure.parseClassBody(structtype,
			__in);
		
		// Build class structure
		return new ClassStructure(structtype, name, typeparms,
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
	 * @throws StructureParseException If it is not a valid field.
	 * @since 2018/04/28
	 */
	public static FieldStructure parseField(ClassStructureType __ct,
		Modifiers __mods, BufferedTokenSource __in)
		throws NullPointerException, StructureParseException
	{
		if (__ct == null || __mods == null || __in == null)
			throw new NullPointerException("NARG");
		
		// Always parse basic fields
		return BasicField.parse(__mods, __in);
	}
	
	/**
	 * Parses a single method which is appropriate for a given class type.
	 *
	 * @param __ct The structure of the class.
	 * @param __mods The modifiers to the method.
	 * @param __in The input tokens.
	 * @return The parsed method.
	 * @throws NullPointerException On null arguments.
	 * @throws StructureParseException If it is not a valid method.
	 * @since 2018/04/28
	 */
	public static MethodStructure parseMethod(ClassStructureType __ct,
		Modifiers __mods, BufferedTokenSource __in)
		throws NullPointerException, StructureParseException
	{
		if (__ct == null || __mods == null || __in == null)
			throw new NullPointerException("NARG");
		
		// Annotations only have a single format
		if (__ct == ClassStructureType.ANNOTATION)
			return AnnotationMethod.parse(__mods, __in);
		
		// Initializer method?
		Token token = __in.peek();
		if ((__ct == ClassStructureType.CLASS ||
			__ct == ClassStructureType.ENUM) &&
			token.type() == TokenType.SYMBOL_OPEN_BRACE)
			try
			{
				// Might not be one
				__in.mark();
				ClassInitializer rv = ClassInitializer.parse(__mods, __in);
				
				// Is one
				__in.commit();
				return rv;
			}
			catch (StructureParseException e)
			{
				__in.reset();
			}
		
		// Parse any type parameters which are used
		TypeParameter[] typeparams;
		token = __in.peek();
		if (__ct != ClassStructureType.ANNOTATION &&
			token.type() == TokenType.COMPARE_LESS_THAN)
			typeparams = TypeParameter.parseTypeParameters(__in);
		else
			typeparams = new TypeParameter[0];
		
		// Constructor?
		token = __in.peek();
		if (__ct != ClassStructureType.INTERFACE &&
			token.type() == TokenType.IDENTIFIER &&
			__in.peek(1).type() == TokenType.SYMBOL_OPEN_PARENTHESIS)
			try
			{
				// Could be one
				__in.mark();
				ClassConstructor rv = ClassConstructor.parse(__mods,
					typeparams, __in);
				
				// Is one
				__in.commit();
				return rv;
			}
			catch (StructureParseException e)
			{
				__in.reset();
			}
		
		// General method
		return SimpleMethod.parse(__mods, typeparams, __in);
	}
}

