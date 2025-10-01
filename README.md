# the-wikiverse-api

## Code Formatting

This project uses Prettier for consistent code formatting. The following npm scripts are available:

- `npm run format` - Format all files in the project
- `npm run format:check` - Check if files are properly formatted
- `npm run format:java` - Format only Java files
- `npm run format:java:check` - Check formatting for Java files only

### Usage

```bash
# Format all Java files
npm run format:java

# Check if code is properly formatted (useful for CI/CD)
npm run format:java:check

# Format all files (including any future non-Java files)
npm run format
```

## To-Do's

- [ ] Check out Spring Boot Actuator use for helpful metric endpoints [tutorial](https://www.baeldung.com/spring-boot-actuators)
- [ ] Setup and walk through the whole data structure
- [ ] Spring Boot Devtools [here](https://docs.spring.io/spring-boot/4.0/reference/using/devtools.html)
