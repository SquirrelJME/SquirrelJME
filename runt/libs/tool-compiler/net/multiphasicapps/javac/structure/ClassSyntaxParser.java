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
import java.util.List;
import net.multiphasicapps.javac.syntax.ClassConstructorSyntax;
import net.multiphasicapps.javac.syntax.ClassSyntax;
import net.multiphasicapps.javac.syntax.MemberSyntax;
import net.multiphasicapps.javac.syntax.QualifiedIdentifierSyntax;
import net.multiphasicapps.javac.syntax.TypeParametersSyntax;
import net.multiphasicapps.javac.syntax.TypeParameterSyntax;
import net.multiphasicapps.javac.syntax.TypeSyntax;

/**
 * This class parses individual classes.
 *
 * @since 2018/05/08
 */
@Deprecated
public final class ClassSyntaxParser
	implements Runnable
{
	/** The parent class, may be {@code null}. */
	protected final ClassSyntax parent;
	
	/** The current class. */
	protected final ClassSyntax current;
	
	/** The run-time input. */
	protected final RuntimeInput runtime;
	
	/** The name lookup for this class. */
	protected final ClassNameLookup namelookup;
	
	/**
	 * Initializes the class syntax parser.
	 *
	 * @param __parent The parent class, may be {@code null}.
	 * @param __cur The current class being parsed.
	 * @param __pnl The parent name lookup.
	 * @param __ri The runtime input.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/05/08
	 */
	public ClassSyntaxParser(ClassSyntax __parent, ClassSyntax __cur,
		NameLookup __pnl, RuntimeInput __ri)
		throws NullPointerException
	{
		if (__cur == null || __pnl == null || __ri == null)
			throw new NullPointerException("NARG");
		
		this.parent = __parent;
		this.current = __cur;
		this.runtime = __ri;
		this.namelookup = new ClassNameLookup(__pnl, __cur);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/05/08
	 */
	@Override
	public final void run()
	{
		ClassSyntax current = this.current;
		ClassNameLookup namelookup = this.namelookup;
		RuntimeInput runtime = this.runtime;
		
		// Handle extends of the class
		for (TypeSyntax t : current.extending())
			throw new todo.TODO();
		
		// Handle implements of the class
		for (TypeSyntax t : current.implementing())
			throw new todo.TODO();
		
		// Handle class members
		List<ClassMemberStructure> members = new ArrayList<>();
		for (MemberSyntax m : current.members())
		{
			// Is just another class to be parsed
			if (m instanceof ClassSyntax)
			{
				ClassSyntax xm = (ClassSyntax)m;
				
				// Run that class through the processor
				new ClassSyntaxParser(current, xm,
					new ClassNameLookup(namelookup, xm), runtime).run();
				
				throw new todo.TODO();
			}
			
			// Class constructor
			else if (m instanceof ClassConstructorSyntax)
				members.add(this.__parseConstructor(
					(ClassConstructorSyntax)m));
			
			// Unhandled member type
			else
				throw new RuntimeException(String.format("OOPS %s",
					m.getClass()));
		}
		
		// Build class structure
		throw new todo.TODO();
	}
	
	/**
	 * Parses the class constructor syntax and returns a structure for the
	 * class constructor.
	 *
	 * @param __syn The input syntax.
	 * @return The resulting structure.
	 * @throws NullPointerException On null arguments.
	 * @throws StructureException If the structure is not valid.
	 * @since 2018/05/08
	 */
	private final ClassConstructorStructure __parseConstructor(
		ClassConstructorSyntax __syn)
		throws NullPointerException, StructureException
	{
		if (__syn == null)
			throw new NullPointerException("NARG");
		
		ClassSyntax current = this.current;
		ClassNameLookup namelookup = this.namelookup;
		
		// Parse type parameters
		TypeParameters typeparms = TypeParameters.parseSyntax(
			__syn.typeParameters(), namelookup);
		
		// Setup lookup with type parameters because arguments and throwables
		// will need to know the types and such
		TypeParametersNameLookup tpnl = new TypeParametersNameLookup(
			typeparms, namelookup);
		
		// Parse formal parameters and perform name lookup
		FormalParameters fparms = FormalParameters.parseSyntax(
			__syn.formalParameters(), tpnl);
		
		// Parse thrown types
		List<TypeSymbol> thrown = new ArrayList<>();
		for (QualifiedIdentifierSyntax qi : __syn.thrownTypes())
			thrown.add(namelookup.lookupType(new TypeSyntax(qi)));
		
		// Construct class symbol
		return new ClassConstructorStructure(StructureModifiers.parse(
			__syn.modifiers(), namelookup), typeparms, fparms, thrown,
			__syn.code());
	}
}

