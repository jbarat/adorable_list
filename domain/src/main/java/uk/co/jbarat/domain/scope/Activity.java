package uk.co.jbarat.domain.scope;

import java.lang.annotation.Retention;

import javax.inject.Scope;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Interface to create a custom scope for dagger.
 */
@Scope
@Retention(RUNTIME)
public @interface Activity {
}
