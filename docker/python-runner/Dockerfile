FROM python:3.11-slim

# Create non-root user (same uid/gid your backend uses if needed)
RUN adduser --disabled-password --gecos "" runner

WORKDIR /sandbox

COPY entrypoint.sh /entrypoint.sh
RUN chmod +x /entry.sh

USER runner
ENTRYPOINT ["/entrypoint.sh"]
CMD ["--help"]
