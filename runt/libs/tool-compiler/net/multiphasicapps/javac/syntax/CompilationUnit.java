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
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import net.multiphasicapps.javac.token.BufferedTokenSource;
import net.multiphasicapps.javac.token.Token;
import net.multiphasicapps.javac.token.TokenSource;
import net.multiphasicapps.javac.token.TokenType;

/**
 * This represents the compilation unit which contains the package, the
 * import statements, and any declared classes.
 *
 * @since 2018/04/21
 */
public final class CompilationUnit
{
	/** The modifiers for the package. */
	protected final Modifiers modifiers;
	
	/** The package this compilation unit is in. */
	protected final QualifiedIdentifier inpackage;
	
	/** The imports being performed. */
	private final ImportStatement[] _imports;
	
	/** The classes being declared. */
	private final ClassStructure[] _classes;
	
	/**
	 * Initializes the compilation unit.
	 *
	 * @param __pmod The modifiers to the package.
	 * @param __pk The owning package.
	 * @throws NullPointerException On null arguments.
	 * @throws StructureDefinitionException If the compilation unit is not
	 * valid.
	 * @since 2018/04/30
	 */
	public CompilationUnit(Modifiers __pmod, QualifiedIdentifier __pk)
		throws NullPointerException, StructureDefinitionException
	{
		if (__pmod == null || __pk == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error AQ4u Only annotation are valid modifiers for
		// packages.}
		if (__pmod.isPublic() || __pmod.isProtected() || __pmod.isPrivate() ||
			__pmod.isStatic() || __pmod.isAbstract() || __pmod.isFinal() ||
			__pmod.isNative() || __pmod.isSynchronized() ||
			__pmod.isTransient() || __pmod.isVolatile() ||
			__pmod.isStrictFloatingPoint())
			throw new StructureDefinitionException(
				String.format("AQ4u %s", __pmod));
		
		this.modifiers = __pmod;
		this.inpackage = __pk;
		this._imports = new ImportStatement[0];
		this._classes = new ClassStructure[0];
	}
	
	/**
	 * Initializes the compilation unit.
	 *
	 * @param __pk The owning package.
	 * @param __imports Imports that are used.
	 * @param __classes Classes which have been declared.
	 * @throws NullPointerException On null arguments.
	 * @throws StructureDefinitionException If the compilation unit is not
	 * valid.
	 * @since 2018/04/30
	 */
	public CompilationUnit(QualifiedIdentifier __pk,
		ImportStatement[] __imports, ClassStructure[] __classes)
		throws NullPointerException, StructureDefinitionException
	{
		this(__pk, Arrays.<ImportStatement>asList(
			__imports == null ? new ImportStatement[0] : __imports),
			Arrays.<ClassStructure>asList(__classes == null ?
			new ClassStructure[0] : __classes));
	}
	
	/**
	 * Initializes the compilation unit.
	 *
	 * @param __pk The owning package.
	 * @param __imports Imports that are used.
	 * @param __classes Classes which have been declared.
	 * @throws NullPointerException On null arguments.
	 * @throws StructureDefinitionException If the compilation unit is not
	 * valid.
	 * @since 2018/04/30
	 */
	public CompilationUnit(QualifiedIdentifier __pk,
		Iterable<ImportStatement> __imports,
		Iterable<ClassStructure> __classes)
		throws NullPointerException, StructureDefinitionException
	{
		if (__pk == null || __imports == null || __classes == null)
			throw new NullPointerException("NARG");
		
		// Check imports
		Set<ImportStatement> imports = new LinkedHashSet<>();
		for (ImportStatement v : __imports)
		{
			if (v == null)
				throw new NullPointerException("NARG");
			
			imports.add(v);
		}
		
		// Check classes
		Set<ClassStructure> classes = new LinkedHashSet<>();
		for (ClassStructure v : __classes)
		{
			if (v == null)
				throw new NullPointerException("NARG");
			
			classes.add(v);
		}
		
		this.modifiers = new Modifiers();
		this.inpackage = __pk;
		this._imports = imports.<ImportStatement>toArray(
			new ImportStatement[imports.size()]);
		this._classes = classes.<ClassStructure>toArray(
			new ClassStructure[classes.size()]);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/04/21
	 */
	@Override
	public final boolean equals(Object __o)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/04/21
	 */
	@Override
	public final int hashCode()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/04/21
	 */
	@Override
	public final String toString()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Parses the compilation unit of the class file.
	 *
	 * @param __in The input token source.
	 * @return The parsed compilation unit.
	 * @throws NullPointerException On null arguments.
	 * @throws StructureParseException If the structure is not valid.
	 * @since 2018/04/21
	 */
	public static CompilationUnit parse(BufferedTokenSource __in)
		throws NullPointerException, StructureParseException
	{
		if (__in == null)
			throw new NullPointerException("NARG");
		
		// This may be set early for class parse
		Modifiers modifiers = null;
		
		// The package the class is in, if it is in one
		QualifiedIdentifier inpackage = null;
		
		// This may be a package-info file which contains annotations
		// associated with a package
		// Detect annotation types here in the event they have no modifiers
		Token token = __in.peek();
		if (token.type() == TokenType.SYMBOL_AT &&
			__in.peek(1).type() != TokenType.KEYWORD_INTERFACE)
			modifiers = Modifiers.parse(__in);
		
		// Read in the package statement, if it is there
		token = __in.peek();
		if (token.type() == TokenType.KEYWORD_PACKAGE)
		{
			// Consume package
			__in.next();
			
			// Read package declaration
			inpackage = QualifiedIdentifier.parse(__in);
			
			// {@squirreljme.error AQ3j Expected semi-colon to follow the
			// package statement.}
			token = __in.next();
			if (token.type() != TokenType.SYMBOL_SEMICOLON)
				throw new StructureParseException(token, "AQ3j");
			
			// Only semi-colons and EOF may follow
			if (modifiers != null)
			{
				// {@squirreljme.error AQ3f Expected end of file or semicolons
				// after an annotated package statement, annotated packages
				// are only valid in package-info.java.}
				while ((token = __in.next()).type() != TokenType.END_OF_FILE)
					if (token.type() != TokenType.SYMBOL_SEMICOLON)
						throw new StructureParseException(token, "AQ3f");
				
				return new CompilationUnit(modifiers, inpackage);
			}
		}
		
		// Read import statements but if there are modifiers then a class
		// must directly follow
		Set<ImportStatement> imports = new LinkedHashSet<>();
		if (modifiers == null)
			for (;;)
			{
				// Stop when there are no more imports
				token = __in.peek();
				if (token.type() != TokenType.KEYWORD_IMPORT)
					break;
				
				// Parse import statement
				imports.add(ImportStatement.parse(__in));
			}
		
		// Read in classes
		List<ClassStructure> classes = new ArrayList<>();
		for (;;)
		{
			// Need to read in the class modifiers
			if (modifiers == null)
			{
				// Ignore semi-colons
				token = __in.peek();
				if (token.type() == TokenType.SYMBOL_SEMICOLON)
					continue;
				
				// But stop parsing on EOF
				else if (token.type() == TokenType.END_OF_FILE)
					break;
				
				// Parse modifiers
				modifiers = Modifiers.parse(__in);
			}
			
			// Read entire class container
			ClassStructure struct = ClassStructure.parseEntireClass(modifiers,
				__in);
			classes.add(struct);
			
			// Clear modifiers so that they are not used again
			modifiers = null;
		}
		
		return new CompilationUnit(inpackage, imports, classes);
	}
}

