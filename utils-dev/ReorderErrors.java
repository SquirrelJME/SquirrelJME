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
	/** The path to reorder errors for. */
	protected final Path path;
	
	/**
	 * Initializes the error reordering on the given path.
	 *
	 * @param __p The path to reorder.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/10/26
	 */
	public ReorderErrors(Path __p)
		throws NullPointerException
	{
		if (__p == null)
			throw new NullPointerException("NARG");
		
		this.path = __p;
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
		prefix = prefix.trim();
		
		// Obtain Java source files
		Set<Path> files = new TreeSet<>();
		Files.walk(path).filter((__p) -> !Files.isDirectory(__p)).
			filter((__p) -> __p.toString().endsWith(".java")).
			forEach(files::add);
		
		// The next prefix to use
		int next = 1;
		
		// Go through all files since they must be parsed to recode versions
		for (Path file : files)
		{
			// Read in all bytes from the file since they will be managed and
			// handled specially depending on which error codes were found
			byte[] data = Files.readAllBytes(file);
			
			// Treat as string to extract all the error codes
			String sdata = new String(data, "utf-8");
			
			// This is the remapping of input error codes to the output error
			// codes
			Map<Bi, Bi> errormap = new LinkedHashMap<>();
			
			// Use simple index search to find the error indicator
			for (int pos = 0; pos >= 0; pos++)
			{
				// Search for string index
				pos = sdata.indexOf("{@squirreljme.error", pos);
				
				// End of search
				if (pos < 0)
					break;
				
				// Start and skip any non-whitespace, then skip all whitespace
				// until a character is found
				boolean flag = false;
				for (;;)
				{
					char c = sdata.charAt(pos);
					
					if (flag !=
						(c == ' ' || c == '\t' || c == '\r' || c == '\n'))
					{
						if (flag)
							break;
						flag = true;
					}
					else
						pos++;
				}
				
				// Generate code
				System.err.printf("Code == %s%n",
					sdata.substring(pos, pos + 4));
				errormap.put(new Bi(sdata.substring(pos, pos + 4)),
					new Bi(prefix, next++));
			}
			
			// Nothing needs to be done?
			if (errormap.isEmpty())
				continue;
			
			// Note
			System.err.printf("Recoding %s%n", file);
			
			// Although this is not as fast or elegant, it is quick to write
			// and does what it needs to do. This is really just what is
			// needed really
			// This program is really only ran when it needs to be anyway
			StringBuilder rework = new StringBuilder(sdata);
			for (Map.Entry<Bi, Bi> e : errormap.entrySet())
			{
				Bi from = e.getKey(),
					to = e.getValue();
				String sfrom = from.toString(),
					sto = to.toString();
				
				// Replace whenever it appears
				for (int pos = 0;; pos++)
				{
					// Sequence appears?
					pos = rework.indexOf(sfrom, pos);
					if (pos < 0)
						break;
					
					// To prevent changing other characters which might match
					// the error code (such as hex values), the previous
					// character must be a quote or whitespace
					char before = rework.charAt(pos - 1);
					if ((before != '"' && !(before == ' ' || before == '\t')))
						continue;
					
					// Replace it
					rework.replace(pos, pos + 4, sto);
				}
			}
			
			// Print codes for debugging
			//System.err.printf("Remap %s%n", errormap);
			
			// Rewrite to file
			Files.write(file, rework.toString().getBytes("utf-8"));
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
	public static final class Bi
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
		public Bi(String __full)
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
		public Bi(String __pre, String __num)
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
		public Bi(String __pre, int __num)
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
			if (!(__o instanceof Bi))
				return false;
			
			Bi o = (Bi)__o;
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

