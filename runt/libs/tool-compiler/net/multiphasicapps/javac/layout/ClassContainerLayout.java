// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.javac.layout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import net.multiphasicapps.classfile.BinaryName;
import net.multiphasicapps.classfile.ClassIdentifier;
import net.multiphasicapps.javac.token.ExpandedToken;
import net.multiphasicapps.javac.token.ExpandingSource;
import net.multiphasicapps.javac.token.ExpandingStacker;
import net.multiphasicapps.javac.token.TokenType;

/**
 * This contains the layout for the class container which essentially is the
 * outer portion of the class declaration which includes modifiers (including
 * annotations), classnames, possible generic type declarations, extends, and
 * implements.
 *
 * @since 2018/03/22
 */
public final class ClassContainerLayout
	implements ClassMemberLayout, Layout
{
	/**
	 * Parses a single class container and returns it.
	 *
	 * @param __t Where to read class containers from.
	 * @throws LayoutParserException If the class container could not be
	 * parsed.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/04/08
	 */
	public static final ClassContainerLayout parse(ExpandingSource __t)
		throws LayoutParserException, IOException, NullPointerException
	{
		if (__t == null)
			throw new NullPointerException("NARG");
		
		// Read modifiers then continue on to parsing the class
		// This has to be done because it is unknown what kind of member is
		// in a class before the modifiers are read so this allows simple
		// parsing of classes as members accordingly.
		return ClassContainerLayout.__parse(Modifiers.parse(__t), __t);
	}
		
	/**
	 * Parses a single class container with the given modifiers then
	 * returns it. This method is needed
	 *
	 * @param __m The modifiers for the class.
	 * @param __t Where to read class containers from.
	 * @throws LayoutParserException If the class container could not be
	 * parsed.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/04/08
	 */
	private static final ClassContainerLayout __parse(Modifiers __m,
		ExpandingSource __t)
		throws LayoutParserException, IOException, NullPointerException
	{
		if (__m == null || __t == null)
			throw new NullPointerException("NARG");
		
		ExpandedToken token = __t.next();
		TokenType type = token.type();
		
		// Is this an annotation?
		boolean isannotation = false;
		if (type == TokenType.SYMBOL_AT)
		{
			isannotation = true;
			
			// This should be interface
			token = __t.next();
			type = token.type();
		}
		
		// Determine class type
		ClassType classtype;
		switch (type)
		{
				// Class
			case KEYWORD_CLASS:
				// {@squirreljme.error AQ2e Cannot have an annotation
				// definition that is a class. (The token)}
				if (isannotation)
					throw new LayoutParserException(token,
						String.format("AQ2e %s", token));
				
				classtype = ClassType.CLASS;
				break;
			
				// Interface
			case KEYWORD_INTERFACE:
				if (isannotation)
					classtype = ClassType.ANNOTATION;
				else
					classtype = ClassType.INTERFACE;
				break;
			
				// Enumeration
			case KEYWORD_ENUM:
				// {@squirreljme.error AQ2f Cannot have an annotation
				// definition that is an enumeration. (The token)}
				if (isannotation)
					throw new LayoutParserException(token,
						String.format("AQ2f %s", token));
				
				classtype = ClassType.ENUM;
				break;
			
				// {@squirreljme.error AQ2d Unknown token when parsing the
				// class type. (The token)}
			default:
				throw new LayoutParserException(token,
					String.format("AQ2d %s", token));
		}
		
		// For potential read of generic
		token = __t.next();
		type = token.type();
		
		// Read generic arguments to the class, if applicable
		if (type == TokenType.COMPARE_LESS_THAN)
		{
			if (true)
				throw new todo.TODO();
		}
		
		// {@squirreljme.error AQ2g Expected identifier for the class name.
		// (The token)}
		if (type != TokenType.IDENTIFIER)
			throw new LayoutParserException(token,
				String.format("AQ2g %s", token));
		ClassIdentifier name = new ClassIdentifier(token.characters());
		
		// Could be extends, implements, or opening brace
		token = __t.next();
		type = token.type();
		
		// Read extends
		Set<GenericBinaryName> cextends = new LinkedHashSet<>();
		if (type == TokenType.KEYWORD_EXTENDS)
			for (;;)
			{
				// Parse name
				cextends.add(LayoutParserUtils.readGenericBinaryName(__t));
				
				// Read next
				token = __t.next(); 
				type = token.type();
				
				// Stop at implements and opening brace
				if (type == TokenType.KEYWORD_IMPLEMENTS ||
					type == TokenType.SYMBOL_OPEN_BRACE)
					break;
				
				// Commas are okay
				else if (type == TokenType.SYMBOL_COMMA)
					continue;
				
				// {@squirreljme.error AQ2n Expected implements, comma, or
				// opening brace to follow extends. (The token)}
				else
					throw new LayoutParserException(token,
						String.format("AQ2n %s", token));
			}
		
		// Read implements
		Set<GenericBinaryName> cimplements = new LinkedHashSet<>();
		if (type == TokenType.KEYWORD_IMPLEMENTS)
		{
			if (true)
				throw new todo.TODO();
		}
		
		// {@squirreljme.error AQ2h Expected opening brace after start of
		// class declaration. (The token)}
		if (type != TokenType.SYMBOL_OPEN_BRACE)
			throw new LayoutParserException(token,
				String.format("AQ2h %s", token));
		
		// Parse the members of the class
		ClassMemberLayout[] members;
		try (ExpandingSource src = new ExpandingStacker(
			LayoutParserUtils.readGroup(token, __t)))
		{
			members = ClassContainerLayout.__parseMembers(classtype, src);
		}
		
		throw new todo.TODO();
	}
	
	/**
	 * Parses the layout of a single member within the class.
	 *
	 * @param __t The source tokens to read from.
	 * @return The read member.
	 * @throws LayoutParserException If the member could not be read.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/04/08
	 */
	private static final ClassMemberLayout __parseMember(ExpandingSource __t)
		throws LayoutParserException, IOException, NullPointerException
	{
		if (__t  == null)
			throw new NullPointerException("NARG");
		
		// Read the modifiers for the member
		Modifiers modifiers = Modifiers.parse(__t);
		
		ExpandedToken token = __t.next();
		TokenType type = token.type();
		
		// Member class type
		if (type == TokenType.KEYWORD_CLASS ||
			type == TokenType.KEYWORD_ENUM ||
			type == TokenType.KEYWORD_INTERFACE ||
			type == TokenType.SYMBOL_AT)
			return ClassContainerLayout.__parse(modifiers, __t);
		
		throw new todo.TODO();
	}
	
	/**
	 * Parses the members of a class, this is needed for enumeration
	 * elements and additionally for anonymous classes defined within methods.
	 *
	 * @param __ct The type of class to parse.
	 * @param __t The input token source.
	 * @throws LayoutParserException If the member layout is not valid.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/04/08
	 */
	private static final ClassMemberLayout[] __parseMembers(ClassType __ct,
		ExpandingSource __t)
		throws LayoutParserException, IOException, NullPointerException
	{
		if (__ct == null || __t == null)
			throw new NullPointerException("NARG");
		
		ExpandedToken token;
		
		// {@squirreljme.error AQ2k Expected opening brace at start of
		// class. (The token)}
		token = __t.next();
		if (token.type() != TokenType.SYMBOL_OPEN_BRACE)
			throw new LayoutParserException(token,
				String.format("AQ2k %s", token));
		
		// Read enumeration members
		if (__ct == ClassType.ENUM)
		{
			throw new todo.TODO();
		}
		
		// Member reading loop
		List<ClassMemberLayout> members = new ArrayList<>();
		for (;;)
		{
			// No more members to read?
			token = __t.peek();
			if (token.type() == TokenType.SYMBOL_CLOSED_BRACE)
			{
				__t.next();
				break;
			}
			
			// Read single member
			members.add(ClassContainerLayout.__parseMember(__t));
		}
		
		// {@squirreljme.error AQ2m Expected a closing brace at the end
		// of a class declaration. (The token)}
		token = __t.next();
		if (token.type() != TokenType.SYMBOL_CLOSED_BRACE)
			throw new LayoutParserException(token,
				String.format("AQ2m %s", token));
		
		// {@squirreljme.error AQ2l Expected no more tokens to follow
		// the class declaration. (The read token)}
		token = __t.next();
		if (token.type() != TokenType.END_OF_FILE)
			throw new LayoutParserException(token,
				String.format("AQ2l %s", token));
		
		return members.<ClassMemberLayout>toArray(
			new ClassMemberLayout[members.size()]);
	}
}

