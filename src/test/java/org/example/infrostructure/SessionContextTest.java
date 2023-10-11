package org.example.infrostructure;

import org.example.core.models.User;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class SessionContextTest {

    @Test
    void testSetAndGetLoggedInUser() {
        User user = new User("username", "password", "f1", "l1", 100);
        SessionContext.setLoggedInUser(user);

        User loggedInUser = SessionContext.getLoggedInUser();
        assertThat(loggedInUser).isEqualTo(user);
    }

    @Test
    void testClearLoggedInUser() {
        User user = new User("username", "password", "f1", "l1", 100);
        SessionContext.setLoggedInUser(user);

        SessionContext.clearLoggedInUser();
        User loggedInUser = SessionContext.getLoggedInUser();
        assertThat(loggedInUser).isNull();
    }
}
