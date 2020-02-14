// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.util;

/**
 * This represents a conversion that can be used for formatted printing.
 *
 * @since 2018/09/28
 */
enum __PrintFConversion__
{
	/** Literal percent. */
	PERCENT,
	
	/** Newline. */
	NEWLINE,
	
	/** Boolean. */
	BOOLEAN,
	
	/** Hashcode. */
	HASHCODE,
	
	/** String. */
	STRING,
	
	/** Character. */
	CHARACTER,
	
	/** Decimal Integer. */
	DECIMAL_INTEGER,
	
	/** Octal Integer. */
	OCTAL_INTEGER,
	
	/** Hexadecimal Integer. */
	HEXADECIMAL_INTEGER,
	
	/** Scientific decimal floating point. */
	SCIENTIFIC_DECIMAL_FLOAT,
	
	/** Decimal floating point. */
	NORMAL_DECIMAL_FLOAT,
	
	/** Floating point in scientific or normal mode. */
	SCIENTIFIC_OR_NORMAL_DECIMAL_FLOAT,
	
	/** Hour: 00 - 23. */
	TIME_MILITARY_HOUR_TWO_DIGIT_LEADING_ZERO,
	
	/** Hour: 01 - 12. */
	TIME_STANDARD_HOUR_TWO_DIGIT_LEADING_ZERO,
	
	/** Hour: 0 - 24. */
	TIME_MILITARY_HOUR,
	
	/** Hour: 1 - 12. */
	TIME_STANDARD_HOUR,
	
	/** Minute: 00 - 59. */
	TIME_MINUTE,
	
	/** Seconds: 00 - 60 (60 for leap seconds). */
	TIME_SECONDS,
	
	/** Milliseconds: 000 - 999. */
	TIME_MILLISECONDS,
	
	/** Locale based am or pm. */
	TIME_AM_PM,
	
	/** RFC 822 zone offset from GMT, such as -0800 for the current zone. */
	TIME_ZONE_RFC822_OFFSET,
	
	/** The abbreviation for this timezone. */
	TIME_ZONE_ABBREVIATION,
	
	/** Seconds since UNIX Epoch. */
	TIME_UNIX_SECONDS,
	
	/** Milliseconds since UNIX Epoch. */
	TIME_UNIX_MILLISECONDS,
	
	/** Local abbreviated month: Jan, Feb. */
	DATE_ABBREVIATED_MONTH_NAME,
	
	/** Local short name of the day of week: Sun, Mon. */
	DATE_ABBREVIATED_DAY_NAME,
	
	/** Century, two digits with leading zero: 00 - 99. */
	DATE_CENTURY_TWO_DIGIT_LEADING_ZERO,
	
	/** Year formatted with at least four digits. */
	DATE_YEAR_FOUR_DIGITS,
	
	/** Last two digits of the year. */
	DATE_YEAR_LAST_TWO_DIGITS,
	
	/** Day of year, with leading zeros as needed: 001 - 366. */
	DATE_DAY_OF_YEAR,
	
	/** Month with two digits with leading zero: 01 - 13. */
	DATE_MONTH_TWO_DIGIT_LEADING_ZERO,
	
	/** Day of month, two digits with leading zero: 01 - 31. */
	DATE_DAY_TWO_DIGIT_LEADING_ZERO,
	
	/** Day of month: 1 - 31. */
	DATE_DAY,
	
	/** 24-hour clock format: %tH:%tM. */
	DATE_TIME_MILITARY,
	
	/** 24-hour clock format with seconds: %tH:%tM:%tS. */
	DATE_TIME_MILITARY_WITH_SECONDS,
	
	/** 12-hour clock format with seconds: %tI:%tM:%tS %Tp. */
	DATE_TIME_STANDARD,
	
	/** Date formatted as %tm/%td/%ty. */
	DATE_MONTH_DAY_YEAR,
	
	/** ISO 8601 Date: %tY-%tm-%td. */
	DATE_ISO8601,
	
	/** Date and time as: %ta %tb %td %tT %tZ %tY. */
	DATE_LONG_FORMAT,
	
	/** End. */
	;
	
	/**
	 * Returns the category of this conversion.
	 *
	 * @return The conversion category.
	 * @since 2018/09/29
	 */
	final __PrintFCategory__ __category()
	{
		switch (this)
		{
			case PERCENT:
				return __PrintFCategory__.PERCENT;
			
			case NEWLINE:
				return __PrintFCategory__.LINE_SEPARATOR;
			
			case BOOLEAN:
			case HASHCODE:
			case STRING:
				return __PrintFCategory__.GENERAL;
				
			case CHARACTER:
				return __PrintFCategory__.CHARACTER;
				
			case DECIMAL_INTEGER:
			case OCTAL_INTEGER:
			case HEXADECIMAL_INTEGER:
				return __PrintFCategory__.INTEGRAL;
				
			case SCIENTIFIC_DECIMAL_FLOAT:
			case NORMAL_DECIMAL_FLOAT:
			case SCIENTIFIC_OR_NORMAL_DECIMAL_FLOAT:
				return __PrintFCategory__.FLOATING_POINT;
				
			case TIME_MILITARY_HOUR_TWO_DIGIT_LEADING_ZERO:
			case TIME_STANDARD_HOUR_TWO_DIGIT_LEADING_ZERO:
			case TIME_MILITARY_HOUR:
			case TIME_STANDARD_HOUR:
			case TIME_MINUTE:
			case TIME_SECONDS:
			case TIME_MILLISECONDS:
			case TIME_AM_PM:
			case TIME_ZONE_RFC822_OFFSET:
			case TIME_ZONE_ABBREVIATION:
			case TIME_UNIX_SECONDS:
			case TIME_UNIX_MILLISECONDS:
			case DATE_ABBREVIATED_MONTH_NAME:
			case DATE_ABBREVIATED_DAY_NAME:
			case DATE_CENTURY_TWO_DIGIT_LEADING_ZERO:
			case DATE_YEAR_FOUR_DIGITS:
			case DATE_YEAR_LAST_TWO_DIGITS:
			case DATE_DAY_OF_YEAR:
			case DATE_MONTH_TWO_DIGIT_LEADING_ZERO:
			case DATE_DAY_TWO_DIGIT_LEADING_ZERO:
			case DATE_DAY:
			case DATE_TIME_MILITARY:
			case DATE_TIME_MILITARY_WITH_SECONDS:
			case DATE_TIME_STANDARD:
			case DATE_MONTH_DAY_YEAR:
			case DATE_ISO8601:
			case DATE_LONG_FORMAT:
				return __PrintFCategory__.DATE_TIME;
				
			default:
				throw new todo.OOPS();
		}
	}
	
	/**
	 * Is the specified flag valid?
	 *
	 * @param __f The flag to check.
	 * @return If it is valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/09/29
	 */
	final boolean __hasFlag(__PrintFFlag__ __f)
		throws NullPointerException
	{
		if (__f == null)
			throw new NullPointerException("NARG");
		
		// If the category does not have it then it will never have it
		__PrintFCategory__ cat = this.__category();
		if (!cat.__hasFlag(__f))
			return false;
		
		// Only this category has exclusions
		if (cat == __PrintFCategory__.INTEGRAL)
			switch (__f)
			{
					// Only valid for octal and hex ints
				case ALTERNATIVE_FORM:
					return this == OCTAL_INTEGER ||
						this == HEXADECIMAL_INTEGER;
				
					// Only valid for decimal ints
				case LOCALE_GROUPING:
					return this == DECIMAL_INTEGER;
				
				default:
					break;
			}
		
		// Is valid!
		return true;
	}
	
	/**
	 * Returns the conversion that is used for the characters.
	 *
	 * @param __f The first character.
	 * @param __s The second character.
	 * @return The conversion or {@code null} if it is not valid.
	 * @since 2018/09/28
	 */
	static final __PrintFConversion__ __decode(int __f, int __s)
	{
		// There are a ton of date formats, so just if a date is used do
		// not bother
		if (__f != 't' && __f != 'T')
			switch (__f)
			{
				case '%':	return PERCENT;
				case 'n':	return NEWLINE;
				case 'B':
				case 'b':	return BOOLEAN;
				case 'H':
				case 'h':	return HASHCODE;
				case 'S':
				case 's':	return STRING;
				case 'C':
				case 'c':	return CHARACTER;
				case 'd':	return DECIMAL_INTEGER;
				case 'o':	return OCTAL_INTEGER;
				case 'X':
				case 'x':	return HEXADECIMAL_INTEGER;
				case 'E':
				case 'e':	return SCIENTIFIC_DECIMAL_FLOAT;
				case 'f':	return NORMAL_DECIMAL_FLOAT;
				case 'G':
				case 'g':	return SCIENTIFIC_OR_NORMAL_DECIMAL_FLOAT;
				
				default:
					return null;
			}
		
		// A date format
		switch (__s)
		{
			case 'H':	return TIME_MILITARY_HOUR_TWO_DIGIT_LEADING_ZERO;
			case 'I':	return TIME_STANDARD_HOUR_TWO_DIGIT_LEADING_ZERO;
			case 'k':	return TIME_MILITARY_HOUR;
			case 'l':	return TIME_STANDARD_HOUR;
			case 'M':	return TIME_MINUTE;
			case 'S':	return TIME_SECONDS;
			case 'L':	return TIME_MILLISECONDS;
			case 'p':	return TIME_AM_PM;
			case 'z':	return TIME_ZONE_RFC822_OFFSET;
			case 'Z':	return TIME_ZONE_ABBREVIATION;
			case 's':	return TIME_UNIX_SECONDS;
			case 'Q':	return TIME_UNIX_MILLISECONDS;
			case 'h':
			case 'b':	return DATE_ABBREVIATED_MONTH_NAME;
			case 'a':	return DATE_ABBREVIATED_DAY_NAME;
			case 'C':	return DATE_CENTURY_TWO_DIGIT_LEADING_ZERO;
			case 'Y':	return DATE_YEAR_FOUR_DIGITS;
			case 'y':	return DATE_YEAR_LAST_TWO_DIGITS;
			case 'j':	return DATE_DAY_OF_YEAR;
			case 'm':	return DATE_MONTH_TWO_DIGIT_LEADING_ZERO;
			case 'd':	return DATE_DAY_TWO_DIGIT_LEADING_ZERO;
			case 'e':	return DATE_DAY;
			case 'R':	return DATE_TIME_MILITARY;
			case 'T':	return DATE_TIME_MILITARY_WITH_SECONDS;
			case 'r':	return DATE_TIME_STANDARD;
			case 'D':	return DATE_MONTH_DAY_YEAR;
			case 'F':	return DATE_ISO8601;
			case 'c':	return DATE_LONG_FORMAT;
		
			default:
				return null;
		}
	}
}

