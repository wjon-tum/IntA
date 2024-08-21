# IntA - Democratic Events

A web-based **Interactive Agenda** software, which can be used to agree on an agenda at events, courses etc.

## Licence

This software is licenced under Commons Clause v1.0 in combination with GNU GPL v3. Please have a look at the LICENCE
file for more information.

## Setup & Server Deployment

### Setup

- Java 22 or newer
- Gradle 8.9 or newer
- Execute `gradle build` or `./gradlew build`
- Environment Variables:

| Name                 | Description                                                                                                                                   |
 |----------------------|-----------------------------------------------------------------------------------------------------------------------------------------------|
| _NVD_API_KEY_        | An API Key for nvd, see [their website](https://nvd.nist.gov/developers/request-an-api-key) for details  **[Optional, for development only]** |
| _INTA_SERVER_SECRET_ | A secret for signing user certificates / jwt tokens. Can be anything, but should remain the same for at least one session                     |

## Other Software & Services

- This product uses the NVD API but is not endorsed or certified by the NVD.