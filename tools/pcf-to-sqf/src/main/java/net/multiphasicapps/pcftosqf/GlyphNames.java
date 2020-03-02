// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.pcftosqf;

/**
 * Glyph name conversion.
 *
 * @since 2018/11/28
 */
public class GlyphNames
{
	/**
	 * Converts a glyph name to character index.
	 *
	 * @param __n The name of the glyph.
	 * @return The index of the glyph.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/11/28
	 */
	public static final int toChar(String __n)
		throws NullPointerException
	{
		if (__n == null)
			throw new NullPointerException("NARG");
		
		// Depends
		switch (__n)
		{
			case "uni0000":			return 0;
			case "space":			return ' ';
			case "exclam":			return '!';
			case "quotedbl":		return '"';
			case "numbersign":		return '#';
			case "dollar":			return '$';
			case "percent":			return '%';
			case "ampersand":		return '&';
			case "quotesingle":		return '\'';
			case "parenleft":		return '(';
			case "parenright":		return ')';
			case "asterisk":		return '*';
			case "plus":			return '+';
			case "comma":			return ',';
			case "hyphen":			return '-';
			case "period":			return '.';
			case "slash":			return '/';
			case "zero":			return '0';
			case "one":				return '1';
			case "two":				return '2';
			case "three":			return '3';
			case "four":			return '4';
			case "five":			return '5';
			case "six":				return '6';
			case "seven":			return '7';
			case "eight":			return '8';
			case "nine":			return '9';
			case "colon":			return ':';
			case "semicolon":		return ';';
			case "less":			return '<';
			case "equal":			return '=';
			case "greater":			return '>';
			case "question":		return 63;
			case "at":				return '@';
			case "A":				return 'A';
			case "B":				return 'B';
			case "C":				return 'C';
			case "D":				return 'D';
			case "E":				return 'E';
			case "F":				return 'F';
			case "G":				return 'G';
			case "H":				return 'H';
			case "I":				return 'I';
			case "J":				return 'J';
			case "K":				return 'K';
			case "L":				return 'L';
			case "M":				return 'M';
			case "N":				return 'N';
			case "O":				return 'O';
			case "P":				return 'P';
			case "Q":				return 'Q';
			case "R":				return 'R';
			case "S":				return 'S';
			case "T":				return 'T';
			case "U":				return 'U';
			case "V":				return 'V';
			case "W":				return 'W';
			case "X":				return 'X';
			case "Y":				return 'Y';
			case "Z":				return 'Z';
			case "bracketleft":		return '[';
			case "backslash":		return '\\';
			case "bracketright":	return ']';
			case "asciicircum":		return '^';
			case "underscore":		return '_';
			case "grave":			return '`';
			case "a":				return 'a';
			case "b":				return 'b';
			case "c":				return 'c';
			case "d":				return 'd';
			case "e":				return 'e';
			case "f":				return 'f';
			case "g":				return 'g';
			case "h":				return 'h';
			case "i":				return 'i';
			case "j":				return 'j';
			case "k":				return 'k';
			case "l":				return 'l';
			case "m":				return 'm';
			case "n":				return 'n';
			case "o":				return 'o';
			case "p":				return 'p';
			case "q":				return 'q';
			case "r":				return 'r';
			case "s":				return 's';
			case "t":				return 't';
			case "u":				return 'u';
			case "v":				return 'v';
			case "w":				return 'w';
			case "x":				return 'x';
			case "y":				return 'y';
			case "z":				return 'z';
			case "braceleft":		return '{';
			case "bar":				return '|';
			case "braceright":		return '}';
			case "asciitilde":		return '~';
			case "uni007F":			return 0x7F;
			case "uni00A0":			return 0xA0;
			case "exclamdown":		return 0xA1;
			case "cent":			return 0xA2;
			case "sterling":		return 0xA3;
			case "Euro":			return 0xA4;
			case "yen":				return 0xA5;
			case "Scaron":			return 0xA6;
			case "section":			return 0xA7;
			case "scaron":			return 0xA8;
			case "copyright":		return 0xA9;
			case "ordfeminine":		return 0xAA;
			case "guillemotleft":	return 0xAB;
			case "logicalnot":		return 0xAC;
			case "uni00AD":			return 0xAD;
			case "registered":		return 0xAE;
			case "macron":			return 0xAF;
			case "degree":			return 0xB0;
			case "plusminus":		return 0xB1;
			case "uni00B2":			return 0xB2;
			case "uni00B3":			return 0xB3;
			case "Zcaron":			return 0xB4;
			case "mu":				return 0xB5;
			case "paragraph":		return 0xB6;
			case "periodcentered":	return 0xB7;
			case "zcaron":			return 0xB8;
			case "uni00B9":			return 0xB9;
			case "ordmasculine":	return 0xBA;
			case "guillemotright":	return 0xBB;
			case "OE":				return 0xBC;
			case "oe":				return 0xBD;
			case "Ydieresis":		return 0xBE;
			case "questiondown":	return 0xBF;
			case "Agrave":			return 0xC0;
			case "Aacute":			return 0xC1;
			case "Acircumflex":		return 0xC2;
			case "Atilde":			return 0xC3;
			case "Adieresis":		return 0xC4;
			case "Aring":			return 0xC5;
			case "AE":				return 0xC6;
			case "Ccedilla":		return 0xC7;
			case "Egrave":			return 0xC8;
			case "Eacute":			return 0xC9;
			case "Ecircumflex":		return 0xCA;
			case "Edieresis":		return 0xCB;
			case "Igrave":			return 0xCC;
			case "Iacute":			return 0xCD;
			case "Icircumflex":		return 0xCE;
			case "Idieresis":		return 0xCF;
			case "Eth":				return 0xD0;
			case "Ntilde":			return 0xD1;
			case "Ograve":			return 0xD2;
			case "Oacute":			return 0xD3;
			case "Ocircumflex":		return 0xD4;
			case "Otilde":			return 0xD5;
			case "Odieresis":		return 0xD6;
			case "multiply":		return 0xD7;
			case "Oslash":			return 0xD8;
			case "Ugrave":			return 0xD9;
			case "Uacute":			return 0xDA;
			case "Ucircumflex":		return 0xDB;
			case "Udieresis":		return 0xDC;
			case "Yacute":			return 0xDD;
			case "Thorn":			return 0xDE;
			case "germandbls":		return 0xDF;
			case "agrave":			return 0xE0;
			case "aacute":			return 0xE1;
			case "acircumflex":		return 0xE2;
			case "atilde":			return 0xE3;
			case "adieresis":		return 0xE4;
			case "aring":			return 0xE5;
			case "ae":				return 0xE6;
			case "ccedilla":		return 0xE7;
			case "egrave":			return 0xE8;
			case "eacute":			return 0xE9;
			case "ecircumflex":		return 0xEA;
			case "edieresis":		return 0xEB;
			case "igrave":			return 0xEC;
			case "iacute":			return 0xED;
			case "icircumflex":		return 0xEE;
			case "idieresis":		return 0xEF;
			case "eth":				return 0xF0;
			case "ntilde":			return 0xF1;
			case "ograve":			return 0xF2;
			case "oacute":			return 0xF3;
			case "ocircumflex":		return 0xF4;
			case "otilde":			return 0xF5;
			case "odieresis":		return 0xF6;
			case "divide":			return 0xF7;
			case "oslash":			return 0xF8;
			case "ugrave":			return 0xF9;
			case "uacute":			return 0xFA;
			case "ucircumflex":		return 0xFB;
			case "udieresis":		return 0xFC;
			case "yacute":			return 0xFD;
			case "thorn":			return 0xFE;
			case "ydieresis":		return 0xFF;
			
				// Unknown
			default:
				throw new RuntimeException(__n);
		}
	}
}

