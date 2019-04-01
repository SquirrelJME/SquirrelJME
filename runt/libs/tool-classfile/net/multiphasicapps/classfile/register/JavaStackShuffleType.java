// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classfile.register;

/**
 * This represents the type of stack shuffle to perform. Since these
 * operations depend on the types on the stack, this is used to contain the
 * information to simplify the operations.
 *
 * @since 2019/03/30
 */
public enum JavaStackShuffleType
{
	/** dup. */
	DUP("a:aa"),
	
	/** dup_x1. */
	DUP_X1("ba:aba"),
	
	/** dup_x2. */
	DUP_X2("cba:acba"),
	
	/** dup2. */
	DUP2("ba:baba",
		"A:AA"),
	
	/** dup2_x1. */
	DUP2_X1("cba:bacba",
		"bA:AbA"),
	
	/** dup2_x2. */
	DUP2_X2("dcba:badcba",
		"cbA:Acba",
		"Cba:baCba",
		"BA:ABA"),
	
	/** pop. */
	POP("a:"),
	
	/** pop2. */
	POP2("ba:",
		"A:"),
	
	/** swap. */
	SWAP("ba:ab"),
	
	/** End. */
	;
	
	/** Forms of this operation. */
	final Function[] _functions;
	
	/**
	 * Initialize the shuffle form information.
	 *
	 * The forms consist of characters for the various items on the stack.
	 * A lowercase letter represents a narrow type while a capital letter
	 * represents a wide type. Input and output is separated by a colon. The
	 * operation is just that whatever is pushed to the stack has the same
	 * value as the items removed from the stack.
	 *
	 * @param __fs The forms.
	 * @since 2019/04/01
	 */
	private JavaStackShuffleType(String... __fs)
	{
		int n = __fs.length;
		Function[] functions = new Function[n];
		for (int i = 0; i < n; i++)
			functions[i] = Function.of(__fs[i]);
		this._functions = functions;
	}
	
	/**
	 * Contains information on how to push or pop operations.
	 *
	 * @since 2019/04/01
	 */
	public static final class Function
	{
		/** Input slots. */
		public final Slots in;
		
		/** Output slots. */
		public final Slots out;
		
		/**
		 * Initializes the function.
		 *
		 * @param __in The input.
		 * @param __out The output.
		 * @throws NullPointerException On null arguments.
		 * @since 2019/04/01
		 */
		public Function(Slots __in, Slots __out)
			throws NullPointerException
		{
			if (__in == null || __out == null)
				throw new NullPointerException("NARG");
			
			this.in = __in;
			this.out = __out;
		}
		
		/**
		 * Returns the function for the given string.
		 *
		 * @param __s The string to parse.
		 * @throws IllegalArgumentException If the function is not valid.
		 * @throws NullPointerException On null arguments.
		 * @since 2019/04/01
		 */
		public static final Function of(String __s)
			throws IllegalArgumentException, NullPointerException
		{
			if (__s == null)
				throw new NullPointerException("NARG");
			
			// {@squirreljme.error JC31 Expected colon in function form.}
			int col = __s.indexOf(':');
			if (col < 0)
				throw new IllegalArgumentException("JC31");
			
			return new Function(new Slots(__s.substring(0, col)),
				new Slots(__s.substring(col + 1)));
		}
	}
	
	/**
	 * Represents the slots used for the stack.
	 *
	 * @since 2019/04/01
	 */
	public static final class Slots
	{
		/** The maximum push/pop count. */
		public final int max;
		
		/** The variable index, negative values mean top types. */
		final byte[] _var;
		
		/** Which slots are considered wide or not. */
		final boolean[] _wide;
		
		/**
		 * Initializes the slots.
		 *
		 * @param __s The string source.
		 * @throws IllegalArgumentException If the slots are not valid.
		 * @throws NullPointerException On null arguments.
		 * @since 2019/04/01
		 */
		public Slots(String __s)
			throws IllegalArgumentException, NullPointerException
		{
			if (__s == null)
				throw new NullPointerException("NARG");
			
			// Determine the actual popping, with top types and such
			int n = __s.length(),
				max = 0;
			for (int i = 0; i < n; i++)
				if (Character.isUpperCase(__s.charAt(i)))
					max += 2;
				else
					max += 1;
			
			// Stores top and wide states
			byte[] var = new byte[max];
			boolean[] wide = new boolean[max];
			
			// Go through again and fill the output
			for (int i = 0, o = 0; i < n; i++)
			{
				char c = __s.charAt(i);
				boolean iswide = Character.isUpperCase(c);
				
				// Store information here
				var[o] = (byte)(Character.toLowerCase(c) - 'a');
				wide[o++] = iswide;
				
				// The tops of wide types are considered narrow but also have
				// no variable type
				if (iswide)
					var[o++] = -1;
			}
			
			// Store
			this.max = max;
			this._var = var;
			this._wide = wide;
		}
	}
}

