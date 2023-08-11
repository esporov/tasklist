# TASKLIST

You can access Swagger and see all available endpoints by visiting `http://localhost:8080/swagger-ui/index.html`

## Environments
To run this application you need to create .env file in root directory with next environments:
- `HOST` - host of Postgresql database
- `POSTGRES_USERNAME` - username for Postgresql database
- `POSTGRES_PASSWORD` - password for Postgresql database
- `POSTGRES_DATABASE` - name of Postgresql database
- `POSTGRES_SCHEMA` - name of Postgresql schema
- `JWT_SECRET` - signature for JWT token (e.g. YXNkYWRhc2ZkZ2VkaGRn)