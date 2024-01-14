# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------

# We need the entire JDK here for this to work!
FROM openjdk:8-jdk AS build

# emulator-base uses JNI to provide Assembly methods, we need a C++ compiler
ARG DEBIAN_FRONTEND=noninteractive
RUN apt-get update
RUN apt-get install -y -q --no-install-recommends build-essential gcc g++ \
	cmake xorgxrdp xrdp

# Copy repository for building and use it for building
COPY . /tmp/src
WORKDIR /tmp/src

# Build entire JAR distribution (we do not need a daemon here)
RUN ./gradlew --no-daemon :emulators:standalone:shadowJar

# We do not need a big complex environment to run SquirrelJME now, so we
# can use a more compact image here
FROM openjdk:8-jre

# Description
LABEL cc.squirreljme.vm="springcoat"
LABEL cc.squirreljme.version="0.3.0"
LABEL version="0.3.0"
LABEL description="SquirrelJME is a Java ME 8 Virtual Machine for embedded and Internet of Things devices. It has the ultimate goal of being 99.9% compatible with the Java ME standard."
LABEL maintainer="Stephanie Gawroriski <xerthesquirrel@gmail.com>"

# All of the SquirrelJME data is here
RUN mkdir /squirreljme

# Copy standalone over and helper scripts to the container
COPY --from=build /tmp/src/.docker/squirreljme.sh /squirreljme/squirreljme.sh
RUN chmod +x /squirreljme/squirreljme.sh
COPY --from=build \
	/tmp/src/emulators/standalone/build/libs/squirreljme-standalone-*.jar \
	/squirreljme/squirreljme.jar

# Where the user classpath exists (to run extra programs)
RUN mkdir /squirreljme/jars
VOLUME /squirreljme/jars

# Expose VNC and RDP
EXPOSE 5900/tcp
EXPOSE 3389/tcp
EXPOSE 3389/udp

# Options for the virtual machine
ENV EMULATOR=springcoat

# Run the VM and go to the launcher
ENTRYPOINT /usr/bin/xrdp-sesrun \
	-t Xorg \
	-S "/squirreljme/squirreljme.sh" \
	root
