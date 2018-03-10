// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.javac.token;

/**
 * This represents the type of token which was parsed.
 *
 * @since 2017/09/04
 */
public enum BottomType
{
	/** A comment. */
	COMMENT,
	
	/** Identifier. */
	IDENTIFIER,
	
	/** Keyword abstract. */
	KEYWORD_ABSTRACT,
	
	/** Keyword assert. */
	KEYWORD_ASSERT,
	
	/** Keyword boolean. */
	KEYWORD_BOOLEAN,
	
	/** Keyword break. */
	KEYWORD_BREAK,
	
	/** Keyword byte. */
	KEYWORD_BYTE,
	
	/** Keyword case. */
	KEYWORD_CASE,
	
	/** Keyword catch. */
	KEYWORD_CATCH,
	
	/** Keyword char. */
	KEYWORD_CHAR,
	
	/** Keyword class. */
	KEYWORD_CLASS,
	
	/** Keyword continue. */
	KEYWORD_CONTINUE,
	
	/** Keyword default. */
	KEYWORD_DEFAULT,
	
	/** Keyword do. */
	KEYWORD_DO,
	
	/** Keyword double. */
	KEYWORD_DOUBLE,
	
	/** Keyword else. */
	KEYWORD_ELSE,
	
	/** Keyword enum. */
	KEYWORD_ENUM,
	
	/** Keyword extends. */
	KEYWORD_EXTENDS,
	
	/** Keyword final. */
	KEYWORD_FINAL,
	
	/** Keyword finally. */
	KEYWORD_FINALLY,
	
	/** Keyword float. */
	KEYWORD_FLOAT,
	
	/** Keyword for. */
	KEYWORD_FOR,
	
	/** Keyword if. */
	KEYWORD_IF,
	
	/** Keyword implements. */
	KEYWORD_IMPLEMENTS,
	
	/** Keyword import. */
	KEYWORD_IMPORT,
	
	/** Keyword instanceof. */
	KEYWORD_INSTANCEOF,
	
	/** Keyword int. */
	KEYWORD_INT,
	
	/** Keyword interface. */
	KEYWORD_INTERFACE,
	
	/** Keyword long. */
	KEYWORD_LONG,
	
	/** Keyword native. */
	KEYWORD_NATIVE,
	
	/** Keyword new. */
	KEYWORD_NEW,
	
	/** Keyword package. */
	KEYWORD_PACKAGE,
	
	/** Keyword private. */
	KEYWORD_PRIVATE,
	
	/** Keyword protected. */
	KEYWORD_PROTECTED,
	
	/** Keyword public. */
	KEYWORD_PUBLIC,
	
	/** Keyword return. */
	KEYWORD_RETURN,
	
	/** Keyword short. */
	KEYWORD_SHORT,
	
	/** Keyword static. */
	KEYWORD_STATIC,
	
	/** Keyword strictfp. */
	KEYWORD_STRICTFP,
	
	/** Keyword super. */
	KEYWORD_SUPER,
	
	/** Keyword switch. */
	KEYWORD_SWITCH,
	
	/** Keyword synchronized. */
	KEYWORD_SYNCHRONIZED,
	
	/** Keyword this. */
	KEYWORD_THIS,
	
	/** Keyword throw. */
	KEYWORD_THROW,
	
	/** Keyword throws. */
	KEYWORD_THROWS,
	
	/** Keyword transient. */
	KEYWORD_TRANSIENT,
	
	/** Keyword try. */
	KEYWORD_TRY,
	
	/** Keyword void. */
	KEYWORD_VOID,
	
	/** Keyword volatile. */
	KEYWORD_VOLATILE,
	
	/** Keyword while. */
	KEYWORD_WHILE,
	
	/** The null literal. */
	LITERAL_NULL,
	
	/** The false literal. */
	LITERAL_FALSE,
	
	/** The true literal. */
	LITERAL_TRUE,
	
	/** Binary integer literal. */
	LITERAL_BINARY_INTEGER,
	
	/** Octal integer literal. */
	LITERAL_OCTAL_INTEGER,
	
	/** Decimal integer literal. */
	LITERAL_DECIMAL_INTEGER,
	
	/** Hexadecimal integer literal. */
	LITERAL_HEXADECIMAL_INTEGER,
	
	/** Decimal float literal. */
	LITERAL_DECIMAL_FLOAT,
	
	/** Hexadecimal float literal. */
	LITERAL_HEXADECIMAL_FLOAT,
	
	/** String literal. */
	LITERAL_STRING,
	
	/** Compare for equality. */
	COMPARE_EQUALS,
	
	/** Compare for inequality. */
	COMPARE_NOT_EQUALS,
	
	/** Compare less than. */
	COMPARE_LESS_THAN,
	
	/** Compare less than or equals to. */
	COMPARE_LESS_OR_EQUAL,
	
	/** Compare greater than. */
	COMPARE_GREATER_THAN,
	
	/** Compare greater than or equal. */
	COMPARE_GREATER_OR_EQUAL,
	
	/** Compare AND. */
	COMPARE_AND,
	
	/** Compare OR. */
	COMPARE_OR,
	
	/** Assign operator. */
	OPERATOR_ASSIGN,
	
	/** Add operator. */
	OPERATOR_PLUS,
	
	/** Add and assign operator. */
	OPERATOR_PLUS_ASSIGN,
	
	/** Subtract operator. */
	OPERATOR_MINUS,
	
	/** Subtract and assign operator. */
	OPERATOR_MINUS_ASSIGN,
	
	/** Multiply operator. */
	OPERATOR_MULTIPLY,
	
	/** Multiply and assign operator. */
	OPERATOR_MULTIPLY_ASSIGN,
	
	/** Divide operator. */
	OPERATOR_DIVIDE,
	
	/** Divide and assign operator. */
	OPERATOR_DIVIDE_ASSIGN,
	
	/** Remainder operator. */
	OPERATOR_REMAINDER,
	
	/** Remainder and assign operator. */
	OPERATOR_REMAINDER_ASSIGN,
	
	/** Shift left. */
	OPERATOR_SHIFT_LEFT,
	
	/** Shift left and assign. */
	OPERATOR_SHIFT_LEFT_ASSIGN,
	
	/** Signed Shift right. */
	OPERATOR_SSHIFT_RIGHT,
	
	/** Signed Shift right and assign. */
	OPERATOR_SSHIFT_RIGHT_ASSIGN,
	
	/** Unsigned Shift right. */
	OPERATOR_USHIFT_RIGHT,
	
	/** Unsigned Shift right and assign. */
	OPERATOR_USHIFT_RIGHT_ASSIGN,
	
	/** Ternary question operator. */
	OPERATOR_TERNARY_QUESTION,
	
	/** The NOT operator. */
	OPERATOR_NOT,
	
	/** The NOT and assign operator. */
	OPERATOR_NOT_ASSIGN,
	
	/** The AND operator. */
	OPERATOR_AND,
	
	/** The AND and assign operator. */
	OPERATOR_AND_ASSIGN,
	
	/** The XOR operator. */
	OPERATOR_XOR,
	
	/** The XOR and assign operator. */
	OPERATOR_XOR_ASSIGN,
	
	/** The OR operator. */
	OPERATOR_OR,
	
	/** The OR and assign operator. */
	OPERATOR_OR_ASSIGN,
	
	/** Bitwise complement. */
	OPERATOR_COMPLEMENT,
	
	/** Increment. */
	OPERATOR_INCREMENT,
	
	/** Decrement. */
	OPERATOR_DECREMENT,
	
	/** Ternary colon operator or label. */
	SYMBOL_COLON,
	
	/** Double colon. */
	SYMBOL_DOUBLE_COLON,
	
	/** Open parenthesis. */
	SYMBOL_OPEN_PARENTHESIS,
	
	/** Closed parenthesis. */
	SYMBOL_CLOSED_PARENTHESIS,
	
	/** Open brace. */
	SYMBOL_OPEN_BRACE,
	
	/** Closed brace. */
	SYMBOL_CLOSED_BRACE,
	
	/** Open bracket. */
	SYMBOL_OPEN_BRACKET,
	
	/** Closed bracket. */
	SYMBOL_CLOSED_BRACKET,
	
	/** Open angle bracket. */
	SYMBOL_OPEN_ANGLE,
	
	/** Closed angle bracket. */
	SYMBOL_CLOSED_ANLGE,
	
	/** Semicolon. */
	SYMBOL_SEMICOLON,
	
	/** Comma. */
	SYMBOL_COMMA,
	
	/** The dot separator. */
	SYMBOL_DOT,
	
	/** The ellipses. */
	SYMBOL_ELLIPSES,
	
	/** Lambda symbol. */
	SYMBOL_LAMBDA,
	
	/** At symbol for annotations. */
	SYMBOL_AT,
	
	/** End. */
	;
	
	/**
	 * Is this a modifier to a class?
	 *
	 * @return If this is a class modifier.
	 * @since 2018/03/10
	 */
	public final boolean isClassModifier()
	{
		switch (this)
		{
			case KEYWORD_ABSTRACT:
			case KEYWORD_FINAL:
			case KEYWORD_PRIVATE:
			case KEYWORD_PROTECTED:
			case KEYWORD_PUBLIC:
			case KEYWORD_STATIC:
			case KEYWORD_STRICTFP:
				return true;
			
			default:
				return false;
		}
	}
	
	/**
	 * Is this a comment?
	 *
	 * @return If this is a comment or not.
	 * @since 2017/09/06
	 */
	public final boolean isComment()
	{
		switch (this)
		{
				// Comments
			case COMMENT:
				return true;
			
				// Not a comment
			default:
				return false;
		}
	}
	
	/**
	 * Is this an identifier?
	 *
	 * @return If this is an identifier.
	 * @since 2018/03/07
	 */
	public final boolean isIdentifier()
	{
		return this == IDENTIFIER;
	}
	
	/**
	 * Is this a keyword?
	 *
	 * @return If this is a keyword.
	 * @since 2018/03/07
	 */
	public final boolean isKeyword()
	{
		switch (this)
		{
			case KEYWORD_ABSTRACT:
			case KEYWORD_ASSERT:
			case KEYWORD_BOOLEAN:
			case KEYWORD_BREAK:
			case KEYWORD_BYTE:
			case KEYWORD_CASE:
			case KEYWORD_CATCH:
			case KEYWORD_CHAR:
			case KEYWORD_CLASS:
			case KEYWORD_CONTINUE:
			case KEYWORD_DEFAULT:
			case KEYWORD_DO:
			case KEYWORD_DOUBLE:
			case KEYWORD_ELSE:
			case KEYWORD_ENUM:
			case KEYWORD_EXTENDS:
			case KEYWORD_FINAL:
			case KEYWORD_FINALLY:
			case KEYWORD_FLOAT:
			case KEYWORD_FOR:
			case KEYWORD_IF:
			case KEYWORD_IMPLEMENTS:
			case KEYWORD_IMPORT:
			case KEYWORD_INSTANCEOF:
			case KEYWORD_INT:
			case KEYWORD_INTERFACE:
			case KEYWORD_LONG:
			case KEYWORD_NATIVE:
			case KEYWORD_NEW:
			case KEYWORD_PACKAGE:
			case KEYWORD_PRIVATE:
			case KEYWORD_PROTECTED:
			case KEYWORD_PUBLIC:
			case KEYWORD_RETURN:
			case KEYWORD_SHORT:
			case KEYWORD_STATIC:
			case KEYWORD_STRICTFP:
			case KEYWORD_SUPER:
			case KEYWORD_SWITCH:
			case KEYWORD_SYNCHRONIZED:
			case KEYWORD_THIS:
			case KEYWORD_THROW:
			case KEYWORD_THROWS:
			case KEYWORD_TRANSIENT:
			case KEYWORD_TRY:
			case KEYWORD_VOID:
			case KEYWORD_VOLATILE:
			case KEYWORD_WHILE:
				return true;
			
			default:
				return false;
		}	
	}
	
	/**
	 * Is this a literal?
	 *
	 * @return If this is a literal.
	 * @since 2018/03/07
	 */
	public final boolean isLiteral()
	{
		switch (this)
		{
			case LITERAL_NULL:
			case LITERAL_FALSE:
			case LITERAL_TRUE:
			case LITERAL_BINARY_INTEGER:
			case LITERAL_OCTAL_INTEGER:
			case LITERAL_DECIMAL_INTEGER:
			case LITERAL_HEXADECIMAL_INTEGER:
			case LITERAL_DECIMAL_FLOAT:
			case LITERAL_HEXADECIMAL_FLOAT:
			case LITERAL_STRING:
				return true;
			
			default:
				return false;
		}
	}
	
	/**
	 * Is this the potential start of a class declaration?
	 *
	 * @return If this is the potential start of a class declaration.
	 * @since 2018/03/07
	 */
	public final boolean isPotentialClassStart()
	{
		switch (this)
		{
			case KEYWORD_ABSTRACT:
			case KEYWORD_ENUM:
			case KEYWORD_FINAL:
			case KEYWORD_INTERFACE:
			case KEYWORD_PRIVATE:
			case KEYWORD_PROTECTED:
			case KEYWORD_PUBLIC:
			case KEYWORD_STATIC:
			case KEYWORD_STRICTFP:
			case SYMBOL_AT:
				return true;
			
			default:
				return false;
		}
	}
	
	/**
	 * Is this a word?
	 *
	 * @return If this is a word.
	 * @since 2018/03/07
	 */
	public final boolean isWord()
	{
		return this == IDENTIFIER ||
			this.isKeyword();
	}
}

