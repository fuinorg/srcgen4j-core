// CHECKSTYLE:OFF Test class
package org.fuin.srcgen4j.core.emf;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.fuin.srcgen4j.commons.ArtifactFactory;
import org.fuin.srcgen4j.commons.GenerateException;
import org.fuin.srcgen4j.commons.GeneratedArtifact;
import org.fuin.utils4j.Utils4J;
import org.fuin.xsample.xSampleDsl.Greeting;

public final class ManualHelloTstGen implements ArtifactFactory<Greeting> {

    private String artifact;

    @Override
    public final Class<? extends Greeting> getModelType() {
        return Greeting.class;
    }

    @Override
    public final void init(final String artifact) {
        this.artifact = artifact;
    }

    @Override
    public final GeneratedArtifact create(final Greeting greeting) throws GenerateException {
        try {
            final String src = FileUtils.readFileToString(new File(
                    "src/test/resources/Hello.template"));
            final Map<Object, Object> vars = new HashMap<Object, Object>();
            vars.put("name", greeting.getName());
            return new GeneratedArtifact(artifact, "a/b/c/Hello" + greeting.getName() + ".java",
                    Utils4J.replaceVars(src, vars));
        } catch (final IOException ex) {
            throw new RuntimeException(ex);
        }
    }

}
// CHECKSTYLE:ON
