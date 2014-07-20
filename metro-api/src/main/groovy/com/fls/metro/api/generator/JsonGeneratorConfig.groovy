package com.fls.metro.api.generator

import com.sun.jersey.api.wadl.config.WadlGeneratorConfig
import com.sun.jersey.api.wadl.config.WadlGeneratorDescription
import com.sun.jersey.wadl.generators.json.WadlGeneratorJSONGrammarGenerator

/**
 * User: NFadin
 * Date: 21.04.14
 * Time: 18:22
 */
class JsonGeneratorConfig extends WadlGeneratorConfig {
    @Override
    List<WadlGeneratorDescription> configure() {
        return generator(WadlGeneratorJSONGrammarGenerator.class).descriptions()
    }
}
