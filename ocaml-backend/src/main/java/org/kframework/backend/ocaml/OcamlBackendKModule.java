// Copyright (c) 2015 K Team. All Rights Reserved.
package org.kframework.backend.ocaml;

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.MapBinder;
import com.google.inject.name.Names;
import org.kframework.Rewriter;
import org.kframework.kompile.CompiledDefinition;
import org.kframework.main.AbstractKModule;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Created by dwightguth on 5/27/15.
 */
public class OcamlBackendKModule extends AbstractKModule {

    private int OCAML_CHECKPOINT_INTERVAL = 1000000;

    @Override
    public List<Module> getKompileModules() {
        return Collections.singletonList(new AbstractModule() {
            @Override
            protected void configure() {

                MapBinder<String, Consumer<CompiledDefinition>> mapBinder = MapBinder.newMapBinder(
                        binder(), TypeLiteral.get(String.class), new TypeLiteral<Consumer<CompiledDefinition>>() {});
                mapBinder.addBinding("ocaml").to(OcamlBackend.class);
            }
        });
    }

    @Override
    public List<Module> getDefinitionSpecificKRunModules() {
        return Collections.singletonList(new AbstractModule() {
            @Override
            protected void configure() {

                MapBinder<String, Function<org.kframework.definition.Module, Rewriter>> rewriterBinder = MapBinder.newMapBinder(
                        binder(), TypeLiteral.get(String.class), new TypeLiteral<Function<org.kframework.definition.Module, Rewriter>>() {
                        });

                MapBinder<String, Integer> checkpointIntervalMap = MapBinder.newMapBinder(
                        binder(), String.class, Integer.class, Names.named("checkpointIntervalMap"));

                checkpointIntervalMap.addBinding("ocaml").toInstance(new Integer(OCAML_CHECKPOINT_INTERVAL));
                rewriterBinder.addBinding("ocaml").to(OcamlRewriter.class);
            }
        });
    }
}
