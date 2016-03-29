// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.interpreter;

import java.util.Objects;

/**
 * This represents a single link in the chain of operations that a method
 * performs.
 *
 * Each link is unique and has a unique ID.
 *
 * @since 2016/03/24
 */
public class JVMOperatorLink
{
	/**
	 * The unique which is on the A side of the operation, this is valid if
	 * {@code constanta} is {@code null}.
	 */
	protected final long uniquea;
	
	/**
	 * The constant which is on the A side of the operation, this is valid
	 * if this is not {@code null}.
	 */
	protected final Object constanta;
	
	/** The operation to perform. */
	protected final Operation op;
	
	/**
	 * The value which is on the B side of the operation, if {@link constantb}
	 * is {@code null} and {@code op} is not {@code null} then this value is
	 * valid.
	 */
	protected final long uniqueb;
	
	/**
	 * The constant which is on the B side of the operation, if this is not
	 * {@code null} then {@code uniqueb} is invalid. {@code op} must not be
	 * {@code null} for this to be valid.
	 */
	protected final Object constantb;
	
	/**
	 * This initializes an identity operator which has no operation and is
	 * only the state for a single operation.
	 *
	 * @param __ua The slot to become an identity value for.
	 * @since 2016/03/27
	 */
	public JVMOperatorLink(long __ua)
	{
		// Set
		uniquea = __ua;
		
		// These are not valid
		constanta = null;
		op = null;
		uniqueb = Long.MIN_VALUE;
		constantb = null;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/27
	 */
	@Override
	public boolean equals(Object __o)
	{
		// Must be this
		if (!(__o instanceof JVMOperatorLink))
			return false;
		
		// Cast and compare
		JVMOperatorLink o = (JVMOperatorLink)__o;
		return uniquea == o.uniquea &&
			Objects.equals(constanta, o.constanta) &&
			Objects.equals(op, o.op) &&
			uniqueb == o.uniqueb &&
			Objects.equals(constantb, o.constantb);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/27
	 */
	@Override
	public int hashCode()
	{
		long va = uniquea;
		long vb = uniqueb;
		return (((int)(va >>> 32L)) ^ ((int)(va))) ^
			(((int)(vb >>> 32L)) ^ ((int)(vb))) ^
			Objects.hashCode(op) ^ Objects.hashCode(constanta) ^
			Objects.hashCode(constantb);
	}
	
	/**
	 * This returns the input value which is either a constant or a slot.
	 *
	 * @param __ps The program state to get the unique slot for.
	 * @return {@code null} if this is the identity operation, otherwise a
	 * constant boxed numerical type, a {@link String} type, or a slot.
	 * @return The input value.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/27
	 */
	public Object input(JVMProgramState __ps)
		throws NullPointerException
	{
		// Check
		if (__ps == null)
			throw new NullPointerException("NARG");
		
		// Either a constant or a slot
		return (constanta != null ? constanta : __ps.getUnique(uniquea));
	}
	
	/**
	 * Is this the identity operation?
	 *
	 * @return {@code true} if this is the identity operation.
	 * @since 2016/03/27
	 */
	public boolean isIdentity()
	{
		return (null == operation());
	}
	
	/**
	 * Returns the modifier which is on side B of the operation.
	 *
	 * @param __ps The program state to get the unique slot for.
	 * @return {@code null} if this is the identity operation, otherwise a
	 * constant boxed numerical type, a {@link String} type, or a slot.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/27
	 */
	public Object modifier(JVMProgramState __ps)
		throws NullPointerException
	{
		// Check
		if (__ps == null)
			throw new NullPointerException("NARG");
		
		// If no operation, then there is no modifier
		if (op == null)
			return null;
		
		// Either a constant or a slot
		return (constantb != null ? constantb : __ps.getUnique(uniqueb));
	}
	
	/**
	 * This returns the operation to be performed on the input.
	 *
	 * @return The operation to be performed or {@code null} if this is the
	 * identity operation.
	 * @since 2016/03/27
	 */
	public Operation operation()
	{
		return op;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/27
	 */
	@Override
	public String toString()
	{
		// Setup
		StringBuilder sb = new StringBuilder("(");
		
		// The side A operation
		if (constanta != null)
			sb.append(constanta);
		else
			sb.append(uniqueToString(uniquea));
		
		// Has an operation?
		if (op != null)
		{
			// Add the symbol
			sb.append(' ');
			sb.append(op.symbol);
			sb.append(' ');
			
			// Add the modifier
			if (constantb != null)
				sb.append(constantb);
			else
				sb.append(uniqueToString(uniqueb));
		}
		
		// End
		sb.append(')');
		return sb.toString();
	}
	
	/**
	 * Translates a unique value to a string representation.
	 *
	 * @param __u The unique to transform.
	 * @return The string representation of the unique.
	 * @since 2016/03/27
	 */
	public static String uniqueToString(long __u)
	{
		// Setup
		StringBuilder sb = new StringBuilder("<");
		
		// Stack or locals?
		boolean isstack = (0L != (__u & 0x8000_0000__0000_0000L));
		sb.append((isstack ? 'S' : 'L'));
		
		// Which slot?
		sb.append((int)(__u & 0xFFFF_FFFFL));
		
		// The PC address
		sb.append('@');
		sb.append(((int)(__u  >>> 32L)) & 0x7FFF_FFFF);
		
		// End
		sb.append('>');
		return sb.toString();
	}
	
	/**
	 * The operation being performed on another link, if applicable.
	 *
	 * @since 2016/03/24
	 */
	public static enum Operation
	{
		/** Set of a value. */
		SET("="),
		
		/** Add. */
		ADD("+"),
		
		/** Subtract. */
		SUBTRACT("-"),
		
		/** Multiply. */
		MULTIPLY("*"),
		
		/** Division. */
		DIVIDE("/"),
		
		/** Remainder. */
		REMAINDER("%"),
		
		/** Binary AND. */
		BINARY_AND("&"),
		
		/** Binary OR. */
		BINARY_OR("|"),
		
		/** Binary XOR. */
		BINARY_XOR("^"),
		
		/** Shift left. */
		SHIFT_LEFT("<<"),
		
		/** Logical shift right. */
		LOGICAL_SHIFT_RIGHT(">>>"),
		
		/** Arithmetic shift right. */
		ARITHMETIC_SHIFT_RIGHT(">>"),
		
		/** End. */
		;
		
		/** The symbol which represents this operation. */
		protected final String symbol;
		
		/**
		 * Initializes the operation.
		 *
		 * @param __sym The operation's symbol.
		 * @since 2016/03/27
		 */
		private Operation(String __sym)
			throws NullPointerException
		{
			// Check
			if (__sym == null)
				throw new NullPointerException("NARG");
			
			// Set
			symbol = __sym;
		}
	}
}

