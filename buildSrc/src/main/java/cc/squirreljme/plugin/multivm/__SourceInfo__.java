// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.multivm;

import java.io.IOException;
import java.io.InputStream;
import java.io.StreamTokenizer;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

/**
 * Information on a source code file.
 *
 * @since 2020/10/09
 */
final class __SourceInfo__
{
	/** The package this source is in. */
	public final String inPackage;
	
	/** The name of the current class. */
	public final String thisClass;
	
	/** The super-class of this one, if one is set. */
	public final String superClass;
	
	/**
	 * Initializes the source information.
	 * 
	 * @param __inPackage The package this source is in.
	 * @param __thisClass The current class.
	 * @param __superClass The super class, may be {@code null}.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/10/09
	 */
	__SourceInfo__(String __inPackage, String __thisClass, String __superClass)
		throws NullPointerException
	{
		if (__inPackage == null || __thisClass == null)
			throw new NullPointerException("NARG");
		
		this.inPackage = __inPackage;
		this.thisClass = __thisClass;
		this.superClass = __superClass;
	}
	
	/**
	 * Loads information gleaned from source code in Jasmin.
	 * 
	 * @param __in The file to read from.
	 * @return The information on the class.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/10/10
	 */
	public static __SourceInfo__ loadJasmin(InputStream __in)
		throws IOException, NullPointerException
	{
		if (__in == null)
			throw new NullPointerException("NARG");
		
		StreamTokenizer stream = new StreamTokenizer(__in);
		
		// Jasmin files, do not use standard syntax so clear everything
		stream.resetSyntax();
		
		// Set these specifically
		stream.commentChar(';');
		stream.wordChars('.', '.');
		stream.wordChars('/', '/');
		stream.wordChars('$', '$');
		stream.wordChars('a', 'z');
		stream.wordChars('A', 'Z');
		stream.wordChars('0', '9');
		stream.wordChars('_', '_');
		stream.eolIsSignificant(true);
		stream.quoteChar('"');
		stream.quoteChar('\'');
		stream.whitespaceChars(0, ' ');
		
		// Current state
		String thisClass = null;
		String superClass = null;
		
		// Parse tokens
		Deque<String> queue = new LinkedList<>();
		for (;;)
		{
			int type = stream.nextToken();
			
			// End of file or line, will be a statement
			if (type == StreamTokenizer.TT_EOL ||
				type == StreamTokenizer.TT_EOF)
			{
				String first = queue.pollFirst();
				
				// Declares current class?
				if (".class".equals(first))
				{
					// Ignore any access specifiers before the class name
					String second;
					do
					{
						second = queue.pollFirst();
					} while (second != null &&
						__SourceInfo__.__isAccessSpec(second));
					
					// The class name just follows these
					if (second != null)
						thisClass = second; 
				}
				
				// Declares super class?
				else if (".super".equals(first))
				{
					// The super class just follows this
					String second = queue.pollFirst();
					if (second != null)
						superClass = second;
				}
				
				// Stop on EOF of if we found both our class and super-class,
				// the order could be swapped so we can only reliably stop on
				// both conditions being true
				if ((thisClass != null && superClass != null) ||
					type == StreamTokenizer.TT_EOF)
					break;
				
				// Clear the queue for the next run
				queue.clear();
			}
			
			// Push these to the queue
			else if (type == StreamTokenizer.TT_NUMBER)
				queue.addLast(Double.toString(stream.nval));
			else if (type == StreamTokenizer.TT_WORD)
				queue.addLast(stream.sval);
			
			// .class public foo/bar
			// .super foo/bar
		}
		
		// This should not happen, unless the source is malformed
		if (thisClass == null)
			throw new IOException("Jasmin class has no name?");
		
		// Determine the package we are in, which is just the package of our
		// binary class name
		int ls = thisClass.lastIndexOf('/');
		String inPackage = (ls < 0 ? "" :
			thisClass.substring(0, ls).replace('/', '.'));
		
		// Normalize to Java forms, as these all use binary names
		return new __SourceInfo__(inPackage,
			thisClass.replace('/', '.'),
			(superClass == null ? null :
				superClass.replace('/', '.')));
	}
	
	/**
	 * Loads information gleaned from source code in Java.
	 * 
	 * @param __in The file to read from.
	 * @return The information on the class.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/10/09
	 */
	public static __SourceInfo__ loadJava(InputStream __in)
		throws IOException, NullPointerException
	{
		if (__in == null)
			throw new NullPointerException("NARG");
		
		StreamTokenizer stream = new StreamTokenizer(__in);
		
		// Initialize tokenizer parameters
		stream.slashStarComments(true);
		stream.slashSlashComments(true);
		stream.whitespaceChars(0, ' ');
		stream.parseNumbers();
		stream.wordChars('.', '.'); 
		stream.wordChars('_', '_');
		stream.wordChars('$', '$');
		stream.ordinaryChar('<');
		stream.ordinaryChar('>');
		stream.ordinaryChar('@');
		stream.ordinaryChar(';');
		stream.ordinaryChar('{');
		stream.ordinaryChar('}');
		
		// Parsed state
		String inPackage = null;
		String thisClass = null;
		String superClass = null;
		Map<String, String> imports = new HashMap<>();
		boolean foundClassName = false;
		
		// Parse tokens
		Deque<String> queue = new LinkedList<>();
		for (;;)
		{
			int type = stream.nextToken();
			
			// End of line or statement, curly braces are included since they
			// open the class file
			if (type == ';' || type == '{' || type == StreamTokenizer.TT_EOF)
			{
				boolean isPackage = false;
				boolean isImport = false;
				
				// Is this a package or import statement?
				String first = queue.pollFirst();
				if (first != null)
					switch (first)
					{
						case "package":
							isPackage = true;
							break;
						
						case "import":
							isImport = true;
							break;
					}
				
				// Package or import statement
				if (isPackage || isImport)
				{
					String second = __SourceInfo__.__glue(queue);
					
					// Declare package
					if (isPackage)
						inPackage = second;
					
					// Import statement that is not a static import
					else if (!queue.contains("static"))
					{
						// Determine the identifier this links to
						int ld = second.lastIndexOf('.');
						String ident = (ld < 0 ? second :
							second.substring(ld + 1));
						
						// Declare import, used for full name getting
						imports.put(ident, second);
					}
				}
				
				// Potential class statement?
				else if (type == '{' && queue.contains("class"))
				{
					// If no package was found, then this will be the default
					// package
					if (inPackage == null)
						inPackage = "";
					
					// It may or may not be here on this line
					String maybeClass = __SourceInfo__
						.__follow(queue, "class");
					String maybeExtend = __SourceInfo__
						.__follow(queue, "extends");
					
					// We did find the class name?
					if (maybeClass != null)
					{
						// Determine the name of the current class
						thisClass = (inPackage.isEmpty() ? maybeClass :
							inPackage + "." + maybeClass);
						
						// We found this, so we should be able to stop now
						foundClassName = true;
					}
					
					// We did find the super class?
					if (maybeExtend != null)
					{
						// Super class is from an import statement?
						String foundImport = imports.get(maybeExtend);
						if (foundImport != null)
							superClass = foundImport;
						
						// Otherwise assume it is in the same package as our
						// current class
						else
							superClass = (inPackage.isEmpty() ? maybeExtend :
								inPackage + "." + maybeExtend);
					}
				}
				
				// Stop on EOF or if we found the class we want
				if (foundClassName || type == StreamTokenizer.TT_EOF)
					break;
				
				// Clear the queue for the next run
				queue.clear();
			}
			
			// Push these to the queue
			else if (type == StreamTokenizer.TT_NUMBER)
				queue.addLast(Double.toString(stream.nval));
			else if (type == StreamTokenizer.TT_WORD)
				queue.addLast(stream.sval);
		}
		
		// This should not happen, unless the source is malformed
		if (inPackage == null || thisClass == null)
			throw new IOException("Java class has no package or name?");
		
		return new __SourceInfo__(inPackage, thisClass, superClass);
	}
	
	/**
	 * Attempts to locate the token that follows the given identifying token.
	 * 
	 * For example {@code __follow(["public", "class", "foo"], "class")} will
	 * return {@code "foo"}.
	 * 
	 * @param __seq The token sequence.
	 * @param __token The token to follow from.
	 * @return The following word.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/10/10
	 */
	private static String __follow(Iterable<String> __seq, String __token)
		throws NullPointerException
	{
		if (__seq == null || __token == null)
			throw new NullPointerException("NARG");
		
		// Try to discover a match
		for (Iterator<String> it = __seq.iterator(); it.hasNext();)
		{
			// Did we match our token?
			String at = it.next();
			if (!__token.equals(at))
				continue;
			
			// Return the follower if it is a valid identifier
			String follower = it.next();
			if (follower != null && __SourceInfo__.__isJavaWord(follower))
				return follower;
		}
		
		// No match found
		return null;
	}
	
	/**
	 * Glues the given strings together.
	 * 
	 * @param __strings The strings to glue together.
	 * @return The joined together strings.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/10/10
	 */
	private static String __glue(Iterable<String> __strings)
		throws NullPointerException
	{
		if (__strings == null)
			throw new NullPointerException("NARG");
		
		StringBuilder sb = new StringBuilder();
		for (String s : __strings)
			sb.append(s);
		
		return sb.toString();
	}
	
	/**
	 * Is this an access specifier? This is used by Jasmin to declare a class
	 * or method with special flag types.
	 * 
	 * @param __word The word to check.
	 * @return If this is an access specifier.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/10/10
	 */
	private static boolean __isAccessSpec(String __word)
		throws NullPointerException
	{
		if (__word == null)
			throw new NullPointerException("NARG");
		
		switch (__word)
		{
			case "public":
			case "private":
			case "protected":
			case "static":
			case "final":
			case "synchronized":
			case "volatile":
			case "transient":
			case "native":
			case "interface":
			case "abstract":
				return true;
		}
		
		return false;
	}
	
	/**
	 * Checks if this is a Java word or not.
	 * 
	 * @param __word The word to check.
	 * @return If this is a Java word or not.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/10/10
	 */
	private static boolean __isJavaWord(String __word)
		throws NullPointerException
	{
		if (__word == null)
			throw new NullPointerException("NARG");
		
		// Blank words are never words
		int n = __word.length();
		if (n == 0)
			return false;
		
		// Check for invalid characters
		for (int i = 0; i < n; i++)
		{
			char c = __word.charAt(i);
			if ((i == 0 && !Character.isJavaIdentifierStart(c)) ||
				(i > 0 && !Character.isJavaIdentifierPart(c)))
				return false;
		}
		
		// Did not fail, so must be a Java word
		return true;
	}
}
