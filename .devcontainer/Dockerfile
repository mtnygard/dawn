FROM openjdk:14-jdk

# This Dockerfile adds a non-root user with sudo access. Update the “remoteUser” property in
# devcontainer.json to use it. More info: https://aka.ms/vscode-remote/containers/non-root-user.
ARG USERNAME=vscode
ARG USER_UID=1000
ARG USER_GID=$USER_UID

RUN groupadd --gid $USER_GID $USERNAME \
    && useradd -s /bin/bash --uid $USER_UID --gid $USER_GID -m $USERNAME \
    # [Optional] Add sudo support for the non-root user
    && yum install -y sudo \
    && echo $USERNAME ALL=\(root\) NOPASSWD:ALL > /etc/sudoers.d/$USERNAME \
    && chmod 0440 /etc/sudoers.d/$USERNAME

# Verify git, needed tools installed
RUN yum install -y git openssh-clients less net-tools which curl procps unzip 

# Install Clojure
RUN yum install -y rlwrap
ENV CLOJURE_VERSION 1.10.1.561
RUN curl -s https://download.clojure.org/install/linux-install-${CLOJURE_VERSION}.sh | bash 

# Clean yum cache
RUN yum clean all

# Allow for a consistent java home location for settings - image is changing over time
RUN if [ ! -d "/docker-java-home" ]; then ln -s "${JAVA_HOME}" /docker-java-home; fi
