FROM eclipse-temurin:17-jdk

RUN groupadd -g 10001 runner && useradd -u 10001 -g 10001 -m runner


# create sandbox and set ownership to runner (root required here)
RUN mkdir -p /sandbox && chown 10001:10001 /sandbox

# make sure 'timeout' exists
RUN apt-get update && apt-get install -y --no-install-recommends coreutils jq && rm -rf /var/lib/apt/lists/*

COPY entrypoint.sh /usr/local/bin/entrypoint.sh
RUN chmod +x /usr/local/bin/entry.sh

USER runner
WORKDIR /sandbox
ENTRYPOINT ["/usr/local/bin/entrypoint.sh"]