# ---------------------------------------------------------------------------
# Multi-Phasic Applications: WinterCoat
#     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
#     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3+, or later.
# For more information see license.mkd.
# ---------------------------------------------------------------------------
# This is a miniature Java VM virtual machine for simplistic sanity purposes.
# Download: <http://hg.openjdk.java.net/jdk8/jdk8/jdk/archive/tip.tar.gz>
# Set JVM_INCLUDE to the directory of: `src/share/javavm/export`.
# ---------------------------------------------------------------------------

CFLAGS := $(CFLAGS) -D_JNI_IMPLEMENTATION_ -I$(JVM_INCLUDE) -I. --std=c99

libjvm.so:		wintercoat.o
				$(CC) -o $@ -shared $(LDFLAGS) $<

wintercoat.o:	wintercoat.c jni_md.h jvm_md.h
				$(CC) -o $@ -c $< $(CFLAGS)

jni_md.h:		
				:
		
jvm_md.h:
				:

