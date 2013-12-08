// CHECKSTYLE:OFF Test class
package org.fuin.srcgen4j.core.emf;

import java.util.Iterator;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.fuin.srcgen4j.commons.ArtifactFactory;
import org.fuin.srcgen4j.commons.ArtifactFactoryConfig;
import org.fuin.srcgen4j.commons.GenerateException;
import org.fuin.srcgen4j.commons.GeneratedArtifact;
import org.fuin.xsample.xSampleDsl.Greeting;

public class HelloPropertiesTstGen implements ArtifactFactory<ResourceSet> {

    private String artifact;

    private boolean incremental = true;

    @Override
    public final Class<? extends ResourceSet> getModelType() {
        return ResourceSet.class;
    }

    @Override
    public final void init(final ArtifactFactoryConfig config) {
        this.artifact = config.getArtifact();
        this.incremental = config.isIncremental();
    }

    @Override
    public final boolean isIncremental() {
        return incremental;
    }

    @Override
    public final GeneratedArtifact create(final ResourceSet resourceSet) throws GenerateException {

        final StringBuffer sb = new StringBuffer();

        final Iterator<Notifier> it = resourceSet.getAllContents();
        while (it.hasNext()) {
            final Notifier notifier = it.next();
            if (notifier instanceof Greeting) {
                final Greeting greeting = (Greeting) notifier;
                sb.append("a.b.c.Hello" + greeting.getName() + "=" + greeting.getName() + "\n");
            }
        }

        return new GeneratedArtifact(artifact, "hello.properties", sb.toString().getBytes());

    }

}
// CHECKSTYLE:ON
