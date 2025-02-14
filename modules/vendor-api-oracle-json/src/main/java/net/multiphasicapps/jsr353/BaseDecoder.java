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
import com.oracle.json.stream.JsonParsingException;
import java.io.Closeable;
import java.util.ArrayDeque;
import java.util.Deque;

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
	/** Input tokenizer thing. */
	protected final BaseDecoderInput input;
	
	/** Synchronization lock. */
	protected final Object lock = new Object();
	
	/** Bits to be flushed out. */
	private final Deque<BaseDecoderBit> _flush = new ArrayDeque<>();
	
	/** Scope work. */
	private final Deque<__Scope__> _scopes = new ArrayDeque<>();
	
	/** Decoder has been closed. */
	private boolean _closed;
	
	/** Times the scope has been emptied (on close). */
	private int _emptied;
	
	/**
	 * Starts parsing of the specified input decoder.
	 *
	 * @param __i Input decoder to use.
	 * @since 2014/08/03
	 */
	protected BaseDecoder(BaseDecoderInput __i)
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
	protected final BaseDecoderBit nextBit()
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
	 * the information is obtained at the end of a toss.
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
		return new JsonParsingException(
			String.format(__lstr, __args) + " (" + this._scopes + ")", __t,
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
		BaseDecoderData d = this.input.next();
		String ds;
		BaseDecoderType dt;
		
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
			dt = BaseDecoderType.END_OF_STREAM;
		}
		
		// Depends on type
		switch (dt)
		{
			// Start of an object
			case START_OBJECT:
				// no scope and never used, or Expecting value
				if ((this._emptied == 0 && this._scopes.isEmpty()) || this.__top()
					.want(__Exp__.VALUE) || this.__top().want(
					__Exp__.VALUE_OR_END))
				{
					// OK, add to scope
					this._scopes.addLast(new __Scope__(dt));
					
					// Create bit for this action
					this._flush.addLast(
						new BaseDecoderBit(BaseDecoderKind.PUSH_OBJECT));
				}
				
				// Unknown
				else
					throw this.__fail(null, "mpso");
				break;
			
			// End of an object
			case END_OBJECT:
				// Want end of object
				if (this.__top().want(__Exp__.VALUE_OR_END) || this.__top()
					.want(__Exp__.KEY_OR_END) || this.__top().want(
					__Exp__.COMMA_OR_END))
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
						this._flush.addLast(new BaseDecoderBit(
							BaseDecoderKind.FINISHED_OBJECT));
					}
					
					// Top is now an object, set value
					else if (this.__top().isObject())
					{
						this.__top().need(__Exp__.COMMA_OR_END);
						this._flush.addLast(new BaseDecoderBit(
							BaseDecoderKind.POP_OBJECT_ADD_OBJECT_KEYVAL));
					}
					
					// Top is now an array
					else if (this.__top().isArray())
					{
						this.__top().need(__Exp__.COMMA_OR_END);
						this._flush.addLast(new BaseDecoderBit(
							BaseDecoderKind.POP_ARRAY_ADD_ARRAY));
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
				if ((this._emptied == 0 && this._scopes.isEmpty()) || this.__top()
					.want(__Exp__.VALUE) || this.__top().want(
					__Exp__.VALUE_OR_END))
				{
					// OK, add to scope
					this._scopes.addLast(new __Scope__(dt));
					
					// Create bit for this action
					this._flush.addLast(
						new BaseDecoderBit(BaseDecoderKind.PUSH_ARRAY));
				}
				
				// Unknown
				else
					throw this.__fail(null, "mpsa");
				break;
			
			// End of an array
			case END_ARRAY:
				// Want end of object
				if (this.__top().want(__Exp__.VALUE_OR_END) || this.__top()
					.want(__Exp__.COMMA_OR_END))
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
						this._flush.addLast(new BaseDecoderBit(
							BaseDecoderKind.FINISHED_ARRAY));
					}
					
					// Top is now an object, set value
					else if (this.__top().isObject())
					{
						this.__top().need(__Exp__.COMMA_OR_END);
						this._flush.addLast(new BaseDecoderBit(
							BaseDecoderKind.POP_ARRAY_ADD_OBJECT_KEYVAL));
					}
					
					// Top is now an array
					else if (this.__top().isArray())
					{
						this.__top().need(__Exp__.COMMA_OR_END);
						this._flush.addLast(new BaseDecoderBit(
							BaseDecoderKind.POP_ARRAY_ADD_ARRAY));
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
				if (this.__top().want(__Exp__.VALUE) || this.__top().want(
					__Exp__.VALUE_OR_END))
				{
					// Expected value type to use
					char cz = ds.charAt(0);
					JsonValue vv;
					try
					{
						vv = (cz == 't' ? JsonValue.TRUE :
							(cz == 'f' ? JsonValue.FALSE :
								(cz == 'n' ? JsonValue.NULL :
									new ImplValueNumber(ds))));
					}
					
					// Bad number
					catch (NumberFormatException nfe)
					{
						throw this.__fail(nfe, "litnan", ds);
					}
					
					// Add to object
					if (this.__top().isObject())
						this._flush.addLast(new BaseDecoderBit(
							BaseDecoderKind.ADD_OBJECT_KEYVAL, vv));
						
						// Add to array
					else if (this.__top().isArray())
						this._flush.addLast(
							new BaseDecoderBit(BaseDecoderKind.ADD_ARRAY_VALUE,
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
				if (this.__top().want(__Exp__.KEY_OR_END) || this.__top().want(
					__Exp__.KEY))
				{
					// Declare key
					if (this.__top().isObject())
						this._flush.addLast(
							new BaseDecoderBit(BaseDecoderKind.DECLARE_KEY,
								ds));
						
						// Not a key!?
					else
						throw new RuntimeException("incknoto");
					
					// Want colon now, which after that is some kind of value
					this.__top().need(__Exp__.COLON);
				}
				
				// Expecting a value
				else if (this.__top().want(__Exp__.VALUE) || this.__top().want(
					__Exp__.VALUE_OR_END))
				{
					// Object value
					if (this.__top().isObject())
						this._flush.addLast(new BaseDecoderBit(
							BaseDecoderKind.ADD_OBJECT_KEYVAL,
							new ImplValueString(ds)));
						
						// Array entry
					else if (this.__top().isArray())
						this._flush.addLast(
							new BaseDecoderBit(BaseDecoderKind.ADD_ARRAY_VALUE,
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
				if (dt != null && dt != BaseDecoderType.END_OF_STREAM)
					throw new RuntimeException(
						String.format("unkt", dt.name()));
				
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
	
}

