// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.jsr353;

import com.oracle.json.JsonException;
import com.oracle.json.JsonValue;
import com.oracle.json.stream.JsonLocation;
import com.oracle.json.stream.JsonParsingException;
import java.io.Closeable;
import java.util.AbstractList;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;

/**
 * This is a basic class which takes very raw JSON data and decodes it into a
 * form more usable by Reader and Parser, the output of this class is assumed
 * and considered to be correct.
 *
 * @since 2014/08/03
 */
public abstract class BaseDecoder
	implements Closeable
{
	/** Synchronization lock. */
	protected final Object lock =
		new Object();
	
	/** Input tokenizer thing. */
	protected final Input input;
	
	/** Scope work. */
	private final Deque<__Scope__> _scopes =
		new ArrayDeque<>();
	
	/** Bits to be flushed out. */
	private final Deque<Bit> _flush =
		new ArrayDeque<>();
	
	/** Times the scope has been emptied (on close). */
	private int _emptied; 
	
	/** Decoder has been closed. */
	private boolean _closed;
	
	/**
	 * Starts parsing of the specified input decoder.
	 *
	 * @param __i Input decoder to use.
	 * @since 2014/08/03
	 */
	protected BaseDecoder(Input __i)
	{
		// Cannot be null
		if (__i == null)
			throw new NullPointerException("noinp");
		
		// Set
		this.input = __i;
	}
	
	/**
	 * Closes this decoder, also closes the input.
	 *
	 * @throws JsonException If it could not be closed.
	 * @since 2014/08/03
	 */
	@Override
	public void close()
	{
		synchronized (this.lock)
		{
			// Ignore if already closed
			if (this._closed)
				return;
			
			// Set closed and clear cache
			this._closed = true;
			
			// Close input
			try
			{
				this.input.close();
			}
			
			// Rethrow if JSON exception was thrown
			catch (JsonException je)
			{
				throw je;
			}
		}
	}
	
	/**
	 * Obtains the next segment of the decoding process.
	 *
	 * @return The next piece of segment read, {@code null} if none left.
	 * @throws IllegalStateException If closed.
	 * @since 2014/08/04
	 */
	protected final Bit nextBit()
	{
		synchronized (this.lock)
		{
			// Cannot be called if closed
			if (this._closed)
				throw new IllegalStateException("Decoder has been closed.");
			
			// Fill queue if empty
			if (this._flush.isEmpty())
				this.__internalGet();
			
			// From flush queue
			if (!this._flush.isEmpty())
				return this._flush.removeFirst();
			
			// Done
			return null;
		}
	}
	
	/**
	 * Returns an exception in a short and easier fashion so that
	 * the information information is obtained at the end of a toss.
	 *
	 * This is returned rather than thrown so that the runtime could better
	 * optimize things when needed, and at compile time to make the dead code
	 * detector work.
	 *
	 * @param __t Exception that may have caused this.
	 * @param __lstr Localized string for obtaining the message.
	 * @param __args Arguments to pass to the string localizer.
	 * @return The exception to be thrown.
	 * @since 2015/04/12
	 */
	private final JsonParsingException __fail(Throwable __t, String __lstr,
		Object... __args)
	{
		return new JsonParsingException(String.format(__lstr, __args) + " (" + this._scopes + ")", __t,
			this.input.getLocation());
	}
	
	/**
	 * Adds bit actions to the flush queue.
	 *
	 * @since 2015/04/12
	 */
	private final void __internalGet()
	{
		// Read next data bit
		Input.Data d = this.input.next();
		String ds;
		Input.Type dt;
		
		// Valid?
		if (d != null)
		{
			ds = d.toString();
			dt = d.getType();
		}
		
		// End
		else
		{
			ds = null;
			dt = Input.Type.END_OF_STREAM;
		}
		
		// Depends on type
		switch (dt)
		{
				// Start of an object
			case START_OBJECT:
				// no scope and never used, or Expecting value
				if ((this._emptied == 0 && this._scopes.isEmpty()) || this.__top().want(__Exp__.VALUE) || this.__top().want(__Exp__.VALUE_OR_END))
				{
					// OK, add to scope
					this._scopes.addLast(new __Scope__(dt));
				
					// Create bit for this action
					this._flush.addLast(new Bit(Bit.Kind.PUSH_OBJECT));
				}
				
				// Unknown
				else
					throw this.__fail(null, "mpso");
				break;
				
				// End of an object
			case END_OBJECT:
				// Want end of object
				if (this.__top().want(__Exp__.VALUE_OR_END) || this.__top().want(__Exp__.KEY_OR_END) || this.__top().want(__Exp__.COMMA_OR_END))
				{
					// Must be an object
					if (!this.__top().isObject())
						throw this.__fail(null, "eonotobj");
					
					// Remove from scope
					this._scopes.pollLast();
					
					// If emptied, then increase empty count
					if (this._scopes.isEmpty())
					{
						// Increase empty
						this._emptied++;
						
						// Gets as finished complete
						this._flush.addLast(new Bit(Bit.Kind.FINISHED_OBJECT));
					}
					
					// Top is now an object, set value
					else if (this.__top().isObject())
					{
						this.__top().need(__Exp__.COMMA_OR_END);
						this._flush.addLast(new Bit(
							Bit.Kind.POP_OBJECT_ADD_OBJECT_KEYVAL));
					}
					
					// Top is now an array
					else if (this.__top().isArray())
					{
						this.__top().need(__Exp__.COMMA_OR_END);
						this._flush.addLast(new Bit(Bit.Kind.POP_ARRAY_ADD_ARRAY));
					}
					
					// Unknown
					else
						throw new RuntimeException("eounss");
				}
				
				// Is not
				else
					throw this.__fail(null, "mpeo");
				break;
				
				// Start of an array
			case START_ARRAY:
				// no scope and never used, or Expecting value
				if ((this._emptied == 0 && this._scopes.isEmpty()) || this.__top().want(__Exp__.VALUE) || this.__top().want(__Exp__.VALUE_OR_END))
				{
					// OK, add to scope
					this._scopes.addLast(new __Scope__(dt));
				
					// Create bit for this action
					this._flush.addLast(new Bit(Bit.Kind.PUSH_ARRAY));
				}
				
				// Unknown
				else
					throw this.__fail(null, "mpsa");
				break;
				
				// End of an array
			case END_ARRAY:
				// Want end of object
				if (this.__top().want(__Exp__.VALUE_OR_END) ||
					this.__top().want(__Exp__.COMMA_OR_END))
				{
					// Must be an array
					if (!this.__top().isArray())
						throw this.__fail(null, "eonotarr");
					
					// Remove from scope
					this._scopes.pollLast();
					
					// If emptied, then increase empty count
					if (this._scopes.isEmpty())
					{
						// Increase empty
						this._emptied++;
						
						// Gets as finished complete
						this._flush.addLast(new Bit(Bit.Kind.FINISHED_ARRAY));
					}
					
					// Top is now an object, set value
					else if (this.__top().isObject())
					{
						this.__top().need(__Exp__.COMMA_OR_END);
						this._flush.addLast(new Bit(
							Bit.Kind.POP_ARRAY_ADD_OBJECT_KEYVAL));
					}
					
					// Top is now an array
					else if (this.__top().isArray())
					{
						this.__top().need(__Exp__.COMMA_OR_END);
						this._flush.addLast(new Bit(Bit.Kind.POP_ARRAY_ADD_ARRAY));
					}
					
					// Unknown
					else
						throw new RuntimeException("eaunss");
				}
				
				// Is not
				else
					throw this.__fail(null, "mpea");
				break;
				
				// A literal, either numerical or false/true/null
			case LITERAL:
				// Expecting a value
				if (this.__top().want(__Exp__.VALUE) ||
					this.__top().want(__Exp__.VALUE_OR_END))
				{
					// Expected value type to use
					char cz = ds.charAt(0);
					JsonValue vv;
					try
					{
						vv = (cz == 't' ? JsonValue.TRUE : (cz == 'f' ?
							JsonValue.FALSE : (cz == 'n' ? JsonValue.NULL :
							new ImplValueNumber(ds))));
					}
					
					// Bad number
					catch (NumberFormatException nfe)
					{
						throw this.__fail(nfe, "litnan", ds);
					}
					
					// Add to object
					if (this.__top().isObject())
						this._flush.addLast(new Bit(Bit.Kind.ADD_OBJECT_KEYVAL,
							vv));
					
					// Add to array
					else if (this.__top().isArray())
						this._flush.addLast(new Bit(Bit.Kind.ADD_ARRAY_VALUE,
							vv));
					
					// Unknown
					else
						throw new RuntimeException("unklitv");
					
					// Expect comma or end
					this.__top().need(__Exp__.COMMA_OR_END);
				}
				
				// No idea what to do with this.
				else
					throw this.__fail(null, "mpliteral", ds);
				break;
				
				// A string
			case STRING:
				// Expecting a key value here
				if (this.__top().want(__Exp__.KEY_OR_END) || this.__top().want(__Exp__.KEY))
				{
					// Declare key
					if (this.__top().isObject())
						this._flush.addLast(new Bit(Bit.Kind.DECLARE_KEY, ds));
					
					// Not a key!?
					else
						throw new RuntimeException("incknoto");
					
					// Want colon now, which after that is some kind of value
					this.__top().need(__Exp__.COLON);
				}
				
				// Expecting a value
				else if (this.__top().want(__Exp__.VALUE) || this.__top().want(__Exp__.VALUE_OR_END))
				{
					// Object value
					if (this.__top().isObject())
						this._flush.addLast(new Bit(Bit.Kind.ADD_OBJECT_KEYVAL,
							new ImplValueString(ds)));
					
					// Array entry
					else if (this.__top().isArray())
						this._flush.addLast(new Bit(Bit.Kind.ADD_ARRAY_VALUE,
							new ImplValueString(ds)));
					
					// Unknown
					else
						throw new RuntimeException("unkvalv");
					
					// Comma or end
					this.__top().need(__Exp__.COMMA_OR_END);
				}
				
				// No idea what to do with this.
				else
					throw this.__fail(null, "mpstring");
				break;
				
				// A colon, which links between a key and a value
			case COLON:
				// Must be expecting colon
				if (this.__top().want(__Exp__.COLON))
				{
					// If we are in an object a value is expected
					if (this.__top().isObject())
						this.__top().need(__Exp__.VALUE);
					
					// if in an array, need comma or end
					else if (this.__top().isArray())
						throw this.__fail(null, "colinarr");
					
					// Bad
					else
						throw new RuntimeException("unkcolao");
					
					// Recursive self
					this.__internalGet();
				}
				
				// Did not want
				else
					throw this.__fail(null, "mpcolon");
				break;
				
				// The next value in the file
			case COMMA:
				// Want comma
				if (this.__top().want(__Exp__.COMMA_OR_END))
				{
					// Expect key if an object
					if (this.__top().isObject())
						this.__top().need(__Exp__.KEY);
					
					// Expect another value if array, but not the end
					else if (this.__top().isArray())
						this.__top().need(__Exp__.VALUE);
					
					// Bad
					else
						throw new RuntimeException("unkcomao");
					
					// Recourse to find next item
					this.__internalGet();
				}
				
				// Did not want
				else
					throw this.__fail(null, "mpcomma");
				break;
			
				// Unknown or end
			case END_OF_STREAM:
			default:
				// Unknown
				if (dt != null && dt != Input.Type.END_OF_STREAM)
					throw new RuntimeException(String.format(
						"unkt", dt.name()));
				
				// Cannot end when nothing was given or inside of a scope
				if (this._emptied == 0)
					throw this.__fail(null, "blankeof");
				if (!this._scopes.isEmpty())
					throw this.__fail(null, "scopeeof");
				
				throw new RuntimeException("TODO -- null");
		}
		
		// Cannot have remained empty
		if (this._flush.isEmpty())
			throw new IllegalStateException("stillempty");
	}
	
	/**
	 * Returns the scope at the top of the stack.
	 *
	 * @return The topmost scope on the stack.
	 * @since 2015/04/12
	 */
	private final __Scope__ __top()
	{
		if (this._scopes.isEmpty())
			throw this.__fail(null, "emptyscopestack");
		return this._scopes.peekLast();
	}
	
	/**
	 * An expectation, replaces tons of booleans.
	 *
	 * @since 2015/04/12
	 */
	public enum __Exp__
	{
		/** Expecting a key. */
		KEY,
		
		/** Name of a key or end of object. */
		KEY_OR_END,
		
		/** Expect a colon. */
		COLON,
		
		/** A kind of value. */
		VALUE,
		
		/** Value or end of array. */
		VALUE_OR_END,
		
		/** Expecting comma or end. */
		COMMA_OR_END,
		
		/** End. */
		;
	}
	
	/**
	 * Private decoding state information, for each scope.
	 *
	 * @since 2015/04/12
	 */
	private class __Scope__
	{
		/** The starting type of the scope information. */
		public final Input.Type type;
		
		/** Expectation set. */
		public final Set<__Exp__> expect =
			new HashSet<>();
		
		/**
		 * Initializes start of scope.
		 *
		 * @param __t Type which starts the scope.
		 * @throws IllegalArgumentException If not start of an object or an
		 * array.
		 * @since 2015/04/12
		 */
		private __Scope__(Input.Type __t)
			throws IllegalArgumentException
		{
			// Check
			if (__t != Input.Type.START_OBJECT &&
				__t != Input.Type.START_ARRAY)
				throw new IllegalArgumentException("snsoa");
			
			// Set
			this.type = __t;
			
			// Initial state that depends on the entry type
			switch (this.type)
			{
					// Start of an object
				case START_OBJECT:
					this.expect.add(__Exp__.KEY_OR_END);
					break;
					
					// Start of an array
				case START_ARRAY:
					this.expect.add(__Exp__.VALUE_OR_END);
					break;
				
					// Unhandled
				default:
					throw new RuntimeException(String.format(
						"unhsct", this.type.name()));
			}
		}
		
		/**
		 * Is this an array?
		 *
		 * @return {@code true} if it is.
		 * @since 2015/04/12
		 */
		public boolean isArray()
		{
			return this.type == Input.Type.START_ARRAY;
		}
		
		/**
		 * Is this an object?
		 *
		 * @return {@code true} if it is.
		 * @since 2015/04/12
		 */
		public boolean isObject()
		{
			return this.type == Input.Type.START_OBJECT;
		}
		
		/**
		 * Need these things, clears the current expectation set.
		 *
		 * @param __n Things that are needed.
		 * @since 2015/04/12
		 */
		public void need(__Exp__... __n)
		{
			this.expect.clear();
			for (__Exp__ x : __n)
				this.expect.add(x);
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2015/04/12
		 */
		@Override
		public String toString()
		{
			return (this.type == Input.Type.START_OBJECT ? "Object" :
				this.type == Input.Type.START_ARRAY ? "Array" : "???") + " [" + this.expect + "]";
		}
		
		/**
		 * Checks if the specified thing is desired.
		 *
		 * @return {@code true} If this is being expected.
		 * @since 2015/04/12
		 */
		public boolean want(__Exp__ __e)
		{
			return this.expect.contains(__e);
		}
	}
	
	/**
	 * This class reads input in the form of very raw JSON tokens as it is up
	 * to the decoder to handle them correctly.
	 *
	 * @since 2014/08/03
	 */
	public abstract static class Input
		implements Closeable
	{
		/** Internal input lock. */
		protected final Object ilock;
		
		/**
		 * Initializes the lock.
		 *
		 * @since 2014/08/04
		 */
		protected Input()
		{
			this.ilock = new Object();
		}
		
		/**
		 * Reads some input token which could be from some varying kind of
		 * input.
		 *
		 * @return The raw type of token just read, {@code null} if there is
		 * nothing left.
		 * @throws JsonException Any internal reading errors possibly.
		 * @throws JsonParsingException On any parser errors.
		 * @since 2014/08/03
		 */
		protected abstract Data next();
		
		/**
		 * Closes the input.
		 *
		 * @throws JsonException If it cannot be closed.
		 * @since 2014/08/03
		 */
		@Override
		public abstract void close();
		
		/**
		 * Returns the current JSON location.
		 *
		 * @return The current location.
		 * @since 2014/08/04
		 */
		public JsonLocation getLocation()
		{
			// Replaced by sub-classes if they support this.
			return new SomeLocation();
		}
		
		/**
		 * Represents a single input token kind.
		 *
		 * @since 2015/04/12
		 */
		public static final class Data
		{
			/** The type of input token that this represents. */
			protected final Type type;
			
			/** The containing token text. */
			protected final String token;
			
			/**
			 * Initializes new immutable data.
			 *
			 * @param __t The data type.
			 * @param __s The token text.
			 * @throws NullPointerException If any argument is {@code null}
			 * on specified {@code Type} values.
			 * @since 2015/04/12
			 */
			public Data(Type __t, String __s)
				throws NullPointerException
			{
				// Check
				if (__t == null || (__s == null && (__t == Type.STRING ||
					__t == Type.LITERAL)))
					throw new NullPointerException("na");
				
				// Set
				this.type = __t;
				this.token = __s;
			}
			
			/**
			 * Returns the type of data that this is.
			 *
			 * @return The type of data this is.
			 * @since 2015/04/12
			 */
			public Type getType()
			{
				return this.type;
			}
			
			/**
			 * {@inheritDoc}
			 * @since 2015/04/12
			 */
			@Override
			public String toString()
			{
				return (this.token != null ? this.token : "");
			}
		}
		
		/**
		 * The type of thing that was just read.
		 *
		 * @since 2014/08/03
		 */
		public enum Type
		{
			/** Start of an object. */
			START_OBJECT,
		
			/** End of object. */
			END_OBJECT,
		
			/** Start of an array. */
			START_ARRAY,
		
			/** End of array. */
			END_ARRAY,
		
			/** A string (quoted). */
			STRING,
		
			/** A literal (number, true, false, null). */
			LITERAL,
		
			/** A colon. */
			COLON,
		
			/** A comma. */
			COMMA,
			
			/** End of stream. */
			END_OF_STREAM,
		
			/** End. */
			;
		}
	}
	
	/**
	 * This specifies a decoding bit, in essence an action to be performed such
	 * as creating an array or closing one.
	 *
	 * @since 2014/08/04
	 */
	public static final class Bit
		extends AbstractList<Object>
	{
		/** The kind. */
		private final Kind _k;
		
		/** Values. */
		private final Object[] _v;
		
		/**
		 * Sets the kind of action to use.
		 *
		 * @param __k Action kind to use.
		 * @param __v Values, uses the input value rather than a copy.
		 * @since 2014/08/04
		 */
		Bit(Kind __k, Object... __v)
		{
			// Cannot be null
			if (__k == null)
				throw new NullPointerException("Null kind specified.");
			
			// Set
			this._k = __k;
			this._v = (__v != null ? Arrays.<Object>copyOf(__v, __v.length) :
				new JsonValue[0]);
		}
		
		/**
		 * Returns the kind of action to perform.
		 *
		 * @return The action to perform.
		 * @since 2014/08/04
		 */
		public Kind getKind()
		{
			return this._k;
		}
		
		/**
		 * Obtains a value from the internal array.
		 *
		 * @param __i Value index to obtain.
		 * @throws IndexOutOfBoundsException If the index exceeds bounds.
		 * @since 2014/08/05
		 */
		@Override
		public Object get(int __i)
		{
			// Return, bounds check is done by Java
			return this._v[__i];
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2015/04/12?
		 */
		@Override
		public int size()
		{
			return this._v.length;
		}
		
		/**
		 * This represents the action to take when working with a decoder.
		 *
		 * @since 2014/08/04
		 */
		public enum Kind
		{
			/** Push an object. */
			PUSH_OBJECT,
			
			/** Push an array. */
			PUSH_ARRAY,
			
			/** Declare key. */
			DECLARE_KEY,
			
			/** Sets the value of the key in an object. */
			ADD_OBJECT_KEYVAL,
			
			/** Add array value. */
			ADD_ARRAY_VALUE,
			
			/** Pop an array, then add to the value of a key. */
			POP_ARRAY_ADD_OBJECT_KEYVAL,
			
			/** Pop an array, then add to an array. */
			POP_ARRAY_ADD_ARRAY,
			
			/** Pop an object, then add to the array. */
			POP_OBJECT_ADD_ARRAY,
			
			/** Pop object and add to object as value of an object. */
			POP_OBJECT_ADD_OBJECT_KEYVAL,
			
			/** Finished object. */
			FINISHED_OBJECT,
			
			/** Finished array. */
			FINISHED_ARRAY,
			
			/** End. */
			;
		}
	}
}

