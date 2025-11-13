# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: System and architecture mappings

# Architecture mappings
list(APPEND SQUIRRELJME_ARCH_MAP
	"i386!ia32"
	"i486!ia32"
	"i586!ia32"
	"i686!ia32"
	"x86!ia32"
	"x86_32!ia32"
	"ia64!ia64"
	"itanium!ia64"
	"itanic!ia64"
	"amd64!amd64"
	"x86_64!amd64"
	"em64t!amd64"
	"mips!mips32b"
	"mipsel!mips32l"
	"mips64!mips32b"
	"mips64el!mips32l"
	"riscv64!riscv64"
	"aarch64!arm64"
	"powerpc!powerpc32b"
	"powerpcle!powerpc32l"
	"powerpc64!powerpc64b"
	"powerpc64le!powerpc64l")

# System mappings
list(APPEND SQUIRRELJME_SYSTEM_MAP
	"linux!linux"
	"linux-gnu!linux"
	"linux-gnueabi!linux"
	"linux-gnueabihf!linux"
	"linux-gnuabi64!linux"
	"w64-mingw32!windows"
	"w64-mingw32ucrt!windows"
	"win16!windows"
	"win32!windows"
	"win64!windows"
	"beos!beos"
	"bsd!bsd"
	"openbsd!bsd"
	"freebsd!bsd"
	"netbsd!bsd"
	"cygwin!cygwin"
	"msys2!cygwin"
	"dos!dos"
	"freedos!dos"
	"emscripten!emscripten"
	"macosx!macosx"
	"darwin!macosx"
	"3ds!3ds")

