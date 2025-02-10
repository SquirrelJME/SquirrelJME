// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.jsr353;

import com.oracle.json.JsonArray;
import com.oracle.json.JsonException;
import com.oracle.json.JsonNumber;
import com.oracle.json.JsonObject;
import com.oracle.json.JsonString;
import com.oracle.json.JsonValue;
import com.oracle.json.stream.JsonGenerationException;
import com.oracle.json.stream.JsonGenerator;
import java.io.IOException;
import java.io.Writer;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * This has the capability to write JSON data to an output stream.
 *
 * @since 2014/08/07
 */
public class ImplGenerator
	implements JsonGenerator
{
	/** Indentation level. */
	public static final int INDENT = 2;
	
	/** Lock. */
	private final Object _lock;
	
	/** Stream to print to. */
	private final Writer _pw;
	
	/** Indent writer for pretty printing .*/
	private final IndentedWriter _iw;
	
	/** Has been closed. */
	private boolean _closed;
	
	/** Done writing. */
	private boolean _done;
	
	/** Scope stack. */
	private List<Scope> _ss;
	
	/** Scope counts in specified element. */
	private List<Integer> _sc;
	
	/** Did this {@link IOException}? */
	private volatile boolean _didFail;
	
	/**
	 * Initializes the generator to write the specified output stream.
	 *
	 * @param __w Output stream to write data to.
	 * @param __pretty Whether the output looks really nice.
	 * @since 2014/08/07
	 */
	public ImplGenerator(Writer __w, boolean __pretty)
	{
		// Cannot be null
		if (__w == null)
			throw new NullPointerException("No output stream specified.");
		
		// Pretty printing?
		if (__pretty)
			this._iw = new IndentedWriter(__w, ' ', 0);
		else
			this._iw = null;
		
		// Create writer
		this._pw = (this._iw == null ? __w : this._iw);
		
		// Init lock
		this._lock = new Object();
		
		// Working variables
		this._ss = new LinkedList<Scope>();
		this._sc = new LinkedList<Integer>();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2014/08/07
	 */
	@Override
	public void close()
	{
		synchronized (this._lock)
		{
			// Ignore if closed already
			if (this._closed)
				return;
			
			// Close
			this._closed = true;
			try
			{
				this._pw.close();
			}
			catch (IOException ignored)
			{
				this._didFail = true;
			}
			
			// Possible there were some errors in it.
			if (this._didFail)
				throw new JsonException("There were Writer errors.");
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2014/08/07
	 */
	@Override
	public void flush()
	{
		synchronized (this._lock)
		{
			// Error if closed
			if (this._closed)
				throw new JsonException("Generator has been closed.");
			
			// Flush substream
			this.__flush();
		}
	}
	
	/**
	 * Checks whether a comma needs to be prepended, and increases the current
	 * count set.
	 *
	 * @return {@code this}.
	 * @since 2014/08/07
	 */
	private JsonGenerator doCommaCount()
	{
		// There are elements (not in no context)
		if (!this._sc.isEmpty())
		{
			// Prepend comma
			if (this._sc.get(0) > 0)
			{
				this.__print(',');
				if (this._iw != null)
					this.__printLn();
			}
			
			// Increase count
			this._sc.set(0, this._sc.get(0) + 1);
		}
		
		// Self
		return this.__check();
	}
	
	/**
	 * Checks whether the specified state is valid for addition.
	 *
	 * @param __obj Wanting to add to object.
	 * @param __v Value to check.
	 * @since 2014/08/08 
	 */
	private boolean checkState(boolean __obj, JsonValue __v)
	{
		// Cannot be null
		if (__v == null)
			throw new NullPointerException("No value specified.");
		
		// Never valid if done
		if (this._done)
			return false;
		
		// In no scope
		if (this._ss.isEmpty())
		{
			// Object is always invalid
			if (__obj)
				return false;
			
			// Otherwise OK as long as Array or Object
			if ((__v instanceof JsonObject) || (__v instanceof JsonArray))
				return true;
			
			// Not a structured type, fail
			return false;
		}
		
		// In a scope, only if matched type
		else
			return (this._ss.get(0) == Scope.OBJECT) == __obj;
	}
	
	/**
	 * Places the key name without any state considerations.
	 *
	 * @param __n Key name to use.
	 * @since 2014/08/08
	 */
	private void putKeyName(String __n)
	{
		// Cannot be null
		if (__n == null)
			throw new NullPointerException("No key name specified.");
		
		// Print it
		this.__printf("\"%s\":", ImplValueString.escapedForm(__n));
	}
	
	/**
	 * Writes the specified value, ignoring array or object context.
	 *
	 * @param __v Value to write.
	 * @throws JsonGenerationException On generation errors.
	 * @since 2014/08/07
	 */
	private void statelessWriteVal(JsonValue __v)
	{
		// Cannot be null
		if (__v == null)
			throw new NullPointerException("No value to write.");
		
		// True
		if (__v == JsonValue.TRUE)
			this.__print("true");
		
		// False
		else if (__v == JsonValue.FALSE)
			this.__print("false");
		
		// Null
		else if (__v == JsonValue.NULL)
			this.__print("null");
		
		// Number
		else if (__v instanceof JsonNumber)
		{
			JsonNumber jv = (JsonNumber)__v;
			this.__print(jv.toString());
		}
		
		// String
		else if (__v instanceof JsonString)
		{
			JsonString jv = (JsonString)__v;
			this.__printf("\"%s\"",
				ImplValueString.escapedForm(jv.getString()));
		}
		
		// Array
		else if (__v instanceof JsonArray)
		{
			JsonArray jv = (JsonArray)__v;
			
			// Key would have been placed (maybe)
			this.statelessStartX(null, Scope.ARRAY);
			
			// Write values
			for (JsonValue v : jv)
				this.write(v);
			
			// End
			this.writeEnd();
		}
		
		// Object
		else if (__v instanceof JsonObject)
		{
			JsonObject jv = (JsonObject)__v;
			
			// The key would have already be prepended
			this.statelessStartX(null, Scope.OBJECT);
			
			// Go through object values and write them
			for (Map.Entry<String, JsonValue> e : jv.entrySet())
				this.write(e.getKey(), e.getValue());
			
			// End current scope
			this.writeEnd();
		}
		
		// Unknown
		else
			throw new JsonGenerationException("Unknown type for generation.");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2014/08/07
	 */
	@Override
	public JsonGenerator write(boolean __v)
	{
		synchronized (this._lock)
		{
			return this.write(__v ? JsonValue.TRUE : JsonValue.FALSE);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2014/08/07
	 */
	@Override
	public JsonGenerator write(double __v)
	{
		synchronized (this._lock)
		{
			// Write as normalized value
			return this.write(new ImplValueNumber(__v));
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2014/08/07
	 */
	@Override
	public JsonGenerator write(int __v)
	{
		synchronized (this._lock)
		{
			// Write as normalized value
			return this.write(new ImplValueNumber(__v));
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2014/08/07
	 */
	@Override
	public JsonGenerator write(JsonValue __v)
	{
		synchronized (this._lock)
		{
			// Cannot be null.
			if (__v == null)
				throw new NullPointerException("No value specified.");
		
			// Illegal state?
			if (!this.checkState(false, __v))
				throw new JsonGenerationException(
					"Invoked in incorrect context.");
		
			// Comma count
			this.doCommaCount();
		
			// Write value without any state
			this.statelessWriteVal(__v);
		
			// Return this
			return this.__check();
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2014/08/07
	 */
	@Override
	public JsonGenerator write(long __v)
	{
		synchronized (this._lock)
		{
			// Write as normalized value
			return this.write(new ImplValueNumber(__v));
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2014/08/07
	 */
	@Override
	public JsonGenerator write(String __v)
	{
		synchronized (this._lock)
		{
			return this.write(new ImplValueString(__v));
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2014/08/07
	 */
	@Override
	public JsonGenerator write(String __n, boolean __v)
	{
		synchronized (this._lock)
		{
			return this.write(__n, (__v ? JsonValue.TRUE : JsonValue.FALSE));
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2014/08/07
	 */
	@Override
	public JsonGenerator write(String __n, double __v)
	{
		synchronized (this._lock)
		{
			// Write as normalized value
			return this.write(__n, new ImplValueNumber(__v));
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2014/08/07
	 */
	@Override
	public JsonGenerator write(String __n, int __v)
	{
		synchronized (this._lock)
		{
			// Write as normalized value
			return this.write(__n, new ImplValueNumber(__v));
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2014/08/07
	 */
	@Override
	public JsonGenerator write(String __n, JsonValue __v)
	{
		synchronized (this._lock)
		{
			// Cannot be null
			if (__n == null)
				throw new NullPointerException("No key name specified.");
			if (__v == null)
				throw new NullPointerException("No value specified.");
		
			// Illegal state?
			if (!this.checkState(true, __v))
				throw new JsonGenerationException("Invoked in incorrect " +
					"context.");
		
			// Comma count
			this.doCommaCount();
		
			// Write key then stateless value
			this.putKeyName(__n);
			this.statelessWriteVal(__v);
		
			// Return this
			return this.__check();
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2014/08/07
	 */
	@Override
	public JsonGenerator write(String __n, long __v)
	{
		synchronized (this._lock)
		{
			// Write as normalized value
			return this.write(__n, new ImplValueNumber(__v));
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2014/08/07
	 */
	@Override
	public JsonGenerator write(String __n, String __v)
	{
		synchronized (this._lock)
		{
			return this.write(__n, new ImplValueString(__v));
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2014/08/07
	 */
	@Override
	public JsonGenerator writeEnd()
	{
		synchronized (this._lock)
		{
			// Must be in a context
			if (this._done || this._ss.isEmpty())
				throw new JsonGenerationException("Not within a context.");
		
			// Character depends on current scope
			char e = (this._ss.get(0) == Scope.ARRAY ? ']' : '}');
		
			// Print end
			if (this._iw != null)
			{
				this._iw.addIndent(-ImplGenerator.INDENT);
				this.__printLn();
			}
			this.__print(e);
			
			// Down the stack
			this._ss.remove(0);
			this._sc.remove(0);
		
			// If the stack was cleared, cannot write anymore
			if (this._ss.isEmpty())
			{
				this._done = true;
			
				// Ending newline
				if (this._iw != null)
					this.__printLn();
			}
			
			// Must be flushed, otherwise the printwriter will just hold
			// onto some data
			this.__flush();
		
			// Self
			return this.__check();
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2014/08/07
	 */
	@Override
	public JsonGenerator writeNull()
	{
		synchronized (this._lock)
		{
			return this.write(JsonValue.NULL);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2014/08/07
	 */
	@Override
	public JsonGenerator writeNull(String __n)
	{
		synchronized (this._lock)
		{
			return this.write(__n, JsonValue.NULL);
		}
	}
	
	/**
	 * Starts X, but is stateless.
	 *
	 * @param __n Key for object value (optional).
	 * @param __s Scope to use.
	 * @since 2014/08/08
	 */
	private JsonGenerator statelessStartX(String __n, Scope __s)
	{
		// Cannot be null
		if (__s == null)
			throw new NullPointerException("No scope specified.");
	
		// Print
		if (__n != null)
			this.putKeyName(__n);
		this.__print((__s == Scope.ARRAY ? '[' : '{'));
		if (this._iw != null)
		{
			this._iw.addIndent(ImplGenerator.INDENT);
			this.__printLn();
		}
	
		// Push in
		this._ss.add(0, __s);
		this._sc.add(0, 0);
	
		// Error?
		if (this._didFail)
			throw new JsonException("Error in output stream.");
	
		// Self
		return this.__check();
	}
	
	/**
	 * Starts and entered a state (from nothing or an array).
	 *
	 * @param __s Scope to write.
	 * @return {@code this}.
	 * @since 2014/08/07
	 */
	private JsonGenerator writeStartX(Scope __s)
	{
		// Check context
		if (this._done || (!this._ss.isEmpty() &&
			this._ss.get(0) != Scope.ARRAY))
			throw new JsonGenerationException("Not within valid context.");
		
		// Need to increase count or add comma
		this.doCommaCount();
		
		// Stateless
		return this.statelessStartX(null, __s);
	}
	
	/**
	 * Starts and entered a state (from an object).
	 *
	 * @param __n Name of key to use.
	 * @param __s Scope to write.
	 * @return {@code this}.
	 * @since 2014/08/07
	 */
	private JsonGenerator writeStartKeyedX(String __n, Scope __s)
	{
		// Must be in object
		if (this._done || this._ss.isEmpty() ||
			this._ss.get(0) != Scope.OBJECT)
			throw new JsonGenerationException("Not within valid context.");
		
		// Need to increase count or add comma
		this.doCommaCount();
		
		// Stateless
		return this.statelessStartX(__n, __s);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2014/08/07
	 */
	@Override
	public JsonGenerator writeStartArray()
	{
		synchronized (this._lock)
		{
			return this.writeStartX(Scope.ARRAY);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2014/08/07
	 */
	@Override
	public JsonGenerator writeStartArray(String __n)
	{
		synchronized (this._lock)
		{
			// Cannot be null
			if (__n == null)
				throw new NullPointerException("No key name specified.");
			
			// Enter array
			return this.writeStartKeyedX(__n, Scope.ARRAY);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2014/08/07
	 */
	@Override
	public JsonGenerator writeStartObject()
	{
		synchronized (this._lock)
		{
			return this.writeStartX(Scope.OBJECT);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2014/08/07
	 */
	@Override
	public JsonGenerator writeStartObject(String __n)
	{
		synchronized (this._lock)
		{
			// Cannot be null
			if (__n == null)
				throw new NullPointerException("No key name specified.");
			
			// Enter object
			return this.writeStartKeyedX(__n, Scope.OBJECT);
		}
	}
	
	/**
	 * Checks if the generator's PrintWriter failed for some reason.
	 *
	 * @return {@code this}.
	 * @since 20156/06/13
	 */
	private JsonGenerator __check()
	{
		if (this._didFail)
			throw new JsonException("pwerr");
		
		// Self
		return this;
	}
	
	/**
	 * Flushes the stream.
	 * 
	 * @since 2022/07/12
	 */
	private void __flush()
	{
		try
		{
			this._pw.flush();
		}
		catch (IOException ignored)
		{
			this._didFail = true;
		}
	}
	
	/**
	 * Prints the given value.
	 * 
	 * @param __v The value to print.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/07/12
	 */
	private void __print(Object __v)
		throws NullPointerException
	{
		if (__v == null)
			throw new NullPointerException("NARG");
		
		try
		{
			this._pw.append(__v.toString());
		}
		catch (IOException ignored)
		{
			this._didFail = true;
		}
	}
	
	/**
	 * Prints the given formatted string.
	 * 
	 * @param __fmt The value to print.
	 * @param __args The arguments to the formatter.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/07/12
	 */
	private void __printf(String __fmt, Object... __args)
		throws NullPointerException
	{
		this.__print(String.format(__fmt, __args));
	}
	
	/**
	 * Prints a line to the writer.
	 * 
	 * @since 2022/07/12
	 */
	private void __printLn()
	{
		try
		{
			this._pw.append(System.getProperty("line.ending"));
		}
		catch (IOException ignored)
		{
			this._didFail = true;
		}
	}
	
	/**
	 * This is a scope for generation
	 *
	 * @since 2014/08/07
	 */
	private enum Scope
	{
		/** Object scope. */
		OBJECT,
		
		/** Array scope. */
		ARRAY,
		
		/** End. */
		;
	}
}

