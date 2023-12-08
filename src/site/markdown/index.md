# SSH Java Client

The SSH Java Client enables you to execute basic SSH operations, including:

* Initializing the SSH Client
* Establishing a secure connection to a remote server
* Running commands on the remote server
* Conducting file transfers between local and remote machines through SCP

## How to run the SSH Client inside Java

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

import org.sentrysoftware.ssh.SshClient;
import org.sentrysoftware.ssh.Utils;
import org.sentrysoftware.ssh.SshClient.CommandResult;

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

		try (final SshClient sshClient = new SshClient(hostname, charset)) {
			
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

