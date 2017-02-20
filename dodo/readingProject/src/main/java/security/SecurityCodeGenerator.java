package security;

import exception.security.GenerationException;

/**
 * Created by fan on 7/2/2016.
 */
public interface SecurityCodeGenerator {
    String generate(Object object) throws GenerationException;
}
