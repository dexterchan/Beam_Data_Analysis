package io.beam.exp.boilerplate;

import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.coders.StringUtf8Coder;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.Create;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.transforms.SerializableFunction;
import org.apache.beam.sdk.values.KV;
import org.apache.beam.sdk.values.PCollection;
import org.joda.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class Main {
    static final Logger log = LoggerFactory.getLogger(Main.class);
    public static void  main(String [] args){

        String[] WORDS_ARRAY = new String[] {
                "hi there", "hi", "hi sue bob",
                "hi sue", "", "bob hi"};

        log.info("Running pipeline...");
        PipelineOptions options = PipelineOptionsFactory.create();
        Pipeline p = Pipeline.create(options);

        PCollection<String> input = p.apply(Create.of(Arrays.asList(WORDS_ARRAY))).setCoder(StringUtf8Coder.of());
        PCollection<String> output =input.apply( ParDo.of( SingleProcessWords));

        output.apply(ParDo.of(new DoFn<String,Void>(){
            @ProcessElement
            public void processElement(@Element String c,OutputReceiver<Void>out) {
                System.out.println(c);
            }
        }));
        p.run().waitUntilFinish();
    }



    static DoFn<String, String> SingleProcessWords = new DoFn<String, String>() {
        String TOKENIZER_PATTERN = "[^\\p{L}]+";
        @ProcessElement
        public void processElement(ProcessContext c) {
            for (String word : c.element().split(TOKENIZER_PATTERN)) {
                if (!word.isEmpty()) {
                    c.output(word);
                }
            }
        }
    };

}
