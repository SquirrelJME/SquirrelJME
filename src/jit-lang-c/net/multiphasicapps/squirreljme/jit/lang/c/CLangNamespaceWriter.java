// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.lang.c;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.HashSet;
import java.util.Set;
import net.multiphasicapps.squirreljme.java.symbols.ClassNameSymbol;
import net.multiphasicapps.squirreljme.jit.JITException;
import net.multiphasicapps.squirreljme.jit.JITOutputConfig;
import net.multiphasicapps.squirreljme.jit.lang.LangClassWriter;
import net.multiphasicapps.squirreljme.jit.lang.LangNamespaceWriter;
import net.multiphasicapps.squirreljme.jit.lang.ResourceOutputStream;
import net.multiphasicapps.zip.ZipCompressionType;
import net.multiphasicapps.zip.streamwriter.ZipStreamWriter;

/**
 * This is a namespace writer which targets the C programming language.
 *
 * @since 2016/07/09
 */
public class CLangNamespaceWriter
	extends LangNamespaceWriter
{
	/** The prefix to identifiers. */
	protected final String idprefix;
	
	/** Print stream which writes to the global header. */
	protected final PrintStream namespaceheader;
	
	/** Print stream which writes to the namespace specific source file. */
	protected final PrintStream namespacesource;
	
	/** C Strings. */
	protected final PrintStream namespacestrings;
	
	/** The namespace base name. */
	protected final String nsbasename;
	
	/** Strings which have been declared. */
	private final Set<String> _usedstrings =
		new HashSet<>();
	
	/** Second level global header which is written last. */
	private final ByteArrayOutputStream _nshout =
		new ByteArrayOutputStream();
	
	/** Support source code. */
	private final ByteArrayOutputStream _nscout =
		new ByteArrayOutputStream();
	
	/** String output. */
	private final ByteArrayOutputStream _nssout =
		new ByteArrayOutputStream();
	
	/** Was this writer closed? */
	private volatile boolean _closed;
	
	/**
	 * Initializes the global header printer.
	 *
	 * @since 2016/07/18
	 */
	{
		// Wrap
		try
		{
			this.namespaceheader = new PrintStream(this._nshout, true,
				"utf-8");
			this.namespacesource = new PrintStream(this._nscout, true,
				"utf-8");
			this.namespacestrings = new PrintStream(this._nssout, true,
				"utf-8");
		}
		
		// {@squirreljme.error BA05 Could not initialize the global header
		// and/or namespace source printer.}
		catch (IOException e)
		{
			throw new JITException("BA05");
		}
	}
	
	/**
	 * The namespace writer for C code.
	 *
	 * @param __ns The namespace being written.
	 * @param __config The configuration used.
	 * @throws JITException If the namespace writer could not be initialized.
	 * @since 2016/07/09
	 */
	public CLangNamespaceWriter(String __ns,
		JITOutputConfig.Immutable __config)
		throws JITException
	{
		super(__ns, __config);
		
		// Prefix for identifiers
		String nsbasename = escapeToCIdentifier(__ns);
		this.nsbasename = nsbasename;
		String idprefix = nsbasename + "___";
		this.idprefix = idprefix;
		
		// Header guards for the namespace header
		PrintStream namespaceheader = this.namespaceheader;
		String hguard = headerGuard();
		namespaceheader.print("#ifndef ");
		namespaceheader.println(hguard);
		namespaceheader.print("#define ");
		namespaceheader.println(hguard);
		
		// Need to include the required headers for the strings and namespace
		PrintStream src = this.namespacesource;
		for (PrintStream ps : new PrintStream[]{this.namespacestrings, src})
		{
			ps.println("#include \"squirrel.h\"");
			ps.print("#include \"");
			ps.print(idprefix);
			ps.println(".h\"");
			ps.println();
		}
		
		// Write contents of the namespace
		src.print("const void* contents_");
		src.print(idprefix);
		src.println("[] = ");
		src.println("{");
		
		// Need to copy the global header to the output ZIP, otherwise the
		// code will just end up not compiling at all.
		try (InputStream ish =
			this.getClass().getResourceAsStream("squirrel.h");
			InputStream isc =
				this.getClass().getResourceAsStream("squirrel.c"))
		{
			includeFile("squirrel.h", ish);
			includeFile("squirrel.c", isc);
		}
		
		// {@squirreljme.error BA04 Error copying the global SquirrelJME
		// header to the output namespace.}
		catch (IOException e)
		{
			throw new JITException("BA04", e);
		}
	}
	
	/**
	 * Creates a structure for a given string and returns the identifier which
	 * points to the given string.
	 *
	 * @param __s The string to create a structure for.
	 * @return The macro which points to the string information.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/18
	 */
	public String addString(String __s)
		throws NullPointerException
	{
		// Check
		if (__s == null)
			throw new NullPointerException("NARG");
		
		// Split the string in half
		int n = __s.length();
		String left = __s.substring(0, n >>> 1),
			right = __s.substring(n >>> 1);
		
		// Convert string to identifier, use left side and right side hashcode
		// with their lengths
		String idof = "STRING_" + identifierPrefix() +
			"_l" + n + "_" + (((long)left.hashCode()) & 0xFFFFFFFFL) + "_" +
			(((long)right.hashCode()) & 0xFFFFFFFFL);
		
		// If the string was already output then do not output it again because
		// multiple declarations are in error.
		Set<String> usedstrings = this._usedstrings;
		if (usedstrings.contains(idof))
			return idof;
		usedstrings.add(idof);
		
		// Add string to the global header
		PrintStream namespaceheader = this.namespaceheader;
		namespaceheader.print("extern SJME_String ");
		namespaceheader.print(idof);
		namespaceheader.println(';');
		
		// Start type
		PrintStream namespacestrings = this.namespacestrings;
		namespacestrings.print("SJME_String ");
		namespacestrings.print(idof);
		namespacestrings.println(" =");
		namespacestrings.println("{");
		
		// Structure type
		namespacestrings.println("\tSJME_STRUCTURETYPE_STRING,");
		
		// Write size
		namespacestrings.print('\t');
		namespacestrings.print(n);
		namespacestrings.println(',');
		
		// Write characters
		namespacestrings.print("\t{");
		for (int i = 0; i < n; i++)
		{
			if (i > 0)
				namespacestrings.print(", ");
			namespacestrings.print((int)__s.charAt(i));
		}
		namespacestrings.println("}");
		
		// End type
		namespacestrings.println("};");
		
		// Return the string identifier
		return idof;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/18
	 */
	@Override
	public void close()
		throws JITException
	{
		// If not closed then write the global header and source
		if (!this._closed)
			try (PrintStream namespaceheader = this.namespaceheader;
				PrintStream namespacesource = this.namespacesource;
				PrintStream namespacestrings = this.namespacestrings)
			{
				// Close it
				this._closed = true;
				
				// End with header guard
				namespaceheader.println("#endif");
				namespaceheader.println();
				
				// End contents
				namespacesource.println("\tNULL");
				namespacesource.println("};");
				namespacesource.println();
				
				// Setup namespace declaration
				namespacesource.print("SJME_Namespace ");
				namespacesource.print(this.nsbasename);
				namespacesource.println(" =");
				namespacesource.println("{");
				namespacesource.println("\tSJME_STRUCTURETYPE_NAMESPACE,");
				namespacesource.print("\t&");
				namespacesource.print(addString(this.namespace));
				namespacesource.println(',');
				namespacesource.print("\tcontents_");
				namespacesource.print(this.idprefix);
				namespacesource.println(",");
				namespacesource.println("};");
				namespacesource.println();
				
				// Flush all output
				namespaceheader.flush();
				namespacesource.flush();
				namespacestrings.flush();
				
				// Write other files
				includeFile(identifierPrefix() + ".h", this._nshout);
				
				// Write source
				includeFile(identifierPrefix() + ".c", this._nscout);
				
				// Write strings
				includeFile("strings__" + identifierPrefix() + ".c",
					this._nssout);
			}
			
			// {@squirreljme.error BA06 Could not write the namespace header
			// and/or the namespace source to the output ZIP.}
			catch (IOException e)
			{
				throw new JITException("BA06", e);
			}
		
		// Call super close
		super.close();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/17
	 */
	@Override
	protected LangClassWriter createClassWriter(String __rname,
		ClassNameSymbol __cn, PrintStream __ps)
		throws NullPointerException
	{
		// Check
		if (__rname == null || __cn == null || __ps == null)
			throw new NullPointerException("NARG");
		
		return new CLangClassWriter(this, __rname, __cn, __ps);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/17
	 */
	@Override
	protected ResourceOutputStream createResourceOutputStream(String __rname,
		String __jname, PrintStream __ps)
		throws NullPointerException
	{
		// Check
		if (__rname == null || __jname == null || __ps == null)
			throw new NullPointerException("NARG");
		
		// Create
		return new CResourceOutputStream(this, __rname, __jname, __ps);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/17
	 */
	@Override
	public String extensionClass(String __n)
		throws NullPointerException
	{
		return super.extensionClass(__n) + ".c";
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/17
	 */
	@Override
	public String extensionResource(String __n)
		throws NullPointerException
	{
		return super.extensionResource(__n) + ".c";
	}
	
	/**
	 * Returns the header guard macro.
	 *
	 * @return The header guard macro.
	 * @since 2016/07/18
	 */
	public String headerGuard()
	{
		return identifierPrefix() + "__HEADER";
	}
	
	/**
	 * Returns the used identifier prefix.
	 *
	 * @return The identifier prefix.
	 * @since 2016/07/17
	 */
	public String identifierPrefix()
	{
		return this.idprefix;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/09
	 */
	@Override
	public String nameClass(ClassNameSymbol __name)
		throws NullPointerException
	{
		return this.idprefix + "C_" + escapeToCIdentifier(__name.toString());
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/09
	 */
	@Override
	public String nameResource(String __name)
		throws NullPointerException
	{
		return this.idprefix + "R_" + escapeToCIdentifier(__name);
	}
	
	/**
	 * This returns the namespace header printer.
	 *
	 * @return The namespace header printer.
	 * @since 2016/07/18
	 */
	public PrintStream namespaceHeader()
	{
		return this.namespaceheader;
	}
	
	/**
	 * This prints the global header includes to the target stream.
	 *
	 * @param __ps Stream to print to.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/17
	 */
	final void __globalHeader(PrintStream __ps)
		throws NullPointerException
	{
		// Check
		if (__ps == null)
			throw new NullPointerException("NARG");
		
		// Write out headers
		__ps.println("#include \"squirrel.h\"");
		__ps.print("#include \"");
		__ps.print(identifierPrefix());
		__ps.println(".h\"");
		__ps.println();
	}
	
	/**
	 * Escapes the given string so that it becomes a valid C identifier.
	 *
	 * @param __s The string to escape.
	 * @return An identifier safe string.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/17
	 */
	public static String escapeToCIdentifier(String __s)
		throws NullPointerException
	{
		// Check
		if (__s == null)
			throw new NullPointerException("NARG");
		
		// Setup
		StringBuilder sb = new StringBuilder();
		
		// Go through all characters
		int n = __s.length();
		for (int i = 0; i < n; i++)
		{
			char c = __s.charAt(i);
			
			// These are untouched
			if ((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z') ||
				(i > 0 && (c >= '0' && c <= '9')))
				sb.append(c);
			
			// Otherwise escape
			else
			{
				// Underscore acts as escape character
				sb.append('_');
				
				// If the character cannot fit within two base-32 characters
				// then use a wide sequence.
				boolean wide;
				if ((wide = (c > 1023)))
					sb.append('_');
				
				// Write upper bits?
				if (wide)
				{
					sb.append(Character.forDigit(((c >>> 15) & 0x1F), 32));
					sb.append(Character.forDigit(((c >>> 10) & 0x1F), 32));
				}
				
				// Lower two chars
				sb.append(Character.forDigit(((c >>> 5) & 0x1F), 32));
				sb.append(Character.forDigit((c & 0x1F), 32));
			}
		}
		
		// Done
		return sb.toString();
	}
}


