package org.fungover.jee2025;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

import java.util.HashSet;
import java.util.Set;

@ApplicationPath("api")
public class BookApplication extends Application {
  // Needed to enable Jakarta REST and specify path.
}
