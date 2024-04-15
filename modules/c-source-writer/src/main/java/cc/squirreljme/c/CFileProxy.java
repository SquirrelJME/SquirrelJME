// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.c;

import cc.squirreljme.c.out.CPivotPoint;
import cc.squirreljme.c.std.CFunctionProvider;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.IOException;
import java.util.List;

/**
 * Proxy for {@link CFile}, does not close the target C File, this is more
 * intended to be used by any utility classes that would like to use
 * {@code try-with-resources}.
 *
 * @since 2023/06/04
 */
@SuppressWarnings("resource")
public abstract class CFileProxy
	implements CSourceWriter
{
	/** The actual file. */
	final CFile _file;
	
	/**
	 * Initializes the proxy.
	 * 
	 * @param __writer The file to proxy to.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/06/04
	 */
	public CFileProxy(CSourceWriter __writer)
		throws NullPointerException
	{
		if (__writer == null)
			throw new NullPointerException("NARG");
		
		// Try to get the original file
		if (__writer instanceof CFile)
			this._file = ((CFile)__writer);
		else if (__writer instanceof CFileProxy)
			this._file = ((CFileProxy)__writer)._file;
		else
			throw new ClassCastException("CCEE");
		
		// This should not occur, hopefully...
		if (this._file == null)
			throw new NullPointerException("GCGC");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/04
	 */
	@Override
	public CSourceWriter array(Object... __values)
		throws IOException
	{
		this._file.array(__values);
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/04
	 */
	@Override
	public CSourceWriter array(boolean... __values)
		throws IOException
	{
		this._file.array(__values);
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/04
	 */
	@Override
	public CSourceWriter array(byte... __values)
		throws IOException
	{
		this._file.array(__values);
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/04
	 */
	@Override
	public CSourceWriter array(short... __values)
		throws IOException
	{
		this._file.array(__values);
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/04
	 */
	@Override
	public CSourceWriter array(char... __values)
		throws IOException
	{
		this._file.array(__values);
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/04
	 */
	@Override
	public CSourceWriter array(int... __values)
		throws IOException
	{
		this._file.array(__values);
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/04
	 */
	@Override
	public CSourceWriter array(long... __values)
		throws IOException
	{
		this._file.array(__values);
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/04
	 */
	@Override
	public CSourceWriter array(float... __values)
		throws IOException
	{
		this._file.array(__values);
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/04
	 */
	@Override
	public CSourceWriter array(double... __values)
		throws IOException
	{
		this._file.array(__values);
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/04
	 */
	@Override
	public CSourceWriter array(List<?> __values)
		throws IOException
	{
		this._file.array(__values);
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/04
	 */
	@Override
	public CSourceWriter character(char __c)
		throws IOException
	{
		this._file.character(__c);
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/04
	 */
	@Override
	public CBlock curly()
		throws IOException
	{
		return this._file.curly();
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @since 2023/06/04
	 */
	@Override
	public CSourceWriter declare(CFunctionType __function)
		throws IOException, NullPointerException
	{
		this._file.declare(__function);
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/19
	 */
	@Override
	public CSourceWriter declare(CVariable __var)
		throws IOException, NullPointerException
	{
		this._file.declare(__var);
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/19
	 */
	@Override
	public <B extends CBlock> B define(Class<B> __blockType, CVariable __var)
		throws IOException, NullPointerException
	{
		return this._file.define(__blockType, __var);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/04
	 */
	@Override
	public CSourceWriter define(CStructType __what)
		throws IOException, NullPointerException
	{
		this._file.define(__what);
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/04
	 */
	@Override
	public CFunctionBlock define(CFunctionType __function)
		throws IOException, NullPointerException
	{
		return this._file.define(__function);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/08/12
	 */
	@Override
	public CSourceWriter define(CVariable __variable,
		CExpression __expression)
		throws IllegalArgumentException, IOException, NullPointerException
	{
		if (__variable == null || __expression == null)
			throw new NullPointerException("NARG");
		
		this._file.define(__variable, __expression);
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/24
	 */
	@Override
	public CSourceWriter expression(CExpression __expression)
		throws IOException, NullPointerException
	{
		this._file.expression(__expression);
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/04
	 */
	@Override
	public CSourceWriter freshLine()
		throws IOException
	{
		this._file.freshLine();
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/04
	 */
	@Override
	public CSourceWriter functionCall(
		CFunctionType __function, CExpression... __args)
		throws IllegalArgumentException, IOException, NullPointerException
	{
		this._file.functionCall(__function, __args);
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/24
	 */
	@Override
	public CSourceWriter functionCall(CFunctionProvider __function,
		CExpression... __args)
		throws IOException, NullPointerException
	{
		this._file.functionCall(__function.function(), __args);
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/07/15
	 */
	@Override
	public CSourceWriter gotoLabel(String __target)
		throws IOException, NullPointerException
	{
		this._file.gotoLabel(__target);
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/07/15
	 */
	@Override
	public CSourceWriter gotoLabel(CIdentifier __target)
		throws IOException, NullPointerException
	{
		this._file.gotoLabel(__target);
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/08/12
	 */
	@Override
	public CPPBlock headerGuard(String __fileName)
		throws IllegalArgumentException, IOException, NullPointerException
	{
		return this._file.headerGuard(__fileName);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/08/12
	 */
	@Override
	public CPPBlock headerGuard(CFileName __fileName)
		throws IOException, NullPointerException
	{
		return this._file.headerGuard(__fileName);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/07/15
	 */
	@Override
	public CSourceWriter indent(int __by)
		throws IOException
	{
		this._file.indent(__by);
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/07/15
	 */
	@Override
	public CSourceWriter label(String __label)
		throws IOException, NullPointerException
	{
		this._file.label(__label);
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/07/15
	 */
	@Override
	public CSourceWriter label(CIdentifier __label)
		throws IOException, NullPointerException
	{
		this._file.label(__label);
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/07/19
	 */
	@Override
	public CSourceWriter newLine(boolean __force)
		throws IOException
	{
		this._file.newLine(__force);
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/04
	 */
	@Override
	public CSourceWriter number(Number __number)
		throws IOException, NullPointerException
	{
		this._file.number(__number);
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/04
	 */
	@Override
	public CSourceWriter number(CNumberType __type, Number __number)
		throws IOException, NullPointerException
	{
		this._file.number(__type, __number);
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/07/22
	 */
	@Override
	public CSourceWriter pivot(CPivotPoint __pivot)
		throws IOException, NullPointerException
	{
		this._file.pivot(__pivot);
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/04
	 */
	@Override
	public CSourceWriter preprocessorDefine(CIdentifier __symbol,
		CExpression __expression)
		throws IOException, NullPointerException
	{
		this._file.preprocessorDefine(__symbol, __expression);
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/04
	 */
	@Override
	public CPPBlock preprocessorIf(CExpression __expression)
		throws IOException, NullPointerException
	{
		return this._file.preprocessorIf(__expression);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/04
	 */
	@Override
	public CSourceWriter preprocessorInclude(CFileName __fileName)
		throws IOException, NullPointerException
	{
		this._file.preprocessorInclude(__fileName);
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/04
	 */
	@Override
	public CSourceWriter preprocessorLine(CPPDirective __directive,
		Object... __tokens)
		throws IOException, NullPointerException
	{
		this._file.preprocessorLine(__directive, __tokens);
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/04
	 */
	@Override
	public CSourceWriter preprocessorUndefine(CIdentifier __symbol)
		throws IOException, NullPointerException
	{
		this._file.preprocessorUndefine(__symbol);
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/04
	 */
	@Override
	public CSourceWriter returnValue(CExpression __expression)
		throws IOException
	{
		this._file.returnValue(__expression);
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/04
	 */
	@Override
	public CSourceWriter surround(String __prefix, Object... __tokens)
		throws IOException
	{
		this._file.surround(__prefix, __tokens);
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/04
	 */
	@Override
	public CSourceWriter surroundDelimited(String __prefix, String __delim,
		Object... __tokens)
		throws IOException, NullPointerException
	{
		this._file.surroundDelimited(__prefix, __delim, __tokens);
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/04
	 */
	@Override
	public CSourceWriter token(CharSequence __token)
		throws IllegalArgumentException, IOException, NullPointerException
	{
		this._file.token(__token);
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/23
	 */
	@Override
	public CSourceWriter token(CharSequence __token, boolean __forceNewline)
		throws IllegalArgumentException, IOException, NullPointerException
	{
		this._file.token(__token, __forceNewline);
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/04
	 */
	@Override
	public CSourceWriter token(Object __token)
		throws IllegalArgumentException, IOException
	{
		this._file.token(__token);
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/04
	 */
	@Override
	public CSourceWriter tokens(Object... __tokens)
		throws IOException
	{
		this._file.tokens(__tokens);
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/19
	 */
	@Override
	public CSourceWriter variableSet(CVariable __var, CExpression __value)
		throws IllegalArgumentException, IOException, NullPointerException
	{
		this._file.variableSet(__var, __value);
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/07/03
	 */
	@Override
	public CSourceWriter variableSet(CExpression __var, CExpression __value)
		throws IllegalArgumentException, IOException, NullPointerException
	{
		this._file.variableSet(__var, __value);
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/07/04
	 */
	@Override
	public CSourceWriter variableSetViaFunction(CExpression __var,
		CFunctionType __function, CExpression... __args)
		throws IllegalArgumentException, IOException, NullPointerException
	{
		this._file.variableSetViaFunction(__var, __function, __args);
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/07/04
	 */
	@Override
	public CSourceWriter variableSetViaFunction(CExpression __var,
		CFunctionProvider __function, CExpression... __args)
		throws IllegalArgumentException, IOException, NullPointerException
	{
		this._file.variableSetViaFunction(__var, __function, __args);
		return this;
	}
}
