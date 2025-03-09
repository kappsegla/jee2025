#!/bin/bash
set -e

echo "Configuring PostgreSQL datasource with:"
echo "Host: $DB_HOST"
echo "Port: $DB_PORT"
echo "Database: $DB_NAME"
echo "Datasource name: $DS_NAME"
echo "JNDI name: $DS_JNDI"

# Create a temporary CLI script with environment variables replaced
eval "cat <<EOF
$(cat /tmp/configure-datasource.cli)
EOF" > /tmp/configure-datasource-resolved.cli

# Show the final CLI script for debugging
echo "Using CLI script:"
cat /tmp/configure-datasource-resolved.cli

# Execute the CLI script to configure datasource before starting WildFly
echo "Executing CLI script to configure datasource..."
${JBOSS_HOME}/bin/jboss-cli.sh --file=/tmp/configure-datasource-resolved.cli

echo "Starting WildFly server..."
exec ${JBOSS_HOME}/bin/standalone.sh -b 0.0.0.0 "$@"
