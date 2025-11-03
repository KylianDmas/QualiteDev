FROM debian:trixie-slim

ENV SDKMAN_JDK_VERSION=21.0.8-tem
ENV SDKMAN_GRADLE_VERSION=8.14.3
ENV SDKMAN_QUARKUS_VERSION=3.26.1
ENV NODE_MAJOR_VERSION=24
ENV PNPM_VERSION=10.11.0

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
RUN corepack use pnpm@$PNPM_VERSION

## Install Docker CE Cli
# Add Docker's official GPG key:
RUN apt install -y ca-certificates
RUN install -m 0755 -d /etc/apt/keyrings
RUN curl -fsSL https://download.docker.com/linux/debian/gpg -o /etc/apt/keyrings/docker.asc
RUN chmod a+r /etc/apt/keyrings/docker.asc

# Add the repository to Apt sources:
RUN echo \
  "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.asc] https://download.docker.com/linux/debian \
  $(. /etc/os-release && echo "$VERSION_CODENAME") stable" | \
  tee /etc/apt/sources.list.d/docker.list > /dev/null
RUN apt update

# CLI install
RUN apt install -y docker-ce-cli

# Install requirements for contextmapper

RUN apt install -y libxtst6 graphviz libxi6
