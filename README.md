# SSH Java Client

![GitHub release (with filter)](https://img.shields.io/github/v/release/sentrysoftware/ssh)
![Build](https://img.shields.io/github/actions/workflow/status/sentrysoftware/ssh/deploy.yml)
![GitHub top language](https://img.shields.io/github/languages/top/sentrysoftware/ssh)
![License](https://img.shields.io/github/license/sentrysoftware/ssh)

The SSH Java client is a library that allows to execute commands using SSH protocol in distant hosts.

## Build instructions

This is a simple Maven project. Build with:

```bash
mvn verify
```

## Release instructions

The artifact is deployed to Sonatype's [Maven Central](https://central.sonatype.com/).

The actual repository URL is https://s01.oss.sonatype.org/, with server Id `ossrh` and requires credentials to deploy
artifacts manually.

But it is strongly recommended to only use [GitHub Actions "Release to Maven Central"](actions/workflows/release.yml) to perform a release:

* Manually trigger the "Release" workflow
* Specify the version being released and the next version number (SNAPSHOT)
* Release the corresponding staging repository on [Sonatype's Nexus server](https://s01.oss.sonatype.org/)
* Merge the PR that has been created to prepare the next version

## License

License is Apache-2. Each source file must include the Apache-2 header (build will fail otherwise).
To update source files with the proper header, simply execute the below command:

```bash
mvn license:update-file-header
```

## Run SSH Client inside Java

Add SSH in the list of dependencies in your [Maven **pom.xml**](https://maven.apache.org/pom.html):

```xml
<dependencies>
	<!-- [...] -->
	<dependency>
		<groupId>org.sentrysoftware</groupId>
		<artifactId>ssh</artifactId>
		<version>1.0.00-SNAPSHOT</version> <!-- Use the latest version released -->
	</dependency>
</dependencies>
```

Invoke the SSH Client:

```java
	import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import com.sentrysoftware.matsya.Utils;
import com.sentrysoftware.matsya.ssh.SSHClient.CommandResult;

public class Main {

	public static void main(String[] args) throws IOException {
		final String hostname = "my-host";
		final String username = "my-username";
		final char[] password = new char[] { 'p', 'a', 's', 's' };

		// In seconds
		final int timeout = 120; 
		
		// Language
		final Charset charset = Utils.getCharsetFromLocale("en");

		// Specify a private key file for the authentication if required
		final File keyfile = null;
		
		// Specify the command to execute
		final String command = "echo test";

		try (final SSHClient sshClient = new SSHClient(hostname, charset)) {
			
			sshClient.connect(timeout * 1000);
			
			final boolean authenticated;
			if (keyfile != null) {
				authenticated = sshClient.authenticate(username, keyfile, password);
			} else if (password != null && password.length > 0) {
				authenticated = sshClient.authenticate(username, password);
			} else {
				authenticated = sshClient.authenticate(username);
			}

			if (!authenticated) {
				throw new IllegalStateException(String.format("Error: Failed to authenticate as %s on %s", username, hostname));
			}
			
			final CommandResult commandResult = sshClient.executeCommand(command, timeout * 1000);
			
			if (commandResult.success) {

				// Success
				System.out.format("Execution completed in %s seconds%n", commandResult.executionTime);
				System.out.format("Return code: %s%n", commandResult.exitStatus);
				System.out.println("Output: ");
				
				System.out.println(commandResult.result);

			} else {
				// Failure
				System.err.format("Execution failed: %s%n", commandResult.result);
			}
		}
	}
}
```

