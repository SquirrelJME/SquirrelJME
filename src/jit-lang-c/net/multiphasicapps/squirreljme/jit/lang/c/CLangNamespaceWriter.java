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
	
	/** Second level global header which is written last. */
	private final ByteArrayOutputStream _nshout =
		new ByteArrayOutputStream();
	
	/** Support source code. */
	private final ByteArrayOutputStream _nscout =
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
		}
		
		// {@squirreljme.error BA05 Could not initialize the global header
		// and/oor namespace source printer.}
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
		this.idprefix = escapeToCIdentifier(__ns) + "___";
		
		// Header guards for the namespace header
		PrintStream namespaceheader = this.namespaceheader;
		String hguard = headerGuard();
		namespaceheader.print("#ifndef ");
		namespaceheader.println(hguard);
		namespaceheader.print("#define ");
		namespaceheader.println(hguard);
		
		// Need to copy the global header to the output ZIP, otherwise the
		// code will just end up not compiling at all.
		ZipStreamWriter zipwriter = this.zipwriter;
		try (OutputStream en = zipwriter.nextEntry("squirrel.h",
			ZipCompressionType.DEFAULT_COMPRESSION);
			InputStream gh = this.getClass().getResourceAsStream("squirrel.h"))
		{
			// {@squirreljme.error BA03 Could not locate the global SquirrelJME
			// header file.}
			if (gh == null)
				throw new IOException("BA03");
			
			// Copy all of the data
			byte[] buf = new byte[64];
			for (;;)
			{
				// Read
				int rc = gh.read(buf);
				
				// EOF?
				if (rc < 0)
					break;
				
				// Write
				en.write(buf, 0, rc);
			}
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
		
		// Convert string to identifier
		String idof = "STRING_" + identifierPrefix() +
			escapeToCIdentifier(__s);
		
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
			try
			{
				// Close it
				this._closed = true;
				
				// Get streams
				PrintStream namespaceheader = this.namespaceheader;
				PrintStream namespacesource = this.namespacesource;
				
				// End header with header guard
				namespaceheader.println("#endif");
				namespaceheader.println();
			
				// Flush before close
				namespaceheader.flush();
				namespacesource.flush();
			
				// Write output of the byte array to the ZIP
				ByteArrayOutputStream baos = this._nshout,
					caos = this._nscout;
				try (OutputStream en = zipwriter.nextEntry(
						identifierPrefix() + ".h",
						ZipCompressionType.DEFAULT_COMPRESSION);
					OutputStream sn = zipwriter.nextEntry(
						identifierPrefix() + ".c",
						ZipCompressionType.DEFAULT_COMPRESSION))
				{
					baos.writeTo(en);
					caos.writeTo(sn);
				}
			
				// Close it
				namespaceheader.close();
				namespacesource.close();
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
		PrintStream __ps)
		throws NullPointerException
	{
		// Check
		if (__rname == null || __ps == null)
			throw new NullPointerException("NARG");
		
		// Create
		return new CResourceOutputStream(this, __rname, __ps);
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


