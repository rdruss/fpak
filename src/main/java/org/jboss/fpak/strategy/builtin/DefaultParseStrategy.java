package org.jboss.fpak.strategy.builtin;

import org.jboss.fpak.GenerationContext;
import org.jboss.fpak.model.Definition;
import org.jboss.fpak.parser.FPakParser;
import org.jboss.fpak.parser.sub.FHelpParser;
import org.jboss.fpak.parser.sub.FInitParser;
import org.jboss.fpak.parser.sub.FileTemplateParser;
import org.jboss.fpak.strategy.ParseStrategy;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Mike Brock .
 */
public class DefaultParseStrategy implements ParseStrategy {
    public Definition doStrategy(GenerationContext ctx) {
        ctx.setParser("init", new FInitParser());
        ctx.setParser("help", new FHelpParser());
        ctx.setParser(FPakParser.FILE_TEMPLATE_PARSER, new FileTemplateParser());


        FPakParser fPakParser = new FPakParser(ctx.getParsers());
        Definition definition = new Definition();

        for (File template : ctx.getTemplates()) {
            InputStream stream = null;
            try {
                stream = new FileInputStream(template);

                fPakParser.parse(stream, definition);

            } catch (IOException e) {
                throw new RuntimeException("unknown exception: " + e.getMessage(), e);
            } finally {
                try {
                    if (stream != null) stream.close();
                } catch (Exception e) {
                    throw new RuntimeException("failed to close stream", e);
                }
            }
        }

        return definition;
    }
}
