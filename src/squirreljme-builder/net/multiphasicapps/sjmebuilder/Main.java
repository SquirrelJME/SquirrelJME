// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.sjmebuilder;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * Main entry point for hairball.
 *
 * @since 2016/02/28
 */
public class Main
{
	/**
	 * Main entry point.
	 *
	 * @param __args Program arguments.
	 * @since 2016/02/28
	 */
	public static void main(String... __args)
	{
		// Must exist
		if (__args == null || __args.length < 2)
			throw new IllegalArgumentException(
				"Usage: (Output Dir) (Source Dir) [Target OS] [Target CPU]");
		
		// Get locations
		Path outdir = Paths.get(Objects.<String>requireNonNull(__args[0],
			"No output directory specified."));
		Path srcdir = Paths.get(Objects.<String>requireNonNull(__args[1],
			"No source directory specified."));
		
		// Get target OS and CPU
		String targos = __guessOS(__args);
		String targcpu = __guessCPU(__args);
		
		// Inform the decision
		System.out.printf("Target OS : %s%n", targos);
		System.out.printf("Target CPU: %s%n", targcpu);
		
		throw new Error("TODO");
	}
	
	/**
	 * Performs an ASCII lowercase on the given string.
	 *
	 * @param __in Input string to lowercase.
	 * @return The lowercased string using only ASCII data.
	 * @since 2016/02/28
	 */
	static final String __asciiLowerCase(String __in)
		throws NullPointerException
	{
		// Check
		if (__in == null)
			throw new NullPointerException("NARG");
		
		// Output
		StringBuilder sb = new StringBuilder();
		
		// Go through input string and lowercase
		int n = __in.length();
		for (int i = 0; i < n; i++)
		{
			char c = __in.charAt(i);
			
			// Lower it
			if (c >= 'A' && c <= 'Z')
				c = (char)('a' + (c - 'A'));
			
			sb.append(c);
		}
		
		// Build it
		return sb.toString();
	}
	
	/**
	 * Guesses the CPU to target.
	 *
	 * @param __args Input command line arguments.
	 * @return The CPU to target.
	 * @since 2016/02/29
	 */
	private static String __guessCPU(String... __args)
	{
		// The guess to make
		String guess = null;
		
		// Passed in via the command line
		if (__args != null && __args.length >= 4)
			guess = __args[3];
		
		// Use system property
		if (guess == null || guess.isEmpty())
			guess = System.getProperty("os.arch");
		
		// Use a predefined default
		if (guess == null || guess.isEmpty())
			guess = "m68k";
		
		// Force lowercase
		guess = __asciiLowerCase(guess);
		
		// Depends on the input
		switch (guess)
		{
				// PowerPC 32-bit Big Endian
			case "ppc":
			case "ppc32":
			case "ppceb":
			case "ppc32eb":
			case "powerpc32":
				return "powerpc::32:big";
				
				// PowerPC 32-bit little endian
			case "ppcel":
			case "ppc32el":
				return "powerpc::32:little";
				
				// PowerPC 64-bit Big Endian
			case "ppc64":
			case "ppc64eb":
			case "powerpc64":
				return "powerpc::64:big";
				
				// PowerPC 64-bit little endian
			case "ppc64el":
				return "powerpc::64:little";
			
				// ia16
			case "i286":
			case "x86_16":
			case "x86-16":
				return "x86::16";
			
				// ia32
			case "i386":
			case "i486":
			case "i586":
			case "i686":
			case "x86_32":
			case "x86-32":
				return "x86::32";
			
				// amd64
			case "x86_64":
			case "x86-64":
				return "x86::64";
			
				// m68k
			case "68000":
			case "68k":
				return "m68k:";
			
				// Use the input
			default:
				return guess;
		}
	}
	
	/**
	 * Guesses the OS to target.
	 *
	 * @param __args Input command line arguments.
	 * @return The CPU to target.
	 * @since 2016/02/29
	 */
	private static String __guessOS(String... __args)
	{
		// The guess to make
		String guess = null;
		
		// Passed in via the command line
		if (__args != null && __args.length >= 3)
			guess = __args[2];
		
		// Use system property
		if (guess == null || guess.isEmpty())
			guess = System.getProperty("os.name");
		
		// Use a predefined default
		if (guess == null || guess.isEmpty())
			guess = "palmos:5";
		
		// Force lowercase
		guess = __asciiLowerCase(guess);
		
		// Depends on the input
		switch (guess)
		{
				// Palm OS
			case "palm os":
				return "palmos:5";
				
				// 16-bit Windows
			case "windows 3.1":
			case "windows 3.11":
				return "win16";
				
				// DOS
			case "ms-dos":
			case "freedos":
				return "dos";
			
				// Use the input
			default:
				return guess;
		}
	}
}

