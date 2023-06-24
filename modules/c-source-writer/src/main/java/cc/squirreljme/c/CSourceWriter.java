// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.c;

import cc.squirreljme.c.std.CFunctionProvider;
import java.io.IOException;
import java.util.List;

/**
 * Writer for C source code.
 *
 * @since 2023/05/28
 */
public interface CSourceWriter
{
	/**
	 * Writes the given array to the output.
	 * 
	 * @param __values The values to write.
	 * @return {@code this}.
	 * @throws IOException On write errors.
	 * @since 2023/05/29
	 */
	CSourceWriter array(Object... __values)
		throws IOException;
	
	/**
	 * Writes the given array to the output.
	 * 
	 * @param __values The values to write.
	 * @return {@code this}.
	 * @throws IOException On write errors.
	 * @since 2023/05/29
	 */
	CSourceWriter array(boolean... __values)
		throws IOException;
	
	/**
	 * Writes the given array to the output.
	 * 
	 * @param __values The values to write.
	 * @return {@code this}.
	 * @throws IOException On write errors.
	 * @since 2023/05/29
	 */
	CSourceWriter array(byte... __values)
		throws IOException;
	
	/**
	 * Writes the given array to the output.
	 * 
	 * @param __values The values to write.
	 * @return {@code this}.
	 * @throws IOException On write errors.
	 * @since 2023/05/29
	 */
	CSourceWriter array(short... __values)
		throws IOException;
	
	/**
	 * Writes the given array to the output.
	 * 
	 * @param __values The values to write.
	 * @return {@code this}.
	 * @throws IOException On write errors.
	 * @since 2023/05/29
	 */
	CSourceWriter array(char... __values)
		throws IOException;
	
	/**
	 * Writes the given array to the output.
	 * 
	 * @param __values The values to write.
	 * @return {@code this}.
	 * @throws IOException On write errors.
	 * @since 2023/05/29
	 */
	CSourceWriter array(int... __values)
		throws IOException;
	
	/**
	 * Writes the given array to the output.
	 * 
	 * @param __values The values to write.
	 * @return {@code this}.
	 * @throws IOException On write errors.
	 * @since 2023/05/29
	 */
	CSourceWriter array(long... __values)
		throws IOException;
	
	/**
	 * Writes the given array to the output.
	 * 
	 * @param __values The values to write.
	 * @return {@code this}.
	 * @throws IOException On write errors.
	 * @since 2023/05/29
	 */
	CSourceWriter array(float... __values)
		throws IOException;
	
	/**
	 * Writes the given array to the output.
	 * 
	 * @param __values The values to write.
	 * @return {@code this}.
	 * @throws IOException On write errors.
	 * @since 2023/05/29
	 */
	CSourceWriter array(double... __values)
		throws IOException;
	
	/**
	 * Writes the given array to the output.
	 * 
	 * @param __values The values to write.
	 * @return {@code this}.
	 * @throws IOException On write errors.
	 * @since 2023/05/29
	 */
	CSourceWriter array(List<?> __values)
		throws IOException;
	
	/**
	 * Writes the given character.
	 * 
	 * @param __c The character to write.
	 * @return {@code this}.
	 * @throws IOException On write errors.
	 * @since 2023/05/29
	 */
	CSourceWriter character(char __c)
		throws IOException;
	
	/**
	 * Opens a curly block.
	 * 
	 * @return The block to open.
	 * @throws IOException On 
	 * @since 2023/05/29
	 */
	CBlock curly()
		throws IOException;
	
	/**
	 * Declares the given function and returns a block for writing a function.
	 *
	 * @param __function The function to be declared.
	 * @return {@link this}.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/06/04
	 */
	CSourceWriter declare(CFunctionType __function)
		throws IOException, NullPointerException;
	
	/**
	 * Declares a variable without setting it to anything.
	 * 
	 * @param __var The variable to declare.
	 * @return {@code this}.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/06/19
	 */
	CSourceWriter declare(CVariable __var)
		throws IOException, NullPointerException;
	
	/**
	 * Declares a variable block.
	 * 
	 * @param <B> The block type.
	 * @param __blockType The block type.
	 * @param __var The variable to declare.
	 * @return The block for the given variable.
	 * @throws IOException On write errors. 
	 * @throws NullPointerException On null arguments.
	 * @since 2023/06/19
	 */
	<B extends CBlock> B declare(Class<B> __blockType, CVariable __var)
		throws IOException, NullPointerException;
	
	/**
	 * Defines the given struct.
	 * 
	 * @param __what What is to be defined?
	 * @return {@code this}.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/06/03
	 */
	CSourceWriter define(CStructType __what)
		throws IOException, NullPointerException;
	
	/**
	 * Start on a fresh line.
	 * 
	 * @throws IOException On write errors.
	 * @return {@code this}.
	 * @since 2023/05/28
	 */
	CSourceWriter freshLine()
		throws IOException;
	
	/**
	 * Writes a function.
	 * 
	 * @param __modifier The function modifier.
	 * @param __name The name of the function.
	 * @param __returnVal The return value.
	 * @param __arguments The arguments to the function.
	 * @return {@code this}.
	 * @throws IOException On write errors.
	 * @throws NullPointerException If no name was specified.
	 * @since 2023/05/30
	 */
	CSourceWriter function(CModifier __modifier, CIdentifier __name,
		CType __returnVal, CVariable... __arguments)
		throws IOException, NullPointerException;
	
	/**
	 * Performs a function call.
	 * 
	 * @param __function The function to call.
	 * @param __args The arguments to the call.
	 * @return {@code this}.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/05/31
	 */
	CSourceWriter functionCall(CFunctionType __function, CExpression... __args)
		throws IOException, NullPointerException;
	
	/**
	 * Performs a function call.
	 * 
	 * @param __function The function to call.
	 * @param __args The arguments to the call.
	 * @return {@code this}.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/06/24
	 */
	CSourceWriter functionCall(CFunctionProvider __function,
		CExpression... __args)
		throws IOException, NullPointerException;
	
	/**
	 * Defines a function.
	 *
	 * @param __function@return The block for writing functions.
	 * @throws IOException On write errors.
	 * @throws NullPointerException If no name was specified.
	 * @since 2023/05/30
	 */
	CFunctionBlock functionDefine(CFunctionType __function)
		throws IOException, NullPointerException;
	
	/**
	 * Writes a function prototype.
	 * 
	 * @param __modifier The function modifier.
	 * @param __name The name of the function.
	 * @param __returnVal The return value.
	 * @param __arguments The arguments to the function.
	 * @return {@code this}.
	 * @throws IOException On write errors.
	 * @throws NullPointerException If no name was specified.
	 * @since 2023/05/30
	 */
	CSourceWriter functionPrototype(CModifier __modifier, CIdentifier __name,
		CType __returnVal, CVariable... __arguments)
		throws IOException, NullPointerException;
	
	/**
	 * Writes the specified number.
	 * 
	 * @param __number The number to write.
	 * @return {@code this}.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/05/29
	 */
	CSourceWriter number(Number __number)
		throws IOException, NullPointerException;
	
	/**
	 * Writes the specified number.
	 * 
	 * @param __type The type of number this is.
	 * @param __number The number to write.
	 * @return {@code this}.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/05/29
	 */
	CSourceWriter number(CNumberType __type, Number __number)
		throws IOException, NullPointerException;
	
	/**
	 * Writes a preprocessor define.
	 *
	 * @param __symbol The symbol to define.
	 * @param __expression The tokens to define.
	 * @return {@code this}.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/05/29
	 */
	CSourceWriter preprocessorDefine(CIdentifier __symbol,
		CExpression __expression)
		throws IOException, NullPointerException;
	
	/**
	 * Adds an if check for preprocessing.
	 *
	 * @param __expression The tokens to use for the check.
	 * @return The opened block.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/05/29
	 */
	CPPBlock preprocessorIf(CExpression __expression)
		throws IOException, NullPointerException;
	
	/**
	 * Writes a preprocessor include.
	 * 
	 * @param __fileName The file name to use.
	 * @return {@code this}.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/05/29
	 */
	CSourceWriter preprocessorInclude(CFileName __fileName)
		throws IOException, NullPointerException;
	
	/**
	 * Outputs a preprocessor line.
	 * 
	 * @param __directive The preprocesor symbol. 
	 * @param __tokens The token to use.
	 * @return {@code this}.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/05/28
	 */
	CSourceWriter preprocessorLine(CPPDirective __directive,
		Object... __tokens)
		throws IOException, NullPointerException;
	
	/**
	 * Writes a preprocessor undefine.
	 * 
	 * @param __symbol The symbol to undefine.
	 * @return {@code this}.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/05/29
	 */
	CSourceWriter preprocessorUndefine(CIdentifier __symbol)
		throws IOException, NullPointerException;
	
	/**
	 * Writes a return from a function.
	 *
	 * @param __expression The tokens to write, optional and if {@code null}
	 * will not have one.
	 * @return {@code this}.
	 * @throws IOException On write errors.
	 * @since 2023/06/03
	 */
	CSourceWriter returnValue(CExpression __expression)
		throws IOException;
	
	/**
	 * Surrounds the set of tokens with a parenthesis, with an optional
	 * prefix.
	 * 
	 * @param __prefix The of type, may be {@code null}.
	 * @param __tokens The tokens to wrap.
	 * @return {@code this}.
	 * @throws IOException On write errors.
	 * @since 2023/05/29
	 */
	CSourceWriter surround(String __prefix, Object... __tokens)
		throws IOException;
	
	/**
	 * Surround with parenthesis, potentially delimited.
	 * 
	 * @param __prefix The prefix to use.
	 * @param __delim The delimiter to use.
	 * @param __tokens The tokens to wrap and delimit.
	 * @return {@code this}.
	 * @throws IOException On write errors.
	 * @throws NullPointerException If no delimiter was specified.
	 * @since 3023/05/30
	 */
	CSourceWriter surroundDelimited(String __prefix, String __delim,
		Object... __tokens)
		throws IOException, NullPointerException;
	
	/**
	 * Writes a single token to the output.
	 *
	 * @param __token The token to write.
	 * @return {@code this}.
	 * @throws IllegalArgumentException If newlines or tabs were printed.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/05/29
	 */
	CSourceWriter token(CharSequence __token)
		throws IllegalArgumentException, IOException, NullPointerException;
	
	/**
	 * Writes a single token to the output.
	 *
	 * @param __token The token to write.
	 * @param __forceNewline Force a newline to be written?
	 * @return {@code this}.
	 * @throws IllegalArgumentException If newlines or tabs were printed.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/06/23
	 */
	CSourceWriter token(CharSequence __token, boolean __forceNewline)
		throws IllegalArgumentException, IOException, NullPointerException;
	
	/**
	 * Writes a single token to the output.
	 *
	 * @param __token The token to write.
	 * @return {@code this}.
	 * @throws IllegalArgumentException If the token type is not valid.
	 * @throws IOException On write errors.
	 * @since 2023/05/29
	 */
	CSourceWriter token(Object __token)
		throws IllegalArgumentException, IOException;
	
	/**
	 * Writes the specified tokens to the output.
	 *
	 * @param __tokens The tokens to write.
	 * @return {@code this}.
	 * @throws IOException On write errors.
	 * @since 2023/05/29
	 */
	CSourceWriter tokens(Object... __tokens)
		throws IOException;
	
	/**
	 * Sets the value of this variable.
	 * 
	 * @param __var The variable to set.
	 * @param __value The value to set it as.
	 * @return {@code this}.
	 * @throws IllegalArgumentException If the value is not correct.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/06/19
	 */
	CSourceWriter variableSet(CVariable __var, CExpression __value)
		throws IllegalArgumentException, IOException, NullPointerException;
}
