// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

import java.io.InputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.Set;
import java.util.TreeSet;

/**
 * This class goes through all projects and reorders the error codes so that
 * they appear in order. This program should really only be ran when error
 * codes are a complete mess after a refactor and they need to cleaned up.
 *
 * @since 2017/10/26
 */
public class ReorderErrors
{
	/** The error specifier. */
	private static final String _ERROR_SPECIFIER =
		"@squirreljme.error";
	
	/** The path to reorder errors for. */
	protected final Path path;
	
	/** The prefix for this project. */
	protected final String projectprefix;
	
	/** The next available error code. */
	private volatile Code _nextcode;
	
	/**
	 * Initializes the error reordering on the given path.
	 *
	 * @param __p The path to reorder.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/10/26
	 */
	public ReorderErrors(Path __p)
		throws IOException, NullPointerException
	{
		if (__p == null)
			throw new NullPointerException("NARG");
		
		this.path = __p;
		
		// Read in manifest
		Manifest man;
		try (InputStream in = Files.newInputStream(path.resolve("META-INF").
			resolve("MANIFEST.MF")))
		{
			man = new Manifest(in);
		}
		
		// Get the error prefix
		Attributes attr = man.getMainAttributes();
		String prefix = attr.getValue("X-SquirrelJME-Error");
		if (prefix == null)
			throw new IOException("No error prefix");
		String projectprefix = prefix.trim();
		this.projectprefix = projectprefix;
		
		// The initial code to work with
		this._nextcode = new Code(projectprefix, 1);
	}
	
	/**
	 * Processes everything.
	 *
	 * @throws IOException On read/write errors.
	 * @since 2017/10/26
	 */
	public void run()
		throws IOException
	{
		Path path = this.path;
		
		// Obtain Java source files
		Set<Path> files = new TreeSet<>();
		Files.walk(path).filter((__p) -> !Files.isDirectory(__p)).
			filter((__p) -> __p.toString().endsWith(".java")).
			forEach(files::add);
		
		// Go through all files since they must be parsed to recode versions
		for (Path file : files)
			__process(file);
	}
	
	/**
	 * Processes the given path.
	 *
	 * @param __p The file to process.
	 * @throws IOException On read/write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/01/15
	 */
	private void __process(Path __p)
		throws IOException, NullPointerException
	{
		if (__p == null)
			throw new NullPointerException("NARG");
		
		// Read in all bytes from the file since they will be managed and
		// handled specially depending on which error codes were found
		// Treat as string to extract all the error codes
		String sdata = new String(Files.readAllBytes(__p), "utf-8");
		
		// The first character of the prefix
		String projectprefix = this.projectprefix;
		char firstchar = projectprefix.charAt(0);
		
		// The output string
		StringBuilder out = new StringBuilder(sdata.length());
		
		// Go through all bytes to find sequences for replacing
		boolean replacedsomething = false;
		Code replaceme = new Code(projectprefix, 0),
			withthis = new Code(projectprefix, 0),
			nextcode = this._nextcode;
		char reallylastchar = 0;
		for (int i = 0, n = sdata.length(); i < n; i++)
		{
			char c = sdata.charAt(i),
				lastchar = reallylastchar;
			reallylastchar = c;
			
			// Potentially a code to be replaced
			if (i + 4 < n && c == firstchar &&
				!Character.isAlphabetic(lastchar) &&
				!Character.isDigit(lastchar) && lastchar != '_')
			{
				// Replacing code
				if (sdata.substring(i, i + 4).
					equalsIgnoreCase(replaceme.toString()))
				{
					replacedsomething = true;
					out.append(withthis.toString());
					i += 3;
				}
				
				// Copy as is
				else
					out.append(c);
			}
			
			// Declaration of a new prefix to replace
			else if (c == '@')
			{
				// This is an error declaration
				if (sdata.substring(i).startsWith(_ERROR_SPECIFIER))
				{
					// Ignore whitespace until the code
					int codepos = -1;
					for (int j = i + _ERROR_SPECIFIER.length(); j < n; j++)
					{
						// Ignore whitespace
						char d = sdata.charAt(j);
						if (Character.isWhitespace(d))
							continue;
						
						codepos = j;
						break;
					}
					
					// Is a valid code position
					if (codepos >= 0)
					{
						replaceme = new Code(
							sdata.substring(codepos, codepos + 4));
						withthis = nextcode;
						nextcode = nextcode.next();
					}
					
					// Always copy the character
					out.append(c);
				}
				
				// Not an error sequence
				else
					out.append(c);
			}
			
			// Unknown, just pass it through
			else
				out.append(c);
		}
		
		// Store next code for later
		this._nextcode = nextcode;
		
		// Rewrite to file
		if (replacedsomething)
		{
			if (false)
			{
				System.out.println("----------------------------------");
				System.out.printf("FILE: %s%n", __p);
				System.out.println(out);
			}
			else
				Files.write(__p, out.toString().getBytes("utf-8"),
					StandardOpenOption.WRITE,
					StandardOpenOption.TRUNCATE_EXISTING);
		}
	}
	
	/**
	 * Main entry point.
	 *
	 * @param __args Program arguments.
	 * @since 2017/10/26
	 */
	public static void main(String... __args)
	{
		if (__args == null)
			__args = new String[0];
		
		for (String a : __args)
			try
			{
				new ReorderErrors(Paths.get(a)).run();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
	}
	
	/**
	 * This represents a prefix and an error code.
	 *
	 * @since 2017/10/27
	 */
	public static final class Code
	{
		/** The prefix code. */
		protected final String prefix;
		
		/** The error code number. */
		protected final int number;
		
		/**
		 * Initializes the code.
		 *
		 * @param __full The full code.
		 * @throws IllegalArgumentException If the prefix or number are not
		 * valid.
		 * @throws NullPointerException On null arguments.
		 * @since 2017/10/27
		 */
		public Code(String __full)
			throws IllegalArgumentException, NullPointerException
		{
			this(__full.substring(0, 2), __full.substring(2, 4));
		}
		
		/**
		 * Initializes the code.
		 *
		 * @param __pre The prefix code.
		 * @param __num The error code number.
		 * @throws IllegalArgumentException If the prefix or number are not
		 * valid.
		 * @throws NullPointerException On null arguments.
		 * @since 2017/10/27
		 */
		public Code(String __pre, String __num)
			throws IllegalArgumentException, NullPointerException
		{
			this(__pre, Integer.parseInt(
				Objects.<String>requireNonNull(__num), 36));
		}
		
		/**
		 * Initializes the code.
		 *
		 * @param __pre The prefix code.
		 * @param __num The error code number.
		 * @throws IllegalArgumentException If the prefix is not valid.
		 * @throws NullPointerException On null arguments.
		 * @since 2017/10/27
		 */
		public Code(String __pre, int __num)
			throws IllegalArgumentException, NullPointerException
		{
			if (__pre == null)
				throw new NullPointerException("NARG");
			
			if (__pre.length() != 2)
				throw new IllegalArgumentException(
					"Prefix must have length of two.");
			
			// The high number comes from base 36 encoding
			if (__num < 0 || __num > 1295)
				throw new IllegalArgumentException(String.format(
					"Error code number ouf of range: %d.", __num));
			
			this.prefix = __pre.toUpperCase();
			this.number = __num;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2017/10/27
		 */
		@Override
		public boolean equals(Object __o)
		{
			if (!(__o instanceof Code))
				return false;
			
			Code o = (Code)__o;
			return this.prefix.equals(o.prefix) &&
				this.number == o.number;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2017/10/27
		 */
		@Override
		public int hashCode()
		{
			return this.prefix.hashCode() + this.number;
		}
		
		/**
		 * Returns the next index.
		 *
		 * @return The next index.
		 * @since 2018/01/15
		 */
		public Code next()
		{
			return new Code(this.prefix, this.number + 1);
		}
		
		/**
		 * Returns the number as a string.
		 *
		 * @return The numnber as a string.
		 * @since 2017/10/27
		 */
		public String numberAsString()
		{
			String base = Integer.toString(number, 36).toLowerCase();
			if (base.length() == 1)
				return "0" + base;
			return base;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2017/10/27
		 */
		@Override
		public String toString()
		{
			return this.prefix + numberAsString();
		}
	}
}

