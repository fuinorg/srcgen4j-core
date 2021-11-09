// CHECKSTYLE:OFF Test class
package org.fuin.srcgen4j.core.emf;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotNull;

import org.apache.commons.io.FileUtils;
import org.fuin.srcgen4j.commons.ArtifactFactory;
import org.fuin.srcgen4j.commons.ArtifactFactoryConfig;
import org.fuin.srcgen4j.commons.GenerateException;
import org.fuin.srcgen4j.commons.GeneratedArtifact;
import org.fuin.utils4j.Utils4J;
import org.fuin.xsample.xSampleDsl.Greeting;

public final class AbstractHelloTstGen implements ArtifactFactory<Greeting> {

    private String artifact;

    private boolean incremental = true;

    private Map<String, String> varMap;

    @Override
    public final Class<? extends Greeting> getModelType() {
        return Greeting.class;
    }

    @Override
    public final void init(final ArtifactFactoryConfig config) {
        this.artifact = config.getArtifact();
        this.incremental = config.isIncremental();
        this.varMap = config.getVarMap();
    }

    @Override
    public final boolean isIncremental() {
        return incremental;
    }

    @Override
    public final List<GeneratedArtifact> create(@NotNull final Greeting greeting, @NotNull final Map<String, Object> context,
            final boolean preparationRun) throws GenerateException {
        try {
            final String src = FileUtils.readFileToString(new File("src/test/resources/AbstractHello.template"), StandardCharsets.UTF_8);
            final Map<String, String> vars = new HashMap<>();
            vars.put("name", greeting.getName());
            final String pkg = varMap.get("package");
            final String path = pkg.replace('.', '/');
            vars.put("package", pkg);
            return Collections.singletonList(new GeneratedArtifact(artifact, path + "/AbstractHello" + greeting.getName() + ".java",
                    Utils4J.replaceVars(src, vars).getBytes()));
        } catch (final IOException ex) {
            throw new RuntimeException(ex);
        }
    }

}
// CHECKSTYLE:ON
