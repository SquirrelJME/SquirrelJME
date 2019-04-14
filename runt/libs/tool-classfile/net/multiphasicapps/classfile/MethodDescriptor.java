// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classfile;

import java.util.ArrayList;
import java.util.List;

/**
 * This represents the type descriptor of a method.
 *
 * @since 2017/06/12
 */
public final class MethodDescriptor
	implements MemberDescriptor
{
	/** String representation of the descriptor. */
	protected final String string;
	
	/** The return value, null is void. */
	protected final FieldDescriptor rvalue;
	
	/** The arguments in the method. */
	private final FieldDescriptor[] _args;
	
	/**
	 * Initializes the descriptor with the given descriptors.
	 *
	 * @param __rv The return value.
	 * @param __args Arguments to the descriptor.
	 * @throws InvalidClassFormatException If the descriptor is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/12/08
	 */
	public MethodDescriptor(FieldDescriptor __rv, FieldDescriptor... __args)
		throws InvalidClassFormatException, NullPointerException
	{
		if (__rv == null || __args == null)
			throw new NullPointerException("NARG");
		
		// Need to build the string representation
		StringBuilder sb = new StringBuilder();
		sb.append('(');
		
		// Check for more nulls and build the string
		__args = __args.clone();
		for (FieldDescriptor f : __args)
		{
			if (f == null)
				throw new NullPointerException("NARG");
			
			sb.append(f.toString());
		}
		
		// Finish with the return value
		sb.append(')');
		sb.append((__rv == null ? "V" : __rv.toString()));
		
		// Set because these are copies
		this.rvalue = __rv;
		this._args = __args;
		this.string = sb.toString();
	}
	
	/**
	 * Initializes the method descriptor.
	 *
	 * @param __n The method descriptor to decode.
	 * @throws InvalidClassFormatException If it is not a valid method
	 * descriptor.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/06/12
	 */
	public MethodDescriptor(String __n)
		throws InvalidClassFormatException, NullPointerException
	{
		// Check
		if (__n == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.string = __n;
		
		// {@squirreljme.error JC1o Method descriptors must start with an open
		// parenthesis. (The method descriptor)}
		if (!__n.startsWith("("))
			throw new InvalidClassFormatException(
				String.format("JC1o %s", __n));
		
		// Parse all input arguments
		List<FieldDescriptor> args = new ArrayList<>();
		int i = 1, n = __n.length();
		for (; i < n;)
		{
			char c = __n.charAt(i);
			
			// End of descriptor arguments
			if (c == ')')
				break;
			
			// Skip array markers
			int at = i;
			for (; at < n; at++)
				if ('[' != __n.charAt(at))
					break;
			
			// {@squirreljme.error JC1p Reached end of descriptor parsing
			// arguments. (The method descriptor)}
			if (at >= n)
				throw new InvalidClassFormatException(
					String.format("JC1p %s", __n));
			
			// Find end sequence
			switch (__n.charAt(at))
			{
					// Primitive
				case 'B':
				case 'C':
				case 'D':
				case 'F':
				case 'I':
				case 'J':
				case 'S':
				case 'Z':
					break;
					
					// Class, find ;
				case 'L':
					for (; at < n; at++)
						if (';' == __n.charAt(at))
							break;
					break;
				
					// {@squirreljme.error JC1q Unknown field descriptor in
					// method descriptor argument. (The descriptor)}
				default:
					throw new InvalidClassFormatException(
						String.format("JC1q %s", __n));
			}
			
			// Parse field
			args.add(new FieldDescriptor(__n.substring(i, at + 1)));
			
			// Go to next field
			i = at + 1;
		}
		this._args = args.<FieldDescriptor>toArray(
			new FieldDescriptor[args.size()]);
		
		// Skip the ending parenthesis
		i++;
		
		// {@squirreljme.error JC1r The method descriptor has no return
		// value. (The method descriptor)}
		if (i >= n)
			throw new InvalidClassFormatException(
				String.format("JC1r %s", __n));
		
		// No return value?
		char c = __n.charAt(i);
		if (c == 'V' && (i + 1) == n)
			this.rvalue = null;
		
		// Parse as a field
		else
			this.rvalue = new FieldDescriptor(__n.substring(i));
	}
	
	/**
	 * Returns the argument of the given parameter number.
	 *
	 * @param __i The parameter to get the argument for.
	 * @throws IndexOutOfBoundsException If the argument is not within bounds.
	 * @since 2017/07/28
	 */
	public FieldDescriptor argument(int __i)
		throws IndexOutOfBoundsException
	{
		return this._args[__i];
	}
	
	/**
	 * Returns all of the arguments.
	 *
	 * @return The arguments.
	 * @since 2019/04/14
	 */
	public FieldDescriptor[] arguments()
	{
		return this._args.clone();
	}
	
	/**
	 * Returns the number of arguments this descriptor has.
	 *
	 * @return The number of arguments this descriptor has.
	 * @since 2017/07/28
	 */
	public int argumentCount()
	{
		return this._args.length;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/06/12
	 */
	@Override
	public boolean equals(Object __o)
	{
		// Check
		if (!(__o instanceof MethodDescriptor))
			return false;
		
		return this.string.equals(((MethodDescriptor)__o).string);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/06/12
	 */
	@Override
	public int hashCode()
	{
		return this.string.hashCode();
	}
	
	/**
	 * Does this method have a return value?
	 *
	 * @return If the method has a return value.
	 * @since 2018/09/16
	 */
	public final boolean hasReturnValue()
	{
		return null != this.rvalue;
	}
	
	/**
	 * Returns the Java type stack for this descriptor.
	 * 
	 * @return The descriptor as it appears on the Java Stack.
	 * @since 2017/09/16
	 */
	public JavaType[] javaStack()
	{
		// Handle all arguments now
		int n = argumentCount();
		List<JavaType> rv = new ArrayList<>(n * 2);
		for (int i = 0; i < n; i++)
		{
			FieldDescriptor a;
			JavaType j;
			rv.add(j = new JavaType(a = argument(i)));
			
			// Add top of long/double but with unique distinct types
			if (j.isWide())
				rv.add(j.topType());
		}
		
		return rv.<JavaType>toArray(new JavaType[rv.size()]);
	}
	
	/**
	 * Returns the return value of this descriptor.
	 *
	 * @return The value returned in this descriptor or {@code null} if there
	 * is no return value.
	 * @since 2017/09/22
	 */
	public FieldDescriptor returnValue()
	{
		return this.rvalue;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/06/12
	 */
	@Override
	public String toString()
	{
		return this.string;
	}
}

