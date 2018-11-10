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
import net.multiphasicapps.classfile.BinaryName;
import net.multiphasicapps.collections.UnmodifiableArrayList;
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
public final class CompilationUnitSyntax
{
	/** The modifiers for the package. */
	protected final ModifiersSyntax modifiers;
	
	/** The package this compilation unit is in. */
	protected final QualifiedIdentifierSyntax inpackage;
	
	/** The imports being performed. */
	private final ImportStatementSyntax[] _imports;
	
	/** The classes being declared. */
	private final ClassSyntax[] _classes;
	
	/**
	 * Initializes the compilation unit.
	 *
	 * @param __pmod The modifiers to the package.
	 * @param __pk The owning package.
	 * @throws NullPointerException On null arguments.
	 * @throws SyntaxDefinitionException If the compilation unit is not
	 * valid.
	 * @since 2018/04/30
	 */
	public CompilationUnitSyntax(ModifiersSyntax __pmod,
		QualifiedIdentifierSyntax __pk)
		throws NullPointerException, SyntaxDefinitionException
	{
		if (__pmod == null || __pk == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error AQ1q Only annotation are valid modifiers for
		// packages.}
		if (__pmod.isPublic() || __pmod.isProtected() || __pmod.isPrivate() ||
			__pmod.isStatic() || __pmod.isAbstract() || __pmod.isFinal() ||
			__pmod.isNative() || __pmod.isSynchronized() ||
			__pmod.isTransient() || __pmod.isVolatile() ||
			__pmod.isStrictFloatingPoint())
			throw new SyntaxDefinitionException(
				String.format("AQ1q %s", __pmod));
		
		this.modifiers = __pmod;
		this.inpackage = __pk;
		this._imports = new ImportStatementSyntax[0];
		this._classes = new ClassSyntax[0];
	}
	
	/**
	 * Initializes the compilation unit.
	 *
	 * @param __pk The owning package.
	 * @param __imports Imports that are used.
	 * @param __classes Classes which have been declared.
	 * @throws NullPointerException On null arguments.
	 * @throws SyntaxDefinitionException If the compilation unit is not
	 * valid.
	 * @since 2018/04/30
	 */
	public CompilationUnitSyntax(QualifiedIdentifierSyntax __pk,
		ImportStatementSyntax[] __imports, ClassSyntax[] __classes)
		throws NullPointerException, SyntaxDefinitionException
	{
		this(__pk, Arrays.<ImportStatementSyntax>asList(
			__imports == null ? new ImportStatementSyntax[0] : __imports),
			Arrays.<ClassSyntax>asList(__classes == null ?
			new ClassSyntax[0] : __classes));
	}
	
	/**
	 * Initializes the compilation unit.
	 *
	 * @param __pk The owning package.
	 * @param __imports Imports that are used.
	 * @param __classes Classes which have been declared.
	 * @throws NullPointerException On null arguments, except for {@code __pk}.
	 * @throws SyntaxDefinitionException If the compilation unit is not
	 * valid.
	 * @since 2018/04/30
	 */
	public CompilationUnitSyntax(QualifiedIdentifierSyntax __pk,
		Iterable<ImportStatementSyntax> __imports,
		Iterable<ClassSyntax> __classes)
		throws NullPointerException, SyntaxDefinitionException
	{
		if (__imports == null || __classes == null)
			throw new NullPointerException("NARG");
		
		// Check imports
		Set<ImportStatementSyntax> imports = new LinkedHashSet<>();
		for (ImportStatementSyntax v : __imports)
		{
			if (v == null)
				throw new NullPointerException("NARG");
			
			imports.add(v);
		}
		
		// Check classes
		List<ClassSyntax> classes = new ArrayList<>();
		for (ClassSyntax v : __classes)
		{
			if (v == null)
				throw new NullPointerException("NARG");
			
			// {@squirreljme.error AQ1r Classes contained within a compilation
			// unit cannot be static, protected, or private. (The modifiers)}
			ModifiersSyntax modifiers = v.modifiers();
			if (modifiers.isStatic() || modifiers.isProtected() ||
				modifiers.isPrivate())
				throw new SyntaxDefinitionException(
					String.format("AQ1r %s", modifiers));
			
			// Is okay to use
			classes.add(v);
		}
		
		this.modifiers = new ModifiersSyntax();
		this.inpackage = __pk;
		this._imports = imports.<ImportStatementSyntax>toArray(
			new ImportStatementSyntax[imports.size()]);
		this._classes = classes.<ClassSyntax>toArray(
			new ClassSyntax[classes.size()]);
	}
	
	/**
	 * Returns the classes which have been defined.
	 *
	 * @return The defined classes.
	 * @since 2018/05/07
	 */
	public final List<ClassSyntax> classes()
	{
		return UnmodifiableArrayList.<ClassSyntax>of(this._classes);
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
	 * Returns the package that this compilation unit is within.
	 *
	 * @return The package of the compilation unit or {@code null} if it is
	 * not in one.
	 * @since 2018/05/07
	 */
	public final BinaryName inPackage()
	{
		QualifiedIdentifierSyntax inpackage = this.inpackage;
		return (inpackage == null ? null : inpackage.name());
	}
	
	/**
	 * Returns the modifiers used for the package.
	 *
	 * @return The package modifiers.
	 * @since 2018/05/07
	 */
	public final ModifiersSyntax packageModifiers()
	{
		return this.modifiers;
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
	 * @throws SyntaxParseException If the syntax is not valid.
	 * @since 2018/04/21
	 */
	public static CompilationUnitSyntax parse(BufferedTokenSource __in)
		throws NullPointerException, SyntaxParseException
	{
		if (__in == null)
			throw new NullPointerException("NARG");
		
		// This may be set early for class parse
		ModifiersSyntax modifiers = null;
		
		// The package the class is in, if it is in one
		QualifiedIdentifierSyntax inpackage = null;
		
		// This may be a package-info file which contains annotations
		// associated with a package
		// Detect annotation types here in the event they have no modifiers
		Token token = __in.peek();
		if (token.type() == TokenType.SYMBOL_AT &&
			__in.peek(1).type() != TokenType.KEYWORD_INTERFACE)
			modifiers = ModifiersSyntax.parse(__in);
		
		// Read in the package statement, if it is there
		token = __in.peek();
		if (token.type() == TokenType.KEYWORD_PACKAGE)
		{
			// Consume package
			__in.next();
			
			// Read package declaration
			inpackage = QualifiedIdentifierSyntax.parse(__in);
			
			// {@squirreljme.error AQ1s Expected semi-colon to follow the
			// package statement.}
			token = __in.next();
			if (token.type() != TokenType.SYMBOL_SEMICOLON)
				throw new SyntaxParseException(token, "AQ1s");
			
			// Only semi-colons and EOF may follow
			if (modifiers != null)
			{
				// {@squirreljme.error AQ1t Expected end of file or semicolons
				// after an annotated package statement, annotated packages
				// are only valid in package-info.java.}
				while ((token = __in.next()).type() != TokenType.END_OF_FILE)
					if (token.type() != TokenType.SYMBOL_SEMICOLON)
						throw new SyntaxParseException(token, "AQ1t");
				
				return new CompilationUnitSyntax(modifiers, inpackage);
			}
		}
		
		// Read import statements but if there are modifiers then a class
		// must directly follow
		Set<ImportStatementSyntax> imports = new LinkedHashSet<>();
		if (modifiers == null)
			for (;;)
			{
				// Stop when there are no more imports
				token = __in.peek();
				if (token.type() != TokenType.KEYWORD_IMPORT)
					break;
				
				// Parse import statement
				imports.add(ImportStatementSyntax.parse(__in));
			}
		
		// Read in classes
		List<ClassSyntax> classes = new ArrayList<>();
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
				modifiers = ModifiersSyntax.parse(__in);
			}
			
			// Read entire class container
			ClassSyntax struct = ClassSyntax.parseEntireClass(modifiers,
				__in);
			classes.add(struct);
			
			// Clear modifiers so that they are not used again
			modifiers = null;
		}
		
		return new CompilationUnitSyntax(inpackage, imports, classes);
	}
}

