FROM debian:trixie-slim

ENV SDKMAN_JDK_VERSION=23.0.5.r17-mandrel
ENV SDKMAN_GRADLE_VERSION=8.14.3
ENV SDKMAN_QUARKUS_VERSION=3.26.1
ENV NODE_MAJOR_VERSION=24

RUN apt update
RUN apt install -y git curl wget unzip zip

# Install SDKMAN
RUN curl -s "https://get.sdkman.io" | bash

# Install JDK 17
RUN bash -c "source $HOME/.sdkman/bin/sdkman-init.sh && sdk install java $SDKMAN_JDK_VERSION"
# Install Gradle 7
RUN bash -c "source $HOME/.sdkman/bin/sdkman-init.sh && sdk install gradle $SDKMAN_GRADLE_VERSION"
# Install Quarkus CLI
RUN bash -c "source $HOME/.sdkman/bin/sdkman-init.sh && sdk install quarkus $SDKMAN_QUARKUS_VERSION"

# Install native compiler to enable quarkus native compilation
RUN apt install -y gcc zlib1g-dev

# Install Node 24
RUN curl -fsSL https://deb.nodesource.com/setup_${NODE_MAJOR_VERSION}.x -o nodesource_setup.sh
RUN bash nodesource_setup.sh
RUN apt install -y nodejs

# Install PNPM
RUN corepack enable pnpm
RUN corepack use pnpm@10.15.0

# Install requirements for contextmapper
RUN apt install -y libxtst6 graphviz libxi6
