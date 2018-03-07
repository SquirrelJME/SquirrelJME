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
 * This is used for testing of characters so that the {@link Tokenizer} class
 * is not filled with large methods that perform many checks.
 *
 * @since 2017/09/09
 */
public final class CharacterTest
{
	/**
	 * Not used.
	 *
	 * @since 2017/09/09
	 */
	private CharacterTest()
	{
	}
	
	/**
	 * This checks the specified character to determine if it is valid for
	 * being part of an identifier after the first character.
	 *
	 * @param __c The character to check.
	 * @return If this is valid for the part of an identifier.
	 * @since 2017/09/09
	 */
	public static boolean isIdentifierPart(int __c)
	{
		return (isIdentifierStart(__c) ||
			(__c >= 0x0001 && __c < 0x0009) ||
			(__c >= 0x000E && __c < 0x001C) ||
			(__c >= 0x0030 && __c < 0x003A) ||
			(__c >= 0x007F && __c < 0x00A0) ||
			__c == 0x00AD ||
			(__c >= 0x0300 && __c < 0x0370) ||
			(__c >= 0x0483 && __c < 0x0488) ||
			(__c >= 0x0591 && __c < 0x05BE) ||
			__c == 0x05BF ||
			(__c >= 0x05C1 && __c < 0x05C3) ||
			(__c >= 0x05C4 && __c < 0x05C6) ||
			__c == 0x05C7 ||
			(__c >= 0x0600 && __c < 0x0605) ||
			(__c >= 0x0610 && __c < 0x061B) ||
			(__c >= 0x064B && __c < 0x066A) ||
			__c == 0x0670 ||
			(__c >= 0x06D6 && __c < 0x06DE) ||
			(__c >= 0x06DF && __c < 0x06E5) ||
			(__c >= 0x06E7 && __c < 0x06E9) ||
			(__c >= 0x06EA && __c < 0x06EE) ||
			(__c >= 0x06F0 && __c < 0x06FA) ||
			__c == 0x070F ||
			__c == 0x0711 ||
			(__c >= 0x0730 && __c < 0x074B) ||
			(__c >= 0x07A6 && __c < 0x07B1) ||
			(__c >= 0x07C0 && __c < 0x07CA) ||
			(__c >= 0x07EB && __c < 0x07F4) ||
			(__c >= 0x0816 && __c < 0x081A) ||
			(__c >= 0x081B && __c < 0x0824) ||
			(__c >= 0x0825 && __c < 0x0828) ||
			(__c >= 0x0829 && __c < 0x082E) ||
			(__c >= 0x0859 && __c < 0x085C) ||
			(__c >= 0x08E4 && __c < 0x08FF) ||
			(__c >= 0x0900 && __c < 0x0904) ||
			(__c >= 0x093A && __c < 0x093D) ||
			(__c >= 0x093E && __c < 0x0950) ||
			(__c >= 0x0951 && __c < 0x0958) ||
			(__c >= 0x0962 && __c < 0x0964) ||
			(__c >= 0x0966 && __c < 0x0970) ||
			(__c >= 0x0981 && __c < 0x0984) ||
			__c == 0x09BC ||
			(__c >= 0x09BE && __c < 0x09C5) ||
			(__c >= 0x09C7 && __c < 0x09C9) ||
			(__c >= 0x09CB && __c < 0x09CE) ||
			__c == 0x09D7 ||
			(__c >= 0x09E2 && __c < 0x09E4) ||
			(__c >= 0x09E6 && __c < 0x09F0) ||
			(__c >= 0x0A01 && __c < 0x0A04) ||
			__c == 0x0A3C ||
			(__c >= 0x0A3E && __c < 0x0A43) ||
			(__c >= 0x0A47 && __c < 0x0A49) ||
			(__c >= 0x0A4B && __c < 0x0A4E) ||
			__c == 0x0A51 ||
			(__c >= 0x0A66 && __c < 0x0A72) ||
			__c == 0x0A75 ||
			(__c >= 0x0A81 && __c < 0x0A84) ||
			__c == 0x0ABC ||
			(__c >= 0x0ABE && __c < 0x0AC6) ||
			(__c >= 0x0AC7 && __c < 0x0ACA) ||
			(__c >= 0x0ACB && __c < 0x0ACE) ||
			(__c >= 0x0AE2 && __c < 0x0AE4) ||
			(__c >= 0x0AE6 && __c < 0x0AF0) ||
			(__c >= 0x0B01 && __c < 0x0B04) ||
			__c == 0x0B3C ||
			(__c >= 0x0B3E && __c < 0x0B45) ||
			(__c >= 0x0B47 && __c < 0x0B49) ||
			(__c >= 0x0B4B && __c < 0x0B4E) ||
			(__c >= 0x0B56 && __c < 0x0B58) ||
			(__c >= 0x0B62 && __c < 0x0B64) ||
			(__c >= 0x0B66 && __c < 0x0B70) ||
			__c == 0x0B82 ||
			(__c >= 0x0BBE && __c < 0x0BC3) ||
			(__c >= 0x0BC6 && __c < 0x0BC9) ||
			(__c >= 0x0BCA && __c < 0x0BCE) ||
			__c == 0x0BD7 ||
			(__c >= 0x0BE6 && __c < 0x0BF0) ||
			(__c >= 0x0C01 && __c < 0x0C04) ||
			(__c >= 0x0C3E && __c < 0x0C45) ||
			(__c >= 0x0C46 && __c < 0x0C49) ||
			(__c >= 0x0C4A && __c < 0x0C4E) ||
			(__c >= 0x0C55 && __c < 0x0C57) ||
			(__c >= 0x0C62 && __c < 0x0C64) ||
			(__c >= 0x0C66 && __c < 0x0C70) ||
			(__c >= 0x0C82 && __c < 0x0C84) ||
			__c == 0x0CBC ||
			(__c >= 0x0CBE && __c < 0x0CC5) ||
			(__c >= 0x0CC6 && __c < 0x0CC9) ||
			(__c >= 0x0CCA && __c < 0x0CCE) ||
			(__c >= 0x0CD5 && __c < 0x0CD7) ||
			(__c >= 0x0CE2 && __c < 0x0CE4) ||
			(__c >= 0x0CE6 && __c < 0x0CF0) ||
			(__c >= 0x0D02 && __c < 0x0D04) ||
			(__c >= 0x0D3E && __c < 0x0D45) ||
			(__c >= 0x0D46 && __c < 0x0D49) ||
			(__c >= 0x0D4A && __c < 0x0D4E) ||
			__c == 0x0D57 ||
			(__c >= 0x0D62 && __c < 0x0D64) ||
			(__c >= 0x0D66 && __c < 0x0D70) ||
			(__c >= 0x0D82 && __c < 0x0D84) ||
			__c == 0x0DCA ||
			(__c >= 0x0DCF && __c < 0x0DD5) ||
			__c == 0x0DD6 ||
			(__c >= 0x0DD8 && __c < 0x0DE0) ||
			(__c >= 0x0DF2 && __c < 0x0DF4) ||
			__c == 0x0E31 ||
			(__c >= 0x0E34 && __c < 0x0E3B) ||
			(__c >= 0x0E47 && __c < 0x0E4F) ||
			(__c >= 0x0E50 && __c < 0x0E5A) ||
			__c == 0x0EB1 ||
			(__c >= 0x0EB4 && __c < 0x0EBA) ||
			(__c >= 0x0EBB && __c < 0x0EBD) ||
			(__c >= 0x0EC8 && __c < 0x0ECE) ||
			(__c >= 0x0ED0 && __c < 0x0EDA) ||
			(__c >= 0x0F18 && __c < 0x0F1A) ||
			(__c >= 0x0F20 && __c < 0x0F2A) ||
			__c == 0x0F35 ||
			__c == 0x0F37 ||
			__c == 0x0F39 ||
			(__c >= 0x0F3E && __c < 0x0F40) ||
			(__c >= 0x0F71 && __c < 0x0F85) ||
			(__c >= 0x0F86 && __c < 0x0F88) ||
			(__c >= 0x0F8D && __c < 0x0F98) ||
			(__c >= 0x0F99 && __c < 0x0FBD) ||
			__c == 0x0FC6 ||
			(__c >= 0x102B && __c < 0x103F) ||
			(__c >= 0x1040 && __c < 0x104A) ||
			(__c >= 0x1056 && __c < 0x105A) ||
			(__c >= 0x105E && __c < 0x1061) ||
			(__c >= 0x1062 && __c < 0x1065) ||
			(__c >= 0x1067 && __c < 0x106E) ||
			(__c >= 0x1071 && __c < 0x1075) ||
			(__c >= 0x1082 && __c < 0x108E) ||
			(__c >= 0x108F && __c < 0x109E) ||
			(__c >= 0x135D && __c < 0x1360) ||
			(__c >= 0x1712 && __c < 0x1715) ||
			(__c >= 0x1732 && __c < 0x1735) ||
			(__c >= 0x1752 && __c < 0x1754) ||
			(__c >= 0x1772 && __c < 0x1774) ||
			(__c >= 0x17B4 && __c < 0x17D4) ||
			__c == 0x17DD ||
			(__c >= 0x17E0 && __c < 0x17EA) ||
			(__c >= 0x180B && __c < 0x180E) ||
			(__c >= 0x1810 && __c < 0x181A) ||
			__c == 0x18A9 ||
			(__c >= 0x1920 && __c < 0x192C) ||
			(__c >= 0x1930 && __c < 0x193C) ||
			(__c >= 0x1946 && __c < 0x1950) ||
			(__c >= 0x19B0 && __c < 0x19C1) ||
			(__c >= 0x19C8 && __c < 0x19CA) ||
			(__c >= 0x19D0 && __c < 0x19DA) ||
			(__c >= 0x1A17 && __c < 0x1A1C) ||
			(__c >= 0x1A55 && __c < 0x1A5F) ||
			(__c >= 0x1A60 && __c < 0x1A7D) ||
			(__c >= 0x1A7F && __c < 0x1A8A) ||
			(__c >= 0x1A90 && __c < 0x1A9A) ||
			(__c >= 0x1B00 && __c < 0x1B05) ||
			(__c >= 0x1B34 && __c < 0x1B45) ||
			(__c >= 0x1B50 && __c < 0x1B5A) ||
			(__c >= 0x1B6B && __c < 0x1B74) ||
			(__c >= 0x1B80 && __c < 0x1B83) ||
			(__c >= 0x1BA1 && __c < 0x1BAE) ||
			(__c >= 0x1BB0 && __c < 0x1BBA) ||
			(__c >= 0x1BE6 && __c < 0x1BF4) ||
			(__c >= 0x1C24 && __c < 0x1C38) ||
			(__c >= 0x1C40 && __c < 0x1C4A) ||
			(__c >= 0x1C50 && __c < 0x1C5A) ||
			(__c >= 0x1CD0 && __c < 0x1CD3) ||
			(__c >= 0x1CD4 && __c < 0x1CE9) ||
			__c == 0x1CED ||
			(__c >= 0x1CF2 && __c < 0x1CF5) ||
			(__c >= 0x1DC0 && __c < 0x1DE7) ||
			(__c >= 0x1DFC && __c < 0x1E00) ||
			(__c >= 0x200B && __c < 0x2010) ||
			(__c >= 0x202A && __c < 0x202F) ||
			(__c >= 0x2060 && __c < 0x2065) ||
			(__c >= 0x206A && __c < 0x2070) ||
			(__c >= 0x20D0 && __c < 0x20DD) ||
			__c == 0x20E1 ||
			(__c >= 0x20E5 && __c < 0x20F1) ||
			(__c >= 0x2CEF && __c < 0x2CF2) ||
			__c == 0x2D7F ||
			(__c >= 0x2DE0 && __c < 0x2E00) ||
			(__c >= 0x302A && __c < 0x3030) ||
			(__c >= 0x3099 && __c < 0x309B) ||
			(__c >= 0xA620 && __c < 0xA62A) ||
			__c == 0xA66F ||
			(__c >= 0xA674 && __c < 0xA67E) ||
			__c == 0xA69F ||
			(__c >= 0xA6F0 && __c < 0xA6F2) ||
			__c == 0xA802 ||
			__c == 0xA806 ||
			__c == 0xA80B ||
			(__c >= 0xA823 && __c < 0xA828) ||
			(__c >= 0xA880 && __c < 0xA882) ||
			(__c >= 0xA8B4 && __c < 0xA8C5) ||
			(__c >= 0xA8D0 && __c < 0xA8DA) ||
			(__c >= 0xA8E0 && __c < 0xA8F2) ||
			(__c >= 0xA900 && __c < 0xA90A) ||
			(__c >= 0xA926 && __c < 0xA92E) ||
			(__c >= 0xA947 && __c < 0xA954) ||
			(__c >= 0xA980 && __c < 0xA984) ||
			(__c >= 0xA9B3 && __c < 0xA9C1) ||
			(__c >= 0xA9D0 && __c < 0xA9DA) ||
			(__c >= 0xAA29 && __c < 0xAA37) ||
			__c == 0xAA43 ||
			(__c >= 0xAA4C && __c < 0xAA4E) ||
			(__c >= 0xAA50 && __c < 0xAA5A) ||
			__c == 0xAA7B ||
			__c == 0xAAB0 ||
			(__c >= 0xAAB2 && __c < 0xAAB5) ||
			(__c >= 0xAAB7 && __c < 0xAAB9) ||
			(__c >= 0xAABE && __c < 0xAAC0) ||
			__c == 0xAAC1 ||
			(__c >= 0xAAEB && __c < 0xAAF0) ||
			(__c >= 0xAAF5 && __c < 0xAAF7) ||
			(__c >= 0xABE3 && __c < 0xABEB) ||
			(__c >= 0xABEC && __c < 0xABEE) ||
			(__c >= 0xABF0 && __c < 0xABFA) ||
			__c == 0xFB1E ||
			(__c >= 0xFE00 && __c < 0xFE10) ||
			(__c >= 0xFE20 && __c < 0xFE27) ||
			__c == 0xFEFF ||
			(__c >= 0xFF10 && __c < 0xFF1A) ||
			(__c >= 0xFFF9 && __c < 0xFFFC));
	}
	
	/**
	 * This checks the specified character to determine if it is valid for
	 * being used at the start of an identifier.
	 *
	 * @param __c The character to check.
	 * @return If this is valid for the start of an identifier.
	 * @since 2017/09/09
	 */
	public static boolean isIdentifierStart(int __c)
	{
		return (__c == 0x0024 ||
			(__c >= 0x0041 && __c < 0x005B) ||
			__c == 0x005F ||
			(__c >= 0x0061 && __c < 0x007B) ||
			(__c >= 0x00A2 && __c < 0x00A6) ||
			__c == 0x00AA ||
			__c == 0x00B5 ||
			__c == 0x00BA ||
			(__c >= 0x00C0 && __c < 0x00D7) ||
			(__c >= 0x00D8 && __c < 0x00F7) ||
			(__c >= 0x00F8 && __c < 0x02C2) ||
			(__c >= 0x02C6 && __c < 0x02D2) ||
			(__c >= 0x02E0 && __c < 0x02E5) ||
			__c == 0x02EC ||
			__c == 0x02EE ||
			(__c >= 0x0370 && __c < 0x0375) ||
			(__c >= 0x0376 && __c < 0x0378) ||
			(__c >= 0x037A && __c < 0x037E) ||
			__c == 0x0386 ||
			(__c >= 0x0388 && __c < 0x038B) ||
			__c == 0x038C ||
			(__c >= 0x038E && __c < 0x03A2) ||
			(__c >= 0x03A3 && __c < 0x03F6) ||
			(__c >= 0x03F7 && __c < 0x0482) ||
			(__c >= 0x048A && __c < 0x0528) ||
			(__c >= 0x0531 && __c < 0x0557) ||
			__c == 0x0559 ||
			(__c >= 0x0561 && __c < 0x0588) ||
			__c == 0x058F ||
			(__c >= 0x05D0 && __c < 0x05EB) ||
			(__c >= 0x05F0 && __c < 0x05F3) ||
			__c == 0x060B ||
			(__c >= 0x0620 && __c < 0x064B) ||
			(__c >= 0x066E && __c < 0x0670) ||
			(__c >= 0x0671 && __c < 0x06D4) ||
			__c == 0x06D5 ||
			(__c >= 0x06E5 && __c < 0x06E7) ||
			(__c >= 0x06EE && __c < 0x06F0) ||
			(__c >= 0x06FA && __c < 0x06FD) ||
			__c == 0x06FF ||
			__c == 0x0710 ||
			(__c >= 0x0712 && __c < 0x0730) ||
			(__c >= 0x074D && __c < 0x07A6) ||
			__c == 0x07B1 ||
			(__c >= 0x07CA && __c < 0x07EB) ||
			(__c >= 0x07F4 && __c < 0x07F6) ||
			__c == 0x07FA ||
			(__c >= 0x0800 && __c < 0x0816) ||
			__c == 0x081A ||
			__c == 0x0824 ||
			__c == 0x0828 ||
			(__c >= 0x0840 && __c < 0x0859) ||
			__c == 0x08A0 ||
			(__c >= 0x08A2 && __c < 0x08AD) ||
			(__c >= 0x0904 && __c < 0x093A) ||
			__c == 0x093D ||
			__c == 0x0950 ||
			(__c >= 0x0958 && __c < 0x0962) ||
			(__c >= 0x0971 && __c < 0x0978) ||
			(__c >= 0x0979 && __c < 0x0980) ||
			(__c >= 0x0985 && __c < 0x098D) ||
			(__c >= 0x098F && __c < 0x0991) ||
			(__c >= 0x0993 && __c < 0x09A9) ||
			(__c >= 0x09AA && __c < 0x09B1) ||
			__c == 0x09B2 ||
			(__c >= 0x09B6 && __c < 0x09BA) ||
			__c == 0x09BD ||
			__c == 0x09CE ||
			(__c >= 0x09DC && __c < 0x09DE) ||
			(__c >= 0x09DF && __c < 0x09E2) ||
			(__c >= 0x09F0 && __c < 0x09F4) ||
			__c == 0x09FB ||
			(__c >= 0x0A05 && __c < 0x0A0B) ||
			(__c >= 0x0A0F && __c < 0x0A11) ||
			(__c >= 0x0A13 && __c < 0x0A29) ||
			(__c >= 0x0A2A && __c < 0x0A31) ||
			(__c >= 0x0A32 && __c < 0x0A34) ||
			(__c >= 0x0A35 && __c < 0x0A37) ||
			(__c >= 0x0A38 && __c < 0x0A3A) ||
			(__c >= 0x0A59 && __c < 0x0A5D) ||
			__c == 0x0A5E ||
			(__c >= 0x0A72 && __c < 0x0A75) ||
			(__c >= 0x0A85 && __c < 0x0A8E) ||
			(__c >= 0x0A8F && __c < 0x0A92) ||
			(__c >= 0x0A93 && __c < 0x0AA9) ||
			(__c >= 0x0AAA && __c < 0x0AB1) ||
			(__c >= 0x0AB2 && __c < 0x0AB4) ||
			(__c >= 0x0AB5 && __c < 0x0ABA) ||
			__c == 0x0ABD ||
			__c == 0x0AD0 ||
			(__c >= 0x0AE0 && __c < 0x0AE2) ||
			__c == 0x0AF1 ||
			(__c >= 0x0B05 && __c < 0x0B0D) ||
			(__c >= 0x0B0F && __c < 0x0B11) ||
			(__c >= 0x0B13 && __c < 0x0B29) ||
			(__c >= 0x0B2A && __c < 0x0B31) ||
			(__c >= 0x0B32 && __c < 0x0B34) ||
			(__c >= 0x0B35 && __c < 0x0B3A) ||
			__c == 0x0B3D ||
			(__c >= 0x0B5C && __c < 0x0B5E) ||
			(__c >= 0x0B5F && __c < 0x0B62) ||
			__c == 0x0B71 ||
			__c == 0x0B83 ||
			(__c >= 0x0B85 && __c < 0x0B8B) ||
			(__c >= 0x0B8E && __c < 0x0B91) ||
			(__c >= 0x0B92 && __c < 0x0B96) ||
			(__c >= 0x0B99 && __c < 0x0B9B) ||
			__c == 0x0B9C ||
			(__c >= 0x0B9E && __c < 0x0BA0) ||
			(__c >= 0x0BA3 && __c < 0x0BA5) ||
			(__c >= 0x0BA8 && __c < 0x0BAB) ||
			(__c >= 0x0BAE && __c < 0x0BBA) ||
			__c == 0x0BD0 ||
			__c == 0x0BF9 ||
			(__c >= 0x0C05 && __c < 0x0C0D) ||
			(__c >= 0x0C0E && __c < 0x0C11) ||
			(__c >= 0x0C12 && __c < 0x0C29) ||
			(__c >= 0x0C2A && __c < 0x0C34) ||
			(__c >= 0x0C35 && __c < 0x0C3A) ||
			__c == 0x0C3D ||
			(__c >= 0x0C58 && __c < 0x0C5A) ||
			(__c >= 0x0C60 && __c < 0x0C62) ||
			(__c >= 0x0C85 && __c < 0x0C8D) ||
			(__c >= 0x0C8E && __c < 0x0C91) ||
			(__c >= 0x0C92 && __c < 0x0CA9) ||
			(__c >= 0x0CAA && __c < 0x0CB4) ||
			(__c >= 0x0CB5 && __c < 0x0CBA) ||
			__c == 0x0CBD ||
			__c == 0x0CDE ||
			(__c >= 0x0CE0 && __c < 0x0CE2) ||
			(__c >= 0x0CF1 && __c < 0x0CF3) ||
			(__c >= 0x0D05 && __c < 0x0D0D) ||
			(__c >= 0x0D0E && __c < 0x0D11) ||
			(__c >= 0x0D12 && __c < 0x0D3B) ||
			__c == 0x0D3D ||
			__c == 0x0D4E ||
			(__c >= 0x0D60 && __c < 0x0D62) ||
			(__c >= 0x0D7A && __c < 0x0D80) ||
			(__c >= 0x0D85 && __c < 0x0D97) ||
			(__c >= 0x0D9A && __c < 0x0DB2) ||
			(__c >= 0x0DB3 && __c < 0x0DBC) ||
			__c == 0x0DBD ||
			(__c >= 0x0DC0 && __c < 0x0DC7) ||
			(__c >= 0x0E01 && __c < 0x0E31) ||
			(__c >= 0x0E32 && __c < 0x0E34) ||
			(__c >= 0x0E3F && __c < 0x0E47) ||
			(__c >= 0x0E81 && __c < 0x0E83) ||
			__c == 0x0E84 ||
			(__c >= 0x0E87 && __c < 0x0E89) ||
			__c == 0x0E8A ||
			__c == 0x0E8D ||
			(__c >= 0x0E94 && __c < 0x0E98) ||
			(__c >= 0x0E99 && __c < 0x0EA0) ||
			(__c >= 0x0EA1 && __c < 0x0EA4) ||
			__c == 0x0EA5 ||
			__c == 0x0EA7 ||
			(__c >= 0x0EAA && __c < 0x0EAC) ||
			(__c >= 0x0EAD && __c < 0x0EB1) ||
			(__c >= 0x0EB2 && __c < 0x0EB4) ||
			__c == 0x0EBD ||
			(__c >= 0x0EC0 && __c < 0x0EC5) ||
			__c == 0x0EC6 ||
			(__c >= 0x0EDC && __c < 0x0EE0) ||
			__c == 0x0F00 ||
			(__c >= 0x0F40 && __c < 0x0F48) ||
			(__c >= 0x0F49 && __c < 0x0F6D) ||
			(__c >= 0x0F88 && __c < 0x0F8D) ||
			(__c >= 0x1000 && __c < 0x102B) ||
			__c == 0x103F ||
			(__c >= 0x1050 && __c < 0x1056) ||
			(__c >= 0x105A && __c < 0x105E) ||
			__c == 0x1061 ||
			(__c >= 0x1065 && __c < 0x1067) ||
			(__c >= 0x106E && __c < 0x1071) ||
			(__c >= 0x1075 && __c < 0x1082) ||
			__c == 0x108E ||
			(__c >= 0x10A0 && __c < 0x10C6) ||
			__c == 0x10C7 ||
			__c == 0x10CD ||
			(__c >= 0x10D0 && __c < 0x10FB) ||
			(__c >= 0x10FC && __c < 0x1249) ||
			(__c >= 0x124A && __c < 0x124E) ||
			(__c >= 0x1250 && __c < 0x1257) ||
			__c == 0x1258 ||
			(__c >= 0x125A && __c < 0x125E) ||
			(__c >= 0x1260 && __c < 0x1289) ||
			(__c >= 0x128A && __c < 0x128E) ||
			(__c >= 0x1290 && __c < 0x12B1) ||
			(__c >= 0x12B2 && __c < 0x12B6) ||
			(__c >= 0x12B8 && __c < 0x12BF) ||
			__c == 0x12C0 ||
			(__c >= 0x12C2 && __c < 0x12C6) ||
			(__c >= 0x12C8 && __c < 0x12D7) ||
			(__c >= 0x12D8 && __c < 0x1311) ||
			(__c >= 0x1312 && __c < 0x1316) ||
			(__c >= 0x1318 && __c < 0x135B) ||
			(__c >= 0x1380 && __c < 0x1390) ||
			(__c >= 0x13A0 && __c < 0x13F5) ||
			(__c >= 0x1401 && __c < 0x166D) ||
			(__c >= 0x166F && __c < 0x1680) ||
			(__c >= 0x1681 && __c < 0x169B) ||
			(__c >= 0x16A0 && __c < 0x16EB) ||
			(__c >= 0x16EE && __c < 0x16F1) ||
			(__c >= 0x1700 && __c < 0x170D) ||
			(__c >= 0x170E && __c < 0x1712) ||
			(__c >= 0x1720 && __c < 0x1732) ||
			(__c >= 0x1740 && __c < 0x1752) ||
			(__c >= 0x1760 && __c < 0x176D) ||
			(__c >= 0x176E && __c < 0x1771) ||
			(__c >= 0x1780 && __c < 0x17B4) ||
			__c == 0x17D7 ||
			(__c >= 0x17DB && __c < 0x17DD) ||
			(__c >= 0x1820 && __c < 0x1878) ||
			(__c >= 0x1880 && __c < 0x18A9) ||
			__c == 0x18AA ||
			(__c >= 0x18B0 && __c < 0x18F6) ||
			(__c >= 0x1900 && __c < 0x191D) ||
			(__c >= 0x1950 && __c < 0x196E) ||
			(__c >= 0x1970 && __c < 0x1975) ||
			(__c >= 0x1980 && __c < 0x19AC) ||
			(__c >= 0x19C1 && __c < 0x19C8) ||
			(__c >= 0x1A00 && __c < 0x1A17) ||
			(__c >= 0x1A20 && __c < 0x1A55) ||
			__c == 0x1AA7 ||
			(__c >= 0x1B05 && __c < 0x1B34) ||
			(__c >= 0x1B45 && __c < 0x1B4C) ||
			(__c >= 0x1B83 && __c < 0x1BA1) ||
			(__c >= 0x1BAE && __c < 0x1BB0) ||
			(__c >= 0x1BBA && __c < 0x1BE6) ||
			(__c >= 0x1C00 && __c < 0x1C24) ||
			(__c >= 0x1C4D && __c < 0x1C50) ||
			(__c >= 0x1C5A && __c < 0x1C7E) ||
			(__c >= 0x1CE9 && __c < 0x1CED) ||
			(__c >= 0x1CEE && __c < 0x1CF2) ||
			(__c >= 0x1CF5 && __c < 0x1CF7) ||
			(__c >= 0x1D00 && __c < 0x1DC0) ||
			(__c >= 0x1E00 && __c < 0x1F16) ||
			(__c >= 0x1F18 && __c < 0x1F1E) ||
			(__c >= 0x1F20 && __c < 0x1F46) ||
			(__c >= 0x1F48 && __c < 0x1F4E) ||
			(__c >= 0x1F50 && __c < 0x1F58) ||
			__c == 0x1F59 ||
			__c == 0x1F5B ||
			__c == 0x1F5D ||
			(__c >= 0x1F5F && __c < 0x1F7E) ||
			(__c >= 0x1F80 && __c < 0x1FB5) ||
			(__c >= 0x1FB6 && __c < 0x1FBD) ||
			__c == 0x1FBE ||
			(__c >= 0x1FC2 && __c < 0x1FC5) ||
			(__c >= 0x1FC6 && __c < 0x1FCD) ||
			(__c >= 0x1FD0 && __c < 0x1FD4) ||
			(__c >= 0x1FD6 && __c < 0x1FDC) ||
			(__c >= 0x1FE0 && __c < 0x1FED) ||
			(__c >= 0x1FF2 && __c < 0x1FF5) ||
			(__c >= 0x1FF6 && __c < 0x1FFD) ||
			(__c >= 0x203F && __c < 0x2041) ||
			__c == 0x2054 ||
			__c == 0x2071 ||
			__c == 0x207F ||
			(__c >= 0x2090 && __c < 0x209D) ||
			(__c >= 0x20A0 && __c < 0x20BB) ||
			__c == 0x2102 ||
			__c == 0x2107 ||
			(__c >= 0x210A && __c < 0x2114) ||
			__c == 0x2115 ||
			(__c >= 0x2119 && __c < 0x211E) ||
			__c == 0x2124 ||
			__c == 0x2126 ||
			__c == 0x2128 ||
			(__c >= 0x212A && __c < 0x212E) ||
			(__c >= 0x212F && __c < 0x213A) ||
			(__c >= 0x213C && __c < 0x2140) ||
			(__c >= 0x2145 && __c < 0x214A) ||
			__c == 0x214E ||
			(__c >= 0x2160 && __c < 0x2189) ||
			(__c >= 0x2C00 && __c < 0x2C2F) ||
			(__c >= 0x2C30 && __c < 0x2C5F) ||
			(__c >= 0x2C60 && __c < 0x2CE5) ||
			(__c >= 0x2CEB && __c < 0x2CEF) ||
			(__c >= 0x2CF2 && __c < 0x2CF4) ||
			(__c >= 0x2D00 && __c < 0x2D26) ||
			__c == 0x2D27 ||
			__c == 0x2D2D ||
			(__c >= 0x2D30 && __c < 0x2D68) ||
			__c == 0x2D6F ||
			(__c >= 0x2D80 && __c < 0x2D97) ||
			(__c >= 0x2DA0 && __c < 0x2DA7) ||
			(__c >= 0x2DA8 && __c < 0x2DAF) ||
			(__c >= 0x2DB0 && __c < 0x2DB7) ||
			(__c >= 0x2DB8 && __c < 0x2DBF) ||
			(__c >= 0x2DC0 && __c < 0x2DC7) ||
			(__c >= 0x2DC8 && __c < 0x2DCF) ||
			(__c >= 0x2DD0 && __c < 0x2DD7) ||
			(__c >= 0x2DD8 && __c < 0x2DDF) ||
			__c == 0x2E2F ||
			(__c >= 0x3005 && __c < 0x3008) ||
			(__c >= 0x3021 && __c < 0x302A) ||
			(__c >= 0x3031 && __c < 0x3036) ||
			(__c >= 0x3038 && __c < 0x303D) ||
			(__c >= 0x3041 && __c < 0x3097) ||
			(__c >= 0x309D && __c < 0x30A0) ||
			(__c >= 0x30A1 && __c < 0x30FB) ||
			(__c >= 0x30FC && __c < 0x3100) ||
			(__c >= 0x3105 && __c < 0x312E) ||
			(__c >= 0x3131 && __c < 0x318F) ||
			(__c >= 0x31A0 && __c < 0x31BB) ||
			(__c >= 0x31F0 && __c < 0x3200) ||
			(__c >= 0x3400 && __c < 0x4DB6) ||
			(__c >= 0x4E00 && __c < 0x9FCD) ||
			(__c >= 0xA000 && __c < 0xA48D) ||
			(__c >= 0xA4D0 && __c < 0xA4FE) ||
			(__c >= 0xA500 && __c < 0xA60D) ||
			(__c >= 0xA610 && __c < 0xA620) ||
			(__c >= 0xA62A && __c < 0xA62C) ||
			(__c >= 0xA640 && __c < 0xA66F) ||
			(__c >= 0xA67F && __c < 0xA698) ||
			(__c >= 0xA6A0 && __c < 0xA6F0) ||
			(__c >= 0xA717 && __c < 0xA720) ||
			(__c >= 0xA722 && __c < 0xA789) ||
			(__c >= 0xA78B && __c < 0xA78F) ||
			(__c >= 0xA790 && __c < 0xA794) ||
			(__c >= 0xA7A0 && __c < 0xA7AB) ||
			(__c >= 0xA7F8 && __c < 0xA802) ||
			(__c >= 0xA803 && __c < 0xA806) ||
			(__c >= 0xA807 && __c < 0xA80B) ||
			(__c >= 0xA80C && __c < 0xA823) ||
			__c == 0xA838 ||
			(__c >= 0xA840 && __c < 0xA874) ||
			(__c >= 0xA882 && __c < 0xA8B4) ||
			(__c >= 0xA8F2 && __c < 0xA8F8) ||
			__c == 0xA8FB ||
			(__c >= 0xA90A && __c < 0xA926) ||
			(__c >= 0xA930 && __c < 0xA947) ||
			(__c >= 0xA960 && __c < 0xA97D) ||
			(__c >= 0xA984 && __c < 0xA9B3) ||
			__c == 0xA9CF ||
			(__c >= 0xAA00 && __c < 0xAA29) ||
			(__c >= 0xAA40 && __c < 0xAA43) ||
			(__c >= 0xAA44 && __c < 0xAA4C) ||
			(__c >= 0xAA60 && __c < 0xAA77) ||
			__c == 0xAA7A ||
			(__c >= 0xAA80 && __c < 0xAAB0) ||
			__c == 0xAAB1 ||
			(__c >= 0xAAB5 && __c < 0xAAB7) ||
			(__c >= 0xAAB9 && __c < 0xAABE) ||
			__c == 0xAAC0 ||
			__c == 0xAAC2 ||
			(__c >= 0xAADB && __c < 0xAADE) ||
			(__c >= 0xAAE0 && __c < 0xAAEB) ||
			(__c >= 0xAAF2 && __c < 0xAAF5) ||
			(__c >= 0xAB01 && __c < 0xAB07) ||
			(__c >= 0xAB09 && __c < 0xAB0F) ||
			(__c >= 0xAB11 && __c < 0xAB17) ||
			(__c >= 0xAB20 && __c < 0xAB27) ||
			(__c >= 0xAB28 && __c < 0xAB2F) ||
			(__c >= 0xABC0 && __c < 0xABE3) ||
			(__c >= 0xAC00 && __c < 0xD7A4) ||
			(__c >= 0xD7B0 && __c < 0xD7C7) ||
			(__c >= 0xD7CB && __c < 0xD7FC) ||
			(__c >= 0xF900 && __c < 0xFA6E) ||
			(__c >= 0xFA70 && __c < 0xFADA) ||
			(__c >= 0xFB00 && __c < 0xFB07) ||
			(__c >= 0xFB13 && __c < 0xFB18) ||
			__c == 0xFB1D ||
			(__c >= 0xFB1F && __c < 0xFB29) ||
			(__c >= 0xFB2A && __c < 0xFB37) ||
			(__c >= 0xFB38 && __c < 0xFB3D) ||
			__c == 0xFB3E ||
			(__c >= 0xFB40 && __c < 0xFB42) ||
			(__c >= 0xFB43 && __c < 0xFB45) ||
			(__c >= 0xFB46 && __c < 0xFBB2) ||
			(__c >= 0xFBD3 && __c < 0xFD3E) ||
			(__c >= 0xFD50 && __c < 0xFD90) ||
			(__c >= 0xFD92 && __c < 0xFDC8) ||
			(__c >= 0xFDF0 && __c < 0xFDFD) ||
			(__c >= 0xFE33 && __c < 0xFE35) ||
			(__c >= 0xFE4D && __c < 0xFE50) ||
			__c == 0xFE69 ||
			(__c >= 0xFE70 && __c < 0xFE75) ||
			(__c >= 0xFE76 && __c < 0xFEFD) ||
			__c == 0xFF04 ||
			(__c >= 0xFF21 && __c < 0xFF3B) ||
			__c == 0xFF3F ||
			(__c >= 0xFF41 && __c < 0xFF5B) ||
			(__c >= 0xFF66 && __c < 0xFFBF) ||
			(__c >= 0xFFC2 && __c < 0xFFC8) ||
			(__c >= 0xFFCA && __c < 0xFFD0) ||
			(__c >= 0xFFD2 && __c < 0xFFD8) ||
			(__c >= 0xFFDA && __c < 0xFFDD) ||
			(__c >= 0xFFE0 && __c < 0xFFE2) ||
			(__c >= 0xFFE5 && __c < 0xFFE7));
	}
	
	/**
	 * Is the specified character deemed an end of line?
	 *
	 * @param __c The character to check.
	 * @return If it is the end of the line.
	 * @since 2017/09/05
	 */
	public static boolean isNewline(int __c)
	{
		return __c < 0 || __c == '\r' || __c == '\n';
	}
	
	/**
	 * Is this character the start of a symbol.
	 *
	 * @param __c The character to test.
	 * @return If it is the start of a symbol.
	 * @since 2018/03/06
	 */
	public static boolean isSymbolStart(int __c)
	{
		return __c == '^' || __c == '~' || __c == '<' || __c == '=' ||
			__c == '>' || __c == '|' || __c == '-' || __c == ',' ||
			__c == ';' || __c == ':' || __c == '!' || __c == '?' ||
			__c == '/' || __c == '.' || __c == '(' || __c == ')' ||
			__c == '[' || __c == ']' || __c == '{' || __c == '}' ||
			__c == '*' || __c == '&' || __c == '%' || __c == '+';
	}
	
	/**
	 * Is this a character which is possible being in a number literal.
	 *
	 * @param __c The character check.
	 * @return If the character may be placed in a number literal.
	 * @since 2018/03/06
	 */
	public static boolean isPossibleNumberChar(int __c)
	{
		return __c == 'l' || __c == 'L' ||
			__c == 'x' || __c == 'X' ||
			__c == '+' || __c == '-' ||
			__c == 'p' || __c == 'P' ||
			__c == 'e' || __c == 'E' ||
			__c == '.' || __c == '_' ||
			(__c >= '0' && __c <= '9') ||
			(__c >= 'a' && __c <= 'f') ||	// includes bdf
			(__c >= 'A' && __c <= 'F');		// includes BDF
	}
	
	/**
	 * Is the specified character whitespace?
	 *
	 * @param __c The character to check.
	 * @return If it is whitespace.
	 * @since 2017/09/05
	 */
	public static boolean isWhite(int __c)
	{
		return __c == ' ' || __c == '\t' || __c == '\f' || __c == '\r' ||
			__c == '\n';
	}
}

