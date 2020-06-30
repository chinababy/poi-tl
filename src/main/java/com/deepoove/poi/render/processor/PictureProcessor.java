/*
 * Copyright 2014-2020 Sayi
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.deepoove.poi.render.processor;

import org.apache.commons.lang3.ClassUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.exception.RenderException;
import com.deepoove.poi.policy.DocxRenderPolicy;
import com.deepoove.poi.policy.RenderPolicy;
import com.deepoove.poi.render.compute.RenderDataCompute;
import com.deepoove.poi.resolver.Resolver;
import com.deepoove.poi.template.ElementTemplate;
import com.deepoove.poi.template.PictureTemplate;

public class PictureProcessor extends DefaultTemplateProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(PictureProcessor.class);

    public PictureProcessor(XWPFTemplate template, Resolver resolver, RenderDataCompute renderDataCompute) {
        super(template, resolver, renderDataCompute);
    }

    @Override
    public void visit(PictureTemplate pictureTemplate) {
        RenderPolicy policy = pictureTemplate.findPolicy(template.getConfig());
        if (null == policy) {
            throw new RenderException(
                    "Cannot find render policy: [" + ((ElementTemplate) pictureTemplate).getTagName() + "]");
        }
        LOGGER.info("Start render TemplateName:{}, Sign:{}, policy:{}", pictureTemplate.getTagName(),
                pictureTemplate.getSign(), ClassUtils.getShortClassName(policy.getClass()));
        policy.render(((ElementTemplate) pictureTemplate), renderDataCompute.compute(pictureTemplate.getTagName()),
                template);

    }

}
